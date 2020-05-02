package bakalauras.demo.entities;

import bakalauras.demo.entities.domain.RequestStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "requests")
public class Requests {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column
    RequestStatus status;

    @Column
    String name;

    @Column
    String choices;

    @Column
    String requesterId;

}
