package tw.idv.fy.widget.webview.base

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Message
import android.view.KeyEvent
import android.webkit.*
import androidx.annotation.RequiresApi

@Suppress("DEPRECATION")
open class AbWebViewClient(private val delegate: WebViewClient? = null) : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        delegate?.onPageFinished(view, url)
                ?: super.onPageFinished(view, url)
    }

    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? =
            delegate?.shouldInterceptRequest(view, url)
                    ?: super.shouldInterceptRequest(view, url)

    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? =
            delegate?.shouldInterceptRequest(view, request)
                    ?: super.shouldInterceptRequest(view, request)

    override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean =
            delegate?.shouldOverrideKeyEvent(view, event)
                    ?: super.shouldOverrideKeyEvent(view, event)

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onSafeBrowsingHit(view: WebView?, request: WebResourceRequest?, threatType: Int, callback: SafeBrowsingResponse?) {
        delegate?.onSafeBrowsingHit(view, request, threatType, callback)
                ?: super.onSafeBrowsingHit(view, request, threatType, callback)
    }

    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
        delegate?.doUpdateVisitedHistory(view, url, isReload)
                ?: super.doUpdateVisitedHistory(view, url, isReload)
    }

    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
        delegate?.onReceivedError(view, errorCode, description, failingUrl)
                ?: super.onReceivedError(view, errorCode, description, failingUrl)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        delegate?.onReceivedError(view, request, error)
                ?: super.onReceivedError(view, request, error)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean =
            delegate?.onRenderProcessGone(view, detail)
                    ?: super.onRenderProcessGone(view, detail)

    override fun onReceivedLoginRequest(view: WebView?, realm: String?, account: String?, args: String?) {
        delegate?.onReceivedLoginRequest(view, realm, account, args)
                ?: super.onReceivedLoginRequest(view, realm, account, args)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
        delegate?.onReceivedHttpError(view, request, errorResponse)
                ?: super.onReceivedHttpError(view, request, errorResponse)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        delegate?.onPageStarted(view, url, favicon)
                ?: super.onPageStarted(view, url, favicon)
    }

    override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
        delegate?.onScaleChanged(view, oldScale, newScale)
                ?: super.onScaleChanged(view, oldScale, newScale)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean =
            delegate?.shouldOverrideUrlLoading(view, url)
                    ?: super.shouldOverrideUrlLoading(view, url)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean =
            delegate?.shouldOverrideUrlLoading(view, request)
                    ?: super.shouldOverrideUrlLoading(view, request)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPageCommitVisible(view: WebView?, url: String?) {
        delegate?.onPageCommitVisible(view, url)
                ?: super.onPageCommitVisible(view, url)
    }

    override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
        delegate?.onUnhandledKeyEvent(view, event)
                ?: super.onUnhandledKeyEvent(view, event)
    }

    override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
        delegate?.onReceivedClientCertRequest(view, request)
                ?: super.onReceivedClientCertRequest(view, request)
    }

    override fun onReceivedHttpAuthRequest(view: WebView?, handler: HttpAuthHandler?, host: String?, realm: String?) {
        delegate?.onReceivedHttpAuthRequest(view, handler, host, realm)
                ?: super.onReceivedHttpAuthRequest(view, handler, host, realm)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        delegate?.onReceivedSslError(view, handler, error)
                ?: super.onReceivedSslError(view, handler, error)
    }

    override fun onTooManyRedirects(view: WebView?, cancelMsg: Message?, continueMsg: Message?) {
        delegate?.onTooManyRedirects(view, cancelMsg, continueMsg)
                ?: super.onTooManyRedirects(view, cancelMsg, continueMsg)
    }

    override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
        delegate?.onFormResubmission(view, dontResend, resend)
                ?: super.onFormResubmission(view, dontResend, resend)
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        delegate?.onLoadResource(view, url)
                ?: super.onLoadResource(view, url)
    }
}