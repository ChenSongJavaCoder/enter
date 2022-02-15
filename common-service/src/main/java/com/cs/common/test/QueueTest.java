package com.cs.common.test;

import com.cs.common.thread.ThreadExecutor;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: CS
 * @date: 2021/7/9 上午11:28
 * @description: 队列相关
 * @see java.util.Collection
 * @see java.util.LinkedList
 * @see PriorityQueue
 * @see java.util.concurrent.LinkedBlockingQueue
 * @see java.util.concurrent.BlockingQueue
 * @see java.util.concurrent.ArrayBlockingQueue
 * @see java.util.concurrent.LinkedBlockingQueue
 * @see java.util.concurrent.PriorityBlockingQueue
 * @see java.util.concurrent.ConcurrentLinkedQueue
 * 哨兵节点：<p>https://blog.csdn.net/m0_45406092/article/details/113527205<p/>
 */
public class QueueTest {


    public static void main(String[] args) {
//        priorityQueue();
        delayQueue();
//        concurrentLinkedQueue();
    }

    /**
     * 优先级队列，可实现自定义排序
     */
    public static void priorityQueue() {
        // 基础类型排序
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<Integer>(Comparator.reverseOrder());
        List<Integer> data = Lists.newArrayList(1, 2, 4, 5, 6, 3);
        priorityQueue.addAll(data);
        while (true) {
            if (priorityQueue.isEmpty()) {
                break;
            }
            System.out.println(priorityQueue.poll());
        }

        // 引用对象排序
        PriorityQueue<CompareObject> compareObjectPriorityQueue = new PriorityQueue<>(
                // age 正序
//                Comparator.comparingInt(i -> i.getAge())
                // age 倒序
//                (x,y) -> y.getAge().compareTo(x.getAge())
                // age 倒序 然后比较 name 倒序
                Comparator.comparing(CompareObject::getAge, Comparator.reverseOrder())
                        .thenComparing((x, y) -> y.getName().compareTo(x.getName()))
        );

        List<CompareObject> compareObjects = Lists.newArrayList(
                new CompareObject(1, "cs1")
                , new CompareObject(3, "cs3")
                , new CompareObject(2, "cs4")
                , new CompareObject(2, "cs2"));
        compareObjectPriorityQueue.addAll(compareObjects);
        while (true) {
            if (compareObjectPriorityQueue.isEmpty()) {
                break;
            }
            System.out.println(compareObjectPriorityQueue.poll().name);
        }

        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue<Integer>(10);
        List<Integer> list = Lists.newArrayList(1, 3, 4, 5, 2);
        priorityBlockingQueue.addAll(list);
        while (true) {
            if (priorityBlockingQueue.isEmpty()) {
                break;
            }
            System.out.println(priorityBlockingQueue.poll());
        }
    }

    /**
     * 延时队列
     * DelayQueue是一个无界的BlockingQueue，用于放置实现了Delayed接口的对象，其中的对象只能在其到期时才能从队列中取走。
     * 这种队列是有序的，即队头对象的延迟到期时间最长。
     * 注意：不能将null元素放置到这种队列中。
     *
     * @see <p>https://www.cnblogs.com/myseries/p/10944211.html<p/>
     * @see <p>https://zhuanlan.zhihu.com/p/64152280</p>
     */
    public static void delayQueue() {
//        DelayQueue delayQueue = new DelayQueue();
//
//        new Thread(() -> {
//            delayQueue.offer(new MyDelayedTask("task1", 10000));
//            delayQueue.offer(new MyDelayedTask("task2", 3900));
//            delayQueue.offer(new MyDelayedTask("task3", 1900));
//            delayQueue.offer(new MyDelayedTask("task4", 5900));
//            delayQueue.offer(new MyDelayedTask("task5", 6900));
//            delayQueue.offer(new MyDelayedTask("task6", 7900));
//            delayQueue.offer(new MyDelayedTask("task7", 4900));
//        }).start();
//
//        while (true) {
//            Delayed take = null;
//            try {
//                System.out.println("1");
//                take = delayQueue.take();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(take + "当前时间：" + System.currentTimeMillis() + " 队列大小：" + delayQueue.size());
//        }

        DelayQueue<DelayMessage> queue = new DelayQueue<>();

        long now = System.currentTimeMillis();

        // 启动一个线程从队列中取元素
        new Thread(() -> {
            do {
                try {
                    // 将依次打印1000，2000，5000，7000，8000
                    System.out.println(queue.take().deadline - now);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!queue.isEmpty());
        }).start();

        // 添加5个元素到队列中
        queue.add(new DelayMessage(now + 5000));
        queue.add(new DelayMessage(now + 8000));
        queue.add(new DelayMessage(now + 2000));
        queue.add(new DelayMessage(now + 1000));
        queue.add(new DelayMessage(now + 7000));
    }

    /**
     * 线程安全的链表队列,CAS
     * 无界队列
     * FIFO
     * 链表头部元素存在队列时间越长
     */
    public static void concurrentLinkedQueue() {
        ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();
        List<String> list = Lists.newArrayList("1", "2", "3", "4", "5");

        list.stream().forEach(e -> {
            ThreadExecutor.execute(() -> {
                concurrentLinkedQueue.offer(e);
            });
        });
        while (!concurrentLinkedQueue.isEmpty()) {
            System.out.println(concurrentLinkedQueue.poll());
        }
    }
}


@Getter
class CompareObject {
    Integer age;
    String name;

    public CompareObject(int age, String name) {
        this.age = age;
        this.name = name;
    }
}

class FIFOEntry<E extends Comparable<? super E>>
        implements Comparable<FIFOEntry<E>> {
    static final AtomicLong seq = new AtomicLong(0);
    final long seqNum;
    final E entry;

    public FIFOEntry(E entry) {
        seqNum = seq.getAndIncrement();
        this.entry = entry;
    }

    public E getEntry() {
        return entry;
    }

    public int compareTo(FIFOEntry<E> other) {
        int res = entry.compareTo(other.entry);
        if (res == 0 && other.entry != this.entry)
            res = (seqNum < other.seqNum ? -1 : 1);
        return res;
    }
}

/**
 * compareTo 方法必须提供与 getDelay 方法一致的排序
 */
class MyDelayedTask implements Delayed {
    /**
     * 元素名称
     */
    private String name;
    /**
     * 当前时间
     */
    private long start = System.currentTimeMillis();
    /**
     * 过期时间
     */
    private long time;

    public MyDelayedTask(String name, long time) {
        this.name = name;
        this.time = time;
    }

    /**
     * 需要实现的接口，获得延迟时间   用过期时间-当前时间
     *
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert((start + time) - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * 用于延迟队列内部比较排序   当前时间的延迟时间 - 比较对象的延迟时间
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return "MyDelayedTask{" +
                "name='" + name + '\'' +
                ", time=" + time +
                '}';
    }
}

class DelayMessage implements Delayed {
    long deadline;

    public DelayMessage(long deadline) {
        this.deadline = deadline;
    }


    @Override
    public long getDelay(TimeUnit unit) {
        return deadline - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }
}



