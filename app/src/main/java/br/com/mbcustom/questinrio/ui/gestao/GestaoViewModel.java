package br.com.mbcustom.questinrio.ui.gestao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GestaoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GestaoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue( "This is slideshow fragment" );
    }

    public LiveData<String> getText() {
        return mText;
    }
}