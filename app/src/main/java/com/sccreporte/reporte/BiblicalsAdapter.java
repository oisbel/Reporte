package com.sccreporte.reporte;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sccreporte.reporte.data.Biblical;
import com.sccreporte.reporte.utilities.DataUtils;

/**
 * Created by simpson on 3/29/2018.
 */

public class BiblicalsAdapter {

    final private ListItemClickListener mOnClickListener;

    // La interfaz que recibe el mensaje onClick
    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }

    public BiblicalsAdapter(ListItemClickListener listener){
        // listener del layout padre
        mOnClickListener = listener;
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
        public BiblicalViewHolder(View itemView){
            super(itemView);
            nombreTB = itemView.findViewById(R.id.nombreTextView);
            monthTB = itemView.findViewById(R.id.monthTextView);
            yearTB = itemView.findViewById(R.id.yearTextView);
            direccionTB = itemView.findViewById(R.id.direccionTextView);

            itemView.setOnClickListener(this);
        }

        void Bind(Biblical biblical){
            if(biblical == null)
                return;
            // Estableciendo los valores al list item del estudio biblico
            nombreTB.setText(String.valueOf(biblical.nombre));
            monthTB.setText(DataUtils.MonthsShort[biblical.month - 1]);
            yearTB.setText(String.valueOf(biblical.year).substring(2));
            direccionTB.setText(String.valueOf(biblical.direccion));
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
