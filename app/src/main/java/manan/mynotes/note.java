package manan.mynotes;

/**
 * Created by root on 25/4/17.
 */
public class note {
    private static int count;
    public int id;
    public String note;
    public String content;
    note(String d,String e){
        note=d;
        content=e;
        id=count;
        count++;
    }
    public int getcount(){
        return count;
    }
}
