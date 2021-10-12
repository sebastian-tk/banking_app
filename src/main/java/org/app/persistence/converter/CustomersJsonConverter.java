package org.app.persistence.converter;

import lombok.NonNull;
import org.app.persistence.model.CustomersList;

public class CustomersJsonConverter extends JsonConverter<CustomersList> {
    public CustomersJsonConverter(@NonNull String jsonFileName) {
        super(jsonFileName);
    }
}
