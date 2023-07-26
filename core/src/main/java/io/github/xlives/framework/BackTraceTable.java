package io.github.xlives.framework;

import io.github.xlives.framework.descriptiontree.Tree;
import io.github.xlives.framework.descriptiontree.TreeNode;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class BackTraceTable {
    private Map<Map<Integer, String[]>, List<Map<String, Map<TreeNode<Set<String>>, BigDecimal>>>> backTraceTable = new HashMap<>();

    private static Integer index = 0;
    // debuh
    public File output_file = new File("/Users/rchn/Desktop/backtracetable");
    public StringBuilder test = new StringBuilder();

    private String[] cnPair = new String[2];

    // method to input values for the innermost list of tree nodes
    public void inputTreeNodeValue(Tree<Set<String>> node, BigDecimal values, int order) throws IOException {

        if (output_file.exists()) {
            output_file.delete();
        }

        Map<TreeNode<Set<String>>, BigDecimal> treeNodeMap = new HashMap<>();
        Map<String, Map<TreeNode<Set<String>>, BigDecimal>> innerMap = new HashMap<>();
        List<Map<String, Map<TreeNode<Set<String>>, BigDecimal>>> innerList = new ArrayList<>();

        treeNodeMap.put(node.getNodes().get(0), values);

        if (order == 1){
            innerMap.put(cnPair[0], treeNodeMap);
        } else {
            innerMap.put(cnPair[1], treeNodeMap);
        }
        //test.append("=================\n");

        innerList.add(innerMap);
        // test.append(cnPair[0]).append(cnPair[1]).append("\n").append(innerMap.values()).append("\n");

        backTraceTable.put(setKeyMap(index, cnPair), innerList);

        test.append(index).append(backTraceTable).append("\n");

        index++;

        FileUtils.writeStringToFile(output_file, test.toString(), true);

    }

    // method to input a key for the outermost HashMap
    public void inputConceptName(String concept1, String concept2) {
        cnPair[0] = concept1;
        cnPair[1] = concept2;
    }

    public Map<Integer, String[]> setKeyMap(Integer index, String[] cnPair) {
        Map<Integer, String[]> outermostMap = new HashMap<>();
        outermostMap.put(index, cnPair);
        return outermostMap;
    }

    public Map<Map<Integer, String[]>, List<Map<String, Map<TreeNode<Set<String>>, BigDecimal>>>> getBackTraceTable() {
        return backTraceTable;
    }
}
