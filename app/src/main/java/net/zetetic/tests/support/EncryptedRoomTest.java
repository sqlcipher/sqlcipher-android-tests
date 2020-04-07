package net.zetetic.tests.support;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.TestResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EncryptedRoomTest implements ISupportTest {
  private static final String DB_NAME = "room.db";

  private final Activity activity;

  @Entity()
  public static class ParentEntity {
    @PrimaryKey(autoGenerate = true) long id;
    int intValue;
    float floatValue;
    double doubleValue;
    char charValue;
    boolean boolValue;
  }

  @Entity(foreignKeys = @ForeignKey(entity = ParentEntity.class,
      parentColumns = "id", childColumns = "parentId", onDelete = ForeignKey.CASCADE),
      indices = @Index("parentId"))
  public static class ChildEntity {
    @PrimaryKey @NonNull String uuid;
    String stringValue;
    long parentId;
  }

  @Dao
  public static abstract class TestDao {
    @Query("SELECT * FROM ChildEntity WHERE parentId = :parentId or :parentId = -1")
    abstract List<ChildEntity> getAllChildrenForParent(long parentId);

    @Query("SELECT * FROM ChildEntity")
    abstract List<ChildEntity> getAllChildren();

    @Insert
    abstract void insert(List<ChildEntity> entities);

    @Insert
    abstract long insert(ParentEntity entity);
  }

  @Database(entities = {ParentEntity.class, ChildEntity.class}, version = 1)
  public static abstract class TestDatabase extends RoomDatabase {
    abstract TestDao testDao();
  }

  public EncryptedRoomTest(Activity activity) {
    this.activity = activity;
  }

  @SuppressLint("DefaultLocale")
  public TestResult run() {
    File dbFile = ZeteticApplication.getInstance().getDatabasePath(DB_NAME);

    if (dbFile.exists()){
      dbFile.delete();
    }

    final TestResult result = new TestResult(getName(), false);
    final byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
    final SupportFactory factory = new SupportFactory(passphrase, ZeteticApplication.getInstance().wrapHook(null));
    final TestDatabase room = Room.databaseBuilder(activity, TestDatabase.class, DB_NAME)
        .openHelperFactory(factory)
        .build();
    ParentEntity parent = new ParentEntity();

    parent.boolValue = true;
    parent.charValue = 'x';
    parent.intValue = 1337;
    parent.doubleValue = 3.14159;
    parent.floatValue = 2.71828f;
    parent.id = room.testDao().insert(parent);

    result.setResult(true);

    ChildEntity firstChild = new ChildEntity();

    firstChild.uuid = UUID.randomUUID().toString();
    firstChild.stringValue = "Um, hi!";
    firstChild.parentId = parent.id;

    ChildEntity secondChild = new ChildEntity();

    secondChild.uuid = UUID.randomUUID().toString();
    secondChild.stringValue = "And now for something completely different";
    secondChild.parentId = parent.id;

    List<ChildEntity> children = new ArrayList<>();

    children.add(firstChild);
    children.add(secondChild);

    room.testDao().insert(children);

    List<ChildEntity> allChildren = room.testDao().getAllChildren();
    List<ChildEntity> allChildrenNegativeOne = room.testDao().getAllChildrenForParent( -1);
    List<ChildEntity> other = room.testDao().getAllChildrenForParent( parent.id);

//    SQLiteDatabase db = (SQLiteDatabase)room.getOpenHelper().getReadableDatabase();
//    String[] projection = new String[] { "*" };
//    String[] args = new String[] { "-1" };
//    Cursor c = db.query("ChildEntity", projection, "parentId = ?1 or ?1 = -1", args, null, null, null);
//
//    //the query should have returned both the list with same sizes here
//    if (allChildren.size() != c.getCount()) {
//      result.setResult(false);
//      result.setMessage(String.format("expected all children, found %d from entity and %d from main query", c.getCount(), allChildren.size()));
//      return result;
//    }

    //the query should have returned both the list with same sizes here
    if (allChildren.size() != allChildrenNegativeOne.size()) {
      result.setResult(false);
      result.setMessage(String.format("expected all children, found %d from entity and %d from main query", allChildrenNegativeOne.size(), allChildren.size()));
      return result;
    }

    return result;
  }

  @Override
  public String getName() {
    return "Encrypted Room Test";
  }
}
