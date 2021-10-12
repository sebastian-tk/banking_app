package org.app.persistence.converter;


import lombok.NonNull;
import org.app.persistence.model.BusinessCustomerList;

public class BusinessCustomersJsonConverter extends JsonConverter<BusinessCustomerList> {
    public BusinessCustomersJsonConverter(@NonNull String jsonFileName) {
        super(jsonFileName);
    }
}
