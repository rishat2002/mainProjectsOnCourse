package parseVacancy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;

public class StoreforSqlru implements Store {
    private Connection conn;
    private File properties;

    public StoreforSqlru(File properties) throws SQLException {
        this.properties = properties;
        this.init();
    }

    public boolean init() throws SQLException {
        try (FileInputStream fis = new FileInputStream(properties)) {
            Properties config = new Properties();
            config.load(fis);
            Class.forName(config.getProperty("driver-class-name"));
            this.conn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        System.out.println(conn.isClosed());
        return this.conn != null;
    }

    @Override
    public void save(Vacancy post) throws SQLException {
        PreparedStatement pr = this.conn.prepareStatement
                ("insert into vacancy (name , link ,text,date ) values (?,?,?,?)");
        pr.setString(1, post.getName());
        pr.setString(2, post.getLink());
        pr.setString(3, post.getText());
        pr.setString(4, post.getDate());
        System.out.println(pr.execute());
    }

    @Override
    public List<Vacancy> get(Predicate<Vacancy> filter) throws SQLException {
        List<Vacancy> vacancyList = new ArrayList<Vacancy>();
        PreparedStatement pr = this.conn.prepareStatement
                ("select * from vacancy");
        ResultSet t = pr.executeQuery();
        while (t.next()) {
            Vacancy vacancyBeforeFilter = new Vacancy(t.getString("name"), t.getString("link"), t.getString("text"));
            if (filter.test(vacancyBeforeFilter)) {
                vacancyList.add(vacancyBeforeFilter);
            }
        }
        return vacancyList;
    }

    public void close() throws SQLException {
        conn.close();
    }

    public static void main(String[] args) throws SQLException, IOException {
    }

}
