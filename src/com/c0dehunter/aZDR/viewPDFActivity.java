package com.c0dehunter.aZDR;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class viewPDFActivity extends Activity{
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
	    web_view.loadUrl("http://docs.google.com/gview?embedded=true&url=http://www.cdc.gov/vaccines/pubs/vis/downloads/vis-flulive.pdf");
	}
}
