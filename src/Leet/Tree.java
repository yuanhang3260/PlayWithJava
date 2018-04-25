package Leet;

import java.lang.*;
import java.util.*;

/** Tree Questions:
  * (1) Binary tree level order traversal
  * (2) Binary Tree Maximum Path Sum
  * (3) Flatten Binary Tree to Linked List
  * (4) Unique Binary Search Trees II
  * (5) Recover Binaray search tree
  * (6) Symmetric tree
  * (7) Recover binary search tree
  * (8) Convert Sorted List to Binary Search Tree
  */
public class Tree {

 	public class TreeNode {
    	int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    /* List Node inner class */
	static class ListNode {
		int val;
		ListNode next;
		
		ListNode(int x) {
		   val = x;
		   next = null;
       }
    }
 

	public List<Integer> preorderTraversal(TreeNode root) {
        ArrayList<Integer> result = new ArrayList<Integer>();

        if (root == null) {
            return result;
        }
        
        _preorderTraversal(root, result);
        return result;
    }
    
    private void _preorderTraversal(TreeNode root, ArrayList<Integer> result) {
        if (root == null) {
            return;
        }
        
        _preorderTraversal(root.left, result);
        result.add(root.val);
        _preorderTraversal(root.right, result);
    }

    public ArrayList<ArrayList<Integer>> levelOrderBottom(TreeNode root) {
        ArrayList<ArrayList<Integer>> result =
            new ArrayList<ArrayList<Integer>>();
        if (root == null) {
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            ArrayList<Integer> level = new ArrayList<Integer>();
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            result.add(0, level);
        }
        return result;
    }

    public ArrayList<ArrayList<Integer>> zigzagLevelOrder(TreeNode root) {
        ArrayList<ArrayList<Integer>> result =
            new ArrayList<ArrayList<Integer>>();
        if (root == null) {
            return result;
        }
        
        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
        queue.add(root);
        
        boolean left_to_right = true;
        while (!queue.isEmpty()) {
            ArrayList<Integer> level = new ArrayList<Integer>();
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (left_to_right) {
                    level.add(node.val);
                }
                else {
                    level.add(0, node.val);
                }
                
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            result.add(level);
            left_to_right = !left_to_right;
        }
        return result;
    }

    private class ResultType1 {
        int singleSum = 0;
        int maxSum = Integer.MIN_VALUE;
    }
    
    public int maxPathSum(TreeNode root) {
        if (root == null) {
            return Integer.MIN_VALUE;
        }
        
        ResultType1 result = _maxPathSum(root);
        return result.maxSum;
    }
    
    private ResultType1 _maxPathSum(TreeNode root) {
        ResultType1 result = new ResultType1();
        if (root == null) {
            return result;
        }
        
        if (root.left == null && root.right == null) {
            result.maxSum = Math.max(result.maxSum, root.val);
            result.singleSum = root.val;
            return result;
        }
        
        ResultType1 left = _maxPathSum(root.left);
        ResultType1 right = _maxPathSum(root.right);
        
        result.singleSum = Math.max(left.singleSum, right.singleSum) + root.val;
        result.singleSum = Math.max(result.singleSum, 0);

        result.maxSum = Math.max(left.maxSum, right.maxSum);
        result.maxSum = Math.max(result.maxSum, root.val + left.singleSum + right.singleSum);
        
        return result;
    }

    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        
        TreeNode dummy = new TreeNode(0);
        _flatten(root, dummy);
    }
    
    private TreeNode _flatten(TreeNode root, TreeNode last) {
        if (root == null) {
            return last;
        }
        
        last.right = root;
        last = root;
        last = _flatten(root.left, last);
        last = _flatten(root.right, last);
        return last;
    }

    private class ResultType2 {
        int depth = 0;
        boolean balanced = true;
        public ResultType2(int depth, boolean balanced) {
            this.depth = depth;
            this.balanced = balanced;
        }
    }
    public boolean isBalanced(TreeNode root) {
        if (root == null) {
            return true;
        }
        
        ResultType2 result = _isBalanced(root);
        return result.balanced;
    }
    
    private ResultType2 _isBalanced(TreeNode root) {
        if (root == null) {
            return (new ResultType2(0, true));
        }
        
        ResultType2 left = _isBalanced(root.left);
        ResultType2 right = _isBalanced(root.right);
        
        int depth = Math.max(left.depth, right.depth);
        boolean balanced = left.balanced && right.balanced && (Math.abs(left.depth - right.depth) <= 1);
        return (new ResultType2(depth, balanced));
    }

    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
        queue.add(root);
        
        int maxDepth = 0;
        while (!queue.isEmpty()) {
            maxDepth++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
        }
        return maxDepth;
    }

    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return false;
        }
        
        ArrayList<Integer> result = new ArrayList<Integer>();
        inOrderTraverse(root, result);
        
        for (int i = 1; i < result.size(); i++) {
            if (result.get(i -1 ) >= result.get(i)) {
                return false;
            }
        }
        return true;
    }
    
    private void inOrderTraverse(TreeNode root, ArrayList<Integer> result) {
        if (root == null) {
            return;
        }
        inOrderTraverse(root.left, result);
        result.add(root.val);
        inOrderTraverse(root.right, result);
    }

    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        if (root.right == null && root.left != null) {
            return false;
        }
        if (root.right != null && root.left == null) {
            return false;
        }
        
        LinkedList<TreeNode> queue1 = new LinkedList<TreeNode>();
        queue1.add(root.left);
        LinkedList<TreeNode> queue2 = new LinkedList<TreeNode>();
        queue2.add(root.right);
        
        while (!queue1.isEmpty() && !queue2.isEmpty()) {
            if (queue1.size() != queue2.size()) {
                return false;
            }
            int size = queue1.size();
            for (int i = 0; i < size; i++) {
                TreeNode node1 = queue1.poll();
                TreeNode node2 = queue2.get(size - 1 -i);
                if (node1.val != node2.val) {
                    return false;
                }
                if ((node1.left == null && node2.left != null) || (node1.left != null && node2.left == null)) {
                    return false;
                }
                
                if (node1.left != null) {
                    queue1.add(node1.left);
                }
                if (node1.right != null) {
                    queue1.add(node1.right);
                }
                if (node2.left != null) {
                    queue2.add(node2.left);
                }
                if (node2.right != null) {
                    queue2.add(node2.right);
                }
            }
        }
        if (!(queue1.isEmpty() && queue2.isEmpty())) {
            return false;
        }
        return true;
    }

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder.length != inorder.length) {
            return null;
        }
        return _buildTree(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1);
    }
    
    private int findPosition(int val, int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == val) {
                return i;
            }
        }
        return -1;
    }
    
    private TreeNode _buildTree(int[] preorder, int prestart, int preend, int[] inorder, int instart, int inend) {
        
        int root = preorder[prestart];
        int inpos = findPosition(root, inorder);
        TreeNode left = _buildTree(preorder, prestart + 1, prestart + inpos, inorder, instart, inpos - 1);
        TreeNode right = _buildTree(preorder, prestart + inpos + 1, preend, inorder, inpos + 1, inend);
        TreeNode root_node = new TreeNode(root);
        root_node.left = left;
        root_node.right = right;
        return root_node;
    }

    public TreeNode sortedListToBST(ListNode head) {
        if (head == null) {
            return null;
        }
        
        return _sortedListToBST(head);
    }
    
    private TreeNode _sortedListToBST(ListNode head) {
        if (head == null) {
            return null;
        }
        
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        TreeNode root = new TreeNode(slow.next.val);
        slow.next = null;
        ListNode right_head = slow.next.next;
        
        root.left = _sortedListToBST(head);
        root.right = _sortedListToBST(right_head);
        return root;
    }

    public TreeNode sortedArrayToBST(int[] num) {
        if (num == null || num.length == 0) {
            return null;
        }
        return _sortedArrayToBST(num, 0, num.length - 1);
    }
    
    private TreeNode _sortedArrayToBST(int[] num, int start, int end) {
        if (start >= end) {
            return (new TreeNode(num[start]));
        }
        
        int middle = start + (int) ((end - start) / 2);
        TreeNode root = new TreeNode(num[middle]);
        root.left = _sortedArrayToBST(num, start, middle - 1);
        root.right = _sortedArrayToBST(num, middle + 1, end);
        return root;
    }


    public int minimumTotal(List<List<Integer>> triangle) {
        if (triangle == null || triangle.size() == 0) {
            return 0;
        }
        
        int[] result = new int[triangle.size()];
        result[0] = triangle.get(0).get(0);
        int min = Integer.MAX_VALUE;
        for (int i = 1; i < triangle.size(); i++) {
            int[] newresult = new int[triangle.size()];
            for (int j = 0; j < i + 1; j++) {
                if (j == 0) {
                    newresult[j] = result[0] + triangle.get(i).get(j);
                }
                else if (j == i) {
                    newresult[j] = result[i - 1] + triangle.get(i).get(j);
                }
                else {
                    newresult[j] = Math.min(result[j-1], result[j]) + triangle.get(i).get(j);
                }
                min = Math.min(min, newresult[j]);
            }
            result = newresult;
        }
        return min;
    }
}