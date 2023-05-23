package nyan.ecommerce.spreeze.auth;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nyan.ecommerce.spreeze.auth.token.AuthToken;
import nyan.ecommerce.spreeze.user.User;
import nyan.ecommerce.spreeze.user.UserToForgotPasswordDto;
import nyan.ecommerce.spreeze.user.UserToLoginDao;
import nyan.ecommerce.spreeze.user.UserToResetPasswordDto;
import nyan.ecommerce.spreeze.user.UserToUpdatePasswordDto;
import nyan.ecommerce.spreeze.user.UserToUpdateProfileDto;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("register")

    public ResponseEntity<AuthToken> register(@RequestBody User user, HttpServletResponse response) {
        return new ResponseEntity<AuthToken>(authService.register(response, user), HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<AuthToken> login(@RequestBody UserToLoginDao userToLogin, HttpServletResponse response) {
        return new ResponseEntity<AuthToken>(authService.login(response, userToLogin), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        return new ResponseEntity<String>(authService.logOut(response), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody UserToUpdatePasswordDto userToUpdatePassword,
            @CurrentSecurityContext(expression = "authentication") Authentication auth) {

        User currentUser = (User) auth.getPrincipal();

        return new ResponseEntity<String>(
                authService.updatePassword(currentUser.getEmail(), userToUpdatePassword.currentPassword,
                        userToUpdatePassword.password),
                HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("updateProfile")
    public ResponseEntity<String> updatePassword(@RequestBody UserToUpdateProfileDto userToUpdateProfile,
            @CurrentSecurityContext(expression = "authentication") Authentication auth) {

        User currentUser = (User) auth.getPrincipal();

        return new ResponseEntity<String>(
                authService.updateProfile(currentUser, userToUpdateProfile),
                HttpStatus.OK);
    }

    @PostMapping("forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody UserToForgotPasswordDto userToForgotPassword) {
        return new ResponseEntity<String>(
                authService.forgotPassword(userToForgotPassword.email),
                HttpStatus.OK);
    }

    @PostMapping("resetPassword/{resetToken}")
    public ResponseEntity<String> resetPassword(@PathVariable("resetToken") String resetToken,
            @RequestBody UserToResetPasswordDto userToResetPassword) {
        return new ResponseEntity<String>(authService.resetPassword(resetToken, userToResetPassword.password),
                HttpStatus.OK);
    }
}
