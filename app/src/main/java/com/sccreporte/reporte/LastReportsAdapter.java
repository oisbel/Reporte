package com.sccreporte.reporte;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by simpson on 2/2/2018.
 */
// El adaptador que relaciona los datos con la vista mediante ReportViewHolder
public class LastReportsAdapter extends RecyclerView.Adapter<LastReportsAdapter.ReportViewHolder> {

    private static final String TAG = LastReportsAdapter.class.getSimpleName();

    private int mNumberItems;

    public LastReportsAdapter(int numberOfItems) {
        mNumberItems = numberOfItems;
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.report_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ReportViewHolder viewHolder = new ReportViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReportViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    // Representa el item xml(report_list_item)
    class ReportViewHolder extends RecyclerView.ViewHolder{

        TextView listItemReportView;

        public ReportViewHolder(View itemView){
            super(itemView);
            listItemReportView = (TextView)itemView.findViewById(R.id.reportContentTextView);
        }
        //Establece el valor que va a tener el item
        void bind(int listIndex){
            listItemReportView.setText(String.valueOf(listIndex));
        }
    }
}
