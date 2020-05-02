package bakalauras.demo.entities;

import bakalauras.demo.entities.domain.RequestStatus;
import bakalauras.demo.entities.domain.VotingStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Votings {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column
    VotingStatus status;

    @Column
    String name;

    @Column
    String choices;

    @Column
    String requesterId;

}
