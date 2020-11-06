package net.zetetic;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

public class NativeInitializer {

  static {
    Log.i("NativeInitializer", "Before loadLibs");
    SQLiteDatabase.loadLibs(ZeteticApplication.getInstance());
    Log.i("NativeInitializer", "After loadLibs");
  }

  public static void initialize(){}

}
