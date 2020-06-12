import org.junit.Assert;
import org.junit.Test;
import tracker.ConnectionRollback;
import tracker.Item;
import tracker.TrackerSQL;


import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class TrackerSQLTest {
    public Connection init() {
        try (FileReader in = new FileReader(new File("src/main/resources/app.properties")) ) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            return DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")

            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

   @Test
   public void createItem() throws Exception {
    try (TrackerSQL tracker = new TrackerSQL("item", ConnectionRollback.create(this.init()))) {
           Item i=new Item("tail");
         i.setId("hor");
          tracker.add(i);
         Assert.assertEquals(tracker.findall().size(),1);
          Assert.assertEquals(tracker.findall().get(0).getName(),"tail");
}
}
}
