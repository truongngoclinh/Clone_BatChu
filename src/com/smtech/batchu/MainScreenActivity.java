package com.smtech.batchu;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.sutech.batchu.R;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppAd.AdMode;
import com.startapp.android.publish.StartAppSDK;
import com.startapp.android.publish.nativead.NativeAdDetails;
import com.startapp.android.publish.nativead.NativeAdPreferences;
import com.startapp.android.publish.nativead.StartAppNativeAd;
import com.startapp.android.publish.nativead.NativeAdPreferences.NativeAdBitmapSize;
import com.startapp.android.publish.splash.SplashConfig;
import com.startapp.android.publish.splash.SplashConfig.Theme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import com.facebook.android.AsyncFacebookRunner;
//import com.facebook.android.DialogError;
//import com.facebook.android.Facebook;
//import com.facebook.android.AsyncFacebookRunner.RequestListener;
//import com.facebook.android.Facebook.DialogListener;
//import com.facebook.android.FacebookError;
//import com.facebook.android.Util;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;

public class MainScreenActivity extends Activity {

	private Context context = this;

	private static final String[] chars = { "A", "B", "C", "D", "E", "G", "H",
			"I", "K", "L", "M", "N", "O", "P", "Q", "X", "Y" };
	private String[] keywords = {};
	private String[] keyHints = {};

	private ArrayList<String> keyboardChars;
	private final int KEYBOARD_MAX_CHAR = 16;
	private int CURRENT_CONTROL_ANSWER = 0;
	private int length = 0;
	private String answer = "";
	private int[] clickTracing = new int[16];
	private boolean[] isHintedIdx = new boolean[16];
	private int[] rdQuestion = {};
	private String allQuestion;
	private int currentQuestionIdx;

	private HashMap<String, ArrayList<Bitmap>> imgList;
	private ImageView img1;
	private ImageView img2;
	private ImageView img3;
	private ImageView img4;

	private TextView[] answer_line1 = new TextView[8];
	private TextView[] answer_line2 = new TextView[8];
	private TextView[] kb_line1 = new TextView[8];
	private TextView[] kb_line2 = new TextView[8];

	private TextView txttimer;
	private Button skipTimer;
	CountDownTimer timer;
	private final int constTime = 60;
	private int currentTimerValue = 60;

	private int[] imgSize;
	private Bitmap[] imgResizeBitmap;
	private float width = 0;
	private float height = 0;

	private int tmpCount = 0;

	private Button btnHint;
	private TextView txtCoins;
	private TextView txtTotalQ;
	private TextView txtHint;

	private static int hintIdx = -1;
	private boolean checkValidCoints = true;
	private boolean isFirstQuestion = true;

	// for saving data
	public static final String PREF_DATA = "pref_data";
	public static final String DATA_COINS = "data_total_coins";
	public static final String DATA_NUM_OF_QT = "data_number_of_questions";
	public static final String DATA_LEVEL = "data_current_level";
	public static final String DATA_QUESTION_LIST = "data_current_question_list";
	public static final String DATA_LOAD_ON_FIRST_TIME = "data_load_on_first_time";

	private final int REQUEST_FROM_NEXT_QUESTION = 100;

	private LinearLayout layout_answer_line1;
	private LinearLayout layout_answer_line2;

	private final int TYPE_CLICK_SOUND = 100;
	private final int TYPE_INCORRECT_ANSWER_SOUND = 101;
	private final int TYPE_CORRECT_ANSWER_SOUND = 102;

	private RelativeLayout root;
	public static Typeface Rabanera_shadow_font;
	public static Typeface V_de_vacia_font;

	private float w;
	private float h;

	private ImageView mFacebookBtn;

	// for ad
	// private AdView adView;
	private LinearLayout layout;
	// private InterstitialAd interstitial;
	private int count = -1;
	/** StartAppAd object declaration */
	private StartAppAd startAppAd = new StartAppAd(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("LINH", "onCreate()");
		setContentView(R.layout.activity_main_screen);
		StartAppSDK.init(this, "109866585", "209031052", true);
		this.registerReceiver(this.mConnReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));

		initActivityBackground();
		initUserData();
		initQuestion();
		initKeyboardId();
		initKeyboard(keywords[currentQuestionIdx]);
		initKeyboardOnclick();
		initImage();
		initTimer(false);
		initHintButton();
		initFbShareButton();
		// initAdView();
		// initAmobiView();

		
		// StartAppAd.showSlider(this);
		// startAppAd.loadAd(AdMode.AUTOMATIC);
		// StartAppAd.showSplash(this, savedInstanceState);

		// if (checkInternetConnection()) {
		// Log.d("LINH", "loadAd");
		// startAppAd.showAd();
		// startAppAd.loadAd();
		// } else {
		// }
		
	}

	//
	// private void initAmobiView() {
	// if (checkInternetConnection()) {
	// startAppAd.showAd();
	// startAppAd.loadAd();
	// }
	// // adView = (AmobiAdView) findViewById(R.id.main_menu_adView);
	// this.registerReceiver(this.mConnReceiver, new IntentFilter(
	// ConnectivityManager.CONNECTIVITY_ACTION));
	// }

	// private void initAdView() {
	// // /* Adview */
	// adView = new AdView(context);
	// adView.setAdSize(AdSize.BANNER);
	// adView.setAdUnitId("ca-app-pub-1819876831517948/5111540712");
	//
	// interstitial = new InterstitialAd(context);
	// interstitial.setAdUnitId("ca-app-pub-1819876831517948/3914009118");
	//
	// this.registerReceiver(this.mConnReceiver, new IntentFilter(
	// ConnectivityManager.CONNECTIVITY_ACTION));
	// }

	private void initActivityBackground() {
		root = (RelativeLayout) findViewById(R.id.root);
		// root.setBackgroundResource(R.drawable.bg_main_screen);
	}

	private void initUserData() {
		Rabanera_shadow_font = Typeface.createFromAsset(getAssets(),
				"fonts/Rabanera-outline-shadow.ttf");
		V_de_vacia_font = Typeface.createFromAsset(getAssets(),
				"fonts/V_devacia.ttf");
		layout = (LinearLayout) findViewById(R.id.layoutAd);
		keywords = getResources().getStringArray(R.array.keywords);
		keyHints = getResources().getStringArray(R.array.keyHints);
		txtHint = (TextView) findViewById(R.id.txtHint);
		txtCoins = (TextView) findViewById(R.id.txtCoins);
		txtTotalQ = (TextView) findViewById(R.id.txtTotalQ);
		txtTotalQ.setTypeface(Rabanera_shadow_font);
		SharedPreferences sp = getSharedPreferences(PREF_DATA, 0);
		int userTotalQt = sp.getInt(DATA_NUM_OF_QT, 0);
		int userTotalCns = sp.getInt(DATA_COINS, 0);
		allQuestion = sp.getString(DATA_QUESTION_LIST, "");
		if (userTotalQt > 0)
			isFirstQuestion = false;
		txtCoins.setText(userTotalCns + "");
		txtTotalQ.setText(userTotalQt + "");
		count++;
	}

	private void initFbShareButton() {
		mFacebookBtn = (ImageView) findViewById(R.id.btnFbShare);
		mFacebookBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playButtonSoundOnClick(TYPE_CLICK_SOUND);
				if (isNetworkAvailable(context)) {
					// Intent i = new Intent(getApplicationContext(),
					// FacebookUploadActivity.class);
					// Bitmap bmp = screenShot(root);
					// ByteArrayOutputStream stream = new
					// ByteArrayOutputStream();
					// bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
					// byte[] byteArray = stream.toByteArray();
					// i.putExtra("image", byteArray);
					// startActivity(i);
					Intent shareCaptionIntent = new Intent(Intent.ACTION_SEND);
					shareCaptionIntent.setType("image/*");

					// set photo
					Bitmap bmp = screenShot(root);
					Uri photoUri = getImageUri(context, bmp);
					Intent sharingIntent = new Intent(Intent.ACTION_SEND);
					sharingIntent.setType("image/png");
					sharingIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
					startActivity(Intent.createChooser(sharingIntent,
							"Share image using"));

				} else {
					Toast.makeText(context, "Bạn chưa kết nối mạng!",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

	public Bitmap screenShot(View view) {
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
		return bitmap;
	}

	private void initQuestion() {
		SharedPreferences sp = getSharedPreferences(PREF_DATA, 0);
		boolean onFirstTimeLoadingData = sp.getBoolean(DATA_LOAD_ON_FIRST_TIME,
				true);
		rdQuestion = new int[keywords.length];
		rdQuestion = randomAllocate(keywords.length);
		if (onFirstTimeLoadingData) {
			allQuestion = "";
			for (int i = 0; i < rdQuestion.length; i++) {
				String tmp = rdQuestion[i] + ",";
				allQuestion += tmp;
			}
			sp.edit().putString(DATA_QUESTION_LIST, allQuestion)
					.putBoolean(DATA_LOAD_ON_FIRST_TIME, false).commit();
		}
		String tmpAllQuestion[] = allQuestion.split(",");
		currentQuestionIdx = Integer.parseInt(tmpAllQuestion[0]);
		Log.d("LINH", "Loading.. \n strQuestion = " + currentQuestionIdx);
		txtHint.setText(keyHints[currentQuestionIdx]);
	}

	private void initHintButton() {
		btnHint = (Button) findViewById(R.id.btnHint);
		btnHint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playButtonSoundOnClick(TYPE_CLICK_SOUND);
				int userTotalCoins = Integer.parseInt(txtCoins.getText()
						.toString());
				if (userTotalCoins >= 20) {
					checkValidCoints = true;
				} else {
					checkValidCoints = false;
				}
				final Dialog d = new Dialog(context);
				d.setCancelable(true);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(R.layout.activity_main_screen_dialog_show_hinted_text);
				Button btnOk = (Button) d
						.findViewById(R.id.dialogShowHinted_btnOk);
				Button btnCancel = (Button) d
						.findViewById(R.id.dialogShowHinted_btnCancel);
				TextView txtContent = (TextView) d
						.findViewById(R.id.dialogShowHinted_txtNotify);
				btnOk.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (checkValidCoints)
							handleHintText();
						d.dismiss();
					}
				});
				btnCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						d.dismiss();

					}
				});
				if (checkValidCoints) {
					txtContent
							.setText("Mở gợi ý sẽ bị trừ 20 coins, bạn có chắc không?");
				} else {
					txtContent.setText("Bạn không có đủ coins!");
				}
				d.show();

			}
		});
	}

	private void handleHintText() {
		CURRENT_CONTROL_ANSWER = 1;
		hintIdx++;
		// Log.d("LINH", "hintIdx = " + hintIdx);
		for (int i = 0; i < 8; i++) {
			if (hintIdx < 8) {
				if (i <= hintIdx) {
					answer_line1[i].setTextColor(Color.MAGENTA);
					answer_line1[i].setClickable(false);
					if (i == hintIdx) {
						String tmp = keywords[currentQuestionIdx].substring(
								hintIdx, hintIdx + 1);
						answer_line1[i].setText(tmp);
						answer = setAnswerAtPos(tmp, hintIdx);
						setKeyboardInvisibleForHintedText(tmp, 1);

					}
				} else {
					answer_line1[i].setText(" ");
					answer_line1[i].setClickable(true);
					answer_line1[i].setTextColor(Color.GREEN);
				}
				answer_line2[i].setText(" ");
				answer_line2[i].setClickable(true);
			} else {
				answer_line1[i].setTextColor(Color.MAGENTA);
				answer_line1[i].setClickable(false);
				if (i <= hintIdx - 8) {
					answer_line2[i].setTextColor(Color.MAGENTA);
					answer_line2[i].setClickable(false);
					if (i == hintIdx - 8) {
						String tmp = keywords[currentQuestionIdx].substring(
								hintIdx, hintIdx + 1);
						answer_line2[i].setText(tmp);
						answer = setAnswerAtPos(tmp, hintIdx);
						setKeyboardInvisibleForHintedText(tmp, 2);
					}
				} else {
					answer_line2[i].setText(" ");
					answer_line2[i].setClickable(true);
					answer_line2[i].setTextColor(Color.GREEN);
				}
			}
		}
		CURRENT_CONTROL_ANSWER += hintIdx;
		// handleHintedTextOnKb();
		if (CURRENT_CONTROL_ANSWER == length) {
			checkAnswer(answer);
		}
		setKeyboardClickable(true);
		SharedPreferences sp = getSharedPreferences(PREF_DATA, 0);
		SharedPreferences.Editor ed = sp.edit();
		int userTotalCoins = Integer.parseInt(txtCoins.getText().toString());
		userTotalCoins -= 20;
		ed.putInt(DATA_COINS, userTotalCoins).commit();
		txtCoins.setText(userTotalCoins + "");
	}

	// private void handleHintedTextOnKb() {
	// setKeyboardVisible(0, 0, true);
	// for (int i = 0; i <= hintIdx; i++) {
	// String hintedChar = keywords[currentQuestionIdx]
	// .substring(i, i + 1);
	// for (int j = 0; j < 16; j++) {
	// String tmp = "";
	// if (j < 8) {
	// tmp = kb_line1[j].getText().toString();
	// if (tmp.equals(hintedChar)) {
	// kb_line1[j].setVisibility(View.INVISIBLE);
	// break;
	// }
	// } else {
	// tmp = kb_line2[j - 8].getText().toString();
	// if (tmp.equals(hintedChar)) {
	// kb_line2[j - 8].setVisibility(View.INVISIBLE);
	// break;
	// }
	// }
	// }
	// }
	// }

	@SuppressLint("NewApi")
	private Rotate3dAnimation initAnimation(float x, float y, int numbers) {
		// Log.d("LINH", "x = " + x + " & y = " + y);
		final int n = numbers;
		Rotate3dAnimation rt = new Rotate3dAnimation(-90, 0, x, y, 310.0f,
				false);
		rt.setDuration(1000);
		rt.setFillAfter(true);
		rt.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				switch (n) {
				case 1:
					img1.setImageBitmap(imgResizeBitmap[0]);
					break;

				case 2:
					img2.setImageBitmap(imgResizeBitmap[1]);
					break;

				case 3:
					img3.setImageBitmap(imgResizeBitmap[2]);
					break;

				case 4:
					img4.setImageBitmap(imgResizeBitmap[3]);
					break;

				default:
					break;
				}

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}
		});
		return rt;
	}

	@SuppressLint("NewApi")
	private void initTimer(boolean fromPause) {
		txttimer = (TextView) findViewById(R.id.txtTimer);
		skipTimer = (Button) findViewById(R.id.btnSkipTimer);
		tmpCount = 0;
		if (!fromPause) {
			timer = new CountDownTimer(constTime * 1000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					int currentTime = (int) millisUntilFinished / 1000;
					currentTimerValue = currentTime;
					txttimer.setText(currentTime + "");
					img1.setImageBitmap(imgResizeBitmap[0]);
					switch (currentTime) {
					case 45:
						img2.startAnimation(initAnimation(img2.getX() / 2,
								img2.getY() / 2, 2));
						break;

					case 30:
						img3.startAnimation(initAnimation(img3.getX() / 2,
								img3.getY() / 2, 3));
						break;

					case 15:
						img4.startAnimation(initAnimation(img4.getX() / 2,
								img4.getY() / 2, 4));
						break;

					default:
						break;
					}
				}

				@Override
				public void onFinish() {
					txttimer.setText("0");
				}
			};
			timer.start();
		} else {
			if (currentTimerValue > 45) {
				img1.setImageBitmap(imgResizeBitmap[0]);
			} else if (currentTimerValue > 30 && currentTimerValue < 45) {
				img1.setImageBitmap(imgResizeBitmap[0]);
				img2.setImageBitmap(imgResizeBitmap[1]);
			} else if (currentTimerValue > 15 && currentTimerValue < 30) {
				img1.setImageBitmap(imgResizeBitmap[0]);
				img2.setImageBitmap(imgResizeBitmap[1]);
				img3.setImageBitmap(imgResizeBitmap[2]);
			} else {
				img1.setImageBitmap(imgResizeBitmap[0]);
				img2.setImageBitmap(imgResizeBitmap[1]);
				img3.setImageBitmap(imgResizeBitmap[2]);
				img4.setImageBitmap(imgResizeBitmap[3]);
			}
			timer = new CountDownTimer((currentTimerValue) * 1000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					int currentTime = (int) millisUntilFinished / 1000;
					currentTimerValue = currentTime;
					txttimer.setText(currentTime + "");
					// img1.setImageBitmap(imgResizeBitmap[0]);
					switch (currentTime) {
					case 45:
						img2.startAnimation(initAnimation(img2.getX() / 2,
								img2.getY() / 2, 2));
						break;

					case 30:
						img3.startAnimation(initAnimation(img3.getX() / 2,
								img3.getY() / 2, 3));
						break;

					case 15:
						img4.startAnimation(initAnimation(img4.getX() / 2,
								img4.getY() / 2, 4));
						break;

					default:
						break;
					}
				}

				@Override
				public void onFinish() {
					txttimer.setText("0");
				}
			};
			timer.start();
		}
		skipTimer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playButtonSoundOnClick(TYPE_CLICK_SOUND);
				int currentTimer = Integer.parseInt(txttimer.getText()
						.toString());
				if (currentTimer <= 60 && currentTimer >= 45) {
					tmpCount = 0;
					timer.cancel();
					timer = new CountDownTimer((constTime - 15) * 1000, 1000) {

						@Override
						public void onTick(long millisUntilFinished) {
							int currentTime = (int) millisUntilFinished / 1000;
							currentTimerValue = currentTime;
							txttimer.setText(currentTime + "");
							if (tmpCount == 0) {
								img1.setImageBitmap(imgResizeBitmap[0]);
								img2.startAnimation(initAnimation(
										img2.getX() / 2, img2.getY() / 2, 2));
								tmpCount++;
							}
							switch (currentTime) {
							case 30:
								img3.startAnimation(initAnimation(
										img3.getX() / 2, img3.getY() / 2, 3));
								break;

							case 15:
								img4.startAnimation(initAnimation(
										img4.getX() / 2, img4.getY() / 2, 4));
								break;

							default:
								break;
							}
						}

						@Override
						public void onFinish() {
							txttimer.setText("0");
						}
					};
					timer.start();
					// clickCount++;
				} else if (currentTimer < 45 && currentTimer >= 30) {
					tmpCount = 0;
					timer.cancel();
					timer = new CountDownTimer((constTime - 30) * 1000, 1000) {

						@Override
						public void onTick(long millisUntilFinished) {
							int currentTime = (int) millisUntilFinished / 1000;
							currentTimerValue = currentTime;
							txttimer.setText(currentTime + "");
							if (tmpCount == 0) {
								img1.setImageBitmap(imgResizeBitmap[0]);
								img2.setImageBitmap(imgResizeBitmap[1]);
								img3.startAnimation(initAnimation(
										img3.getX() / 2, img3.getY() / 2, 3));
								tmpCount++;
							}
							switch (currentTime) {
							case 15:
								img4.startAnimation(initAnimation(
										img3.getX() / 2, img3.getY() / 2, 3));
								break;

							default:
								break;
							}
						}

						@Override
						public void onFinish() {
							txttimer.setText("0");
						}
					};
					timer.start();
					// clickCount++;
				} else if (currentTimer < 30 && currentTimer >= 15) {
					tmpCount = 0;
					timer.cancel();
					timer = new CountDownTimer((constTime - 45) * 1000, 1000) {

						@Override
						public void onTick(long millisUntilFinished) {
							int currentTime = (int) millisUntilFinished / 1000;
							currentTimerValue = currentTime;
							txttimer.setText(currentTime + "");
							if (tmpCount == 0) {
								img1.setImageBitmap(imgResizeBitmap[0]);
								img2.setImageBitmap(imgResizeBitmap[1]);
								img3.setImageBitmap(imgResizeBitmap[2]);
								img4.startAnimation(initAnimation(
										img4.getX() / 2, img4.getY() / 2, 4));
								tmpCount++;
							}
						}

						@Override
						public void onFinish() {
							txttimer.setText("0");
						}
					};
					timer.start();
					// clickCount++;
				}

			}
		});
	}

	private void initKeyboard(String keyword) {
		/* INIT ANSWER */
		length = keyword.length();
		if (length <= 8) {
			for (int i = 0; i < length; i++) {
				answer_line1[i].setText(" ");
				answer_line1[i].setVisibility(View.VISIBLE);
			}
		} else {
			for (int i = 0; i < 8; i++) {
				answer_line1[i].setText(" ");
				answer_line1[i].setVisibility(View.VISIBLE);
				if (i + 8 < length) {
					answer_line2[i].setText(" ");
					answer_line2[i].setVisibility(View.VISIBLE);
				}
			}
		}

		/* INIT KEYBOARD */
		keyboardChars = new ArrayList<String>();
		int[] indexs = new int[length];
		indexs = randomAllocate(length);
		for (int i = 0; i < indexs.length; i++) {
			int idx = indexs[i];
			keyboardChars.add(keyword.substring(idx, idx + 1));
		}

		int[] tmpIdxOutKeyword = new int[KEYBOARD_MAX_CHAR - length];
		tmpIdxOutKeyword = randomAllocate(KEYBOARD_MAX_CHAR - length);
		for (int i = 0; i < KEYBOARD_MAX_CHAR - length; i++) {
			keyboardChars.add(chars[tmpIdxOutKeyword[i]]);
		}

		int[] finalIndex = new int[keyboardChars.size()];
		finalIndex = randomAllocate(keyboardChars.size());

		// set keyboard
		for (int i = 0; i < 8; i++) {
			kb_line1[i].setText(keyboardChars.get(finalIndex[i]));
			kb_line1[i].setVisibility(View.VISIBLE);
			kb_line2[i].setText(keyboardChars.get(finalIndex[i + 8]));
			kb_line2[i].setVisibility(View.VISIBLE);
		}
		setAnswerTextGreenColor();
	}

	private void initKeyboardOnclick() {
		for (int i = 0; i < 8; i++) {
			final int tmpI = i;
			kb_line1[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String text = kb_line1[tmpI].getText().toString();
					setAnswerText(text, tmpI, 1);
					playButtonSoundOnClick(TYPE_CLICK_SOUND);
				}
			});

			kb_line2[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String text = kb_line2[tmpI].getText().toString();
					setAnswerText(text, tmpI, 2);
					playButtonSoundOnClick(TYPE_CLICK_SOUND);
				}
			});

			answer_line1[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (tmpI > hintIdx) {
						answer_line1[tmpI].setText(" ");
						answer_line1[tmpI].setClickable(false);
						if (CURRENT_CONTROL_ANSWER > 0) {
							CURRENT_CONTROL_ANSWER--;
						}
						answer = removeChar(answer_line1[tmpI].getText()
								.toString(), tmpI);
						setKeyboardVisible(tmpI, 1, false);
						setAnswerTextGreenColor();
						setKeyboardClickable(true);
					}
				}
			});

			answer_line2[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (tmpI > hintIdx - 8) {
						answer_line2[tmpI].setText(" ");
						answer_line2[tmpI].setClickable(false);
						if (CURRENT_CONTROL_ANSWER > 0) {
							CURRENT_CONTROL_ANSWER--;
						}
						answer = removeChar(answer_line2[tmpI].getText()
								.toString(), tmpI + 8);
						setKeyboardVisible(tmpI, 2, false);
						setAnswerTextGreenColor();
						setKeyboardClickable(true);
					}
				}
			});
		}
	}

	private String removeFirstQuestionIndex(String c) {
		String tmp = "";
		int secondQuestionIdx = 0;
		for (int i = 0; i < c.length(); i++) {
			if (c.substring(i, i + 1).equals(",")) {
				secondQuestionIdx = i;
				break;
			}
		}
		for (int i = secondQuestionIdx + 1; i < c.length(); i++) {
			tmp += c.substring(i, i + 1);
		}
		return tmp;
	}

	private String removeChar(String c, int index) {
		String tmp = "";
		for (int i = 0; i < answer.length(); i++) {
			if (i == index) {
				tmp += " ";
			} else {
				tmp += answer.substring(i, i + 1);
			}
		}
		answer = tmp;
		return answer;
	}

	private String setAnswerAtPos(String c, int index) {
		if (index == answer.length()) {
			answer += c;
		} else {
			String tmp = "";
			for (int i = 0; i < answer.length(); i++) {
				if (i == index) {
					tmp += c;
				} else {
					tmp += answer.substring(i, i + 1);
				}
			}
			answer = tmp;
		}
		return answer;
	}

	private void checkAnswer(String answer) {
		if (answer.equals(keywords[currentQuestionIdx])) {
			// Toast.makeText(context, "Yeee!", Toast.LENGTH_SHORT).show();
			saveCorrectAnswer();
		} else {
			if (length <= 8) {
				for (int i = 0; i < 8; i++) {
					answer_line1[i].setTextColor(Color.RED);
				}
			} else {
				for (int i = 0; i < 8; i++) {
					answer_line1[i].setTextColor(Color.RED);
					answer_line2[i].setTextColor(Color.RED);
				}
			}
			incorrectAnswerAnimationStart();
			playButtonSoundOnClick(TYPE_INCORRECT_ANSWER_SOUND);
			Toast.makeText(context, "Sai mất rồi!", Toast.LENGTH_SHORT).show();
		}
		setKeyboardClickable(false);
	}

	private void playButtonSoundOnClick(int type) {
		boolean touchSoundsEnabled = android.provider.Settings.System.getInt(
				getContentResolver(),
				android.provider.Settings.System.SOUND_EFFECTS_ENABLED, 0) != 0;
		MediaPlayer mp;
		if (type == TYPE_CLICK_SOUND && !touchSoundsEnabled) {
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

		if (type == TYPE_INCORRECT_ANSWER_SOUND) {
			mp = MediaPlayer.create(context, R.raw.incorrect_answer_sound);
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

		if (type == TYPE_CORRECT_ANSWER_SOUND) {
			mp = MediaPlayer.create(context, R.raw.correct_answer_sound);
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

	private void saveCorrectAnswer() {
		SharedPreferences sp = getSharedPreferences(PREF_DATA, 0);
		SharedPreferences.Editor ed = sp.edit();
		// int userCurrentLevel =
		// Integer.parseInt(txtLevel.getText().toString());
		int userTotalCoins = Integer.parseInt(txtCoins.getText().toString());
		int userTotalQt = Integer.parseInt(txtTotalQ.getText().toString());
		String notifyEarnedCoints = "Chúc mừng bạn đã được thêm ";
		if (currentTimerValue >= 45) {
			notifyEarnedCoints += "45 coins!";
			userTotalCoins += 45;
		} else if (currentTimerValue >= 30) {
			notifyEarnedCoints += "35 coins!";
			userTotalCoins += 35;
		} else if (currentTimerValue > 15) {
			notifyEarnedCoints += "25 coins!";
			userTotalCoins += 25;
		} else if (currentTimerValue > 0) {
			notifyEarnedCoints += "15 coins!";
			userTotalCoins += 20;
		} else {
			notifyEarnedCoints += "10 coins!";
			userTotalCoins += 15;
		}

		timer.cancel();

		notifyEarnedCoints += (" \nĐáp án: " + answer);
		userTotalQt += 1;
		ed.putInt(DATA_COINS, userTotalCoins);
		ed.putInt(DATA_NUM_OF_QT, userTotalQt);
		ed.commit();

		/*
		 * remove question
		 */
		allQuestion = removeFirstQuestionIndex(allQuestion);
		ed.putString(DATA_QUESTION_LIST, allQuestion).commit();
		if (allQuestion.equals("")) {
			Toast.makeText(
					getApplicationContext(),
					"Bạn đã vượt qua hết các câu hỏi, chương trình đang được khởi động lại...!",
					Toast.LENGTH_LONG);
			for (int i : rdQuestion) {
				allQuestion += i;
			}
			ed.putString(DATA_QUESTION_LIST, allQuestion).commit();
		}

		playButtonSoundOnClick(TYPE_CORRECT_ANSWER_SOUND);

		Intent i = new Intent(getApplicationContext(),
				NextQuestionActivity.class);
		i.putExtra("correct_answer", answer);
		i.putExtra("text_notify", notifyEarnedCoints);
		startActivityForResult(i, REQUEST_FROM_NEXT_QUESTION);
		overridePendingTransition(R.anim.anim_fall_from_top,
				R.anim.anim_out_to_top);

	}

	private void setKeyboardVisible(int currentIdx, int answerNum,
			boolean visibleAll) {
		if (!visibleAll) {
			int trcIndex = 0;
			if (currentIdx == 0) {
				trcIndex = 69;
			} else {
				trcIndex = currentIdx + 8 * (answerNum - 1);
			}
			for (int i = 0; i < clickTracing.length; i++) {
				if (!isHintedIdx[i]) {
					if (clickTracing[i] == trcIndex) {
						if (i < 8) {
							kb_line1[i].setVisibility(View.VISIBLE);
						} else {
							kb_line2[i - 8].setVisibility(View.VISIBLE);
						}
					}
				}
			}
		} else {
			for (int i = 0; i < 16; i++) {
				if (!isHintedIdx[i]) {
					if (i < 8) {
						kb_line1[i].setVisibility(View.VISIBLE);
					} else {
						kb_line2[i - 8].setVisibility(View.VISIBLE);
					}
				}
			}
		}

	}

	private void setKeyboardInvisibleForHintedText(String c, int answerNum) {
		for (int i = 0; i < clickTracing.length; i++) {
			if (!isHintedIdx[i]) {
				if (clickTracing[i] != 0) {
					if (i < 8) {
						kb_line1[i].setVisibility(View.VISIBLE);
					} else {
						kb_line2[i - 8].setVisibility(View.VISIBLE);
					}
				}
			}
		}

		int tmpIdx = 0;
		for (int i = 0; i < 16; i++) {
			if (i < 8) {
				if (kb_line1[i].getText().toString().equals(c)
						&& (kb_line1[i].getVisibility() == View.VISIBLE)) {
					kb_line1[i].setVisibility(View.INVISIBLE);
					tmpIdx = i;
					break;
				}
			} else {
				if (kb_line2[i - 8].getText().toString().equals(c)
						&& (kb_line2[i - 8].getVisibility() == View.VISIBLE)) {
					kb_line2[i - 8].setVisibility(View.INVISIBLE);
					tmpIdx = i;
					break;
				}
			}
		}
		clickTracing[tmpIdx] = hintIdx;
		isHintedIdx[tmpIdx] = true;

	}

	private void setAnswerTextGreenColor() {
		for (int i = 0; i < 8; i++) {
			if (hintIdx < 8) {
				if (i > hintIdx) {
					answer_line1[i].setTextColor(Color.GREEN);
				} else {
					answer_line1[i].setTextColor(Color.MAGENTA);
				}
				answer_line2[i].setTextColor(Color.GREEN);
			} else {
				answer_line1[i].setTextColor(Color.MAGENTA);
				if (i + 8 < hintIdx) {
					answer_line2[i].setTextColor(Color.MAGENTA);
				} else {
					answer_line2[i].setTextColor(Color.GREEN);
				}
			}
		}
	}

	@SuppressLint("NewApi")
	private void incorrectAnswerAnimationStart() {
		TranslateAnimation t = null;
		t = new TranslateAnimation(10, -10, 0, 0);
		t.setDuration(50);
		t.setRepeatCount(2);
		layout_answer_line1.startAnimation(t);
		layout_answer_line2.startAnimation(t);
	}

	private void setKeyboardClickable(boolean clickable) {
		for (int i = 0; i < 8; i++) {
			kb_line1[i].setClickable(clickable);
			kb_line2[i].setClickable(clickable);
		}
	}

	private void setAnswerText(String text, int kbIndex, int kbType) {
		int firstBlankIndex = 0;
		if (answer.contains(" ")) {
			if (answer.length() <= 8) {
				if (answer_line1[0].getText().equals(" ")) {
					firstBlankIndex = 0;
				} else {
					for (int i = 1; i < 8; i++) {
						String txt1 = answer_line1[i].getText().toString();
						String txt2 = answer_line1[i - 1].getText().toString();
						if (txt1.equals(" ") == true
								&& txt2.equals(" ") == false) {
							firstBlankIndex = i;
							break;
						}
					}
				}
			} else {
				boolean isAnswer1FullFilled = true;
				if (answer_line1[0].getText().equals(" ")) {
					isAnswer1FullFilled = false;
					firstBlankIndex = 0;
				} else {
					for (int i = 1; i < 8; i++) {
						String txt1 = answer_line1[i].getText().toString();
						String txt2 = answer_line1[i - 1].getText().toString();
						if (txt1.equals(" ") == true
								&& txt2.equals(" ") == false) {
							isAnswer1FullFilled = false;
							firstBlankIndex = i;
							break;
						}
					}
				}
				if (isAnswer1FullFilled) {
					if (answer_line2[0].getText().equals(" ")) {
						firstBlankIndex = 8;
					} else {
						for (int i = 1; i < 8; i++) {
							String txt1 = answer_line2[i].getText().toString();
							String txt2 = answer_line2[i - 1].getText()
									.toString();
							if (txt1.equals(" ") == true
									&& txt2.equals(" ") == false) {
								firstBlankIndex = i + 8;
								break;
							}
						}
					}
				}
			}
		} else {
			firstBlankIndex = CURRENT_CONTROL_ANSWER;
		}

		int trcIndex = kbIndex + 8 * (kbType - 1);
		if (firstBlankIndex < 8) {
			answer_line1[firstBlankIndex].setText(text);
			answer_line1[firstBlankIndex].setClickable(true);
			answer_line1[firstBlankIndex].setVisibility(View.VISIBLE);
		} else {
			answer_line2[firstBlankIndex - 8].setText(text);
			answer_line2[firstBlankIndex - 8].setClickable(true);
			answer_line2[firstBlankIndex - 8].setVisibility(View.VISIBLE);
		}
		if (kbType == 1) {
			kb_line1[kbIndex].setVisibility(View.INVISIBLE);
		} else {
			kb_line2[kbIndex].setVisibility(View.INVISIBLE);
		}

		if (firstBlankIndex == 0) {
			// clickTracing[trcIndex] = firstBlankIndex;
			clickTracing[trcIndex] = 69;
		} else {
			clickTracing[trcIndex] = firstBlankIndex;
		}
		isHintedIdx[trcIndex] = false;
		answer = setAnswerAtPos(text, firstBlankIndex);
		if (CURRENT_CONTROL_ANSWER == length - 1) {
			checkAnswer(answer);
		}
		CURRENT_CONTROL_ANSWER++;
	}

	private int[] randomAllocate(int size) {
		int result[] = new int[size];
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			tmp.add(i);
		}
		for (int i = 0; i < size; i++) {
			int rdNumber = random(tmp);
			result[i] = rdNumber;
		}
		return result;
	}

	private int random(ArrayList<Integer> arr) {
		int realSize = arr.size() - 1;
		int tmpValue = 0;
		if (realSize > 0) {
			int index = new Random().nextInt(realSize);
			tmpValue = arr.get(index);
			arr.remove(index);
		} else if (realSize == 0) {
			tmpValue = arr.get(0);
		}
		return tmpValue;
	}

	private void initImage() {
		img1 = (ImageView) findViewById(R.id.img1);
		img2 = (ImageView) findViewById(R.id.img2);
		img3 = (ImageView) findViewById(R.id.img3);
		img4 = (ImageView) findViewById(R.id.img4);
		imgList = new HashMap<String, ArrayList<Bitmap>>();
		imgSize = calculateImgSize();
		// String tmpRes = "";
		for (int i = 0; i < keywords.length; i++) {
			if (i == currentQuestionIdx) {
				ArrayList<Bitmap> lstBm = null;
				lstBm = cropImageInto4RectangleParts(keywords[i].toLowerCase());
				imgList.put(keywords[i].toLowerCase(), lstBm);
				break;
			}
		}

		imgResizeBitmap = new Bitmap[4];
		ArrayList<Bitmap> lstRes = imgList.get(keywords[currentQuestionIdx]
				.toLowerCase());
		for (int i = 0; i < 4; i++) {
			Bitmap resizedBm = Bitmap.createScaledBitmap(lstRes.get(i),
					imgSize[0], imgSize[1], true);
			imgResizeBitmap[i] = resizedBm;
			resizedBm = null;
		}

	}

	private ArrayList<Bitmap> cropImageInto4RectangleParts(String prefix) {
		ArrayList<Bitmap> tmp = new ArrayList<Bitmap>();
		Bitmap originalBm = BitmapFactory.decodeResource(
				getResources(),
				getResources().getIdentifier(prefix, "drawable",
						getPackageName()));
		Bitmap bm1 = Bitmap.createBitmap(originalBm, 0, 0,
				originalBm.getWidth() / 2, originalBm.getHeight() / 2);
		Bitmap bm2 = Bitmap.createBitmap(originalBm, originalBm.getWidth() / 2,
				0, originalBm.getWidth() / 2, originalBm.getHeight() / 2);
		Bitmap bm3 = Bitmap.createBitmap(originalBm, 0,
				originalBm.getHeight() / 2, originalBm.getWidth() / 2,
				originalBm.getHeight() / 2);
		Bitmap bm4 = Bitmap.createBitmap(originalBm, originalBm.getWidth() / 2,
				originalBm.getHeight() / 2, originalBm.getWidth() / 2,
				originalBm.getHeight() / 2);
		tmp.add(bm1);
		tmp.add(bm2);
		tmp.add(bm3);
		tmp.add(bm4);
		originalBm = null;
		bm1 = null;
		bm2 = null;
		bm3 = null;
		bm4 = null;

		return tmp;
	}

	private void initKeyboardId() {
		for (int i = 0; i < 8; i++) {
			answer_line1[i] = (TextView) findViewById(getResources()
					.getIdentifier("line1_char" + (i + 1), "id",
							getPackageName()));
			answer_line2[i] = (TextView) findViewById(getResources()
					.getIdentifier("line2_char" + (i + 1), "id",
							getPackageName()));
			kb_line1[i] = (TextView) findViewById(getResources().getIdentifier(
					"kb_line1_char" + (i + 1), "id", getPackageName()));
			kb_line2[i] = (TextView) findViewById(getResources().getIdentifier(
					"kb_line2_char" + (i + 1), "id", getPackageName()));
			answer_line1[i].setText(" ");
			answer_line2[i].setText(" ");
		}
		layout_answer_line1 = (LinearLayout) findViewById(R.id.layout_answer_line1);
		layout_answer_line2 = (LinearLayout) findViewById(R.id.layout_answer_line2);
	}

	private int[] calculateImgSize() {
		int[] result = new int[2];
		DisplayMetrics display = getResources().getDisplayMetrics();
		width = display.widthPixels;
		height = display.heightPixels;
		float density = display.density;
		result[0] = (int) (width - density * 20) / 2; // width
		result[1] = (int) (density * 100); // height
		return result;

	}

	@Override
	protected void onDestroy() {
		imgList.clear();
		answer = "";
		CURRENT_CONTROL_ANSWER = 0;
		hintIdx = -1;
		// clickCount = 0;
		unregisterReceiver(mConnReceiver);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		Log.d("LINH", "onBackPressed!");
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Bạn có muốn thoát ứng dụng?")
				.setCancelable(false)
				.setPositiveButton("Có", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						imgList.clear();
						answer = "";
						CURRENT_CONTROL_ANSWER = 0;
						hintIdx = -1;
						finish();
					}
				})
				.setNegativeButton("Không",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("LINH", "onResume()");
		timer.start();
		startAppAd.onResume();
	}

	private void resetData() {
		imgList.clear();
		answer = "";
		CURRENT_CONTROL_ANSWER = 0;
		hintIdx = -1;
		img1.setImageBitmap(null);
		img2.setImageBitmap(null);
		img3.setImageBitmap(null);
		img4.setImageBitmap(null);
		for (int i = 0; i < 8; i++) {
			answer_line1[i].setVisibility(View.GONE);
			answer_line2[i].setVisibility(View.GONE);
		}
	}

	@Override
	protected void onPause() {
		timer.cancel();
		Log.d("LINH", "onPause()");
		super.onPause();
		startAppAd.onPause();
	}

	@Override
	protected void onRestart() {
		initTimer(true);
		super.onRestart();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == REQUEST_FROM_NEXT_QUESTION
					&& resultCode == RESULT_OK) {
				resetData();
				initUserData();
				initQuestion();
				initKeyboardId();
				initKeyboard(keywords[currentQuestionIdx]);
				initKeyboardOnclick();
				initImage();
				initTimer(false);
				initHintButton();
				initFbShareButton();
			}
		} else {
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static boolean isNetworkAvailable(Context context) {
		return ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo() != null;
	}

	private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			boolean noConnectivity = intent.getBooleanExtra(
					ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
			String reason = intent
					.getStringExtra(ConnectivityManager.EXTRA_REASON);
			boolean isFailover = intent.getBooleanExtra(
					ConnectivityManager.EXTRA_IS_FAILOVER, false);

			NetworkInfo currentNetworkInfo = (NetworkInfo) intent
					.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			NetworkInfo otherNetworkInfo = (NetworkInfo) intent
					.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

			if (currentNetworkInfo.isConnected()) {
				if (count == 0 && (count % 3) == 0) {
					startAppAd.loadAd(new AdEventListener() {

						@Override
						public void onReceiveAd(Ad ad) {
							Log.d("LINH", "Ad received!");
							startAppAd.showAd();
						}

						@Override
						public void onFailedToReceiveAd(Ad ad) {
							Log.d("LINH", "Ad failed");

						}
					});
				}

			} else {
				Toast.makeText(context,
						"Please turn on network connection to download data!",
						Toast.LENGTH_LONG).show();
			}
		}
	};

	public boolean checkInternetConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (null == ni)
			return false;
		return ni.isConnectedOrConnecting();
	}

}
