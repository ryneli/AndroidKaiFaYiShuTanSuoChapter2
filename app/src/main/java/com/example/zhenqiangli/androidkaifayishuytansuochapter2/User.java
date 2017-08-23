package com.example.zhenqiangli.androidkaifayishuytansuochapter2;

import java.io.Serializable;

/**
 * Created by zhenqiangli on 8/22/17.
 */

public class User implements Serializable {
  private static final long serialVersionUID = 193774972895L;
  public int userId;
  public String userName;
  public boolean isMale;

  public User(int userId, String userName, boolean isMale) {
    this.userId = userId;
    this.userName = userName;
    this.isMale = isMale;
  }
}
