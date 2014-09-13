package getCordinat;


import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

public class UpdatesService extends Service {
	SaveCordinatAsyncTask saveCordinatAsyncTask;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("mes", "onStartCommand");
		if (isGPRSConnectoins()) {
			Log.d("mes", "GPR_Work");
			saveCordinatAsyncTask = new SaveCordinatAsyncTask(this,this);
			saveCordinatAsyncTask.execute("");
		} else
			stopService();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isGPRSConnectoins() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
		NetworkInfo netInf = cm.getActiveNetworkInfo();
		if (netInf != null && netInf.isConnectedOrConnecting())
			return true;
		else
			return false;
	}

	public void stopService() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				stopSelf();
			}
		}).start();
	}

	public boolean callBack() {
		stopService();
		return true;
	}

}
