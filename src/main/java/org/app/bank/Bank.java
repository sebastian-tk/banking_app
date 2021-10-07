package org.app.bank;

import org.app.business_customer.BusinessCustomer;
import org.app.customer.*;
import org.app.customer.transaction.Transaction;
import org.app.persistence.converter.BusinessCustomersJsonConverter;
import org.app.persistence.converter.CustomersJsonConverter;
import org.app.persistence.converter.EncryptedPasswordsJsonConverter;
import org.app.persistence.converter.TransactionsJsonConverter;
import org.app.persistence.model.BusinessCustomerList;
import org.app.persistence.model.CustomersList;
import org.app.persistence.model.EncryptedPasswordsList;
import org.app.persistence.model.TransactionsList;

import java.util.*;
import java.util.stream.Collectors;

public class Bank {
    private Map<Pesel, Customer> customersMap;
    private Map<Pesel, EncryptedPassword> hashPasswordsMap;
    private Map<Pesel, List<Transaction>> transactionsMap;

    private EncryptedPasswordsJsonConverter encryptedPasswordsJsonConverter;
    private CustomersJsonConverter customersJsonConverter;
    private BusinessCustomersJsonConverter businessCustomersJsonConverter;
    private TransactionsJsonConverter transactionsJsonConverter;
    private CustomersService customersService;

    private Bank(String fileNameBusinessCustomers,String fileNameCustomers, String fileNameEncryptedPasswords,String fileNameTransactions) {
        customersMap = new HashMap<>();
        hashPasswordsMap = new HashMap<>();
        transactionsMap = new HashMap<>();

        encryptedPasswordsJsonConverter = new EncryptedPasswordsJsonConverter(fileNameEncryptedPasswords);
        customersJsonConverter = new CustomersJsonConverter(fileNameCustomers);
        businessCustomersJsonConverter = new BusinessCustomersJsonConverter(fileNameBusinessCustomers);
        transactionsJsonConverter = new TransactionsJsonConverter(fileNameTransactions);

        loadCustomersToMap();
        loadBusinessCustomersToMap();
        loadHashPasswords();
        loadTransactionsToMap();

        customersService = CustomersService.createCustomersService(hashPasswordsMap,customersMap,transactionsMap);
    }

    /**
     *
     *
     * @param fileNameBusinessCustomers String as file name with business customers
     * @param fileNameCustomers String as file name with customers
     * @param fileNameEncryptedPasswords String as file name with encrypted passwords
     * @param fileNameTransactions String as the name of the customers transaction history file
     * @return new object Bank
     */
    public static Bank createBank(String fileNameBusinessCustomers,String fileNameCustomers, String fileNameEncryptedPasswords,String fileNameTransactions){
        if(fileNameBusinessCustomers == null || fileNameBusinessCustomers.isEmpty()){
            throw new IllegalArgumentException("Invalid business customers file name when creating bank");
        }
        if(fileNameCustomers == null || fileNameCustomers.isEmpty()){
            throw new IllegalArgumentException("Invalid customers file name when creating bank");
        }
        if(fileNameEncryptedPasswords == null || fileNameEncryptedPasswords.isEmpty()){
            throw new IllegalArgumentException("Invalid passwords file name when creating bank");
        }
        if(fileNameTransactions == null || fileNameTransactions.isEmpty()){
            throw new IllegalArgumentException("Invalid transactions file name when creating bank");
        }
        return new Bank(fileNameBusinessCustomers,fileNameCustomers, fileNameEncryptedPasswords,fileNameTransactions);
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
     * @param password  String as password
     */
    public void addCustomer(Customer customer,StringBuilder password){
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
     * Method loads transactions to transactionsMap from fileNameTransactions file when file is not empty
     */
    private void loadTransactionsToMap(){
        var data = transactionsJsonConverter.fromJson();
        if (data.isPresent()) {
            var transactions = data
                    .stream()
                    .map(TransactionsList::getTransactions)
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(Transaction::getPesel));
            transactionsMap.putAll(transactions);
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
        transactionsMap = customersService.getMapTransactions();
    }

    /**
     * Method saves customersMap and hashPasswordsMap to .json files
     */
    private void saveData(){
        saveCustomers();
        saveBusinessCustomers();
        saveEncryptedPasswords();
        saveTransactions();
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
     * Method saves customers to file from customersMap
     */
    private void saveTransactions() {
        transactionsJsonConverter.toJson(new TransactionsList(transactionsMap
                                                                .values()
                                                                .stream()
                                                                .flatMap(Collection::stream)
                                                                .toList()));
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

