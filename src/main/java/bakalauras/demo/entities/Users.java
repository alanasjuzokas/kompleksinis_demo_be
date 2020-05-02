package bakalauras.demo.entities;

import bakalauras.demo.entities.domain.Type;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

    public Users() {
    }

    public Users(String name) {
        this.name = name;
        this.type = Type.REQUESTER;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column
    Type type;

    @Column
    String name;

    @Column
    String surname;

    @Column
    String phone;

    @Column
    String address;

    @Column
    String personCode;

    @Column
    String email;
}
