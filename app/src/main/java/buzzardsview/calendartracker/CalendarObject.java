package buzzardsview.calendartracker;

public class CalendarObject {
    private String name;
    private long id;
    private int color;

    CalendarObject (String name, Long id, int color) {
        this.name = name;
        this.id = id;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
