package com.sccreporte.reporte;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by simpson on 2/2/2018.
 */
// El adaptador que relaciona los datos con la vista mediante ReportViewHolder
public class LastReportsAdapter extends RecyclerView.Adapter<LastReportsAdapter.ReportViewHolder> {

    private static final String TAG = LastReportsAdapter.class.getSimpleName();

    final private ListItemClickListener mOnClickListener;

    private int mNumberItems;

    //Lista de datos a mostrar
    List<LastReportsActivity.Report> mData;

    // La interfaz que recibe el mensaje onClick
    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }

    public LastReportsAdapter(List<LastReportsActivity.Report> data, ListItemClickListener listener) {
        mNumberItems = data.size();
        mData = data;
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
        LastReportsActivity.Report current = mData.get(position);
        holder.bind(position, current.data);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
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
        void bind(int listIndex, String data){
            listItemReportView.setText(String.valueOf(listIndex) + "-" + data);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
