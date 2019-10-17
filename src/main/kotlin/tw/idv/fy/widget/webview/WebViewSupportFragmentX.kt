package tw.idv.fy.widget.webview

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JsResult
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import tw.idv.fy.widget.webview.base.BaseWebViewSupportFragmentX

@SuppressLint("SetJavaScriptEnabled")
open class WebViewSupportFragmentX : BaseWebViewSupportFragmentX() {

    private companion object {
        init {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        }
    }

    private var mProgressBar: ProgressBar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            runCatching {
                inflater.inflate(R.layout.tw_idv_fy_widget_webview_layout_webview_fragment, container, false)
                        ?.run { this as? ViewGroup }
                        ?.apply {
                            addView(super.onCreateView(inflater, container, savedInstanceState), 0)
                            mProgressBar = findViewById(android.R.id.progress)
                        }
                        ?: super.onCreateView(inflater, container, savedInstanceState)
            }.getOrNull()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getWebView()?.apply(::onSettings)
    }

    fun getWebViewClientInstance(): WebViewClient = WebViewClient()

    fun getWebChromeClientInstance(): WebChromeClient = WebChromeClient()

    /**
     *  僅先預設幾個設定
     */
    protected fun onSettings(webView: WebView) {
        with(webView) {
            webViewClient = getWebViewClientInstance()
            webChromeClient = getWebChromeClientInstance()

            //setBackgroundColor(android.graphics.Color.TRANSPARENT)
            //isVerticalScrollBarEnabled = false // default: false
            //isHorizontalScrollBarEnabled = false // default: false

            with(settings) {
                domStorageEnabled = true // default: false
                databaseEnabled = true // default: false
                //saveFormData = false // default: true , but deprecated in API level 26
                javaScriptEnabled = true // default: false

                //allowContentAccess = false // default: true
                //allowFileAccess = false // default: true
                //allowFileAccessFromFileURLs = true // default: false
                /*
                    allow cross domain for javascript

                    (solution of Access-Control-Allow-Origin → Origin: file:///)
                 */
                //allowUniversalAccessFromFileURLs = true // default: false

                /*
                    跟 auto-play 有關係
                 */
                mediaPlaybackRequiresUserGesture = false // default: true

                //setSupportZoom(false) // default: true
                //builtInZoomControls = true // default: false
                displayZoomControls = false // default: true

                //loadWithOverviewMode = true // default: false
                useWideViewPort = false
                javaScriptCanOpenWindowsAutomatically = true // default: false

                /*
                    跟 http 和 https 混合資源有關
                 */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW // default: MIXED_CONTENT_NEVER_ALLOW
                }
            }
        }
    }

    internal fun showProgress() {
        mProgressBar?.visibility = View.VISIBLE
    }

    internal fun hideProgress() {
        mProgressBar?.postDelayed(1000L) {
            visibility = View.GONE
        }
    }

    open inner class WebViewClient : android.webkit.WebViewClient()

    open inner class WebChromeClient: android.webkit.WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress < 100) {
                showProgress()
            } else {
                hideProgress()
            }
        }

        override fun onJsAlert(view: WebView, url: String?, message: String?, result: JsResult?): Boolean {
            AlertDialog.Builder(view.context)
                       .setMessage(message)
                       .setCancelable(false)
                       .setPositiveButton(android.R.string.ok) { _, _ -> result?.confirm() }
                       .show()
            return true
        }

        override fun onJsConfirm(view: WebView, url: String?, message: String?, result: JsResult?): Boolean {
            AlertDialog.Builder(view.context)
                       .setMessage(message)
                       .setCancelable(false)
                       .setPositiveButton(android.R.string.ok) { _, _ -> result?.confirm() }
                       .setNegativeButton(android.R.string.no) { _, _ -> result?.cancel()  }
                       .show()
            return true
        }
    }

    private fun View.postDelayed(delayMillis: Long, block: View.() -> Unit) =
            postDelayed({ run(block) }, delayMillis)
}