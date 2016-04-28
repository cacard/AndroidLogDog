package cacard;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nolanlawson.logcat.R;

/**
 * Created by cunqingli on 2016/4/28.
 */
public class MainActivity extends Activity {

    private TextView tvLog;
    private Handler mHandler = new Handler();
    private Thread mThread;
    private Button mBtnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        startMonitor();
        startWriteLog();
    }

    private void initView() {
        tvLog = (TextView) findViewById(R.id.tvLog);
        tvLog.setMovementMethod(new ScrollingMovementMethod());
        mBtnReset = (Button) findViewById(R.id.btnReset);
    }

    private void initListener() {
        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mThread != null) {
                    mThread.interrupt();
                    mThread = null;
                    tvLog.setText("");
                    startMonitor();
                }
            }
        });
    }

    private void startMonitor() {
        if (mThread == null) {
            mThread = new Thread(new ReadingLogThread(new ReadingLogThread.Callback() {
                @Override
                public void onLogCome(final String line) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (tvLog != null && !TextUtils.isEmpty(line)) {
                                tvLog.append(line + "\n\n");
                            }
                        }
                    });
                }
            }));
        }

        mThread.start();
    }

    private void startWriteLog() {
        for (int i = 0; i < 30; i++) {
            final int _i = i;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogUtil.log("hello" + _i);
                }
            }, 1000 * i);
        }
    }
}
