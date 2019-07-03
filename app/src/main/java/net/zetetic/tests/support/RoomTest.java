package net.zetetic.tests.support;

import android.annotation.SuppressLint;
import android.app.Activity;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.TestResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Update;

public class RoomTest implements ISupportTest {
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
        @Query("SELECT * FROM ParentEntity")
        abstract List<ParentEntity> getAllParents();

        @Query("SELECT * FROM ParentEntity WHERE id = :id")
        abstract ParentEntity findParentById(long id);

        @Query("SELECT * FROM ChildEntity WHERE parentId = :parentId")
        abstract List<ChildEntity> getAllChildrenForParent(long parentId);

        @Query("SELECT * FROM ChildEntity WHERE uuid = :uuid")
        abstract ChildEntity findChildById(String uuid);

        @Insert
        abstract void insert(List<ChildEntity> entities);

        @Insert
        abstract long insert(ParentEntity entity);

        @Update
        abstract void update(ChildEntity entity);

        @Update
        abstract void update(ParentEntity entity);

        @Delete
        abstract void delete(ChildEntity entity);

        @Delete
        abstract void delete(ParentEntity entity);
    }

    @Database(entities = {ParentEntity.class, ChildEntity.class}, version = 1)
    public static abstract class TestDatabase extends RoomDatabase {
        abstract TestDao testDao();
    }

    public RoomTest(Activity activity) {
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
        final SupportFactory factory = new SupportFactory(passphrase);
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

        List<ParentEntity> parents = room.testDao().getAllParents();

        if (parents.size() != 1) {
            result.setResult(false);
            result.setMessage(String.format("expected 1 parent, found %d", parents.size()));
            return result;
        }

        ParentEntity retrievedParent = parents.get(0);

        if (!assertParent(retrievedParent, parent)) {
            result.setResult(false);
            result.setMessage("retrieved parent from getAllParents() did not match original");
            return result;
        }

        retrievedParent = room.testDao().findParentById(parent.id);

        if (retrievedParent == null) {
            result.setResult(false);
            result.setMessage(String.format("retrieved parent from findParentById() was null for %d", parent.id));
            return result;
        }

        if (!assertParent(retrievedParent, parent)) {
            result.setResult(false);
            result.setMessage("retrieved parent from findParentById() did not match original");
            return result;
        }

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

        List<ChildEntity> retrievedChildren = room.testDao().getAllChildrenForParent(parent.id);

        if (retrievedChildren.size() != 2) {
            result.setResult(false);
            result.setMessage(String.format("expected 2 children, found %d", retrievedChildren.size()));
            return result;
        }

        ChildEntity retrievedChild = retrievedChildren.get(0);

        if (!assertChild(retrievedChild, firstChild) && !assertChild(retrievedChild, secondChild)) {
            result.setResult(false);
            result.setMessage("retrieved child from getAllChildrenForParent() did not match either original");
            return result;
        }

        retrievedChild = retrievedChildren.get(1);

        if (!assertChild(retrievedChild, firstChild) && !assertChild(retrievedChild, secondChild)) {
            result.setResult(false);
            result.setMessage("retrieved child from getAllChildrenForParent() did not match either original");
            return result;
        }

        parent.boolValue = false;
        parent.charValue = 'z';
        parent.intValue = 65536;
        parent.doubleValue = 2.02214076e23; // # of atoms in a mole
        parent.floatValue = 299729.458f;    // speed of light in km/s

        room.testDao().update(parent);

        retrievedParent = room.testDao().findParentById(parent.id);

        if (!assertParent(retrievedParent, parent)) {
            result.setResult(false);
            result.setMessage("retrieved parent from post-update findParentById() did not match original");
            return result;
        }

        secondChild.stringValue = "urgent pegboard untied kimono boiler downstairs";

        room.testDao().update(secondChild);

        retrievedChild = room.testDao().findChildById(secondChild.uuid);

        if (!assertChild(retrievedChild, secondChild)) {
            result.setResult(false);
            result.setMessage("retrieved child from post-updated findChildById() did not match original");
            return result;
        }

        room.testDao().delete(firstChild);

        retrievedChildren = room.testDao().getAllChildrenForParent(parent.id);

        if (retrievedChildren.size() != 1) {
            result.setResult(false);
            result.setMessage(String.format("expected 1 children post-delete, found %d", retrievedChildren.size()));
            return result;
        }

        retrievedChild = retrievedChildren.get(0);

        if (!assertChild(retrievedChild, secondChild)) {
            result.setResult(false);
            result.setMessage("retrieved child from post-delete getAllChildrenForParent() did not match original");
            return result;
        }

        room.testDao().delete(parent);

        if (room.testDao().getAllParents().size() != 0) {
            result.setResult(false);
            result.setMessage("after delete of parent, parent count != 0");
            return result;
        }

        if (room.testDao().findParentById(parent.id) != null) {
            result.setResult(false);
            result.setMessage("after delete of parent, was able to retrieve parent");
            return result;
        }

        if (room.testDao().getAllChildrenForParent(parent.id).size() != 0) {
            result.setResult(false);
            result.setMessage("after delete of parent, child count != 0");
            return result;
        }

        if (room.testDao().findChildById(firstChild.uuid) != null) {
            result.setResult(false);
            result.setMessage("after delete of parent, was able to retrieve first child");
            return result;
        }

        if (room.testDao().findChildById(secondChild.uuid) != null) {
            result.setResult(false);
            result.setMessage("after delete of parent, was able to retrieve second child");
            return result;
        }

        return result;
    }

    @Override
    public String getName() {
        return "Room Test";
    }

    private boolean assertParent(ParentEntity one, ParentEntity two) {
        return one.id == two.id &&
          one.boolValue == two.boolValue &&
          one.charValue == two.charValue &&
          one.intValue == two.intValue &&
          Math.abs(one.floatValue - two.floatValue) < 0.01 &&
          Math.abs(one.doubleValue - two.doubleValue) < 0.01;
    }

    private boolean assertChild(ChildEntity one, ChildEntity two) {
        return one.uuid.equals(two.uuid) &&
          one.stringValue.equals(two.stringValue) &&
          one.parentId == two.parentId;
    }
}
