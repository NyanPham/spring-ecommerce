package nyan.ecommerce.spreeze.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import nyan.ecommerce.spreeze.auth.token.AuthToken;
import nyan.ecommerce.spreeze.user.User;
import nyan.ecommerce.spreeze.user.UserRepository;
import nyan.ecommerce.spreeze.user.UserToLoginDao;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthHelper authHelper;

    public AuthToken register(HttpServletResponse response, User user) {
        User newUser = userRepository.insert(user);

        String encodedPassword = bCryptPasswordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        userRepository.save(newUser);

        AuthToken authToken = authHelper.createToken(newUser.getId(), newUser.getUserRole());
        Cookie authCookie = authHelper.createCookie("jwt", authToken.getToken(), false, false, 1, null);

        response.addCookie(authCookie);
        return authToken;
    }

    public AuthToken login(HttpServletResponse response, UserToLoginDao userToLogin) {
        User currentUser = (User) userRepository.findUserByEmail(userToLogin.email);

        if (currentUser == null) {
            throw new IllegalStateException("user not found");
        }

        Boolean correctPassword = bCryptPasswordEncoder.matches(userToLogin.password, currentUser.getPassword());
        if (!correctPassword) {
            throw new IllegalStateException("Incorrect Password");
        }

        AuthToken authToken = authHelper.createToken(currentUser.getId(), currentUser.getUserRole());
        Cookie authCookie = authHelper.createCookie("jwt", authToken, false, false, 1, null);

        response.addCookie(authCookie);

        return authToken;
    }
}
