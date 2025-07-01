package fpt.backend.MasterAuth.user;

import fpt.backend.MasterAuth.filter.JwtService;
import fpt.backend.MasterAuth.requests.AddUserRequest;
import fpt.backend.MasterAuth.requests.LoginRequest;
import fpt.backend.MasterAuth.requests.UpdateUserRequest;
import fpt.backend.MasterAuth.response.AuthResponse;
import fpt.backend.MasterAuth.response.UserResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest request){
        User user = userService.login(request.getEmail(),request.getPassword());
        return ResponseEntity.ok(
            AuthResponse.builder()
                .code(200)
                .message("Login successfully!")
                .accessToken(jwtService.generateToken(user.getUsername(),7200000))
                .refreshToken(jwtService.generateToken(user.getUsername(),604800000))
                .build()
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId){
        return ResponseEntity.ok(
            UserResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Get user %d successfully!".formatted(userId))
                .user(userService.getUser(userId))
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody AddUserRequest request){
        User user = userService.createUser(request);
        return ResponseEntity.created(URI.create("/api/v1/users/%d".formatted(user.getId()))).body(
            UserResponse.builder()
                .code(201)
                .message("Create a user successfully!")
                .user(user)
                .build()
        );
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable Long userId,@RequestBody UpdateUserRequest request){
        userService.updateUser(userId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
