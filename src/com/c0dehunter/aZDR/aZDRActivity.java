package com.c0dehunter.aZDR;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class aZDRActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
    }
    
    public void showDestinations(View v){
    	Intent showNextActivity=new Intent(aZDRActivity.this, selectCountryActivity.class);
    	aZDRActivity.this.startActivity(showNextActivity);
    }
    
    public void showDiseases(View v){
    	Intent showNextActivity=new Intent(aZDRActivity.this, selectDiseaseActivity.class);
    	aZDRActivity.this.startActivity(showNextActivity);
    }
}