package bakalauras.demo.web.domain;

public class PollRequest {
    public String pollId;

    public PollRequest(String pollId) {
        this.pollId = pollId;
    }

    public String getPollId() {
        return pollId;
    }
}
