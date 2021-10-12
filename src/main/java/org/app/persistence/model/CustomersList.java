package org.app.persistence.model;

import lombok.Getter;
import lombok.Setter;
import org.app.customer.Customer;

import java.util.List;

@Getter
@Setter
public class CustomersList {
    private List<Customer> customers;

    public CustomersList(List<Customer> customers) {
        if(customers == null){
            throw new IllegalArgumentException("Invalid customers argument when create CustomersList");
        }
        this.customers = customers;
    }
}
