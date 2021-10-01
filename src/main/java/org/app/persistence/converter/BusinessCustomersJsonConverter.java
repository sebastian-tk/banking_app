package org.app.persistence.converter;


import org.app.persistence.model.BusinessCustomerList;

public class BusinessCustomersJsonConverter extends JsonConverter<BusinessCustomerList> {
    public BusinessCustomersJsonConverter(String jsonFileName) {
        super(jsonFileName);
    }
}
