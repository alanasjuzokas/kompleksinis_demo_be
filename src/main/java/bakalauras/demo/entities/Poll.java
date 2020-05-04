package bakalauras.demo.entities;

import bakalauras.demo.config.StringListConverter;
import bakalauras.demo.entities.domain.PollStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "polls")
public class Poll {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column
    PollStatus status;

    @Column
    String name;

    @Column
    @Convert(converter = StringListConverter.class)
    List<String> choices;

    @Column
    String requesterId;

    public Poll() {}

    public Poll(Request request) {
        this.status = PollStatus.NOT_STARTED;
        this.name = request.getName();
        this.choices = request.getChoices();
        this.requesterId = request.getRequesterId();
    }

    public String getId() {
        return id;
    }

    public PollStatus getStatus() {
        return status;
    }

    public void setStatus(PollStatus status) {
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
