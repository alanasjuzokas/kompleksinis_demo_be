package bakalauras.demo.adminSpec;

import bakalauras.demo.controller.AdminController;
import bakalauras.demo.db.PollRepository;
import bakalauras.demo.db.RequestRepository;
import bakalauras.demo.entities.Poll;
import bakalauras.demo.entities.Request;
import bakalauras.demo.entities.domain.PollStatus;
import bakalauras.demo.entities.domain.RequestStatus;
import bakalauras.demo.web.domain.PollRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;


public class TempoTest {

    private final RequestRepository requestRepository = mock(RequestRepository.class);
    private final PollRepository pollRepository = mock(PollRepository.class);

    private final AdminController controller = new AdminController(requestRepository, pollRepository);
    private List<String> choices = Arrays.asList("Pirmas", "antras");
    private Poll poll = new Poll(new Request(RequestStatus.APPROVED, "Test", choices, "1" ));
    private PollRequest request = new PollRequest("1");

    @Test
    public void stopPoll() {
        //given
        poll.setStatus(PollStatus.STARTED);
        Mockito.when(pollRepository.findById(anyString())).thenReturn(Optional.of(poll));

        //when
        ResponseEntity response = controller.stopPoll(request);

        //then
        assert response.getStatusCode() == HttpStatus.OK;
        assert poll.getStatus() == PollStatus.STOPPED;
    }

    @Test
    public void returns404ifNoPollFound() {
        //given
        Mockito.when(pollRepository.findById(anyString())).thenReturn(Optional.empty());

        //when
        ResponseEntity response = controller.stopPoll(request);

        //then
        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
    }

    @Test
    public void returnsOkDoesNotChangeTheStatusIfStopped() {
        //given
        poll.setStatus(PollStatus.STOPPED);
        Mockito.when(pollRepository.findById(anyString())).thenReturn(Optional.of(poll));

        //when
        ResponseEntity response = controller.stopPoll(request);

        //then
        assert response.getStatusCode() == HttpStatus.OK;
        assert poll.getStatus() == PollStatus.STOPPED;
    }

}
