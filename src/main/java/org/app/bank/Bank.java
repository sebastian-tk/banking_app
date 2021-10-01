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

    private Bank(String fileNameCustomers, String fileNameEncryptedPasswords) {
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
    public static Bank createBank(String fileNameCustomers, String fileNameEncryptedPasswords){
        if(fileNameCustomers == null || fileNameCustomers.isEmpty()){
            throw new IllegalArgumentException("Invalid customers file name when creating bank");
        }
        if(fileNameEncryptedPasswords == null || fileNameEncryptedPasswords.isEmpty()){
            throw new IllegalArgumentException("Invalid passwords file name when creating bank");
        }
        return new Bank(fileNameCustomers, fileNameEncryptedPasswords);
    }

    /**
     * The method starts the customer service and then downloads the modified maps and saves them in a .json files
     */
    public void service(){
        customersService.service();
        updateData();
    }

    /**
     *
     * @param customer object Customer
     * @param password  array chars as password
     */
    public void addCustomer(Customer customer,char[] password){
        //arguments are checked in the ServiceCustomer class
        customersService.add(customer,password);
        updateData();
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

    /**
     * All changes in the customer database are saved
     */
    private void updateData(){
        downloadChanges();
        saveData();
    }
    /**
     * The method gets the customersMap update and the hashPasswordMap from the customerService
     */
    private void downloadChanges(){
        customersMap = customersService.getMapCustomers();
        hashPasswordsMap = customersService.getMapHashPasswords();
    }

    /**
     * Method saves customersMap and hashPasswordsMap to .json files
     */
    private void saveData(){
        saveCustomers();
        saveEncryptedPasswords();
    }

    /**
     * Method save customers to file from customersMap
     */
    private void saveCustomers(){
        customersJsonConverter.toJson(new CustomersList(convertValuesToList(customersMap)));
    }

    /**
     * Method save hashes passwords to file from hashPasswordsMap
     */
    private void saveEncryptedPasswords(){
        encryptedPasswordsJsonConverter.toJson(new EncryptedPasswordsList(convertValuesToList(hashPasswordsMap)));
    }

    /**
     *
     * @param map Map with keys as Pesel and values of type T
     * @param <T> type of values of Map
     * @return List with objects of type T from map values
     */
    public static  <T> List<T> convertValuesToList(Map<Pesel,T> map){
        return map.values().stream().toList();
    }

}

