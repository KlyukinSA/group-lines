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
            ArrayList<Group> groups = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {
                String[] rows = line.split(";");
                List<String> ints = new ArrayList<>();
                for (String row : rows) {
                    try {
                        ints.add(row.replaceAll("\"", ""));
                    }
                    catch (NumberFormatException exception) {
                        ints.add(null);
                    }
                }
                boolean belongs = false;
                for (Group group : groups) {
                    if (belongs) {
                        break;
                    }
                    List<Set<String>> parts = group.getParts();
                    for (int i = 0; i < Math.min(parts.size(), rows.length); i++) {
                        Set<String> possibleRowValues = parts.get(i);
                        String value = ints.get(i);
                        if (possibleRowValues.contains(value)) {
                            group.getLines().add(line);
                            apply(ints, parts);
                            belongs = true;
                            break;
                        }
                    }
                }
                if (!belongs && ints.stream().anyMatch(s -> !s.isEmpty())) {
                    List<Set<String>> parts = new ArrayList<>();
                    apply(ints, parts);
                    groups.add(Group.builder()
                            .lines(new ArrayList<>(Arrays.asList(line)))
                            .parts(parts)
                            .build());
                }
                line = reader.readLine();
            }
            reader.close();

            System.out.println("--------------");
            for (Group group : groups) {
                System.out.println(group.getLines());
//                group.getParts().stream().forEach(p -> p.stream().forEach(i -> System.out.println(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void apply(List<String> newValues, List<Set<String>> possible) {
        int j = 0;
        for (; j < possible.size(); j++) {
            if (j >= newValues.size()) {
                return;
            }
            possible.get(j).add(newValues.get(j));
        }
        for (; j < newValues.size(); j++) {
            possible.add(new HashSet<>(Arrays.asList(newValues.get(j))));
        }
    }
}