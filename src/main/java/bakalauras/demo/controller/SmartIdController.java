package bakalauras.demo.controller;

import bakalauras.demo.db.UserRepository;
import bakalauras.demo.entities.Users;
import bakalauras.demo.entities.domain.Type;
import bakalauras.demo.web.domain.AuthenticationResponse;
import bakalauras.demo.web.domain.LoginInitRequest;
import bakalauras.demo.web.domain.LoginResponse;
import bakalauras.demo.web.domain.SessionResponse;
import bakalauras.demo.web.domain.SmartIdInitReq;
import bakalauras.demo.web.domain.SmartIdLoginResponse;
import bakalauras.demo.web.domain.Subject;
import ee.sk.smartid.AuthenticationHash;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static bakalauras.demo.config.JwtConfig.SIGN_KEY;

@RestController
@RequestMapping("/v1")
public class SmartIdController {

    private final UserRepository userRepository;

    @Autowired
    public SmartIdController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/login")
    public SmartIdLoginResponse initSmartId(@RequestBody LoginInitRequest request) {

        AuthenticationHash authenticationHash = AuthenticationHash.generateRandomHash();

        String verificationCode = authenticationHash.calculateVerificationCode();

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://sid.demo.sk.ee/smart-id-rp/v1/authentication/etsi/PNOLT-" + request.getIdCode();
        SmartIdInitReq req = new SmartIdInitReq(
                "00000000-0000-0000-0000-000000000000",
                "DEMO",
                authenticationHash.getHashInBase64(),
                "SHA512"
        );

        SessionResponse sessionId = restTemplate.postForObject(url, req, SessionResponse.class);

        return new SmartIdLoginResponse(sessionId.getSessionID(), verificationCode);
    }

    @GetMapping(path = "/login/{sessionId}")
    public LoginResponse smartIdLogin(@PathVariable String sessionId) throws CertificateException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://sid.demo.sk.ee/smart-id-rp/v1/session/" + sessionId;
        AuthenticationResponse authenticationResponse = restTemplate.getForObject(url, AuthenticationResponse.class);
        Subject subject = getSignPerson(authenticationResponse);
        Type userType = Type.VOTER;

        Optional<Users> user = userRepository.findByPersonCode(subject.getSerialNumber());

        if (user.isPresent()) {
            userType = user.get().getType();
        }

        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));

        SecretKey keys = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SIGN_KEY));
        String token = Jwts
                .builder()
                .setSubject("Demo JWT")
                .setExpiration(tomorrow)
                .claim("personCode", subject.getSerialNumber())
                .claim("role", userType)
                .signWith(keys)
                .compact();

        return new LoginResponse(authenticationResponse.getState(), token, subject);
    }

    private Subject getSignPerson(AuthenticationResponse authenticationResponse) throws CertificateException {
        byte encodedCert[] = Base64.getDecoder().decode(authenticationResponse.getCert().getValue());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedCert);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(inputStream);
        String[] subject = cert.getSubjectDN().getName().split(",");
        String name = "";
        String surname = "";
        String personCode = "";
        for (String word : subject) {
            System.out.println(word);
            if (word.contains("GIVENNAME")) {
                name = word.split("=")[1];
            }
            if (word.contains("SERIALNUMBER")) {
                personCode = word.split("=")[1].split("-")[1];
            }
            if (word.contains("SURNAME")) {
                surname = word.split("=")[1];
            }
        }
        return new Subject( name, personCode, surname);
    }

}
