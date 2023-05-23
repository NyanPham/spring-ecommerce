package nyan.ecommerce.spreeze.auth;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import nyan.ecommerce.spreeze.auth.token.AuthToken;
import nyan.ecommerce.spreeze.mail.EmailDataDto;
import nyan.ecommerce.spreeze.mail.EmailService;
import nyan.ecommerce.spreeze.user.User;
import nyan.ecommerce.spreeze.user.UserRepository;
import nyan.ecommerce.spreeze.user.UserToLoginDao;
import nyan.ecommerce.spreeze.user.UserToUpdateProfileDto;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private EmailService emailService;

    public AuthToken register(HttpServletResponse response, User user) {
        Boolean userExists = userRepository.findUserByEmail(user.getEmail()) != null;

        if (userExists) {
            throw new IllegalStateException("User with that email already exists!");
        }

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

    public String logOut(HttpServletResponse response) {
        SecurityContextHolder.getContext().setAuthentication(null);

        Cookie cookie = authHelper.createCookie("jwt", "log_out", false, false, null, 3);
        response.addCookie(cookie);

        return "You have logged out";
    }

    public String updatePassword(String email, String currentPassword, String password) {
        User currentUser = (User) userRepository.findUserByEmail(email);

        if (currentUser == null) {
            throw new IllegalStateException("User with that email does not exist!");
        }

        Boolean correctPassword = bCryptPasswordEncoder.matches(currentPassword, currentUser.getPassword());
        if (!correctPassword) {
            throw new IllegalStateException("Incorrect password!");
        }

        String encodedNewPassword = bCryptPasswordEncoder.encode(password);
        currentUser.setPassword(encodedNewPassword);
        currentUser.setPasswordChangedAt(new Date());

        userRepository.save(currentUser);

        return "Password updated";
    }

    public String updateProfile(User currentUser, UserToUpdateProfileDto userToUpdateProfile) {
        Query profileQuery = new Query();
        profileQuery.addCriteria(Criteria.where("id").is(currentUser.getId()));

        Update profileUpdate = new Update();

        if (userToUpdateProfile.username != null) {
            profileUpdate.set("username", userToUpdateProfile.username);
        }

        if (userToUpdateProfile.email != null) {
            profileUpdate.set("email", userToUpdateProfile.email);
        }

        mongoTemplate.findAndModify(profileQuery, profileUpdate, new FindAndModifyOptions().returnNew(true),
                User.class);

        return "Profile Updated";
    }

    public String forgotPassword(String email) {
        User user = (User) userRepository.findUserByEmail(email);

        if (user == null) {
            throw new IllegalStateException("Email not registered");
        }

        String token = UUID.randomUUID().toString();

        Long now = new Date().getTime();

        user.setResetPasswordToken(token);
        user.setResetPasswordExpiresAt(new Date(now + (15 * 60 * 1000)));
        userRepository.save(user);

        String SEVER_HOST = "localhost:8080/api/v1/auth/resetPassword";
        String messageContent = String.format("reset link: %s/%s", SEVER_HOST, token);

        EmailDataDto emailData = new EmailDataDto(email, "Reset Password", messageContent);

        emailService.sendMessage(emailData);

        return token;
    }

    public String resetPassword(String resetToken, String password) throws UsernameNotFoundException {
        Query userQuery = new Query();
        userQuery.addCriteria(Criteria.where("resetPasswordToken").is(resetToken));

        List<User> users = mongoTemplate.find(userQuery, User.class);

        User user = users.stream().filter(u -> u.getResetPasswordExpiresAt().after(new Date()))
                .findFirst().orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("Token expires, please reset password again!");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setResetPasswordExpiresAt(null);
        user.setResetPasswordToken(null);
        userRepository.save(user);

        return "Your password has been reset successfully";
    }
}
