package totoo.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import totoo.touhouplayer.R;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Constants {

	public static ArrayList<MusicBean> musicBeans_all = new ArrayList<MusicBean>();
	public static ArrayList<MusicBean> playingBeans = new ArrayList<MusicBean>();
	public static List<Map<String, String>> musicSystem = new ArrayList<Map<String, String>>();
	public static List<Map<String, String>> SDSystem = new ArrayList<Map<String, String>>();
	public static final int MusicR_code = 3838;
	public static int Play_id = 0;
	public static int List_id = 0;
	public static final int BTN_Go = 9;
	public static final int BTN_Finsh = 502;
	public static final int changedSong = 793;
	public static final int changedProgress = 794;
	public static final int BTN_next = 798;
	public static final int BTN_back = 835;
	public static final String TAG_changedProgress = "changedProgress";
	public static final String TAG_changedLimit = "changedLimit";
	public static final String TAG_rspID = "rsp_id";
	public static final String PlayerServiceName = "totoo.Music";
	public static final String BROADCAST_NAME = "totoo.music.broadcast";
	public static final String kill_BROADCAST_NAME = "player_killSelf";
	public static final String TAG_ChangeSongFlag = "flag";
	public static BitmapDrawable BG_img;
	public static boolean playFlag = true;
	public static boolean selfKillFlag = false;
	public static int current_play_progress;
	public static int current_play_max;
	public static File Music_file = new File(Environment
			.getExternalStorageDirectory().getPath() + "/Music");
	public static File Root_Music_like = new File(Environment
			.getExternalStorageDirectory().getPath());
	public static String SD_path = Environment.getExternalStorageDirectory()
			.getPath();

	static Thread callThreadTwo() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				Constants.SDSystem = new MusicManager().getSystemSD();
				Activity_Main.SDSystem_Handler.sendEmptyMessage(0);
				Activity_PlayingList.SDSystem_Handler.sendEmptyMessage(0);
			}
		});
		thread.start();
		// ToastShow("少女休惬中");
		return thread;

	}

	static void createListSongs(ListView view) {
		SimpleAdapter simpleAdapter = new SimpleAdapter(Activity_Main.mContext,
				Constants.musicSystem, R.layout.main_list_li_songs, new String[] {
						"id", "name", "statu" }, new int[] { R.id.id,
						R.id.name, R.id.statu });
		view.setAdapter(simpleAdapter);
		view.setOnItemLongClickListener(new OnItemLongClickListener() {
			String readSong;

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				Constants.Play_id = position;
				readSong = musicBeans_all.get(position).getMusicPath();
				new Thread(new Runnable() {

					void MoveToLikes(String path) {
						Message message;
						boolean flag2 = false;
						if (!Music_file.exists())
							Music_file.mkdir();
						File To_file = new File(Root_Music_like, GetFile(path));
						if (!To_file.exists()) {
							File From_file2 = new File(path);
							String stringName = GetName(path);
							try {
								InputStream inputStream = new FileInputStream(
										From_file2);
								//
								message = new Message();
								message.obj = "开始收藏：" + stringName;
								Activity_Main.toast_Handler
										.sendMessage(message);
								//
								OutputStream outputStream = new FileOutputStream(
										To_file);
								int len = 0;
								byte[] buffer = new byte[8096];
								while ((len = inputStream.read(buffer)) > 0) {
									outputStream.write(buffer, 0, len);
								}
								outputStream.close();
								inputStream.close();
								From_file2.delete();

								message = new Message();
								message.obj = stringName + "，已移至sd根目录";
								Activity_Main.toast_Handler
										.sendMessage(message);
								flag2 = true;
							} catch (Exception e) {
								// To_file.delete();
								message = new Message();
								message.obj = "不能再次收藏," + stringName;
								Activity_Main.toast_Handler
										.sendMessage(message);
							}
							if (flag2) {//
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("name", stringName);
								MusicBean bean = new MusicBean();
								bean.setMusicName(stringName);
								bean.setMusicPath(To_file.getPath());//
								Constants.playingBeans.add(bean);
								Constants.SDSystem.add(map);
								try {
									if (-1 != Constants.Play_id) {
										String path2 = To_file.getPath();
										MusicBean musicBean = Constants.musicBeans_all
												.get(Constants.Play_id);
										musicBean.setMusicPath(path2);
										Map<String, String> map2 = Constants.SDSystem
												.get(Constants.Play_id);
										map2.put("path", path2);
									}
								} catch (Exception e) {
								}
								Activity_Main.SDSystem_Handler
										.sendEmptyMessage(0);
								if (null != Activity_PlayingList.listViewLikes)
									Activity_PlayingList.SDSystem_Handler
											.sendEmptyMessage(0);
							}
							Activity_Splash.thread2 = callThreadTwo();
						}
					}

					public void run() {
						MoveToLikes(readSong);
					}
				}).start();

				return false;
			}

		});
		view.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MusicBean value = Constants.musicBeans_all.get(arg2);
				HashMap<String, String> map = new HashMap<String, String>();
				String path = value.getMusicPath();
				String name = value.getMusicName();
				map.put("name", GetName(name));
				map.put("path", path);
				map.put("id", value.getMusicSize() / 1024 / 1024 + "M");

				AddLikes(map);
			}

		});
	}

	static void createListLikes(ListView view) {
		if (null != Constants.SDSystem) {
			SimpleAdapter simpleAdapter2 = new SimpleAdapter(
					Activity_Main.mContext, Constants.SDSystem,
					R.layout.main_list_li_likes, new String[] { "id", "name" },
					new int[] { R.id.id, R.id.name });
			view.setAdapter(simpleAdapter2);

			view.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Activity_Main.ChangeMusicPlay(Constants.List_id = arg2,
							Constants.playingBeans);
				}
			});
			view.setOnItemLongClickListener(new OnItemLongClickListener() {
				String path;

				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					Constants.List_id = position;
					try {
						path = playingBeans.get(position).getMusicPath();
						new Thread(new Runnable() {
							public void run() {
								MoveToMusics(path);
							}

							void MoveToMusics(String path) {
								boolean flag3 = false;
								Message message;
								if (!Music_file.exists())
									Music_file.mkdir();
								File file = new File(Music_file, GetFile(path));
								if (!file.exists()) {
									File file2 = new File(path);
									String stringName = GetName(path);
									try {
										InputStream inputStream = new FileInputStream(
												file2);
										message = new Message();
										message.obj = stringName + "：开始回收";
										Activity_Main.toast_Handler
												.sendMessage(message);
										OutputStream outputStream = new FileOutputStream(
												file);
										int len = 0;
										byte[] buffer = new byte[8096];
										while ((len = inputStream.read(buffer)) > 0) {
											outputStream.write(buffer, 0, len);
										}
										outputStream.close();
										inputStream.close();
										file2.delete();
										if (-1 != Constants.List_id) {
											String path2 = file.getPath();
											MusicBean musicBean = Constants.musicBeans_all
													.get(Constants.List_id);
											musicBean.setMusicPath(path2);
											Map<String, String> map2 = Constants.SDSystem
													.get(Constants.List_id);
											map2.put("path", path2);
										}
										message = new Message();
										message.obj = stringName + "：已回收至Music";
										Activity_Main.toast_Handler
												.sendMessage(message);
										flag3 = true;
									} catch (Exception e) {
										// file.delete();
										message = new Message();
										message.obj = stringName + "：回收撤销";
										Activity_Main.toast_Handler
												.sendMessage(message);

									}
								}
								if (flag3)
									if (-1 != Constants.List_id) {
										Constants.SDSystem.remove(Constants.List_id);
										Constants.playingBeans.remove(Constants.List_id);
										callThreadTwo();
										Activity_Main.SDSystem_Handler
												.sendEmptyMessage(0);
										if (null != Activity_PlayingList.listViewLikes)
											Activity_PlayingList.SDSystem_Handler
													.sendEmptyMessage(0);
									}
							}
						}).start();

					} catch (Exception e) {
					}
					return false;
				}
			});
		}
	}

	static void AddLikes(HashMap<String, String> map) {
		String string_name = map.get("name");
		String string_path = map.get("path");
		// int id;
		boolean change_play_flag = true;
		for (Map<String, String> map2 : Constants.SDSystem)
			if (map2.get("name").equals(string_name)) {
				// id = map.get("id");
				change_play_flag = false;
				break;
			}

		Service_Play.Action_ChangeSong(new Intent().putExtra("name",
				string_path));
		if (change_play_flag) {
			map.put("name", string_name);
			Constants.SDSystem.add(map);
			MusicBean bean = new MusicBean();
			bean.setMusicName(string_name);
			bean.setMusicPath(string_path);
			// bean.setMusicSize(id);
			Constants.playingBeans.add(bean);
			Activity_Main.SDSystem_Handler.sendEmptyMessage(0);
			Activity_PlayingList.SDSystem_Handler.sendEmptyMessage(0);
		}
	}

	static String GetName(String name) {
		return name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("."));
	}

	static String GetFile(String name) {
		return name.substring(name.lastIndexOf("/") + 1, name.length());
	}

	public static String ReadSong(int flag) {
		try {
			String string = Constants.musicBeans_all.get(
					Constants.Play_id = Constants.Play_id + flag).getMusicPath();
			return string;
		} catch (Exception e) {
			return null;
		}
	}

	public static String AD_view(Context context) {
		Builder builder = new Builder(context);
		// builder.setView(view)
		builder.show();
		return null;
	}

	public static void killTask(Context context) {
		if (Constants.selfKillFlag) {
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_UNMOUNTED, Uri
					.parse(Environment.getExternalStorageDirectory().getPath())));
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
					.parse(Environment.getExternalStorageDirectory().getPath())));
			context.sendBroadcast(new Intent(Constants.kill_BROADCAST_NAME));
			context.stopService(new Intent(Constants.PlayerServiceName));
			Service_Play.startMusicSer = null;
		}
	}
}
