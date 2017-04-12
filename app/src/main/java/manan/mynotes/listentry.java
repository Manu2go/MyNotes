package manan.mynotes;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.widget.Toast;

/**
 * Created by manan on 23/11/16.
 */
public class listentry extends Fragment {

    EditText note;
    EditText content;
    Button submit;
    Activity a;
    View v;
    String nt;
    String cnt;
    String index;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.listentry,container,false);
        note=(EditText)v.findViewById(R.id.heading);
        content=(EditText)v.findViewById(R.id.content);
        submit=(Button)v.findViewById(R.id.submit);



            submit.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                     /* SharedPreferences user = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                      SharedPreferences.Editor edit = user.edit();
                      edit.putString("note", note.getText().toString());
                      edit.putString("content",content.getText().toString() );
                      edit.apply();*/
                            nt=note.getText().toString();
                            cnt=content.getText().toString();
                            if(note.getText().length()==0||content.getText().length()==0)
                            {
                                Toast.makeText(a,"Please fill all the details!!",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                notelistener.note(nt,cnt);
                            }
                        }

                    }
            );


        return v;
    }



    public interface listener {
        public void note(String s,String t);

    }

    listener notelistener;
   public void onAttach(Context context)
    {
        super.onAttach(context);

        if (context instanceof Activity) {
            a = (Activity) context;
            try {
                notelistener = (listener) a;
            } catch (ClassCastException e) {
                throw new ClassCastException(a.toString() + " must implement listener");
            }
        }
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}

