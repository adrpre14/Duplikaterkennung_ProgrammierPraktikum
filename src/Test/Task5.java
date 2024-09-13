package src.Test;

import org.junit.jupiter.api.Test;
import src.PPClean.Data.Duplicate;
import src.PPClean.Data.Record;
import src.PPClean.Data.Table;
import src.PPClean.Data.TableFactory;
import src.PPClean.DuplicateDetection.SortedNeighborhoodDetection;
import src.PPClean.Helper;
import src.PPClean.Performance.Performance;
import src.PPClean.Performance.Score;
import src.PPClean.Similarity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Task5 {

    Table inputTable = TableFactory.getDefaultInputTable();
    Set<Duplicate> groundTruth = Helper.readDuplicatesFromDefaultGT();
    Performance performance = Performance.initInstance(groundTruth);

    @Test
    void testKeyGen() {
        System.out.println("[Task5: testKeyGen]");
        int[] keyComponents = {0, 3, 1, 3, 0, 0};
        List<String> expectedKeys = new ArrayList<>();
        expectedKeys.add("arn4los");
        expectedKeys.add("arn4los");
        expectedKeys.add("art1stu");
        expectedKeys.add("art1stu");
        expectedKeys.add("hot7bel");
        for (int i=0; i < 5; i++) {
            Record r = inputTable.getData().get(i);
            System.out.print("Calculating key for record: " + r.toString());
            r.generateKey(keyComponents);
            System.out.println("Expected: " + expectedKeys.get(i) + " - actual: " + r.getKey());
            assertEquals(expectedKeys.get(i), r.getKey());
        }
    }

    @Test
    void testKeyGenWhitespaces() {
        System.out.println("[Task5: testKeyGenWhitespaces]");
        String expectedKey = "435s.la";
        int[] keyComponents = {0, 0, 7, 0, 0, 0};
        Record r = inputTable.getData().get(0);
        System.out.print("Calculating key for record: " + r.toString());
        r.generateKey(keyComponents);
        System.out.println("Expected: " + expectedKey + " - actual: " + r.getKey());
        assertEquals(expectedKey, r.getKey());
    }

    @Test
    void testSNHDetection() {
        System.out.println("[Task5: testSNHDetection]");
        System.out.println("Creating SingleAttributeEquality for name comparison (attributeIndex: 1)...");
        SingleAttributeEquality nameAttributeEquality = new SingleAttributeEquality(1);
        int[] keyComponents = {0, 3, 3, 3, 3, 3};
        System.out.println("Creating SortedNeighborhoodDetection with the following parameteres:");
        System.out.println("  similarity threshold: 0.8, window size: 4, key components: [0,3,3,3,3,3]");
        SortedNeighborhoodDetection SNHDetection = new SortedNeighborhoodDetection(0.8, 4, keyComponents);
        System.out.println("Applying SortedNeighborhoodDetection with SingleAttributeEquality...");
        Set<Duplicate> duplicates = SNHDetection.detect(inputTable, nameAttributeEquality);
        Score s = performance.evaluate(duplicates);
        System.out.println("Expected results: Precision: 0.942, Recall: 0.732, F1-Score: 0.824");
        assertEquals(0.942, s.getPrecision(), 0.05);
        assertEquals(0.732, s.getRecall(), 0.05);
        assertEquals(0.824, s.getF1(), 0.05);
    }
}
