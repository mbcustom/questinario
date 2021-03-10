package br.com.mbcustom.questinrio;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class CadastroQuestaoActivity extends AppCompatActivity {


    private EditText mEditEnuciado, mEditResposta1, mEditResposta2, mEditResposta3, mEditResposta4;

    private Button mBtnCancelar, mBtnSalvar, mBtnDelet;

    private Spinner mSpinnerClassificacao;

    private ProgressBar mProgressBarProduto;

    private String uid;
    private String uidQuestao;

    String ComoSalvar;

    String enuciado, resposta1, resposta2, resposta3, resposta4,
            classificacao;

    String evento;

    private Questao dadosQuestao;
    String textSpinnerClassificacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_cadastro_questao );

        ComoSalvar = "";

        uid = FirebaseAuth.getInstance().getUid();

        evento = getIntent().getExtras().getString( "evento" );

        IniciaComponentes();
        // alert( evento );

        //Array para unidade
        ArrayAdapter<String> listaGrandesa = new ArrayAdapter<String>(CadastroQuestaoActivity.this,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.unidade));
        listaGrandesa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerClassificacao.setAdapter(listaGrandesa);

        //Carrega();
        if(evento.equals( "addQuestao" )) {
            mBtnDelet.setVisibility( View.GONE );
            uidQuestao = UUID.randomUUID().toString();
            //        alert( "entrou do carrega " + uidProduto );

        }else{
            dadosQuestao = getIntent().getExtras().getParcelable( "dadosItem" );

            mBtnDelet.setVisibility( View.VISIBLE);

            uidQuestao = dadosQuestao.getUuidQuestao();

            mEditEnuciado.setText( dadosQuestao.getEnuciado() );
            mEditResposta1.setText( dadosQuestao.getResposta1() );
            mEditResposta2.setText( dadosQuestao.getResposta2() );
            mEditResposta3.setText( dadosQuestao.getResposta3() );
            mEditResposta4.setText( dadosQuestao.getResposta4() );

            //mEditMassa.setText( dadosQuestao.get() );
            textSpinnerClassificacao = dadosQuestao.getClassificacao();
            setSpinText(mSpinnerClassificacao, dadosQuestao.getClassificacao());

            ComoSalvar = "atualização";


        }

        Click();

    }

    private void IniciaComponentes() {

        mEditEnuciado = findViewById( R.id.edit_enuciado_questao );
        mEditResposta1 = findViewById( R.id.edit_resposta_1 );
        mEditResposta2 = findViewById( R.id.edit_resposta_2 );
        mEditResposta3 =findViewById( R.id.edit_resposta_3 );
        mEditResposta4 = findViewById( R.id.edit_resposta_4 );

        mBtnCancelar = findViewById( R.id.btnCancelar );
        mBtnSalvar =findViewById( R.id.btnSalvar );
        mBtnDelet = findViewById( R.id.btnDelete );

        mSpinnerClassificacao = findViewById( R.id.spinner_classificacao );

        mProgressBarProduto = findViewById( R.id.progressBarProduto );
    }

    private void ArrayComponentes() {

    }


    private void Click() {

        mBtnSalvar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                salvaFireStore();

            }
        } );


        mBtnCancelar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );

        mBtnDelet.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(CadastroQuestaoActivity.this)
                        .setTitle("Deletar  Item")
                        .setMessage("Tem certeza que deseja deletar esse Item?")
                        .setPositiveButton("sim", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                classificacao = mSpinnerClassificacao.getSelectedItem().toString();

                                FirebaseStorage.getInstance().getReference( "/imagesitem/").child( uidQuestao )
                                        .delete().addOnSuccessListener( new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseFirestore.getInstance().collection( classificacao )
                                                .document( uidQuestao ).delete();


                                        alert( "Item deletado com sucesso" );



                                        //finish();
                                    }
                                } );

                            } }).setNegativeButton("não", null) .show();

            }
        } );
    }

    private void carrega() {
    }

    public void setSpinText(Spinner spin, String text) {
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {
            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                spin.setSelection(i);
            }
        }
    }

    private void salvaFireStore() {

        enuciado = mEditEnuciado.getText().toString();
        enuciado = mEditEnuciado.getText().toString();
        resposta1 = mEditResposta1.getText().toString();
        resposta2 = mEditResposta2.getText().toString();
        resposta3 = mEditResposta3.getText().toString();
        resposta4 = mEditResposta4.getText().toString();
        classificacao = mSpinnerClassificacao.getSelectedItem().toString();

        if (enuciado == null || enuciado.isEmpty() || resposta1 == null || resposta1.isEmpty()
                ||resposta2 == null || resposta2.isEmpty()
                || classificacao == null || classificacao.isEmpty()) {
            Toast.makeText(this, "Os campos obrigatorios devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressBarProduto.setVisibility( View.VISIBLE );

        //quantidadeFavorito = 0;

        Map<String, Object> itemMenu = new HashMap<>();

        itemMenu.put("uuidQuestao", uidQuestao);
        itemMenu.put("enuciado", enuciado);
        itemMenu.put("resposta1", resposta1);
        itemMenu.put("resposta2", resposta2);
        itemMenu.put("resposta3", resposta3);
        itemMenu.put("resposta4", resposta4);
        itemMenu.put("classificacao", classificacao);


        FirebaseFirestore.getInstance().collection("questoes" ).document(uidQuestao)
                .set(itemMenu).addOnSuccessListener( new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Testes", "Document has been saved!");

                mProgressBarProduto.setVisibility( View.INVISIBLE );

                alert( "Questão cadastrada com Sucesso." );
                mEditEnuciado.setText( "" );
                mEditResposta1.setText( "" );
                mEditResposta2.setText( "" );
                mEditResposta3.setText( "" );
                mEditResposta4.setText( "" );

                uidQuestao = UUID.randomUUID().toString();

//                finish();

            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w( "Teste", "Documento não salvo" );
                alert( "Erro ao realizar o cadastro, tente novamente." );
            }
        } );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void alert(String msg) {
        Toast.makeText( CadastroQuestaoActivity.this, msg, Toast.LENGTH_SHORT ).show();
    }

}

