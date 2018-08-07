package tw.idv.fy.widget.webview.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class FileChooser extends Activity {

    private static final String EXTRA_KEY_RESULT_MESSENGER = "result_messenger";
    private static final String EXTRA_KEY_RESULT_FILES_URI = "result_files_uri";
    private static final String EXTRA_KEY_OPEN_FILE_INTENT = "open_file_intent";
    private static final int REQUEST_CODE_OPEN_FILE_INTENT = EXTRA_KEY_OPEN_FILE_INTENT.hashCode() & 0xFFFF;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent open_file_intent = getIntent().getParcelableExtra(EXTRA_KEY_OPEN_FILE_INTENT);
            if (open_file_intent == null) throw new NullPointerException("no chose file intent");
            startActivityForResult(
                    Intent.createChooser(open_file_intent, "Pick one file"),
                    REQUEST_CODE_OPEN_FILE_INTENT
            );
            return;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        finish(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri[] uris = null;
        if (requestCode == REQUEST_CODE_OPEN_FILE_INTENT && resultCode == Activity.RESULT_OK) {
            ClipData clipData = data.getClipData();
            if (clipData == null) {
                uris = WebChromeClient.FileChooserParams.parseResult(resultCode, data);
            } else {
                int index, count = clipData.getItemCount();
                uris = new Uri[count];
                for (index = 0; index < count; index++) {
                    uris[index] = clipData.getItemAt(index).getUri();
                }
            }
        }
        finish(uris);
    }

    private void finish(@Nullable Uri[] uris) {
        try {
            Messenger result_messenger = getIntent().getParcelableExtra(EXTRA_KEY_RESULT_MESSENGER);
            if (result_messenger == null) throw new NullPointerException("no callback messenger");
            Message message = Message.obtain(null, REQUEST_CODE_OPEN_FILE_INTENT);
            message.getData().putParcelableArray(EXTRA_KEY_RESULT_FILES_URI, uris);
            result_messenger.send(message);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        finish();
    }

    @SuppressWarnings("unused")
    public static boolean Show(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        if (fileChooserParams.isCaptureEnabled()) {
            Toast.makeText(webView.getContext(), "婉拒 capture", Toast.LENGTH_SHORT).show();
            return false; // 本輯不處理 Capture
        }
        Context context = webView.getContext();
        Messenger result_messenger = new Messenger(new Handler(msg -> {
            Uri[] uris = null;
            if (msg.what == REQUEST_CODE_OPEN_FILE_INTENT && msg.peekData() != null) {
                uris = (Uri[]) msg.peekData().getParcelableArray(EXTRA_KEY_RESULT_FILES_URI);
            }
            if (uris == null) {
                Toast.makeText(context, "沒選到", Toast.LENGTH_SHORT).show();
            }
            filePathCallback.onReceiveValue(uris);
            return true;
        }));
        try {
            Intent open_file_intent = fileChooserParams.createIntent();
            String[] accept_Types = fileChooserParams.getAcceptTypes();
            if (accept_Types != null && accept_Types.length < 2) { // 有的機種會黏在一起
                accept_Types = accept_Types[0].split(",");
            }
            if (accept_Types == null || accept_Types.length < 2) { // 有的機種會黏在一起
                accept_Types = String.valueOf(open_file_intent.getType()).split(",");
            }
            if (accept_Types.length > 1) {
                open_file_intent.setType("*/*")
                        .putExtra(Intent.EXTRA_MIME_TYPES, accept_Types);
            }
            if (fileChooserParams.getMode() == WebChromeClient.FileChooserParams.MODE_OPEN_MULTIPLE) {
                open_file_intent
                        .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
            context.startActivity(new Intent(context, FileChooser.class)
                    .putExtra(EXTRA_KEY_OPEN_FILE_INTENT, open_file_intent)
                    .putExtra(EXTRA_KEY_RESULT_MESSENGER, result_messenger)
            );
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
