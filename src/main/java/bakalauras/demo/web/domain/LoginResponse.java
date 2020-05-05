package bakalauras.demo.web.domain;

public class LoginResponse {
    State state;
    String authToken;
    Subject subject;

    public LoginResponse(State state, String authToken, Subject subject) {
        this.state = state;
        this.authToken = authToken;
        this.subject = subject;
    }

    public State getState() {
        return state;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Subject getSubject() {
        return subject;
    }
}
