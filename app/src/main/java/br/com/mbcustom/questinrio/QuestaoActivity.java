package br.com.mbcustom.questinrio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.text.SimpleDateFormat;

public class QuestaoActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView mEnuciado, mResposta1, mResposta2, mResposta3, mResposta4;

    private String uidQuestao, enuciado, resposta1, resposta2, resposta3, resposta4, classificacao,
            evento, respostaCerta, respostaSelecionada;

    private Questao questao;

    private TextView mTextMensagem, mTextAcerto, mTextQuestao, mTextResultadoGeral;

    private LinearLayout mLinearButtons;
    private Button mButtonVerRespsota, mProximaPergunta, mButtonFinalizar;

    Random random = new Random();

    String certaRespostas[] = {"Parabéns", "FO+", "Acertou", "Certa resposta", "Continue assim"},
            erradaResposta[] = {"Ops... Respsota Errada", "FO-", "Leia com atenção", "não é essa resposta","Você errou"};

    public String vetorUid[] =  new String[400];

    int tamanhoArray;

    private ConstraintLayout mConstraintPrimaria, mConstraintSecundaria, mConstraintTerceira;

    private int v=0, q=0, contadorAcertos=0, contadorQuestao=1,contadorDiario=0;
    private String acesso, uidUser, dataSalva;
    private String user;

    Date data = new Date();
    Date date = new Date();
    private Button mButtonPremium;

    private String dataAtual, dataAcessoSalvo;
    private int encerraDiario=5;
    private TextView mTextStatusAcesso;
    private TextView mTextTamanhoVetor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_questao );

        getSupportActionBar().hide();
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uidUser = user.getUid();

        SimpleDateFormat formataData = new SimpleDateFormat( "dd-MM-yyyy" );
        dataAtual = formataData.format(data);

        IniciaComponentes();

        evento = getIntent().getExtras().getString("evento");
        classificacao = getIntent().getExtras().getString( "classificacao" );
        Log.d("teste", "UidFavorito: " + evento + " e classificacao " + classificacao);

        CarregaAcesso();

        Click();

    }

    private void CarregaAcesso() {
        db.collection("usuario").document(uidUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                acesso = documentSnapshot.getString( "acesso" );
                Log.d("testes", "acesso recebido: " +acesso);
                if(acesso.equals( "Bloqueado" )){

                    dataAcessoSalvo = documentSnapshot.getString( "acessodata" );

                    Log.d("testes", "data atual: " + dataAtual + ", data salva: " + dataAcessoSalvo);

                    if(dataAtual.equals( dataAcessoSalvo )){

                        mConstraintPrimaria.setVisibility( View.INVISIBLE );
                        mConstraintSecundaria.setVisibility( View.INVISIBLE );
                        mConstraintTerceira.setVisibility( View.VISIBLE );

                        mTextResultadoGeral.setText( "Você atingiu a quantidade de 5 " +
                                "acesso a perguntas hoje, amanhã será liberado mais 5 questões." +
                                " Para não deixar os estudo parar, seja Premium. Responda quantas perguntas você quiser." );
                    }else{
                        alert( "Foi librerado acesso a 5 questões. Bons estudo." );
                        acesso = "Free";
                        FirebaseFirestore.getInstance().collection("/usuario").document(uidUser).update(
                                "acesso", acesso,
                                "respostaDia", "0");

                        contadorDiario =0;

                        CarregaVetor();

                    }
                }else{
                    String respostaDia =documentSnapshot.getString( "respostaDia" );

                    contadorDiario = Integer.parseInt( respostaDia );

                    CarregaVetor();
                }
            }
        });

    }

    private void CarregaVetor() {

        if(classificacao.equals( "TUDAO" )){
            FirebaseFirestore.getInstance().collection( "/questoes" )
                    .addSnapshotListener( new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.e( "Testes", e.getMessage(), e );
                                return;
                            }

                            Log.d( "testes", "entrou no fetch" );
                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                            // adapter.clear();
                            for (DocumentSnapshot doc : docs) {
                                Questao questao = doc.toObject( Questao.class );

                                tamanhoArray = docs.size();

                                String txt;

                                txt = questao.getUuidQuestao();

                                vetorUid[v] = txt;

                                v++;

                            }

                            EmbaralhaVetor();

                        }
                    } );
        }else {
            classificacao = getIntent().getExtras().getString( "classificacao" );

            Log.d( "testes", "Classificação: " + classificacao );

            FirebaseFirestore.getInstance().collection( "/questoes" ).whereEqualTo( "classificacao", classificacao )
                    .addSnapshotListener( new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.e( "Testes", e.getMessage(), e );
                                return;
                            }

                            Log.d( "testes", "entrou no fetch" );
                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                            // adapter.clear();
                            for (DocumentSnapshot doc : docs) {
                                Questao questao = doc.toObject( Questao.class );
                                //uid = FirebaseAuth.getInstance().getUid();

                                tamanhoArray = docs.size();

                                String txt;

                                txt = questao.getUuidQuestao();

                                vetorUid[v] = txt;

                                //Log.d( "teste", "itens do vetor: " + vetorUid[v] );
                                v++;

                            }

                            Log.d( "tamanho", "quantos arquivos banco de dados" + tamanhoArray );
                            //                        v=0;
                            EmbaralhaVetor();

                        }
                    } );
        }
    }

    private void EmbaralhaVetor() {
        String tmp;
        int numbRandom;

        String tamanho;

        tamanho = Integer.toString( tamanhoArray );

        Log.d("testes", "uid: " +uidUser);
        if(uidUser.equals("iSlTMgK1h5NnMZDMvc2Q7gO0Xiy2")){
            Log.d("testes", "entou no uid, tamanho: " + tamanho);
            mTextTamanhoVetor.setVisibility( View.VISIBLE );
            mTextTamanhoVetor.setText( tamanho );
        }else{
            mTextTamanhoVetor.setVisibility( View.GONE );
            Log.d("testes", "entou no else uid ");
        }
        //Log.d( "tamanho", "Total de documentos no banco de dados" + tamanhoArray );


        for (int i=0; i < tamanhoArray; i++) {

            //sorteia um índice
            int j = random.nextInt( tamanhoArray );

            //troca o conteúdo dos índices i e j do vetor
            String temp = vetorUid[i];
            vetorUid[i] = vetorUid[j];
            vetorUid[j] = temp;
        }

   //     for (int i=0; i < tamanhoArray; i++) {
   //         Log.d("testes", "Dado embaralhado " + vetorUid[i]);
   //     }

        if(q==0){
            uidQuestao = vetorUid[0];
        }

        CarregaTexto();

    }

    private void CarregaTexto(){

        Log.d("testes", "uid é " + uidQuestao);
        DocumentReference docRef = db.collection( "/questoes" ).document( uidQuestao );
        docRef.get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //  Log.d( "testes", "DocumentSnapshot data: " + document.getData() );
                        uidQuestao = document.getString( "uuidQuestao" );
                        enuciado = document.getString( "enuciado" );
                        resposta1 = document.getString( "resposta1" );
                        resposta2 = document.getString( "resposta2" );
                        resposta3 = document.getString( "resposta3" );
                        resposta4 = document.getString( "resposta4" );

                        // mTextQuantidadeFavoritos.setText( quantidadeFavString);
                        mEnuciado.setText( enuciado );

                        CarregaQuestao();

                    } else {
                        Log.d( "testes", "No such document" );
                    }
                } else {
                    Log.d( "testes", "get failed with ", task.getException() );
                }
            }
        } );
    }

    private void CarregaQuestao(){
        mEnuciado.setText(enuciado);

        respostaCerta = resposta1;

        String respostas[] = {resposta1, resposta2, resposta3, resposta4};

        String novaPosicao[] = new String[4];

        String tmp;
        int numbRandom;

        int TamanhoLista = respostas.length;

        Log.d("testes", "tamanho da lisata é "+ TamanhoLista);

        String sorteioAtual, sorteio1="não vazio", sorteio2="não vazio", sorteio3="não vazio", sorteio4="não vazio";

        for (int i=0; i < (respostas.length); i++) {

            //sorteia um índice
            int j = random.nextInt( respostas.length );

            //troca o conteúdo dos índices i e j do vetor
            String temp = respostas[i];
            respostas[i] = respostas[j];
            respostas[j] = temp;
        }

        for (int j=0; j < (respostas.length); j++){
//            Log.d("testes", "j= " +j);
            if(j==0){
                if(respostas[0].equals( "" )){
                    mResposta1.setVisibility( View.GONE );
                }else{
                    mResposta1.setVisibility( View.VISIBLE );
                    mResposta1.setText(respostas[0]);
                    resposta1 = respostas[0];
                }
            }else if(j==1){
                if(respostas[1].equals( "" )){
                    mResposta2.setVisibility( View.GONE );
                }else{
                    mResposta2.setVisibility( View.VISIBLE );
                    mResposta2.setText(respostas[1]);
                    resposta2 = respostas[1];
                }
            }else if(j==2){
                if(respostas[2].equals( "" )){
                    mResposta3.setVisibility( View.GONE );
                }else{
                    mResposta3.setVisibility( View.VISIBLE );
                    mResposta3.setText(respostas[2]);
                    resposta3 = respostas[2];
                }
            }else if(j==3){
                if(respostas[3].equals( "" )){
                    mResposta4.setVisibility( View.GONE );
                }else{
                    mResposta4.setVisibility( View.VISIBLE );
                    mResposta4.setText(respostas[3]);
                    resposta4 = respostas[3];
                }
            }

            mConstraintSecundaria.setVisibility( View.GONE );
            mConstraintPrimaria.setVisibility( View.VISIBLE );

        }
        mResposta1.setEnabled( true );
        mResposta2.setEnabled( true );
        mResposta3.setEnabled( true );
        mResposta4.setEnabled( true );

        contadorDiario++;

        String quantidadeResposta = String.valueOf( contadorDiario );

        FirebaseFirestore.getInstance().collection("/usuario").document(uidUser).update(
                "respostaDia", quantidadeResposta
        );
    }

    private void Click() {
        mResposta1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResposta1.setBackgroundResource(R.drawable.arredonda_cantos_azul );
                mResposta2.setBackgroundResource(R.drawable.arredonda_cantos );
                mResposta3.setBackgroundResource(R.drawable.arredonda_cantos );
                mResposta4.setBackgroundResource(R.drawable.arredonda_cantos );
                mLinearButtons.setVisibility( View.VISIBLE );
                mButtonVerRespsota.setVisibility( View.VISIBLE );
                respostaSelecionada = resposta1;
            }
        } );

        mResposta2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResposta1.setBackgroundResource(R.drawable.arredonda_cantos );
                mResposta2.setBackgroundResource(R.drawable.arredonda_cantos_azul );
                mResposta3.setBackgroundResource(R.drawable.arredonda_cantos );
                mResposta4.setBackgroundResource(R.drawable.arredonda_cantos );
                mLinearButtons.setVisibility( View.VISIBLE );
                mButtonVerRespsota.setVisibility( View.VISIBLE );
                respostaSelecionada = resposta2;
            }
        } );

        mResposta3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResposta1.setBackgroundResource(R.drawable.arredonda_cantos );
                mResposta2.setBackgroundResource(R.drawable.arredonda_cantos );
                mResposta3.setBackgroundResource(R.drawable.arredonda_cantos_azul );
                mResposta4.setBackgroundResource(R.drawable.arredonda_cantos );
                mLinearButtons.setVisibility( View.VISIBLE );
                mButtonVerRespsota.setVisibility( View.VISIBLE );
                respostaSelecionada = resposta3;
            }
        } );

        mResposta4.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResposta1.setBackgroundResource(R.drawable.arredonda_cantos );
                mResposta2.setBackgroundResource(R.drawable.arredonda_cantos );
                mResposta3.setBackgroundResource(R.drawable.arredonda_cantos );
                mResposta4.setBackgroundResource(R.drawable.arredonda_cantos_azul );
                mLinearButtons.setVisibility( View.VISIBLE );
                mButtonVerRespsota.setVisibility( View.VISIBLE );
                respostaSelecionada = resposta4;
            }
        } );

        mButtonFinalizar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        } );

        mButtonVerRespsota.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResposta1.setEnabled( false );
                mResposta2.setEnabled( false );
                mResposta3.setEnabled( false );
                mResposta4.setEnabled( false );

                if (contadorDiario == encerraDiario && acesso.equals( "Free" )) {
                    mProximaPergunta.setText( "Restultado Geral" );
                    mProximaPergunta.setVisibility( View.VISIBLE );
                    mButtonVerRespsota.setVisibility( View.GONE );
                } else if (classificacao.equals( "TUDAO" ) && contadorQuestao == 40) {
                    mProximaPergunta.setText( "Restultado Geral" );
                    mProximaPergunta.setVisibility( View.VISIBLE );
                    mButtonVerRespsota.setVisibility( View.GONE );
                } else if (!classificacao.equals( "TUDAO" ) && contadorQuestao == 10) {
                    mProximaPergunta.setText( "Restultado Geral" );
                    mProximaPergunta.setVisibility( View.VISIBLE );
                    mButtonVerRespsota.setVisibility( View.GONE );
                    Log.d( "teste", "Classificacao dentro else tudao " + classificacao );
                } else

                    mProximaPergunta.setVisibility( View.VISIBLE );
                    mButtonVerRespsota.setVisibility( View.GONE );
                if (respostaSelecionada == respostaCerta) {
                    int r = random.nextInt( certaRespostas.length );
                    mTextMensagem.setVisibility( View.VISIBLE );
                    mTextMensagem.setText( certaRespostas[r] );

                    contadorAcertos++;

                    mTextAcerto.setText( Integer.toString( contadorAcertos ) );

                    if (respostaSelecionada == resposta1) {
                        mResposta1.setBackgroundResource( R.drawable.arredonda_cantos_verde );
                    } else if (respostaSelecionada == resposta2) {
                        mResposta2.setBackgroundResource( R.drawable.arredonda_cantos_verde );
                    } else if (respostaSelecionada == resposta3) {
                        mResposta3.setBackgroundResource( R.drawable.arredonda_cantos_verde );
                    } else {
                        mResposta4.setBackgroundResource( R.drawable.arredonda_cantos_verde );
                    }
                } else {
                    int r = random.nextInt( certaRespostas.length );
                    mTextMensagem.setVisibility( View.VISIBLE );
                    mTextMensagem.setText( erradaResposta[r] );

                    if (respostaSelecionada == resposta1) {
                        mResposta1.setBackgroundResource( R.drawable.arredonda_cantos_vermelho );
                    } else if (respostaSelecionada == resposta2) {
                        mResposta2.setBackgroundResource( R.drawable.arredonda_cantos_vermelho );
                    } else if (respostaSelecionada == resposta3) {
                        mResposta3.setBackgroundResource( R.drawable.arredonda_cantos_vermelho );
                    } else {
                        mResposta4.setBackgroundResource( R.drawable.arredonda_cantos_vermelho );
                    }

                    if (respostaCerta == resposta1) {
                        mResposta1.setBackgroundResource( R.drawable.arredonda_cantos_verde );
                    } else if (respostaCerta == resposta2) {
                        mResposta2.setBackgroundResource( R.drawable.arredonda_cantos_verde );
                    } else if (respostaCerta == resposta3) {
                        mResposta3.setBackgroundResource( R.drawable.arredonda_cantos_verde );
                    } else {
                        mResposta4.setBackgroundResource( R.drawable.arredonda_cantos_verde );
                    }

                }
            }
        } );

        mProximaPergunta.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mButtonVerRespsota.setVisibility( View.GONE );
                mProximaPergunta.setVisibility( View.GONE );

                mConstraintSecundaria.setVisibility( View.VISIBLE );
                mConstraintPrimaria.setVisibility( View.INVISIBLE );
                mConstraintTerceira.setVisibility( View.GONE );

                mTextMensagem.setVisibility( View.INVISIBLE );
                mResposta1.setBackgroundResource(R.drawable.arredonda_cantos );
                mResposta2.setBackgroundResource(R.drawable.arredonda_cantos );
                mResposta3.setBackgroundResource(R.drawable.arredonda_cantos );
                mResposta4.setBackgroundResource(R.drawable.arredonda_cantos );

                Log.d("testes", "texto de acesso: " + acesso);

                if(contadorDiario==encerraDiario && !acesso.equals( "Premium" )){
                    mConstraintPrimaria.setVisibility( View.INVISIBLE );
                    mConstraintSecundaria.setVisibility( View.INVISIBLE );
                    mConstraintTerceira.setVisibility( View.VISIBLE );

                    acesso = "Bloqueado";

                    Log.d("testes", "depois do resposta dez " + uidUser);

                    mTextResultadoGeral.setText( "Total de acertos: " + contadorAcertos + ". Você atingiu a quantidade de 5 " +
                            "acesso a perguntas hoje, amanhã será liberado mais 5 questões." +
                            " Para não deixar os estudo parar, seja Premium. Responda quantas perguntas você quiser." );

                    FirebaseFirestore.getInstance().collection("/usuario").document(uidUser).update(
                            "acesso", acesso,
                            "acessodata", dataAtual
                    );

                    //   respostaDez();
                }else if(classificacao.equals( "TUDAO" )&& contadorQuestao==40){
                        mConstraintPrimaria.setVisibility( View.INVISIBLE );
                        mConstraintSecundaria.setVisibility( View.INVISIBLE );
                        mConstraintTerceira.setVisibility( View.VISIBLE );

                        if(contadorAcertos<=13){
                            mTextResultadoGeral.setText( "Você precisa estudar mais. Total de acerto(s) " + Integer.toString( contadorAcertos ));
                        }else if(contadorAcertos<=26){
                            mTextResultadoGeral.setText( "Você está progredindo. Total de acerto(s) " + Integer.toString( contadorAcertos ));
                        }else if(contadorAcertos<=39){
                            mTextResultadoGeral.setText( "Muito bem, continue assim. Total de acerto(s) " + Integer.toString( contadorAcertos ));
                        }else{
                            mTextResultadoGeral.setText( "Parabéns, você gabaritou. Total de acerto(s) " + Integer.toString( contadorAcertos ));
                        }
                }else if(!classificacao.equals( "TUDAO" ) && contadorQuestao==10 ) {
                    mConstraintPrimaria.setVisibility( View.INVISIBLE );
                    mConstraintSecundaria.setVisibility( View.INVISIBLE );
                    mConstraintTerceira.setVisibility( View.VISIBLE );

                    respostaDez();

                }else {
                    Log.d( "testes", "clicou" );
                    q++;

                    contadorQuestao++;

                    mTextQuestao.setText( Integer.toString( contadorQuestao ) );
                    uidQuestao = vetorUid[q];

                    CarregaTexto();
                }
            }
        } );
    }

    private void respostaDez() {
        if(contadorAcertos<=3){
            mTextResultadoGeral.setText( "Você precisa estudar mais. Total de acerto(s) " + Integer.toString( contadorAcertos ));
        }else if(contadorAcertos<=6){
            mTextResultadoGeral.setText( "Você está progredindo. Total de acerto(s) " + Integer.toString( contadorAcertos ));
        }else if(contadorAcertos<=9){
            mTextResultadoGeral.setText( "Muito bem, continue assim. Total de acerto(s) " + Integer.toString( contadorAcertos ));
        }else{
            mTextResultadoGeral.setText( "Parabéns, você gabaritou. Total de acerto(s) " + Integer.toString( contadorAcertos ));
        }

    }

    private void IniciaComponentes() {

        mTextStatusAcesso =findViewById( R.id.textStatus );
        mLinearButtons = findViewById( R.id.linearLayout );
        mResposta1 = findViewById( R.id.textResposta1 );
        mResposta2 = findViewById( R.id.textResposta2 );
        mResposta3 = findViewById( R.id.textResposta3 );
        mResposta4 = findViewById( R.id.textResposta4 );
        mEnuciado = findViewById( R.id.textEnuciado );
        mButtonVerRespsota = findViewById( R.id.btnResponder );
        mTextMensagem = findViewById( R.id.textMensagem );
        mProximaPergunta =findViewById( R.id.btnProxima );
        mTextAcerto = findViewById( R.id.textRendimento );
        mTextQuestao = findViewById( R.id.textNumeroQuestao );
        mButtonFinalizar = findViewById( R.id.btnFinaliar );
        mButtonPremium = findViewById( R.id.button_premiun);
        mButtonPremium = findViewById( R.id.button_premiun);

        mButtonVerRespsota.setVisibility( View.GONE );
        mButtonPremium.setVisibility( View.GONE );

        mConstraintPrimaria = findViewById( R.id.constraint_principal );
        mConstraintSecundaria = findViewById( R.id.constraint_secundaria );
        mConstraintTerceira = findViewById( R.id.constraint_terceira );
        mTextTamanhoVetor =findViewById( R.id.text_tamanho_vetor );

        mTextTamanhoVetor.setVisibility( View.GONE );
        mConstraintPrimaria.setVisibility( View.GONE );
        mConstraintSecundaria.setVisibility( View.VISIBLE );

        mTextResultadoGeral = findViewById( R.id.textRendimentoGeral );
        mTextQuestao.setText( Integer.toString( contadorQuestao ) );
    }

    private void getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.format( date );
    }

    private void alert(String msg) {
        Toast.makeText( QuestaoActivity.this, msg, Toast.LENGTH_SHORT ).show();
    }
}