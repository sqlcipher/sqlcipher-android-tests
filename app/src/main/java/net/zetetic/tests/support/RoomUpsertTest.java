package net.zetetic.tests.support;

import android.app.Activity;
import android.util.Log;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Upsert;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.TestResult;

import java.io.File;

class RoomUpsertTest implements ISupportTest {

   private Activity activity;
   private static final String DB_NAME = "room.db";
   private String TAG = getClass().getSimpleName();

   public RoomUpsertTest(Activity activity){

      this.activity = activity;
   }

   @Override
   public String getName() {
      return "Room Upsert Test";
   }

   @Override
   public TestResult run() {
      TestResult result = new TestResult(getName(), false);
      File databasePath = this.activity.getDatabasePath(DB_NAME);
      if(databasePath.exists()){
         databasePath.delete();
      }
      try {
         final byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
         final SupportFactory factory = new SupportFactory(passphrase);
         final UserDatabase room = Room.databaseBuilder(activity, UserDatabase.class, DB_NAME)
           .openHelperFactory(factory)
           .build();
         User user = new User();
         user.name = "Foo Bar";
         user.age = 41;
         user.id = room.userDao().upsert(user);
         user.age = 42;
         room.userDao().upsert(user);
         User[] searchUser = room.userDao().findById(user.id);
         result.setResult(searchUser[0].age == 42);
      }
      catch (android.database.sqlite.SQLiteConstraintException ex){
         Log.i(TAG, "Error in Room Upsert test", ex);
      }
      return result;
   }

   @Entity
   public static class User {
      @PrimaryKey(autoGenerate = true) long id;
      String name;
      int age;
   }

   @Dao
   public static abstract class UserDao {
      @Upsert
      abstract long upsert(User user);
      @Query("SELECT * FROM user WHERE id=:id")
      abstract User[] findById(long id);
   }

   @Database(entities = {User.class}, version = 1)
   public static abstract class UserDatabase extends RoomDatabase {
      abstract UserDao userDao();
   }
}