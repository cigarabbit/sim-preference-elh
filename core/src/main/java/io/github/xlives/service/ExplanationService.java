package io.github.xlives.service;

import io.github.xlives.framework.BackTraceTable;
import io.github.xlives.framework.descriptiontree.TreeNode;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ExplanationService {

    private List<String> conceptName = new ArrayList<>();

    private StringBuilder explanation = new StringBuilder();

    private File output_file = new File("/Users/rchn/Desktop/simservice");

    public void explainSimilarity(BackTraceTable backTraceTable) throws IOException {

        BigDecimal degree;

        Set<String> cnPair;
        String conceptName1;
        String conceptName2;

        Set<String> roles1 = new HashSet<>();
        Set<String> roles2 = new HashSet<>();

        List<String> matchedCon = new ArrayList<>();

        // iterate through each value in backTraceTable
        for (Map.Entry<Set<String>, List<Map<String, Map<TreeNode<Set<String>>, BigDecimal>>>> val : backTraceTable.getBackTraceTable().entrySet()) {
            cnPair = val.getKey();

            for (Map<String, Map<TreeNode<Set<String>>, BigDecimal>> node : val.getValue()){
                explanation.append(node).append("\n");
                for (Map.Entry<String, Map<TreeNode<Set<String>>, BigDecimal>> nodeChildren : node.entrySet()){
                    String[] wordsArray1 = nodeChildren.getValue().keySet().toString().split("\\s+");
                    removeUnwantedChar(wordsArray1);
                    explanation.append(Arrays.toString(wordsArray1));
                    explanation.append("The similarity between ").append(cnPair).append(" is ");
                    explanation.append(nodeChildren.getValue().values()); // homomorphism degree
                    explanation.append("because\n");
                }
            }

        }

        FileUtils.writeStringToFile(output_file, explanation.toString(), false);
    }

    private static void removeUnwantedChar(String[] wordsArray) {
        for (int i = 0; i < wordsArray.length; i++) {
            wordsArray[i] = wordsArray[i].replaceAll("'", "");
        }
    }

    private List<String> matchConcepts() {
        List<String> matchedCon = new ArrayList<>();

        return  matchedCon;
    }
    public StringBuilder getExplanation() {
        return explanation;
    }

    public void setConceptName(String conceptName) {
        this.conceptName.add(conceptName);
    }

    //public Map<Set<String>, Map<Tree<Set<String>>, BigDecimal>> getBackTraceTable() {
       // return backTraceTable;
    //}

}
