package org.app.persistence.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public abstract class JsonConverter <T>{
    private final String jsonFileName;
    private final Gson gsonObj =new GsonBuilder().setPrettyPrinting().create();
    private final Type typeT = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    public JsonConverter(String jsonFileName) {
        this.jsonFileName = jsonFileName;
    }

    /**
     *
     * @param objectToConvert object to convert
     *                        Method converts objectToConvert to Json format
     */
    public void toJson(final T objectToConvert){
        try(FileWriter writer= new FileWriter(jsonFileName) ){
            gsonObj.toJson(objectToConvert,writer);
        }catch (IOException exc){
            throw new IllegalStateException("Json filename exception- convert to Json");
        }catch (NullPointerException exc){
            throw new IllegalArgumentException("Invalid object argument when convert to Json");
        }
    }

    /**
     *
     * @return object Optional<T> with object T type
     */
    public Optional<T> fromJson(){
        try(FileReader reader = new FileReader(jsonFileName)){
            return Optional.of(gsonObj.fromJson(reader,typeT));
        }catch (IOException exc){
            throw new IllegalStateException("Json filename exception - convert from Json");
        }catch (NullPointerException exc){
            return Optional.empty();
        }
    }

}
