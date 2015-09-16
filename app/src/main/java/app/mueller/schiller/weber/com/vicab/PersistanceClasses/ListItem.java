package app.mueller.schiller.weber.com.vicab.PersistanceClasses;


public class ListItem {

    private String name, hasLanguage;

    private int _id = 0;

    public ListItem(String name, String hasLanguage) {
        this.name = name;
        this.hasLanguage = hasLanguage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHasLanguage(String hasLanguage) {
        this.hasLanguage = hasLanguage;
    }

    public int get_id() {
        return _id;
    }

    public void set_id (int dbID) {
        _id = dbID;
    }

    public String getName() {
        return name;
    }

    public String getHasLanguage() {
        return hasLanguage;
    }

}
