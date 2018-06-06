package tw.idv.fy.widget.webview.example;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import tw.idv.fy.widget.webview.WebViewFragment;

public class NonAppCompatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_app_compat);

        Fragment f = getFragmentManager().findFragmentById(R.id.fragment);
        if (f instanceof WebViewFragment) {
            ((WebViewFragment) f).getWebView().loadUrl("http://www.yahoo.com.hk");
        }
    }
}
