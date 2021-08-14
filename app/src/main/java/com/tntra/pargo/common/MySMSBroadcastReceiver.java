package com.tntra.pargo.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MySMSBroadcastReceiver extends BroadcastReceiver {


    private OTPReceiveListener otpReceiveListener;

    public MySMSBroadcastReceiver(OTPReceiveListener otpReceiveListener) {
        this.otpReceiveListener = otpReceiveListener;
    }

    public MySMSBroadcastReceiver() {
    }

    /**
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        /*if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);


            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:

                    //This is the full message

                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Log.e("getmessafe", "" + message);
                    *//*<#> Your ExampleApp code is: 123ABC78
                    FA+9qCX9VSu*//*


                    Pattern pattern = Pattern.compile("(\\d{6})");
                    Matcher matcher = pattern.matcher(message);

                    String value = "";

                    if (matcher.find()) {
                        System.out.println(matcher.group(1));
                        value = matcher.group(1);

                        if (value != null && !value.equals("")) {
                            if (otpReceiveListener != null) {
                                otpReceiveListener.onOTPReceived(value);
                            }
                        }
                    }


                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)


                    break;

                case CommonStatusCodes.API_NOT_CONNECTED:


                    break;

                case CommonStatusCodes.NETWORK_ERROR:
                    Log.i("NETWORK_ERROR", "++++++");


                    break;

                case CommonStatusCodes.ERROR:
                    Log.i("ERROR", "++++++");


                    break;

            }
        }*/

        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody().replaceAll("\\D", "");

                    //message = message.substring(0, message.length()-1);
                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    Intent myIntent = new Intent("otp");
                    myIntent.putExtra("message", message);
                    myIntent.putExtra("number", senderNum);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);


                    // Show Alert

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }

    }


    /**
     *
     */

    public interface OTPReceiveListener {
        void onSuccess(Intent intent);

        void onOTPReceived(String otp);
    }

    public void onOTPReceived(OTPReceiveListener receiveListener) {
        this.otpReceiveListener = receiveListener;
    }

}