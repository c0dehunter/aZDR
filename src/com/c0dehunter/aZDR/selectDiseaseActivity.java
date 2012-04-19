package com.c0dehunter.aZDR;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class selectDiseaseActivity extends Activity{
	
	private ArrayAdapter<String> adapter;
	private ArrayList<String> diseases;
	private class getDataTask extends AsyncTask<String, Void, Elements>{
		private selectDiseaseActivity parentActivity;
		private ProgressDialog progress;
		
		public getDataTask(selectDiseaseActivity parent_activity){
			this.parentActivity=parent_activity;
			progress=new ProgressDialog(parent_activity);
		}
		
		@Override
		protected void onPreExecute(){
			 progress.setMessage("Retrieving diseases...");
			 progress.show();
		}
		
		@Override
		protected Elements doInBackground(String... url) {
			Document doc = null;
			try {
				doc = Jsoup.connect(url[0]).timeout(10*1000).get();
				return doc.select("div.azindex span.head");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return new Elements();
		}
		
		@Override
		protected void onPostExecute(Elements parsed_elements){
			if(this.progress.isShowing()) this.progress.dismiss();
			
			if(parsed_elements.isEmpty()){
				Toast.makeText(parentActivity, "Server seems unavailable, try again later.", Toast.LENGTH_LONG).show();
				selectDiseaseActivity.this.finish();
			}
			else{
				for(Element el: parsed_elements) diseases.add(el.text());
				adapter.notifyDataSetChanged();
			}
		}
	}

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.disease_select);
        diseases=new ArrayList<String>();
        adapter=new ArrayAdapter<String>(this,R.layout.listitem, diseases);
        ListView list=(ListView)findViewById(R.id.listView_diseases);
        list.setAdapter(adapter);
        
        populateList();
        
        list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				Toast.makeText(getApplicationContext(), ((TextView)view).getText(), Toast.LENGTH_LONG).show();	
			}
		});
    }

	private void populateList(){
		String url="http://www.vaccinestoday.eu/diseases/";
        new getDataTask(this).execute(url);
	}
}
