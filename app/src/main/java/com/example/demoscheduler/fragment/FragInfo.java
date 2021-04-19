package com.example.demoscheduler.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoscheduler.R;
import com.example.demoscheduler.adapter.AdapterJobDetails;
import com.example.demoscheduler.customInterface.InterfaceInfo;
import com.example.demoscheduler.dbHelper.DatabaseHelper;
import com.example.demoscheduler.setGet.SetGetJobDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import soup.neumorphism.NeumorphButton;

public class FragInfo extends Fragment implements View.OnClickListener,InterfaceInfo {

    private EditText mEt_subject, mEt_desc, mEt_search;
    private NeumorphButton mBtn_add;
    private DatabaseHelper databaseHelper;
    private ArrayList<SetGetJobDetails> arrayList_jobDetails;
    private AdapterJobDetails adapterJobDetails;
    private RecyclerView mRv_details;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_info, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setViewReferences();
        bindEventHandlers();
        setAdapterPrelim();

        databaseHelper=new DatabaseHelper(getActivity());
        arrayList_jobDetails.addAll(databaseHelper.getContent());

        mEt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterJobDetails.getFilter().filter(s.toString().toUpperCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onItemClicked(int id, String subject, String desc, String dateTime, boolean isEdit, boolean isDel) {
        View view_edit=LayoutInflater.from(getActivity()).inflate(R.layout.view_job_details_frag_edit_desc,null);
        EditText mEt_updateContent;
        Button mBtn_update;
        mEt_updateContent = view_edit.findViewById(R.id.et_view_job_details_frag_edit_update_text);
        mBtn_update = view_edit.findViewById(R.id.btn_view_job_details_frag_edit_update);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view_edit);
        builder.setCancelable(true);
        AlertDialog alertDialog_edit=builder.create();
        alertDialog_edit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mEt_updateContent.setText(desc);

        if(isEdit){
            alertDialog_edit.show();
            mBtn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mEt_updateContent.getText().toString().length()>0){
                        if(databaseHelper.editContent(id,mEt_updateContent.getText().toString())){
                            alertDialog_edit.dismiss();
                            Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                            reloadFrag();
                        }
                    }else{
                        mEt_updateContent.setError("Enter Update");
                        mEt_updateContent.requestFocus();
                    }
                }
            });
        }


        if(isDel){
            AlertDialog.Builder alertDialogBuilder_del = new AlertDialog.Builder(getActivity())
                    .setCancelable(false)
                    .setTitle("Alert")
                    .setMessage("Want to Delete ?")
                    .setIcon(R.drawable.ic_alert)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(databaseHelper.removeContent(id)){
                                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                reloadFrag();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog alertDialog_del=alertDialogBuilder_del.create();
            alertDialog_del.getWindow().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
            alertDialog_del.getWindow().setDimAmount(0.5f);
            alertDialog_del.show();
        }

    }

    private void setViewReferences() {
        mEt_subject = getActivity().findViewById(R.id.et_frag_info_subject);
        mEt_desc = getActivity().findViewById(R.id.et_frag_info_desc);
        mEt_search = getActivity().findViewById(R.id.et_frag_info_search_desc);
        mBtn_add = getActivity().findViewById(R.id.btn_frag_info_add_desc);
        mRv_details = getActivity().findViewById(R.id.rv_frag_info_details);
    }

    private void bindEventHandlers() {
        mBtn_add.setOnClickListener(this);
    }

    private void setAdapterPrelim(){
        arrayList_jobDetails=new ArrayList<>();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        mRv_details.setLayoutManager(linearLayoutManager);
        adapterJobDetails=new AdapterJobDetails(getActivity(),arrayList_jobDetails,this::onItemClicked);
        mRv_details.setAdapter(adapterJobDetails);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_add){
            if(mEt_subject.getText().toString().length()>0){
                if(mEt_desc.getText().toString().length()>0){
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    if(databaseHelper.addContent(mEt_subject.getText().toString(), mEt_desc.getText().toString(),formatter.format(date))){
                        reloadFrag();
                    }
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
        arrayList_jobDetails.clear();
        arrayList_jobDetails.addAll(databaseHelper.getContent());
        adapterJobDetails.notifyDataSetChanged();
        mEt_subject.setText("");mEt_desc.setText("");
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

}
