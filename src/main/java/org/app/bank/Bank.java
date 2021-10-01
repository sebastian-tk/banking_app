package org.app.bank;

import org.app.business_customer.BusinessCustomer;
import org.app.customer.*;
import org.app.persistence.converter.BusinessCustomersJsonConverter;
import org.app.persistence.converter.CustomersJsonConverter;
import org.app.persistence.converter.EncryptedPasswordsJsonConverter;
import org.app.persistence.model.BusinessCustomerList;
import org.app.persistence.model.CustomersList;
import org.app.persistence.model.EncryptedPasswordsList;

import java.util.*;
import java.util.stream.Collectors;

public class Bank {
    private Map<Pesel, Customer> customersMap;
    private Map<Pesel, EncryptedPassword> hashPasswordsMap;

    private EncryptedPasswordsJsonConverter encryptedPasswordsJsonConverter;
    private CustomersJsonConverter customersJsonConverter;
    private BusinessCustomersJsonConverter businessCustomersJsonConverter;
    private CustomersService customersService;

    private Bank(String fileNameBusinessCustomers,String fileNameCustomers, String fileNameEncryptedPasswords) {
        customersMap = new HashMap<>();
        hashPasswordsMap = new HashMap<>();

        encryptedPasswordsJsonConverter = new EncryptedPasswordsJsonConverter(fileNameEncryptedPasswords);
        customersJsonConverter = new CustomersJsonConverter(fileNameCustomers);
        businessCustomersJsonConverter = new BusinessCustomersJsonConverter(fileNameBusinessCustomers);

        loadCustomersToMap();
        loadBusinessCustomersToMap();
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
     * Method loads customers to customersMap from fileNameCustomers file when file is not empty
     */
    private void loadCustomersToMap(){
        var data = customersJsonConverter.fromJson();
        if (data.isPresent()) {
            var customers =  data
                    .stream()
                    .map(CustomersList::getCustomers)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toMap(
                            Customer::getPesel,
                            customer -> customer,
                            (c1, c2) -> c1,
                            HashMap::new
                    ));
            customersMap.putAll(customers);
        }
    }

    /**
     * Method loads businessCustomers to customersMap from fileNameCustomers file when file is not empty
     */
    private void loadBusinessCustomersToMap(){
        var data = businessCustomersJsonConverter.fromJson();
        if (data.isPresent()) {
            var businessCustomers = data
                    .stream()
                    .map(BusinessCustomerList::getBusinessCustomers)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toMap(
                            BusinessCustomer::getPesel,
                            businessCustomer ->  businessCustomer,
                            (c1, c2) -> c1,
                            HashMap::new
                    ));
            customersMap.putAll(businessCustomers);
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
        saveBusinessCustomers();
        saveEncryptedPasswords();
    }

    /**
     * Method saves customers to file from customersMap
     */
    private void saveCustomers() {
        customersJsonConverter.toJson(new CustomersList(customersMap
                .values()
                .stream()
                .filter(customer -> customer.getClass() == Customer.class)
                .toList()));
    }


    /**
     * Method saves customers to file from customersMap
     */
    private void saveBusinessCustomers() {
        businessCustomersJsonConverter.toJson(new BusinessCustomerList(customersMap
                .values()
                .stream()
                .filter(customer -> customer.getClass() == BusinessCustomer.class)
                .map(customer -> (BusinessCustomer)customer)
                .toList()));
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

