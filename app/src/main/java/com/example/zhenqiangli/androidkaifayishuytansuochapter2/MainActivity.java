package com.example.zhenqiangli.androidkaifayishuytansuochapter2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.example.zhenqiangli.androidkaifayishuytansuochapter2.aidl.Book;
import com.example.zhenqiangli.androidkaifayishuytansuochapter2.aidl.IBookManager;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = "MainActivity";
  private Messenger service;
  private Messenger getReplyMessenger = new Messenger(new MessengerHandler());
  private ServiceConnection connection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder binder) {
      Log.d(TAG, "onServiceConnected: ");
      service = new Messenger(binder);
      Message msg = Message.obtain(null, MyConstants.MSG_FROM_CLIENT);
      Bundle data = new Bundle();
      data.putString("msg", "Hello, this is client.");
      msg.setData(data);
      msg.replyTo = getReplyMessenger;
      try {
        service.send(msg);
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
  };

  private ServiceConnection bookManagerConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder binder) {
      IBookManager bookManager = IBookManager.Stub.asInterface(binder);
      try {
        List<Book> bookList = bookManager.getBookList();
        Log.d(TAG, "onServiceConnected: " + bookList.getClass().getCanonicalName());
        Log.d(TAG, "onServiceConnected: " + bookList.toString());
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Log.d(TAG, "onCreate: " + UserManager.userId);
    UserManager.userId = 2;
    Log.d(TAG, "onCreate: " + UserManager.userId);
    // streamUser();

    Intent intent = new Intent(this, MessengerService.class);
    bindService(intent, connection, Context.BIND_AUTO_CREATE);
    Intent intent2 = new Intent(this, BookManagerService.class);
    bindService(intent2, bookManagerConnection, Context.BIND_AUTO_CREATE);
  }

  @Override
  protected void onDestroy() {
    unbindService(bookManagerConnection);
    unbindService(connection);
    super.onDestroy();
  }

  private void streamUser() {
    User user = new User(0, "Jake", true);
    try {
      ObjectOutputStream out = new ObjectOutputStream(
          new FileOutputStream("cache.txt")
      );
      out.writeObject(user);
      out.close();

      ObjectInputStream in = new ObjectInputStream(
          new FileInputStream("cache.txt")
      );
      User newUser = (User) in.readObject();
      in.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onClickSecondActivity(View v) {
    startActivity(new Intent(this, SecondActivity.class));
  }

  public void onClickThirdActivity(View v) {
    startActivity(new Intent(this, ThirdActivity.class));
  }

  private static class MessengerHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MyConstants.MSG_FROM_CLIENT:
          Log.i(TAG, "receive msg from server: " + msg.getData().getString("reply"));
          break;
        default:
          super.handleMessage(msg);
      }
    }
  }
}
