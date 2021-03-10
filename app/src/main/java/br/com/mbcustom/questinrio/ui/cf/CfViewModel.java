package br.com.mbcustom.questinrio.ui.cf;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CfViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CfViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue( "This is slideshow fragment" );
    }

    public LiveData<String> getText() {
        return mText;
    }
}