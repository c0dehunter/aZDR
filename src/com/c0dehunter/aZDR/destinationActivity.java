package com.c0dehunter.aZDR;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class destinationActivity extends Activity{
	
	private ExpandListAdapter ExpAdapter;
	private ArrayList<ExpandListGroup> ExpListItems;
	private ExpandableListView ExpandList;
	
	
	private class getDataTask extends AsyncTask<String, Void, Elements[]>{
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
		protected Elements[] doInBackground(String... url) {
			Document doc = null;
			Elements[] elementi = new Elements[2];
			try {
				doc = Jsoup.connect(url[0]).timeout(10*1000).get();
				elementi[0]= doc.select("div.vaccine-recommendations td > a");
				elementi[1]= doc.select("div.vaccine-recommendations tr td:eq(1)");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return elementi;
		}
		
		@Override
		protected void onPostExecute(Elements[] elementi){
			if(this.progress.isShowing()) this.progress.dismiss();
			
			if(elementi[0].isEmpty()){
				Toast.makeText(parentActivity, "Server seems unavailable, try again later.", Toast.LENGTH_LONG).show();
				destinationActivity.this.finish();
			}
			else{
				ArrayList<String> naslovi=new ArrayList<String>();
				ArrayList<String> opisi=new ArrayList<String>();
				
				for(Element naslov: elementi[0]){
					naslovi.add(naslov.text());
				}
				for(Element opis: elementi[1]){
					opisi.add(opis.text());
				}
				
				ExpandList = (ExpandableListView) findViewById(R.id.SeznamCepljenj);
		        ExpListItems = populateData(naslovi, opisi);
		        ExpAdapter = new ExpandListAdapter(destinationActivity.this, ExpListItems);
		        ExpandList.setAdapter(ExpAdapter);
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
	
	public ArrayList<ExpandListGroup> populateData(ArrayList<String> naslovi, ArrayList<String> opisi) {
		ArrayList<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
    	for(int i=0; i<naslovi.size(); i++){
	        ExpandListGroup gru1 = new ExpandListGroup();
	        gru1.setName(naslovi.get(i));
	        gru1.setDescription(opisi.get(i));
	        list.add(gru1);
    	}
        
        return list;
    }
	
	public void showDisease(View view){
		Intent showNextActivity=new Intent(destinationActivity.this, diseaseActivity.class);
    	Bundle extraInfo=new Bundle();
    	extraInfo.putString("DISEASE", ExpListItems.get(Integer.parseInt(view.getTag().toString())).getName());	
    	
    	showNextActivity.putExtras(extraInfo);
    	
    	destinationActivity.this.startActivity(showNextActivity);
	}

}
