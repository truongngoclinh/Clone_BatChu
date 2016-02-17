package com.smtech.batchu;
//package com.example.bat_chu;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class LevelListAdapter extends BaseAdapter {
//	private final Context mContext;
//	private final int layout;
//	private final int[] data;
//	private Holder holder;
//	private int levelUnlocked;
//	private int clWidth;
//	private final int clHeight = 70;
//
//	public LevelListAdapter(Context context, int resource, int[] objects,
//			int lv, int w) {
//		super();
//		mContext = context;
//		layout = resource;
//		data = objects;
//		levelUnlocked = lv;
//		clWidth = w;
//		// TODO Auto-generated constructor stub
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return data.length;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		LayoutInflater inflater = (LayoutInflater) mContext
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View row = convertView;
//		if (row == null) {
//			holder = new Holder();
//			row = inflater.inflate(layout, null);
//			holder.imgView = (ImageView) row
//					.findViewById(R.id.level_list_item_img);
//			holder.txtView = (TextView) row
//					.findViewById(R.id.level_list_item_number);
//			row.setTag(holder);
//		} else {
//			holder = (Holder) row.getTag();
//		}
//
//		if (position <= levelUnlocked - 1) {
////			Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(),
////					R.drawable.bg_level);
////			Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, clWidth,
////					(int) (70 * LevelListActivity.density), false);
//			holder.imgView.setImageResource(R.drawable.bg_level);
//			holder.txtView.setText(position + 1 + "");
//		} else {
//			holder.imgView.setImageResource(R.drawable.bg_lock);
//		}
//
//		return row;
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	private static class Holder {
//		ImageView imgView = null;
//		TextView txtView = null;
//	}
//
//}
