package manan.mynotes;

/**
 * Created by root on 25/4/17.
 */
public class note {
    public int id;
    public String note;
    public String content;

    note(String d, String e, int id) {
        note = d;
        content = e;
        this.id = id;
    }
}