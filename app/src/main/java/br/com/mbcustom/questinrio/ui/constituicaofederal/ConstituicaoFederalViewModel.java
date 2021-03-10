package br.com.mbcustom.questinrio.ui.constituicaofederal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConstituicaoFederalViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConstituicaoFederalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue( "This is slideshow fragment" );
    }

    public LiveData<String> getText() {
        return mText;
    }
}