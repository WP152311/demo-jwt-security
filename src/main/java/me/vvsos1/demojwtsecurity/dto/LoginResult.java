package me.vvsos1.demojwtsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.vvsos1.demojwtsecurity.vo.UserVo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult  {
    private String id;

    private String name;

    private String authority = "USER";

    private String authToken;

    public static LoginResult from(UserVo user, String authToken) {
        return new LoginResult(user.getId(),user.getName(),user.getAuthority(),authToken);
    }
}
