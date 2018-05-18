package Leet;

import java.lang.*;
import java.util.*;

/** Graph List Questions:
  * (1) clone graph
  */
public class Graph {
    static class UndirectedGraphNode {
        int label;
        List<UndirectedGraphNode> neighbors;
        UndirectedGraphNode(int x) { label = x; neighbors = new ArrayList<UndirectedGraphNode>(); }
    };

    static public UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
        if (node == null) {
            return null;
        }
        HashMap<UndirectedGraphNode, UndirectedGraphNode> map = 
            new HashMap<UndirectedGraphNode, UndirectedGraphNode>();
        
        LinkedList<UndirectedGraphNode> queue = new LinkedList<UndirectedGraphNode>();
        queue.offer(node);
        
        while (!queue.isEmpty()) {
            UndirectedGraphNode crtnode = queue.poll();
            /* clone a new node */
            UndirectedGraphNode newNode = new UndirectedGraphNode(crtnode.label);
            for (UndirectedGraphNode neighbor : crtnode.neighbors) {
                if (map.containsKey(neighbor)) {
                    newNode.neighbors.add(map.get(neighbor));
                }
                else {
                    UndirectedGraphNode newneighbor = new UndirectedGraphNode(neighbor.label);
                    newNode.neighbors.add(newneighbor);
                    map.put(neighbor, newneighbor);
                    queue.add(newneighbor);
                }
            }
        }
        return map.get(node);
    }
}