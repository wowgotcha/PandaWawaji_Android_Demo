package com.panda.demo;

import android.app.UiModeManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.panda.wawajisdk.core.PlayerManager;
import com.panda.wawajisdk.core.XHLiveManager;
import com.panda.wawajisdk.core.listener.PlayerConnectionListener;
import com.panda.wawajisdk.core.listener.PlayerManagerListener;
import com.panda.wawajisdk.core.listener.XHLiveListener;
import com.tencent.ilivesdk.view.AVRootView;

import org.json.JSONObject;

public class TVMainActivity extends AppCompatActivity {
    private final static String TAG = "TVMainActivity";
    private TVMainActivity activity;
    private AVRootView avRootView;
    private XHLiveManager xhLiveManager;
    private int sdkAppid = 1400050793; //互动直播 sdkAppid
    private int accountType = 19331; //互动直播 accountType
    private int roomId = 500139; //视频房间号
    private String mMainCameraId = "wowgotcha_500139_1"; //主摄像头主播ID
    private String mSideCameraId = "wowgotcha_500139_2"; //侧摄像头主播 ID
    private String userid = "test"; //玩家ID
    private String usersig = "eJxlj1FPgzAYRd-5FYRno22hZZr4IAPNGLi4ucz40iAU9sEopHRzZPG-O3GJJN7Xc25u7skwTdN6jVbXSZo2e6m57lthmXemhayrP9i2kPFEc1tl-6A4tqAET3It1AAxpZQgNHYgE1JDDhdDi06PaJdVfJj4rTvnLkXurT1WoBhgHKynsxdfwnzx4W9U*njAhKz9cFfX0Y7VE-nuxdEmcJqQPXXb6i0oZoW335aOG8f5g1vC8rkvFpnPVp30wmMPKSXTGzwvJVGfbnU-mtRQi8sfwsjEYS4Z0YNQHTRyEAjCFBMb-cQyvoxvsGhcvA__"; //玩家互动直播登录签名凭证
    private String wsUrl = "ws://ws1.open.wowgotcha.com:9090/play/1baa7a17fe19428452243476662d30facf8de4f1";
    private Toast toast = null;

    private View.OnKeyListener viewOnKeyListener = new View.OnKeyListener(){
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            switch(keyEvent.getKeyCode()){
                case KeyEvent.KEYCODE_ENTER:
                    String url = wsUrl;
                    if (PlayerManager.sharedManager().isPlaying()) {
                        // 下抓
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_GRAB);
                    } else {
                        // 开始游戏
                        PlayerManager.sharedManager().connect(url, new PlayerConnectionListener(){
                            @Override
                            public void onSuccess() {
                                // 连接成功
                                Toast.makeText(activity, "connect success", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onFailure(Throwable t) {
                                // 连接失败
                                Toast.makeText(activity, "connect failure", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
                case KeyEvent.KEYCODE_1:
                    XHLiveManager.sharedManager().switchCamera();
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    // 上
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_UP);
                    } else {
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_UP_RELEASE);
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    // 下
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_DOWN);
                    } else {
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_DOWN_RELEASE);
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    // 左
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_LEFT);
                    } else {
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_LEFT_RELEASE);
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    // 右
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_RIGHT);
                    } else {
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_RIGHT_RELEASE);
                    }
                    break;
            }
            return false;
        }
    };

    private PlayerManagerListener managerListener = new PlayerManagerListener(){
        @Override
        public void roomReady(JSONObject readyInfo) {
            Log.d(TAG, "roomReady -> " + readyInfo.toString());
            showToast("room ready");
            PlayerManager.sharedManager().startQueue();
        }
        @Override
        public void roomError(int errcode, String errmsg) {
            Log.d(TAG, "roomError -> errcode=" + errcode + ", errmsg=" + errmsg);
            showToast("room error");
        }
        @Override
        public void insertCoinResult(boolean success, JSONObject data, int errcode, String errmsg) {
            String text = "insertCoinResult -> success=" + success;
            if (data != null) {
                text += ", data=" + data.toString();
            }
            Log.d(TAG, text);
            String result = "投币成功";
            if (! success) {
                result = errmsg;
            }
            showToast(result);
        }
        @Override
        public void receiveGameResult(boolean success, int sessionId) {
            Log.d(TAG, "receiveGameResult -> success=" + success + ", sessionId=" + sessionId);
            String result = "没抓中";
            if (success) {
                result = "抓中";
            }
            showToast(result);
        }
        @Override
        public void gameReconnect(JSONObject data) {
            Log.d(TAG, "gameReconnect -> " + data.toString());
            showToast("重连");
        }

        @Override
        public void websocketClosed() {
            Log.d(TAG, "websocketClosed");
        }

        @Override
        public void startQueueResult(boolean success, int errcode, String errmsg) {
            String result = "排队成功";
            if (! success) {
                result = errmsg;
            }
            showToast(result);
        }

        @Override
        public void cancelQueueResult(boolean success, int errcode, String errmsg) {
            String result = "取消排队成功";
            if (! success) {
                result = errmsg;
            }
            showToast(result);
        }

        @Override
        public void roomQueueStatus(int queueNo, int position) {
            String text = "排队人数：" + queueNo + " 排队位置：" + position;
            showToast(text);
        }

        @Override
        public void gameReady(int timeLeft) {
            showToast("游戏准备就绪倒计时：" + timeLeft);
            PlayerManager.sharedManager().insertCoins();
        }

        @Override
        public void roomQueueKickOff() {
            showToast("被踢出排队队列");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_main);
        activity = this;
        avRootView = (AVRootView) findViewById(R.id.avRootView);
        // 初始化视频
        xhLiveManager = XHLiveManager.sharedManager();
        xhLiveManager.initSdk(getBaseContext(), sdkAppid, accountType);
        xhLiveManager.setLogPrint(false);
        login(); //视频房间登录

        avRootView.setOnKeyListener(viewOnKeyListener);
        //PlayerManager.init(getBaseContext()); // 不使用排队功能
        PlayerManager.init(getBaseContext(), true); // 使用排队功能
        PlayerManager.setLogPrint(true);
        PlayerManager.sharedManager().setManagerListener(managerListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        XHLiveManager.sharedManager().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        XHLiveManager.sharedManager().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XHLiveManager.sharedManager().onDestory();
    }

    private void login() {
        xhLiveManager.login(userid, usersig, new XHLiveListener(){
            @Override
            public void onSuccess() {
                Toast.makeText(activity, "Login Success", Toast.LENGTH_SHORT).show();
                joinRoom();
            }
            @Override
            public void onError(String module, int errCode, String errMsg) {
                Toast.makeText(activity, "Login Error", Toast.LENGTH_SHORT).show();
                login();
            }
        });
    }
    private void joinRoom() {
        XHLiveManager.sharedManager().joinRoom(roomId, mMainCameraId, mSideCameraId, avRootView, new XHLiveListener(){
            @Override
            public void onSuccess() {
                Toast.makeText(activity, "Enter Room Success", Toast.LENGTH_SHORT).show();
                xhLiveManager.upToVideoMember(null);
            }
            public void onError(String module, int errCode, String errMsg) {
                Toast.makeText(activity, "Enter Room Error", Toast.LENGTH_SHORT).show();
                joinRoom();
            }
        });
    }
    private void quitRoom() {
        XHLiveManager.sharedManager().quitRoom(new XHLiveListener(){
            @Override
            public void onSuccess() {
                Toast.makeText(activity, "quit Room Success", Toast.LENGTH_SHORT).show();
            }
            public void onError(String module, int errCode, String errMsg) {
                Toast.makeText(activity, "quit Room Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 弹出Toast(中断正在显示的Toast)
     * @param
     */
    public void showToast(String message) {
        if (toast == null) {
            toast = Toast.makeText(activity, "", Toast.LENGTH_SHORT);
        }
        toast.setText(message);
        toast.show();
    }
}
