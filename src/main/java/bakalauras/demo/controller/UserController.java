package bakalauras.demo.controller;

import bakalauras.demo.db.PollRepository;
import bakalauras.demo.db.RequestRepository;
import bakalauras.demo.db.UserRepository;
import bakalauras.demo.entities.Poll;
import bakalauras.demo.entities.Request;
import bakalauras.demo.entities.Users;
import bakalauras.demo.entities.domain.CreateRequester;
import bakalauras.demo.entities.domain.RequestStatus;
import bakalauras.demo.web.domain.CreateRequest;
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

    //TODO: add endpoint for requesting a poll, getting a list of your requests, get poll results endpoint from blockchain
    @PostMapping(path = "requests/create")
    public ResponseEntity<Request> createRequest(@RequestBody CreateRequest createRequest) {
        Request request = new Request(
                RequestStatus.PENDING,
                createRequest.name,
                createRequest.choices,
                createRequest.requesterId
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
    public ResponseEntity<Users> getUserByPersonCode(@PathVariable String personCode) {
        Users users = userRepository.findByPersonCode(personCode);
        if (users != null) {
            return new ResponseEntity<>(users, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "users/{requesterId}/requests")
    public ResponseEntity<List<Request>> getRequesterRequests(@PathVariable String requesterId) {
        return new ResponseEntity<>(requestRepository.findAllByRequesterId(requesterId), HttpStatus.FOUND);
    }

    @GetMapping(path = "users/{requesterId}/polls")
    public ResponseEntity<List<Poll>> getRequesterPolls(@PathVariable String requesterId) {
        return new ResponseEntity<>(pollRepository.findAllByRequesterId(requesterId), HttpStatus.FOUND);
    }

}