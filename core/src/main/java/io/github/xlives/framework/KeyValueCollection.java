package io.github.xlives.framework;

import io.github.xlives.framework.descriptiontree.TreeNode;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KeyValueCollection {
    private List<TreeNode<Set<String>>> key;
    private Map<TreeNode<Set<String>>, BigDecimal> value = new HashMap<>();

    private Map<List<TreeNode<Set<String>>>, Map<TreeNode<Set<String>>, BigDecimal>> mapKeyVal = new HashMap<>();

    public void setKey(TreeNode<Set<String>> key) {
        this.key.add(key);
    }

    public void setValue(Map<TreeNode<Set<String>>, BigDecimal> value) {
        this.value = value;
    }

    public Map<List<TreeNode<Set<String>>>, Map<TreeNode<Set<String>>, BigDecimal>> returnMapKeyValue() {
        mapKeyVal.put(key, value);
        return mapKeyVal;
    }

}
