package br.com.mbcustom.questinrio.ui.cppm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CppmViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CppmViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue( "This is slideshow fragment" );
    }

    public LiveData<String> getText() {
        return mText;
    }
}