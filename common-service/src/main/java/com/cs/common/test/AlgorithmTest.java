package com.cs.common.test;

import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: CS
 * @Date: 2020/8/24 2:01 下午
 * @Description:
 */
public class AlgorithmTest {


    /**
     * recursive
     * 通过递归计算出x的阶乘
     * eg: 3的阶乘 3*2*1
     *
     * @param x
     * @return
     */
    private static int fact(int x) {
        if (x == 1) {
            return x;
        } else {
            return x * fact(x - 1);
        }
    }

    /**
     * 数组递归求和
     *
     * @param list
     * @return
     */
    private static int sum(List<Integer> list) {
        if (CollectionUtils.isEmpty(list)) {
            // 基线条件 数组元素清空
            return 0;
        } else {
            Integer item = list.get(0);
            list.remove(0);
            // 递归条件
            return item + sum(list);
        }
    }

    /**
     * divide and conquer
     * 假设你是一位农场主，有一块长1680m，宽640m的土地，需要将这块土地尽可能大的切分成方块
     * 1.方块 长宽相等
     * 每次按宽的长度作为正方形的边，长宽取模可以得到需要进一步划分的正方形边大小，递归直至长宽取模为0
     *
     * @param list 数组中对应长和宽
     * @return
     */
    private static int[] divideAndConquer(int[] list) {
        int x = list[0];
        int y = list[1];

        int mod = x % y;
        if (mod == 0) {
            list[0] = y;
            return list;
        } else {
            list[0] = y;
            list[1] = mod;
            return divideAndConquer(list);
        }

    }

    /**
     * 递归获取列表最大值
     *
     * @param list
     * @return
     */
    private static int max(List<Integer> list) {
        // 基线条件
        if (!CollectionUtils.isEmpty(list) && (list.size() == 1)) {
            return list.get(0);
        } else {
            int first = list.get(0);
            int second = list.get(1);
            if (first > second) {
                list.remove(1);
            } else {
                list.remove(0);
            }
            return max(list);
        }
    }

    static int deepLength = 0;

    /**
     * 二分法搜索/插入元素的位置
     *
     * @param nums
     * @param target
     * @return
     */
    private static int searchInsert(int[] nums, int target) {

        // 通过分别判断移动左右下标，进一步缩小判断的下标区间，直至区间没有数据，左下标就是需要插入值的下标
        // 左下标
        int l = 0;
        // 右下标
        int r = nums.length - 1;

        // 跳出循环（左右下标间已经没有数据）
        while (l <= r) {
            int mid = (l + r) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] > target) {
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    /**
     * 快速排序
     * 分而治之
     *
     * @param list
     * @return
     */
    private static List<Integer> quickSort(List<Integer> list) {

        // 基线条件，当数组就剩1个值时返回
        if (list.size() < 2) {
            return list;
        }
        // 基数
        Integer pivot = list.get(0);
        List<Integer> less = list.stream().skip(1).filter(f -> f < pivot).collect(Collectors.toList());
        List<Integer> greater = list.stream().skip(1).filter(f -> f > pivot).collect(Collectors.toList());

        List<Integer> newList = quickSort(less);
        newList.add(pivot);
        newList.addAll(quickSort(greater));
        return newList;
    }

    /**
     * 给定一个数据组，数组中两数相加等于target的数组下标
     * 其执行的次数受数组的长度成平方新增
     * 时间复杂度：O(N²)
     * 空间复杂度：O(1)
     * 空间最优
     *
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSumA(int[] nums, int target) {
        int size = nums.length;
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{0};
    }

    /**
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
     * 时间最优
     *
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSumB(int[] nums, int target) {
        int size = nums.length;
        Map<Integer, Integer> dic = new HashMap<>();
        for (int i = 0; i < size; i++) {
            if (dic.containsKey(target - nums[i])) {
                return new int[]{dic.get(target - nums[i]), i};
            }
            dic.put(nums[i], i);
        }
        return new int[0];
    }

    /**
     * 替换空格
     * 请实现一个函数，把字符串 s 中的每个空格替换成"%20"。
     *
     * @param s
     * @return
     */
    public static String replaceSpace(String s) {
        // 字符串不可变，所以需要有容器重新组装接收
        char[] chars = s.toCharArray();
        // 1、StringBuilder作为容器接收
//        StringBuilder sb = new StringBuilder();
//        for (char c : chars) {
//            if (c == ' ') {
//                sb.append("%20");
//            } else {
//                sb.append(c);
//            }
//        }
//        return sb.toString();

        // 2、char[]作为容器接收，数组的长度不可变，所以按最大的长度初始化，其空间复杂度为O(N)
        char[] target = new char[chars.length * 3];
        int size = 0;
        for (char c : chars) {
            if (c == ' ') {
                target[size++] = '%';
                target[size++] = '2';
                target[size++] = '0';
            } else {
                target[size++] = c;
            }
        }
        return new String(target, 0, size);
    }

    /**
     * 输入一个链表的头节点，从尾到头反过来返回每个节点的值（用数组返回）。
     *
     * @param listNode
     * @return
     */
    public static int[] reversePrint(ListNode listNode) {
        // 使用栈结构：先进后出实现
//        LinkedList<Integer> stack = new LinkedList();
//        while (listNode != null) {
//            stack.add(listNode.val);
//            listNode = listNode.next;
//        }
//        int[] res = new int[stack.size()];
//        int idx = 0;
//        while (stack.iterator().hasNext()) {
//            res[idx++] = stack.removeLast();
//        }
//
//        return res;
        //先获取链表长度，创建对应长度数组
        ListNode currNode = listNode;
        int len = 0;
        while (currNode != null) {
            len++;
            currNode = currNode.next;
        }
        int[] result = new int[len];

        //再次遍历链表，将值倒序填充至结果数组
        currNode = listNode;
        while (currNode != null) {
            result[len - 1] = currNode.val;
            len--;
            currNode = currNode.next;
        }
        return result;

    }

    /**
     * 反转链表
     *
     * @param head
     * @return
     */
    public static ListNode reverseList(ListNode head) {
        // 双指针
//        ListNode cur = head, pre = null;
//        while (cur != null) {
//            // 临时节点
//            ListNode tmp = cur.next;
//            // 下一个节点与前一个节点对换位置
//            cur.next = pre;
//            pre = cur;
//            // 当前节点取下一个节点
//            cur = tmp;
//        }
//       return pre;
        // 递归
        // 为什么定义current、pre？ 因为是需要拿到next、cur、pre改变其顺序关系

        return recursiveNode(head, null);
    }

    private static ListNode recursiveNode(ListNode current, ListNode pre) {
        // 递归出口 原链表的最后一个节点
        if (current == null) {
            // 此时的pre是原链表最后一个元素
            return pre;
        }
        // 后继调用
        ListNode next = current.next;
        ListNode res = recursiveNode(next, current);
        // 前后关系反转
        current.next = pre;

        return res;
    }


    /**
     * 广度优先搜索
     *
     * @return
     */
    private static String breadthFirstSearch(Map<String, List<String>> relation) {
        // 散列表
        // 一度关系 > 二度关系


        return null;
    }

    /**
     * 目标字符串相邻字符相同去重
     *
     * @param s
     * @return
     */
    private static String deWeight(String s) {
        s = Objects.isNull(s) ? "abbccdjhsgsgskskshbjklfghjklbnmssss" : s;
        char[] arr = s.toCharArray();

        Stack stack = new Stack();
        StringBuilder sb = new StringBuilder();
        for (char c : arr) {
            if (stack.empty()) {
                stack.push(c);
                sb.append(c);
            } else {
                char target = (char) stack.peek();
                if (target == c) {
                    continue;
                } else {
                    stack.push(c);
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static void testOOM() {
        deepLength++;
        testOOM();
    }

    public static void main(String[] args) {
//        int v = fact(1000);
//
//        int count = sum(Lists.newArrayList(2, 3, 4, 1, 5));
//
//        int[] o = divideAndConquer(new int[]{1680, 640});
//
//        int max = max(Lists.newArrayList(28, 3, 4, 6, 8, 9));
//
//        List<Integer> qSort = quickSort(Lists.newArrayList(28, 3, 4, 6, 8, 9, 30, 89, 10));
//
//        int idx = searchInsert(new int[]{1, 2, 3, 4, 5, 7}, 0);


//        String s = deWeight("jjklklklklklllkk");

//        int algorithm = algorithm(10);

//        int[] ints = twoSumA(new int[]{1, 4, 2, 3, 4}, 5);

//        String s = replaceSpace("We are happy.");

//        ListNode listNode = new ListNode(1, new ListNode(3, new ListNode(2, null)));
//        int[] ints = reversePrint(listNode);
//        ListNode reverseList = reverseList(listNode);
        try {
            testOOM();
        } catch (Throwable e) {
            System.out.println("stack length: " + deepLength);
            throw e;
        }

    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x, ListNode n) {
            val = x;
            next = n;
        }
    }

    class CQueue {
        LinkedList<Integer> A, B;

        public CQueue() {
            A = new LinkedList<>();
            B = new LinkedList<>();
        }

        public void appendTail(int value) {
            B.addLast(value);
        }

        public int deleteHead() {
            if (!A.isEmpty()) {
                return A.removeLast();
            }
            if (A.isEmpty() && !B.isEmpty()) {
                while (!B.isEmpty()) {
                    A.addLast(B.removeLast());
                }
                return A.removeLast();
            }
            return -1;
        }
    }
}
