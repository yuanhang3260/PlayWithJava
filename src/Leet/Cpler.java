package Leet;

import java.lang.*;
import java.util.*;

/**
 *  (1) Flatten Binary Tree to Linked List
 *  (2) Remove Duplicates from Sorted List II
 */

public class Cpler {
    /* List Node inner class */
    static class ListNode {
        int val;
        ListNode next;
        
        ListNode(int x) {
           val = x;
           next = null;
       }
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    public static ListNode makeList(int[] num) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        for (int i = 0; i < num.length; i++) {
            tail.next = new ListNode(num[i]);
            tail = tail.next;
        }
        return dummy.next;
    }

    public static void printList(ListNode head) {
        while (head != null) {
            System.out.printf("%d  ", head.val);
            head = head.next;
        }
        System.out.println();
    }

    /**************************************************************************/
    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        
        TreeNode dummy = new TreeNode(0);
        flattenHelper(root, dummy);
    }
    
    private TreeNode flattenHelper(TreeNode root, TreeNode tail) {
        if (root == null) {
            return tail;
        }
        tail.right = root;
        tail = root;
        tail = flattenHelper(root.left, tail);
        tail = flattenHelper(root.right, tail);
        return tail;
    }
}