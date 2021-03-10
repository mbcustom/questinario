package br.com.mbcustom.questinrio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestoesActivity2 extends AppCompatActivity {

    private TextView mEnuciado, mResposta1, mResposta2, mResposta3, mResposta4;

    String enuciado, resposta1, resposta2, resposta3, resposta4;

    private String uidProduto, evento, uidFavorito, uidDestaque;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_questoes );

        getSupportActionBar().hide();
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

//        uid = FirebaseAuth.getInstance().getUid();
        Log.d("teste", "UidFavorito: " + evento);

        IniciaComponentes();

//        produtoSelecionado = "";

        evento = getIntent().getExtras().getString("evento");

        Log.d("teste", "UidFavorito: " + evento);

//        mConstraintSecundaria.setVisibility( View.VISIBLE );
//        mConstraintPrincipal.setVisibility( View.INVISIBLE );

//        VerificaEvento();

//        Click();

    }

    private void IniciaComponentes() {

    }


/*

    private void fetchCategoria() {

//        uid = FirebaseAuth.getInstance().getUid();
//        Log.d("XXXX", uid);

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference studentsCollectionReference = rootRef.collection("questao");
        studentsCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Questao> ListRelacionados = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Questao questao = document.toObject(Questao.class);
                        ListRelacionados.add(questao);
                        //                      Log.d("testes","entrou no For");
                    }

                    int TamanhoLista = ListRelacionados.size();
                    int j = 0;

                    String Contador;
                    //     Log.d("testes", "tamanho " + TamanhoLista);

                    List<Questao> randomRelacionadosLista = new ArrayList<>();
                    for(int i = 0; i < TamanhoLista; i++) {

//                        Log.d("testes","entrou no For 2");
                        Questao randomRelacionados = ListRelacionados.get(new Random().nextInt(TamanhoLista));

                        String produtoAtualLista = randomRelacionados.getEnuciado();

                        Log.d("testes", "o nome do produto atual " + nomeProduto + " produto da lista " + randomRelacionados.getNomeproduto());
                        Log.d("teste", "produto selecionado " + produtoSelecionado);
                        if(!nomeProduto.equals( produtoAtualLista)){

                            if(!produtoSelecionado.equals( produtoAtualLista )){
                                if(!randomRelacionadosLista.contains(randomRelacionados)) {

                                    j++;

                                    // Log.d("testes","entrou no IF");
                                    randomRelacionadosLista.add(randomRelacionados);

                                    //                                Log.d("testes", "Lista " + randomRelacionados.getNomeproduto());

                                    categoriaRecomendado = randomRelacionados.getCategoriaProduto();

                                    if(j == 1){

                                        categoriaRecomendado = randomRelacionados.getCategoriaProduto();
                                        mConstraintRelacionado1.setVisibility( View.VISIBLE );
                                        uidRelacionado1 = randomRelacionados.getUuidProduto();
                                        mTextNomeRelacionado1.setText(randomRelacionados.getNomeproduto());

                                        Log.d( "testes", "primeiro produto relacionado " + randomRelacionados.getNomeproduto() );

                                        Picasso.get()
                                                .load( randomRelacionados.getUrlproduto() )
                                                .into( mImgItemRelacionado1 );

                                        //                                  Log.d("testes", "Entrou no 1");

                                    }else{
                                        if(j == 2){

                                            categoriaRecomendado = randomRelacionados.getCategoriaProduto();
                                            mConstraintRelacionado2.setVisibility(View.VISIBLE );
                                            uidRelacionado2 = randomRelacionados.getUuidProduto();
                                            mTextNomeRelacionado2.setText(randomRelacionados.getNomeproduto());

                                            Log.d( "testes", "Segundo produto relacionado " + randomRelacionados.getNomeproduto() );

                                            Picasso.get()
                                                    .load( randomRelacionados.getUrlproduto() )
                                                    .into( mImgItemRelacionado2 );
                                            //                                    Log.d("testes", "Entrou no 2");
                                        }else{
                                            if(j == 3){

                                                categoriaRecomendado = randomRelacionados.getCategoriaProduto();
                                                mConstraintRelacionado3.setVisibility(View.VISIBLE );
                                                uidRelacionado3 = randomRelacionados.getUuidProduto();
                                                mTextNomeRelacionado3.setText(randomRelacionados.getNomeproduto());

                                                Picasso.get()
                                                        .load( randomRelacionados.getUrlproduto() )
                                                        .into( mImgItemRelacionado3 );
                                                //                                      Log.d("testes", "Entrou no 3");
                                                mConstraintRelacionado4.setVisibility( View.INVISIBLE );
                                            }else{
                                                if(j == 4){

                                                    categoriaRecomendado = randomRelacionados.getCategoriaProduto();
                                                    mConstraintRelacionado4.setVisibility(View.VISIBLE );
                                                    uidRelacionado4 = randomRelacionados.getUuidProduto();
                                                    mTextNomeRelacionado4.setText(randomRelacionados.getNomeproduto());

                                                    Picasso.get()
                                                            .load( randomRelacionados.getUrlproduto() )
                                                            .into( mImgItemRelacionado4 );
                                                    //                                        Log.d("testes", "Entrou no 4");
                                                }
                                            }

                                        }
                                    }

                                    if(TamanhoLista == 2){
                                        if(randomRelacionadosLista.size() == 1 ) {
                                            break;
                                        }
                                    }else {
                                        if (TamanhoLista == 3) {
                                            if (randomRelacionadosLista.size() == 2) {
                                                break;
                                            }
                                        } else {
                                            if (TamanhoLista == 4) {
                                                if (randomRelacionadosLista.size() == 3) {
                                                    break;
                                                }
                                            } else {
                                                if (TamanhoLista >= 4) {
                                                    if (randomRelacionadosLista.size() == 4) {
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }


                            }else {

                                Log.d("testes", "entou no se não, valor do I " + i );
                                if (TamanhoLista == 1) {

                                    mConstraintRelacionado1.setVisibility( View.GONE );
                                    mConstraintRelacionado2.setVisibility( View.GONE );
                                    mConstraintRelacionado3.setVisibility( View.GONE );
                                    mConstraintRelacionado4.setVisibility( View.GONE );

                                    break;
                                }
                                i--;

                                Log.d("testes", "valor do i " + i);
                            }

                        }
                    }
                } else {
                    Log.d("Teste", "Error getting documents: ", task.getException());
                }
            }
        });
    }
*/
}
