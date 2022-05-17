package com.cs.common.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.Executor;

/**
 * A task queue that always run all tasks in order. The executor to run the tasks is passed
 * when the tasks are executed, this executor is not guaranteed to be used, as if several
 * tasks are queued, the original thread will be used.
 * <p>
 * More specifically, any call B to the {@link #execute(Runnable, Executor)} method that happens-after another call A to the
 * same method, will result in B's task running after A's.
 *
 * @author <a href="david.lloyd@jboss.com">David Lloyd</a>
 * @author <a href="mailto:tim.fox@jboss.com">Tim Fox</a>
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TaskQueue {

    static final Logger log = LoggerFactory.getLogger(TaskQueue.class);
    // @protectedby tasks
    private final LinkedList<Task> tasks = new LinkedList<>();
    private final Runnable runner;
    // @protectedby tasks
    private Executor current;

    public TaskQueue() {
        runner = this::run;
    }

    private void run() {
        for (; ; ) {
            final Task task;
            synchronized (tasks) {
                task = tasks.poll();
                if (task == null) {
                    current = null;
                    return;
                }
                if (task.exec != current) {
                    tasks.addFirst(task);
                    task.exec.execute(runner);
                    current = task.exec;
                    return;
                }
            }
            try {
                task.runnable.run();
            } catch (Throwable t) {
                log.error("Caught unexpected Throwable", t);
            }
        }
    }

    /**
     * Run a task.
     *
     * @param task the task to run.
     */
    public void execute(Runnable task, Executor executor) {
        synchronized (tasks) {
            tasks.add(new Task(task, executor));
            if (current == null) {
                current = executor;
                executor.execute(runner);
            }
        }
    }

    ;

    private static class Task {

        private final Runnable runnable;
        private final Executor exec;

        public Task(Runnable runnable, Executor exec) {
            this.runnable = runnable;
            this.exec = exec;
        }
    }
}
