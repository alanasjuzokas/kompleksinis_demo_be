package bakalauras.demo.userTests;

import bakalauras.demo.config.JwtConfig;
import bakalauras.demo.controller.SmartIdController;
import bakalauras.demo.db.UserRepository;
import bakalauras.demo.web.domain.CreateRequest;
import bakalauras.demo.web.domain.LoginInitRequest;
import bakalauras.demo.web.domain.LoginResponse;
import bakalauras.demo.web.domain.SmartIdLoginResponse;
import org.junit.jupiter.api.Test;

import java.security.cert.CertificateException;
import java.util.Arrays;

import static org.mockito.Mockito.mock;


public class LoginTest {

    private final UserRepository userRepository = mock(UserRepository.class);

    private final SmartIdController controller = new SmartIdController(userRepository);

    @Test
    public void initLogin() {
        //when
        SmartIdLoginResponse response = controller.initSmartId(new LoginInitRequest("10101010005"));

        //then
        assert !response.getSessionId().isEmpty();
        assert !response.getVerificationCode().isEmpty();
    }

    @Test
    public void generatesToken() throws CertificateException {
        //given
        SmartIdLoginResponse smartIdLoginResponse = controller.initSmartId(new LoginInitRequest("10101010005"));

        //when
        LoginResponse response = controller.smartIdLogin(smartIdLoginResponse.getSessionId());

        //then
        assert !response.getAuthToken().isEmpty();
    }

    @Test
    public void generateTokenContainsScopeForRequester() throws CertificateException {
        //given
        SmartIdLoginResponse smartIdLoginResponse = controller.initSmartId(new LoginInitRequest("10101010005"));

        //when
        LoginResponse response = controller.smartIdLogin(smartIdLoginResponse.getSessionId());

        //then
        String token = response.getAuthToken();

        assert !JwtConfig.getRole(token).isEmpty();
    }

}
