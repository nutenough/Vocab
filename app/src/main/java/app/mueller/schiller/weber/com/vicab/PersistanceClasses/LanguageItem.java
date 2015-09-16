package app.mueller.schiller.weber.com.vicab.PersistanceClasses;


public class LanguageItem {

    private String sourceLanguage, targetLanguage;
    private int _id = 0;

    public LanguageItem (String sourceLanguage, String targetLanguage) {
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public int get_id() {
        return _id;
    }

    public void set_id (int dbID) {
        _id = dbID;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

}
