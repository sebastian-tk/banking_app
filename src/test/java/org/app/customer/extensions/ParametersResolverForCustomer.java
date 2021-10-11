package org.app.customer.extensions;

import org.app.customer.Customer;
import org.app.persistence.converter.CustomersJsonConverter;
import org.app.persistence.model.CustomersList;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.Collection;
import java.util.Optional;

public class ParametersResolverForCustomer implements ParameterResolver {
    private static final String fileName= "src/test/resources/customerCorrectInputData.json";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(Customer.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        CustomersJsonConverter customersJsonConverter = new CustomersJsonConverter(fileName);
        Optional<CustomersList> customersList = customersJsonConverter.fromJson();
        if(customersList.isEmpty()){
            throw new IllegalStateException("Data corrupted from test Customer file");
        }
        return customersList
                .stream()
                .map(CustomersList::getCustomers)
                .flatMap(Collection::stream)
                .toList()
                .get(0);

    }
}
