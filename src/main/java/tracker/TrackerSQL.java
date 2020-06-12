package tracker;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TrackerSQL implements ITracker, AutoCloseable {
    private String tableName;
    private static Connection connection;

    public TrackerSQL(String tableName, Connection connection) {
        this.tableName = tableName;
        this.connection = connection;
    }

    public  Connection init() {
        try (FileReader in = new FileReader(new File("src/main/resources/app.properties")) ) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return connection;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    @Override
    public Item add(Item item) {
        try {
            PreparedStatement pr = this.connection.prepareStatement("insert into " + tableName + " (Id_item , Name) values (?,?)");
            pr.setString(1, item.getId());
            pr.setString(2, item.getName());
            int rs = pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public boolean replace(String id, Item item) {
        boolean succesOperation = false;
        try {
            PreparedStatement pr = null;
            int rs = 0;
            pr = connection.prepareStatement(String.format("UPDATE %s SET name=?,Id_item=?  WHERE id_item=?;",
                    tableName));
            pr.setString(1, item.getName());
            pr.setString(2, item.getId());
            pr.setString(3, id);
            rs = pr.executeUpdate();
            if (rs > 0) {
                succesOperation = true;
            }
            ;
            pr.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                this.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return succesOperation;
    }

    @Override
    public boolean delete(String id) {
        boolean succesOperation = false;
        try {
            PreparedStatement pr = connection.prepareStatement(String.format("DELETE FROM %s WHERE id_item=?;", tableName));
            pr.setString(1, id);
            int rs = pr.executeUpdate();
            if (rs > 0) {
                succesOperation = true;
            }
            ;
            pr.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                this.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return succesOperation;
    }

    @Override
    public List<Item> findall() {
        List<Item> itemlist = new ArrayList<Item>();
        try {
            PreparedStatement pr = connection.prepareStatement(String.format("select * from %s", tableName));
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                Item item = new Item(rs.getString("name"));
                item.setId(rs.getString("id_item"));
                itemlist.add(item);
            }
            rs.close();
            pr.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemlist;
    }

    @Override
    public List<Item> findByName(String key) {
        ArrayList<Item> list = new ArrayList<>();
        try {
            this.init();
            PreparedStatement pr = connection.prepareStatement(String.format("select * from %s where name=?", tableName));
            pr.setString(1, key);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                Item item = new Item(rs.getString("name"));
                item.setId(rs.getString("id_item"));
                list.add(item);
            }
            rs.close();
            pr.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Item findById(String id) {
        Item item = null;
        try {
            this.init();
            PreparedStatement pr = connection.prepareStatement(String.format("select * from %s where id_item=?", tableName));
            pr.setString(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next() == false) {
                return null;
            }
            item = new Item(rs.getString("name"));
            item.setId(rs.getString("id_item"));
            rs.close();
            pr.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    public static void main(String[] args) {

    }
}
