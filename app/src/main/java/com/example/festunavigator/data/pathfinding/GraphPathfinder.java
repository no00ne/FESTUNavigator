package com.example.festunavigator.data.pathfinding;

import com.example.festunavigator.data.App;
import com.example.festunavigator.data.data_source.TreeNodeDto;
import java.util.*;

public class GraphPathfinder {

    public static List<Integer> findPath(int startId, int endId) {
        // 准备数据：节点映射和邻接表
        List<TreeNodeDto> nodes = App.getGraphRepository().getNodes();
        List<com.example.festunavigator.data.data_source.EdgeDto> edges = App.getGraphRepository().getEdges();
        Map<Integer, TreeNodeDto> nodeMap = new HashMap<>();
        for (TreeNodeDto node : nodes) {
            nodeMap.put(node.id, node);
        }
        Map<Integer, List<Integer>> neighbors = new HashMap<>();
        for (com.example.festunavigator.data.data_source.EdgeDto edge : edges) {
            neighbors.computeIfAbsent(edge.fromId, k -> new ArrayList<>()).add(edge.toId);
            neighbors.computeIfAbsent(edge.toId, k -> new ArrayList<>()).add(edge.fromId);
        }

        // A* 路径查找
        Set<Integer> openSet = new HashSet<>();
        PriorityQueue<Integer> openQueue = new PriorityQueue<>(Comparator.comparingDouble(node -> fScore.getOrDefault(node, Double.POSITIVE_INFINITY)));
        Map<Integer, Integer> cameFrom = new HashMap<>();
        Map<Integer, Double> gScore = new HashMap<>();
        Map<Integer, Double> fScore = new HashMap<>();

        for (Integer nodeId : nodeMap.keySet()) {
            gScore.put(nodeId, Double.POSITIVE_INFINITY);
            fScore.put(nodeId, Double.POSITIVE_INFINITY);
        }
        gScore.put(startId, 0.0);
        fScore.put(startId, heuristic(nodeMap, startId, endId));

        openSet.add(startId);
        openQueue.add(startId);

        while (!openQueue.isEmpty()) {
            int current = openQueue.poll();
            openSet.remove(current);
            if (current == endId) {
                return reconstructPath(cameFrom, current);
            }
            List<Integer> adj = neighbors.getOrDefault(current, Collections.emptyList());
            for (int neighbor : adj) {
                double tentativeG = gScore.get(current) + distance(nodeMap, current, neighbor);
                if (tentativeG < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);
                    fScore.put(neighbor, tentativeG + heuristic(nodeMap, neighbor, endId));
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                        openQueue.add(neighbor);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private static double heuristic(Map<Integer, TreeNodeDto> nodeMap, int id1, int id2) {
        TreeNodeDto n1 = nodeMap.get(id1);
        TreeNodeDto n2 = nodeMap.get(id2);
        if (n1 == null || n2 == null) return 0.0;
        double dx = n1.x - n2.x;
        double dy = n1.y - n2.y;
        double dz = n1.z - n2.z;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    private static double distance(Map<Integer, TreeNodeDto> nodeMap, int id1, int id2) {
        return heuristic(nodeMap, id1, id2);
    }

    private static List<Integer> reconstructPath(Map<Integer, Integer> cameFrom, int current) {
        List<Integer> path = new ArrayList<>();
        path.add(current);
        int node = current;
        while (cameFrom.containsKey(node)) {
            node = cameFrom.get(node);
            path.add(node);
        }
        Collections.reverse(path);
        return path;
    }
}
