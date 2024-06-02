package com.verygoodbank.tes.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DataGenerator {

    public static void generateTradeCSV(String filePath, int numberOfRecords, int numberOfProductRecords) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            writer.write("date,product_id,currency,price\n"); // header
            Random random = new Random();
            for (int i = 1; i <= numberOfRecords; i++) {
                String date = "20160101";
                String productId = String.valueOf(random.nextInt(numberOfProductRecords) + 1);
                String currency = "EUR";
                String price = String.format("%.2f", 10 + random.nextFloat() * 90); // price between 10.00 and 100.00
                writer.write(String.join(",", date, productId, currency, price) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateProductCSV(String filePath, int numberOfRecords) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            writer.write("product_id,product_name\n"); // header
            for (int i = 1; i <= numberOfRecords; i++) {
                String productId = String.valueOf(i);
                String productName = "Product " + i;
                writer.write(String.join(",", productId, productName) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
