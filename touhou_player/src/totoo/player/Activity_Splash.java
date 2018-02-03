package totoo.player;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import totoo.touhouplayer.R;
import android.view.GestureDetector;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.Toast;

public class Activity_Splash extends Activity implements OnGestureListener {
	GestureDetector gestureDetector;
	GestureOverlayView gestureDetector2;
	static ContentResolver contentResolver;
	static Thread thread1;
	static Thread thread2;
	static Context mContext;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MaxSreen();
		setContentView(R.layout.activity_splash);
		mContext = Activity_Splash.this;
		regConView();
		thread2 = callThreadTwo();
		thread1 = callOneThread();

	}

	protected void onRestart() {
		if (null == Service_Play.startMusicSer)
			Service_Play.startMusicSer = startService(new Intent(
					Constants.PlayerServiceName));
		super.onRestart();
	}

	protected void onDestroy() {
		Constants.selfKillFlag = true;
		Constants.killTask(this);
		super.onDestroy();
	}

	static Thread callOneThread() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				Constants.musicSystem = new MusicManager()
						.getSystemMuisc(contentResolver);
				Activity_Main.SystemFlash_Handler.sendEmptyMessage(0);
			}
		});
		thread.start();
		Service_Play.songBack = null;
		Constants.Play_id = 0;
		Service_Play.songNext = Constants.ReadSong(0);
		return thread;
	}

	static Thread callThreadTwo() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				Constants.SDSystem = new MusicManager().getSystemSD();
				Activity_Main.SDSystem_Handler.sendEmptyMessage(0);

			}
		});
		thread.start();
		ToastShow("少女休惬中");
		return thread;

	}

	private void regConView() {
		gestureDetector = new GestureDetector(this, this);
		gestureDetector2 = (GestureOverlayView) findViewById(R.id.Splash_gestureOverlayView);
		if (null == Service_Play.startMusicSer)
			Service_Play.startMusicSer = startService(new Intent(
					Constants.PlayerServiceName));
		Kill_BroadCastReceiver killService_receiver = new Kill_BroadCastReceiver();
		registerReceiver(killService_receiver, new IntentFilter(
				Constants.kill_BROADCAST_NAME));
		contentResolver = getContentResolver();
	}

	private void MaxSreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			ToastShow("敬请期待全新概念，绘图识别正在紧锣密鼓地制作中");
		return gestureDetector.onTouchEvent(event);
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

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		if (e1.getX() - e2.getX() > 5) {

			startActivity(new Intent(this, Activity_Main.class));
			this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
			return true;

		} else if (e1.getX() - e2.getX() < -5) {
			this.overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			finish();
			return true;
		}
		return false;
	}

	class Kill_BroadCastReceiver extends BroadcastReceiver {

		public void onReceive(Context arg0, Intent intent) {
			finish();
		}
	}

	static void ToastShow(String string) {
		Toast.makeText(mContext, string,Toast.LENGTH_SHORT).show();
	}
}
