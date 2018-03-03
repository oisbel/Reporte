package com.sccreporte.reporte;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sccreporte.reporte.data.Report;
import com.sccreporte.reporte.utilities.DataUtils;

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
        // view: report_list_item
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

        // Todos los campos del layout de los items de los reportes
        TextView ayunosTB;
        TextView avivamientosTB;
        TextView fechaTB;
        TextView lugarTB;
        TextView visitasTB;

        public ReportViewHolder(View itemView){
            super(itemView);
            ayunosTB = (TextView)itemView.findViewById(R.id.ayunosTextView);
            avivamientosTB = (TextView)itemView.findViewById(R.id.avivamientosTextView);
            fechaTB = (TextView)itemView.findViewById(R.id.fechaTextView);
            lugarTB = (TextView)itemView.findViewById(R.id.lugarTextView);
            visitasTB = (TextView)itemView.findViewById(R.id.visitasTextView);

            itemView.setOnClickListener(this);
        }

        /**
         * Establece el valor que va a tener el item
         * @param listIndex posicion del item en el recycler view
         * @param report valor que se va a hacer bind
         */
        void bind(int listIndex, Report report){
            if(report == null) return;
            // Estableciendo los valores al list item del reporte
            ayunosTB.setText("Ayunos: " + String.valueOf(report.ayunos));
            avivamientosTB.setText("Avivamientos: " + String.valueOf(report.avivamientos));

            fechaTB.setText(String.valueOf(report.day) + "/" +
                    DataUtils.Months[report.month - 1] + "/" +
                    String.valueOf(report.year));
            //lugarTB.setText(String.valueOf(report.lugar));
            visitasTB.setText("Visitas: " + String.valueOf(report.visitas));
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
