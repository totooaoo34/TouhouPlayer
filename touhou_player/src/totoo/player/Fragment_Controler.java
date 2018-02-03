package totoo.player;

import totoo.touhouplayer.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

public class Fragment_Controler extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list_controler, null);
		
		Activity_PlayingList.listViewLikes = (ListView) view
				.findViewById(R.id.listViewPlayingSongs);
		Activity_PlayingList.playing_progressBar = (ProgressBar) view
				.findViewById(R.id.PlayingProgress);
		Activity_PlayingList.playing_progressBar.setMax(100);
		Activity_PlayingList.playing_progressBar.setProgress(50);
		// cl(listViewSongs, simpleAdapter_Songs, R.layout.li_all_found);
		// cl(listViewLikes, simpleAdapter_Likes, R.layout.li_playing_list);
		
		try {
			Constants.createListLikes(Activity_PlayingList.listViewLikes);
		} catch (Exception e) {
		}
		return view;
	}

//	void ChangeMusicPlay(int id) {
//		try {
//			MusicBean musicBean = (MusicBean) Constants.musicBeans_all.get(id);
//			String stringname = musicBean.getMusicPath();
//			Service_Play.Action_ChangeSong(new Intent().putExtra("name",
//					stringname));
//		} catch (Exception e) {
//		}
//	}
//
//	String ReadSong(int flag) {
//		try {
//			String string = Constants.musicBeans_all.get(
//					Constants.Play_id = Constants.Play_id + flag).getMusicPath();
//			return string;
//		} catch (Exception e) {
//			return null;
//		}
//	}

}
