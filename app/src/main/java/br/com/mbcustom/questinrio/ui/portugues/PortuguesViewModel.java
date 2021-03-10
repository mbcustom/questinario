package br.com.mbcustom.questinrio.ui.portugues;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PortuguesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PortuguesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue( "This is slideshow fragment" );
    }

    public LiveData<String> getText() {
        return mText;
    }
}