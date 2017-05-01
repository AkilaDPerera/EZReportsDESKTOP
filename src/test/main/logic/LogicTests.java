package test.main.logic;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ProjectFileTest.class, SheetsTest.class, SQLiteControllerTest.class, StudentTest.class })
public class LogicTests {

}
