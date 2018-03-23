package com.sccreporte.reporte;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.databinding.ActivityUserDataBinding;
import com.sccreporte.reporte.utilities.DataUtils;

public class UserDataActivity extends AppCompatActivity {

    ImageButton backBT;
    User mUser;
    ActivityUserDataBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        // Set the content view to the layout
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_data);

        // Back button click
        backBT = findViewById(R.id.backButton);
        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mUser = DataUtils.loadUserData(this);
        if(mUser.id == -1)
            finish();

        displayUserData();
    }

    /**
     * Bind each attribute in the views to the corresponding data
     */
    private void displayUserData(){
        mBinding.nameUserTextView.setText(String.valueOf(mUser.nombre));
        mBinding.lugarUserTextView.setText(String.valueOf(mUser.lugar));

        String temp = String.valueOf(mUser.grado);
        if(temp != "")
            mBinding.gradoTextView.setText(temp);

        temp = String.valueOf(mUser.ministerio);
        if(temp != "")
            mBinding.ministerioTextView.setText(temp);

        temp = String.valueOf(mUser.responsabilidad);
        if(temp != "")
            mBinding.responsabilidadTextView.setText(temp);

        temp = String.valueOf(mUser.pastor);
        if(temp != "")
            mBinding.pastorTextView.setText(temp);

        temp = String.valueOf(mUser.numero);
        if(temp != "")
            mBinding.numeroTextView.setText(temp);
    }
}
