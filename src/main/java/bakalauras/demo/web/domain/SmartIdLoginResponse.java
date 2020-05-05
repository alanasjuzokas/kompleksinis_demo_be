package bakalauras.demo.web.domain;

public class SmartIdLoginResponse {
    String sessionId;
    String verificationCode;

    public SmartIdLoginResponse(String sessionId, String verificationCode) {
        this.sessionId = sessionId;
        this.verificationCode = verificationCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getVerificationCode() {
        return verificationCode;
    }
}
