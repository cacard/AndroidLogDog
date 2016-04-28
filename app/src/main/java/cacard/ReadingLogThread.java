package cacard;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by cunqingli on 2016/4/28.
 */
public class ReadingLogThread implements Runnable {

    private boolean isStop;
    private boolean isPause;
    private Object lock4Pause = new Object();
    private Process process;
    private Callback mCallback;

    public interface Callback {
        void onLogCome(String line);
    }

    public ReadingLogThread(Callback callback) {
        this.mCallback = callback;
        try {
            process = Runtime.getRuntime().exec("logcat logdog:v -d");
        } catch (Exception e) {
            //
        }
    }

    public void stopReading() {
        isStop = true;
    }

    public void pauseReading() {
        isPause = true;
    }

    public void continueReading() {
        isPause = false;
        lock4Pause.notify();
    }

    public void destory() {

    }

    @Override
    public void run() {
        if (process != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while (true && !Thread.currentThread().isInterrupted()/* invoker can use interrupt to stop this thread */) {
                if (isStop) {
                    break;
                }

                if (isPause) {
                    LogUtil.log("run() paused.");
                    try {
                        lock4Pause.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    final String line = bufferedReader.readLine();
                    if (line != null) {
                        mCallback.onLogCome(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


        dump("run() will quite.thread stop");
    }

    private static void dump(String msg) {
        LogUtil.log(ReadingLogThread.class.getSimpleName() + "|" + msg);
    }
}
