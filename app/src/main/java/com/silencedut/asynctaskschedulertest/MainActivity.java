package com.silencedut.asynctaskschedulertest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.silencedut.asynctaskscheduler.AsyncTaskScheduler;
import com.silencedut.asynctaskscheduler.SingleAsyncTask;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SingleAsyncTask mSingleAsyncTask;
    private AsyncTaskScheduler mAsyncTaskScheduler;
    private TextView mLogMessageTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLogMessageTv = (TextView) findViewById(R.id.log_message_tv);
        mAsyncTaskScheduler = new AsyncTaskScheduler();

        mSingleAsyncTask =new SingleAsyncTask<Void,String>() {
            @Override
            public String doInBackground() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i(TAG,"doInBackground: "+Thread.currentThread().getName());
                return "singleTask"+Thread.currentThread().getName();
            }

            @Override
            public void onExecuteSucceed(String s) {
                super.onExecuteSucceed(s);
                mLogMessageTv.setText("\n onExecuteSucceed:"+s+mLogMessageTv.getText());
                Log.i(TAG,"onExecuteSucceed:"+s+Thread.currentThread());
            }

            @Override
            public void onExecuteCancelled(String result) {
                super.onExecuteCancelled(result);
                Log.i(TAG,"onExecuteCancelled:"+result+Thread.currentThread());
            }

            @Override
            public void onExecuteFailed(Exception exception) {
                super.onExecuteFailed(exception);
                Log.i(TAG,"onExecuteCancelled:"+exception.getMessage()+Thread.currentThread());
            }
        };
        SingleAsyncTask singleAsyncTask1 =new SingleAsyncTask<Void,String>() {
            @Override
            public String doInBackground() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    onExecuteFailed(e);
                }
                Log.i(TAG,"doInBackground: "+Thread.currentThread().getName());
                return "singleAsyncTask1"+Thread.currentThread().getName();
            }
            @Override
            public void onExecuteSucceed(String s) {
                super.onExecuteSucceed(s);
                mLogMessageTv.setText("\n onExecuteSucceed:"+s+mLogMessageTv.getText());
            }
        };
        SingleAsyncTask singleAsyncTask2 =new SingleAsyncTask<Void,String>() {
            @Override
            public String doInBackground() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    onExecuteFailed(e);
                }
                Log.i(TAG,"doInBackground: "+Thread.currentThread().getName());
                return "singleAsyncTask1"+Thread.currentThread().getName();
            }
            @Override
            public void onExecuteSucceed(String s) {
                super.onExecuteSucceed(s);
                mLogMessageTv.setText("\n onExecuteSucceed:"+s+mLogMessageTv.getText());
            }
        };

        mAsyncTaskScheduler.execute(singleAsyncTask1).execute(singleAsyncTask2);
        mSingleAsyncTask.executeSingle();

        mLogMessageTv.setText("\n MainThread:"+Thread.currentThread());
        Log.i(TAG, "Main"+Thread.currentThread());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAsyncTaskScheduler.cancelAllTasks(true);
        mSingleAsyncTask.cancel(true);
    }
}
