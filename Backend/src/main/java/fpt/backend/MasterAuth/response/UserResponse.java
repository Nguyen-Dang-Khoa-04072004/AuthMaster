package fpt.backend.MasterAuth.response;

import fpt.backend.MasterAuth.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private int code;
    private String message;
    private User user;
}
