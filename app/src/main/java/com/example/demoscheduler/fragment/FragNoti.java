package com.example.demoscheduler.fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.demoscheduler.R;
import com.example.demoscheduler.customClass.MyAlarm;

import java.util.Calendar;

import soup.neumorphism.NeumorphButton;

public class FragNoti extends Fragment implements View.OnClickListener {

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private NeumorphButton mBtn_timePicker, mBtn_datePicker,mBtn_setAlarm;
    private int mYear=0,mMonth=0,mDay=0,mHour=0,mMin=0;
    private int selc_year=0,selc_month=0,selc_day=0,selc_hour=0,selc_min=0,selc_sec=0;
    private EditText mEt_subject,mEt_desc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_noti, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setViewReferences();
        bindEventHandlers();
        //setAdapterPrelim();

        mBtn_datePicker.setClickable(false);
    }

    private void setViewReferences() {
        mBtn_timePicker = getActivity().findViewById(R.id.btn_frag_noti_pick_time);
        mBtn_datePicker = getActivity().findViewById(R.id.btn_frag_noti_pick_date);
        mBtn_setAlarm = getActivity().findViewById(R.id.btn_frag_noti_set_alarm);
        mEt_subject = getActivity().findViewById(R.id.et_frag_noti_subject);
        mEt_desc = getActivity().findViewById(R.id.et_frag_noti_desc);
    }

    private void bindEventHandlers() {
        mBtn_timePicker.setOnClickListener(this);
        mBtn_datePicker.setOnClickListener(this);
        mBtn_setAlarm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_timePicker) {
            calendar=Calendar.getInstance();
            mHour=calendar.get(Calendar.HOUR_OF_DAY);
            mMin=calendar.get(Calendar.MINUTE);
            timePickerDialog=new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mBtn_timePicker.setText("Time : "+String.format("%02d",hourOfDay)+"  :  "+String.format("%02d",minute));
                    mBtn_datePicker.setClickable(true);
                    selc_hour=hourOfDay;
                    selc_min=minute;
                    selc_sec=0;
                }
            },mHour,mMin,false);
            timePickerDialog.show();
        }
        if(v==mBtn_datePicker){
            calendar=Calendar.getInstance();
            mYear=calendar.get(Calendar.YEAR);
            mMonth=calendar.get(Calendar.MONTH);
            mDay=calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mBtn_datePicker.setText("Date : "+String.format("%02d",dayOfMonth)+"  :  "+String.format("%02d",(month+1))+"  :  "+String.valueOf(year));
                    selc_day=dayOfMonth;
                    selc_month=month;
                    selc_year=year;
                }
            },mYear,mMonth,mDay);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        }
        if(v==mBtn_setAlarm){
            if(mEt_subject.getText().toString().length()>0){
                if(mEt_desc.getText().toString().length()>0){

                    calendar.set(Calendar.HOUR_OF_DAY, selc_hour);
                    calendar.set(Calendar.MINUTE, selc_min);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.DAY_OF_MONTH, selc_day);
                    calendar.set(Calendar.MONTH, selc_month);
                    calendar.set(Calendar.YEAR, selc_year);

                    SharedPreferences sharedPreferences_notiID=getActivity().getSharedPreferences("NOTI_ID", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences_notiID.edit();
                    if(!sharedPreferences_notiID.getBoolean("isFirst",false)){
                        editor.putInt("notiId",1);
                        editor.putBoolean("isFirst",true);
                        editor.commit();
                    }else{
                        editor.putInt("notiId",sharedPreferences_notiID.getInt("notiId",0)+1);
                        editor.putBoolean("isFirst",false);
                        editor.commit();
                    }
                    int notiID=sharedPreferences_notiID.getInt("notiId",0);

                    Intent intent=new Intent(getActivity(), MyAlarm.class);
                    intent.putExtra("notiId",notiID);
                    intent.putExtra("Subject",mEt_subject.getText().toString());
                    intent.putExtra("Desc",mEt_desc.getText().toString());

                    PendingIntent pendingIntent=PendingIntent.getBroadcast(getActivity(),notiID,intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager=(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                    }else{
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                    }


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                            .setTitle("Confirmation")
                            .setMessage("Alarm is set")
                            .setIcon(R.drawable.ic_alert)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reloadFrag();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
                    alertDialog.getWindow().setDimAmount(0.1f);
                    alertDialog.show();

                }else{
                    mEt_desc.setError("Enter Description");
                    mEt_desc.requestFocus();
                }
            }else{
                mEt_subject.setError("Enter Subject");
                mEt_subject.requestFocus();
            }
        }
    }

    private void reloadFrag(){
        mEt_subject.setText("");mEt_desc.setText("");
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

}
