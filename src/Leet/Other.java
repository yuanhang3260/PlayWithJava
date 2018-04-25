package Leet;

import java.lang.*;
import java.util.*;

/** Other Questions:
  * (1) generate parentheses
  * (2) Longest Valid Parentheses
  * (3) Longest substring without repeated element
  * (4) Next permutation
  * (5) Minimum window substring
  * (6) Longest Consecutive Sequence
  * (7) Search in rotated sorted array
  */
public class Other {

    public static String simplifyPath(String path) {
        if (path == null) {
            return null;
        }
        
        String[] tokens = path.split("/");
        LinkedList<String> stack = new LinkedList<String>();
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals(".") || tokens[i].equals("")) {
                continue;
            }
            else if (tokens[i].equals("..")) {
                if (stack.isEmpty()) {
                    continue;
                }
                else {
                    stack.remove(stack.size() - 1);
                    continue;
                }
            }
            else {
                stack.add(tokens[i]);
            }
        }
        
        /* start building the simplified path */
        StringBuilder strbuilder = new StringBuilder("/");
        for (int i = 0; i < stack.size(); i++) {
            String name = stack.get(i);
            strbuilder.append(name);
            if (i < stack.size() - 1) {
                strbuilder.append("/");
            }
        }
        return strbuilder.toString();
    }

    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        HashSet<Character> set= new HashSet<Character>();
        int max = 0, start = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!set.contains(c)) {
                set.add(c);
                max = Math.max(max, i - start + 1);
            }
            else {
                while (start < i && s.charAt(start) != c) {
                    start++;
                }
                start++;
            }
        }
        return max;
    }

    public static ArrayList<String> generateParenthesis(int n) {
        ArrayList<String> results = new ArrayList<String>();
        if (n == 0) {
            return results;
        }
        char[] str = new char[2*n];
        _generateParenthesis(n, n, str, results);
        return results;
    }
    
    private static void _generateParenthesis(int left, int right, char[] str, ArrayList<String> results) {
        if (left == 0 && right == 0) {
            results.add(str.toString());
            return;
        }
        int index = str.length - left - right;
        if (left < right) {
            if (left > 0) {
                str[index] = '(';
                _generateParenthesis(left - 1, right, str, results);
            }
            str[index] = ')';
            _generateParenthesis(left, right - 1, str, results);
        }
        else {
            str[index] = '(';
            _generateParenthesis(left - 1, right, str, results);
        }
        return;
    }

    public void nextPermutation(int[] num) {
        if (num == null || num.length <= 1) {
            return;
        }
        
        int last = num[num.length - 1];
        int i = num.length - 1;
        while (i >=1 && num[i] <= num[i-1]) {
            i--;
        }
        /* last permutationm (complete descending order) -> first permutation */
        if (i == 0) {
            reverse(num, 0, num.length - 1);
            return;
        }
        /*  */
        int start = i - 1;
        int pivot = num[i-1];
        while (i < num.length && num[i] > pivot) {
            i++;
        }
        swap(num, start, i- 1);
        reverse(num, start + 1, num.length - 1);
    }
    
    private void swap(int[] num, int i, int j) {
        if (i == j) {
            return;
        }
        int tmp  = num[i];
        num[i] = num[j];
        num[j] = tmp;
    }
    
    private void reverse(int[] num, int i, int j) {
        if (i >= j) {
            return;
        }
        int size = (int)((j - i + 1) / 2);
        for (int k = 0; k < size; k++) {
            swap(num, i + k, j - k);
        }
    }

    public void sortColors(int[] A) {
        if (A == null || A.length == 0) {
            return;
        }
        
        /* start putting 0 to front */
        int start = 0, end = A.length - 1;
        while (start < end) {
            while (start < end && A[start] == 0) {
                start++;
            }
            while (start < end && A[end] != 0) {
                end--;
            }
            swap(A, start, end);
        }
        
        /* start sorting 1 and 2 */
        end = A.length - 1;
        while (start < end) {
            while (start < end && A[start] == 1) {
                start++;
            }
            while (start < end && A[end] == 2) {
                end--;
            }
            swap(A, start, end);
        }
    }

    public int maxSubArray(int[] A) {
        if (A == null || A.length == 0) {
            return 0;
        }
        
        int max = A[0], sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum = 0;
            while (i < A.length && A[i] <= 0) {
                i++;
            }
            while (i < A.length && sum + A[i] >= 0) {
                sum += A[i];
                max = Math.max(max, sum);
                i++;
            }
        }
        return max;
    }

    public ArrayList<Integer> spiralOrder(int[][] matrix) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        if (matrix == null || matrix.length == 0) {
            return result;
        }
        
        int left = 0, right = matrix[0].length - 1, up = 0, down = matrix.length - 1;
        while (left <= right && up <= down) {
            for (int i = left; i <= right; i++) {
                result.add(matrix[up][i]);
            }
            if (up == down) {
                break;
            }
            for (int i = up + 1; i <= down; i++) {
                result.add(matrix[i][right]);
            }
            if (left == right) {
                break;
            }
            for (int i = right - 1; i >= left; i--) {
                result.add(matrix[down][i]);
            }
            for (int i = down + 1; i >= up + 1; i--) {
                result.add(matrix[i][left]);
            }
            
            left++; right--; up++; down--;
        }
        return result;
    }

    public String countAndSay(int n) {
        String str = "1";
        for (int i = 1; i <= n; i++) {
            str = genNext(str);
        }
        return str;
    }
    
    private String genNext(String crt) {
        int i = 0;
        int length = crt.length();
        
        String re = "";
        while (i < length) {
            char c = crt.charAt(i);
            int num = 0;
            while (i < length && crt.charAt(i) == c) {
                i++;
                num++;
            }
            re += (Integer.toString(num) + c);
        }
        return re;
    }

    public int trap(int[] A) {
        if (A == null || A.length == 0) {
            return 0;
        }
        
        int max = -1;
        int maxIndex = -1;
        for (int i = 0; i < A.length; i++) {
            if (max < A[i]) {
                max = A[i];
                maxIndex = i;
            }
        }
        
        int i = 0;
        int total = 0;
        int start = i;
        while (i <= maxIndex) {
            start = i;
            while (i <= maxIndex && A[start] >= A[i])  {
                i++;
            }
            for (int j = start; j < i; j++) {
                total += (A[j] - A[start]);
            }
        }
        
        i = A.length - 1;
        while (i >= maxIndex) {
            start = i;
            while (i >= maxIndex && A[start] >= A[i])  {
                i--;
            }
            for (int j = start; j > i; j--) {
                total += (A[j] - A[start]);
            }
        }
        return total;
    }

    public int[] plusOne(int[] digits) {
        int[] result = new int[digits.length];
        System.arraycopy(digits, 0, result, 0, digits.length);
        
        if (digits == null || digits.length == 0) {
            return digits;
        }
        
        for (int i = result.length-1; i >= 0; i--) {
            if (result[i] < 9) {
                result[i]++;
                break;
            }
            else {
                result[i] = 0;
            }
        }
        if (result[0] == 0) {
            int[] newresult = new int[digits.length + 1];
            System.arraycopy(result, 1, newresult, 0, digits.length);
            newresult[0] = 1;
            result = newresult;
        }
        return result;
    }

    public boolean isValidSudoku(char[][] board) {
        return checkLine(board) && checkCol(board) && checkSub(board);
    }
    
    private boolean checkLine(char[][] board) {
        for (int i = 0; i < 9; i++) {
            HashSet<Character> set = new HashSet<Character>();
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == '.') {
                    continue;
                }
                if (!set.contains(board[i][j])) {
                    set.add(board[i][j]);
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean checkCol(char[][] board) {
        for (int i = 0; i < 9; i++) {
            HashSet<Character> set = new HashSet<Character>();
            for (int j = 0; j < 9; j++) {
                if (board[j][i] == '.') {
                    continue;
                }
                if (!set.contains(board[j][i])) {
                    set.add(board[j][i]);
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean checkSub(char[][] board) {
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                HashSet<Character> set = new HashSet<Character>();
                for (int m = i; m < i + 3; m++) {
                    for (int n = j; n < j + 3; n++) {
                        if (board[m][n] == '.') {
                            continue;
                        }
                        if (!set.contains(board[m][n])) {
                            set.add(board[m][n]);
                        }
                        else {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static String minWindow(String S, String T) {
        if (S == null || S.length() == 0) {
            return S;
        }
        if (T == null || T.length() == 0) {
            return "";
        }
        
        HashMap<Character, Integer> tCounter = new HashMap<Character, Integer>();
        for (int i = 0; i < T.length(); i++) {
            Character c = T.charAt(i);
            if (tCounter.containsKey(c)) {
                tCounter.put(c, tCounter.get(c) + 1);
            } else {
                tCounter.put(c, 1);
            }
        }
        
        int left = 0, right = 0, i = 0, j = 0, min = Integer.MAX_VALUE;
        int size = 0;
        while (right < S.length()) {
            /* move the right boundary */
            while (right < S.length() && size < tCounter.size()) {
                char c = S.charAt(right);
                if (tCounter.containsKey(c)) {
                    int cnt = tCounter.remove(c);
                    tCounter.put(c, cnt - 1);
                    if (cnt - 1 == 0) {
                        size++;
                    }
                }
                right++;
            }
            if (right == S.length()) {
                break;
            }
            if (right -left < min) {
                i = left; j = right;
                min = right -left;
            }
            
            /* move the left boundary */
            while (left < S.length() && size == tCounter.size()) {
                char c = S.charAt(left);
                if (tCounter.containsKey(c)) {
                    int cnt = tCounter.remove(c);
                    tCounter.put(c, cnt + 1);
                    if (cnt + 1 == 1) {
                        size--;
                    }
                }
                left++;
            }
        }
        return S.substring(i, j + 1);
    }

    public int minDistance(String word1, String word2) {
        if (word1 == null || word2 == null) {
            return 0;
        }
        
        String str1, str2;
        if (word1.length() < word2.length()) {
            str1 = word1; str2 = word2; 
        }
        else {
            str1 = word2; str2 = word1;
        }
        
        int index = 0, max = 0;
        for (int k = 0; k < str1.length(); k++) {
            int match = 0;
            for (int i = k; i < str1.length(); i++) {
                char c = str1.charAt(i);
                while (index < str2.length() && str2.charAt(index) != c) {
                    index++;
                }
                if (index == str2.length()) {
                    break;
                }
                else {
                    match++;
                    index++;
                }
            }
            max = Math.max(max, match);
        }
        return str2.length() - max;
    }

    public static int[] twoSum(int[] numbers, int target) {
        if (numbers == null || numbers.length <= 1) {
            return null;
        }

        int[] arr = new int[numbers.length];
        System.arraycopy(numbers, 0, arr, 0, numbers.length);
        Arrays.sort(arr);
        int i = 0, j = arr.length - 1;
        while (i < j) {
            while (i < j && arr[i] + arr[j] < target) {
                i++;
            }
            if (i != j && arr[i] + arr[j] == target) {
                break;
            }
            while (i < j && arr[i] + arr[j] > target) {
                j--;
            }
            if (i != j && arr[i] + arr[j] == target) {
                break;
            }
        }
        if (i >= j) {
            return null;
        }
        int m = -1, n = -1;
        for (int k = 0; k < numbers.length; k++) {
            if (m == -1 && numbers[k] == arr[i]) {
                m = k;
                continue;
            }
            if (n == -1 && numbers[k] == arr[j]) {
                n = k;
            }
        }

        int[] re = new int[2];
        if (m < n) {
            re[0] = m; re[1] = n;
        }
        else {
            re[0] = n; re[1] = m;
        }
        return re;
    }

    public int longestConsecutive(int[] num) {
        // {100, 4, 200, 2, 1, 3}
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i : num) {
            map.put(i, 0);
        }

        int max = 1;
        for (int i = 0; i < num.length; i++) {
            int crt = num[i];
            if (!map.containsKey(crt)) {
                continue;
            }

            map.remove(crt);

            int tmp1 = crt + 1;
            while (map.containsKey(tmp1)) {
                map.remove(tmp1);
                tmp1++;
            }

            int tmp2 = crt - 1;
            while (map.containsKey(tmp2)) {
                map.remove(tmp2);
                tmp2--;
            }
            max = Math.max(max, tmp1 - tmp2 - 1);
        }

        return max;
    }

    public static boolean isPalindrome(int x) {
        int size = 1;
        while (size <= 9) {
            int pow = (int)(Math.pow(10, size));
            if ((int)(x/pow) == 0) {
                break;
            }
            else {
                size++;
            }
        }

        while (size > 0) {
            int tail = x % 10;
            int pow = (int)(Math.pow(10, size - 1)); 
            int head = (int) (x / pow);
            if (head != tail) {
                return false;
            }
            size -= 2;
            x = (int)(x % pow);
            x = (int)(x / 10);
        }
        return true;
    }

    public int search(int[] A, int target) {
        return searchHelper(A, target, 0, A.length - 1);
    }
    
    private int searchHelper(int[] A, int target, int left, int right) {
        if (left > right) {
            return -1;
        }
        int middle = (int) (left + (right - left) / 2);
        if (A[middle] == target) {
            return middle;
        }
        
        /* left side is normally sorted */
        if (A[left] < A[middle]) {
            if (A[left] <= target && target <= A[middle]) {
                return searchHelper(A, target, left, middle - 1);
            }
            else {
                return searchHelper(A, target, middle + 1, right);
            }
        }
        else if (A[middle] < A[right]) {
            if (A[middle] <= target && target <= A[right]) {
                return searchHelper(A, target, middle + 1, right);
            }
            else {
                return searchHelper(A, target, left, middle - 1);
            }
        }
        else if (A[left] == A[middle]) {
            if (A[middle] != A[right]) {
                return searchHelper(A, target, middle + 1, right);
            }
            else {
                int re = searchHelper(A, target, left, middle - 1);
                if (re != -1) {
                    return re;
                }
                else {
                    return searchHelper(A, target, middle + 1, right);
                }
            }
        }
        else {
            return -1;
        }
    }
}