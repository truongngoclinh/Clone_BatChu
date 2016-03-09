package com.smtech.batchu.control;

import com.sutech.batchu.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LevelListActivity extends Activity {

	private Context context;
	public static float density;
	private Button btnOk;

	private final int DELAY_TIME = 2000;
	private Handler delayHander;
	private Runnable r;

	public static String DATA_UNLOCK_LEVEL = "current_level_unlocked";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_level_list);
		context = this;
		Typeface V_de_vacia_font = Typeface.createFromAsset(getAssets(),
				"fonts/V_devacia.ttf");
		btnOk = (Button) findViewById(R.id.btnGo);
		btnOk.setTypeface(V_de_vacia_font);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						MainScreenActivity.class);
				startActivity(i);
				delayHander.removeCallbacks(r);
				finish();

			}
		});
		delayHander = new Handler();
		r = new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(getApplicationContext(),
						MainScreenActivity.class);
				startActivity(i);
				finish();
			}
		};
		delayHander.postDelayed(r, DELAY_TIME);

	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}
