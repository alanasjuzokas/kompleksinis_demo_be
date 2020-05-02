package bakalauras.demo.db;

import bakalauras.demo.entities.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollRepository extends JpaRepository<Poll, String> {
    List<Poll> findAllByRunningTrue();
}
