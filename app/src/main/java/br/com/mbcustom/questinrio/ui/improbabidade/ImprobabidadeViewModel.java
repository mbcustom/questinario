package br.com.mbcustom.questinrio.ui.improbabidade;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ImprobabidadeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ImprobabidadeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue( "This is slideshow fragment" );
    }

    public LiveData<String> getText() {
        return mText;
    }
}