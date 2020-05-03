package bakalauras.demo.db;

import bakalauras.demo.entities.Poll;
import bakalauras.demo.entities.domain.PollStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollRepository extends JpaRepository<Poll, String> {
    List<Poll> findAllByStatus(PollStatus status);
    List<Poll> findAllByRequesterId(String requesterId);
}
