package bakalauras.demo.entities;

import bakalauras.demo.entities.domain.CreateRequester;
import bakalauras.demo.entities.domain.Type;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

    public Users() {
    }

    public Users(CreateRequester createRequester) {
        this.name = createRequester.name;
        this.type = Type.REQUESTER;
        this.surname = createRequester.surname;
        this.phone = createRequester.phone;
        this.address = createRequester.address;
        this.personCode = createRequester.personCode;
        this.email = createRequester.email;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column
    @Enumerated(EnumType.STRING)
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
