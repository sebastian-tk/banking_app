package org.app.persistence.converter;

import org.app.persistence.model.CustomersList;

public class CustomersJsonConverter extends JsonConverter<CustomersList> {
    public CustomersJsonConverter(String jsonFileName) {
        super(jsonFileName);
    }
}
