package rbibank.web.app.controller;

import rbibank.web.app.dto.ChangePasswordDto;
import rbibank.web.app.dto.UserDto;
import rbibank.web.app.entity.User;
import rbibank.web.app.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.registerUser(userDto));
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticateUser(@RequestBody UserDto userDto) {
        var authObject = userService.authenticateUser(userDto);
        var token = (String) authObject.get("token");
        System.out.println("Jwt token: " + token);
        return ResponseEntity.ok().header("Authorization", token).header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization").body(authObject.get("user"));
    }

    @PostMapping("/password")
    public ResponseEntity<Map<String, String>> changePasswordWithPost(@RequestBody ChangePasswordDto dto, Authentication authentication) {
        return changePassword(dto, authentication);
    }

    @PatchMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody ChangePasswordDto dto, Authentication authentication) {
        userService.changePassword((User) authentication.getPrincipal(), dto.getCurrentPassword(), dto.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

    @java.lang.SuppressWarnings("all")
    public UserController(final UserService userService) {
        this.userService = userService;
    }
}
