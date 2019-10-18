package tw.idv.fy.widget.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JsResult
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import tw.idv.fy.widget.webview.base.AbWebChromeClient
import tw.idv.fy.widget.webview.base.AbWebViewClient
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
                (inflater.inflate(R.layout.tw_idv_fy_widget_webview_layout_webview_fragment, container, false) as? ViewGroup)
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

    @CallSuper
    open fun getWebViewClientInstance(): AbWebViewClient = WebViewClient()

    @CallSuper
    open fun getWebChromeClientInstance(): AbWebChromeClient = WebChromeClient()

    /**
     *  僅先預設幾個設定
     */
    @CallSuper
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
                //javaScriptCanOpenWindowsAutomatically = true // default: false; OpenWindowsAutomatically mean UN-requested popup

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

    private inner class WebViewClient : AbWebViewClient()

    private inner class WebChromeClient: AbWebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress < 100) {
                showProgress()
            } else {
                hideProgress()
            }
        }

        override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean =
                view?.apply {
                    AlertDialog.Builder(context)
                            .setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok) { _, _ -> result?.confirm() }
                            .show()
                } != null ?: super.onJsAlert(view, url, message, result)

        override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean =
                view?.apply {
                    AlertDialog.Builder(view.context)
                            .setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok) { _, _ -> result?.confirm() }
                            .setNegativeButton(android.R.string.no) { _, _ -> result?.cancel()  }
                            .show()
                } != null ?: super.onJsConfirm(view, url, message, result)

        /**
         * 因為 部分 Android WebView 對於 getDefaultVideoPoster 回傳 null 沒有處理 導致 crash
         *
         * 所以有需要的話覆寫他
         *
         * https://blog.csdn.net/Ltp_zy/article/details/80580446
         */
        override fun getDefaultVideoPoster(): Bitmap? = super.getDefaultVideoPoster()
                ?: runCatching {
                    BitmapFactory.decodeResource(resources, android.R.drawable.sym_def_app_icon)
                }.getOrNull()
    }

    private fun View.postDelayed(delayMillis: Long, block: View.() -> Unit) =
            postDelayed({ run(block) }, delayMillis)
}