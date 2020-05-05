package bakalauras.demo.web.domain;

public class LoginResponse {
    State state;
    String authToken;

    public LoginResponse(State state, String authToken) {
        this.state = state;
        this.authToken = authToken;
    }

    public State getState() {
        return state;
    }

    public String getAuthToken() {
        return authToken;
    }
}
