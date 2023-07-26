package io.github.xlives.service;

import io.github.xlives.framework.BackTraceTable;
import io.github.xlives.framework.descriptiontree.Tree;
import io.github.xlives.framework.descriptiontree.TreeNode;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ExplanationService {

    private static StringBuilder explanation = new StringBuilder();

    private File output_file = new File("/Users/rchn/Desktop/simservice");
    public File backtrace_file = new File("/Users/rchn/Desktop/backtracetable");

    public void explainSimilarity(BackTraceTable backTraceTable) throws IOException {

        if (output_file.exists() && backtrace_file.exists()) {
            output_file.delete();
            backtrace_file.delete();
        }

        BigDecimal degree = null;

        String conceptName1 = "";
        String conceptName2 = "";

        StringBuilder res = new StringBuilder();

        List<String[]> priList = new ArrayList<>();

        int count = 0;

        // iterate through each value in backTraceTable
        for (Map.Entry<Map<Integer, String[]>, Map<String, Map<Tree<Set<String>>, BigDecimal>>> backtrace : backTraceTable.getBackTraceTable().entrySet()) {
            Map<Integer, String[]> keyMap = backtrace.getKey();

            for (int i = 0; i < keyMap.size(); i++) {
                String[] arrayValue = keyMap.get(i);
                if (arrayValue != null) {
                    for (int j = 0; j < arrayValue.length; j++) {
                        if (j == 0) {
                            conceptName1 = arrayValue[0];
                        } else {
                            conceptName2 = arrayValue[1];
                        }
                    }
                }
            }

            // retrieve primitives, existential, and degree
            for (Map.Entry<String, Map<Tree<Set<String>>, BigDecimal>> entry : backtrace.getValue().entrySet()) {
                String key = entry.getKey();
                String exi = "";
                for (Map.Entry<Tree<Set<String>>, BigDecimal> child : entry.getValue().entrySet()){
                    for (Map.Entry<Integer, TreeNode<Set<String>>> tree : child.getKey().getNodes().entrySet()) {
                        exi = tree.getValue().getEdgeToParent();

                        if (exi == null) {
                            exi = "** DO NOT HAVE ANY ROLES **";
                        }
                    }
                    if ( count < 2) {
                        degree = child.getValue();

                        // sets of common primitives
                        String[] priArr = new String[]{child.getKey().getNodes().get(0).toString()};
                        removeUnwantedChar(priArr);
                        priList.add(priArr);

                        res.append("\t").append(key).append(" = ").append(Arrays.toString(priArr))
                                .append(", ").append(exi)
                                .append("\n");
                    }
                    count++;

                }
            }

        }

        Set<String> matching = findMatchingWords(priList);

        explanation.append("The similarity between ").append(conceptName1).append(" and ").append(conceptName2)
                .append(" is ").append(degree.setScale(5, BigDecimal.ROUND_HALF_UP));
        explanation.append(" because they have ").append(matching).append(" in common.");
        explanation.append("\n").append(res);

        FileUtils.writeStringToFile(backtrace_file, backTraceTable.getBackTraceTable().toString(), true);
        FileUtils.writeStringToFile(output_file, explanation.toString(), false);
    }

    private void removeUnwantedChar(String[] wordsArray) {
        for (int i = 0; i < wordsArray.length; i++) {
            wordsArray[i] = wordsArray[i].replaceAll("'", "");
        }
    }

    private static Set<String> findMatchingWords(List<String[]> wordsList) {
        Set<String> matchingWords = new HashSet<>();

        if (!wordsList.isEmpty()) {
            String[] concept1 = wordsList.get(0);
            String[] concept2 = wordsList.get(1);

            int c1_length = concept1.length;
            int c2_length = concept2.length;

            for (int i = 0; i < c1_length; i++) {
                String[] conName1 = concept1[i].split("\\s+");

                for (int j = 0; j < c2_length; j++) {
                    String[] conName2 = concept2[i].split("\\s+");

                    for (String cn1 : conName1) {
                        for (String cn2 : conName2) {
                            if (cn1.equals(cn2)) {
                                matchingWords.add(cn1);
                            }
                        }
                    }
                }
            }
        }

        return matchingWords;
    }

}