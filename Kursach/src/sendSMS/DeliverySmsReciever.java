package sendSMS;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DeliverySmsReciever extends BroadcastReceiver {

	@Override
	 public void onReceive(Context context, Intent intent) {
		  switch(getResultCode()) {
		  case Activity.RESULT_OK:
		   Log.i("mes", "SMS delivered");
		   break;
		  case Activity.RESULT_CANCELED:
		   Log.i("mes", "SMS not delivered");
		   break;
		  }
	}
}
