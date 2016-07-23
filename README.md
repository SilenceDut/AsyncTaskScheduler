#AsyncTaskScheduler
[![](https://jitpack.io/v/SilenceDut/AsyncTaskScheduler.svg)](https://jitpack.io/#SilenceDut/AsyncTaskScheduler)
##背景
[详细解读AsyncTask的黑暗面以及一种替代方案](http://www.jianshu.com/p/d83fd0e8a062)
##特点
- 默认多个任务并行处理。
- 执行单个任务无需使用线程池。
- 支持自定义线程池。
- 支持错误处。
- 多个任务的管理管理方便。
- 支持任何线程处使用，结果都会在UI线程处理。

##方法介绍
很多方法都是和AsyncTask类似。

- doInBackground
- onProgressUpdate
- onExecuteSucceed
- onExecuteCancelled
- onExecuteFailed
    发生异常时回调
      
##添加到项目
[![](https://jitpack.io/v/SilenceDut/AsyncTaskScheduler.svg)](https://jitpack.io/#SilenceDut/AsyncTaskScheduler)
Step 1. Add the JitPack repository to your build file

**gradle**
```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
**maven**
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Step 2. Add the dependency

**gradle**

```groovy
compile 'com.github.SilenceDut:AsyncTaskScheduler:{latest-version}'
```
**maven**

```xml
<dependency>
	    <groupId>com.github.SilenceDut</groupId>
	    <artifactId>AsyncTaskScheduler</artifactId>
	    <version>{latest-version}</version>
	</dependency>
```
##使用
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.SilenceDut:AsyncTaskScheduler:v0.5.0'
	}
1. 单个任务——是在单个线程里执行，不需要线程池。

        SingleAsyncTask singleTask = new SingleAsyncTask<Void,String>() {   
           @Override    
           public String doInBackground() {   
               return null;   
           }
           @Override
           public void onExecuteSucceed(String result) {      
               super.onExecuteSucceed(result);      
           }
           @Override
           public void onExecuteFailed(Exception exception) {      
               super.onExecuteFailed(exception);    
               Log.i(TAG,"onExecuteCancelled:"+exception.getMessage()+Thread.currentThread());
           }
        };
        singleTask.executeSingle();

        //取消通过executeSingle执行的任务
        mSingleAsyncTask.cancel(true);
2. 多个任务

        //多个任务新建一个任务调度器
        AsyncTaskScheduler mAsyncTaskScheduler = new AsyncTaskScheduler();
        
        SingleAsyncTask singleTask1 = new  SingleTask() { ... }；
        SingleAsyncTask singleTask2 = new  SingleTask() { ... }；
        SingleAsyncTask singleTask3 = new  SingleTask() { ... }；
        ...
        
        //并行执行多个任务
        mAsyncTaskScheduler.execute(singleTask1)
        .execute(singleTask2).execute(singleTask3).
    
         //取消通过AsyncTaskScheduler任务
        mAsyncTaskScheduler.cancelAllTasks(true);
3. 设置默认的线程池

        //设置默认的线程池
        Executor defaultPoolExecutor = ...
        AsyncTaskScheduler mAsyncTaskScheduler = new AsyncTaskScheduler(Executor defaultPoolExecutor);
4. 确保正确的取消任务以防止避免内存泄露或其他问题

        //取消通过executeSingle执行的任务
        mSingleAsyncTask.cancel(true);
        
        //取消通过AsyncTaskScheduler任务
        mAsyncTaskScheduler.cancelAllTasks(true);
        