package totoo.player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import totoo.player.network.Ex_adp;
import totoo.player.network.downLoad_item_Info;
import totoo.touhouplayer.R;
import totoo.ui.ItemListActivity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Activity_Main extends Activity implements OnSeekBarChangeListener,
		OnClickListener {

	static ListView lv_song, lv_like, listView;
	ImageView btn_go, btn_next, btn_back;
	SimpleAdapter simpleAdapter_Likes, simpleAdapter_Songs;
	static RelativeLayout main_RelativeLayout;
	static LinearLayout Main_c_LinearLayout;
	// static DrawerLayout main_drawerLayout;
	List<Map<String, String>> list_song = new ArrayList<Map<String, String>>();
	List<Map<String, String>> list_like = new ArrayList<Map<String, String>>();
	// GestureDetector gestureDetector;
	public static SeekBar Main_seekbar;
	static Context mContext;
	public static boolean SongSinglePlay = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (null == Service_Play.startMusicSer)
			Service_Play.startMusicSer = startService(new Intent(
					Constants.PlayerServiceName));
		regConView();
		Uri data = getIntent().getData();

		if (data != null) {
			// Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show();
			Service_Play.Action_ChangeSong(data);
		}
	}

	protected void onRestart() {
		if (null == Service_Play.startMusicSer)
			Service_Play.startMusicSer = startService(new Intent(
					Constants.PlayerServiceName));
		Uri data = getIntent().getData();

		if (data != null) {
			// Toast.makeText(this, data.toString(), 0).show();
			Service_Play.Action_ChangeSong(data);
		}
		macthBG();
		super.onRestart();
	}

	protected void onDestroy() {
		// VH.killTask(this);
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		bg_Handler.sendEmptyMessage(0);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (touchAble)
			switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:
				try {
					if (main_RelativeLayout.getX() > 75) {
						main_RelativeLayout.setX(0);
						Main_c_LinearLayout.setX(-200);
					} else {
						main_RelativeLayout.setX(150);
						Main_c_LinearLayout.setX(0);
					}
				} catch (Exception e) {
				}
				break;

			default:
				break;
			}
		return super.onKeyDown(keyCode, event);
		// true;

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Main_act_go_imageView:
			sendBroadcast(new Intent(Constants.BROADCAST_NAME).putExtra(
					Constants.TAG_rspID, Constants.BTN_Go));
			// Service_Play.play();
			break;
		case R.id.Main_act_next_ImageView02:
			ChangeMusicPlay(Constants.Play_id = Constants.Play_id + 1,
					Constants.musicBeans_all);

			break;
		case R.id.Main_act_back_ImageView01:
			ChangeMusicPlay(Constants.Play_id = Constants.Play_id - 1,
					Constants.musicBeans_all);

			break;

		default:
			break;
		}
	}

	// class MusicBroadCastReceiver extends BroadcastReceiver {
	//
	// public void onReceive(Context arg0, Intent intent) {
	// int id = intent.getIntExtra(VH.TAG_rspID, -1);
	// switch (id) {
	// case VH.changedProgress:
	// int progress = intent.getIntExtra(
	// VH.TAG_changedProgress, 0);
	// int max = intent
	// .getIntExtra(VH.TAG_changedLimit, 100);
	// Main_seekbar.setMax(max);
	// Main_seekbar.setProgress(progress);
	// default:
	// }
	// }
	// }

	static void ChangeMusicPlay(int id, List<MusicBean> list) {
		try {
			MusicBean hashMap = list.get(id);
			String stringname = hashMap.getMusicPath();

			Service_Play.Action_ChangeSong(new Intent().putExtra("name",
					stringname));
		} catch (Exception e) {
		}
	}

	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.playing_list, menu);
	// menu.add("放大");
	// menu.add("分享");
	// menu.add("刪除");
	// main_drawerLayout.openDrawer(Gravity.LEFT);
	// return true;
	// }

	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case 0:
	// Intent intent = new Intent(this, Activity_PlayingList.class);
	// startActivityForResult(intent, Constants.MusicR_code);
	// break;
	// case 1:
	//
	// break;
	// case 2:
	//
	// break;
	//
	// default:
	// break;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	class Kill_BroadCastReceiver extends BroadcastReceiver {

		public void onReceive(Context arg0, Intent intent) {
			finish();
		}
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		Intent intent = new Intent(Constants.BROADCAST_NAME);
		intent.putExtra(Constants.TAG_rspID, Constants.changedProgress);
		int i = seekBar.getProgress();
		intent.putExtra(Constants.TAG_changedProgress, i);
		sendBroadcast(intent);
	}

	// 空事件_________________________________________________________________________________________________

	//
	void tz(String str) {//
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();//
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
	}

	// final int touchXLimit = 400;
	float beforeNum = 0;
	int sinceNum;
	float fillingNum;

	boolean clickFlag = false;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public boolean onTouchEvent(MotionEvent event) {//
		// tz("你摸了我一下");
		try {
			Thread.sleep(40);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (touchAble)
			try {
				fillingNum = event.getX();
				main_RelativeLayout.setX((int) fillingNum - beforeNum);
				// Main_c_LinearLayout.setX(fillingNum - 120 - beforeNum);
				// listView.setX((int) (fillingNum ));
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					beforeNum = event.getX();
					Main_c_LinearLayout.setX(0);
					break;
				case MotionEvent.ACTION_UP:
					// sinceNum = (int) event.getX();
					if (main_RelativeLayout.getX() < 150) {
						main_RelativeLayout.setX(0);
						Main_c_LinearLayout.setX(-200);
					} else {
						main_RelativeLayout.setX(150);
						Main_c_LinearLayout.setX(0);
					}

					// if (clickFlag) {
					// clickFlag = false;
					// startActivity(new Intent(this,
					// Activity_PlayingList.class));
					// this.overridePendingTransition(R.anim.right_in,
					// R.anim.left_out);
					// }
					// if (beforeNum > touchXLimit) {
					// clickFlag = true;

					if (beforeNum - event.getX() > 350) {
						startActivity(new Intent(this,
								Activity_PlayingList.class));
						this.overridePendingTransition(R.anim.right_in,
								R.anim.left_out);
					} else if (event.getX() - beforeNum > 350) {
						this.overridePendingTransition(
								android.R.anim.slide_in_left,
								android.R.anim.slide_out_right);
						finish();
					}
					break;

				// case KeyEvent.KEYCODE_MENU:
				// // if (main_drawerLayout.isDrawerOpen(Gravity.LEFT))
				// // main_drawerLayout.closeDrawers();
				// // else
				// // main_drawerLayout.openDrawer(Gravity.LEFT);
				//
				}

			} catch (Exception e) {
			}
		else {
			bflag++;
			if (bflag>15) {
				macthTouch(); 
				bflag = 0;

			}
		}
		return false;
		// gestureDetector.onTouchEvent(event);//
	}//

	int bflag = 0;

	// public boolean onDown(MotionEvent e) {//
	// return false;//
	// }//
	// //
	//
	// public void onShowPress(MotionEvent e) {//
	// //
	// }//
	//
	// public boolean onSingleTapUp(MotionEvent e) {//
	// return false;//
	// }//
	// //
	//
	// public boolean onScroll(MotionEvent e1, MotionEvent e2, float
	// distanceX,//
	// float distanceY) {//
	// return false;
	// }//
	// //
	//
	// public void onLongPress(MotionEvent e) {//
	// //
	// }//
	// //
	// 空事件结束_________________________________________________________________________________________________
	//
	// public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	// float velocityY) {
	//
	// // if (event.getX() - beforeX > 50) {
	// // if (main_drawerLayout.isDrawerVisible(Gravity.RIGHT)) {
	// // main_drawerLayout.closeDrawers();
	// // }
	// // } else if (beforeX - event.getX() > 50) {
	// // try {
	// // main_drawerLayout.openDrawer(Gravity.RIGHT);
	// // } catch (Exception e) {
	// // }
	// // }
	// // // else {
	// // // startActivity(new Intent(Activity_Main.this,
	// // // Activity_PlayingList.class));
	// // // Activity_Main.this.overridePendingTransition(
	// // // R.anim.right_in, R.anim.left_out);
	// // // }
	// // }
	// // if (event.getX() - beforeX > 300) {
	// // // if (main_drawerLayout.isDrawerVisible(Gravity.LEFT)) {
	// // // main_drawerLayout.closeDrawers();
	// // // } else {
	// // Activity_Main.this.overridePendingTransition(
	// // android.R.anim.slide_in_left,
	// // android.R.anim.slide_out_right);
	// // finish();}}
	// return true;
	// }

	void regConView() {
		// MusicBroadCastReceiver MusicService_receiver; = new
		// MusicBroadCastReceiver();
		// registerReceiver(MusicService_receiver,
		// new IntentFilter(VH.BROADCAST_NAME));
		Kill_BroadCastReceiver killService_receiver = new Kill_BroadCastReceiver();
		registerReceiver(killService_receiver, new IntentFilter(
				Constants.kill_BROADCAST_NAME));
		// gestureDetector = new GestureDetector(this, this);
		mContext = Activity_Main.this;
		if (null == Activity_Splash.thread1) {
			Activity_Splash.mContext = Activity_Main.this;
			Activity_Splash.contentResolver = Activity_Main.this
					.getContentResolver();
			Activity_Splash.thread1 = Activity_Splash.callOneThread();
			Activity_Splash.thread2 = Activity_Splash.callThreadTwo();
		}
		if (null == Service_Play.startMusicSer)
			Service_Play.startMusicSer = startService(new Intent(
					Constants.PlayerServiceName));
		//
		listView = (ListView) findViewById(R.id.Main_c_ListView);
		// listViewLinearLayout = (LinearLayout)
		// findViewById(R.id.Main_controler_LinearLayout);
		// listView.setLeft(-200);
		lv_song = (ListView) findViewById(R.id.listViewSongs);
		Constants.createListSongs(lv_song);
		lv_like = (ListView) findViewById(R.id.listViewlikes);
		Constants.createListLikes(lv_like);
		Main_seekbar = (SeekBar) findViewById(R.id.Main_seekBar);
		Main_seekbar.setOnSeekBarChangeListener(this);
		Main_seekbar.setMax(Constants.current_play_max);
		btn_go = (ImageView) findViewById(R.id.Main_act_go_imageView);
		btn_back = (ImageView) findViewById(R.id.Main_act_back_ImageView01);
		btn_next = (ImageView) findViewById(R.id.Main_act_next_ImageView02);
		btn_go.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		// main_drawerLayout = (DrawerLayout)
		// findViewById(R.id.Main_controler_DrawerLayout);
		// main_drawerLayout.setOnTouchListener(new OnTouchListener() {
		// @SuppressLint("NewApi")
		// public boolean onTouch(View v, MotionEvent event) {
		//
		// fillingNum = event.getX();
		// // main_RelativeLayout.setLeft((int) fillingNum);
		// // listView.setLeft((int) (fillingNum - 120));
		//
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// if (fillingNum > 100) {
		// if (main_drawerLayout.isDrawerOpen(Gravity.LEFT))
		// main_drawerLayout.closeDrawers();
		// else
		// main_drawerLayout.openDrawer(Gravity.LEFT);
		// if (main_RelativeLayout.getLeft() < 100)
		// if (!main_drawerLayout.isDrawerOpen(Gravity.LEFT))
		// main_RelativeLayout.setLeft(150);
		// else
		// main_RelativeLayout.setLeft(0);
		// }
		// break;
		// case MotionEvent.ACTION_UP:
		// // sinceNum = (int) event.getX();
		//
		// // listView.setLeft(0);
		//
		// }
		// return false;
		// }
		// });
		// cl(lv_like, simpleAdapter_Likes, R.layout.li_likes, list_like);
		// cl(lv_song, simpleAdapter_Songs, R.layout.li_songs, list_song);
		Activity_Main.LayoutSystem_Handler.sendEmptyMessage(0);
		main_RelativeLayout = (RelativeLayout) findViewById(R.id.main_RelativeLayout);
		Main_c_LinearLayout = (LinearLayout) findViewById(R.id.Main_c_LinearLayout);
		// new Thread(new Runnable() {
		// public void run() {
		macthBG();
		// }
		// }).start();
//		macthTouch();
	}

	void macthTouch() {
		// TODO Auto-generated method stub

//		preferences = getSharedPreferences("boy", Context.MODE_PRIVATE);
//
//		editor = preferences.edit();
//		if (-1 == preferences.getInt("a", -1) || !touchAble) {
			builder = new Builder(this);
			builder.setTitle("您正在使用的是2.2以上系统吗？");
			builder.setPositiveButton("打开预览模式",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
//
//							editor.putInt("a", defaultNum);
//							editor.commit();// 提交数据保存
							touchAble = true;
						}
					});
			builder.setNegativeButton("兼容",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
//							editor.putInt("a", 0);
//							editor.commit();// 提交数据保存
							touchAble = false;
						}
					});
			builder.show();
		}
//	}

	Builder builder;
	boolean touchAble = false;
	SharedPreferences preferences;
	Editor editor;
	int defaultNum = 1;
	static File bg_file;
	static Handler bg_Handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			bg_file = new File(Environment.getExternalStorageDirectory(),
					"/music/bg.jpg");
			try {
				if (null == Constants.BG_img) {
					Constants.BG_img = ((BitmapDrawable) (mContext
							.getResources().getDrawable(R.drawable.bg)));
				}
				main_RelativeLayout.setBackgroundDrawable(Constants.BG_img);
				if (bg_file.exists())
					Constants.BG_img = new BitmapDrawable(
							BitmapFactory.decodeFile((bg_file.getPath())));

			} catch (Exception e) {
			}
		}
	};
	static ArrayAdapter<String> List_arrayAdapter;
	static Handler LayoutSystem_Handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			List_arrayAdapter = new ArrayAdapter<String>(mContext,
					android.R.layout.simple_list_item_1);
			List_arrayAdapter.add("「更换・背景」");// 0
			List_arrayAdapter.add("「单曲・循环」");
			List_arrayAdapter.add("「收藏・歌单」");
			List_arrayAdapter.add("「填词・图片」");
			List_arrayAdapter.add("「在线・资源」");
			List_arrayAdapter.add("「网络・传输」");// 5

			List_arrayAdapter.add("「设置・铃声」");
			List_arrayAdapter.add("「退出」");
			List_arrayAdapter.add("");
			List_arrayAdapter.add("版本：1.8.0");
			List_arrayAdapter.add("");
			List_arrayAdapter.add("");
			List_arrayAdapter.add("");
			List_arrayAdapter.add("");
			List_arrayAdapter.add("");
			List_arrayAdapter.add("");
			listView.setAdapter(List_arrayAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				Builder builder;

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					switch (position) {
					case 5:
						Intent intent4 = new Intent(Activity_Main.mContext,
								ItemListActivity.class);
						mContext.startActivity(intent4);

						break;

					case 4:
						try {
							builder = new Builder(Activity_Main.mContext);
							View view2 = LayoutInflater.from(mContext).inflate(
									R.layout.online_get, null);
							// builder.setMessage("敬请期待");
							ExpandableListView findViewById = (ExpandableListView) view2
									.findViewById(R.id.online_get_expandableListView1);
							List<downLoad_item_Info> mListKind = new ArrayList<downLoad_item_Info>();
							List<List<downLoad_item_Info>> mListAll = new ArrayList<List<downLoad_item_Info>>();

							mListKind.add(new downLoad_item_Info("       【"
									+ "        幽闭サテライト - " + "】", "", "", ""));

							List<downLoad_item_Info> mlist = new ArrayList<downLoad_item_Info>();
							mlist.add(new downLoad_item_Info("华鸟风月 ", "", "",
									"              - senya- "));
							mlist.add(new downLoad_item_Info("三千世界 ", "", "",
									"              - senya- "));
							mlist.add(new downLoad_item_Info(" UN 孤独月 ", "",
									"", "              - senya- "));
							mlist.add(new downLoad_item_Info(" 千华缭乱 ", "", "",
									"              - senya- "));
							mlist.add(new downLoad_item_Info(" カフカ群青へ ", "",
									"", "              - senya- "));
							mListAll.add(mlist);
							mListKind
									.add(new downLoad_item_Info("       【"
											+ "        舞风 - MAIKAZE" + "】", "",
											"", ""));
							mlist = new ArrayList<downLoad_item_Info>();
							mlist.add(new downLoad_item_Info(" そこに在るもの ", "",
									"", "              - TOKINE - "));
							mlist.add(new downLoad_item_Info(" 愿いを呼ぶ季节 ", "",
									"", "              - TOKINE - "));
							mlist.add(new downLoad_item_Info(" Memory ", "",
									"", "              - 朝木ゆう - "));
							mListAll.add(mlist);

							Ex_adp adapter = new Ex_adp(mListKind, mListAll,
									mContext);
							findViewById.setAdapter(adapter);
							builder.setView(view2);
							builder.setNegativeButton("终了",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									});
							builder.show();
						} catch (Exception e) {
							Toast.makeText(mContext, "阿勒，这个界面奔溃了",
									Toast.LENGTH_SHORT).show();
						}

						break;
					case 3:
						Intent intent3 = new Intent(Activity_Main.mContext,
								Activity_PlayingList.class);
						intent3.putExtra("num", 2);
						Activity_Main.mContext.startActivity(intent3);
						break;

					case 2:
						Intent intent2 = new Intent(Activity_Main.mContext,
								Activity_PlayingList.class);
						intent2.putExtra("num", 0);
						Activity_Main.mContext.startActivity(intent2);
						break;
					case 6:
						builder = new Builder(Activity_Main.mContext);
						//
						// builder.setView(LayoutInflater.from(mContext).inflate(
						// R.layout.ad_view, null));
						builder.setMessage("这个页面,呵其实真的是懒得做");
						builder.setNegativeButton("终了",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								});
						builder.show();
						break;
					case 9:
						builder = new Builder(Activity_Main.mContext);

						builder.setTitle("1.8重磅打造");
						builder.setMessage("好音质，无需隐藏");
						builder.setNegativeButton("看过了",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								});
						builder.show();
						break;
					case 0:
						Intent intent = new Intent(Activity_Main.mContext,
								Activity_change_bg.class);
						((Activity_Main) (Activity_Main.mContext))
								.startActivityForResult(intent, 0);
						break;

					case 1:
						// builder = new Builder(Activity_Main.mContext);
						//
						// builder.setMessage("这个还没有做完呢");
						// builder.setNegativeButton("终了",
						// new DialogInterface.OnClickListener() {
						// public void onClick(DialogInterface dialog,
						// int which) {
						// }
						// });
						// builder.show();
						String stringTip = "";
						if (SongSinglePlay) {
							SongSinglePlay = false;
							stringTip = "现在将以单曲循环";
						} else {
							SongSinglePlay = true;
							stringTip = "单曲播放";
						}
						Toast.makeText(Activity_Main.mContext, stringTip,
								Toast.LENGTH_SHORT).show();
						break;

					case 7:
						Constants.selfKillFlag = true;
						Constants.killTask(Service_Play.mContext);

						break;

					}
				}
			});
		}
	};
	static Handler toast_Handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String string = (String) msg.obj;
			Toast.makeText(Activity_Main.mContext, string, Toast.LENGTH_SHORT)
					.show();

		}
	};
	static Handler SDSystem_Handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (lv_like != null)
				try {
					Constants.createListLikes(lv_like);
				} catch (Exception e) {
				}
		}
	};

	static Handler SystemFlash_Handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (lv_song != null)
				try {
					if (null == Constants.musicSystem) {
						Constants.musicSystem = new ArrayList<Map<String, String>>();
						Map<String, String> item = new HashMap<String, String>();
						item.put("name", "三千世界鸦杀尽");
						item.put("statu", "无名");
						item.put("id", "2MB");
						Constants.musicBeans_all.add(new MusicBean("三千世界鸦杀尽",
								""));
						Constants.musicSystem.add(item);
					}
					Constants.createListSongs(lv_song);
				} catch (Exception e) {
				}
		}
	};

	private void macthBG() {
		// TODO Auto-generated method stub
		try {
			File file = new File(Constants.SD_path + "/Music/bg.jpg");
			if (file.exists()) {
				Bitmap decodeFile = BitmapFactory.decodeFile(Constants.SD_path
						+ "/music/bg.jpg");
				Constants.BG_img = new BitmapDrawable(decodeFile);
			} else {
				Constants.BG_img = null;
			}
		} catch (Exception e) {
			Constants.BG_img = null;
		}
		bg_Handler.sendEmptyMessage(0);
	}
}

// void cl(ListView listView, SimpleAdapter sa, int ItemLayout_ID,
// List<Map<String, String>> list) {
//
// for (int i = 0; i < 3; i++) {
// HashMap<String, String> item = new HashMap<String, String>();
// item.put("id", "bilibili" + i);
// item.put("name", "bilibili" + i);
// item.put("statu", "bilibili" + i);
// list.add(item);
// }
// sa = new SimpleAdapter(this, list, ItemLayout_ID, new String[] { "id",
// "name", "statu" }, new int[] { R.id.id, R.id.name, R.id.statu });
// listView.setAdapter(sa);
// }
class MusicManager {
	public static final String[] MUISC_ATTRS = new String[] {
			// 歌曲名
			MediaStore.Audio.Media.DISPLAY_NAME,
			// 歌手名
			MediaStore.Audio.Media.ARTIST,
			// 歌曲大小
			MediaStore.Audio.Media.SIZE,
			// 歌曲时间__________________________________________________________________________
			MediaStore.Audio.Media.DURATION,
			// 专辑名
			MediaStore.Audio.Media.ALBUM,
			// 歌曲ID
			MediaStore.Audio.Media._ID,
			// 歌曲路径
			MediaStore.Audio.Media.DATA };

	public List<Map<String, String>> getSystemMuisc(ContentResolver cr) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// 查询所有音乐信息
		Map<String, String> item;
		MusicBean musicBean;

		Cursor c = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				MUISC_ATTRS, null, null, null);
		String name;
		String song;
		int size;
		// 遍历获取数据信息
		while (c.moveToNext()) {
			item = new HashMap<String, String>();
			musicBean = new MusicBean();
			musicBean.setMusicSize(size = c.getInt(2));
			musicBean.setMusicSinger(name = c.getString(1));
			musicBean.setMusicPath(c.getString(6));
			musicBean.setMusicName(c.getString(0));
			song = c.getString(0);
			if (song.endsWith(".mp3") || song.endsWith(".ogg")
					|| song.endsWith(".acc") || song.endsWith(".wav")
					|| song.endsWith(".f4v") || song.endsWith(".ape")
					|| song.endsWith(".flac"))
				if (size > 1000000)
					if (name != null) {
						item.put("name", song);
						item.put("statu", name);
						item.put("id", (int) (size / 1024 / 1024) + "MB");
						Constants.musicBeans_all.add(musicBean);
						list.add(item);
					}
		}
		if (list.isEmpty()) {
			item = new HashMap<String, String>();
			item.put("name", "三千世界鸦杀尽");
			item.put("statu", "无名");
			item.put("id", "2MB");
			Constants.musicBeans_all.add(new MusicBean("三千世界鸦杀尽", ""));
			list.add(item);
		}
		return list;
	}

	public List<Map<String, String>> getSystemSD() {
		ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
		Constants.playingBeans = new ArrayList<MusicBean>();
		// 查询所有sd信息
		Map<String, String> item;
		MusicBean musicBean;
		File file = Environment.getExternalStorageDirectory();
		File[] node = file.listFiles();
		String song;
		String name;
		long size;
		for (File child : node) {
			song = child.getAbsolutePath();
			size = child.length();
			if (song.endsWith(".mp3") || song.endsWith(".ogg")
					|| song.endsWith(".acc") || song.endsWith(".wav")
					|| song.endsWith(".f4v") || song.endsWith(".ape")
					|| song.endsWith(".flac"))
				if (size > 1000000) {

					musicBean = new MusicBean();
					name = song.substring(song.lastIndexOf("/") + 1,
							song.lastIndexOf("."));
					musicBean.setMusicName(name);
					musicBean.setMusicPath(song);
					musicBean.setMusicSize((int) size);
					Constants.playingBeans.add(musicBean);
					item = new HashMap<String, String>();
					item.put("name", name);
					item.put("id", (int) (size / 1024 / 1024) + "MB");
					arrayList.add(item);
				}
		}
		return arrayList;
	}
}

class MusicBean {
	private int _id;
	private String musicName;
	private String musicSinger;
	private int musicTime;
	private String musicAlbum;
	private int musicSize;
	private String musicPath;
	private String sortLetter;

	public MusicBean() {
	}

	public MusicBean(String name, String path) {
		musicName = name;
		musicPath = path;
	}

	public String getSortLetter() {
		return sortLetter;
	}

	public void setSortLetter(String sortLetter) {
		this.sortLetter = sortLetter;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getMusicName() {
		return musicName;
	}

	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}

	public String getMusicSinger() {
		return musicSinger;
	}

	public void setMusicSinger(String musicSinger) {
		this.musicSinger = musicSinger;
	}

	public int getMusicTime() {
		return musicTime;
	}

	public void setMusicTime(int musicTime) {
		this.musicTime = musicTime;
	}

	public String getMusicAlbum() {
		return musicAlbum;
	}

	public void setMusicAlbum(String musicAlbum) {
		this.musicAlbum = musicAlbum;
	}

	public int getMusicSize() {
		return musicSize;
	}

	public void setMusicSize(int musicSize) {
		this.musicSize = musicSize;
	}

	public String getMusicPath() {
		return musicPath;
	}

	public void setMusicPath(String musicPath) {
		this.musicPath = musicPath;
	}
}

// class MusicExpandData {
// List<String> groupListData;
// List<List<MusicBean>> childListData;
//
// public List<String> getGroupListData() {
// return groupListData;
// }
//
// public void setGroupListData(List<String> groupListData) {
// this.groupListData = groupListData;
// }
//
// public List<List<MusicBean>> getChildListData() {
// return childListData;
// }
//
// public void setChildListData(List<List<MusicBean>> childListData) {
// this.childListData = childListData;
// }
// }

// public MusicExpandData getSystemMusicForExpand(ContentResolver cr) {
// MusicExpandData med = new MusicExpandData();
//
// List<MusicBean> list = getSystemMuisc(cr);
//
// List<String> groupList = new ArrayList<String>();
// List<List<MusicBean>> chirdList = new ArrayList<List<MusicBean>>();
// int k = 0;// 用于计算字母的下标
//
// groupList.add("A");
// chirdList.add(new ArrayList<MusicBean>());
//
// for (int i = 0; i < list.size(); i++) {
// MusicBean mb = list.get(i);
// String sortLetter = mb.getSortLetter();
// for (int j = k; j < Letters.length; j++) {
// if (sortLetter.equals(Letters[j])) {
// // 有相等的时候
// List<MusicBean> _list = chirdList.get(j);
// _list.add(mb);
// break;
// } else {// 将下标移动到下一位
//
// k++;
// if (k == Letters.length)
// break;
// groupList.add(Letters[j + 1]);
// chirdList.add(new ArrayList<MusicBean>());
// }
// }
// }
// // 遍历数组去掉空的数组
// for (int i = 0; i < groupList.size(); i++) {
// if (chirdList.get(i).size() < 1) {
// groupList.remove(i);
// chirdList.remove(i);
// i--;
// }
// }
// med.setChildListData(chirdList);
// med.setGroupListData(groupList);
// return med;
// }
