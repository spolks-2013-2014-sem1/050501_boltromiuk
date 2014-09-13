package getCordinat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UpdatesReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Log.d("mes", "onReceive UpdatesReceiver");
		PendingIntent pi = PendingIntent.getService(arg0, 0, new Intent(arg0, UpdatesService.class),PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarm = (AlarmManager) arg0.getSystemService(arg0.ALARM_SERVICE);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP,	System.currentTimeMillis()+2000 ,  1000*10, pi);
		Log.d("mes", "onReceive UpdatesReceiver_1");
	}

}
