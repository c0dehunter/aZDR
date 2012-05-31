package com.c0dehunter.aZDR;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class viewWikiActivity extends Activity{
	private class myWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
	}
	private WebView web_view;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.view_webview);

	    web_view = (WebView) findViewById(R.id.Webview);
	    web_view.setWebViewClient(new myWebViewClient());
	    web_view.getSettings().setJavaScriptEnabled(true);
	    Bundle extraInfo=getIntent().getExtras();
        String disease_name=extraInfo.getString("DISEASE");
	    web_view.loadUrl("http://en.wikipedia.org/wiki/"+disease_name);
	}
}
