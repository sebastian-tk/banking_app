# banking_app
A simple program that simulates operations performed in a bank in Java

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Demo](#demo)
* [Functionality](#functionality)

## General info
The program was created in order to use the acquired programming skills in basic Java along with code testing.

The program simulates the operation of a banking application. It serves two types of clients: client and business client.
It enables logging in, registration, banking transactions and browsing history depending on the selected criterion

## Technologies
Project is created with:
* Java 16
* Maven 3.8.2
* Junit 5.7
* AssertJ 3.2
* Mockito 4.0

## Setup
### First option
Copy app.jar and being in the same folder enter in the console:
```
java -jar --enable-preview app.jar
```
after execution, a folder with the files to be saved will be created,you get empty files

### Second option
Run from maven:
```
mvn clean package
```
or
```
mvn clean install
```
then the project will be built for you anew. There are sample clients in the resources folder that you can test this application on

## Demo
Short demo about starting the program

https://user-images.githubusercontent.com/74151967/137168975-7c125512-927d-491a-b19b-6eb64e63b5f2.mp4

## Functionality

### Task
The goal was to write a program that would allow to implement a customer service mechanism in a banking application.
Assumptions:
1. The bank has two types of clients: individual clients and business clients
2. business client, withdraws funds each time, charged with a commission on the funds withdrawn.
3. a business customer can make a specified maximum payment amount in a settlement period in a given month. After it is exceeded, each subsequent payment is charged with a         fixed commission.
4. the application is to enable:
* *New User Registration*
* *Logging into the banking system*
* *Checking the balance after logging in*
* *Making a payment after logging in with checking availability of funds and calculating commissions for business clients*
* *Making a payment after logging in with commission calculation for business customers*
* *Ability to view and search payment history. Various transaction search criteria.*

### Solution
The bank manages the application.
Then it uses customerService to manage the customer base which is taken from the .json file and passed to customerService. Then customerService allows you to register a new customer and log in.
* If a new client is selected, the data is fetched from the user and validated and a new client is added. The client base is returned to the bank which saves the changes to the file.
* If login is selected, the data is downloaded from the client, then the password is hashed and compared with the password in the file. If the password is correct, customerService calls a given customer who allows payments to the account, withdrawals from the account, checking the balance and searching for transactions according to the selected criterion



