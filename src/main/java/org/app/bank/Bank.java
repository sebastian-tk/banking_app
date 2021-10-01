package org.app.bank;

import org.app.customer.*;
import org.app.persistence.converter.CustomersJsonConverter;
import org.app.persistence.converter.EncryptedPasswordsJsonConverter;
import org.app.persistence.model.CustomersList;
import org.app.persistence.model.EncryptedPasswordsList;

import java.util.*;
import java.util.stream.Collectors;

public class Bank {
    private Map<Pesel, Customer> customersMap;
    private Map<Pesel, EncryptedPassword> hashPasswordsMap;

    private EncryptedPasswordsJsonConverter encryptedPasswordsJsonConverter;
    private CustomersJsonConverter customersJsonConverter;
    private CustomersService customersService;

    private Bank(String fileNameBusinessCustomers,String fileNameCustomers, String fileNameEncryptedPasswords) {
        customersMap = new HashMap<>();
        hashPasswordsMap = new HashMap<>();

        encryptedPasswordsJsonConverter = new EncryptedPasswordsJsonConverter(fileNameEncryptedPasswords);
        customersJsonConverter = new CustomersJsonConverter(fileNameCustomers);

        loadCustomersMap();
        loadHashPasswords();

        customersService = CustomersService.createCustomersService(hashPasswordsMap,customersMap);
    }

    /**
     *
     * @param fileNameCustomers String as file name with customers
     * @param fileNameEncryptedPasswords String as file name with encrypted passwords
     * @return new object Bank
     */
    public static Bank createBank(String fileNameBusinessCustomers,String fileNameCustomers, String fileNameEncryptedPasswords){
        if(fileNameBusinessCustomers == null || fileNameBusinessCustomers.isEmpty()){
            throw new IllegalArgumentException("Invalid business customers file name when creating bank");
        }
        if(fileNameCustomers == null || fileNameCustomers.isEmpty()){
            throw new IllegalArgumentException("Invalid customers file name when creating bank");
        }
        if(fileNameEncryptedPasswords == null || fileNameEncryptedPasswords.isEmpty()){
            throw new IllegalArgumentException("Invalid passwords file name when creating bank");
        }
        return new Bank(fileNameBusinessCustomers,fileNameCustomers, fileNameEncryptedPasswords);
    }


    /**
     * Method loads customersMap from fileNameCustomers file when file is not empty
     */
    private void loadCustomersMap(){
        var data = customersJsonConverter.fromJson();
        if (data.isPresent()) {
            customersMap = data
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
    }

    /**
     *
     * Method loads customersMap from fileNameCustomers file when file is not empty
     */
    private void loadHashPasswords() {
        var data = encryptedPasswordsJsonConverter.fromJson();
        if (data.isPresent()) {
            hashPasswordsMap = data
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

}

