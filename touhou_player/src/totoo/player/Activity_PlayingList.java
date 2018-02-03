package totoo.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import totoo.touhouplayer.R;
import android.os.Bundle;
import android.os.Handler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

public class Activity_PlayingList extends FragmentActivity
// implements OnGestureListener
{
	GestureDetector gestureDetector;
	static ListView listViewSongs, listViewLikes;
	static SimpleAdapter simpleAdapter_Songs, simpleAdapter_Likes;

	List<Map<String, String>> list = new ArrayList<Map<String, String>>();

	public static ProgressBar playing_progressBar;
	ViewPager pager;
	ListView lrc_ListView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MaxSreen();
		setContentView(R.layout.activity_playing_list);
		if (null == Service_Play.startMusicSer)
			Service_Play.startMusicSer = startService(new Intent(
					Constants.PlayerServiceName));
		FragmentManager manager = getSupportFragmentManager();
		pager = (ViewPager) findViewById(R.id.ViewPager_FragmentControler);
		pager.setAdapter(new controlerAdp(manager));
		try {
			Intent intent = getIntent();
			int pageNum = intent.getIntExtra("num", -1);
			if (pageNum != -1)
				pager.setCurrentItem(pageNum);
			else
				pager.setCurrentItem(1);
		} catch (Exception e) {
		}
		regConView();

	}

	protected void onRestart() {
		if (null == Service_Play.startMusicSer)
			Service_Play.startMusicSer = startService(new Intent(
					Constants.PlayerServiceName));
		super.onRestart();
	}

	protected void onDestroy() {
		// VH.killTask(this);
		super.onDestroy();
	}

	class controlerAdp extends FragmentPagerAdapter {

		public controlerAdp(FragmentManager fm) {
			super(fm);
		}

		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0:

				return new Fragment_Controler();
			case 1:

				return new Fragment_lrc();
			case 2:

				return new Fragment_pic();
			default:
				return null;
			}
		}

		public int getCount() {
			return 3;
		}
	}

	static Handler SDSystem_Handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (null != listViewLikes)
				Constants.createListLikes(listViewLikes);
		}
	};

	static Handler SystemFlash_Handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (null != listViewSongs)
				Constants.createListSongs(listViewSongs);
		}
	};

	class Kill_BroadCastReceiver extends BroadcastReceiver {

		public void onReceive(Context arg0, Intent intent) {
			finish();
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	void regConView() {
		lrc_ListView = (ListView) findViewById(R.id.lrc_ListView);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				getApplication(), android.R.layout.simple_list_item_1);
		arrayAdapter.add("歌词：");
		arrayAdapter.add("正在播放");
		arrayAdapter.add("详细");
		lrc_ListView.setAdapter(arrayAdapter);
		pager.setOnTouchListener(new OnTouchListener() {
			float beforeX;

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					beforeX = event.getX();
					break;
				case MotionEvent.ACTION_UP:
					if (beforeX - event.getX() > 50) {
						if (2 == pager.getCurrentItem()) {
							// startActivity(new
							// Intent(Activity_PlayingList.this,
							// Activity_Main.class));
							Activity_PlayingList.this
									.overridePendingTransition(R.anim.right_in,
											R.anim.left_out);
							finish();
						}
					} else if (event.getX() - beforeX > 50) {
						if (0 == pager.getCurrentItem()) {
							Activity_PlayingList.this
									.overridePendingTransition(
											android.R.anim.slide_in_left,
											android.R.anim.slide_out_right);
							finish();
						}
					}
				}
				return false;
			}
		});
		// gestureDetector = new GestureDetector(this, this);
		Kill_BroadCastReceiver killService_receiver = new Kill_BroadCastReceiver();
		// progress_changer_BroadCastReceiver MusicService_receiver = new
		// progress_changer_BroadCastReceiver();
		// registerReceiver(MusicService_receiver, new IntentFilter(
		// VH.BROADCAST_NAME));
		registerReceiver(killService_receiver, new IntentFilter(
				Constants.kill_BROADCAST_NAME));
	}

	private void MaxSreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.playing_list, menu);
		return true;
	}

	public boolean onDown(MotionEvent e) {
		return false;
	}

	public void onShowPress(MotionEvent e) {

	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	public void onLongPress(MotionEvent e) {

	}
	//
	// public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	// float velocityY) {
	// if (e1.getX() - e2.getX() > 5) {
	// // startActivity(new Intent(this, Activity_Splash.class));
	// Intent intent = new Intent(Constants.kill_BROADCAST_NAME);
	// sendBroadcast(intent);
	// this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
	// finish();
	// return true;
	//
	// } else if (e1.getX() - e2.getX() < -5) {
	// this.overridePendingTransition(android.R.anim.slide_in_left,
	// android.R.anim.slide_out_right);
	// finish();
	// return true;
	// }
	// return false;
	// }

	// void cl(ListView listView, SimpleAdapter sa, int ItemLayout_ID) {
	//
	// for (int i = 0; i < 3; i++) {
	// HashMap<String, String> item = new HashMap<String, String>();
	// item.put("id", "編號" + i);
	// item.put("name", "姓名" + i);
	// item.put("statu", "狀態" + i);
	// list.add(item);
	// }
	// sa = new SimpleAdapter(this, list, ItemLayout_ID, new String[] { "id",
	// "name", "statu" }, new int[] { R.id.id, R.id.name, R.id.statu });
	// listView.setAdapter(sa);
	// }
	// class progress_changer_BroadCastReceiver extends BroadcastReceiver {
	//
	// public void onReceive(Context arg0, Intent intent) {
	// int progress = intent.getIntExtra(VH.TAG_changedProgress, 0);
	// int max = intent.getIntExtra(VH.TAG_changedLimit, 100);
	// playing_progressBar.setMax(max);
	// playing_progressBar.setProgress(progress);
	// }
	// }

}
