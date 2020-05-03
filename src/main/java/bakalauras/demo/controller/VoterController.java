package bakalauras.demo.controller;

import bakalauras.demo.db.PollRepository;
import bakalauras.demo.web.domain.VoteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
public class VoterController {

    private final PollRepository pollRepository;

    @Autowired
    public VoterController(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @PostMapping(path = "polls/vote")
    public ResponseEntity vote(@RequestBody VoteRequest request) {
        //TODO: add blockchain voting here
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }

}
