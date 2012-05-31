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
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
				} catch (Exception e) {
					e.printStackTrace();
				}

				return elementi;
			}
			
			@Override
			protected void onPostExecute(Elements[] elems){
				if(this.progress.isShowing()) this.progress.dismiss();
							
				if(elems[0] == null){
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
					LinearLayout layout = (LinearLayout) parentActivity.findViewById(R.id.disease_scrollview_lin_layout);
					layout.addView(tv);
				
					Element img = elems[1].get(0);
				    ImageView imgView=new ImageView(parentActivity);
				    imgView.setPadding(getPixelsFromDip(10), 0, getPixelsFromDip(10), 0);
					URL newurl = null;
					boolean imageSet=false;
					try {
						newurl = new URL(img.attr("href"));
						
						Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
						imgView.setImageBitmap(mIcon_val);
						imageSet=true;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					imgView.setPadding(0, 0, 0, getPixelsFromDip(20));
				    layout.addView(imgView);
				    
				    LayoutInflater layoutInflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				    int indx=1;
				    if(imageSet) indx=2;
				    else indx=1;
				    layout.addView(layoutInflater.inflate(R.layout.pdf_wiki_footer, layout, false), 2);
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
	
	public int getPixelsFromDip(int dip){
		final float scale = getResources().getDisplayMetrics().density;
	    return (int) (dip * scale + 0.5f);
	}
	
	public void showPDF(View view){
		Intent showNextActivity=new Intent(diseaseActivity.this, viewPDFActivity.class);
    	Bundle extraInfo=new Bundle();
    	TextView txt_diseaseName=(TextView) findViewById(R.id.txt_diseaseName);
    	extraInfo.putString("DISEASE", (String) (txt_diseaseName.getText()));
    	
    	showNextActivity.putExtras(extraInfo);
    	diseaseActivity.this.startActivity(showNextActivity);
	}
	
	public void showWiki(View view){
		Intent showNextActivity=new Intent(diseaseActivity.this, viewWikiActivity.class);
    	Bundle extraInfo=new Bundle();
    	TextView txt_diseaseName=(TextView) findViewById(R.id.txt_diseaseName);
    	extraInfo.putString("DISEASE", (String) (txt_diseaseName.getText()));
    	
    	showNextActivity.putExtras(extraInfo);
    	diseaseActivity.this.startActivity(showNextActivity);
	}
}
