package parseVacancy;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface Parse {
    List<Vacancy> list() throws IOException, ParseException;
    Vacancy detail(String link) throws IOException;
}
