package com.panda.demo;

import android.app.UiModeManager;
import android.content.res.Configuration;
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
import com.panda.wawajisdk.core.XHLivePlayer;
import com.panda.wawajisdk.core.listener.PlayerConnectionListener;
import com.panda.wawajisdk.core.listener.PlayerManagerListener;
import com.panda.wawajisdk.core.listener.XHLiveListener;
import com.panda.wawajisdk.core.listener.XHLivePlayerListener;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMTextElem;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.json.JSONObject;

import java.util.List;

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
    private int roomId = 500822; //视频房间号
    private String mMainCameraId = "wowgotcha_" + roomId + "_1"; //主摄像头主播ID
    private String mSideCameraId = "wowgotcha_" + roomId + "_2"; //侧摄像头主播 ID
    private String userid = "wowgotcha_player_74av86efni"; //玩家ID
    //玩家互动直播登录签名凭证
    private String usersig = "eJxlj11PgzAYhe-5FYRro4VSYCZeIB-OhUUNyNSbpmnLaNigQoER439XcYkkvrfPc87J*6Hpum5kSXpJKG36WmE1SW7o17oBjIs-KKVgmCgMW-YP8pMULcekULydoYkQsgBYOoLxWolCnI2xGfeNoiXB8kAm3mLXJoPn8KIWi1DHKjwv-7ba35UrGzhoqYj9DLfRc3B-59M*J4-JVb*L05jsqgltBsms7Clcu6swjJN3wqoXFZwcX0R*mebOsEkPb2VxdF65zPwp34r8NihUZCUgoHRcp*qhtzpws5hU4sjPb0LoeTaE7oIOvO1EU8*CBUxkWhD8nKF9al*qq2Wp";
    private String wsUrl = "ws://ws1.open.wowgotcha.com:9090/play/b404217fbd5a2df1d6bfb462c3d60a7a0ee7b693";
    private String masterUrl = "rtmp://15814.liveplay.myqcloud.com/live/15814_8985b20c42e3bf0a5e30330158febabd";
    private String slaveUrl = "rtmp://15814.liveplay.myqcloud.com/live/15814_c1e71d8fe70e5733538879ed715e81c0";
    private Toast toast = null;
    private TXCloudVideoView mMasterLiveViewoView;
    private TXCloudVideoView mSlaveLiveViewoView;

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
            if (success) {
                // 上麦
                upToVideoMember();
            } else {
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
            downToVideoMember();
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
            if (success) {
                // 加入互动直播房间
                joinRoom();
            } else {
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
            // 退出互动直播房间
            quitRoom();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
        if (isTV()) {
            Intent intent = new Intent(MainActivity.this, TVMainActivity.class);
            startActivity(intent);
            return;
        }
         **/
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
        // 互动直播
        ivSwitch.setOnClickListener(this);
        xhLiveManager = XHLiveManager.getInstance();
        xhLiveManager
                .initSdk(getBaseContext(), sdkAppid, accountType)
                .setLogPrint(false)
                .imsupport(false);
        login();
        // 旁路直播
        XHLivePlayer player = XHLivePlayer.getInstance();
        mMasterLiveViewoView = (TXCloudVideoView) findViewById(R.id.master_live_video_view);
        mSlaveLiveViewoView = (TXCloudVideoView) findViewById(R.id.slave_live_video_view);
        player.setVideoView(mMasterLiveViewoView, mSlaveLiveViewoView);
        player.initPlayer(this, masterUrl, slaveUrl);
        player.play();
        player.setListener(new XHLivePlayerListener() {
            @Override
            public void onPlayEvent(int position, int event, Bundle param) {
                switch (event) {
                    case TXLiveConstants.PLAY_ERR_NET_DISCONNECT:
                        Log.e(TAG, "[AnswerRoom] 拉流失败：网络断开");
                        break;
                    case TXLiveConstants.PLAY_EVT_CONNECT_SUCC:
                        break;
                    case TXLiveConstants.PLAY_EVT_RTMP_STREAM_BEGIN:
                        break;
                    case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME:
                        break;
                    case TXLiveConstants.PLAY_EVT_PLAY_BEGIN:
                        if (position == XHLivePlayer.CAMERA_MASTER) {
                            // 旁路正面摄像头渲染成功
                        } else if (position == XHLivePlayer.CAMERA_MASTER) {
                            // 旁路侧面正面摄像头渲染成功
                        }
                        break;
                    case TXLiveConstants.PLAY_EVT_PLAY_LOADING:
                        break;
                    case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                        // 播放中
                        break;
                    default:
                        Log.e(TAG, "TXLivePlayer index: " + position + "  event:" + event);
                }
            }

            @Override
            public void onNetStatus(int position, Bundle bundle) {

            }
        });

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
        XHLiveManager.getInstance().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        XHLiveManager.getInstance().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XHLiveManager.getInstance().onDestory();
        XHLivePlayer.getInstance().destroy();
    }

    /**
     * TV 模式
     * @return
     */
    private boolean isTV(){
        UiModeManager uiModeManager = (UiModeManager)getSystemService(UI_MODE_SERVICE);
        Log.d(TAG, "getCurrentModeType: " + uiModeManager.getCurrentModeType());
        return uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION
                || uiModeManager.getCurrentModeType() == Configuration.TOUCHSCREEN_NOTOUCH;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivSwitch:
                if (xhLiveManager.isEnterRoom()) {
                    XHLiveManager.getInstance().switchCamera();
                } else {
                    XHLivePlayer.getInstance().switchCamera();
                }
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
                // 退出互动直播房间
                quitRoom();
                PlayerManager.sharedManager().cancelQueue();
                break;
            case R.id.insertCoinsBtn:
                PlayerManager.sharedManager().insertCoins("123");
                break;
        }
    }

    private void login() {
        xhLiveManager.login(userid, usersig, new XHLiveListener(){
            @Override
            public void onSuccess() {
                Toast.makeText(activity, "Login Success", Toast.LENGTH_SHORT).show();
                // 加入群聊房间
                TIMGroupManager.getInstance().applyJoinGroup(roomId + "", "request to join" + roomId, new TIMCallBack() {
                    @Override
                    public void onError(int code, String desc) {
                        Log.d(TAG, "IM Join group error code:" + code + " message:" + desc);
                    }

                    @Override
                    public void onSuccess() {
                        //这里跟官方文档有些出入了，这里再去加入直播房间，不需要绑定
                        Log.d(TAG, "IM join group success");
                    }
                });

                // 监听接收消息
                TIMManager.getInstance().addMessageListener(new TIMMessageListener() {
                    @Override
                    public boolean onNewMessages(List<TIMMessage> list) {
                        for (int f = 0; f < list.size(); f++) {
                            TIMMessage msg = list.get(f);
                            for (int i = 0; i < msg.getElementCount(); ++i) {
                                TIMElem elem = msg.getElement(i);
                                //获取当前元素的类型
                                TIMElemType elemType = elem.getType();
                                if (elemType == TIMElemType.Text) {
                                    //文本消息
                                    TIMTextElem textElem = (TIMTextElem) elem;
                                    TIMUserProfile userProfile = msg.getSenderProfile();
                                    Log.d(TAG, "IM Message" + textElem.getText());
                                }
                            }
                        }
                        return false;
                    }
                });
                //joinRoom();
            }
            @Override
            public void onError(String module, int errCode, String errMsg) {
                Toast.makeText(activity, "Login Error", Toast.LENGTH_SHORT).show();
                login();
            }
        });
    }
    private void joinRoom() {
        XHLiveManager.getInstance().joinRoom(roomId, mMainCameraId, mSideCameraId, avRootView, new XHLiveListener(){
            @Override
            public void onSuccess() {
                Toast.makeText(activity, "Enter Room Success", Toast.LENGTH_SHORT).show();
                sendTestMessage(2, "android 进来了");
                sendTestMessage(1, "android 哈哈");
                sendTestMessage(3, "android 退出");

            }
            @Override
            public void onError(String module, int errCode, String errMsg) {
                Toast.makeText(activity, "Enter Room Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void quitRoom() {
        XHLivePlayer.getInstance().showVideoView();
        avRootView.setVisibility(View.INVISIBLE);
        XHLiveManager.getInstance().quitRoom(new XHLiveListener(){
            @Override
            public void onSuccess() {
                Toast.makeText(activity, "quit Room Success", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(String module, int errCode, String errMsg) {
                Toast.makeText(activity, "quit Room Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void upToVideoMember() {
        if (! xhLiveManager.isEnterRoom()) {
            Log.e(TAG, "未进入房间");
            return;
        }
        XHLivePlayer.getInstance().pause();
        XHLivePlayer.getInstance().hideVideoView();
        avRootView.setVisibility(View.VISIBLE);
        xhLiveManager.upToVideoMember(new XHLiveListener(){
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError(String module, int errCode, String errMsg) {
                Toast.makeText(activity, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void downToVideoMember() {
        XHLivePlayer.getInstance().resume();
        xhLiveManager.downToVideoMember(new XHLiveListener(){
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError(String module, int errCode, String errMsg) {
                Toast.makeText(activity, errMsg, Toast.LENGTH_SHORT).show();
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

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(String message){
        //构造一条消息
        TIMMessage msg = new TIMMessage();
        //添加文本内容
        TIMTextElem elem = new TIMTextElem();
        elem.setText(message);
        //将 Elem 添加到消息
        if(msg.addElement(elem) != 0) {
            Log.d(TAG, "addElement failed");
            return;
        }
        TIMConversation timcon = TIMManager.getInstance().getConversation(TIMConversationType.Group, roomId + "");
        //发送消息
        timcon.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 含义请参见错误码表
                Log.d(TAG, "send message failed. code: " + code + " errmsg: " + desc);
            }
            @Override
            public void onSuccess(TIMMessage msg) {//发送消息成功
                Log.d(TAG, "SendMsg ok");
            }
        });
    }

    public void sendTestMessage(int type, String content) {
        String msg = String.format("{\"headimgurl\":\"http://thirdwx.qlogo.cn/mmopen/vi_32/VOqEEtr4SSwzN7H8JFLOrlEahcsm1H3VGgeg2hvdTzH4dzyxwaWfNiaJaOib72e2lUyqjqNCUg2JWyibHcDmYxhFw/132\",\"sendName\":\"Player\",\"msgType\":\"%d\",\"content\":\"%s\"}", type, content);
        sendMessage(msg);
    }
}
