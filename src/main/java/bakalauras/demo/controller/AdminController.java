package bakalauras.demo.controller;

import bakalauras.demo.db.PollRepository;
import bakalauras.demo.db.RequestRepository;
import bakalauras.demo.entities.Poll;
import bakalauras.demo.entities.Request;
import bakalauras.demo.entities.domain.PollStatus;
import bakalauras.demo.entities.domain.RequestStatus;
import bakalauras.demo.web.domain.PollRequest;
import bakalauras.demo.web.domain.RequestActionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.NoSuchElementException;

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

    //TODO: add endpoint for approving, getting, declining requests. Starting and stopping polls
    @PostMapping(path = "/polls/stop")
    public ResponseEntity stopPoll(@RequestBody PollRequest request) {
        Poll poll = pollRepository.findById(request.getPollId()).orElseThrow(NoSuchElementException::new);
        if (poll.getStatus() == PollStatus.STARTED) {
            poll.setStatus(PollStatus.STOPPED);
        }
        pollRepository.save(poll);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(path = "/polls/start")
    public ResponseEntity startPoll(@RequestBody PollRequest request) {
        Poll poll = pollRepository.findById(request.getPollId()).orElseThrow(NoSuchElementException::new);
        if (poll.getStatus() == PollStatus.NOT_STARTED) {
            poll.setStatus(PollStatus.STARTED);
        }
        pollRepository.save(poll);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/requests/approve")
    public ResponseEntity<Poll> approveRequest(@RequestBody RequestActionRequest actionRequest) {
        Request request = requestRepository.findById(actionRequest.getRequestId()).orElseThrow(NoSuchElementException::new);
        request.setStatus(RequestStatus.APPROVED);
        requestRepository.save(request);
        Poll poll = new Poll(request);
        pollRepository.save(poll);
        return new ResponseEntity<>(poll, HttpStatus.OK);
    }

    @PostMapping(path = "/requests/reject")
    public ResponseEntity rejectPoll(@RequestBody RequestActionRequest actionRequest) {
        Request request = requestRepository.findById(actionRequest.getRequestId()).orElseThrow(NoSuchElementException::new);
        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/requests")
    public ResponseEntity<List<Request>> getAllRequests() {
        return new ResponseEntity(requestRepository.findAll() ,HttpStatus.OK);
    }

    @GetMapping(path = "/requests/{status}")
    public ResponseEntity<List<Request>> getRequestsByStatus(@PathVariable RequestStatus status) {
        return new ResponseEntity<>(requestRepository.findAllByStatus(status) , HttpStatus.OK);
    }

}
