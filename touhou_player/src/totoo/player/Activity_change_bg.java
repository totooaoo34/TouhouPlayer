package totoo.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import totoo.player.util.SystemUiHider;
import totoo.touhouplayer.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

public class Activity_change_bg extends Activity {
	private static final boolean AUTO_HIDE = true;
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	private static final boolean TOGGLE_ON_CLICK = true;
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	private SystemUiHider mSystemUiHider;
	GridView gridView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_change_bg);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.dummy_button).setOnTouchListener(
				mDelayHideTouchListener);
		regConView();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {

		@SuppressLint("NewApi")
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}

			gridView.setY(200);
			return false;
		}
	};
	//
	// @SuppressLint("NewApi")
	// public boolean onTouchEvent(MotionEvent event) {
	// return false;
	// };

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	@SuppressLint("NewApi")
	void regConView() {
		gridView = (GridView) findViewById(R.id.activity_change_bg_gridView1);
		ArrayList<Changer_Item> arrayList = new ArrayList<Changer_Item>();
		Changer_Item changer_Item = new Changer_Item();
		changer_Item.checked = true;
		changer_Item.img = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg);
		changer_Item.name = "默认";
		arrayList.add(changer_Item);
		Changer_Item changer_Item2 = new Changer_Item();
		changer_Item2.checked = false;
		changer_Item2.img = BitmapFactory.decodeResource(getResources(),
				R.drawable.gaoqing);
		changer_Item2.name = "高清";
		arrayList.add(changer_Item2);
		Changer_Item changer_Item3 = new Changer_Item();
		changer_Item3.checked = false;
		changer_Item3.img = BitmapFactory.decodeResource(getResources(),
				R.drawable.jijian);
		changer_Item3.name = "极简";
		arrayList.add(changer_Item3);
		Changer_Item changer_Item4 = new Changer_Item();
		changer_Item4.checked = false;
		changer_Item4.img = BitmapFactory.decodeResource(getResources(),
				R.drawable.jiandui);
		changer_Item4.name = "舰队";
		arrayList.add(changer_Item4);
		// Changer_Item changer_Item5 = new Changer_Item();
		// changer_Item5.checked = false;
		// changer_Item5.img = BitmapFactory.decodeResource(getResources(),
		// R.drawable.china);
		// changer_Item5.name = "自选";
		// arrayList.add(changer_Item5);
		li_changer_adp adapter_li_changer = new li_changer_adp(this, arrayList);
		gridView.setAdapter(adapter_li_changer);
		gridView.setY(300);
		Button button = (Button) findViewById(R.id.buttonChoose);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				// intent.addCategory("android.intent.category.DEFAULT");
				// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setType("image/*");
				Activity_change_bg.this.startActivityForResult(intent, 100);
			}
		});

	}

	File old_file, bg_file;
	Uri uri;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		String picturePath = "";
		if (100 == requestCode) {
			if (null != data)
				uri = data.getData();
			else {
				finish();
			}
			if (uri != null) {
				// Toast.makeText(this, uri.toString(),
				// Toast.LENGTH_LONG).show();
				try {
					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = this.getContentResolver().query(uri,
							filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					// picturePath 就是图片的路径
					picturePath = cursor.getString(columnIndex);
					cursor.close();

					if (new File(picturePath).exists()) {
						Constants.BG_img = new BitmapDrawable(
								BitmapFactory.decodeFile(picturePath));

						Message message = new Message();
						message.obj = picturePath;
						handler.sendMessage(message);
					} else {
						bg_file = new File(Constants.Music_file, "bg.jpg");
						if (bg_file.exists()) {
							if (null == Constants.BG_img) {
								Constants.BG_img = new BitmapDrawable(
										BitmapFactory.decodeFile((bg_file
												.getPath())));
							}
						} else {
							Constants.BG_img = null;
						}
					}

					Activity_Main.bg_Handler.sendEmptyMessage(0);

				} catch (Exception e) {
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// static Handler handler2 = new Handler();
	// Thread thread = new Thread() {
	// public void run() {
	//
	// bg_file = new File(VH.Music_file, "bg.jpg");
	// if (bg_file.exists()) {// 有背景图更名为旧的
	// old_file = new File(VH.Music_file, "old.jpg");
	// //
	// op_flag = false;
	// //
	// if (op_flag)
	// bg_file.renameTo(new File("old.jpg"));
	// }
	// if (op_flag)
	// try {
	// OutputStream outputStream = new FileOutputStream(bg_file);
	// String string = uri.getPath();
	// InputStream inputStream = new FileInputStream(string);
	// int len = 0;
	// byte[] buffer = new byte[8096];
	// while ((len = inputStream.read(buffer)) > 0) {
	// outputStream.write(buffer, 0, len);
	// }
	// outputStream.close();
	// inputStream.close();
	// } catch (Exception e) {
	// bg_file = new File(VH.Music_file, "bg.jpg");
	// old_file = new File(VH.Music_file, "old.jpg");
	// if (old_file.exists()) {// 旧的还原背景图
	// if (bg_file.exists())
	// bg_file.delete();
	// old_file.renameTo(new File(VH.Music_file, "bg.jpg"));
	// }
	// }
	//
	// try {
	// bg_file = new File(VH.Music_file, "bg.jpg");
	// if (bg_file.exists()) {
	// Bitmap decodeFile = BitmapFactory.decodeFile(bg_file
	// .getPath());
	// VH.BG_img = new BitmapDrawable(decodeFile);
	// }
	// } catch (Exception e) {
	// }
	// Activity_Main.bg_Handler.sendEmptyMessage(0);
	// }
	// };

	// Thread thread2 = new Thread() {
	// public void run() {
	//
	// bg_file = new File(VH.Music_file, "bg.jpg");
	// if (bg_file.exists()) {// 有背景图更名为旧的
	// bg_file.delete();
	// }
	//
	// Activity_Main.bg_Handler.sendEmptyMessage(0);
	// }
	// };

	class Changer_Item {
		public String name;
		public Bitmap img;
		public boolean checked;

	}

	View[] clickImgs;

	class li_changer_adp extends BaseAdapter {
		Context mContext;
		ArrayList<Changer_Item> mList;

		public li_changer_adp(Context mContext, ArrayList<Changer_Item> mList) {
			super();
			this.mContext = mContext;
			this.mList = mList;
		}

		public int getCount() {
			return mList.size();
		}

		public Object getItem(int position) {
			return mList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		int count = 0;
		int count2 = 0;

		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.li_bg_changer, null);
			}
			CheckBox checkBox = (CheckBox) convertView
					.findViewById(R.id.li_bg_changer_checkBox1);
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.li_bg_changer_imageView1);
			Changer_Item changer_Item = mList.get(position);
			// if(changer_Item.checked)
			checkBox.setChecked(changer_Item.checked);
			checkBox.setText(changer_Item.name);
			imageView.setImageDrawable(new BitmapDrawable(changer_Item.img));
			imageView.setTag(changer_Item.img);
			imageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Constants.BG_img = null;
					Constants.BG_img = new BitmapDrawable((Bitmap) v.getTag());
					Builder builder = new Builder(Activity_change_bg.this);
					View View = new ImageView(Activity_change_bg.this);
					View.setBackgroundDrawable(Constants.BG_img);
					builder.setView(View);
					builder.setTitle("预览");
					builder.setPositiveButton("使用（此功能正在做呢）", null);
					builder.setNegativeButton("关闭",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub

									finish();
								}
							});
					builder.show();
				}
			});
			return convertView;
		}
	}

	Handler handler = new Handler() {

		String string_path;

		public void handleMessage(android.os.Message msg) {
			string_path = (String) msg.obj;
			overrideBGImg();
		}
	};
	String string_path;

	void overrideBGImg() {
		if (new File(Constants.Music_file, "bg.jpg").exists()) {
			Builder builder = new Builder(Activity_change_bg.this);
			builder.setTitle("警告");
			builder.setMessage("旧的背景存在，是否要删除它");
			builder.setPositiveButton("删就删啰",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							bg_file = new File(Constants.Music_file, "bg.jpg");
							bg_file.delete();
							try {
								OutputStream outputStream = new FileOutputStream(
										bg_file);
								InputStream inputStream = new FileInputStream(
										string_path);
								int len = 0;
								byte[] buffer = new byte[8096];
								while ((len = inputStream.read(buffer)) > 0) {
									outputStream.write(buffer, 0, len);
								}
								outputStream.close();
								inputStream.close();
							} catch (Exception e) {
								bg_file = new File(Constants.Music_file,
										"bg.jpg");
								bg_file.delete();
							}
							// VH.BG_img = (BitmapDrawable) getResources()
							// .getDrawable(R.drawable.bg);
							// Activity_Main.bg_Handler.sendEmptyMessage(0);
							finish();
						}
					});
			builder.setNegativeButton("不要动它！",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			builder.show();

		} else {
			if (new File(string_path).exists()) {
				bg_file = new File(Constants.Music_file, "bg.jpg");
				try {
					OutputStream outputStream = new FileOutputStream(bg_file);
					InputStream inputStream = new FileInputStream(string_path);
					int len = 0;
					byte[] buffer = new byte[8096];
					while ((len = inputStream.read(buffer)) > 0) {
						outputStream.write(buffer, 0, len);
					}
					outputStream.close();
					inputStream.close();
				} catch (Exception e) {
				}

				Activity_Main.bg_Handler.sendEmptyMessage(0);
			}
			finish();
		}
	}
}
