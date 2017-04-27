package manan.mynotes;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.Display;
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
public class listupdate extends Fragment {

    EditText note;
    EditText content;
    Button submit;
    Button delete;
    Activity a;
    View v;
    String argument1;
    String argument2;
    String index;
    String nt;
    String cnt;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.listupdate,container,false);
        note=(EditText)v.findViewById(R.id.heading1);
        content=(EditText)v.findViewById(R.id.content1);
        submit=(Button)v.findViewById(R.id.submit1);
        delete=(Button)v.findViewById(R.id.delete);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        double  width1 = width/2;
        double width2=width/6;
        content.setHeight((int)width1);
        note.setHeight((int)width2);
        Bundle b= getArguments();
            argument1= getArguments().getString("note");
            argument2=getArguments().getString("content");
            index=getArguments().getString("index");

            note.setText(argument1);
            content.setText(argument2);
            submit.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {

                            nt = note.getText().toString();
                            cnt = content.getText().toString();
                            if(note.getText().length()==0||content.getText().length()==0)
                                {
                                    Toast.makeText(a,"Please fill all the details!!",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    notelistener.update(nt, cnt, index);
                                }
                            }



                    }
            );

            delete.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            nt = note.getText().toString();
                            cnt = content.getText().toString();
                            notelistener.delete(nt,cnt,index);
                        }
                    }
            );

        return v;
    }



    public interface listener1 {
        public void update(String s,String t,String position);
        public void delete(String s,String t,String postion);

    }

    listener1 notelistener;
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if (context instanceof Activity) {
            a = (Activity) context;
            try {
                notelistener = (listener1) a;
            } catch (ClassCastException e) {
                throw new ClassCastException(a.toString() + " must implement listener1");
            }
        }
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}

