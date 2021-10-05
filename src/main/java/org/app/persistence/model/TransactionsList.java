package org.app.persistence.model;

import lombok.Getter;
import org.app.customer.transaction.Transaction;

import java.util.List;

@Getter
public class TransactionsList{
    private List<Transaction> transactions;

    public TransactionsList(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
