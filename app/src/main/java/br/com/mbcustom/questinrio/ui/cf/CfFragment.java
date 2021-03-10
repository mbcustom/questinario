package br.com.mbcustom.questinrio.ui.cf;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.xwray.groupie.GroupAdapter;

import br.com.mbcustom.questinrio.QuestaoActivity;
import br.com.mbcustom.questinrio.R;

public class CfFragment extends Fragment {


    int i=0;

    private CfViewModel cfViewModel;

    private GroupAdapter adapter;

    private String uid;



   // private Questao questao;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cfViewModel =
                ViewModelProviders.of( this ).get( CfViewModel.class );
        View root = inflater.inflate( R.layout.fragment_home, container, false );

        Intent intent = new Intent(getActivity(), QuestaoActivity.class);

        intent.putExtra( "classificacao", "CF ARTs 18 a 33 e 76 a 91" );
        intent.putExtra( "evento", "responder" );

        startActivity(intent);

//        fetchCategoria();


        return root;
    }


    private void alert(String msg) {
        Toast.makeText( getActivity(), msg, Toast.LENGTH_SHORT ).show();
    }


}
