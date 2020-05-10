package bakalauras.demo.web.domain;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateRequest {

    public CreateRequest() {}

    public CreateRequest(@NotNull String name, @NotNull List<String> choices) {
        this.name = name;
        this.choices = choices;
    }

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
