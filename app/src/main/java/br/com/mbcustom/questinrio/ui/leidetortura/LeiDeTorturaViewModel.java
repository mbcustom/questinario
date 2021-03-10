package br.com.mbcustom.questinrio.ui.leidetortura;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LeiDeTorturaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LeiDeTorturaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue( "This is slideshow fragment" );
    }

    public LiveData<String> getText() {
        return mText;
    }
}