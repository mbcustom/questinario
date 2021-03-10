package br.com.mbcustom.questinrio.ui.portaria;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PortariaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PortariaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue( "This is slideshow fragment" );
    }

    public LiveData<String> getText() {
        return mText;
    }
}