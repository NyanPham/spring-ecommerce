package nyan.ecommerce.spreeze.auth.token;

import java.util.Date;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nyan.ecommerce.spreeze.user.UserRole;

@AllArgsConstructor
@Getter
@Setter
public class VerifiedToken {
    private ObjectId id;
    private UserRole userRole;
    private Date issuedAt;
    private Date expiresAt;
}
