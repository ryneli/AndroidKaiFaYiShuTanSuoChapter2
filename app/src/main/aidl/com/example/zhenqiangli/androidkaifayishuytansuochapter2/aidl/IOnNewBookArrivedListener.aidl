// IOnNewBookArrivedListener.aidl
package com.example.zhenqiangli.androidkaifayishuytansuochapter2.aidl;

import com.example.zhenqiangli.androidkaifayishuytansuochapter2.aidl.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
