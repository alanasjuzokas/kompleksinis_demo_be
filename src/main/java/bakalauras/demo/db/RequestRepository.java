package bakalauras.demo.db;

import bakalauras.demo.entities.Request;
import bakalauras.demo.entities.domain.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, String> {
    List<Request> findAllByRequesterId(String requestedId);
    List<Request> findAllByStatus(RequestStatus status);
}
