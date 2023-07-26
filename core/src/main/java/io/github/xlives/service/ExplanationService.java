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

    private StringBuilder result_exp = new StringBuilder();
    private StringBuilder explanation;
    private StringBuilder header;

    private File output_file = new File("/Users/rchn/Desktop/simservice");

    public void explainSimilarity(BackTraceTable backTraceTable) throws IOException {
        BigDecimal degree = null;

        Set<String> exiSet = new HashSet<>();
        Set<String> priSet = new HashSet<>();

        List<Set<String>> priList = new ArrayList<>();

        String concept1 = "";
        String concept2 = "";
        String curConcept = "";
        String exi = "";

        boolean headerAdded = false;

        List<String> matchedCon = new ArrayList<>();

        if (output_file.exists()) {
            output_file.delete();
        }

        // iterate through each value in backTraceTable
        for (Map.Entry<Map<Integer, String[]>, List<Map<String, Map<TreeNode<Set<String>>, BigDecimal>>>> val : backTraceTable.getBackTraceTable().entrySet()) {
            explanation = new StringBuilder();
            header = new StringBuilder();

            priSet.clear();
            exiSet.clear();

            Map<Integer, String[]> keyMap = val.getKey();

            for (int i = 0; i < keyMap.size(); i++) {
                String[] arrayValue = keyMap.get(i);
                if (arrayValue != null) {
                    for (int j = 0; j < arrayValue.length; j++) {
                        if (j == 0) { concept1 = arrayValue[0]; curConcept = concept1;}
                        else { concept2 = arrayValue[1]; }
                    }
                }
            }

            for (Map<String, Map<TreeNode<Set<String>>, BigDecimal>> node : val.getValue()) {
                for (Map.Entry<String, Map<TreeNode<Set<String>>, BigDecimal>> nodeChildren : node.entrySet()) {
                    explanation.append(nodeChildren.getKey());
                    Map<TreeNode<Set<String>>, BigDecimal> innerMap = nodeChildren.getValue();
                    for (Map.Entry<TreeNode<Set<String>>, BigDecimal> child : innerMap.entrySet()) {

                        // Get the homomorphism degree for each node
                        degree = child.getValue();
                        priSet.add(child.getKey().getData().toString());

                        // explanation.append(pri);
                        // existential

                    }
                }
            }

            priList.add(priSet);

            // explanation.append("The similarity between ").append(concept1).append(" and ").append(concept2).append(" is ");

            // explanation.append(degree).append("\n"); // homomorphism degree
            explanation.append("\t").append(priSet).append("\n");

            /*
            explanation.append("\t").append(cnPair).append(" = ").append(priSet).append("\n");
            explanation.append("\t\t").append(exiSet).append("\n"); */

           result_exp.append(explanation);
        }

        FileUtils.writeStringToFile(output_file, result_exp.toString(), true);
    }

    private void writeToFile() throws IOException {
        result_exp.append(header);

    }
    private String removeUnwantedChar(String word) {
        return word.replace("'", "");
    }

    private List<String> matchConcepts() {
        List<String> matchedCon = new ArrayList<>();

        return  matchedCon;
    }


    public void setConceptName(String conceptName) {
        this.conceptName.add(conceptName);
    }

    //public Map<Set<String>, Map<Tree<Set<String>>, BigDecimal>> getBackTraceTable() {
       // return backTraceTable;
    //}

}
