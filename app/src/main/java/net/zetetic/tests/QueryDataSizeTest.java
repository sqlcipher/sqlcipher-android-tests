package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteQueryStats;

public class QueryDataSizeTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    database.execSQL("CREATE TABLE t1(a,b);");
    database.execSQL("INSERT INTO t1(a,b) VALUES(?, ?);",
        new Object[]{generateRandomByteArray(256), generateRandomByteArray(256)});
    database.execSQL("INSERT INTO t1(a,b) VALUES(?, ?);",
        new Object[]{generateRandomByteArray(1024), generateRandomByteArray(64)});
    SQLiteQueryStats result = database.getQueryStats("SELECT * FROM t1;", new Object[]{});
    return result.getTotalQueryResultSize() > 0 && result.getLargestIndividualRowSize() > 0;
  }

  @Override
  public String getName() {
    return "Query Data Size Test";
  }
}
