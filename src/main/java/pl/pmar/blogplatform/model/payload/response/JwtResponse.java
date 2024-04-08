package pl.pmar.blogplatform.model.payload.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {

    private String accessToken;

    private String tokenType = "Bearer";

    private String refreshToken;

    private Integer id;

    private String username;

    private String email;

    @Setter(AccessLevel.NONE)
    private List<String> roles;


    public JwtResponse(
            String accessToken,
            String refreshToken,
            Integer id,
            String username,
            String email,
            List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
