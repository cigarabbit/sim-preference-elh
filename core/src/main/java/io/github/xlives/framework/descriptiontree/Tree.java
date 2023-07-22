package io.github.xlives.framework.descriptiontree;

import io.github.xlives.enumeration.TraversalStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Tree<T> {

    private static final int DEFAULT_ROOT_LEVEL = 0;

    private String label;
    private Map<Integer, TreeNode<T>> nodes;
    private TraversalStrategy traversalStrategy;
    private int runningNodeId;

    public Tree(String label) {
        this(label, TraversalStrategy.BREADTH_FIRST_SEARCH);
    }

    public Tree(String label, TraversalStrategy traversalStrategy) {
        this.label = label;
        this.nodes = new HashMap<Integer, TreeNode<T>>();
        this.traversalStrategy = traversalStrategy;
        this.runningNodeId = 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Protected ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected String toString(StringBuilder builder, int id, int depth) {
        if (depth == DEFAULT_ROOT_LEVEL) {
            builder.append("\n");

            // Handle specific types of data
            if (nodes.get(id).getData() instanceof Set) {
                builder.append("{");
                builder.append(nodes.get(id).toString());
                builder.append("}");
            }

            else {
                builder.append(nodes.get(id).getData().toString());
            }

            builder.append("\n");
        }

        else {
            String tabs = String.format("%0" + depth + "d", 0);
            builder.append(StringUtils.replace(tabs, "0", "\t"));
            builder.append("<");
            builder.append(nodes.get(id).getEdgeToParent());
            builder.append(">");

            // Handle specific types of data
            if (nodes.get(id).getData() instanceof Set) {
                builder.append("{");
                builder.append(nodes.get(id).toString());
                builder.append("}");
            }

            else {
                builder.append(nodes.get(id).getData().toString());
            }

            builder.append("\n");
        }

        List<TreeNode<T>> children = nodes.get(id).getChildren();
        for (TreeNode<T> child : children) {

            // Recursive call
            this.toString(builder, child.getId(), depth + 1);
        }

        return builder.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public TreeNode<T> addNode(String edge, TreeNode<T> parentNode, T childNodeData) {
        TreeNode<T> node;

        // If it is not the root
        if (edge != null && parentNode != null) {
            this.runningNodeId++;
            node = parentNode.addChild(edge, childNodeData, runningNodeId);
        }

        // Otherwise,
        else {
            node = new TreeNode<T>(edge, childNodeData, runningNodeId);
        }

        this.nodes.put(node.getId(), node);



        return node;
    }

    public String toString(int id) {
        StringBuilder builder = new StringBuilder();

        return this.toString(builder, id, DEFAULT_ROOT_LEVEL);
    }

    public Iterator<TreeNode<T>> iterator(int nodeId) {
        return this.iterator(nodeId, this.traversalStrategy);
    }

    public Iterator<TreeNode<T>> iterator(int nodeId, TraversalStrategy traversalStrategy) {
        switch (traversalStrategy) {
            case BREADTH_FIRST_SEARCH:
                return new BreadthFirstTreeIterator<T>(nodes, nodeId);

            case DEPTH_FIRST_SEARCH:
                return new DepthFirstTreeIterator<T>(nodes, nodeId);

            default:
                return new BreadthFirstTreeIterator<T>(nodes, nodeId);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters and Setters /////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getLabel() {
        return label;
    }

    public Map<Integer, TreeNode<T>> getNodes() {
        return nodes;
    }

    public TraversalStrategy getTraversalStrategy() {
        return traversalStrategy;
    }
}
