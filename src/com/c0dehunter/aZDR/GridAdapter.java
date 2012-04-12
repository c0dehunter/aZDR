package com.c0dehunter.aZDR;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter{

	private ArrayList<String> countries;
	private String[] flags;
	private Activity rootActivity;
	private AssetManager manager;
	
	public GridAdapter(Activity activity, ArrayList<String> countries, String[] flags, AssetManager manager){
		this.countries=countries;
		this.flags=flags;
		this.rootActivity=activity;
		this.manager=manager;
	}
	
	public int getCount() {
		return countries.size();
	}

	public Object getItem(int index) {
		return countries.get(index);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	
	
	public static class ViewHolder{
		public ImageView imgFlag;
		public TextView txtCountry;
	}

	public View getView(int index, View inView, ViewGroup parent) {
		ViewHolder view;
		LayoutInflater inflator=rootActivity.getLayoutInflater();
		
		if(inView==null){
			view=new ViewHolder();
			inView=inflator.inflate(R.layout.gridrow, null);
			
			view.txtCountry=(TextView) inView.findViewById(R.id.textView_country_name);
			view.imgFlag=(ImageView) inView.findViewById(R.id.imageView_flag);
			
			inView.setTag(view);
		}
		else{
			view=(ViewHolder) inView.getTag();
		}
		
		try {
			view.txtCountry.setText(countries.get(index));
			BufferedInputStream buf=new BufferedInputStream(manager.open("flags/"+flags[index]));
			Bitmap bitmap=BitmapFactory.decodeStream(buf);
			view.imgFlag.setImageBitmap(bitmap);
			view.imgFlag.setBackgroundResource(R.drawable.flag_border);
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return inView;
	}
}
