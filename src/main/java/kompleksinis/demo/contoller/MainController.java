package kompleksinis.demo.contoller;

import kompleksinis.demo.db.Repository;
import kompleksinis.demo.entities.Poll;
import kompleksinis.demo.web.domain.CreateRequest;
import kompleksinis.demo.web.domain.PollRequest;
import kompleksinis.demo.entities.Results;
import kompleksinis.demo.web.domain.VoteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/v1")
public class MainController {

    private final Repository repository;

    @Autowired
    public MainController(Repository repository) {
        this.repository = repository;
    }

    @PostMapping(path = "/create")
    public String createPoll(@RequestBody CreateRequest request) {
        Poll poll = new Poll(request.question);
        repository.save(poll);
        return poll.getId();
    }

    @PostMapping(path = "/vote")
    public ResponseEntity vote(@RequestBody VoteRequest request) {
        Poll poll = repository.findById(request.getPollId()).orElseThrow(NoSuchElementException::new);
        if (poll.isRunning()) {
            if (request.isVotedYes()) {
                poll.setYes(poll.getYes() + 1);
            } else {
                poll.setNo(poll.getNo()+ 1);
            }
            repository.save(poll);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "{pollId}/results")
    public ResponseEntity<Results> getVoteResults(@PathVariable String pollId) {
        Poll poll = repository.findById(pollId).orElseThrow(NoSuchElementException::new);
        Results response = new Results(poll.getYes(), poll.getNo());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping(path = "/polls")
    public ResponseEntity<List<Poll>> getPolls() {
        List<Poll> response = repository.findAll();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{pollId}")
    public ResponseEntity<Poll> getPoll(@PathVariable String pollId) {
        Poll response = repository.findById(pollId).orElseThrow(NoSuchElementException::new);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping(path = "/active")
    public ResponseEntity<List<Poll>> getActive() {
        List<Poll> response = repository.findAllByRunningTrue();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping(path = "/stop")
    public ResponseEntity stopPoll(@RequestBody PollRequest request) {
        Poll poll = repository.findById(request.getPollId()).orElseThrow(NoSuchElementException::new);
        if (poll.isRunning()) {
            poll.setRunning(false);
        }
        repository.save(poll);
        return new ResponseEntity(HttpStatus.OK);
    }
}
