To run: clone this repo and open with a recent version of Android Studio, or [build from the command line](https://developer.android.com/studio/build/building-cmdline).

It is possible to run on an emulator or device, as documented in the [SQLCipher for Android compatibility section](https://github.com/sqlcipher/android-database-sqlcipher#compatibility).

More information can be found in [SQLCipher for Android](https://zetetic.net/sqlcipher/sqlcipher-for-android/).

### Creating A New Test

1. Open this repository within Android Studio
2. Add a new class within `net.zetetic.tests` package that extends `SQLCipherTest`:

```Java
package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;

public class AwesomeTest extends SQLCipherTest {

  @Override
  public boolean execute(SQLiteDatabase database) {
    try {
      // Add your scenario here
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public String getName() {
    return "Awesome Test";
  }
}
```

3. Add `AwesomeTest` to the [`TestSuiteRunner`](https://github.com/sqlcipher/sqlcipher-android-tests/blob/master/src/main/java/net/zetetic/tests/TestSuiteRunner.java):

```Java
tests.add(new AwesomeTest());
```
4. Build and run the application on an Android device or emulator
