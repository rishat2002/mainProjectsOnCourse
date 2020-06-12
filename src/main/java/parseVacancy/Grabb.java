package parseVacancy;

import org.quartz.SchedulerException;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public interface Grabb {
     void init() throws IOException, SchedulerException;
     void job() throws SQLException, ParseException;
}
