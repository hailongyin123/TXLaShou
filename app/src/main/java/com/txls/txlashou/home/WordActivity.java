package com.txls.txlashou.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.txls.txlashou.R;

/**
 * 注册协议word预览
 */
public class WordActivity extends Activity {
    //private PoiUtils poiUtils;
    ImageView img_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        img_return = (ImageView) findViewById(R.id.img_return);
        img_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //poiUtils = new PoiUtils();
        try {
            //poiUtils.convert2Html(Environment.getExternalStorageDirectory().getPath() + "/txlashou/content.doc",this.getWindowManager().getDefaultDisplay().getWidth() - 10,this);
            WebView webView = (WebView) this.findViewById(R.id.webview);
            WebSettings webSettings = webView.getSettings();
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setTextSize(WebSettings.TextSize.SMALLER);
            //展示html格式的协议
            webView.loadUrl("file:///android_asset/content.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
