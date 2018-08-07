package tw.idv.fy.widget.webview.example;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import tw.idv.fy.widget.webview.WebViewSupportFragment;
import tw.idv.fy.widget.webview.utils.FileChooser;

public class MainActivityWebViewFragment extends WebViewSupportFragment {
    @Override
    public WebChromeClient getWebChromeClientInstance() {
        return new WebChromeClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                return FileChooser.Show(webView, filePathCallback, fileChooserParams);
            }
        };
    }
}
