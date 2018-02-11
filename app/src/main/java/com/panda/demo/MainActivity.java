package com.panda.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "MainActivity";
    private MainActivity activity;
    private Button upBtn, leftBtn, downBtn, rightBtn, startBtn, grabBtn, joinRoomBtn, quitRoomBtn, startQueueBtn, cancelQueueBtn, insertCoinsBtn;
    private EditText wsUrlText;
    private ImageView ivSwitch;
    private AVRootView avRootView;
    private XHLiveManager xhLiveManager;
    private int sdkAppid = 0; //互动直播 sdkAppid
    private int accountType = 0; //互动直播 accountType
    private int roomId = 500138; //视频房间号
    private String mMainCameraId = "wowgotcha_500138_1"; //主摄像头主播ID
    private String mSideCameraId = "wowgotcha_500138_2"; //侧摄像头主播 ID
    private String userid = "test"; //玩家ID
    private String usersig = ""; //玩家互动直播登录签名凭证
    private String wsUrl = "ws://ws1.open.wowgotcha.com:9090/play/1685c6fbb5dd8bdae98db3e65ecfd90a7a5bdc7d";
    private Toast toast = null;

    private View.OnTouchListener operationTouchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == R.id.upBtn) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按钮按下逻辑
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_UP);
                        break;
                    case MotionEvent.ACTION_UP:
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_UP_RELEASE);
                        //按钮弹起逻辑
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        //按钮弹起逻辑
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_UP_RELEASE);
                        break;
                }
            }
            else if (v.getId() == R.id.leftBtn) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按钮按下逻辑
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_LEFT);
                        break;
                    case MotionEvent.ACTION_UP:
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_LEFT_RELEASE);
                        //按钮弹起逻辑
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        //按钮弹起逻辑
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_LEFT_RELEASE);
                        break;
                }
            }
            else if (v.getId() == R.id.downBtn) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按钮按下逻辑
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_DOWN);
                        break;
                    case MotionEvent.ACTION_UP:
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_DOWN_RELEASE);
                        //按钮弹起逻辑
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        //按钮弹起逻辑
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_DOWN_RELEASE);
                        break;
                }
            }
            else if (v.getId() == R.id.rightBtn) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按钮按下逻辑
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_RIGHT);
                        break;
                    case MotionEvent.ACTION_UP:
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_RIGHT_RELEASE);
                        //按钮弹起逻辑
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        //按钮弹起逻辑
                        PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_RIGHT_RELEASE);
                        break;
                }
            }
            return false;
        }
    };

    private PlayerManagerListener managerListener = new PlayerManagerListener(){
        @Override
        public void roomReady(JSONObject readyInfo) {
            Log.d(TAG, "roomReady -> " + readyInfo.toString());
            showToast("room ready");
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
        }

        @Override
        public void roomQueueKickOff() {
            showToast("被踢出排队队列");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        upBtn = (Button) findViewById(R.id.upBtn);
        leftBtn = (Button) findViewById(R.id.leftBtn);
        downBtn = (Button) findViewById(R.id.downBtn);
        rightBtn = (Button) findViewById(R.id.rightBtn);
        startBtn = (Button) findViewById(R.id.startBtn);
        grabBtn = (Button) findViewById(R.id.grabBtn);
        joinRoomBtn = (Button) findViewById(R.id.joinRoomBtn);
        quitRoomBtn = (Button) findViewById(R.id.quitRoomBtn);
        startQueueBtn = (Button) findViewById(R.id.startQueueBtn);
        cancelQueueBtn = (Button) findViewById(R.id.cancelQueueBtn);
        insertCoinsBtn = (Button) findViewById(R.id.insertCoinsBtn);
        wsUrlText = (EditText) findViewById(R.id.wsUrlText);
        ivSwitch = (ImageView) findViewById(R.id.ivSwitch);
        avRootView = (AVRootView) findViewById(R.id.avRootView);
        // 初始化视频
        ivSwitch.setOnClickListener(this);
        xhLiveManager = XHLiveManager.sharedManager();
        xhLiveManager.initSdk(getBaseContext(), sdkAppid, accountType);
        XHLiveManager.setLogPrint(false);
        login();

        // 初始化游戏
        wsUrlText.setText(wsUrl);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = wsUrlText.getText().toString();
                if (TextUtils.isEmpty(url) || ! url.contains("ws")) {
                    Toast.makeText(getBaseContext(), "请填写需要链接的地址", Toast.LENGTH_SHORT).show();
                    return;
                }
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
        });
        upBtn.setOnTouchListener(operationTouchListener);
        leftBtn.setOnTouchListener(operationTouchListener);
        downBtn.setOnTouchListener(operationTouchListener);
        rightBtn.setOnTouchListener(operationTouchListener);
        grabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerManager.sharedManager().sendOperation(PlayerManager.PLAYER_OPERATION_GRAB);
            }
        });
        joinRoomBtn.setOnClickListener(this);
        quitRoomBtn.setOnClickListener(this);
        startQueueBtn.setOnClickListener(this);
        cancelQueueBtn.setOnClickListener(this);
        insertCoinsBtn.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivSwitch:
                XHLiveManager.sharedManager().switchCamera();
                break;
            case R.id.joinRoomBtn:
                joinRoom();
                break;
            case R.id.quitRoomBtn:
                quitRoom();
                break;
            case R.id.startQueueBtn:
                PlayerManager.sharedManager().startQueue();
                break;
            case R.id.cancelQueueBtn:
                PlayerManager.sharedManager().cancelQueue();
                break;
            case R.id.insertCoinsBtn:
                PlayerManager.sharedManager().insertCoins();
                break;
        }
    }

    private void login() {
        xhLiveManager.login(userid, usersig, new XHLiveListener(){
            @Override
            public void onSuccess() {
                Toast.makeText(activity, "Login Success", Toast.LENGTH_SHORT).show();
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
