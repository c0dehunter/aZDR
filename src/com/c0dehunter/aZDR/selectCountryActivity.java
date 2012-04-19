package com.c0dehunter.aZDR;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class selectCountryActivity extends Activity{
    
	private GridAdapter gridAdapter;
	private ArrayList<String> countries;
	private String[] flags;
	private AssetManager manager;
	private GridView gridView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.destinations_select);
        
        populateArrays();
        
        gridAdapter=new GridAdapter(this, countries, flags, manager);
        
        gridView=(GridView) findViewById(R.id.gridView_countryList);
        gridView.setAdapter(gridAdapter);
    }
    
    private void populateArrays(){  	
    	manager=this.getApplicationContext().getAssets();
    	
    	countries=new ArrayList<String>();
    	try {
			flags=manager.list("flags");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	for(int i=0; i<flags.length; i++){
    		String filename=flags[i];
    		filename=filename.replace("--", ",");
			filename=filename.replace("-", " ");
			filename=filename.replace(".jpg", "");
			
			countries.add(filename);
    	}
    }
    
    public void handleClick(View v){
    	TextView textView = (TextView) v.findViewById(R.id.textView_country_name);
    	String country_name=textView.getText().toString();
    	int index=-1;
    	for(int i=0; i<countries.size(); i++){
    		String s=countries.get(i);
    		if(s.equals(country_name)){
    			index=i;
    			break;
    		}
    	}
    	
    	
    	Intent showNextActivity=new Intent(selectCountryActivity.this, destinationActivity.class);
    	Bundle extraInfo=new Bundle();
    	extraInfo.putString("COUNTRY", country_name);
    	extraInfo.putString("ASSET_NAME", flags[index]);
    	
    	showNextActivity.putExtras(extraInfo);
    	
    	selectCountryActivity.this.startActivity(showNextActivity);
    }
}
