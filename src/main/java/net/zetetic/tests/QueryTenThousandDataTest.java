package net.zetetic.tests;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * QueryTenThousandDataTest
 *
 * It collected two questions:
 * 1.In the Samsung Galaxy Note 5, Initially insert 14000 rows of data, the first time to query all the data, it is normal.
 * When I execute more than one query all the data, the exception occurs.
 * The exception follows:
 * Fatal signal 11 (SIGSEGV), code 1, fault addr 0x70bef5dc in tid 26296 (AsyncTask #2)
 *
 * 2.Regardless of any device, it consume 4 to 8 seconds when query 14000 rows of data.
 * When I do not use SQLCipher to query 14000 rows of data, it takes only 200 to 400 milliseconds
 *
 * Would you be able to tell me how to solve the second problem which query slowly? Sincere thanks.
 * @author force
 *
 */
public class QueryTenThousandDataTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {

    createTable(database, true);

    insertData(database);

    Cursor cursor = database.rawQuery("SELECT * FROM UserInfo", new String[]{});
    log("Query ten thousand row data cursor move");
    long beforeCursorMove = System.nanoTime();
    List<UserEntity> userList = new ArrayList<UserEntity>();
    try {
      for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
        UserEntity userEntity = readEntity(cursor, 0);
        userList.add(userEntity);
      }
      long afterCursorMove = System.nanoTime();
      log(String.format("Query thousand row data userList size:%d", userList.size()));
      log(String.format("Complete cursor operation time:%d ms",
          toMilliseconds(beforeCursorMove, afterCursorMove)));
    } finally {
      cursor.close();
    }

    return true;
  }

  @Override
  public String getName() {
    return "Query fourteen thousand rows Test";
  }

  private long toMilliseconds(long before, long after){
    return (after - before)/1000000L;
  }


  public void createTable(SQLiteDatabase db, boolean ifNotExists) {
    String constraint = ifNotExists? "IF NOT EXISTS ": "";
    db.execSQL("CREATE TABLE " + constraint + "'UserInfo' (" +
        "'aaaa' INTEGER PRIMARY KEY AUTOINCREMENT ," +
        "'bbbb' INTEGER NOT NULL UNIQUE," +
        "'cccc' INTEGER NOT NULL ," +
        "'dddd' TEXT NOT NULL ," +
        "'eeee' TEXT NOT NULL ," +
        "'ffff' TEXT NOT NULL ," +
        "'gggg' TEXT NOT NULL ," +
        "'hhhh' TEXT NOT NULL ," +
        "'iiii' TEXT NOT NULL ," +
        "'jjjj' INTEGER NOT NULL ," +
        "'kkkk' INTEGER NOT NULL ," +
        "'llll' INTEGER NOT NULL ," +
        "'mmmm' INTEGER NOT NULL ," +
        "'nnnn' TEXT NOT NULL );");
  }

  public void insertData(SQLiteDatabase database) {

    try {
      String sql = "INSERT INTO UserInfo ( aaaa, bbbb, cccc, dddd, eeee, ffff, gggg, hhhh, iiii, jjjj, kkkk, llll, mmmm, nnnn ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      database.beginTransaction();
      SQLiteStatement stmt = database.compileStatement(sql);

      for (int index = 0; index < 14000; index++) {
        stmt.bindLong(1, Long.valueOf(index + ""));
        stmt.bindDouble(2, index);
        stmt.bindDouble(3, 1);
        stmt.bindString(4, "tom");
        stmt.bindString(5, "lucy");
        stmt.bindString(6, "force");
        stmt.bindString(7, "http");
        stmt.bindString(8, "0201111");
        stmt.bindString(9, "email");
        stmt.bindDouble(10, 222);
        stmt.bindDouble(11, 1);
        stmt.bindDouble(12, 333);
        stmt.bindDouble(13, 444);
        stmt.bindString(14, "short");

        stmt.execute();
        stmt.clearBindings();
      }
    } finally {
      database.setTransactionSuccessful();
      database.endTransaction();
    }
  }

  public UserEntity readEntity(Cursor cursor, int offset) {
    UserEntity entity = new UserEntity( //
        cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0),
        cursor.getInt(offset + 1),
        cursor.getInt(offset + 2),
        cursor.getString(offset + 3),
        cursor.getString(offset + 4),
        cursor.getString(offset + 5),
        cursor.getString(offset + 6),
        cursor.getString(offset + 7),
        cursor.getString(offset + 8),
        cursor.getInt(offset + 9),
        cursor.getInt(offset + 10),
        cursor.getInt(offset + 11),
        cursor.getInt(offset + 12),
        cursor.getString(offset + 13)
    );
    return entity;
  }

  public class UserEntity {

    private int gender;
    /** Not-null value. */
    private String pinyinName;
    /** Not-null value. */
    private String realName;
    /** Not-null value. */
    private String phone;
    /** Not-null value. */
    private String shortPhone;
    /** Not-null value. */

    protected Long id;
    protected int peerId;
    /** Not-null value.
     * userEntity --> nickName
     * groupEntity --> groupName
     * */
    protected String mainName;
    /** Not-null value.*/
    protected String avatar;
    protected int created;
    protected int updated;
    private int searchType;

    private int tempGroupRoleType;
    private String email;
    private int departmentId;
    private int status;
    private int msgUpdataTime;

    public UserEntity(Long id, int peerId, int gender, String mainName,
                      String pinyinName, String realName, String avatar,
                      String phone, String email, int departmentId,
                      int status, int created, int updated, String shortPhone) {
      this.id = id;
      this.peerId = peerId;
      this.gender = gender;
      this.mainName = mainName;
      this.pinyinName = pinyinName;
      this.realName = realName;
      this.avatar = avatar;
      this.phone = phone;
      this.email = email;
      this.departmentId = departmentId;
      this.status = status;
      this.created = created;
      this.updated = updated;
      this.shortPhone = shortPhone;
    }

    public int getGender() {
      return gender;
    }

    public void setGender(int gender) {
      this.gender = gender;
    }

    public String getPinyinName() {
      return pinyinName;
    }

    public void setPinyinName(String pinyinName) {
      this.pinyinName = pinyinName;
    }

    public String getRealName() {
      return realName;
    }

    public void setRealName(String realName) {
      this.realName = realName;
    }

    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
    }

    public String getShortPhone() {
      return shortPhone;
    }

    public void setShortPhone(String shortPhone) {
      this.shortPhone = shortPhone;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public int getPeerId() {
      return peerId;
    }

    public void setPeerId(int peerId) {
      this.peerId = peerId;
    }

    public String getMainName() {
      return mainName;
    }

    public void setMainName(String mainName) {
      this.mainName = mainName;
    }

    public String getAvatar() {
      return avatar;
    }

    public void setAvatar(String avatar) {
      this.avatar = avatar;
    }

    public int getCreated() {
      return created;
    }

    public void setCreated(int created) {
      this.created = created;
    }

    public int getUpdated() {
      return updated;
    }

    public void setUpdated(int updated) {
      this.updated = updated;
    }

    public int getSearchType() {
      return searchType;
    }

    public void setSearchType(int searchType) {
      this.searchType = searchType;
    }

    public int getTempGroupRoleType() {
      return tempGroupRoleType;
    }

    public void setTempGroupRoleType(int tempGroupRoleType) {
      this.tempGroupRoleType = tempGroupRoleType;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public int getDepartmentId() {
      return departmentId;
    }

    public void setDepartmentId(int departmentId) {
      this.departmentId = departmentId;
    }

    public int getStatus() {
      return status;
    }

    public void setStatus(int status) {
      this.status = status;
    }

    public int getMsgUpdataTime() {
      return msgUpdataTime;
    }

    public void setMsgUpdataTime(int msgUpdataTime) {
      this.msgUpdataTime = msgUpdataTime;
    }
  }

}