package org.app.persistence.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.nio.file.Files.*;

public abstract class JsonConverter <T>{
    private final static String SEPARATOR ="/";
    private final static String DIRECTORY_NAME ="./data/";
    private final Gson gsonObj;
    private final Type typeT;
    private final String defaultFile;
    private String jsonFileName;


    public JsonConverter(String jsonFileName) {
        this.jsonFileName = jsonFileName;
        this.gsonObj = new GsonBuilder().setPrettyPrinting().create();
        this.typeT = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.defaultFile = DIRECTORY_NAME.concat(getNameJsonFile());
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
        ifNotExitsCreate();
        try(FileReader reader = new FileReader(jsonFileName)){
            return Optional.of(gsonObj.fromJson(reader,typeT));
        }catch (IOException exc){
            throw new IllegalStateException("Json filename exception - convert from Json");
        }catch (NullPointerException exc){
            return Optional.empty();
        }
    }

    /**
     * Method check if file exist , if not create this file with default name
     */
    private void ifNotExitsCreate(){
        if(!exists(Path.of(jsonFileName))){
            try{
                File file = new File(defaultFile);
                file.getParentFile().mkdirs();
                file.createNewFile();
                jsonFileName = defaultFile;
            }catch (IOException exc){
                System.out.println(exc.getMessage());
            }
        }
    }

    /**
     *
     * @return String as only name file without path
     */
    private String getNameJsonFile(){
        var arrayNames = jsonFileName.split(SEPARATOR);
        return arrayNames[arrayNames.length-1];
    }


}
