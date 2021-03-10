package br.com.mbcustom.questinrio.ui.rdpm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RdpmViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RdpmViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue( "This is slideshow fragment" );
    }

    public LiveData<String> getText() {
        return mText;
    }
}