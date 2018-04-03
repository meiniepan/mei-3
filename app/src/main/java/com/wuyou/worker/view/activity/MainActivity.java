package com.wuyou.worker.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.R;
import com.wuyou.worker.adapter.MainPagerAdapter;
import com.wuyou.worker.mvp.order.MyOrderFragment;
import com.wuyou.worker.mvp.store.MineFragment;
import com.wuyou.worker.mvp.wallet.WalletFragment;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.view.widget.NoScrollViewPager;
import com.yinglan.alphatabs.AlphaTabsIndicator;
import com.yinglan.alphatabs.OnTabChangedListner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class MainActivity extends BaseActivity implements OnTabChangedListner {
    @BindView(R.id.main_tab)
    AlphaTabsIndicator bottomView;
    @BindView(R.id.main_pager)
    NoScrollViewPager viewPager;
    @BindView(R.id.main_title)
    TextView mainTitle;
    @BindView(R.id.main_title_area)
    View titleView;
    List<Fragment> fragments = new ArrayList<>();
    MyOrderFragment orderFragment = new MyOrderFragment();
    private long mkeyTime = 0;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        fragments.add(orderFragment);
        fragments.add(new WalletFragment());
        fragments.add(getMessageFragment());
        fragments.add(new MineFragment());
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragments));
        bottomView.setViewPager(viewPager);
        bottomView.setOnTabChangedListner(this);
        bottomView.setTabCurrenItem(0);
        initRC();
    }

    private void initRC() {
        if (getApplicationInfo().packageName.equals(CommonUtil.getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(CommonUtil.getCurProcessName(getApplicationContext()))) {
            RongIM.init(this);
            //连接服务器
            connect(CarefreeDaoSession.getInstance().getUserInfo().getRc_token());
        }
    }

    private void connect(String token) {
        if (getApplicationInfo().packageName.equals(CommonUtil.getCurProcessName(getApplicationContext()))) {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.e("err", "onTokenIncorrect");
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

    private Fragment getMessageFragment() {
        ConversationListFragment fragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getCtx().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();
        fragment.setUri(uri);
        return fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mkeyTime) > 2000) {
                mkeyTime = System.currentTimeMillis();
                ToastUtils.ToastMessage(getCtx(), "再按一次退出");
            } else {
                finish();
                System.exit(0);
            }
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTabSelected(int tabNum) {
        switch (tabNum) {
            case 0:
                mainTitle.setText(R.string.main_order);
                titleView.setVisibility(View.VISIBLE);
                break;
            case 1:
                titleView.setVisibility(View.GONE);
                break;
            case 2:
                mainTitle.setText(R.string.message);
                titleView.setVisibility(View.VISIBLE);
                break;
            case 3:
                titleView.setVisibility(View.GONE);
                break;

        }
    }
}
