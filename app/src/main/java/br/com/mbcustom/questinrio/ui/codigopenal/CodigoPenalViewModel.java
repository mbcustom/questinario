package br.com.mbcustom.questinrio.ui.codigopenal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CodigoPenalViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CodigoPenalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue( "This is slideshow fragment" );
    }

    public LiveData<String> getText() {
        return mText;
    }
}