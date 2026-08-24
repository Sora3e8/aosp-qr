package com.sora3e8.aosp_qr;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.webkit.WebViewAssetLoader;

import androidx.annotation.NonNull;
import androidx.webkit.WebViewClientCompat;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


public class qrscanner extends WebView
{

    public qrscanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    final WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this.getContext())).build();

    public interface on_qrdata { void qrdata_received(String data); }
    public static on_qrdata qrdata_interface;
    public void setOnQRdataListener(on_qrdata listener){ qrdata_interface = listener; }
    @JavascriptInterface
    public void QRdata(String data) { qrdata_interface.qrdata_received(data);}
    public void stopScanner() { this.evaluateJavascript("scanner.stop();",null);}

    @SuppressLint("SetJavaScriptEnabled")
    public void setup()
    {
        super.setWebViewClient(new WebViewClientCompat() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }
            @Override
            @SuppressWarnings("deprecation") // for API < 21
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return assetLoader.shouldInterceptRequest(Uri.parse(url));
            }
        });

        WebSettings view_settings = super.getSettings();
        view_settings.setJavaScriptEnabled(true);
        view_settings.setAllowContentAccess(true);
        view_settings.setAllowFileAccess(true);
        view_settings.setDomStorageEnabled(true);
        super.setBackgroundColor(Color.TRANSPARENT);


        super.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request)
            {
                request.grant(request.getResources());

            }
        });
        super.loadUrl("https://appassets.androidplatform.net/assets/webview/index.html");
        //super.loadUrl("file:///android_asset/webview/index.html");
        super.addJavascriptInterface(this,"webview");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setup();

        //.reload();
    }
}