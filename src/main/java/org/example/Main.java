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
            List<Set<String>> groups = new ArrayList<>();
            List<Map<String, Integer>> parts = new ArrayList<>();

            String line = reader.readLine();
            while (line != null) {
                String[] columns = getColumnsOf(line);
                Integer groupNumber = null;
                for (int i = 0; i < Math.min(parts.size(), columns.length); i++) {
                    Integer groupNumber2 = parts.get(i).get(columns[i]);
                    if (groupNumber2 != null) {
                        if (groupNumber == null) {
                            groupNumber = groupNumber2;
                        } else if (!Objects.equals(groupNumber, groupNumber2)) {
                            for (String line2 : groups.get(groupNumber2)) {
                                groups.get(groupNumber).add(line2);
                                apply(getColumnsOf(line2), groupNumber, parts);
                            }
                            groups.set(groupNumber2, new HashSet<>());
                        }
                    }
                }
                if (groupNumber == null) {
                    if (Arrays.stream(columns).anyMatch(s -> !s.isEmpty())) {
                        groups.add(new HashSet<>(List.of(line)));
                        apply(columns, groups.size() - 1, parts);
                    }
                } else {
                    groups.get(groupNumber).add(line);
                    apply(columns, groupNumber, parts);
                }
                line = reader.readLine();
            }
            reader.close();

            System.out.println("Групп размера больше 1: " + groups.stream().filter(s -> s.size() > 1).count());
            groups.sort(Comparator.comparingInt(s -> -s.size()));
            int i = 0;
            for (Set<String> group : groups) {
                i++;
                System.out.println("\nГруппа " + i);
                for (String val : group) {
                    System.out.println(val);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String[] getColumnsOf(String line) {
        for (int i = 1; i < line.length() - 1; i++) {
            if (line.charAt(i) == '"' && line.charAt(i - 1) != ';' && line.charAt(i + 1) != ';') {
                return new String[0];
            }
        }
        return line.replaceAll("\"", "").split(";");
    }

    private static void apply(String[] newValues, int groupNumber, List<Map<String, Integer>> parts) {
        for (int i = 0; i < newValues.length; i++) {
            if (newValues[i].isEmpty()) {
                continue;
            }
            if (i < parts.size()) {
                parts.get(i).put(newValues[i], groupNumber);
            } else {
                HashMap<String, Integer> map = new HashMap<>();
                map.put(newValues[i], groupNumber);
                parts.add(map);
            }
        }
    }
}
