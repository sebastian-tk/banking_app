package org.app.persistence.model;

import lombok.Getter;
import lombok.Setter;
import org.app.customer.EncryptedPassword;

import java.util.List;

@Getter
@Setter
public class EncryptedPasswordsList {
    private List<EncryptedPassword> encryptedPasswords;

    public EncryptedPasswordsList(List<EncryptedPassword> encryptedPasswords) {
        if(encryptedPasswords == null){
            throw new IllegalArgumentException("Invalid encryptedPasswords argument when create EncryptedPasswordsList");
        }
        this.encryptedPasswords = encryptedPasswords;
    }
}
