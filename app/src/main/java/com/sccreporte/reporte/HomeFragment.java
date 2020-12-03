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
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sccreporte.reporte.data.SocialMediaData;
import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.utilities.DataUtils;
import com.sccreporte.reporte.utilities.NetworkUtils;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

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
    private User mUser;

    ViewPager viewPager;
    DotsIndicator dotsIndicator;
    ViewPagerSocialAdapter viewPagerSocialAdapter;

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
        final Context context = view.getContext();

        helpBT = view.findViewById(R.id.helpButton);
        imageUserBT = view.findViewById(R.id.imageUserButton);
        addReportBT = view.findViewById(R.id.imageButtonAddReport);
        biblicalBT = view.findViewById(R.id.imageButtonBiblical);
        userDataTV = view.findViewById(R.id.myDataTextView);
        welcomeTV = view.findViewById(R.id.welcomeTextView);

        viewPager = view.findViewById(R.id.view_pager);
        dotsIndicator = view.findViewById(R.id.indicator);
        viewPagerSocialAdapter = new ViewPagerSocialAdapter(context);

        JSONObject jsonObject = DataUtils.loadSocialMediaLinks(context);
        String sccLink = "";
        String radioLink = "";
        String facebookLink = "";
        String facebook_page_id = "";
        String youtubeLink = "";
        String twitterLink = "";
        String instagramLink = "";

        try{
            sccLink = jsonObject.getString("website");
            radioLink = jsonObject.getString("radio");
            facebookLink = jsonObject.getString("facebook");
            facebook_page_id = jsonObject.getString("facebook_page_id");
            youtubeLink = jsonObject.getString("youtube");
            twitterLink = jsonObject.getString("twitter");
            instagramLink = jsonObject.getString("instagram");
        }catch (JSONException e){
            e.printStackTrace();
        }

        SocialMediaData[] socialMediaData = {
                new SocialMediaData(R.drawable.ic_action_scc, R.drawable.ic_action_radio, R.drawable.ic_action_facebook,
                        getString(R.string.scc_label), getString(R.string.radio_label), getString(R.string.facebook_label),
                        sccLink, radioLink, facebookLink),
                new SocialMediaData(R.drawable.ic_action_youtube, R.drawable.ic_action_twitter, R.drawable.ic_action_instagram,
                        getString(R.string.youtube_label), getString(R.string.twitter_label), getString(R.string.instagram_label),
                        youtubeLink, twitterLink, instagramLink)};

        viewPagerSocialAdapter.FillData(socialMediaData, facebook_page_id);

        viewPager.setAdapter(viewPagerSocialAdapter);
        dotsIndicator.setViewPager(viewPager);



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
}
