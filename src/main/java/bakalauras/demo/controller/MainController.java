package bakalauras.demo.controller;

import bakalauras.demo.db.PollRepository;
import bakalauras.demo.db.UserRepository;
import bakalauras.demo.entities.Poll;
import bakalauras.demo.entities.Users;
import bakalauras.demo.web.domain.CreateRequest;
import bakalauras.demo.web.domain.PollRequest;
import bakalauras.demo.web.domain.VoteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/v1")
public class MainController {

    private final PollRepository pollRepository;

    @Autowired
    public MainController(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    //TODO: move endpoints to related controllers,

    @PostMapping(path = "/create")
    public String createPoll(@RequestBody CreateRequest request) {
        Poll poll = new Poll(request.question);
        pollRepository.save(poll);
        return poll.getId();
    }

    @PostMapping(path = "/vote")
    public ResponseEntity vote(@RequestBody VoteRequest request) {
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping(path = "{pollId}/results")
    public ResponseEntity getVoteResults(@PathVariable String pollId) {
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping(path = "/polls")
    public ResponseEntity<List<Poll>> getPolls() {
        List<Poll> response = pollRepository.findAll();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{pollId}")
    public ResponseEntity<Poll> getPoll(@PathVariable String pollId) {
        Poll response = pollRepository.findById(pollId).orElseThrow(NoSuchElementException::new);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping(path = "/active")
    public ResponseEntity<List<Poll>> getActive() {
        List<Poll> response = pollRepository.findAllByRunningTrue();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping(path = "/stop")
    public ResponseEntity stopPoll(@RequestBody PollRequest request) {
        Poll poll = pollRepository.findById(request.getPollId()).orElseThrow(NoSuchElementException::new);
        if (poll.isRunning()) {
            poll.setRunning(false);
        }
        pollRepository.save(poll);
        return new ResponseEntity(HttpStatus.OK);
    }
}
