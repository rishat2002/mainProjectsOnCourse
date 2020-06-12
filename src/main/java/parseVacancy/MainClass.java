package parseVacancy;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class MainClass implements Grabb {
    private File fileWithProperties;
    private List<Parse> listWithSites;
    private Store store;

    public MainClass(File fileWithProperties, List<Parse> listWithSites, Store store) {
        this.fileWithProperties = fileWithProperties;
        this.listWithSites = listWithSites;
        this.store = store;
    }

    @Override
    public void init() throws IOException {
        Properties p = new Properties();
        p.load(new FileInputStream(fileWithProperties));
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            Hellojob.setGrab(this);
            JobDetail job = newJob(Hellojob.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInHours(Integer.parseInt(p.getProperty("time")))
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    public void job() throws SQLException, ParseException {
        for (Parse p : listWithSites) {
            try {
                for (Vacancy v : p.list()) {
                    try {
                        store.save(v);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        store.close();
    }

    public static class Hellojob implements Job {
        private static Grabb grab;

        public static void setGrab(Grabb grab) {
            Hellojob.grab = grab;
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            try {
                grab.job();
            } catch (SQLException | ParseException e) {

            }
        }
    }

    public static void main(String[] args) throws SQLException, IOException, SchedulerException, ParseException {
        File configFile = new File("src/main/resources/vacancy.properties");
        Parse parseSqlRu = new ParseSqlRu(args[0]);
        List<Parse> listWithParse = new ArrayList<>();
        listWithParse.add(parseSqlRu);
        Store store = new StoreforSqlru(configFile);
        MainClass m = new MainClass(configFile, listWithParse, store);
        m.init();
    }
}
