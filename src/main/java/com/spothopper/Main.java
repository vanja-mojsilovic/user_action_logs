package com.spothopper;

import java.io.*;
import java.util.*;
import org.json.JSONObject;
import com.spothopper.MethodsClass;
import com.opencsv.CSVReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONObject;




public class Main extends AbstractClass {
    public static void main(String[] args) throws IOException {
        Map<Integer, JSONObject> resultMap = new HashMap<>();
        MethodsClass methodsClass = new MethodsClass();
        try(
            FileReader fileReader = new FileReader("logs.csv");
            CSVReader reader = new CSVReader(fileReader);
        ){
            reader.readNext();
            String[] parts;
            System.out.println("Reading logs.csv...");
            while ((parts = reader.readNext()) != null)  {
                if (parts.length != 3){
                    System.out.println("CSV row wrong format!");
                    continue;
                }
                String spotId = parts[0].trim().replace("\"", "");
                int spotIdInt = Integer.parseInt(spotId);
                String email = parts[1].trim().replace("\"", "");
                String action = parts[2].trim().replace("\"", "");
                String[] actionParts = action.split(" - ");
                String key;
                if (action.contains("online_ordering feature deactivated")) {
                    key = "number_of_disabled_online_orders";
                } else {
                    boolean isEnabled = actionParts[0].contains("Enabled");
                    String service = actionParts[1].trim().toLowerCase().replace(" ", "_");
                    key = "number_of_" + (isEnabled ? "enabled_" : "disabled_") + service;
                }
                if (!resultMap.containsKey(spotIdInt)) {
                    resultMap.put(spotIdInt, methodsClass.createEmptyRecord(spotIdInt));
                }
                JSONObject record = resultMap.get(spotIdInt);
                record.put(key, record.getInt(key) + 1);


            }
            System.out.println("\nFinal JSON report:");
            for (JSONObject json : resultMap.values()) {
                boolean errorCatcher = methodsClass.errorCatcherMethod(json);
                json.put("error", errorCatcher);
                //System.out.println(json.toString(2));
            }
            methodsClass.exportJsonMapToCsv(resultMap, "report.csv");


        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }


    }
}