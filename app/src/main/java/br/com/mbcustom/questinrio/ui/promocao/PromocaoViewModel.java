package br.com.mbcustom.questinrio.ui.promocao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PromocaoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PromocaoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue( "This is slideshow fragment" );
    }

    public LiveData<String> getText() {
        return mText;
    }
}