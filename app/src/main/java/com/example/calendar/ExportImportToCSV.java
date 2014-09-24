package com.example.calendar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class ExportImportToCSV {
    private static final String TAG = "XmlSerializer";

    public static <T> void writeToCsv(List<T> l, File f) throws IOException,
            IllegalAccessException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(f));

        Field[] fields = l.get(0).getClass().getDeclaredFields();
        String[] line = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            Object value;
            value = fields[i].getName();
            line[i] = value.toString();
        }
        csvWriter.writeNext(line);

        for (T object : l) {
            line = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                Object value;
                value = fields[i].get(object);
                line[i] = value.toString();
            }
            csvWriter.writeNext(line);
            System.out.println("Write: " + Arrays.toString(line));
        }
        csvWriter.close();
    }

    /**
     * For now works only with Strings and Integers
     */
    public static <T> List<T> readFromCsv(Class<T> c, File f)
            throws FileNotFoundException, IOException, InstantiationException,
            IllegalAccessException {
        ArrayList<T> list = new ArrayList<T>();

        Field[] fields = c.getDeclaredFields();

        CSVReader reader = new CSVReader(new FileReader(f));
        reader.readNext();
        for (String[] s : reader.readAll()) {
            // if
            System.out.println("Read: " + Arrays.toString(s));
            // TODO check wether s.length == fields.length
            T object = c.newInstance();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                System.out.println(fields[i].getName() + ":"
                        + fields[i].getType());
                if (fields[i].getType() == Integer.TYPE
                        || fields[i].getType() == Integer.class)
                    fields[i].set(object, Integer.parseInt(s[i]));
                if (fields[i].getType() == String.class)
                    fields[i].set(object, s[i]);
            }
            list.add(object);
        }
        System.out.println("Formed list:" + list);
        return list;
    }

    // public static void main(String... args) {
    // OneEvent testEvent = new OneEvent(1, "title", 1, 2, 3, 4, 5, 6, 7, 8, 9,
    // 10, 11, "category", "location", "info",
    // 1, "file_path", "push_time", "sound", 1);
    // File testFile = new File("test.csv");
    // try {
    // writeToCsv(Arrays.asList(new OneEvent[] {testEvent}), testFile);
    // readFromCsv(OneEvent.class, testFile);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
}
