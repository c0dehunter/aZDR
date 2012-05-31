package com.c0dehunter.aZDR;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class aZDRActivity extends Activity {
	private class getOutbreaksTask extends AsyncTask<String, Void, Elements[]>{
		private aZDRActivity parentActivity;
		
		public getOutbreaksTask(aZDRActivity parentActivity){
			this.parentActivity=parentActivity;
		}
		
		@Override
		protected Elements[] doInBackground(String... url) {
			Document doc = null;
			Elements[] pari=new Elements[2];
			try {
				doc = Jsoup.connect(url[0]).timeout(10*1000).get();
				pari[0] = doc.select("div.subc ul:eq(1) li span.date");  
				Elements links = doc.select("div.subc ul:eq(1) li a"); 
				
				pari[1]=new Elements();
				for(Element l:links){
					Document doc2=Jsoup.connect(l.attr("abs:href")).timeout(10*1000).get();
					pari[1].add(doc2.select("div#page-notice > p").get(2));
					//Log.d("as",doc2.select("div#page-notice p").first().html()+"");
				}
				//Log.d("as",pari[1].size()+"");
//				Log.d("as","date1: "+pari[0].get(0).text()+" - link1: "+pari[1].get(0).attr("href"));
				return pari;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Elements[] pari){
			if(!pari[0].isEmpty() && !pari[1].isEmpty()){
				TextView loading_txt=(TextView) parentActivity.findViewById(R.id.loading_txt);
				loading_txt.setVisibility(View.GONE);
				ProgressBar pb=(ProgressBar) parentActivity.findViewById(R.id.progressBar_loading_outbreaks);
				pb.setVisibility(View.GONE);
				
				ViewFlipper flipper=(ViewFlipper) parentActivity.findViewById(R.id.viewFlipper_outbreaks);
				flipper.setInAnimation(parentActivity, R.anim.fadein);
				flipper.setOutAnimation(parentActivity, R.anim.fadeout);
				for(int i=0; i<pari[0].size(); i++){
					TextView tv1=new TextView(parentActivity);
					TextView tv2=new TextView(parentActivity);

					String p_text=pari[1].get(i).text();
					int MAX_LENGTH=130;
					if(p_text.length() > MAX_LENGTH){
						p_text=p_text.substring(0, MAX_LENGTH);
						p_text+="...";
					}
					
//					if(p_text.length() > MAX_LENGTH){
//					    if(p_text.length()-p_text.lastIndexOf(" ")<4) p_text=p_text.substring(0,p_text.substring(0, p_text.lastIndexOf(" ")-1).lastIndexOf(" ")) + "...";
//					    else p_text=p_text.substring(0,p_text.lastIndexOf(" ")) + "...";
//					}
					tv1.setText(pari[0].get(i).text());
					tv2.setText(p_text);
	
					
					tv1.setPadding(40, 7, 0, 0);
					tv1.setTextColor(Color.BLACK);
					
					tv2.setPadding(40, 4, 0, 0);
					tv2.setTextColor(Color.DKGRAY);
					tv2.setMaxLines(4);

					LinearLayout wrapper=new LinearLayout(parentActivity);
					wrapper.setOrientation(1);
					wrapper.addView(tv1);
					wrapper.addView(tv2);
					flipper.addView(wrapper);	
				}
				
				flipper.setFlipInterval(6000);
				flipper.startFlipping();
			}
		}
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        if(isOnline()) new getOutbreaksTask(this).execute("http://wwwnc.cdc.gov/travel/notices.htm"); //pridobivanje outbreakov
        else {
        	Toast.makeText(getBaseContext(), "NI interneta", Toast.LENGTH_SHORT);
//        	AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this);
//        	TextView myMsg = new TextView(this);
//        	myMsg.setText("Please turn on internet connectivity and start again!");
//        	myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
//        	popupBuilder.setView(myMsg);
        }
    }
    
    public void showDestinations(View v){
    	Intent showNextActivity=new Intent(aZDRActivity.this, selectCountryActivity.class);
    	aZDRActivity.this.startActivity(showNextActivity);
    }
    
    public void showDiseases(View v){
    	Intent showNextActivity=new Intent(aZDRActivity.this, selectDiseaseActivity.class);
    	aZDRActivity.this.startActivity(showNextActivity);
    }
    
    public boolean isOnline() {
        ConnectivityManager cm =
            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}