package br.com.mbcustom.questinrio.ui.cemt;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CemtViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CemtViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue( "This is slideshow fragment" );
    }

    public LiveData<String> getText() {
        return mText;
    }
}