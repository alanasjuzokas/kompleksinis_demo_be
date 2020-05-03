package bakalauras.demo.entities.domain;

import javax.validation.constraints.NotNull;

public class CreateRequester {

    @NotNull
    public String name;

    @NotNull
    public String surname;

    @NotNull
    public String phone;

    @NotNull
    public String address;

    @NotNull
    public String personCode;

    @NotNull
    public String email;

}
