package totoo.player;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class Widget_desktop extends AppWidgetProvider {

	public void onDisabled(Context context) {
		super.onDisabled(context);
		 context.stopService(new Intent(Constants.PlayerServiceName));
	}

	public void onEnabled(Context context) {
		super.onEnabled(context);
//		Service_Play.startMusicSer = context.startService(new Intent(Service_Play.PlayerServiceName));

	}

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}
