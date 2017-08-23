package com.example.zhenqiangli.androidkaifayishuytansuochapter2;

import android.os.Parcel;
import android.os.Parcelable;
import com.example.zhenqiangli.androidkaifayishuytansuochapter2.aidl.Book;

/**
 * Created by zhenqiangli on 8/22/17.
 */

public class Student implements Parcelable {
  public int studentId;
  public String studentName;
  public boolean isMale;
  public Book book;

  public Student(int studentId, String studentName, boolean isMale,
      Book book) {
    this.studentId = studentId;
    this.studentName = studentName;
    this.isMale = isMale;
    this.book = book;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel out, int i) {
    out.writeInt(studentId);
    out.writeString(studentName);
    out.writeInt(isMale ? 1 : 0);
    out.writeParcelable(book, 0);
  }

  public static final Parcelable.Creator<Student> CREATOR =
      new Parcelable.Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
          return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
          return new Student[size];
        }
      };
      private Student(Parcel in) {
        studentId = in.readInt();
        studentName = in.readString();
        isMale = in.readInt() == 1;
        book = in.readParcelable(Thread.currentThread().getContextClassLoader());
      }
}
