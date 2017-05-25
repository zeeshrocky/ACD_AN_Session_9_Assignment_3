package com.example.zeeshan.customdialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button buttonOk, buttonCancel;
    EditText editText;
    int timer;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final AlertDialog dialog = alertDialog.setTitle("Enter the seconds (in number) for Asynctask").create();

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item_dialog, null);


        editText = (EditText) view.findViewById(R.id.editText_id);

        buttonOk = (Button) view.findViewById(R.id.button_ok_id);
        buttonCancel = (Button) view.findViewById(R.id.button_cancel_id);


        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timer = Integer.parseInt(editText.getText().toString());
                MyTask myTask= new MyTask(MainActivity.this);
                myTask.execute();
                dialog.dismiss();

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Process Cancelled.", Toast.LENGTH_LONG).show();

                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);

        dialog.setView(view);
        dialog.show();


    }

    class MyTask extends AsyncTask<String,Integer,String> {

        Context context;
        int counter=timer;

        ProgressBar progressBar;
        AlertDialog dialog;

        MyTask(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.progress_bar, null, false);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            dialog  = new AlertDialog.Builder(context).setTitle("Asynctask running for "+String.valueOf(timer)+ " seconds" ).create();
            dialog.setView(view);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress((values[0]/timer)*100);
            buttonOk.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... params) {
            while(counter>0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(counter--);
            }
            return "Complete";
        }
        @Override
        protected void onPostExecute(String values) {
            super.onPostExecute(values);

            dialog.dismiss();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.create();
            alertDialog.setTitle("Success");
            alertDialog.setMessage(values);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            alertDialog.show();
        }
    }
}