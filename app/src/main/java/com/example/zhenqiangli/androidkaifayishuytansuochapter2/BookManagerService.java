package com.example.zhenqiangli.androidkaifayishuytansuochapter2;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import com.example.zhenqiangli.androidkaifayishuytansuochapter2.aidl.Book;
import com.example.zhenqiangli.androidkaifayishuytansuochapter2.aidl.IBookManager;
import com.example.zhenqiangli.androidkaifayishuytansuochapter2.aidl.IOnNewBookArrivedListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zhenqiangli on 8/23/17.
 */

public class BookManagerService extends Service {
  private static final String TAG = "BookManagerService";
  private AtomicBoolean isServiceDestroyed = new AtomicBoolean(false);
  private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<>();
  private CopyOnWriteArrayList<IOnNewBookArrivedListener> listenerList = new CopyOnWriteArrayList<>();
  private Binder binder = new IBookManager.Stub() {
    @Override
    public List<Book> getBookList() throws RemoteException {
      return bookList;
    }

    @Override
    public void addBook(Book book) throws RemoteException {
      bookList.add(book);
    }

    @Override
    public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
      if (!listenerList.contains(listener)) {
        listenerList.add(listener);
      } else {
        Log.d(TAG, "registerListener: already exsit");
      }
      Log.d(TAG, "registerListener: size " + listenerList.size());
    }

    @Override
    public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
      if (listenerList.contains(listener)) {
        listenerList.remove(listener);
      } else {
        Log.d(TAG, "unregisterListener: not found, can not unregister.");
      }
      Log.d(TAG, "unregisterListener: current size " + listenerList.size());
    }
  };

  @Override
  public void onCreate() {
    super.onCreate();
    bookList.add(new Book(1, "Android"));
    bookList.add(new Book(2, "iOS"));
    new Thread(new ServiceWorker()).start();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  private void onNewBookArrived(Book book) throws RemoteException {
    bookList.add(book);
    Log.d(TAG, "onNewBookArrived: notify listeners: " + listenerList.size());
    for (IOnNewBookArrivedListener listener : listenerList) {
      listener.onNewBookArrived(book);
    }
  }

  private class ServiceWorker implements Runnable {

    @Override
    public void run() {
      while (!isServiceDestroyed.get()) {
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        int bookId = bookList.size() + 1;
        Book newBook = new Book(bookId, "new book#" + bookId);
        try {
          onNewBookArrived(newBook);
        } catch (RemoteException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
