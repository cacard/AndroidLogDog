package cacard;

/**
 * Created by cunqingli on 2016/4/28.
 */
public class ReadingLogThread implements Runnable {

    private boolean isStop;
    private boolean isPause;
    private Object lock4Pause = new Object();

    public ReadingLogThread() {
    }

    public void stop() {
        isStop = true;
    }

    public void pause() {
        isPause = true;
    }

    public void continueReading() {
        isPause = false;
        lock4Pause.notify();
    }

    @Override
    public void run() {
        while (true && !Thread.currentThread().isInterrupted()/* invoker can use interrupt to stop this thread */) {
            if (isStop) {
                break;
            }

            if (isPause) {
                try {
                    lock4Pause.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        LogUtil.log(ReadingLogThread.class.getSimpleName(), "run", "run will quite.thread stop");
    }
}
