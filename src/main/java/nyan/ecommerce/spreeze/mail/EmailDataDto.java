package nyan.ecommerce.spreeze.mail;

public class EmailDataDto {
    public String to;
    public String subject;
    public String text;

    public EmailDataDto(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

}
