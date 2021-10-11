package org.app.customer.extensions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.app.customer.Account;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.FileReader;

public class ParametersResolverForAccount implements ParameterResolver {
    private static final String fileName= "src/test/resources/accountCorrectInputData.json";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(Account.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Account account;
        try(FileReader fileReader = new FileReader(fileName)){
            account = gson.fromJson(fileReader,Account.class);
            if(account == null){
                throw new IllegalStateException("Corrupted data in the account test file");
            }
            return account;
        }catch (Exception exc){
            throw new IllegalStateException(exc.getMessage());
        }

    }
}
