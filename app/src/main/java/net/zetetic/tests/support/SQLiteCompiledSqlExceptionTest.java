package net.zetetic.tests.support;

import android.app.Activity;
import android.content.ClipData;
import android.util.Log;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SimpleSQLiteQuery;

import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.TestResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLiteCompiledSqlExceptionTest implements ISupportTest {

  private Activity activity;
  private TestDatabase testDatabase;

  public SQLiteCompiledSqlExceptionTest(Activity activity) {
    this.activity = activity;
  }

  public TestResult run() {
    boolean result = false;
    try {
      String key = "password";
      File dbFile = ZeteticApplication.getInstance().getDatabasePath("test.db");
      if (dbFile.exists()){
        dbFile.delete();
      }
      SupportFactory factory = new SupportFactory(key.getBytes(), ZeteticApplication.getInstance().wrapHook(null));
      testDatabase = Room.databaseBuilder(ZeteticApplication.getInstance(), TestDatabase.class, dbFile.getName())
          .openHelperFactory(factory) // works without a problem if you comment out this line
          .build();

      for (int i = 0; i < 100; i++) {
        testInsert();
        List<Integer> args = new ArrayList<>(Arrays.asList(new Integer[]{111, 112}));
        testDatabase.itemDao().deleteAll(args);
      }
      result = true;
    } catch (Exception ex){
      Log.e(getClass().getSimpleName(), "Exception in test", ex);
    }
    return new TestResult(getName(), result);
  }

  private void testInsert(){
    for(int i = 0; i < 100; i++){
      testDatabase.itemDao().insert(new Item(i));
    }
  }

  public String getName() {
    return "SQLiteCompiledSqlException Test";
  }

  @Dao
  public interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Item item);

    @Query("DELETE FROM item WHERE id IN (:ids)")
    abstract void deleteAll(List<Integer> ids);
  }

  @Entity
  public static class Item {
    @PrimaryKey(autoGenerate = true)
    long id;
    int value;

    public Item(int value){
      this.value = value;
    }
  }

  @Database(entities = {Item.class}, version = 1)
  public static abstract class TestDatabase extends RoomDatabase {
    abstract ItemDao itemDao();
  }
}
