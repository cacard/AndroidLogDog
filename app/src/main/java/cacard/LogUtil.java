package cacard;

import android.util.Log;

/**
 * Created by cunqingli on 2016/4/28.
 */
public class LogUtil {

    private static final String TAG = "logdog";

    public static void log(String msg) {
        Log.i(TAG, msg);
    }

    public static void log(String className, String methodName, String msg) {
        log("class:" + className + "/method:" + methodName + "/msg:" + msg);
    }

}
