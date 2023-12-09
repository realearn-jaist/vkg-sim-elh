package com.example.mapping.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MappingUtils {
    public static List<String> readLinesFromMultipartFile(MultipartFile file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public static void parseMappings(List<String> mappingLines, Map<String, String> mappingIdToTarget, Map<String, String> mappingIdToSource, List<String> mappingIds) {
        String currentMappingId = null;
        String currentTarget = null;
        String currentSource = null;

        for (String line : mappingLines) {
            if (line.startsWith("mappingId")) {
                if (currentMappingId != null) {
                    mappingIdToTarget.put(currentMappingId, currentTarget);
                    mappingIdToSource.put(currentMappingId, currentSource);
                    mappingIds.add(currentMappingId);
                }
                currentMappingId = line.trim();
            } else if (line.startsWith("target")) {
                currentTarget = line.trim();
            } else if (line.startsWith("source")) {
                currentSource = line.trim();
            }
        }

        if (currentMappingId != null) {
            mappingIdToTarget.put(currentMappingId, currentTarget);
            mappingIdToSource.put(currentMappingId, currentSource);
            mappingIds.add(currentMappingId);
        }
    }

    public static List<String> processThresholdLines(List<String> thresholdLines, Map<String, String> mappingIdToTarget, Map<String, String> mappingIdToSource, List<String> mappingIds, double threshold, List<String> newMappings) {
        for (String line : thresholdLines) {
            String[] parts = line.split("\t");
            if (parts.length >= 3) {
                String className1 = parts[0];
                String className2 = parts[1];

                double similarity = Double.parseDouble(parts[2]);

                if (similarity > threshold) {
                    System.out.println(className1 + " - " + className2 + "(" + similarity + ")");
                    for (String mappingId : mappingIds) {
                        String target = mappingIdToTarget.get(mappingId);
                        String source = mappingIdToSource.get(mappingId);
                        if (target != null && (containsWholeWord(target, className1) || containsWholeWord(target, className2))) {
                            if (containsWholeWord(target, className1)) {
                                target = target.replace(className1, className2);
                                String mappingId1 = mappingId + "-newmapping-" + className2;
                                String target1 = target;
                                String source1 = source;
                                String newMapping = String.format(
                                        "\n%s\n" +
                                                "%s\n" +
                                                "%s", mappingId1, target1, source1);
                                newMappings.add(newMapping);
                            } else if (containsWholeWord(target, className2)) {
                                target = target.replace(className2, className1);
                                String mappingId1 = mappingId + "-newmapping-" + className1;
                                String target1 = target;
                                String source1 = source;
                                String newMapping = String.format(
                                        "\n%s\n" +
                                                "%s\n" +
                                                "%s", mappingId1, target1, source1);
                                newMappings.add(newMapping);
                            }
                        }
                    }
                }
            }
        }
        return newMappings;
    }

    private static boolean containsWholeWord(String inputText, String word) {
        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(word) + "\\b");
        Matcher matcher = pattern.matcher(inputText);
        return matcher.find();
    }

    public static List<String> insertNewMappings(List<String> existingMappings, List<String> newMappings) {
        int index = existingMappings.lastIndexOf("]]");
        if (index != -1) {
            List<String> result = new ArrayList<>(existingMappings.subList(0, index));
            result.addAll(newMappings);
            result.addAll(existingMappings.subList(index, existingMappings.size()));
            return result;
        } else {
            // Handle the case where there are no existing mappings in the file.
            throw new IllegalArgumentException("Invalid mapping file: Header not found");
        }
    }


}
