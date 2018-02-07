package com.sccreporte.reporte;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sccreporte.reporte.data.ReportsData.Report;

import java.util.List;

/**
 * El adaptador que relaciona los datos con la vista mediante ReportViewHolder
 * Created by simpson on 2/2/2018.
 */
public class LastReportsAdapter extends RecyclerView.Adapter<LastReportsAdapter.ReportViewHolder> {

    private static final String TAG = LastReportsAdapter.class.getSimpleName();

    final private ListItemClickListener mOnClickListener;

    //Lista de datos a mostrar
    List<Report> mReportData;

    // La interfaz que recibe el mensaje onClick
    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }

    public LastReportsAdapter(ListItemClickListener listener) {
        // listener del layout padre
        mOnClickListener = listener;
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        // el layout que representa cada item del recyclerView
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
        Report current = mReportData.get(position);
        holder.bind(position, current);
    }

    @Override
    public int getItemCount() {
        if(mReportData == null) return 0;
        return mReportData.size();
    }

    /**
     * This method is used to set the report data on a LastReportsAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new LastReportsAdapter to display it.
     * @param data
     */
    public void setReportData(List<Report> data){
        mReportData = data;
        notifyDataSetChanged();
    }

    // Representa el item xml(report_list_item)
    class ReportViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{

        TextView listItemReportView;

        public ReportViewHolder(View itemView){
            super(itemView);
            listItemReportView = (TextView)itemView.findViewById(R.id.reportContentTextView);
            itemView.setOnClickListener(this);
        }
        //Establece el valor que va a tener el item
        void bind(int listIndex, Report report){
            if(report == null) return;
            listItemReportView.setText(String.valueOf(listIndex) +
                    "-" +String.valueOf(report.mensajes) + ">" + String.valueOf(report.biblias));
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
