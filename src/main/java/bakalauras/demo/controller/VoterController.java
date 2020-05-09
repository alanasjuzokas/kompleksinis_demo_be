package bakalauras.demo.controller;

import bakalauras.demo.db.PollRepository;
import bakalauras.demo.web.domain.BlockResponse;
import bakalauras.demo.web.domain.VoteRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;

import static bakalauras.demo.config.JwtConfig.SIGN_KEY;

@Controller
@RequestMapping("/v1")
public class VoterController {

    private final PollRepository pollRepository;
    private static final String BASE_USER_BC_URL = "https://blockchain-pno.herokuapp.com";
    private static final String BASE_VOTE_BC_URL = "https://blockchain-vote1.herokuapp.com";
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

        BlockResponse latestUserBlock = getLatestUserBlock();
        String isBlockValid  = checkIfBlockIsValid(latestUserBlock);

        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }

    private BlockResponse getLatestUserBlock() {
        String url = BASE_USER_BC_URL + "/block/latest";

        return restTemplate.getForObject(url, BlockResponse.class);
    }

    private String checkIfBlockIsValid(BlockResponse block) {
        String url = BASE_USER_BC_URL + "/block/validate";

        return restTemplate.postForObject(url, block, String.class);
    }

    private String checkIfUserVoted() { return ""; }

    private String registerUserToBlock() { return ""; }

    private String postUserVoteToBlockChain() { return ""; }

}
