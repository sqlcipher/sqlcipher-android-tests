package net.zetetic.tests.support;

import android.util.Pair;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.SQLCipherTest;
import java.io.File;
import java.util.List;
import java.util.UUID;

public class GetAttachedDatabasesTest extends SupportTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    UUID id = UUID.randomUUID();
    File databasePath = ZeteticApplication.getInstance().getDatabasePath(id.toString());
    List<Pair<String, String>> attached = database.getAttachedDbs();
    boolean initialAttach = attached.size() == 1 && attached.get(0).first.equals("main");
    database.execSQL("ATTACH database ? as foo;",
        new Object[]{databasePath.getAbsolutePath()});
    attached = database.getAttachedDbs();
    return initialAttach && attached.size() == 2;
  }

  @Override
  public String getName() {
    return "Get Attached Databases Test";
  }
}
