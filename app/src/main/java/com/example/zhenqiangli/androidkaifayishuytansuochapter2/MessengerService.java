package com.example.zhenqiangli.androidkaifayishuytansuochapter2;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by zhenqiangli on 8/23/17.
 */

public class MessengerService extends Service {
  private static final String TAG = "MessengerService";
  private static class MessengerHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
      Log.d(TAG, "handleMessage: ");
      switch (msg.what) {
        case MyConstants.MSG_FROM_CLIENT:
          Log.i(TAG, "receive msg from client " + msg.getData().getString("msg"));
          Messenger client = msg.replyTo;
          Message replyMsg = Message.obtain(null, MyConstants.MSG_FROM_CLIENT);
          Bundle bundle = new Bundle();
          bundle.putString("reply", "Gotcha!");
          replyMsg.setData(bundle);
          try {
            client.send(replyMsg);
          } catch (RemoteException e) {
            e.printStackTrace();
          }
          break;
        default:
          super.handleMessage(msg);
      }
    }
  }

  private final Messenger messenger = new Messenger(new MessengerHandler());

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    Log.d(TAG, "onBind: ");
    return messenger.getBinder();
  }
}
