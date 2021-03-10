package br.com.mbcustom.questinrio.ui.home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.com.mbcustom.questinrio.CadastroQuestaoActivity;
import br.com.mbcustom.questinrio.R;

public class HomeFragment extends Fragment implements BillingProcessor.IBillingHandler{

    private HomeViewModel homeViewModel;

    private Button mBtnAddQuestao;

    private String user;

    String evento;

    private BillingProcessor bp;
    private TextView tvStatus;
    private Button mBtnPremiun;
    private TransactionDetails subscriptionTrasactionDetails = null;
    private String acesso;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of( this ).get( HomeViewModel.class );
        View root = inflater.inflate( R.layout.fragment_home, container, false );
        mBtnAddQuestao = root.findViewById( R.id.btnAddQuestao );




//        bp = new BillingProcessor(getActivity(), getResources().getString( R.string.play_console_licence ), this);
//        bp.initialize();

        tvStatus = root.findViewById( R.id.tv_premiun );
        mBtnPremiun = root.findViewById( R.id.btn_premiun );

        user = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("/usuario").document(user);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d( "testes", "DocumentSnapshot data: " + document.getData() );
                        acesso = document.getString( "acesso" );

                        Log.d("testes", "acesso dentro do home " + acesso);

                        if (!acesso.equals( "Premium" )){
                            mBtnPremiun.setVisibility( View.VISIBLE );
                        }else{
                            mBtnPremiun.setVisibility( View.GONE );
                        }
                    } else {
                        Log.d( "testes", "get failed with ", task.getException() );
                    }
                }
            }
        });



        if(user.equals("iSlTMgK1h5NnMZDMvc2Q7gO0Xiy2")||user.equals( "WoJK5Bi41wfDQAsmMtXu73EkXAV2" )){
            mBtnAddQuestao.setVisibility( View.VISIBLE );
        }else{
            mBtnAddQuestao.setVisibility( View.GONE );
        }


        mBtnAddQuestao.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent( getActivity(), CadastroQuestaoActivity.class );

                evento = "addQuestao";

                Log.d("XXX", "Dentro do intent em fragmento " + evento);

                i.putExtra( "evento", evento );

                startActivity( i );
            }
        } );

        mBtnPremiun.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /*      if (bp.isSubscriptionUpdateSupported()){
                    bp.subscribe(getActivity(), prodoctId);
                }else{
                    Log.d("mensagemhome", "onBillingInitialized: não tem suporte de assinatura.");
                }

           */

                Log.d("testes", "Clicou no botao para se tornar premiun");

                String texto;

                texto = ("Olá, estou usando o Study com acesso Free, como faço para ter acesso Premiun?");

                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.putExtra("jid", "556581503381@s.whatsapp.net");
                sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                try {
                    startActivity(sendIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText( getActivity(), "Você não possue o Whatsapp instalado em seu dispositivo", Toast.LENGTH_LONG );
                }

            }
        } );

        return root;
    }

    private boolean hasSubcription(){
        return subscriptionTrasactionDetails.purchaseInfo != null;
    }

    @Override
    public void onBillingInitialized() {
        Log.d( "mensagemhome", "onBillingInitialized: ");

        final String prodoctId = getResources().getString( R.string.product_id );

        subscriptionTrasactionDetails=  bp.getSubscriptionTransactionDetails(prodoctId);


        if (hasSubcription()){

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uidUser = user.getUid();

            String acesso = "premiun";

            Log.d("testes", "depois do resposta dez " + uidUser);

            FirebaseFirestore.getInstance().collection("/usuario").document(uidUser).update(
                    "acesso", acesso
            );

            mBtnPremiun.setVisibility( View.GONE );
            tvStatus.setText("Status: Premium");
        }else{
            tvStatus.setText( "Status: Free" );
        }
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Log.d( "mensagemhome", "onProductPurchased: ");
    }

    @Override
    public void onPurchaseHistoryRestored() {
        Log.d( "mensagemhome", "onPurchaseHistoryRestored: ");
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Log.d( "mensagemhome", "onBillingError: " );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}
