package com.jdqm.leakdemo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jdqm on 2018-1-4.
 */

public class TaskManager {

    //使用volatile处理线程间的可见性，以及防止指令重排序
    private static volatile TaskManager instance;

    private List<TaskListener> taskListeners = new ArrayList<>();

    private TaskManager() {
    }

    /**
     * 使用双重检查避免每次都进入同步代码块，这样做只要instance初始化完毕，后续将不再进去同步
     *
     * @return singleton of TaskManager
     */
    public static TaskManager getInstance() {
        if (instance == null) {
            synchronized (TaskManager.class) {
                if (instance == null) {
                    instance = new TaskManager();
                }
            }
        }
        return instance;
    }

    public void registerTaskListener(TaskListener taskListener) {
        taskListeners.add(taskListener);
    }

    public void unRegisterTaskListener(TaskListener taskListener) {
        taskListeners.remove(taskListener);
    }
}
