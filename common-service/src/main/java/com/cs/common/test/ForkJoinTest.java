package com.cs.common.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author: CS
 * @date: 2021/7/23 下午4:09
 * @description:
 */
public class ForkJoinTest {


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool(2);
        CountTask task = new CountTask(1, 10);
        ForkJoinTask<Integer> result = forkJoinPool.submit(task);
        //检查中断
        if (result.isCompletedAbnormally()) {
            System.out.println(task.getException());
        }
        try {
            System.out.println(result.get() + "耗时：" + (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        }
    }

    static class CountTask extends RecursiveTask<Integer> {

        private static final int THRESHOLD = 2;
        private int start;
        private int end;

        public CountTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            int sum = 0;
            boolean canCompute = (end - start) <= THRESHOLD;
            if (canCompute) {
                for (int i = start; i <= end; i++) {
                    sum += i;
                }
//                System.out.println(Thread.currentThread().getName() + " 开始：" + start + " 结束：" + end + " 合计：" + sum);
            } else {
                int middle = (start + end) / 2;
                CountTask leftTask = new CountTask(start, middle);
                CountTask rightTask = new CountTask(middle + 1, end);
                leftTask.fork();
                rightTask.fork();
                int leftResult = leftTask.join();
                int rightResult = rightTask.join();
                sum = leftResult + rightResult;
            }
            return sum;
        }


    }

}
