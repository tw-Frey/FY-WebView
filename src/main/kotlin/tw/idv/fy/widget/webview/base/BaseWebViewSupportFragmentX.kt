package tw.idv.fy.widget.webview.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment

/**
 * 仿作 from {@link android.webkit.WebViewFragment}
 *
 * used to {@link androidx.fragment.app.Fragment}
 *
 * in Kotlin
 */
open class BaseWebViewSupportFragmentX : Fragment() {
    
    private var mWebView: WebView? = null
    private var mIsWebViewAvailable = false

    /**
     * Called to instantiate the view. Creates and returns the WebView.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            WebView(context).apply {
                mWebView?.destroy()
                mWebView = this
                mIsWebViewAvailable = true
            }

    /**
     * Called when the fragment is visible to the user and actively running. Resumes the WebView.
     */
    override fun onPause() {
        super.onPause()
        mWebView?.onPause()
    }

    /**
     * Called when the fragment is no longer resumed. Pauses the WebView.
     */
    override fun onResume() {
        mWebView?.onResume()
        super.onResume()
    }

    /**
     * Called when the WebView has been detached from the fragment.
     *
     * The WebView is no longer available after this time.
     */
    override fun onDestroyView() {
        mIsWebViewAvailable = false
        super.onDestroyView()
    }

    /**
     * Called when the fragment is no longer in use. Destroys the internal state of the WebView.
     */
    override fun onDestroy() {
        mWebView?.destroy()
        mWebView = null
        super.onDestroy()
    }

    /**
     * Gets the WebView.
     */
    fun getWebView(): WebView? = if (mIsWebViewAvailable) mWebView else null
}