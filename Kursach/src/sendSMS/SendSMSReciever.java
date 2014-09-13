package sendSMS;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.gsm.SmsManager;
import android.util.Log;
public class SendSMSReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		switch (getResultCode()) {
		case Activity.RESULT_OK:
			Log.d("mes", "SMS send");
			break;
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			Log.d("mes", "unknown problems");
			break;
		case SmsManager.RESULT_ERROR_RADIO_OFF:
			Log.d("mes", "modul is down");
			break;
		case SmsManager.RESULT_ERROR_NULL_PDU:
			Log.d("mes", "PDU error");
			break;

		}
	}
}
