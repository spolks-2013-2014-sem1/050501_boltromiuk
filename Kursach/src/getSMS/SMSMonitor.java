package getSMS;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

public class SMSMonitor extends BroadcastReceiver {
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	SmsMessage message;
	String s;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent != null && intent.getAction() != null
				&& ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
			Object pduArray = (Object) intent.getExtras().get("pdus");
			message = SmsMessage.createFromPdu((byte[]) pduArray);
		}
		String smsFrom = message.getDisplayOriginatingAddress();
		String messageBody = message.getMessageBody();
		if ((s = messageBody.substring(0, "location:".length()))
				.equals("location:")) {
			
			Intent smsIntnt = new Intent(context, SmsSrevice.class);
			smsIntnt.putExtra("from",smsFrom );
			smsIntnt.putExtra("message",messageBody );
			PendingIntent pi = PendingIntent.getService(context, 0, intent,0);
			context.startService(smsIntnt);
			abortBroadcast();
		}
	}
}
