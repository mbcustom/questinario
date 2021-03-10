package br.com.mbcustom.questinrio.ui.tudao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TudaoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TudaoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue( "This is slideshow fragment" );
    }

    public LiveData<String> getText() {
        return mText;
    }
}