package bakalauras.demo.web.domain;

public class AuthenticationResponse {

    State state;
    Cert cert;

    public AuthenticationResponse() { }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Cert getCert() {
        return cert;
    }

    public void setCert(Cert cert) {
        this.cert = cert;
    }
}
