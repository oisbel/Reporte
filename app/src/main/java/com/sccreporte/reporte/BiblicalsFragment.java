package com.sccreporte.reporte;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sccreporte.reporte.data.Biblical;
import com.sccreporte.reporte.data.BiblicalsData;
import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.utilities.DataUtils;
import com.sccreporte.reporte.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BiblicalsFragment extends Fragment implements BiblicalsAdapter.ListItemClickListener,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private TextView nameUserTV;
    private TextView lugarUserTV;

    //cantidad de elementos del recycler view
    private static final int NUM_LIST_ITEMS = 20;
    private BiblicalsAdapter mBiblicalAdapter;
    private RecyclerView mBiblicalList;

    private CoordinatorLayout coordinatorLayout;

    private TextView mErrorMessageDisplay;
    private TextView mEmptyMessageDisplay;
    // Create a ProgressBar variable to store a reference to the ProgressBar
    private ProgressBar mLoadingIndicator;

    private Toast mToast;

    private ImageButton addBT;

    // Lista de los estudios biblicos
    List<Biblical> mBiblicalsData;

    private User mUser;

    View view;

    public BiblicalsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_biblicals, container, false);

        nameUserTV = view.findViewById(R.id.nameUserTextView);
        lugarUserTV = view.findViewById(R.id.lugarUserTextView);

        final Context context = view.getContext();

        // Add button click
        addBT = view.findViewById(R.id.addButton);
        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = CreateBiblicalActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startActivity(startChildActivityIntent);
            }
        });

        // Get a reference to the error TextView using findViewById
        mErrorMessageDisplay = view.findViewById(R.id.tv_error_message_display);

        // Get a reference to the empty TextView using findViewById
        mEmptyMessageDisplay = view.findViewById(R.id.emptyMessageTextView);

        // Get a reference to the ProgressBar using findViewById
        mLoadingIndicator =  view.findViewById(R.id.pb_loading_indicator);

        // Wiring up RecycerView
        mBiblicalList = view.findViewById(R.id.biblicalRecyclerView);

        coordinatorLayout = view.findViewById(R.id.coordinator_layout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mBiblicalList.setLayoutManager(layoutManager);
        mBiblicalList.setHasFixedSize(true);
        mBiblicalAdapter = new BiblicalsAdapter(this);
        mBiblicalList.setAdapter(mBiblicalAdapter);

        // agregar el nombre y el lugar del usuario
        mUser = DataUtils.loadUserData(context);
        if(mUser.id != -1) {
            nameUserTV.setText(mUser.nombre);
            lugarUserTV.setText(mUser.lugar);
        }
        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(
                0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mBiblicalList);


        /* Once all of our views are setup, we can load the reports data. */
        loadBiblicalData();

        return view;
    }

    private void loadBiblicalData()
    {
        showBiblicalRecyclerView();
        makeBiblicalsQuery();
    }

    /**
     * Establece el id de usuario, y ejecuta el hilo para descargar sus estudios biblicos
     */
    private void makeBiblicalsQuery(){
        new BiblicalsFragment.BiblicalsQueryTask().execute(Integer.toString(mUser.id));
    }

    // Helper methods
    /**
     * This method will make the RecyclerView data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showBiblicalRecyclerView() {
        // First, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // Then, make sure the RecyclerView data is visible
        mBiblicalList.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the Recycler View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        // First, hide the currently visible data
        mBiblicalList.setVisibility(View.INVISIBLE);
        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the empty message visible and hide the Recycler View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showEmptyMessage() {
        // First, hide the currently visible data
        mBiblicalList.setVisibility(View.INVISIBLE);
        // Then, show the error
        mEmptyMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof BiblicalsAdapter.BiblicalViewHolder) {
            // get the removed item name to display it in snack bar
            String name = mBiblicalsData.get(viewHolder.getAdapterPosition()).nombre;

            // backup of removed item for undo purpose
            final Biblical deletedItem = mBiblicalsData.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mBiblicalAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Eliminando a - " + name + " - de los Estudios Bíblicos!", Snackbar.LENGTH_LONG);
            snackbar.setAction("CANCELAR", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mBiblicalAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.addCallback(new Snackbar.Callback(){
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    //super.onDismissed(transientBottomBar, event);
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                        // Snackbar closed on its own
                        AsyncTask deleteBiblicalBackgroundTask = new AsyncTask<String, Void, JSONObject>() {
                            @Override
                            protected JSONObject doInBackground(String... strings) {
                                if(strings.length == 0) return null;
                                int biblical_id = Integer.parseInt(strings[0]);
                                URL deleteBiblicalUrl = NetworkUtils.buildDeleteBiblicalUrl(biblical_id);
                                String deleteBiblicalJSONResult;
                                JSONObject result = null;
                                try{
                                    deleteBiblicalJSONResult = NetworkUtils.geDeleteBiblicalFromHttpUrl(
                                            deleteBiblicalUrl, mUser.email, mUser.password);
                                }catch (IOException e){
                                    e.printStackTrace();
                                    return result;
                                }
                                try {
                                    result = new JSONObject(deleteBiblicalJSONResult);
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                                return result;
                            }

                            @Override
                            protected void onPostExecute(JSONObject jsonObject) {
                                if(jsonObject == null || !jsonObject.has("biblical")){
                                    // no se realizo correctamente
                                    Toast.makeText(view.getContext(),
                                            R.string.delete_biblical_error_message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.execute(Integer.toString(deletedItem.id));
                    }

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    public class BiblicalsQueryTask extends AsyncTask<String, Void, List<Biblical>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Biblical> doInBackground(String... params) {
            if(params.length == 0) return null;

            String user_id = params[0];
            URL biblicalUrl = NetworkUtils.buildBiblicalsUrl(user_id);
            // Para guardar la respuesta string en formato JSON
            String biblicalsJSONResult = null;
            try {
                biblicalsJSONResult = NetworkUtils.getBiblicalsFromHttpUrl(biblicalUrl, mUser.email, mUser.password);
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }
            BiblicalsData biblicalsData = null;

            try {
                biblicalsData =  new BiblicalsData(biblicalsJSONResult);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (biblicalsData != null){
                return biblicalsData.getData();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Biblical> biblicals) {
            // As soon as the loading is complete, hide the loading indicator
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if(biblicals != null && biblicals.size() > 0){
                showBiblicalRecyclerView();
                // Guardo la referecia de la lista de reportes
                mBiblicalsData = biblicals;
                // Mando los datos al adaptador para que los muestre en el recyclerView
                mBiblicalAdapter.setBiblicalData(biblicals);
            }else if (biblicals != null && biblicals.size() == 0){
                showEmptyMessage();
            }
            else{
                showErrorMessage();
            }
        }
    }

}
