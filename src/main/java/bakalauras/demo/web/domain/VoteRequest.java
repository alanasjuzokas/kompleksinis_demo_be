package bakalauras.demo.web.domain;

public class VoteRequest {
    String pollId;
    boolean votedYes;

    public String getPollId() {
        return pollId;
    }

    public boolean isVotedYes() {
        return votedYes;
    }


}
