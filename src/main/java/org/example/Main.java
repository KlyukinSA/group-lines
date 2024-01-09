package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(args[0]));
            List<List<String>> groups = new ArrayList<>();
            List<Map<String, Integer>> parts = new ArrayList<>();

            String line = reader.readLine();
            while (line != null) {
                String[] rows = line.split(";");
                for (int i = 0; i < rows.length; i++) {
                    rows[i] = rows[i].replaceAll("\"", "");
                }
                Integer groupNumber = null;
                for (int i = 0; i < Math.min(parts.size(), rows.length); i++) {
                    String value = rows[i];
                    if (!value.isEmpty()) {
                        Integer groupNumber2 = parts.get(i).get(value);
                        if (groupNumber != null && groupNumber2 != null && !groupNumber2.equals(groupNumber)) {
                            for (String val : groups.get(groupNumber2)) {
                                groups.get(groupNumber).add(val);
                            }
                        }
                        if (groupNumber == null) {
                            groupNumber = groupNumber2;
                        }
                    }
                }
                if (groupNumber == null) {
                    if (Arrays.stream(rows).anyMatch(s -> !s.isEmpty())) {
                        groups.add(new ArrayList<>(List.of(line)));
                        apply(rows, groups.size() - 1, parts);
                    }
                } else {
                    groups.get(groupNumber).add(line);
                    apply(rows, groupNumber, parts);
                }
                line = reader.readLine();
            }
            reader.close();

            System.out.println("--------------");
            int count = 0;
            for (List<String> group : groups) {
                if (group.size() > 1) {
                    count++;
                    System.out.println(group);
                }
            }
            System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void apply(String[] newValues, int groupNumber, List<Map<String, Integer>> parts) {
        int j = 0;
        for (; j < parts.size(); j++) {
            if (j >= newValues.length) {
                return;
            }
            if (!newValues[j].isEmpty()) {
                parts.get(j).put(newValues[j], groupNumber);
            }
        }
        for (; j < newValues.length; j++) {
            if (!newValues[j].isEmpty()) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put(newValues[j], groupNumber);
                parts.add(map);
            }
        }
    }
}