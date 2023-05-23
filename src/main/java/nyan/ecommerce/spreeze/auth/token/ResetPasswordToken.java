package nyan.ecommerce.spreeze.auth.token;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "resetTokens")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordToken {

    @Id
    private ObjectId id;

    private String resetToken;
    private Date expiresAt;
}
