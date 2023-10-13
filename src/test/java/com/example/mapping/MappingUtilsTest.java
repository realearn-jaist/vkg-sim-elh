package com.example.mapping;

import com.example.mapping.util.MappingUtils;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MappingUtilsTest {

    @Test
    void testReadLinesFromMultipartFile() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("Line1\nLine2\nLine3".getBytes()));

        List<String> result = MappingUtils.readLinesFromMultipartFile(mockFile);
        List<String> expected = List.of("Line1", "Line2", "Line3");

        assertEquals(expected, result);
    }

    @Test
    void testParseMappings() {
        List<String> mappingLines = List.of(
                "mappingId\tcl_Authors",
                "source\t\tselect \"wr_code\", \"wr_name\" from \"tb_affiliated_writers\"",
                "target\t\t:author/{wr_code}/ a :Author ; :name {wr_name} .",
                "mappingId\tcl_Books",
                "source\t\tselect \"bk_code\", \"bk_title\" from \"tb_books\"",
                "target\t\t:book/{bk_code}/ a :Book ; :title {bk_title} ."
        );
        Map<String, String> mappingIdToTarget = new HashMap<>();
        Map<String, String> mappingIdToSource = new HashMap<>();
        List<String> mappingIds = new ArrayList<>();

        MappingUtils.parseMappings(mappingLines, mappingIdToTarget, mappingIdToSource, mappingIds);

        assertEquals("target\t\t:author/{wr_code}/ a :Author ; :name {wr_name} .", mappingIdToTarget.get("mappingId\tcl_Authors"));
        assertEquals("source\t\tselect \"wr_code\", \"wr_name\" from \"tb_affiliated_writers\"", mappingIdToSource.get("mappingId\tcl_Authors"));
        assertEquals("target\t\t:book/{bk_code}/ a :Book ; :title {bk_title} .", mappingIdToTarget.get("mappingId\tcl_Books"));
        assertEquals("source\t\tselect \"bk_code\", \"bk_title\" from \"tb_books\"", mappingIdToSource.get("mappingId\tcl_Books"));
        assertEquals(List.of("mappingId\tcl_Authors", "mappingId\tcl_Books"), mappingIds);
    }

    @Test
    void testInsertNewMappings() {
        List<String> existingMappings = List.of("@Collection [[", "mappingId1", "source Class1", "target Class1", "]]");
        List<String> newMappings = List.of("mappingId2", "source Class2", "target Class2");
        List<String> result = MappingUtils.insertNewMappings(existingMappings, newMappings);

        List<String> expected = List.of("@Collection [[", "mappingId1", "source Class1", "target Class1","mappingId2", "source Class2", "target Class2", "]]");

        assertEquals(expected, result);
    }

    @Test
    void testProcessThresholdLines() {
        List<String> thresholdLines = List.of("Author\tBook\t0.9", "Author\tEditor\t0.8");
        Map<String, String> mappingIdToTarget = new HashMap<>();
        Map<String, String> mappingIdToSource = new HashMap<>();
        List<String> mappingIds = new ArrayList<>();

        List<String> mappingLines = List.of(
                "mappingId\tcl_Authors",
                "source\t\tselect \"wr_code\", \"wr_name\" from \"tb_affiliated_writers\"",
                "target\t\t:author/{wr_code}/ a :Author ; :name {wr_name} .",
                "mappingId\tcl_Books",
                "source\t\tselect \"bk_code\", \"bk_title\" from \"tb_books\"",
                "target\t\t:book/{bk_code}/ a :Book ; :title {bk_title} ."
        );

        MappingUtils.parseMappings(mappingLines, mappingIdToTarget, mappingIdToSource, mappingIds);

        double threshold = 0.85;
        List<String> newMappings = new ArrayList<>();

        List<String> result = MappingUtils.processThresholdLines(thresholdLines, mappingIdToTarget, mappingIdToSource, mappingIds, threshold, newMappings);

        List<String> expected = List.of("\n" +
                "mappingId\tcl_Authors-newmapping-Book\n" +
                "target\t\t:author/{wr_code}/ a :Book ; :name {wr_name} .\n" +
                "source\t\tselect \"wr_code\", \"wr_name\" from \"tb_affiliated_writers\"", "\n" +
                "mappingId\tcl_Books-newmapping-Author\n" +
                "target\t\t:book/{bk_code}/ a :Author ; :title {bk_title} .\n" +
                "source\t\tselect \"bk_code\", \"bk_title\" from \"tb_books\"");

        assertEquals(expected, result);
    }
}
