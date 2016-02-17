package com.smtech.batchu;

import java.util.Currency;

import com.sutech.batchu.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

public class LevelListActivity extends Activity {

	// private GridView gridView;
	// private LevelListAdapter gridAdapter;
	private Context context;
	// private int[] totalLevel = new int[15];
	// private int width = 0;
	// private int height = 0;
	// private final int COLLUMS = 3;
	// private final int PADDING = 5;
	// private final static int REQUEST_NEW_LEVEL = 0;
	// private static int levelUnlock = 1;
	public static float density;
	private Button btnOk;

	private final int DELAY_TIME = 2000;
	private Handler delayHander;
	private Runnable r;

	public static String DATA_UNLOCK_LEVEL = "current_level_unlocked";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("LINH", "onCreate()");
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
//				overridePendingTransition(R.anim.anim_come_from_right,
//						R.anim.anim_out_to_left);

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
//				overridePendingTransition(R.anim.anim_come_from_right,
//						R.anim.anim_out_to_left);

			}
		};
		delayHander.postDelayed(r, DELAY_TIME);
		// gridView = (GridView) findViewById(R.id.level_list_gridView);

		// DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		// width = metrics.widthPixels;
		// height = metrics.heightPixels;
		// density = metrics.density;
		// int columnWidth = (int) ((width - 2 * PADDING * density) / COLLUMS);
		// gridView.setColumnWidth(columnWidth);
		//
		// /* handle unlock level intent */
		// SharedPreferences sp = getSharedPreferences(
		// MainScreenActivity.PREF_DATA, 0);
		// levelUnlock = sp.getInt(DATA_UNLOCK_LEVEL, 1);
		// Log.d("LINH", "levelUnlock = " + levelUnlock);
		// gridAdapter = new LevelListAdapter(context,
		// R.layout.activity_level_list_item, totalLevel, levelUnlock,
		// columnWidth);
		// gridView.setAdapter(gridAdapter);
		// gridView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// if (position <= levelUnlock - 1) {
		// Intent i = new Intent(getApplicationContext(),
		// MainScreenActivity.class);
		// // startActivityForResult(i, REQUEST_NEW_LEVEL);
		// startActivity(i);
		// }
		// }
		// });

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
