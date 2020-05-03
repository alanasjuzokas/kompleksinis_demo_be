package bakalauras.demo.controller;

import bakalauras.demo.db.PollRepository;
import bakalauras.demo.entities.Poll;
import bakalauras.demo.entities.domain.PollStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.NoSuchElementException;


@Controller
@RequestMapping("/v1")
public class MainController {

    private final PollRepository pollRepository;

    @Autowired
    public MainController(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @GetMapping(path = "polls/{pollId}/results")
    public ResponseEntity getVoteResults(@PathVariable String pollId) {
        //TODO: results from blockchain implemented
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping(path = "/polls")
    public ResponseEntity<List<Poll>> getPolls() {
        List<Poll> response = pollRepository.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/polls/{pollId}")
    public ResponseEntity<Poll> getPoll(@PathVariable String pollId) {
        Poll response = pollRepository.findById(pollId).orElseThrow(NoSuchElementException::new);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/polls/status/{status}")
    public ResponseEntity<List<Poll>> getPollsByStatus(@PathVariable PollStatus status) {
        return new ResponseEntity<>(pollRepository.findAllByStatus(status), HttpStatus.OK);
    }

}
