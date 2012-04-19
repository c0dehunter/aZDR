package com.c0dehunter.aZDR;

import java.io.BufferedInputStream;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class destinationActivity extends Activity{
	
	private class getDataTask extends AsyncTask<String, Void, Elements>{
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
				return doc.select("div.vaccine-recommendations td > a");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new Elements();
		}
		
		@Override
		protected void onPostExecute(Elements links){
			if(this.progress.isShowing()) this.progress.dismiss();
			
			if(links.isEmpty()){
				Toast.makeText(parentActivity, "Server seems unavailable, try again later.", Toast.LENGTH_LONG).show();
				destinationActivity.this.finish();
			}
			else{
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
	}

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.destination);
        
        Bundle extraInfo=getIntent().getExtras();
        String country_name=extraInfo.getString("COUNTRY");
        String asset_name=extraInfo.getString("ASSET_NAME");
        
        ImageView img_flag=(ImageView) findViewById(R.id.img_flag);
        TextView txt_countryName=(TextView) findViewById(R.id.txt_countryName);
        txt_countryName.setText(country_name);
        
        try {
	        AssetManager manager=this.getApplicationContext().getAssets();
	        BufferedInputStream buf=new BufferedInputStream(manager.open("flags/"+asset_name));
			Bitmap bitmap=BitmapFactory.decodeStream(buf);
			img_flag.setImageBitmap(bitmap);
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

        //Toast t=Toast.makeText(getBaseContext(), "http://wwwnc.cdc.gov/travel/destinations/"+country_name.toLowerCase()+".htm", Toast.LENGTH_LONG);
       //t.show();
        String url="http://wwwnc.cdc.gov/travel/destinations/"+country_name.toLowerCase()+".htm";
        new getDataTask(this).execute(url);
    }
}
