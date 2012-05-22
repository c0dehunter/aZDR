package com.c0dehunter.aZDR;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class diseaseActivity extends Activity{
		private class getDataTask extends AsyncTask<String, Void, Elements[]>{
			private diseaseActivity parentActivity;
			private ProgressDialog progress;
			
			public getDataTask(diseaseActivity parentActivity){
				this.parentActivity=parentActivity;
				progress=new ProgressDialog(parentActivity);
			}
			
			@Override
			protected void onPreExecute(){
				 progress.setMessage("Retrieving disease info...");
				 progress.show();
			}
			
			@Override
			protected Elements[] doInBackground(String... url) {
				Document doc1 = null;
				Document doc2 = null;
				Elements[] elementi=new Elements[2];
				try {
					doc1 = Jsoup.connect(url[0]).timeout(10*1000).get();
					elementi[0]= doc1.select("div.post p");
					
					//za sliko
					doc2 = Jsoup.connect(url[1]).timeout(10*1000).get();
					elementi[1]= doc2.select("li a.thumbnail");
					Log.d("",doc2.text());
				    
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.d("","dela");
				return elementi;
			}
			
			@Override
			protected void onPostExecute(Elements[] elems){
				if(this.progress.isShowing()) this.progress.dismiss();
				
				Log.d("","dela2");
				
				if(elems[0].isEmpty()){
					Toast.makeText(parentActivity, "Server seems unavailable, try again later.", Toast.LENGTH_LONG).show();
					diseaseActivity.this.finish();
				}
				else{
					TextView tv=new TextView(parentActivity);
					LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
					lp.setMargins(10, 0, 0, 20);
					tv.setLayoutParams(lp);
					tv.setTextColor(Color.BLACK);
					tv.setText(elems[0].get(3).text()+"\n\n"+elems[0].get(4).text());
					LinearLayout layout = (LinearLayout) parentActivity.findViewById(R.id.disease_info);
					layout.addView(tv);
					

					
					Log.d("","dela3 #"+elems[1].size());
					Element img = elems[1].get(0);
					Log.d("","dela4");
				    ImageView imgView=new ImageView(parentActivity);
				    Log.d("","dela5");
				    //imgView.setImageDrawable(Drawable.createFromPath(img.absUrl("src")));
					URL newurl = null;
					try {
						newurl = new URL(img.attr("href"));
						Log.d("",img.attr("href"));
						
						Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
						imgView.setImageBitmap(mIcon_val);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				    Log.d("","dela6");
				   // Log.d("",img.attr("src"));
				    layout.addView(imgView);
				    Log.d("","dela7");
				    
				}
			}
		}

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.disease);
        
        Bundle extraInfo=getIntent().getExtras();
        String disease_name=extraInfo.getString("DISEASE");
        
        TextView txt_diseaseName=(TextView) findViewById(R.id.txt_diseaseName);
        txt_diseaseName.setText(disease_name);

        //Toast t=Toast.makeText(getBaseContext(), "http://wwwnc.cdc.gov/travel/destinations/"+country_name.toLowerCase()+".htm", Toast.LENGTH_LONG);
       //t.show();
        System.setProperty("http.keepAlive", "false");
        String url1="http://www.vaccinestoday.eu/diseases/disease/"+disease_name.toLowerCase()+"/";
        String url2="http://www.exalead.com/search/image/results/?q="+disease_name;
        new getDataTask(this).execute(url1,url2);
    }
}
