package org.app.persistence.converter;

import org.app.persistence.model.TransactionsList;

public class TransactionsJsonConverter extends JsonConverter<TransactionsList> {
    public TransactionsJsonConverter(String jsonFileName) {
        super(jsonFileName);
    }
}
