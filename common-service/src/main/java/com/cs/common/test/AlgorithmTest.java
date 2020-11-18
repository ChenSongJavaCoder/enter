package com.cs.common.test;

import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
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

    /**
     * 快速排序
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


        String s = deWeight("jjklklklklklllkk");


    }
}
