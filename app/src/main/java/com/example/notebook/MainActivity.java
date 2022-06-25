package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{
    private static final String TAG = "LOG";
    EditText edtName, edtPassword;
    Button btnGoToActivity, btnShowList,btn_search;
    NoteDatabase myDatabase;
    ImageView timePicker, datePicker;
    TextView txtTime, txtDate;
    CheckBox checkBox;
    AlarmManager alarmManager;
    ArrayList<PendingIntent> pendingIntents;
    private int requestCode = 1001;
    private TextView txtToolbarTitle;
    private boolean isPm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();


        getCurrentDateAndTime();

        txtToolbarTitle.setText("NoteBook");
        myDatabase = new NoteDatabase(getApplicationContext());

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(MainActivity.this, MainActivity.this, 12, 25, true);
                dialog.show();
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtDate.setText(year + "/" + month + "/" + dayOfMonth);
                    }
                }, 2022, 8, 11);

                dialog.show();
            }
        });


        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        btnGoToActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBox.isChecked()) {
                    if (edtName.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty() || txtDate.getText().toString().isEmpty() ||
                            txtTime.getText().toString().isEmpty()) {

                        Toast.makeText(MainActivity.this, "وارد کردن اطلاعات ضروری است", Toast.LENGTH_SHORT).show();
                    } else {
                        long id = myDatabase.addInfo(edtName.getText().toString(), edtPassword.getText().toString(), checkBox.isChecked() ? 1 : 0, txtDate.getText().toString(), txtTime.getText().toString());
                        setAlarm();
                        Toast.makeText(MainActivity.this, id + "", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (edtName.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {

                        Toast.makeText(MainActivity.this, "وارد کردن اطلاعات ضروری است", Toast.LENGTH_SHORT).show();
                    } else {
                        long id = myDatabase.addInfo(edtName.getText().toString(), edtPassword.getText().toString(), checkBox.isChecked() ? 1 : 0, txtDate.getText().toString(), txtTime.getText().toString());
                        Toast.makeText(MainActivity.this, id + "", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

    }


    private void setupViews() {
        txtToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        pendingIntents = new ArrayList<>();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        checkBox = (CheckBox) findViewById(R.id.cb_main_checkBox);
        btnShowList = (Button) findViewById(R.id.btn_main_showList);
        txtTime = (TextView) findViewById(R.id.txt_main_time);
        txtDate = (TextView) findViewById(R.id.txt_main_date);
        timePicker = (ImageView) findViewById(R.id.img_main_timePicker);
        datePicker = (ImageView) findViewById(R.id.img_main_datePicker);
        edtName = (EditText) findViewById(R.id.edt_main_username);
        edtPassword = (EditText) findViewById(R.id.edt_main_password);
        btnGoToActivity = (Button) findViewById(R.id.btn_main_goToActivity);
        btn_search = (Button) findViewById(R.id.btn_main_search);

    }


    public void getCurrentDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        int am = calendar.get(Calendar.AM_PM);

        Log.i("LOG", "getCurrentDateAndTime: " + year + "/" + month + "/" + day + "time is:" + hour + ":" + min + ":" + sec + " am or pm" + am);
    }

    public String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
        String datetime = dateformat.format(c.getTime());
        return datetime;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        txtTime.setText(hourOfDay + ":" + minute);
    }


    public void setAlarm() {
        Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();

        String date = txtDate.getText().toString();
        String time = txtTime.getText().toString();

        String[] times = time.split(":");
        String[] dates = date.split("/");

        if (Integer.parseInt(times[0]) > 12) {
            isPm = true;
        }

        calendar.set(Calendar.YEAR, Integer.parseInt(dates[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dates[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[2]));
        calendar.set(Calendar.HOUR, isPm ? Integer.parseInt(times[0]) - 12 : Integer.parseInt(times[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.AM_PM, isPm ? 1 : 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


        pendingIntents.add(pendingIntent);
        isPm = false;
        requestCode++;
    }

}