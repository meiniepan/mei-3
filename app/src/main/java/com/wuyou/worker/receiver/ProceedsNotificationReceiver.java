package com.wuyou.worker.receiver;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.gs.buluo.common.utils.AppManager;
import com.wuyou.worker.Constant;
import com.wuyou.worker.bean.entity.NotificationDataEntity;
import com.wuyou.worker.bean.entity.NotificationOrderEntity;
import com.wuyou.worker.mvp.order.PayDoneActivity;
import com.wuyou.worker.mvp.order.ProceedsQrActivity;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by Solang on 2018/7/16.
 */

public class ProceedsNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        Log.e("onNotifedPushContent", "========");

        NotificationOrderEntity entity = new Gson().fromJson(pushNotificationMessage.getExtra(), NotificationOrderEntity.class);
        if (entity != null) {
            String orderId = entity.order_id;
            if (!TextUtils.isEmpty(orderId) && AppManager.getAppManager().currentActivity().getClass().isInstance(ProceedsQrActivity.class)) {
                Log.e("orderId", entity.order_id);
                Intent intent = new Intent(context, PayDoneActivity.class);
                intent.putExtra(Constant.ORDER_ID, orderId);
                context.startActivity(intent);
            }
        }
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }
}
