package com.silencedut.asynctaskscheduler;

import android.support.annotation.NonNull;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SilenceDut on 16/7/21.
 *
 * <p>AsyncTaskScheduler is a better substitute good of AsyncTask.It has a default
 * CachedThreadPool, and you can change the default {@link ExecutorService} .use
 * {@link #execute} to execute a new SingleAsyncTask,and it can be called when when you
 * what,not like {@link android.os.AsyncTask} you can only called the execute once.
 * {@link #cancelTask} to cancel a SingleAsyncTask. you can manage all tasks
 * {@link SingleAsyncTask} easy by the method{@link #cancelAllTasks}.
 * </p>
 *
 * <h3>Attention</h3>
 *
 * you should call {@link #cancelTask} or {@link #cancelAllTasks} when on some life cycle
 * such as {@link onDestroy} avoid memory leak
 *
 * <p>for more information
 * see {@link []!(https://github.com/SilenceDut/AsncSingleTaskSample)}
 * </p>
 *
 */
public class AsyncTaskScheduler {

    private final CopyOnWriteArrayList<SingleAsyncTask> mSingleAsyncTaskList = new CopyOnWriteArrayList<SingleAsyncTask>();

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {

            return new Thread(r, "AsyncTaskScheduler  Thread#" + mCount.getAndIncrement());
        }
    };

    public static final Executor CACHED_THREAD_POOL = Executors.newCachedThreadPool(sThreadFactory);

    private  Executor mDefaultPoolExecutor  ;


    /**
     * set an {@link Executor} that can be used to execute tasks in parallel as default .
     */
    public AsyncTaskScheduler() {
       this.mDefaultPoolExecutor = CACHED_THREAD_POOL;
    }

    /**
     * you set an {@link Executor} that you what as default .
     */
    public AsyncTaskScheduler(Executor defaultPoolExecutor ) {
        this.mDefaultPoolExecutor = defaultPoolExecutor;
    }

    public AsyncTaskScheduler execute(@NonNull SingleAsyncTask singleAsyncTask){
        mDefaultPoolExecutor.execute(singleAsyncTask.getFutureTask());
        mSingleAsyncTaskList.add(singleAsyncTask);
        return this;
    }


    /**
        cancel a singleAsyncTask
     */
    public boolean cancelTask(SingleAsyncTask singleAsyncTask, boolean mayInterruptIfRunning) {
        return singleAsyncTask.cancel(mayInterruptIfRunning);
    }

    /**
     cancel all singleTask in the scheduler
     */
    public void cancelAllTasks(boolean mayInterruptIfRunning){
        for(SingleAsyncTask singleAsyncTask : mSingleAsyncTaskList) {
            cancelTask(singleAsyncTask,mayInterruptIfRunning);
        }
        mSingleAsyncTaskList.clear();
    }

//    public void shutDown() {
//        mDefaultPoolExecutor.shutdownNow();
//        synchronized (this) {
//            cancelAllTasks(true);
//        }
//    }
//
//    public boolean isShutDown() {
//        return mDefaultPoolExecutor.isShutdown();
//    }
}

