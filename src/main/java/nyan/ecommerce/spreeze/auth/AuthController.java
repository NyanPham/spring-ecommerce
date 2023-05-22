package nyan.ecommerce.spreeze.auth;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nyan.ecommerce.spreeze.auth.token.AuthToken;
import nyan.ecommerce.spreeze.user.User;
import nyan.ecommerce.spreeze.user.UserToLoginDao;

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
}
