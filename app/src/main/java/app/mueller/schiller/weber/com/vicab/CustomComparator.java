package app.mueller.schiller.weber.com.vicab;

import java.util.Comparator;

import app.mueller.schiller.weber.com.vicab.PersistanceClasses.VocItem;

public class CustomComparator implements Comparator<VocItem> {
    @Override
    public int compare(VocItem o1, VocItem o2) {
        return Integer.compare(o1.getAsked(), (o2.getAsked()));
    }
}
