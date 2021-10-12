package org.app.customer.extensions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.app.customer.transaction.Transaction;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.FileReader;

public class ParametersResolverForTransaction implements ParameterResolver {
    private static final String fileName= "src/test/resources/transactionCorrectInputData.json";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(Transaction.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try(FileReader fileReader = new FileReader(fileName)){
            return gson.fromJson(fileReader,Transaction.class);
        }catch (Exception exc){
            throw new IllegalStateException(exc.getMessage());
        }
    }
}
