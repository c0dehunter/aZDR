package com.c0dehunter.aZDR;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class getDataTask extends AsyncTask<String, Void, Elements>{

	private destinationActivity parentActivity;
	private ProgressDialog progress;
	
	public getDataTask(destinationActivity parentActivity){
		this.parentActivity=parentActivity;
		progress=new ProgressDialog(parentActivity);
	}
	
	@Override
	protected void onPreExecute(){
		 progress.setMessage("Retrieving data...");
		 progress.show();
	}
	
	@Override
	protected Elements doInBackground(String... url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url[0]).timeout(10*1000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc.select("div.vaccine-recommendations td > a");
	}
	
	@Override
	protected void onPostExecute(Elements links){
		if(this.progress.isShowing()) this.progress.dismiss();
		
		for(Element link: links){
			TextView tv=new TextView(parentActivity);
			LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			lp.setMargins(10, 0, 0, 20);
			tv.setLayoutParams(lp);
			tv.setTextColor(Color.BLACK);
			tv.setText(link.text());
			LinearLayout layout = (LinearLayout) parentActivity.findViewById(R.id.links);
			layout.addView(tv);
			
		}
	}

}
