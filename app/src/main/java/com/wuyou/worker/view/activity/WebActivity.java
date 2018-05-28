package com.wuyou.worker.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.gs.buluo.common.BaseApplication;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.Utils;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by hjn on 2017/2/28.
 */
public class WebActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.web_title)
    TextView tvTitle;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setUpWebView();
        String url = getIntent().getStringExtra(Constant.WEB_URL);
        int type = getIntent().getIntExtra(Constant.WEB_TYPE, 0);
        if (url != null) {
            webView.loadUrl(url);
        }
    }

    private void setUpWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLightTouchEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(false);
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        String deviceInfo = Utils.getDeviceInfo(BaseApplication.getInstance().getApplicationContext()) + " app/Android";
        webSettings.setUserAgentString(deviceInfo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        webView.requestFocusFromTouch();
//        webView.requestFocus();
//        webView.requestFocus(View.FOCUS_DOWN|View.FOCUS_UP);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e("Test", "onPageStarted: " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("Test", "shouldOverrideUrlLoading: " + url);
                try {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        view.loadUrl(url);
                        return true;
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return false;
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                Log.e("Test", "onReceivedError: " + errorCode + "..." + description);
                switch (errorCode) {
                    case 404:
                    case 500:
                        break;
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
//                tvTitle.setText(title);
            }
        });


        //js调用本地方法
        webView.addJavascriptInterface(new JSCallNativeInterface(), "root");
    }


    private String loadJSMethod(String methodName, String json) {
        final String[] result = new String[1];
        String js = "javascript:" + methodName + "(" + json + ")";
        Log.e("Test", "loadJSMethod: " + js);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            webView.evaluateJavascript(js, value -> {
                result[0] = value;
            });
        } else {
            webView.loadUrl(js);
        }
        return result[0];
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_web;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        webView.clearHistory();

        ((ViewGroup) webView.getParent()).removeView(webView);
        webView.destroy();
        webView = null;
        super.onDestroy();
    }

    private class JSCallNativeInterface {
        @JavascriptInterface
        public void hybridProtocol(String json) {
        }
    }
}
