package org.app.persistence.model;

import lombok.Getter;
import lombok.Setter;
import org.app.business_customer.BusinessCustomer;

import java.util.List;

@Getter
@Setter
public class BusinessCustomerList {
    List<BusinessCustomer> businessCustomers;

    public BusinessCustomerList(List<BusinessCustomer> businessCustomers) {
        if(businessCustomers == null){
            throw new IllegalArgumentException("Invalid businessCustomers argument when create BusinessCustomersList");
        }
        this.businessCustomers = businessCustomers;
    }
}
