package bakalauras.demo.controller;

import bakalauras.demo.web.domain.AuthenticationResponse;
import bakalauras.demo.web.domain.LoginInitRequest;
import bakalauras.demo.web.domain.LoginResponse;
import bakalauras.demo.web.domain.SessionResponse;
import bakalauras.demo.web.domain.SmartIdInitReq;
import bakalauras.demo.web.domain.SmartIdLoginResponse;
import bakalauras.demo.web.domain.Subject;
import ee.sk.smartid.AuthenticationHash;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

@RestController
@RequestMapping("/v1")
public class SmartIdController {

   @PostMapping(path = "/login")
   SmartIdLoginResponse initSmartId(@RequestBody LoginInitRequest request) {

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
   LoginResponse smartIdLogin(@PathVariable String sessionId) throws CertificateException {
      RestTemplate restTemplate = new RestTemplate();
      String url = "https://sid.demo.sk.ee/smart-id-rp/v1/session/" + sessionId;
      AuthenticationResponse authenticationResponse = restTemplate.getForObject(url, AuthenticationResponse.class);
      Subject subject = getSignPerson(authenticationResponse);

      return new LoginResponse(authenticationResponse.getState(), "");
   }

   private Subject getSignPerson(AuthenticationResponse authenticationResponse) throws CertificateException {
      byte encodedCert[] = Base64.getDecoder().decode(authenticationResponse.getCert().getValue());
      ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedCert);
      CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
      X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(inputStream);
      String[] subject = cert.getSubjectDN().getName().split(",");
      return new Subject(
              subject[5].split("=")[1],
              subject[3].split("-")[1],
              subject[6].split("=")[1]);
   }

}
