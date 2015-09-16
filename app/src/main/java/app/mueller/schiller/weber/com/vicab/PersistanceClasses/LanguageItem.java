package app.mueller.schiller.weber.com.vicab.PersistanceClasses;


public class LanguageItem {


    private String couple, sourceLanguage, targetLanguage;


    public LanguageItem (String couple, String sourceLanguage, String targetLanguage) {
        this.couple = couple;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
    }

    public String getCouple() {
        return couple;
    }

    public void setCouple(String couple) {
        this.couple = couple;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

}
