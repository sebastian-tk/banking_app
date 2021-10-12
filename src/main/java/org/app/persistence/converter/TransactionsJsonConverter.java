package org.app.persistence.converter;

import lombok.NonNull;
import org.app.persistence.model.TransactionsList;

public class TransactionsJsonConverter extends JsonConverter<TransactionsList> {
    public TransactionsJsonConverter(@NonNull String jsonFileName) {
        super(jsonFileName);
    }
}
