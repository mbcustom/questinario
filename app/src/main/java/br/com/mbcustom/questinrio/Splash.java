package br.com.mbcustom.questinrio;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;

public class Splash extends AppCompatActivity {

    private String user, uid, classificacao;
    private NavigationView navigationView;

    Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.splash );

        getSupportActionBar().hide();
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        if (FirebaseAuth.getInstance().getUid() == null) {

            Intent i = new Intent(Splash.this, LoginActivity.class);

            startActivity(i);
            finish();

        }else{
            new Handler( ).post( new Runnable() {
                @Override
                public void run() {
                    user = FirebaseAuth.getInstance().getUid();

                    FirebaseFirestore.getInstance().collection( "usuario" ).document( user )
                            .get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();

                                Intent i = new Intent(Splash.this, MainActivity.class);

                                startActivity(i);
                                finish();
                            } else {
                                Log.d( "TTestes", "get failed with ", task.getException() );
                            }
                        }
                    } );

                }
            });
        }



    }


}
