To run, clone this repo and make sure you have the Android Studio installed:
- SQLCipher for Android defines [compatibility support](https://github.com/sqlcipher/android-database-sqlcipher#compatibility), simply run on either an emulator or device.
- More information can be found at [SQLCipher for Android](https://zetetic.net/sqlcipher/sqlcipher-for-android/).

### Creating A New Test

1. Open this repository within Android Studio
2. Add a new class within `net.zetetic.tests` package that extends `SQLCipherTest`:

```
package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;

public class DemoTest extends SQLCipherTest {

  @Override
  public boolean execute(SQLiteDatabase database) {
    try {
      // Add your scenario here
      return true;
    }catch (Exception e){
      return false;
    }
  }

  @Override
  public String getName() {
    return "Demo Test";
  }
}
```

3. Add `DemoTest` to the [`TestSuiteRunner`](https://github.com/sqlcipher/sqlcipher-android-tests/blob/master/src/main/java/net/zetetic/tests/TestSuiteRunner.java):

```
tests.add(new DemoTest());
```
4. Build and run the application on an Android device/emulator
