package getSMS;




import activities.MainActivity;
import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SmsSrevice extends Service {
	SaveSmsAsyncTask saveSmsAsyncTask;
	String from;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("mes", "onStartCommand");
		saveSmsAsyncTask = new SaveSmsAsyncTask(this,this);
		from=intent.getStringExtra("from");
		saveSmsAsyncTask.execute(intent.getStringExtra("from"),	intent.getStringExtra("message"));
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}
	
	
public void showNotification() {
	
	 Notification notif = new Notification(R.drawable.ic_lock_lock, "smsGetting", 
		      System.currentTimeMillis());
	 	Intent intent = new Intent(this, MainActivity.class);
	    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);  
	    notif.setLatestEventInfo(this, "Locator", from, pIntent);
	    notif.flags |= Notification.FLAG_AUTO_CANCEL;
	    NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	    nManager.notify(1, notif);	
		stopService();
	}
	
	public void stopService() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				stopSelf();
			}
		}).start();
	}
}
