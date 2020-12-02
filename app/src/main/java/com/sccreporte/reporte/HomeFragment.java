package com.sccreporte.reporte;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.utilities.DataUtils;
import com.sccreporte.reporte.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ImageButton helpBT;
    private ImageButton imageUserBT;
    private ImageButton addReportBT;
    private ImageButton biblicalBT;
    private TextView userDataTV;
    private TextView welcomeTV; // El email del usuario
    private ImageButton sccBT;
    private ImageButton radioBT;
    private ImageButton facebookBT;
    private User mUser;

    private AsyncTask mBackgroundTask; // para llamar a ItIsTimeToNewReport

    View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        helpBT = view.findViewById(R.id.helpButton);
        imageUserBT = view.findViewById(R.id.imageUserButton);
        addReportBT = view.findViewById(R.id.imageButtonAddReport);
        biblicalBT = view.findViewById(R.id.imageButtonBiblical);
        userDataTV = view.findViewById(R.id.myDataTextView);
        welcomeTV = view.findViewById(R.id.welcomeTextView);
        sccBT = view.findViewById(R.id.sccImageButton);
        radioBT = view.findViewById(R.id.radioImageButton);
        facebookBT = view.findViewById(R.id.faceBookImageButton);

        final Context context = view.getContext();

        // pop up the menu for help
        helpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TutorialActivity.class);
                startActivity(intent);

            }
        });

        // establecer el click listener para ultimos reportes
        /*lastReportsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = LastReportsActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startActivity(startChildActivityIntent);
            }
        });*/

        // establecer el click listener para mis datos
        userDataTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = UserDataActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, "edit");
                startActivity(startChildActivityIntent);
            }
        });

        imageUserBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = UserDataActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, "logout");
                startActivity(startChildActivityIntent);
            }
        });

        // establecer el click para crear estudios biblicos
        biblicalBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = CreateBiblicalActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startActivity(startChildActivityIntent);
            }
        });

        // establecer el click para crear reporte or edit report
        addReportBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(DataUtils.loadItIsTimeToNewReport(context))
                //    openCreateReport();
                //else openEditReport(DataUtils.loadReportDataForeEdit(context));
                createOrEditReport();
            }
        });

        assignSocialMediaLinks(DataUtils.loadSocialMediaLinks(context));

        // cargar los datos del usuario desde share preferences
        mUser = DataUtils.loadUserData(context);
        if(mUser!= null && mUser.email!=""){
            welcomeTV.setText(mUser.nombre);
        }

        // Schedule the create report reminder
        //ReminderUtilities.scheduleCreateReportReminder(context);

        // Save to sharepreference if its time to create a new report get it from the server
        //createOrEditReport();

        return view;
    }

    /**
     * Abre create report activity
     */
    private void openCreateReport() {
        Context context = view.getContext();
        Class destinationActivity = CreateReportActivity.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        startActivity(startChildActivityIntent);
    }
    /**
     * Abre edit report activity
     */
    private void openEditReport(JSONObject report) {
        Context context = view.getContext();
        Class destinationActivity = EditReportActivity.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, report.toString());
        startActivity(startChildActivityIntent);
    }

    /**
     * Se encarga de preguntar si es tiempo de crear un nuevo reporte y abrir create report activity
     * o simplemente abrir la activity para editar el ultimo reporte
     */
    private void createOrEditReport(){
        mBackgroundTask = new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... voids) {
                URL itIsTimeUrl = NetworkUtils.buildItIsTimeUrl();
                String lastReportJSONResult = "";
                JSONObject result = null;
                try{
                    if(mUser != null) {
                        lastReportJSONResult = NetworkUtils.getItIsTimeFromHttpUrl(
                                itIsTimeUrl, mUser.email, mUser.password);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    return result;
                }
                try {
                    result = new JSONObject(lastReportJSONResult);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                if(jsonObject != null){
                    if(jsonObject.has("id")){
                        // solo editar
                        DataUtils.saveItIsTimeToNewReport(view.getContext(),false);
                        DataUtils.saveReportDataForeEdit(view.getContext(), jsonObject);
                        //openEditReport(jsonObject);

                        //Mostrar cartel indicando que no es tiempo de crear un reporte
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                view.getContext());
                        // Setting Dialog Title
                        alertDialog.setTitle("No es tiempo de crear un reporte");
                        // Setting Dialog Message
                        alertDialog.setMessage("Desea editar su último reporte?");
                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.ic_action_addreport);
                        final JSONObject jsonObjectToEdit = jsonObject;
                        // Setting Positive "Yes" Btn
                        alertDialog.setPositiveButton("SI",
                                new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dialog.dismiss();
                                        openEditReport(jsonObjectToEdit);
                                    }
                                });
                        // Setting Negative "NO" Btn
                        alertDialog.setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        dialog.cancel();
                                    }
                                });
                        // Showing Alert Dialog
                        alertDialog.show();
                    }
                    else{
                        //crear nuevo
                        DataUtils.saveItIsTimeToNewReport(view.getContext(),true);
                        openCreateReport();
                    }
                }
            }
        }.execute();
    }

    /**
     * asigna los link en jsonObject a los botones de las redes sociales
     * @param jsonObject
     */
    private void assignSocialMediaLinks(final JSONObject jsonObject){
        if(jsonObject == null)
            return;
        // Abrir el sitio web de scc
        sccBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    openSCCWebPage(jsonObject.getString("website"));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        // Abrir el sitio web de la radio
        radioBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    openSCCWebPage(jsonObject.getString("radio"));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        // Abrir el facebook
        facebookBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    openSCCWebPage(getFacebookPageURL(jsonObject.getString("facebook_page_id"),
                            jsonObject.getString("facebook")));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void openSCCWebPage(String url){
        PackageManager packageManager = getActivity().getPackageManager();
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent);
        }
    }

    /**
     * Devuelve el valor url en dependencia de si facebook app esta
     * instalado o no (para que se abra el browser o la app)
     * @return
     */
    public String getFacebookPageURL(String facebook_page_id, String facebook_url){
        if(appInstalled("com.facebook.katana")){
            return "fb://page/" + facebook_page_id;
        }else {
            return facebook_url;
        }
    }

    /**
     * To know if facebook app is installed
     * @param uri
     * @return
     */
    private boolean appInstalled(String uri)
    {
        PackageManager packageManager = getActivity().getPackageManager();
        try
        {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            boolean activated =  packageManager.getApplicationInfo(uri, 0).enabled;
            return activated;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
