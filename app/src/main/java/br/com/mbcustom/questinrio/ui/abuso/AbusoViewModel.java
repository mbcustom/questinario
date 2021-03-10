package br.com.mbcustom.questinrio.ui.abuso;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AbusoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AbusoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue( "This is slideshow fragment" );
    }

    public LiveData<String> getText() {
        return mText;
    }
}