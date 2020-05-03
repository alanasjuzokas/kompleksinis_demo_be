package bakalauras.demo.entities;

import bakalauras.demo.config.StringListConverter;
import bakalauras.demo.entities.domain.RequestStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "requests")
public class Request {

    public Request() {
    }

    public Request(RequestStatus status, String name, List<String> choices, String requesterId) {
        this.status = status;
        this.name = name;
        this.choices = choices;
        this.requesterId = requesterId;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column
    @Enumerated(EnumType.STRING)
    RequestStatus status;

    @Column
    String name;

    @Column
    @Convert(converter = StringListConverter.class)
    List<String> choices;

    @Column
    String requesterId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }
}
