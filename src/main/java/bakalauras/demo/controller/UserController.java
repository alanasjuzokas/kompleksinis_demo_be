package bakalauras.demo.controller;

import bakalauras.demo.config.JwtConfig;
import bakalauras.demo.db.PollRepository;
import bakalauras.demo.db.RequestRepository;
import bakalauras.demo.db.UserRepository;
import bakalauras.demo.entities.Poll;
import bakalauras.demo.entities.Request;
import bakalauras.demo.entities.Users;
import bakalauras.demo.entities.domain.CreateRequester;
import bakalauras.demo.entities.domain.RequestStatus;
import bakalauras.demo.entities.domain.Type;
import bakalauras.demo.web.domain.CreateRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Optional;

import static bakalauras.demo.config.JwtConfig.SIGN_KEY;

@RequestMapping("/v1")
@Controller
public class UserController {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final PollRepository pollRepository;

    @Autowired
    public UserController(RequestRepository requestRepository,
                          UserRepository userRepository,
                          PollRepository pollRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.pollRepository = pollRepository;
    }

    @PostMapping(path = "requests/create")
    public ResponseEntity<Request> createRequest(@RequestBody CreateRequest createRequest,
                                                 @RequestHeader(name = "Authorization") String token) {

        if (!JwtConfig.isRequester(token)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SIGN_KEY));

        String personCode = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("personCode", String.class);

        String role = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("role", String.class);

        String requesterId = "";

        if (role.equals(Type.REQUESTER.toString())) {
            Optional<Users> user = userRepository.findByPersonCode(personCode);
            if (user.isPresent()) {
                requesterId = user.get().getId();
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Request request = new Request(
                RequestStatus.PENDING,
                createRequest.name,
                createRequest.choices,
                requesterId
        );

        requestRepository.save(request);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PostMapping(path = "users/requester")
    public ResponseEntity<Users> createRequester(@RequestBody CreateRequester createRequester) {
        Users user = new Users(createRequester);
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "users/{personCode}")
    public ResponseEntity<Users> getUserByPersonCode(@PathVariable String personCode,
                                                     @RequestHeader(name = "Authorization") String token) {
        if (!JwtConfig.isAdministrator(token)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Optional<Users> users = userRepository.findByPersonCode(personCode);
        if (users.isPresent()) {
            return new ResponseEntity<>(users.get(), HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "users/{requesterId}/requests")
    public ResponseEntity<List<Request>> getRequesterRequests(@PathVariable String requesterId,
                                                              @RequestHeader(name = "Authorization") String token) {

        if (!JwtConfig.isAdministrator(token) && !JwtConfig.isRequester(token)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(requestRepository.findAllByRequesterId(requesterId), HttpStatus.FOUND);
    }

    @GetMapping(path = "users/{requesterId}/polls")
    public ResponseEntity<List<Poll>> getRequesterPolls(@PathVariable String requesterId,
                                                        @RequestHeader(name = "Authorization") String token) {

        if (!JwtConfig.isAdministrator(token) && !JwtConfig.isRequester(token)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(pollRepository.findAllByRequesterId(requesterId), HttpStatus.FOUND);
    }

}
