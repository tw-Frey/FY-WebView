package tw.idv.fy.widget.webview;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import tw.idv.fy.widget.webview.base.BaseWebViewSupportFragment;

@SuppressLint("SetJavaScriptEnabled")
@SuppressWarnings("unused")
public class WebViewSupportFragment extends BaseWebViewSupportFragment {

    static {
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
    }
    ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tw_idv_fy_widget_webview_layout_webview_fragment, container, false);
        if (view instanceof ViewGroup) {
            ((ViewGroup) view).addView(super.onCreateView(inflater, container, savedInstanceState), 0);
            mProgressBar = view.findViewById(android.R.id.progress);
            return view;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onSettings(getWebView());
    }

    public WebViewClient getWebViewClientInstance() {
        return new WebViewClient();
    }

    public WebChromeClient getWebChromeClientInstance() {
        return new WebChromeClient();
    }

    /**
     *  僅先預設幾個設定
     */
    protected void onSettings(WebView webView) {
        webView.setWebViewClient(getWebViewClientInstance());
        webView.setWebChromeClient(getWebChromeClientInstance());

        //webView.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        //webView.setVerticalScrollBarEnabled(false);  // default: false
        //webView.setHorizontalScrollBarEnabled(false);  // default: false

        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true); // default: false
        settings.setDatabaseEnabled(true); // default: false
        //settings.setSaveFormData(false);  // default: true
        settings.setJavaScriptEnabled(true);  // default: false

        //settings.setAllowContentAccess(false);  // default: true
        //settings.setAllowFileAccess(false);  // default: true
        //settings.setAllowFileAccessFromFileURLs(true); // default: false
        /*
            allow cross domain for javascript
            (solution of Access-Control-Allow-Origin → Origin: file:///)
         */
        //settings.setAllowUniversalAccessFromFileURLs(true); // default: false

        /*
            跟 auto-play 有關係
         */
        settings.setMediaPlaybackRequiresUserGesture(false); // default: true

        //settings.setSupportZoom(false);  // default: true
        //settings.setBuiltInZoomControls(true);  // default: false
        settings.setDisplayZoomControls(false);  // default: true

        //settings.setLoadWithOverviewMode(true);  // default: false
        settings.setUseWideViewPort(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);  // default: false

        /*
            跟 http 和 https 混合資源有關
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW); // default: MIXED_CONTENT_NEVER_ALLOW
        }
    }

    void showProgress() {
        if (mProgressBar == null) return;
        mProgressBar.setVisibility(View.VISIBLE);
    }

    void hideProgress() {
        if (mProgressBar == null) return;
        mProgressBar.postDelayed(() -> mProgressBar.setVisibility(View.GONE), 1000);
    }

    public class WebViewClient extends android.webkit.WebViewClient {
//
//        @RequiresApi(Build.VERSION_CODES.N)
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            android.util.Log.wtf("Faty", "shouldOverrideUrlLoading = " + request.getUrl().toString());
//            return super.shouldOverrideUrlLoading(view, request);
//        }
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            android.util.Log.i("Faty", "shouldOverrideUrlLoading = " + url);
//            return super.shouldOverrideUrlLoading(view, url);
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            android.util.Log.v("Faty", "onPageStarted = " + url);
//            super.onPageStarted(view, url, favicon);
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            android.util.Log.d("Faty", "onPageFinished = " + url);
//            super.onPageFinished(view, url);
//        }
//
//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//            android.util.Log.v("Faty", "shouldInterceptRequest = " + request.getUrl().toString());
//            return super.shouldInterceptRequest(view, request);
//        }
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress < 100) showProgress();
            else hideProgress();
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            new AlertDialog.Builder(view.getContext())
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm())
                    .show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            new AlertDialog.Builder(view.getContext())
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm())
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> result.cancel())
                    .show();
            return true;
        }
    }
}
