package net.zetetic.tests.support;

import android.app.Activity;
import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.RawQuery;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SimpleSQLiteQuery;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.TestResult;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public class EncryptBytesTest implements ISupportTest {

  Activity activity;

  public EncryptBytesTest(Activity activity) {
    this.activity = activity;
  }

  @Dao
  public interface BlobDao {
    @RawQuery
    byte[] blobRawGet(SimpleSQLiteQuery query);
  }

  public static class Encryptor {
    BlobDao dao;
    byte[] encryptionKey;

    public Encryptor(BlobDao dao, byte[] encryptionKey) {
      this.dao = dao;
      this.encryptionKey = encryptionKey;
    }

    public byte[] encryptBytes(byte[] material) {
      return dao.blobRawGet(new SimpleSQLiteQuery("select sqlcipher_vle_encrypt(?, ?)",
          new Object[]{material, encryptionKey}));
    }

    public byte[] decryptBytes(byte[] material) {
      return dao.blobRawGet(new SimpleSQLiteQuery("select sqlcipher_vle_decrypt(?, ?)",
          new Object[]{material, encryptionKey}));
    }
  }

  public static class Encryptor2 {
    RoomDatabase roomDatabase;
    byte[] encryptionKey;

    public Encryptor2(RoomDatabase roomDatabase, byte[] encryptionKey) {
      this.roomDatabase = roomDatabase;
      this.encryptionKey = encryptionKey;
    }

    public byte[] encryptBytes(byte[] material) {
      byte[] result = null;
      Cursor cursor = roomDatabase.getOpenHelper().getReadableDatabase().query("select sqlcipher_vle_encrypt(?, ?)",
          new Object[]{material, encryptionKey});
      if(cursor != null && cursor.moveToNext()){
        result = cursor.getBlob(0);
        cursor.close();
      }
      return result;
    }

    public byte[] decryptBytes(byte[] material) {
      byte[] result = null;
      Cursor cursor = roomDatabase.getOpenHelper().getReadableDatabase().query("select sqlcipher_vle_decrypt(?, ?)",
          new Object[]{material, encryptionKey});
      if(cursor != null && cursor.moveToNext()){
        result = cursor.getBlob(0);
        cursor.close();
      }
      return result;
    }
  }

  @Entity
  public static class BlobEntity {
    @PrimaryKey(autoGenerate = true)
    long id;
  }

  @Database(entities = {BlobEntity.class}, version = 1)
  public static abstract class BlobDatabase extends RoomDatabase {
    abstract BlobDao blobDao();
  }


  public TestResult run() {
    TestResult result = new TestResult(getName(), false);
    byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
    SupportFactory factory = new SupportFactory(passphrase, ZeteticApplication.getInstance().wrapHook(null));
    BlobDatabase room = Room.databaseBuilder(activity, BlobDatabase.class, "test.db")
        .openHelperFactory(factory)
        .build();

    byte[] key = generateRandomBytes(64);
    //Encryptor encryptor = new Encryptor(room.blobDao(), key);
    Encryptor2 encryptor = new Encryptor2(room, key);

    byte[] source = "hi".getBytes();
    byte[] encrypted = encryptor.encryptBytes(source);
    byte[] decrypted = encryptor.decryptBytes(encrypted);
    result.setResult(Arrays.equals(source, decrypted));
    return result;
  }

  private byte[] generateRandomBytes(int length) {
    Random random = new SecureRandom();
    byte[] value = new byte[length];
    random.nextBytes(value);
    return value;
  }

  public String getName() {
    return "Encrypt Bytes Test";
  }
}