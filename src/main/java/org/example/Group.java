package org.example;

import lombok.*;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Group {
    private List<Set<Integer>> parts;
    private List<String> lines;
}
