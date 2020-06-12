package tracker;

import java.sql.SQLException;
import java.util.List;

public interface ITracker {
    Item add(Item item) throws Exception;
    boolean replace(String id, Item item) throws SQLException;
    boolean delete(String id) throws SQLException;
    List<Item> findall();
    List<Item> findByName(String key);
    Item findById(String id);
}
