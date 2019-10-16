package com.mycheckins;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {
    String mUrl="https://www.wikihow.com/Check-In-on-Facebook";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView=(WebView)findViewById(R.id.gameWv);

        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setAllowFileAccessFromFileURLs(true);
        // webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setDomStorageEnabled(true);
        // webView.setWebViewClient(client);
        //  webView.setWebViewClient(new IgnoreSSLErrorWebViewClient());




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSetting.setMediaPlaybackRequiresUserGesture(false);
        }
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        // webView.setWebViewClient(new MyWebClient());
        System.out.println("url : " + mUrl);

        webView.loadUrl(mUrl);

}
}
