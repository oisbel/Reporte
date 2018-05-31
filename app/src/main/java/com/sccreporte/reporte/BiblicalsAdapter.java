package com.sccreporte.reporte;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sccreporte.reporte.data.Biblical;
import com.sccreporte.reporte.utilities.DataUtils;

import java.util.List;

/**
 * Created by simpson on 3/29/2018.
 */

public class BiblicalsAdapter extends RecyclerView.Adapter<BiblicalsAdapter.BiblicalViewHolder> {

    private static final String TAG = BiblicalsAdapter.class.getSimpleName();

    final private ListItemClickListener mOnClickListener;

    //Lista de datos a mostrar
    List<Biblical> mBiblicaltData;

    // La interfaz que recibe el mensaje onClick
    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }

    public BiblicalsAdapter(ListItemClickListener listener){
        // listener del layout padre
        mOnClickListener = listener;
    }

    @Override
    public BiblicalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        // el layout que representa cada item del recyclerView
        int layoutIdForListItem = R.layout.biblical_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        // view: report_list_item
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        BiblicalViewHolder viewHolder = new BiblicalViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BiblicalViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        Biblical current = mBiblicaltData.get(position);
        holder.bind(position, current);
    }

    @Override
    public int getItemCount() {
        if (mBiblicaltData == null)
            return 0;
        return  mBiblicaltData.size();
    }

    public void removeItem(int position) {
        mBiblicaltData.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Biblical item, int position) {
        mBiblicaltData.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    /**
     * This method is used to set the report data on a LastReportsAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new LastReportsAdapter to display it.
     * @param data
     */
    public void setBiblicalData(List<Biblical> data){
        mBiblicaltData = data;
        notifyDataSetChanged();
    }
    /**
     * Representa los datos para cada item en el recycler view mediante biblical_list_item.xml
     */
    class BiblicalViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        TextView nombreTB;
        TextView monthTB;
        TextView yearTB;
        TextView direccionTB;
        public RelativeLayout viewBackground, viewForeground;

        public BiblicalViewHolder(View itemView){
            super(itemView);
            nombreTB = itemView.findViewById(R.id.nombreTextView);
            monthTB = itemView.findViewById(R.id.monthTextView);
            yearTB = itemView.findViewById(R.id.yearTextView);
            direccionTB = itemView.findViewById(R.id.direccionTextView);

            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

            itemView.setOnClickListener(this);
        }

        void bind(int listIndex, Biblical biblical){
            if(biblical == null)
                return;
            // Estableciendo los valores al list item del estudio biblico
            nombreTB.setText(String.valueOf(biblical.nombre));
            monthTB.setText(DataUtils.MonthsShort[biblical.month - 1]);
            yearTB.setText(String.valueOf(biblical.year));
            direccionTB.setText(String.valueOf(biblical.direccion));
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
