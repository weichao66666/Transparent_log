package io.weichao.transparent_log.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import io.weichao.transparent_log.R;
import io.weichao.transparent_log.local_log.LocalLogListener;
import io.weichao.transparent_log.widget.WebChromeClientWithFullLog;
import io.weichao.transparent_log.widget.WebViewClientWithFullLog;

/**
 * Created by WEI CHAO on 2017/5/11.
 */
public class MainFragment extends Fragment implements MainFragmentView {
    private static final String TAG = "MainFragment";

    private String mUrl;

    private MainActivityView mMainActivityView;
    private LocalLogListener mLocalLogListener;

    private RelativeLayout mRootView;
    private WebView mWebView;

    private WebChromeClient mWebChromeClient = new WebChromeClientWithFullLog();
    private WebViewClient mWebViewClient = new WebViewClientWithFullLog() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            final String msg = "onPageStarted(" + url + ")";
            Log.i(TAG, msg);
            mLocalLogListener.clearLog();
            mLocalLogListener.printLog(msg);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            final String msg = "onLoadResource(" + url + ")";
            Log.i(TAG, msg);
            mLocalLogListener.printLog(msg);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            final String msg = "onPageFinished(" + url + ")";
            Log.i(TAG, msg);
            mLocalLogListener.printLog(msg);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String url) {
            final String msg = "onReceivedError(" + errorCode + ", " + description + ", " + url + ")";
            Log.i(TAG, msg);
            mLocalLogListener.printLog(4, msg);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            // 这个方法在6.0才出现
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                final String msg = "onReceivedError(" + request.getUrl().toString() + ", " + error.getDescription() + ")";
                Log.i(TAG, msg);
                mLocalLogListener.printLog(4, msg);
            }
        }
    };

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            mMainActivityView = (MainActivityView) context;
            mLocalLogListener = (LocalLogListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle arguments = getArguments();
        if (arguments != null) {
            mUrl = arguments.getString("url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (RelativeLayout) inflater.inflate(R.layout.fragment_main, null);

        mWebView = mRootView.findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        // 设置可控制拉伸
//        webSettings.setBuiltInZoomControls(true);
        // 解决在用户调整手机字体大小/用户调整浏览器字体大小后，布局错乱问题。
        webSettings.setTextZoom(100);
        // 设置 Java 可调用 JS 方法
//        webSettings.setJavaScriptEnabled(true);
        // 打开本地缓存
        webSettings.setDomStorageEnabled(true);

        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        // 设置硬件加速
//        WebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);

        webSettings.setDatabaseEnabled(true);

//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setSavePassword(true);
//        webSettings.setSaveFormData(true);
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setGeolocationEnabled(true);
//        webSettings.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
//        webSettings.setDomStorageEnabled(true);

        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mWebViewClient);
        // 设置 JS 可调用 Java 方法
//        mWebView.addJavascriptInterface(new LoginJsInteration(this), ReciteWordJsInteration.JAVA_INTERFACE);

        mWebView.loadUrl(mUrl);

        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.addJavascriptInterface(null, null);
            mWebView.stopLoading();
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mRootView.removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }
}