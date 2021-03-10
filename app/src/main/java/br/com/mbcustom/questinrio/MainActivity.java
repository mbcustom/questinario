package br.com.mbcustom.questinrio;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class
MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private AppBarConfiguration mAppBarConfiguration;

    private String acesso, uidUser;

    private ImageView mImagemUsuario;

    private TextView mTextNomeUsuario, mTextEmailUsuario;
    private String nome, email;
    private Date date = new Date();
    private TextView mTextStatusAcesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_main );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        NavigationView navigationView = findViewById( R.id.nav_view );
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_abuso_de_poder, R.id.nav_administracao_publica, R.id.nav_ce_mt,
                R.id.nav_codigo_penal, R.id.nav_cf_art, R.id.nav_constituicao_federal, R.id.nav_cppm
                , R.id.nav_estatuto_militares, R.id.nav_gestao_estrategica, R.id.nav_improbidade_administrativa
                , R.id.nav_lei_de_tortura, R.id.nav_movimentacao, R.id.nav_pad, R.id.nav_portaria_052
                , R.id.nav_portugues, R.id.nav_promocao, R.id.nav_rdmp, R.id.nav_tudao, R.id.nav_logout )
                .setDrawerLayout( drawer )
                .build();
        NavController navController = Navigation.findNavController( this, R.id.nav_host_fragment );
        NavigationUI.setupActionBarWithNavController( this, navController, mAppBarConfiguration );
        NavigationUI.setupWithNavController( navigationView, navController );

        //**********************************************************
        //CARREGAR INFORMAÇÕES DO USUARIO NA TELA DE NAVEGAÇÃO
        View headView = navigationView.getHeaderView( 0 );
        mImagemUsuario = headView.findViewById( R.id.imageUsuario );

        mTextEmailUsuario = headView.findViewById( R.id.textEmailUsuario );
        mTextNomeUsuario = headView.findViewById( R.id.textNomeUsuario );
        mTextStatusAcesso = headView.findViewById( R.id.textStatus );

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            nome = user.getDisplayName();
            email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            mTextNomeUsuario.setText( nome );
            mTextEmailUsuario.setText( email );

            Picasso.get()
                    .load( photoUrl )
                    .into( mImagemUsuario );

            uidUser = user.getUid();

            Log.d("testes","uid no main " +uidUser);

            db = FirebaseFirestore.getInstance();


                    DocumentReference docRef = db.collection("/usuario").document(uidUser);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("testes", "DocumentSnapshot data: " + document.getData());
                                    acesso = document.getString( "acesso" );

                                    mTextStatusAcesso.setText( acesso );

                                } else {
                                    Log.d("testes", "No such document");
                                    Log.d("teste", "dentro do else do fire main");

                                    Map<String, Object> criaUser = new HashMap<>();

                                    criaUser.put( "iduser" , uidUser);
                                    criaUser.put( "acesso" , "Free");
                                    criaUser.put( "email", email);
                                    criaUser.put( "nameuser" , nome );
                                    criaUser.put( "respostaDia" , "0" );

                                    db.collection("/usuario").document(uidUser)
                                            .set(criaUser)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("testes", "DocumentSnapshot successfully written!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("testes", "Error writing document", e);
                                                }
                                            });

                                }
                            } else {
                                Log.d("testes", "get failed with ", task.getException());
                            }
                        }
                    });

        }

    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController( this, R.id.nav_host_fragment );
        return NavigationUI.navigateUp( navController, mAppBarConfiguration )
                || super.onSupportNavigateUp();
    }
}
