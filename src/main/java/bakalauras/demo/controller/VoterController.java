package bakalauras.demo.controller;

import bakalauras.demo.db.PollRepository;
import bakalauras.demo.entities.Poll;
import bakalauras.demo.entities.domain.PollStatus;
import bakalauras.demo.web.domain.BlockResponse;
import bakalauras.demo.web.domain.BlockStatus;
import bakalauras.demo.web.domain.VoteRegisterRequest;
import bakalauras.demo.web.domain.VoteRequest;
import bakalauras.demo.web.domain.VoterRegisterRequest;
import bakalauras.demo.web.domain.VoterStatus;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;

import static bakalauras.demo.config.JwtConfig.SIGN_KEY;

@Controller
@RequestMapping("/v1")
public class VoterController {

    private final PollRepository pollRepository;
    public static final String BASE_USER_BC_URL = "https://blockchain-pno.herokuapp.com";
    public static final String BASE_VOTE_BC_URL = "https://blockchain-vote1.herokuapp.com";
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public VoterController(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @PostMapping(path = "polls/vote")
    public ResponseEntity vote(@RequestBody VoteRequest request, @RequestHeader(name = "Authorization") String token) {


        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SIGN_KEY));

        String personCode = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("personCode", String.class);

        Poll poll = pollRepository.getOne(request.pollId);

        if (poll.getStatus() != PollStatus.STARTED) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (checkIfChainIsValid(BASE_USER_BC_URL, request.pollId) == BlockStatus.NOT_VALID) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        VoterStatus voterStatus = checkIfUserVoted(personCode, request.pollId);

        if (voterStatus == VoterStatus.VOTED) {
            return new ResponseEntity(HttpStatus.OK);
        }

        if (checkIfChainIsValid(BASE_VOTE_BC_URL, request.pollId) == BlockStatus.NOT_VALID) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (registerVoteToBlock(request) == HttpStatus.BAD_REQUEST) {
            return new ResponseEntity(HttpStatus.OK);
        }

        if (registerUserToBlock(personCode, request.pollId) == HttpStatus.BAD_REQUEST) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    private BlockResponse getLatestBlock(String baseUrl, String pollId) {
        String url = baseUrl + "/chain/" + pollId +  "/block/latest";

        return restTemplate.getForObject(url, BlockResponse.class);
    }

    private BlockStatus checkIfChainIsValid(String baseUrl, String pollId) {
        String url = baseUrl + "/chain/" + pollId + "/validate";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return BlockStatus.VALID;
        }

        return BlockStatus.NOT_VALID;
    }

    private VoterStatus checkIfUserVoted(String personCode, String pollId) {

        String url = BASE_USER_BC_URL + "/chain/" + pollId + "/check/" + personCode;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return VoterStatus.VOTED;
            }

        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            return VoterStatus.HAS_NOT_YET_VOTED;
        }

        return VoterStatus.HAS_NOT_YET_VOTED;
    }

    private HttpStatus registerUserToBlock(String personCode, String pollId) {

        String url = BASE_USER_BC_URL + "/chain/" + pollId + "/block/register";

        VoterRegisterRequest request = new VoterRegisterRequest(personCode);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request), String.class);
        if (response.getStatusCode() == HttpStatus.CREATED) {
            return HttpStatus.OK;
        }

        return HttpStatus.BAD_REQUEST;
    }

    private HttpStatus registerVoteToBlock(VoteRequest request) {

        String url = BASE_VOTE_BC_URL + "/chain/" + request.pollId + "/block/register";

        VoteRegisterRequest registerRequest = new VoteRegisterRequest(request.pollId, request.choiceId);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(registerRequest), String.class);
        if (response.getStatusCode() == HttpStatus.CREATED) {
            return HttpStatus.OK;
        }

        return HttpStatus.BAD_REQUEST;
    }

}
