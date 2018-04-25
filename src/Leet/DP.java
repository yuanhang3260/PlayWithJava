package Leet;

import java.lang.*;
import java.util.*;

/** Graph List and DP and Recursive Problems:
  * (1) word break II
  * (2) parlindrom partition II
  * (3) combination
  * (4) subsets II
  * (5) permute II
  * (6) word ladder I & II
  * (7) jump game II
  */
public class DP {
    /* word break II - Bad Method */
    public List<String> wordBreak_naive(String s, Set<String> dict) {
        ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
        if (s == null) {
            return new ArrayList<String>();
        }
        if (dict == null || dict.size() == 0) {
            return new ArrayList<String>();
        }

        ArrayList<String> list = new ArrayList<String>();
        list.add("");
        results.add(list);
        
        for (int i = 1; i <= s.length(); i++) {
            for (int j = i; j >= 1; j++) {
                if (dict.contains(s.substring(j-1, i))) {
                    ArrayList<String> newList = new ArrayList<String>();
                    for (String str : results.get(j-1)) {
                        newList.add(str + s.substring(j-1, i) + " ");
                    }
                    results.add(newList);
                }
            }
        }
        
        ArrayList<String> re = new ArrayList<String>();
        for (String str : results.get(s.length())) {
            re.add(str.trim());
        }
        
        return re;
    }
    /* word break - Good Method */
    public List<String> wordBreak(String s, Set<String> dict) {
        ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
        if (s == null) {
            return new ArrayList<String>();
        }
        if (dict == null || dict.size() == 0) {
            return new ArrayList<String>();
        }
        Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        return _wordBreak(s,dict,map);
    }
    
    private ArrayList<String> _wordBreak(String s, Set<String> dict, Map<String, ArrayList<String>> map) {
        if (map.containsKey(s)) {
            return map.get(s);
        }
        ArrayList<String> result = new ArrayList<String>();
        int length = s.length();
        for (int i = 0; i < length; i++) {
            String prefix = s.substring(0, i + 1);
            if(dict.contains(prefix)) {
                ArrayList<String> tail = _wordBreak(s.substring(i+1, length), dict, map);
                for (String str : tail) {
                    result.add(prefix + str + " ");
                }
            }
        }
        map.put(s, result);
        return result;
    }

    public int minCut(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        int[] cut = new int[s.length() + 1];
        cut[0] = 0;
        boolean[][] isPalindrome = getIsPalindrome(s);
        for (int i = 1; i <= s.length(); i++) {
            cut[i] = Integer.MAX_VALUE;
            for (int j = i - 1; j >= 0; j--) {
                if (isPalindrome[j][i - 1]) {
                    cut[i] = Math.min(cut[i], cut[j] + 1);
                }
            }
        }
        return cut[s.length()] - 1;
    }
    
    private boolean[][] getIsPalindrome(String s) {
        boolean[][] m = new boolean[s.length()][s.length()];
        for (int i = 0; i < s.length(); i++) {
            m[i][i] = true;
            if (i < s.length() - 1) {
                m[i][i+1] = ( s.charAt(i) == s.charAt(i+1) );
            }
        }
        for (int len = 3; len <= s.length(); len++) {
            for (int i = 0; i <= (s.length() - len); i++) {
                m[i][i + len - 1] = (s.charAt(i) == s.charAt(i+len-1)) && (m[i+1][i+len-2]);
            }
        }
        return m;
    }

    public String longestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        
        int max = 1;
        int start = 0, end = 0;
        /* generate the palindrom matrix */
        boolean[][] matrix = new boolean[s.length()][s.length()];
        for (int i = 0; i < s.length(); i++) {
            matrix[i][i] = true;
            if (i < s.length() - 1) {
                matrix[i][i+1] = (s.charAt(i) == s.charAt(i+1));
                if (matrix[i][i+1]) {
                    max = 2;
                    start = i;
                    end = i + 1;
                }
            }
        }
        
        for (int len = 3; len <= s.length(); len++) {
            for (int i = 0; i <= s.length() - len; i++) {
                matrix[i][i+len-1] = (s.charAt(i) == s.charAt(i+len-1)) && (matrix[i+1][i+len-2]);
                if (matrix[i][i+len-1] && len > max) {
                    max = len;
                    start = i;
                    end = i+len-1;
                }
            }
        }
        return s.substring(start, end+1);
    }

    public ArrayList<ArrayList<Integer>> subsets(int[] S) {
        ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
        results.add(new ArrayList<Integer>());
        
        Arrays.sort(S);
        for (int i = 0; i < S.length; i++) {
            ArrayList<ArrayList<Integer>> newresults = new ArrayList<ArrayList<Integer>>();
            newresults.addAll(results);
            
            for (ArrayList<Integer> list : results) {
                ArrayList<Integer> newlist = new ArrayList<Integer>();
                newlist.addAll(list);
                newlist.add(S[i]);
                newresults.add(newlist);
            }
            results = newresults;
        }
        return results;
    }

    public ArrayList<ArrayList<Integer>> combine(int n, int k) {
        ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
        int[] solution = new int[k];
        
        _combine(results, solution, n, k, 0, 1);
        return results;
    }
    
    private void _combine(ArrayList<ArrayList<Integer>> results, 
                     int[] solution,
                     int n, int k, int index, int start) 
    {
        if (n - start < k - 1 - index) {
            return;
        }
        
        if (index == k) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i: solution) {
                list.add(i);
            }
            results.add(list);
            return;
        }
        
        for (int i = start; i <= n; i++) {
            solution[index] = i;
            _combine(results, solution, n, k, index+1, i+1);
        }
    }

    public ArrayList<ArrayList<Integer>> subsetsWithDup(int[] num) {
        ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> list = new ArrayList<Integer>();
        results.add(list);
        
        Arrays.sort(num);
        _subsetsWithDup(num, 0, list, results);
        return results;
    }
    
    private void _subsetsWithDup(int[] num, int index, ArrayList<Integer> list, ArrayList<ArrayList<Integer>> results) {
        /* end case */
        if (index == num.length) {
            return;
        }
        
        for (int i = index; i < num.length; i++) {
            list.add(num[i]);
            /* add current subset */
            results.add(new ArrayList<Integer>(list));
            /* recurse to add more elements */
            _subsetsWithDup(num, i+1, list, results);
            list.remove(list.size()-1);
            while (i < num.length - 1 && num[i] == num[i+1]) {
                i++;
            }
        }
    }

    public ArrayList<ArrayList<Integer>> permuteUnique(int[] num) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        if (num == null || num.length == 0) {
            return result; 
        }
        
        Arrays.sort(num);
        ArrayList<Integer> list = new ArrayList<Integer>();
        boolean[] visited = new boolean[num.length];
        _permuteUnique(num, visited, list, result);
        return result;
    }
    
    private void _permuteUnique(int[] num, boolean[] visited, ArrayList<Integer> list, ArrayList<ArrayList<Integer>> results) {
        if (list.size() == num.length) {
            results.add(new ArrayList<Integer>(list));
            return;
        }
        
        for (int i = 0; i < num.length; i++) {
            if(visited[i] == true || (i > 0 && num[i] == num[i-1] && visited[i - 1] == false)) {
                continue;
            }
            list.add(num[i]);
            visited[i] = true;
            _permuteUnique(num, visited, list, results);
            list.remove(list.size()-1);
            visited[i] = false;
        }
    }

    public boolean exist(char[][] board, String word) {
        if (board == null || board.length == 0) {
            return false;
        }
        if (word == null || word.length() == 0) {
            return true;
        }
        
        boolean[][] visited = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; i++) {
                if (board[i][j] == word.charAt(0)) {
                    visited[i][j] = true;
                    if (_exist(board, word, 1, i, j, visited)) {
                        return true;
                    }
                    visited[i][j] = false;
                }
            }
        }
        return false;
    }
    
    private boolean _exist(char[][] board, String word, int index, int i, int j, boolean[][] visited) {
        if (index == word.length()) {
            return true;
        }
        /* check upper position */
        if (i > 0 && !visited[i-1][j] && board[i-1][j] == word.charAt(index)) {
            visited[i-1][j] = true;
            if (_exist(board, word, index+1, i-1, j, visited)) {
                return true;
            }
            visited[i-1][j] = false;
        }
        /* check down position */
        if (i < board.length - 1 && !visited[i+1][j] && board[i+1][j] == word.charAt(index)) {
            visited[i+1][j] = true;
            if (_exist(board, word, index+1, i+1, j, visited)) {
                return true;
            }
            visited[i+1][j] = false;
        }
        /* check left position */
        if (j > 0 && !visited[i][j-1] && board[i][j-1] == word.charAt(index)) {
            visited[i][j-1] = true;
            if (_exist(board, word, index+1, i, j-1, visited)) {
                return true;
            }
            visited[i][j-1] = false;
        }
        /* check right position */
        if (j < board[0].length - 1 && !visited[i][j+1] && board[i][j+1] == word.charAt(index)) {
            visited[i][j+1] = true;
            if (_exist(board, word, index+1, i, j+1, visited)) {
                return true;
            }
            visited[i][j+1] = false;
        }
        return false;
    }

    ArrayList<ArrayList<String>> answer;
    
    private class Node {
        String val;
        int no;
        ArrayList<Node> prev;
        public Node(int no, String s) {
            this.val = s;
            this.no = no;
            prev = new ArrayList<Node>();
        }
        public void addPrev(Node node) {
            this.prev.add(node);
        }
    }

    public void findPath(Node node, ArrayList<String> cur, String start) {
        if (node.val.equals(start)) {
            answer.add(cur);
            return;
        }
        ArrayList<String> temp;
        for (Node n : node.prev) {
            temp = new ArrayList<String>(cur);
            temp.add(0, n.val);
            findPath(n, temp, start);
        }
    }

    public ArrayList<ArrayList<String>> findLadders(String start, String end, HashSet<String> dict) {
        HashMap<String, Node> map = new HashMap<String, Node>();
        Queue<Node> queue = new LinkedList<Node>();
        Node node = new Node(0, start);
        Node endNode = null;
        map.put(start, node);
        queue.add(node);
        boolean stop = false;
        while (queue.size() > 0 && !stop) {
            int count = queue.size();
            for (int i = 0; i < count; i++) {
                node = queue.poll();
                for (int j = 0; j < node.val.length(); j++) {
                    StringBuilder t = new StringBuilder(node.val);
                    for (char k = 'a'; k <= 'z'; k++) {
                        t.setCharAt(j, k);
                        if (dict.contains(t.toString())) {
                            Node v = map.get(t.toString());
                            if (v == null) {
                                Node temp = new Node(node.no + 1, t.toString());
                                temp.addPrev(node);
                                queue.add(temp);
                                map.put(t.toString(), temp);
                                if (t.toString().equals(end)) {
                                    endNode = temp;
                                    stop = true;
                                }
                            }
                            else {
                                if (v.no == node.no + 1) {
                                    v.addPrev(node);
                                }
                            }
                        }
                    }
                }
            }
        }
        answer = new ArrayList<ArrayList<String>>();
        if (endNode != null) {
            findPath(endNode, new ArrayList<String>(Arrays.asList(end)), start);
        }
        return answer;
    }


    public static int jump(int[] A) {
        int[] steps = new int[A.length];
        
        steps[0] = 0;
        for (int i = 1; i < A.length; i++) {
            steps[i] = Integer.MAX_VALUE;
            for (int j = 0; j < i; j++) {
                if (steps[j] != Integer.MAX_VALUE && A[j] >= (i-j)) {
                    steps[i] = steps[j] + 1;
                    break;
                }
            }
        }
        
        return steps[A.length - 1];
    }
    
    private static int jumpHelper(int[] a, int[] results, int index) {
        //System.out.println(index);
        if (results[index] >= 0) {
            return results[index];
        }
        
        int min = Integer.MAX_VALUE;
        for (int i = index - 1; i >= 0; i--) {
            if (a[i] >= (index - i)) {
                min = Math.min(min, jumpHelper(a, results, i));
            }
        }
        if (min != Integer.MAX_VALUE) {
            results[index] = min + 1;
            return results[index];
        }
        else {
            results[index] = Integer.MAX_VALUE;
            return Integer.MAX_VALUE;
        }
    }

    public int minPathSum(int[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        
        int[][] result = new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                result[i][j] = -1;
            }
        }
        
        _minPathSum(grid, result, grid.length, grid[0].length);
        return result[grid.length-1][grid[0].length-1];
    }
    
    private void _minPathSum(int[][] grid, int[][] result, int row, int col) {
        if (row == 0 && col == 0) {
            result[0][0] = grid[0][0];
        }
        if (result[row][col] != -1) {
            return;
        }
        
        int left = Integer.MAX_VALUE, up = Integer.MAX_VALUE;
        if (row > 0) {
            _minPathSum(grid, result, row-1, col);
            left = result[row-1][col];
        }
        if (col > 0) {
            _minPathSum(grid, result, row, col-1);
            up = result[row][col-1];
        }
        result[row][col] = Math.min(left, up) + grid[row][col];
    }

    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        if (obstacleGrid.length == 0 || obstacleGrid[0].length == 0 || obstacleGrid[0][0] == 1) {
            return 0;
        }
        
        int[][] result = new int[obstacleGrid.length][obstacleGrid[0].length];
        result[0][0] = 1;
        for (int i = 1; i < obstacleGrid.length; i++) {
            if (obstacleGrid[0][i] == 1) {
                result[0][i] = 0;
            }
            else {
                result[0][i] = result[0][i-1];
            }
        }
        for (int i = 1; i < obstacleGrid.length; i++) {
            if (obstacleGrid[i][0] == 1) {
                result[i][0] = 0;
            }
            else {
                result[i][0] = result[i-1][0];
            }
        }
        for (int i = 1; i < obstacleGrid.length; i++) {
            for (int j = 1; j < obstacleGrid[0].length; j++) {
                if (obstacleGrid[i][j] == 1) {
                    result[i][j] = 0;
                }
                else {
                    result[i][j] = result[i-1][j] + result[i][j-1];
                }
            }
        }
        return result[obstacleGrid.length - 1][obstacleGrid[0].length - 1];
    }

    public ArrayList<ArrayList<Integer>> combinationSum(int[] candidates, int target) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        if (candidates == null || candidates.length == 0) {
            return result;
        }
        
        Arrays.sort(candidates);
        return combinationSumHelper(candidates, target, 0);
    }
    
    ArrayList<ArrayList<Integer>> combinationSumHelper(int[] candidates, int target, int index) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        if (index == candidates.length) {
            if (target == 0) {
                result.add(new ArrayList<Integer>());
            }
            return result;
        }
        
        for (int i = 0; i <= (int)(target / candidates[index]); i++) {
            ArrayList<ArrayList<Integer>> sub_result = 
                combinationSumHelper(candidates, target - i * candidates[index], get_next(candidates, index));
            for (ArrayList<Integer> list : sub_result) {
                ArrayList<Integer> newlist = new ArrayList<Integer>();
                for (int j = 0; j < i; j++) {
                    newlist.add(candidates[index]);
                }
                newlist.addAll(list);
                result.add(newlist);
            }
        }
        return result;
    }
    
    private int get_next(int[] candidates, int index) {
        int i = index;
        while (i < candidates.length && candidates[i] == candidates[index]) {
            i++;
        }
        return i;
    }

    public static ArrayList<ArrayList<Integer>> combinationSum2(int[] num, int target) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        if (num == null || num.length == 0) {
            return result;
        }
        
        Arrays.sort(num);
        ArrayList<Integer> solution = new ArrayList<Integer>();
        combinationSumHelper2(num, target, 0, solution, result);
        return result;
    }
    
    private static void combinationSumHelper2(int[] candidates, int target, int index, 
                            ArrayList<Integer> solution, ArrayList<ArrayList<Integer>> result) {
        if (target == 0) {
           result.add(new ArrayList<Integer>(solution));
           return;
        }
        else if (index == candidates.length) {
            return;
        }
        
        int num = 0, i = index;
        while (i < candidates.length && candidates[i] == candidates[index]) {
            i++; num++;
        }
        
        for (i = 0; i <= Math.min(num, (int)(target / candidates[index])); i++) {
            for (int j = 0; j < i; j++) {
                solution.add(candidates[index]);
            }
            combinationSumHelper2(candidates, target - candidates[index] * i, index + num, solution ,result);
            for (int j = 0; j < i; j++) {
                solution.remove(solution.size() - 1);
            }
        }
    }

    public static ArrayList<Integer> grayCode(int n) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        result.add(0);
        if (n == 0) {
            return result;
        }
        
        result.add(1);
        if (n <= 1) {
            return result;
        }
        
        for (int i = 2; i <= n; i++) {
            ArrayList<Integer> newresult = new ArrayList<Integer>();
            newresult.addAll(result);
            
            ArrayList<Integer> reversed = reverse(result);
            for (Integer ele : reversed) {
                newresult.add(ele + (1<<(i-1)));
            }
            result = newresult;
            //System.out.println(result.toString());
        }
        return result;
    }
    
    public static ArrayList<Integer> reverse (ArrayList<Integer> r) {
        ArrayList<Integer> rev = new ArrayList<Integer>();
        for (int i = r.size() - 1; i >= 0; i--) {
            rev.add(r.get(i));
        }
        return rev;
    }

    public int totalNQueens(int n) {
        if (n == 0) {
            return 0;
        }

        boolean[][] board = new boolean[n][n];
        ArrayList<Integer> solution = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        totalNQueensHelper(board, 0, solution, result);
        return result.size();
    }

    private void totalNQueensHelper(boolean[][] board, int row, 
                                    ArrayList<Integer> solution, ArrayList<ArrayList<Integer>> result)
   {
       if (row == board.length) {
           result.add(new ArrayList<Integer>(solution));
           return;
       }

       for (int col = 0; col < board.length; col++) {
           if (checkQueen(board, row, col)) {
               board[row][col] = true;
               solution.add(col);

               totalNQueensHelper(board, row + 1, solution, result);
               
               solution.remove(solution.size()-1);
               board[row][col] = false;
           }
       }
       return;
   }

    private boolean checkQueen(boolean[][] board, int row, int col) {
        int size = board.length;
        /* check row */
        for (int i = 0; i < size && i != col; i++) {
            if (board[row][i]) {
                return false;
            }
        }
        /* check col */
        for (int i = 0; i < size && i != row; i++) {
            if (board[i][col]) {
                return false;
            }
        }
        /* check diagnol */
        for (int i = 1; row + i < size && col + i < size; i++) {
            if (board[row+i][col+i]) {
                return false;
            }
        }
        for (int i = 1; row - i >= 0 && col - i >= 0; i++) {
            if (board[row-i][col-i]) {
                return false;
            }
        }
        for (int i = 1; row + i < size && col - i >= 0; i++) {
            if (board[row+i][col-i]) {
                return false;
            }
        }
        for (int i = 1; row - i >= 0 && col + i < size; i++) {
            if (board[row-i][col+i]) {
                return false;
            }
        }
        return true;
    }

    public static int numDistinct(String s, String t) {
        if (s == null || t == null || 
            s.length() == 0 || t.length() == 0 || s.length() < t.length()) {
            return 0;
        }
        char[] s_char = s.toCharArray();
        char[] t_char = t.toCharArray();
        return numDistinctHelper(s_char, 0, t_char, 0);
    }

    private static int numDistinctHelper(char[] s_char, int s_start, char[] t_char, int t_start) {
        if (t_char.length == t_start) {
            return 1;
        }
        else if (s_char.length == s_start) {
            return 0;
        }

        int num = 0;
        char c = t_char[t_start];
        for (int j = s_start; j < s_char.length; j++) {
            if (s_char[j] == c) {
                num += numDistinctHelper(s_char, j + 1, t_char, t_start + 1);
            }
        }
        return num;
    }

    public static int numDistinct2(String S, String T) {
        if (S == null || T == null) {
            return 0;
        }

        int[][] nums = new int[S.length() + 1][T.length() + 1];

        for (int i = 0; i < S.length(); i++) {
            nums[i][0] = 1;
        }
        for (int i = 1; i <= S.length(); i++) {
            for (int j = 1; j <= T.length(); j++) {
                nums[i][j] = nums[i - 1][j];
                if (S.charAt(i - 1) == T.charAt(j - 1)) {
                    nums[i][j] += nums[i - 1][j - 1];
                }
            }
        }
        return nums[S.length()][T.length()];
    }


    public static ArrayList<String> restoreIpAddresses(String s) {
        ArrayList<String> result = new ArrayList<String>();
        if (s == null || s.length() == 0) {
            return result;
        }

        String[] solution = new String[4];
        restoreIpAddressesHelper(s, 0, 0, solution, result);
        return result;
    }

    private static void restoreIpAddressesHelper(String s, int index, int size,
                          String[] solution, ArrayList<String> result) {
        if (index == s.length() && size == 4) {
            result.add(solution[0] + "." + solution[1] + "." + 
                   solution[2] + "." + solution[3]);
            return;
        }
        else if (index == s.length() || size == 4) {
            return;
        }
        
        /* try 1 digit */
        int num = Integer.parseInt(s.substring(index, index + 1));
        solution[size] = s.substring(index, index + 1);
        size++;
        restoreIpAddressesHelper(s, index + 1, size, solution, result);
        size--;

        if (num == 0) {
            return;
        }

        /* try 2 digits */
        if (index + 2 <= s.length()) {
            solution[size] = s.substring(index, index + 2);
            size++;
            restoreIpAddressesHelper(s, index + 2, size, solution, result);
            size--;
        }

        /* try 3 digits */
        if (index + 3 <= s.length()) {
            num = Integer.parseInt(s.substring(index, index + 3));
            if ( num >= 0 && num <= 255) {
                solution[size] = s.substring(index, index + 3);
                size++;
                restoreIpAddressesHelper(s, index + 3, size, solution, result);
                size--;
            }
        }
    }
}
