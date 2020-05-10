package bakalauras.demo.web.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateRequest {
    @NotNull
    public String name;

    @NotNull
    public List<String> choices;

    public String getName() {
        return name;
    }

    public List<String> getChoices() {
        return choices;
    }

}
