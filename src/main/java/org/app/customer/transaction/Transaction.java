package org.app.customer.transaction;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.app.customer.Account;
import org.app.customer.Pesel;

import java.math.BigDecimal;
import java.time.LocalDate;

@ToString
@Getter
@EqualsAndHashCode
public class Transaction {
    private Pesel pesel;
    private TransactionType type;
    private BigDecimal money;
    private LocalDate date;
    private Account account;

    public Transaction(@NonNull Pesel pesel,@NonNull TransactionType type,@NonNull BigDecimal money,@NonNull LocalDate date,@NonNull Account account) {
        if(money.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Invalid money argument when create transaction");
        }
        this.pesel = pesel;
        this.type = type;
        this.money = money;
        this.date = date;
        this.account = account;
    }

}
