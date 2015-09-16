package app.mueller.schiller.weber.com.vicab.PersistanceClasses;


public class VocItem {

    private String sourceVocab, targetVocab, fotoLink, soundLink, wordType, hasLanguage;
    private int importance;


    public VocItem(String sourceVocab, String targetVocab, String fotoLink, String soundLink, String wordType, String hasLanguage, int importance) {
        this.sourceVocab = sourceVocab;
        this.targetVocab = targetVocab;
        this.fotoLink = fotoLink;
        this.soundLink = soundLink;

        this.wordType = wordType;
        this.importance = importance;
        this.hasLanguage = hasLanguage;
    }


    public String getSourceVocab() {
        return sourceVocab;
    }

    public void setSourceVocab(String sourceVocab) {
        this.sourceVocab = sourceVocab;
    }

    public String getTargetVocab() {
        return targetVocab;
    }

    public void setTargetVocab(String targetVocab) {
        this.targetVocab = targetVocab;
    }

    public String getFotoLink() {
        return fotoLink;
    }

    public void setFotoLink(String fotoLink) {
        this.fotoLink = fotoLink;
    }

    public String getSoundLink() {
        return soundLink;
    }

    public void setSoundLink(String soundLink) {
        this.soundLink = soundLink;
    }

    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
    }

    public String getHasLanguage() {
        return hasLanguage;
    }

    public void setHasLanguage(String hasLanguage) {
        this.hasLanguage = hasLanguage;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }




}
