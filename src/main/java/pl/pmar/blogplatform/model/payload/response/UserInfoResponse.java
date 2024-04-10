package pl.pmar.blogplatform.model.payload.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserInfoResponse {

    private Integer id;

    private String username;

    private String email;

    @Setter(AccessLevel.NONE)
    private List<String> roles;


    public UserInfoResponse(
            Integer id,
            String username,
            String email,
            List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
