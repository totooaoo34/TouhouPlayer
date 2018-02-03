package totoo.player;

import java.io.IOException;

import totoo.touhouplayer.R;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class Service_Play extends Service {
	static Context mContext;
	public static String songNext;
	public static String songBack;
	public static MediaPlayer mediaPlayer;
	NotificationManager nm;
	NotificationCompat.Builder builder;
	AppWidgetManager widgetManager;
	public RemoteViews ControlerViews;
	public RemoteViews WidgetViews;
	ComponentName Widget_componentName;
	public static ComponentName startMusicSer;
	MusicBroadCastReceiver MusicService_receiver;
	savePower_BroadCastReceiver powerSave_bc;
	powerGo_BroadCastReceiver powerGo_bc;
	Kill_BroadCastReceiver killService_receiver;
	static Runnable runThread;

	// public static final String thisServiceName = "totoo.act";

	public void onCreate() {
		super.onCreate();
		Service_Play.mediaPlayer = MediaPlayer.create(this, R.raw.default_bg);
		if (mediaPlayer != null)
			Constants.current_play_max = mediaPlayer.getDuration();
		regConView();
		ShowUpControler();
		UpdateWidget();
		changeViewShowProgress();
		mContext = Service_Play.this;
	}

	public void onDestroy() {
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
		}
		mediaPlayer = null;
		try {
			handler.removeCallbacks(r);
			if (MusicService_receiver != null)
				unregisterReceiver(MusicService_receiver);
			if (killService_receiver != null)
				unregisterReceiver(killService_receiver);
			if (powerSave_bc != null)
				unregisterReceiver(powerSave_bc);
		} catch (Exception e) {
		}
		nm.cancel(Constants.BTN_Finsh);
		startMusicSer = null;
		super.onDestroy();
	}

	static Handler handler = new Handler() {
	};
	Runnable r = new Runnable() {

		public void run() {
			try {
				if (mediaPlayer != null)
					if (mediaPlayer.isPlaying()) {
						if (null != Activity_Main.Main_seekbar)
							Activity_Main.Main_seekbar.setProgress(mediaPlayer
									.getCurrentPosition());
						handler.postDelayed(r, 1000);
					} else
						handler.removeCallbacks(r);
				ShowUpControler();
				UpdateWidget();
				changeViewShowProgress();
			} catch (Exception e) {
				nm.cancel(Constants.BTN_Finsh);
			}
		}
	};

	public class MusicBroadCastReceiver extends BroadcastReceiver {

		public void onReceive(Context arg0, Intent intent) {
			int id = intent.getIntExtra(Constants.TAG_rspID, -1);
			switch (id) {
			case Constants.BTN_next:
				if (songNext != null)
					intent.putExtra("name", songNext);
				Action_ChangeSong(intent);
				break;
			case Constants.BTN_back:
				if (songBack != null)
					intent.putExtra("name", songBack);
				Action_ChangeSong(intent);
				break;
			case Constants.BTN_Go:
				if (Constants.playFlag) {
					Constants.playFlag = false;
					mediaPlayer.start();
					handler.removeCallbacks(runThread);
					handler.post(r);
				} else {
					Constants.playFlag = true;
					mediaPlayer.pause();
					handler.removeCallbacks(r);
				}
				break;
			case Constants.BTN_Finsh:
				Constants.selfKillFlag = true;
				Constants.killTask(Service_Play.this);
				break;
			case Constants.changedSong:
				Action_ChangeSong(intent);
				break;
			case Constants.changedProgress:
				int i = intent.getIntExtra(Constants.TAG_changedProgress, 0);
				mediaPlayer.seekTo(i);
				break;

			default:
			}
		}
	}

	void callNextSong() {
	}

	RemoteViews createNotifiy() {
		RemoteViews CV = new RemoteViews(getPackageName(),
				R.layout.notifiy_controler);
		nm = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
		builder = new NotificationCompat.Builder(this);
		builder.setContentText("播放音乐").setSmallIcon(R.drawable.ic_launcher);
		builder.setContent(CV).setContentIntent(
				PendingIntent.getActivity(this, 0, new Intent(this,
						Activity_Main.class), 0));
		SetClickEvent(Constants.BTN_Go, R.id.ImageViewGo, CV, 0);
		SetClickEvent(Constants.BTN_Finsh, R.id.controler_CloseImageView, CV, 0);
		SetClickEvent(Constants.BTN_next, R.id.imageViewPlayForward, CV, 1);
		SetClickEvent(Constants.BTN_back, R.id.ImageViewPlayBack, CV, -1);
		return CV;
	}

	RemoteViews createWidget() {
		widgetManager = AppWidgetManager.getInstance(this);
		RemoteViews WV = new RemoteViews(getPackageName(), R.layout.widget_main);
		Widget_componentName = new ComponentName(this, Widget_desktop.class);
		SetClickEvent(Constants.BTN_Go, R.id.widget_imageView_go, WV, 0);
		SetClickEvent(Constants.BTN_next, R.id.widget_ImageView_next, WV, 1);
		SetClickEvent(Constants.BTN_back, R.id.widget_ImageView_back, WV, -1);
		return WV;
	}

	void UpdateWidget() {
		WidgetViews.setTextViewText(R.id.Widget_Time_View,
				getTime(mediaPlayer.getCurrentPosition()));
		widgetManager.updateAppWidget(Widget_componentName, WidgetViews);
	}

	void ShowUpControler() {
		if (mediaPlayer != null)
			if (mediaPlayer.isPlaying())
				ControlerViews.setProgressBar(R.id.progressBarCon,
						mediaPlayer.getDuration(),
						mediaPlayer.getCurrentPosition(), false);
		nm.notify(Constants.BTN_Finsh, builder.build());
	}

	void regConView() {
		MusicService_receiver = new MusicBroadCastReceiver();
		powerSave_bc = new savePower_BroadCastReceiver();
		killService_receiver = new Kill_BroadCastReceiver();
		powerGo_bc = new powerGo_BroadCastReceiver();
		registerReceiver(powerGo_bc, new IntentFilter(
				"android.intent.action.SCREEN_ON"));
		registerReceiver(MusicService_receiver, new IntentFilter(
				Constants.BROADCAST_NAME));
		registerReceiver(powerSave_bc, new IntentFilter(
				"android.intent.action.SCREEN_OFF"));
		registerReceiver(killService_receiver, new IntentFilter(
				Constants.kill_BROADCAST_NAME));
		if (null == ControlerViews)
			ControlerViews = createNotifiy();
		if (null == WidgetViews)
			WidgetViews = createWidget();
		runThread = r;
		handler.post(r);
	}

	class Kill_BroadCastReceiver extends BroadcastReceiver {

		public void onReceive(Context arg0, Intent intent) {
			stopSelf();
		}
	}

	class savePower_BroadCastReceiver extends BroadcastReceiver {
		public void onReceive(Context arg0, Intent intent) {
			try {
				handler.removeCallbacks(r);
			} catch (Exception e) {
			}
		}
	}

	class powerGo_BroadCastReceiver extends BroadcastReceiver {
		public void onReceive(Context arg0, Intent intent) {
			try {
				handler.post(r);
			} catch (Exception e) {
			}
		}
	}

	public String getTime(long milltime) {
		int time = (int) (milltime / 1000);
		int min = time / 60;
		int sec = time % 60;
		return String.format(" %02d:%02d ", min, sec);

	}

	private void SetClickEvent(int Btn_id, int Res, RemoteViews remoteViews,
			int flag) {
		Intent intentPlay = new Intent(Constants.BROADCAST_NAME);
		intentPlay.putExtra(Constants.TAG_rspID, Btn_id);
		intentPlay.putExtra(Constants.TAG_ChangeSongFlag, flag);
		PendingIntent playPendingIntent = PendingIntent.getBroadcast(this,
				Btn_id, // 发送的请求的codeID
				intentPlay, 0);// flag
		remoteViews.setOnClickPendingIntent(Res, playPendingIntent);
	}

	public static void Action_ChangeSong(Intent intent) {
		String filePath = intent.getStringExtra("name");
		int flag = intent.getIntExtra("flag", 1);
		if (null != mediaPlayer) {
			mediaPlayer.reset();
			mediaPlayer.release();
		}
		if (filePath != "")
			try {
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(filePath);
				mediaPlayer.prepare();
			} catch (Exception e) {
			}
		else {
			mediaPlayer = MediaPlayer.create(mContext, R.raw.default_bg);
		}
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer arg0) {
				if (!Activity_Main.SongSinglePlay) {
					// arg0.reset();
					arg0.start();
					arg0.start();
				}
			}
		});
		if (1 == flag)
			Constants.Play_id++;
		else if (-1 == flag)
			Constants.Play_id--;
		songBack = Constants.ReadSong(-1);
		songNext = Constants.ReadSong(1);
		mediaPlayer.start();
		Activity_Main.Main_seekbar.setMax(mediaPlayer.getDuration());
		handler.removeCallbacks(runThread);
		handler.post(runThread);
	}

	public IBinder onBind(Intent arg0) {
		return new serGet();
	}

	public class serGet extends Binder {
		public Service_Play GetSer() {
			return Service_Play.this;
		}
	}

	public static void play() {
		if (mediaPlayer != null)
			mediaPlayer.start();

	}

	public void pause() {
		mediaPlayer.pause();

	}

	public void playNext() {
		mediaPlayer.start();

	}

	private void changeViewShowProgress() {
		if (mediaPlayer != null)
			if (Activity_Main.Main_seekbar != null) {
				Activity_Main.Main_seekbar.setMax(mediaPlayer.getDuration());
				Activity_Main.Main_seekbar.setProgress(mediaPlayer
						.getCurrentPosition());
			}
		if (Activity_PlayingList.playing_progressBar != null) {
			Activity_PlayingList.playing_progressBar.setMax(mediaPlayer
					.getDuration());
			Activity_PlayingList.playing_progressBar.setProgress(mediaPlayer
					.getCurrentPosition());
		}
	}

	public static void Action_ChangeSong(Uri data) {

		if (data != null) {
			if (null != mediaPlayer) {
				// mediaPlayer.reset();
				mediaPlayer.release();
				mediaPlayer = new MediaPlayer();
				try {
					mediaPlayer.setDataSource(Activity_Main.mContext, data);
				} catch (Exception e) {
				}

			}
		} else {
			mediaPlayer = MediaPlayer.create(mContext, R.raw.default_bg);
		}
		try {
			mediaPlayer.prepare();
		} catch (Exception e) {
		}
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer arg0) {
				if (!Activity_Main.SongSinglePlay) {
					arg0.reset();
					arg0.start();
				}
			}
		});
		try {
			songBack = Constants.ReadSong(-1);
			songNext = Constants.ReadSong(1);
		} catch (Exception e) {
		}
		mediaPlayer.start();
		Activity_Main.Main_seekbar.setMax(mediaPlayer.getDuration());
		handler.removeCallbacks(runThread);
		handler.post(runThread);
	}
}
