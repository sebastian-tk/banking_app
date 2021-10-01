package org.app.persistence.converter;

import org.app.persistence.model.EncryptedPasswordsList;

public class EncryptedPasswordsJsonConverter extends JsonConverter<EncryptedPasswordsList>{
    public EncryptedPasswordsJsonConverter(String jsonFileName) {
        super(jsonFileName);
    }
}
