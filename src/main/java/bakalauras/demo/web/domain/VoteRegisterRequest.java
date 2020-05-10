package bakalauras.demo.web.domain;

public class VoteRegisterRequest {

    public String pollId;

    public String choiceId;

    public VoteRegisterRequest(String pollId, String choiceId) {
        this.pollId = pollId;
        this.choiceId = choiceId;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(String choiceId) {
        this.choiceId = choiceId;
    }
}
