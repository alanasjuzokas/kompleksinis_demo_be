package bakalauras.demo.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "vote")
public class Poll {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column
    String question;

    @Column
    int yes = 0;

    @Column
    int no = 0;

    @Column
    boolean running;

    public Poll() {
    }

    public Poll(String question) {
        this.question = question;
        this.running = true;
    }

    public String getId() {
        return id;
    }

    public int getYes() {
        return yes;
    }

    public int getNo() {
        return no;
    }

    public void setYes(int yes) {
        this.yes = yes;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public boolean isRunning() {
        return running;
    }

    public String getQuestion() {
        return question;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
