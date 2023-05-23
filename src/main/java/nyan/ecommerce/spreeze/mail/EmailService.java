package nyan.ecommerce.spreeze.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendMessage(EmailDataDto emailData) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("phamthanhnhanussh@gmail.com");
        message.setTo(emailData.to);
        message.setSubject(emailData.subject);
        message.setText(emailData.text);

        emailSender.send(message);
    }
}
