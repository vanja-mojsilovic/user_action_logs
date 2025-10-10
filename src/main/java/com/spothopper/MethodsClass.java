package com.spothopper;

import java.io.*;
import java.util.*;
import org.json.JSONObject;
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


public class MethodsClass extends AbstractClass {
    public JSONObject createEmptyRecord(int spotId) {
        JSONObject obj = new JSONObject();
        obj.put("spot_id", spotId);
        obj.put("number_of_enabled_reservations", 0);
        obj.put("number_of_disabled_reservations", 0);
        obj.put("number_of_enabled_catering", 0);
        obj.put("number_of_disabled_catering", 0);
        obj.put("number_of_enabled_online_orders", 0);
        obj.put("number_of_disabled_online_orders", 0);
        obj.put("number_of_enabled_job_listings", 0);
        obj.put("number_of_disabled_job_listings", 0);
        obj.put("number_of_enabled_private_parties", 0);
        obj.put("number_of_disabled_private_parties", 0);
        obj.put("error", false);
        return obj;
    }

    public boolean errorCatcherMethod(JSONObject json) {
        boolean result = false;

        int enabledReservations = json.getInt("number_of_enabled_reservations");
        int disabledReservations = json.getInt("number_of_disabled_reservations");
        int enabledCatering = json.getInt("number_of_enabled_catering");
        int disabledCatering = json.getInt("number_of_disabled_catering");
        int enabledOnlineOrders = json.getInt("number_of_enabled_online_orders");
        int disabledOnlineOrders = json.getInt("number_of_disabled_online_orders");
        int enabledJobs = json.getInt("number_of_enabled_job_listings");
        int disabledJobs = json.getInt("number_of_disabled_job_listings");
        int enabledParties = json.getInt("number_of_enabled_private_parties");
        int disabledParties = json.getInt("number_of_disabled_private_parties");

        if ((enabledReservations != disabledReservations) ||
            (enabledCatering != disabledCatering) ||
            (enabledOnlineOrders != disabledOnlineOrders) ||
            (enabledJobs != disabledJobs) ||
            (enabledParties != disabledParties)
        ) {
            result = true;
        }
        return result;
    }

    public void exportJsonMapToCsv(Map<Integer, JSONObject> resultMap, String outputPath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            // Step 1: Collect all unique keys from error=true records only
            Set<String> allKeys = new LinkedHashSet<>();
            for (JSONObject json : resultMap.values()) {
                if (json.optBoolean("error")) {
                    allKeys.addAll(json.keySet());
                }
            }

            List<String> sortedKeys = new ArrayList<>(allKeys);
            Collections.sort(sortedKeys);
            writer.println(String.join(",", sortedKeys));

            // Step 2: Write only error=true records
            for (JSONObject json : resultMap.values()) {
                if (json.optBoolean("error")) {
                    List<String> row = new ArrayList<>();
                    for (String key : sortedKeys) {
                        row.add(String.valueOf(json.opt(key)));
                    }
                    writer.println(String.join(",", row));
                }
            }

            System.out.println("Filtered CSV report saved to " + outputPath);
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }




}