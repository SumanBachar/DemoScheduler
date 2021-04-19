package com.example.demoscheduler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoscheduler.R;
import com.example.demoscheduler.customInterface.InterfaceInfo;
import com.example.demoscheduler.setGet.SetGetJobDetails;

import java.io.FilterReader;
import java.util.ArrayList;

public class AdapterJobDetails extends RecyclerView.Adapter<AdapterJobDetails.JobDetailsViewHolder> implements Filterable {

    private Context context;
    private ArrayList<SetGetJobDetails> arrayList_details;
    private ArrayList<SetGetJobDetails> arrayList_bean;
    private InterfaceInfo interfaceInfo;

    private ValueFilter valueFilter;

    public AdapterJobDetails(Context context, ArrayList<SetGetJobDetails> arrayList_details, InterfaceInfo interfaceInfo) {
        this.context = context;
        this.arrayList_details = arrayList_details;
        this.arrayList_bean = arrayList_details;
        this.interfaceInfo = interfaceInfo;
    }

    @NonNull
    @Override
    public JobDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_info_details, parent, false);
        return new JobDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobDetailsViewHolder holder, int position) {
        holder.mTv_subject.setText(arrayList_details.get(position).getSubject());
        holder.mTv_desc.setText(arrayList_details.get(position).getDesc());
        holder.mTv_dateTime.setText(arrayList_details.get(position).getEntDateTime());

        holder.mIv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceInfo.onItemClicked(arrayList_details.get(position).getId(),arrayList_details.get(position).getSubject(),
                        arrayList_details.get(position).getDesc(),arrayList_details.get(position).getEntDateTime(),true,false);
            }
        });
        holder.mIv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceInfo.onItemClicked(arrayList_details.get(position).getId(),arrayList_details.get(position).getSubject(),
                        arrayList_details.get(position).getDesc(),arrayList_details.get(position).getEntDateTime(),false,true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList_details.size();
    }

    public class JobDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView mTv_subject, mTv_desc, mTv_dateTime;
        ImageView mIv_edit,mIv_del;
        public JobDetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_subject = itemView.findViewById(R.id.tv_row_info_details_subject);
            mTv_desc = itemView.findViewById(R.id.tv_row_info_details_com_desc);
            mTv_dateTime = itemView.findViewById(R.id.tv_row_info_details_date_time);
            mIv_edit = itemView.findViewById(R.id.iv_row_info_details_edit);
            mIv_del = itemView.findViewById(R.id.iv_row_info_details_delete);
        }
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                ArrayList<SetGetJobDetails> arrayList_filter=new ArrayList<>();
                for(int i=0;i<arrayList_details.size();i++){
                    if(arrayList_details.get(i).getSubject().equals(constraint.toString())){
                        SetGetJobDetails setGetJobDetails=new SetGetJobDetails();
                        setGetJobDetails.setId(arrayList_details.get(i).getId());
                        setGetJobDetails.setSubject(arrayList_details.get(i).getSubject());
                        setGetJobDetails.setDesc(arrayList_details.get(i).getDesc());
                        setGetJobDetails.setEntDateTime(arrayList_details.get(i).getEntDateTime());
                        arrayList_filter.add(setGetJobDetails);
                    }
                }
                filterResults.count=arrayList_filter.size();
                filterResults.values=arrayList_filter;
            }else{
                filterResults.count=arrayList_bean.size();
                filterResults.values=arrayList_bean;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList_details=(ArrayList<SetGetJobDetails>) results.values;
            notifyDataSetChanged();
        }
    }

}
