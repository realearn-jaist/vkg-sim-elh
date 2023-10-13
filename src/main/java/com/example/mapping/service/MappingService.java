package com.example.mapping.service;

import com.example.mapping.util.MappingUtils;
import com.example.mapping.model.MappingResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
public class MappingService {

    public MappingResult performMapping(MultipartFile mappingFile, MultipartFile similarityFile, double threshold) throws IOException {
        List<String> mappingLines = MappingUtils.readLinesFromMultipartFile(mappingFile);
        List<String> similarityLines = MappingUtils.readLinesFromMultipartFile(similarityFile);

        Map<String, String> mappingIdToTarget = new HashMap<>();
        Map<String, String> mappingIdToSource = new HashMap<>();

        List<String> mappingIds = new ArrayList<>();

        MappingUtils.parseMappings(mappingLines, mappingIdToTarget, mappingIdToSource, mappingIds);

        List<String> newMappings = new ArrayList<>();
        newMappings = MappingUtils.processThresholdLines(similarityLines, mappingIdToTarget, mappingIdToSource, mappingIds, threshold, newMappings);

        List<String> updatedMappings = MappingUtils.insertNewMappings(mappingLines, newMappings);

        return new MappingResult(mappingLines, newMappings, updatedMappings);
    }
}
