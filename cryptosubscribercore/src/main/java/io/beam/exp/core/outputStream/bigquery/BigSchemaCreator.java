package io.beam.exp.core.outputStream.bigquery;


import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.LegacySQLTypeName;
import com.google.cloud.bigquery.Schema;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class BigSchemaCreator {

    final Map<String, String> bqschemaHelper(Class c){
        String className= c.getName();
        String []classSegments = className.split("\\.");
        String fileName = classSegments[classSegments.length-1] + "_BQ_Schema.json";

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        File jsonFile=null;
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            jsonFile = new File(resource.getFile());
        }

        StringBuilder sb = new StringBuilder();

        try (FileReader reader = new FileReader(jsonFile);
             BufferedReader br = new BufferedReader(reader)) {

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }catch(FileNotFoundException fe){
            log.error(fe.getMessage());
            throw new IllegalArgumentException(fe.getMessage());
        }catch(IOException ioe){
            log.error(ioe.getMessage());
            throw new IllegalArgumentException(ioe.getMessage());
        }
        Gson g = new Gson();
        Map<String, String> schema = g.fromJson(sb.toString(), Map.class);

        return schema;
    }

    private static LegacySQLTypeName getType(String name){
        if(name.equals("TIMESTAMP")){
            return LegacySQLTypeName.TIMESTAMP;
        }else if(name.equals("STRING")){
            return LegacySQLTypeName.STRING;
        }else if(name.equals("NUMERIC")){
            return LegacySQLTypeName.NUMERIC;
        }else if(name.equals("FLOAT")){
            return LegacySQLTypeName.FLOAT;
        }else if(name.equals("INTEGER")){
            return LegacySQLTypeName.INTEGER;
        }
        return LegacySQLTypeName.STRING;
    }
    public Schema createSchema(Class c){
        Map<String, String> fieldMap = bqschemaHelper( c);

        List<Field> schemaFields = fieldMap.entrySet().stream().map( entry->Field.of(entry.getKey(),getType(entry.getValue())))
            .collect(Collectors.toList());
        Schema schema = Schema.of(schemaFields);

        return schema;
    }

}
