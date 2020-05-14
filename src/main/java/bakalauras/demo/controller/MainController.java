package bakalauras.demo.controller;

import bakalauras.demo.db.PollRepository;
import bakalauras.demo.entities.Poll;
import bakalauras.demo.entities.domain.PollStatus;
import bakalauras.demo.web.domain.BlockResponse;
import bakalauras.demo.web.domain.Choice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


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

        Poll poll = pollRepository.getOne(pollId);

        if (poll.getStatus() == PollStatus.STOPPED) {
            String url = VoterController.BASE_VOTE_BC_URL + "/chain/" + pollId + "/results";

            ResponseEntity<BlockResponse[]> response = new RestTemplate().getForEntity(url, BlockResponse[].class);

            List<BlockResponse> blocks = Arrays.asList(response.getBody());

            List<Choice> choices = poll.getChoices();

            Map<String, Integer> results = new HashMap<>();

            int occur = 0;

            for (Choice numberOfChoice : choices) {
                for (BlockResponse block : blocks) {
                    if (block.choiceId.equals(numberOfChoice.getId())) {
                        occur++;
                    }
                }
                results.put(numberOfChoice.getBody(), occur);
                occur = 0;
            }

            return new ResponseEntity(results, HttpStatus.OK);
        }

       return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "polls/{pollId}/raw")
    public ResponseEntity getRawResults(@PathVariable String pollId) {
        Optional<Poll> poll = pollRepository.findById(pollId);
        if (poll.isPresent() && poll.get().getStatus() == PollStatus.STOPPED) {
            String url = VoterController.BASE_VOTE_BC_URL + "/chain/" + pollId + "/results";

            ResponseEntity<BlockResponse[]> response = new RestTemplate().getForEntity(url, BlockResponse[].class);
            return new ResponseEntity(response.getBody(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/polls")
    public ResponseEntity<List<Poll>> getPolls() {
        List<Poll> response = pollRepository.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/polls/{pollId}")
    public ResponseEntity<Poll> getPoll(@PathVariable String pollId) {
        Optional<Poll> response = pollRepository.findById(pollId);
        if (response.isPresent()) {
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/polls/status/{status}")
    public ResponseEntity<List<Poll>> getPollsByStatus(@PathVariable PollStatus status) {
        return new ResponseEntity<>(pollRepository.findAllByStatus(status), HttpStatus.OK);
    }

}
