package com.wuyou.worker;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.gs.buluo.common.BaseApplication;
import com.wuyou.worker.bean.DaoMaster;
import com.wuyou.worker.bean.DaoSession;
import com.wuyou.worker.bean.UserInfo;
import com.wuyou.worker.bean.UserInfoDao;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by hjn on 2016/11/1.
 */
public class CarefreeApplication extends BaseApplication {
    private static CarefreeApplication instance;
    private UserInfo userInfo;
    private UserInfoDao userInfoDao;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initDB();
        initIM();
    }

    private void initIM() {
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            RongIM.init(this);
            //连接服务器
            connect("/cQSg9SehzlP/3P3fh2lCtINp6kjuVs2IhiAJ2G9YOu643UnaN7ZbLLNIAW/jKZ/dLkzStDYZVfJc+viW04+Vg==");
        }
    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

    private void connect(String token) {

        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.d("LoginActivity", "--onSuccess" + userid);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("err", errorCode + "");
                }
            });
        }
    }

    private void initDB() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "carefree.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        userInfoDao = daoSession.getUserInfoDao();
    }

    public static synchronized CarefreeApplication getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {

    }


    @Override
    public String getFilePath() {
        return Environment.getExternalStorageDirectory().toString() + "/carefree/";
    }

    public UserInfo getUserInfo() {
        if (userInfo != null)
            return userInfo;
        else
            return userInfoDao.loadByRowId(0);
    }

    public void setUserInfo(UserInfo userInfo) {
        CarefreeApplication.getInstance().getUserInfoDao().insert(userInfo);
        this.userInfo = userInfo;
    }

    public void clearUserInfo() {
        userInfoDao.deleteAll();
        userInfo = null;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }
}
