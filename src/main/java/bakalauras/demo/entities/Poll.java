package bakalauras.demo.entities;

import bakalauras.demo.config.StringListConverter;
import bakalauras.demo.entities.domain.PollStatus;
import bakalauras.demo.web.domain.Choice;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    @Enumerated(EnumType.STRING)
    PollStatus status;

    @Column
    String name;

    @Column
    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
    List<Choice> choices;

    @Column
    String requesterId;

    public Poll() {}

    public Poll(String name, String requesterId) {
        this.status = PollStatus.NOT_STARTED;
        this.name = name;
        this.requesterId = requesterId;
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

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }
}
