package Leet;

import java.lang.*;
import java.util.*;

/** Linked List Questions:
  * (1) reverse list in k group
  * (2) merge sort list
  * (3) merge k lists
  * (4) reorder list
  * (5) remove duplicates from list II
  */
public class Llist {

	/* List Node inner class */
	static class ListNode {
		int val;
		ListNode next;
		
		ListNode(int x) {
		   val = x;
		   next = null;
       }
    }

    static class RandomListNode {
        int label;
        RandomListNode next, random;
        RandomListNode(int x) { this.label = x; }
    };


    public static boolean hasCycle(ListNode head) {
        if (head == null) {
            return false;
        }
        
        ListNode dummyhead = new ListNode(-1);
        dummyhead.next = head;
        
        ListNode p1 = dummyhead, p2 = dummyhead;
        while (true) {
            p1 = p1.next;
            p2 = p2.next;
            if (p2 != null) {
                p2 = p2.next;
            }
            if (p1 == p2) {
                return true;
            }
            if (p2 == null) {
                break;
            }
        }
        return false;
    }

    public static ListNode reverseBetween(ListNode head, int m, int n) {
        if (head == null || m >= n) {
        	return head;
        }

        ListNode dummyhead = new ListNode(-1);
        dummyhead.next = head;

        ListNode start = dummyhead;
        for (int i = 0; i < m - 1; i++) {
        	start = start.next;
        }

        ListNode p = start.next;
        for (int i = 0; i < n - m; i++) {
        	ListNode q = p.next;
        	p.next = q.next;
        	q.next = start.next;
        	start.next = q;
        }
        return dummyhead.next;
    }

    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1);
        ListNode last = dummy;
        
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                last.next = l1;
                l1 = l1.next;
            }
            else {
                last.next = l2;
                l2 = l2.next;
            }
            last = last.next;
        }
        if (l1 != null) {
            last.next = l1;
        }
        else {
            last.next = l2;
        }
        return dummy.next;
    }

    private static class ListNodeComparatorByValue implements Comparator<ListNode> {
        public int compare(ListNode left, ListNode right) {
            if (left == null) {
                return 1;
            } else if (right == null) {
                return -1;
            }
            return left.val - right.val;
        }
    }

    public static ListNode mergeKLists(List<ListNode> lists) {
        if (lists == null || lists.size() == 0) {
            return null;
        }

        PriorityQueue<ListNode> heap = new PriorityQueue<ListNode>(lists.size(), new ListNodeComparatorByValue());
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i) != null) {
                heap.add(lists.get(i));
            }
        }

        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;
        while (!heap.isEmpty()) {
            ListNode n = heap.poll();
            tail.next = n;
            tail = n;
            if (n.next != null) {
                heap.add(n);
            }
        }
        return dummy.next;
    }

    public static ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        // get middle ListNode and break the original list
        ListNode slow = head, fast = head.next;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        
        ListNode middle = slow.next;
        slow.next = null;
        
        // recursive sort two lists
        ListNode l1 = sortList(head);
        ListNode l2 = sortList(middle);
        
        // merge two lists
        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                tail.next = l1;
                tail = tail.next;
                l1 = l1.next;
            }
            else {
                tail.next = l2;
                tail = tail.next;
                l2 = l2.next;
            }
        }
        if (l1 != null) {
            tail.next = l1;
        }
        else {
            tail.next = l2;
        }
        return dummy.next;
    }

    public static ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        ListNode start = dummy;
        ListNode p, q;
        while (start.next != null && start.next.next != null) {
            p = start.next;
            q = start.next.next;
            p.next = q.next;
            q.next = p;
            start.next = q;
            start = p;
        }
        return dummy.next;
    }

    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || k <= 1) {
            return head;
        }
        
        int length = 0;
        ListNode it = head;
        while (it != null) {
            length++;
            it = it.next;
        }
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode grp_prehead = dummy;
        for (int i = 0; i < (int)(length / k); i++) {
            ListNode grp_tail = grp_prehead.next;
            for (int j = 0; j < k - 1; j++) {
                ListNode move_node = grp_tail.next;
                grp_tail.next = move_node.next;
                move_node.next = grp_prehead.next;
                grp_prehead.next = move_node;
            }
            grp_prehead = grp_tail;
        }
        return dummy.next;
    }

    public static ListNode insertionSortList(ListNode head) {
        if (head == null || head.next ==null) {
            return head;
        }
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        ListNode p = dummy;
        ListNode q = p.next;
        while ( q != null) {
            ListNode it = dummy;
            while (it.next.val < q.val) {
                it = it.next;
            }
            if (it.next == q) {
                p = p.next;
                q = p.next;
            }
            else {
                p.next = q.next;
                q.next = it.next;
                it.next = q;
                q = p.next;
            }
        }
        return dummy.next;
    }

    public ListNode reorderList(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }
        ListNode middle = findMiddle(head);
        ListNode l2 = middle.next;
        middle.next = null;
        
        // reverse the second half
        l2 = reverseList(l2);
        
        // merge two lists
        ListNode l1 = head;
        ListNode tail = head;
        while (l1 != null && l2 != null) {
            ListNode tmp1 = l1.next;
            ListNode tmp2 = l2.next;
            tail.next = l1;
            l1.next = l2;
            tail = l2;
            
            l1 = tmp1;
            l2 = tmp2;
        }
        if (l1 != null) {
            tail.next = l1;
        }
        return head;
        
    }
    
    private ListNode findMiddle(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode slow = head, fast = head.next;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow.next;
    }
    
    private ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode newhead = null;
        ListNode p = head;
        while (p != null) {
            ListNode q = p.next;
            p.next = newhead;
            newhead = p;
            p = q;
        }
        return newhead;
    }

    public static RandomListNode copyRandomList(RandomListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        HashMap<RandomListNode, RandomListNode> map = new HashMap<RandomListNode, RandomListNode>();
        
        /* shallow copy the list */
        RandomListNode dummy = new RandomListNode(0);
        RandomListNode tail = dummy;
        RandomListNode p = head;
        while (p!= null) {
            RandomListNode newNode = new RandomListNode(p.label);
            newNode.next = newNode.random = null;
            tail.next = newNode;
            map.put(p, newNode);
            p = p.next;
        }
        
        /* copy random pointer */
        RandomListNode it = head;
        RandomListNode new_it = dummy.next;
        while (it != null) {
            RandomListNode newNode = map.get(it);
            RandomListNode newrandom = map.get(it.random);
            newNode.random = newrandom;
            it = it.next;
        }
        return dummy.next;
    }

    public ListNode deleteDuplicates2(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        ListNode it = dummy;
        while (it.next != null) {
            ListNode p = it;
            while (p.next.val == it.next.val) {
                p = p.next;
            }
            if (p != it.next) {
                p.next = it.next;
            }
            else
            {
                it = p;
            }
        }
        return dummy.next;
    }

    public ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        ListNode crt = head;
        while (crt != null) {
            ListNode it = crt;
            while (it.next != null && it.next.val == crt.val) {
                it = it.next;
            }
            crt.next = it.next;
        }
        return head;
    }

    public ListNode rotateRight(ListNode head, int n) {
        if (head == null || head.next == null) {
            return head;
        }
        int length = 0;
        ListNode it = head;
        while (it != null) {
            length++;
            it = it.next;
        }
        
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        it = dummy;
        for (int i = 0; i < length - (n % length); i++) {
            it = it.next;
        }
        
        ListNode insert_point = dummy;
        while (it.next != null) {
            ListNode move_node = it.next;
            it.next = move_node.next;
            move_node.next = insert_point.next;
            insert_point.next = move_node;
            insert_point = move_node;
        }
        return dummy.next;
    }

    public ListNode partition(ListNode head, int x) {
        if (head == null || head.next ==null) {
            return head;
        }
        
        ListNode newtail = head, newmiddle = head;
        ListNode it = head.next;
        head.next = null;
        while (it != null) {
            ListNode tmp = it.next;
            if (it.val >= x) {
                newtail.next = it;
                it.next = null;
            }
            else {
                it.next = newmiddle.next;
                newmiddle = it;
            }
            it = tmp;
        }
        return head;
    }
}
