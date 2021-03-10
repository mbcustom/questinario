package br.com.mbcustom.questinrio.ui.tudao;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import br.com.mbcustom.questinrio.QuestaoActivity;
import br.com.mbcustom.questinrio.R;

public class TudaoFragment extends Fragment {

    private TudaoViewModel tudaoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tudaoViewModel =
                ViewModelProviders.of( this ).get( TudaoViewModel.class );
        View root = inflater.inflate( R.layout.fragment_home, container, false );

        Intent intent = new Intent(getActivity(), QuestaoActivity.class);

        intent.putExtra( "classificacao", "TUDAO" );
        intent.putExtra( "evento", "responder" );

        startActivity(intent);


        return root;
    }
}
