package bakalauras.demo.controller;

import bakalauras.demo.config.JwtConfig;
import bakalauras.demo.db.PollRepository;
import bakalauras.demo.db.RequestRepository;
import bakalauras.demo.entities.Poll;
import bakalauras.demo.entities.Request;
import bakalauras.demo.entities.domain.PollStatus;
import bakalauras.demo.entities.domain.RequestStatus;
import bakalauras.demo.web.domain.Choice;
import bakalauras.demo.web.domain.PollRequest;
import bakalauras.demo.web.domain.RequestActionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/v1")
public class AdminController {

    private final RequestRepository requestRepository;
    private final PollRepository pollRepository;

    @Autowired
    public AdminController(RequestRepository requestRepository, PollRepository pollRepository) {
        this.requestRepository = requestRepository;
        this.pollRepository = pollRepository;
    }

    @PostMapping(path = "/polls/stop")
    public ResponseEntity stopPoll(@RequestBody PollRequest request, @RequestHeader(name = "Authorization") String token) {

        if (!JwtConfig.isAdministrator(token)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Optional<Poll> poll = pollRepository.findById(request.getPollId());
        if (poll.isPresent()) {
            if (poll.get().getStatus() == PollStatus.STARTED) {
                poll.get().setStatus(PollStatus.STOPPED);
                pollRepository.save(poll.get());
            }
            return new ResponseEntity(new MainController(pollRepository).getVoteResults(request.pollId).getBody(), HttpStatus.OK);
        }
       return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/polls/start")
    public ResponseEntity startPoll(@RequestBody PollRequest request, @RequestHeader(name = "Authorization") String token) {

        if (!JwtConfig.isAdministrator(token)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Optional<Poll> poll = pollRepository.findById(request.getPollId());
        if (poll.isPresent()){
            if (poll.get().getStatus() == PollStatus.NOT_STARTED) {
                poll.get().setStatus(PollStatus.STARTED);
            }
            pollRepository.save(poll.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/requests/approve")
    public ResponseEntity<Poll> approveRequest(@RequestBody RequestActionRequest actionRequest,
                                               @RequestHeader(name = "Authorization") String token) {

        if (!JwtConfig.isAdministrator(token)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Optional<Request> request = requestRepository.findById(actionRequest.getRequestId());
        if (request.isPresent()) {
            request.get().setStatus(RequestStatus.APPROVED);
            requestRepository.save(request.get());
            Poll poll = new Poll(request.get().getName(), request.get().getRequesterId());

            List<String> requestChoices = request.get().getChoices();
            List<Choice> choices = new ArrayList<>();

            for (String choice : requestChoices) {
                choices.add(new Choice(choice, poll));
            }

            poll.setChoices(choices);

            pollRepository.save(poll);

            if (registerUserBlockChain(poll.getId()) == HttpStatus.BAD_REQUEST) {
                pollRepository.delete(poll);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if(registerVoterBlockChain(poll.getId()) == HttpStatus.BAD_REQUEST) {
                pollRepository.delete(poll);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(poll, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/requests/reject")
    public ResponseEntity<Request> rejectPoll(@RequestBody RequestActionRequest actionRequest,
                                              @RequestHeader(name = "Authorization") String token) {

        if (!JwtConfig.isAdministrator(token)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Optional<Request> request = requestRepository.findById(actionRequest.getRequestId());
        if (request.isPresent()) {
            request.get().setStatus(RequestStatus.REJECTED);
            requestRepository.save(request.get());
            return new ResponseEntity<>(request.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/requests")
    public ResponseEntity<List<Request>> getAllRequests(@RequestHeader(name = "Authorization") String token) {

        if (!JwtConfig.isAdministrator(token)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity(requestRepository.findAll() ,HttpStatus.OK);
    }

    @GetMapping(path = "/requests/{status}")
    public ResponseEntity<List<Request>> getRequestsByStatus(@PathVariable RequestStatus status,
                                                             @RequestHeader(name = "Authorization") String token) {
        if (!JwtConfig.isAdministrator(token)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(requestRepository.findAllByStatus(status) , HttpStatus.OK);
    }

    @PostMapping(path = "/chain/user/{pollId}")
    public ResponseEntity checkIfUserBlockChainIsValid(@PathVariable String pollId,
                                                       @RequestHeader(name = "Authorization") String token) {

        if (!JwtConfig.isAdministrator(token)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        String url = VoterController.BASE_USER_BC_URL + "/chain/" + pollId + "/validate";

        return new RestTemplate().exchange(url, HttpMethod.POST, null, String.class);

    }

    @PostMapping(path = "/chain/votes/{pollId}")
    public ResponseEntity checkIfVoterBlockChainIsValid(@PathVariable String pollId,
                                                        @RequestHeader(name = "Authorization") String token) {

        if (!JwtConfig.isAdministrator(token)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        String url = VoterController.BASE_VOTE_BC_URL + "/chain/" + pollId + "/validate";

        return new RestTemplate().exchange(url, HttpMethod.POST, null, String.class);
    }


    private HttpStatus registerUserBlockChain(String pollId) {

        String url = VoterController.BASE_USER_BC_URL + "/chain/create/" + pollId;

        ResponseEntity<String> responseEntity = new RestTemplate().exchange(url, HttpMethod.POST, null, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            return HttpStatus.CREATED;
        }
        return HttpStatus.BAD_REQUEST;
    }

    private HttpStatus registerVoterBlockChain(String pollId) {

        String url = VoterController.BASE_VOTE_BC_URL + "/chain/create/" + pollId;

        ResponseEntity<String> responseEntity = new RestTemplate().exchange(url, HttpMethod.POST, null, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            return HttpStatus.CREATED;
        }
        return HttpStatus.BAD_REQUEST;
    }

}
