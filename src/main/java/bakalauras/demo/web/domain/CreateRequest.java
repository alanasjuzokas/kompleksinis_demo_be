package bakalauras.demo.web.domain;

import javax.validation.constraints.NotNull;

public class CreateRequest {
    @NotNull
    public String question;

    public String getQuestion() {
        return question;
    }
}
