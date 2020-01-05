package kompleksinis.demo.db;

import kompleksinis.demo.entities.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Repository extends JpaRepository<Poll, String> {
    List<Poll> findAllByRunningTrue();
}
