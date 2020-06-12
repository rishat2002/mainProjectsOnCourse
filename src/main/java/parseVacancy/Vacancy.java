package parseVacancy;

import java.util.Objects;

public class Vacancy {
    private String name;
    private String link;
    private String text;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Vacancy(String name, String link, String text) {
        this.name = name;
        this.link = link;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacancy vacancy = (Vacancy) o;
        return name.equals(vacancy.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
