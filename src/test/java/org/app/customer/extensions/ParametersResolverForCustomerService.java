package org.app.customer.extensions;

import org.app.customer.Customer;
import org.app.customer.CustomersService;
import org.app.customer.EncryptedPassword;
import org.app.customer.Pesel;
import org.app.customer.transaction.Transaction;
import org.app.persistence.converter.CustomersJsonConverter;
import org.app.persistence.converter.EncryptedPasswordsJsonConverter;
import org.app.persistence.converter.TransactionsJsonConverter;
import org.app.persistence.model.CustomersList;
import org.app.persistence.model.EncryptedPasswordsList;
import org.app.persistence.model.TransactionsList;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.*;
import java.util.stream.Collectors;

import static org.app.customer.CustomersService.*;

public class ParametersResolverForCustomerService implements ParameterResolver {
    private static final String customersFile = "src/test/resources/customers.json";
    private static final String passwordsFile = "src/test/resources/passwords.json";
    private static final String transactionsFile = "src/test/resources/transactionsHistory.json";
    private Map<Pesel, EncryptedPassword> mapHashPasswords;
    private Map<Pesel, Customer> mapCustomers;
    private Map<Pesel, List<Transaction>> mapTransactions;

    public ParametersResolverForCustomerService() {
        initCustomers();
        initPasswords();
        initTransactions();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(CustomersService.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return createCustomersService(mapHashPasswords,mapCustomers,mapTransactions);
    }

    private void initCustomers() {
        CustomersJsonConverter customersJsonConverter = new CustomersJsonConverter(customersFile);
        Optional<CustomersList> customersList = customersJsonConverter.fromJson();
        if (customersList.isEmpty()) {
            throw new IllegalStateException("Data corrupted from test Customer file");
        }
        mapCustomers = customersList
                .stream()
                .map(CustomersList::getCustomers)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        Customer::getPesel,
                        customer -> customer,
                        (c1, c2) -> c1,
                        HashMap::new
                ));
    }

    private void initTransactions() {
        TransactionsJsonConverter transactionsJsonConverter = new TransactionsJsonConverter(transactionsFile);
        var data = transactionsJsonConverter.fromJson();
        mapTransactions = data
                .stream()
                .map(TransactionsList::getTransactions)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Transaction::getPesel));
    }

    private void initPasswords() {
        EncryptedPasswordsJsonConverter encryptedPasswordsJsonConverter = new EncryptedPasswordsJsonConverter(passwordsFile);
        var data = encryptedPasswordsJsonConverter.fromJson();
        mapHashPasswords = data
                .stream()
                .map(EncryptedPasswordsList::getEncryptedPasswords)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        EncryptedPassword::getPesel,
                        hashPassword -> hashPassword,
                        (c1, c2) -> c1,
                        HashMap::new
                ));
    }
}

