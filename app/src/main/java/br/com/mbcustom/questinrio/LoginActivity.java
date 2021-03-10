package br.com.mbcustom.questinrio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mBtnEnter;

    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;
    // [END declare_auth]

    private ProgressBar progressBar;

    private String idUser;

    Date date = new Date();

    private SignInButton mBtnSignIn;

    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private TextView mTextLoginEmpresario;
    private String evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        inicializarComponentes();
        inicializarFirebase();
        conectarGoogleApi();
        clickButton();

    }

    private void clickButton() {
        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void signIn() {

        Intent i = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(i,1);
        //progressBar.setVisibility( View.VISIBLE );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult( ApiException.class);
                Log.d("Teste", "firebaseAuthWithGoogle:" + account.getId());
                FirebaseFirestore.getInstance().collection("/usuario").document().update(
                        "respostaDia", "0"
                );
                firebaseLogin(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Teste", "Google sign in failed", e);
                alert("Falha ao logar");
                // ...
            }
        }
    }


    private void firebaseLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            idUser = FirebaseAuth.getInstance().getUid();

                            Intent i = new Intent(LoginActivity.this, MainActivity.class);

//                            i.putExtra( "classificacao", "Cliente" );

                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(i);
                        }else{
                            alert("Falha na autenticação");
                        }
                    }
                });
    }

    private void conectarGoogleApi() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }

    private void inicializarComponentes() {
        mBtnSignIn = (SignInButton) findViewById(R.id.btnSignIn);
    }

    private void inicializarFirebase(){
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        alert("Falha na Conexão");
    }

    private void alert(String msg) {
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}

   /*     getSupportActionBar().hide();
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById( R.id.progressBarLogin );

        mEmailField = (EditText) findViewById(R.id.edit_email);
        mPasswordField = (EditText)findViewById(R.id.edit_password);

        mBtnEnter = (Button)findViewById( R.id.btn_enter );

        mBtnEnter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnEnter.setEnabled( false );
                mPasswordField.setEnabled( false );
                mEmailField.setEnabled( false );
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        } );

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        //  Toast.makeText( this, "Entrou", Toast.LENGTH_SHORT ).show();
        //   showProgressDialog();

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            Toast.makeText( LoginActivity.this, "Email e senha devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility( View.VISIBLE );

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            idUser = FirebaseAuth.getInstance().getUid();

                            Map<String, Object> itemMenu = new HashMap<>();

                            getDateTime();

                            String datahora = date.toString();

                            itemMenu.put("data", date);

                            Log.d("teste", "Data e hora " + datahora);

                            FirebaseFirestore.getInstance().collection("usuario" ).document(idUser)
                                    .collection( "acessos" ).document(datahora)
                                    .set(itemMenu).addOnSuccessListener( new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                                    Log.d("Testes", "Document has been saved!");

                                    Intent intent = new Intent( LoginActivity.this, MainActivity.class);

//                            intent.putExtra( "classificacao", "Empresario" );

                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                    startActivity(intent);

//                finish();

                                }
                            } ).addOnFailureListener( new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w( "Teste", "Documento não salvo" );
//                                        alert( "Erro ao realizar o cadastro, tente novamente." );
                                }
                            } );



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            progressBar.setVisibility( View.INVISIBLE );
                            mEmailField.setEnabled( true );
                            mPasswordField.setEnabled( true );
                            mBtnEnter.setEnabled( true );
                            Toast.makeText( LoginActivity.this, "Falha ao realizar o login, verifique email e senha.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]

                        //   hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }




    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            //    mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
            //            user.getEmail(), user.isEmailVerified()));
            //    mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            //     findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
            //     findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
            //     findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);

            //     findViewById(R.id.verifyEmailButton).setEnabled(!user.isEmailVerified());
        } else {
            //     mStatusTextView.setText(R.string.signed_out);
            //     mDetailTextView.setText(null);

            //  findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
            //  findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
            //  findViewById(R.id.signedInButtons).setVisibility(View.GONE);
        }
    }

    public void alert(String msg){
        Toast.makeText( this, msg , Toast.LENGTH_SHORT ).show();
    }



}*/