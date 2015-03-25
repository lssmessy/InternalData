package com.example.priteshpatel.internaldata;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;


public class InternalData extends Activity implements View.OnClickListener {

    public static String filename="MysharedPrefs";
    EditText et;
    Button save,load;
    SharedPreferences sp;
    TextView tv;
    FileOutputStream fos;//to save the data on file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_data);
        sp=getSharedPreferences(filename,0);
        initStuffs();
    }

    public void initStuffs(){
        //Initialize all the elements
        save=(Button)findViewById(R.id.saveButton);
        load=(Button)findViewById(R.id.loadButton);
        et=(EditText)findViewById(R.id.editText1);
        tv=(TextView)findViewById(R.id.textView);
        save.setOnClickListener(this);
        load.setOnClickListener(this);
        try {
            fos=openFileOutput(filename, Context.MODE_PRIVATE);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            //If user taped on Save button
            case R.id.saveButton:
                if(isEmpty(et)==true)
                {
                    Toast.makeText(this, " Enter something ", Toast.LENGTH_SHORT).show();
                    et.requestFocus();
                    break;
                }
                else {
                    String saveData = et.getText().toString();
                    try {
                        fos=openFileOutput(filename, Context.MODE_PRIVATE);
                        fos.write(saveData.getBytes());
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    /*SharedPreferences.Editor editor = sp.edit();//Create an editer to actually save the data
                    editor.putString("EdittextData", saveData);//save data in preference with a key-value parameter
                    editor.commit();//Need to actually commit to save into file
                    Toast.makeText(this, "'"+saveData + "' has been saved", Toast.LENGTH_SHORT).show();*/
                    Toast.makeText(this, "'"+saveData + "' has been saved", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.loadButton:

                new LoadSomeStuff().execute(filename);

                break;
        }
    }
    public boolean isEmpty(EditText et)
    {
        if(et.getText().toString().trim().length()>0)
        {
            return false;
        }
        else
        {

            return true;
        }

    }

    public class LoadSomeStuff extends AsyncTask<String,Integer,String>{//First String is Input parameter type that is passed
        //Second Integer is the internal process bar
        //Third String is the return type
        ProgressDialog dialog;
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        protected void onPreExecute()
        {
            dialog=new ProgressDialog(InternalData.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setProgressPercentFormat(NumberFormat.getInstance());
            dialog.setMax(100);
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Toast.makeText(InternalData.this,"Interrupted",Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setMessage("Loading your data...");
        }
        @Override
        protected String doInBackground(String... params) {// ... means array of Type or multiple arguments for the same type
            String getData=null;
            FileInputStream fis=null;
            byte[] bytedata;
            for(int i=0; i<20; i++)
            {
                publishProgress(5);
                try {
                    Thread.sleep(88);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            dialog.dismiss();
            try {
                fis=openFileInput(filename) ;//get the actual file to read
                bytedata=new byte[fis.available()];//get the data in the form of bytes
                while(fis.read(bytedata)!= -1 )//it will set its pointer to -1 when it's done with its reading operation
                {
                    getData=new String(bytedata);//save byte data in the form of string
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    fis.close();
                    return getData;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


                /*sp=getSharedPreferences(filename,0);
                String show=sp.getString("EdittextData","Data not found");//First parameter is actual key, 2nd is a message if data not found for the key
                tv.setText(show);*/

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.incrementProgressBy(values[0]);//Start progress from the array position
        }

        @Override
        protected void onPostExecute(String s) {
            tv.setText(s);
        }
    }

}
