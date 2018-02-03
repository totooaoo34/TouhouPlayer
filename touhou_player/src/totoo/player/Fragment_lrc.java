package totoo.player;

import totoo.touhouplayer.R;
import android.content.ContentResolver;
import android.content.Context;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class Fragment_lrc extends Fragment {
	GestureDetector gestureDetector;
	GestureOverlayView gestureDetector2;
	static ContentResolver contentResolver;
	static Thread thread1;
	static Thread thread2;
	static Context mContext;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_list_lrc, null);
		regConView();
		// thread2 = callThreadTwo();
		// thread1 = callOneThread();
		Activity_PlayingList.listViewSongs = (ListView) view
				.findViewById(R.id.listViewAllMusic);
		try {
			Constants.createListSongs(Activity_PlayingList.listViewSongs);
		} catch (Exception e) {
		}
		return view;
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

	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			ToastShow("敬请期待全新概念，绘图识别正在紧锣密鼓地制作中");
		return gestureDetector.onTouchEvent(event);
	}

	static void ToastShow(String string) {
		Toast.makeText(mContext, string, (short) 0).show();
	}
}
