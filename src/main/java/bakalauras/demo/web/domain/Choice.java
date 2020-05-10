package bakalauras.demo.web.domain;

import bakalauras.demo.entities.Poll;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "choices")
public class Choice {

    public Choice() {}

    public Choice(String body, Poll poll) {
        this.body = body;
        this.poll = poll;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column
    String body;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "poll_id", nullable=false)
    Poll poll;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPollId() {
        return poll.getId();
    }
}
