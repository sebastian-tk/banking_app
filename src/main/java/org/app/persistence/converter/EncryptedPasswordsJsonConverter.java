package org.app.persistence.converter;

import lombok.NonNull;
import org.app.persistence.model.EncryptedPasswordsList;

public class EncryptedPasswordsJsonConverter extends JsonConverter<EncryptedPasswordsList>{
    public EncryptedPasswordsJsonConverter(@NonNull String jsonFileName) {
        super(jsonFileName);
    }
}
