package nyan.ecommerce.spreeze.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("sandbox.smtp.mailtrap.io");
        mailSender.setPort(2525);

        mailSender.setUsername("7aea0678396b4b");
        mailSender.setPassword("57b312526ee906");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
