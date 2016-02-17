package com.smtech.batchu;

import com.sutech.batchu.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NextQuestionActivity extends Activity {

	TextView txtNotify;
	Button btnNext;

	String notifyText = "";
	String correctAnswer = "";

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_next_question);
		txtNotify = (TextView) findViewById(R.id.txtNotify);
		btnNext = (Button) findViewById(R.id.btnNext);

		Intent i = getIntent();
		notifyText = i.getStringExtra("text_notify");
		correctAnswer = i.getStringExtra("correct_answer");

		// txtNotify.setTypeface(MainScreenActivity.V_de_vacia_font);
		txtNotify.setText(notifyText);
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// playTouchSound();
				Intent i = new Intent();
				setResult(RESULT_OK, i);
				finish();
			}
		});

	}

	private void playTouchSound() {
		boolean touchSoundsEnabled = android.provider.Settings.System.getInt(
				getContentResolver(),
				android.provider.Settings.System.SOUND_EFFECTS_ENABLED, 0) != 0;
		MediaPlayer mp;
		if (!touchSoundsEnabled) {
			mp = MediaPlayer.create(context, R.raw.button_sound_click);
			mp.setOnCompletionListener(new OnCompletionListener() {

				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mp.reset();
					mp.release();
					mp = null;
				}

			});
			mp.start();
		}
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent();
		setResult(RESULT_OK, i);
		finish();
		super.onBackPressed();
	}

}
