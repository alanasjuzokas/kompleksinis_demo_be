package bakalauras.demo.web.domain;

public class LoginInitRequest {
    public String idCode;

    public LoginInitRequest(){}

    public LoginInitRequest(String idCode) {
        this.idCode = idCode;
    }

    public String getIdCode() {
        return idCode;
    }
}
