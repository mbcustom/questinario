package br.com.mbcustom.questinrio.ui.administracao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AdministracaoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AdministracaoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue( "This is slideshow fragment" );
    }

    public LiveData<String> getText() {
        return mText;
    }
}