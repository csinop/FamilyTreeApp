package com.example.familytreeapp;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph<T> {
    private final Set<Node<T>> nodes = new HashSet<>();

    //methods
    public void addNode(Node<T> nodeToAdd){ nodes.add(nodeToAdd); }

    public Node<T> getLowestDistanceNode(Set<Node<T>> unvisitedNodes){
        Node<T> lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;

        for(Node<T> node : unvisitedNodes){
            int nodeDistance = node.getDistance();

            if(nodeDistance < lowestDistance){
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private void calculateMinimumDistance(Node<T> evaluationNode,
                                          Integer edgeWeigh, Node<T> sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node<T>> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    //Djikstra's Algorithm
    //finds the closest path from the "startingNode" to the last node
    public Graph<T> djikstraTheGraph(Graph<T> graph, Node<T> startingNode){
        startingNode.setDistance(0);

        Set<Node<T>> visitedNodes = new HashSet<>();
        Set<Node<T>> unvisitedNodes = new HashSet<>();

        unvisitedNodes.add(startingNode);

        while(unvisitedNodes.size() != 0){
            Node<T> currentNode = getLowestDistanceNode(unvisitedNodes);
            unvisitedNodes.remove(currentNode);

            for(Map.Entry< Node<T>, Integer> adjacencyPair: currentNode.getAdjacentNodes().entrySet()){
                Node<T> adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if(!visitedNodes.contains(adjacentNode)){
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unvisitedNodes.add(adjacentNode);
                }
            }
            visitedNodes.add(currentNode);
        }
        return graph;
    }
    //Graph class getters and setters
    public Set<Node<T>> getNodes() {
        return nodes;
    }

    //node class
    static class Node<T> {
        private final T item;
        private List<Node<T>> shortestPath = new LinkedList<>();
        private Integer distance = Integer.MAX_VALUE;
        Map<Node<T>, Integer> adjacentNodes = new HashMap<>();

        //constructor
        public Node(T item){ this.item = item; }

        //methods
        public void addDestination(Node<T> destination, int distance){
            adjacentNodes.put(destination, distance);
        }

        //getters and setters
        private int getDistance() { return distance; }
        public void setDistance(int distance) { this.distance = distance; }

        public List<Node<T>> getShortestPath() {
            return shortestPath;
        }
        public void setShortestPath(List<Node<T>> shortestPath){
            this.shortestPath = shortestPath;
        }
        public Map<Node<T>, Integer> getAdjacentNodes() {
            return adjacentNodes;
        }
        public void setAdjacentNodes(Map<Node<T>, Integer> adjacentNodes) {
            this.adjacentNodes = adjacentNodes;
        }

        public T getItem() {
            return item;
        }
    }
}

