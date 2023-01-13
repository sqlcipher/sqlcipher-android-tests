package net.zetetic;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.io.*;

public class ZeteticApplication extends Application {

  public static String DATABASE_NAME = "test.db";
  public static String DATABASE_PASSWORD = "test";
  private static ZeteticApplication instance;
  private Activity activity;
  public static final String TAG = "Zetetic";
  public static final String ONE_X_DATABASE = "1x.db";
  public static final String ONE_X_USER_VERSION_DATABASE = "1x-user-version.db";
  public static final String UNENCRYPTED_DATABASE = "unencrypted.db";
  public static final String licenseCode = "";

  public ZeteticApplication() {
    instance = this;
  }

  public static ZeteticApplication getInstance() {
    return instance;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    NativeInitializer.initialize();
  }

  public void setCurrentActivity(Activity activity) {
    this.activity = activity;
  }

  public Activity getCurrentActivity() {
    return activity;
  }

  public void prepareDatabaseEnvironment() {
    Log.i(TAG, "Entered prepareDatabaseEnvironment");
    Log.i(TAG, "Before getDatabasePath");
    File databaseFile = getDatabasePath(DATABASE_NAME);
    Log.i(TAG, "Before mkdirs on parent of database path");
    databaseFile.getParentFile().mkdirs();

    if (databaseFile.exists()) {
      Log.i(TAG, "Before delete of database file");
      databaseFile.delete();
    }
//        databaseFile.mkdirs();
//        databaseFile.delete();
  }

  public boolean includesLicenseCode(){
    return licenseCode != null && licenseCode.length() > 0;
  }

  public boolean supportsMinLibraryVersionRequired(String requiredVersionString) {
    try {
      DefaultArtifactVersion requiredVersion = new DefaultArtifactVersion(requiredVersionString);
      DefaultArtifactVersion actualVersion = new DefaultArtifactVersion(SQLiteDatabase.SQLCIPHER_ANDROID_VERSION);
      return actualVersion.compareTo(requiredVersion) >= 0;
    } catch (Exception ex){
      return false;
    }
  }

  public SQLiteDatabase createDatabase(File databaseFile) {
    return createDatabase(databaseFile, null);
  }

  public SQLiteDatabase createDatabase(File databaseFile, SQLiteDatabaseHook hook) {
    Log.i(TAG, "Entered ZeteticApplication::createDatabase");
    Log.i(TAG, "Before SQLiteDatabase.openOrCreateDatabase");
    return SQLiteDatabase.openOrCreateDatabase(databaseFile.getPath(), DATABASE_PASSWORD, null, wrapHook(hook));
  }

  public void extractAssetToDatabaseDirectory(String fileName) throws IOException {

    int length;
    InputStream sourceDatabase = ZeteticApplication.getInstance().getAssets().open(fileName);
    File destinationPath = ZeteticApplication.getInstance().getDatabasePath(fileName);
    OutputStream destination = new FileOutputStream(destinationPath);

    byte[] buffer = new byte[4096];
    while ((length = sourceDatabase.read(buffer)) > 0) {
      destination.write(buffer, 0, length);
    }
    sourceDatabase.close();
    destination.flush();
    destination.close();
  }

  public void delete1xDatabase() {

    File databaseFile = getInstance().getDatabasePath(ONE_X_DATABASE);
    databaseFile.delete();
  }

  public void deleteDatabaseFileAndSiblings(String databaseName) {

    File databaseFile = ZeteticApplication.getInstance().getDatabasePath(databaseName);
    File databasesDirectory = new File(databaseFile.getParent());
    for (File file : databasesDirectory.listFiles()) {
      file.delete();
    }
  }

  public SQLiteDatabaseHook wrapHook(final SQLiteDatabaseHook hook) {
    if (hook == null)
    {
      return keyHook;
    }
    return new SQLiteDatabaseHook() {
      @Override
      public void preKey(SQLiteDatabase database) {
        keyHook.preKey(database);
        hook.preKey(database);
      }

      @Override
      public void postKey(SQLiteDatabase database) {
        keyHook.postKey(database);
        hook.preKey(database);
      }
    };
  }

  SQLiteDatabaseHook keyHook = new SQLiteDatabaseHook() {
    @Override
    public void preKey(SQLiteDatabase database) {
      database.rawExecSQL(String.format("PRAGMA cipher_license = '%s';", licenseCode));
    }
    public void postKey(SQLiteDatabase database) {
    }
  };

}
