package br.com.mbcustom.questinrio;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Questao implements Parcelable {

    private String uuidQuestao;
    private String enuciado;
    private String resposta1;
    private String resposta2;
    private String resposta3;
    private String resposta4;
    private String classificacao;

    public Questao(){

    }

    public Questao(String uuidquestao, String enuciado, String resposta1,
                   String resposta2, String resposta3, String resposta4,
                   String classificacao) {

        this.uuidQuestao = uuidquestao;
        this.enuciado = enuciado;
        this.resposta1 = resposta1;
        this.resposta2 = resposta2;
        this.resposta3 = resposta3;
        this.resposta4 = resposta4;
        this.classificacao = classificacao;
    }

    protected Questao(Parcel in) {
        uuidQuestao = in.readString();
        enuciado = in.readString();
        resposta1 = in.readString();
        resposta2 = in.readString();
        resposta3 = in.readString();
        resposta4 = in.readString();
        classificacao = in.readString();
    }

    public static final Creator<Questao> CREATOR = new Creator<Questao>() {
        @Override
        public Questao createFromParcel(Parcel in) {
            return new Questao(in);
        }

        @Override
        public Questao[] newArray(int size) {
            return new Questao[size];
        }
    };

    public String getUuidQuestao() {

        Log.d("XXX", "Entrou aqui no loja");
        return uuidQuestao;
    }

    public void setUuidQuestao(String uuidQuestao) {
        this.uuidQuestao = uuidQuestao;
    }


    public String getEnuciado() {
        return enuciado;
    }

    public void setEnuciado(String enuciado) {
        this.enuciado = enuciado;
    }

    public String getResposta1() {
        return resposta1;
    }

    public void setResposta1(String resposta1) {
        this.resposta1 = resposta1;
    }

    public String getResposta2() {
        return resposta2;
    }

    public void setResposta2(String resposta2) {
        this.resposta2 = resposta2;
    }

    public String getResposta3() {
        return resposta3;
    }

    public void setResposta3(String resposta3) {
        this.resposta3 = resposta3;
    }

    public String getResposta4() {
        return resposta4;
    }

    public void setResposta4(String resposta4) {
        this.resposta4 = resposta4;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuidQuestao);
        dest.writeString(enuciado);
        dest.writeString(resposta1);
        dest.writeString(resposta2);
        dest.writeString(resposta3);
        dest.writeString(resposta4);
        dest.writeString(classificacao);
    }
}
