package io.github.xlives.framework;

import io.github.xlives.framework.descriptiontree.TreeNode;

import java.math.BigDecimal;
import java.util.*;

public class BackTraceTable {
    // Set<String> (Key) -> List<Map<TreeNode<Set<String>>, BigDecimal>> (Value)
    private Map<Set<String>, List<Map<String, Map<TreeNode<Set<String>>, BigDecimal>>>> backTraceTable = new HashMap<>();

    private Set<String> cnPair = new HashSet<>();

    private String concept1;
    private String concept2;

    // method to input values for the innermost list of tree nodes
    public void inputTreeNodeValue(TreeNode<Set<String>> node, BigDecimal values, int order) {
        Map<TreeNode<Set<String>>, BigDecimal> treeNodeMap = new HashMap<>();
        treeNodeMap.put(node, values);

        // get the existing list of maps (innermost list) or create a new one if not present
        List<Map<String, Map<TreeNode<Set<String>>, BigDecimal>>> innerList = backTraceTable.computeIfAbsent(cnPair, k -> new ArrayList<>());

        // create the inner map and add it to the list
        Map<String, Map<TreeNode<Set<String>>, BigDecimal>> innerMap = new HashMap<>();
        if (order == 1){
            innerMap.put(concept1, treeNodeMap);
        } else {
            innerMap.put(concept2, treeNodeMap);
        }
        innerList.add(innerMap);
    }

    // method to input a key for the outermost HashMap
    public void inputConceptName(String concept1, String concept2) {
        this.concept1 = concept1;
        this.concept2 = concept2;

        cnPair.add(concept1);
        cnPair.add(concept2);
    }

    public String getFirstElement(Set<String> set) {
        Iterator<String> iterator = set.iterator();

        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }

    public Map<Set<String>, List<Map<String, Map<TreeNode<Set<String>>, BigDecimal>>>> getBackTraceTable() {
        return backTraceTable;
    }
}
