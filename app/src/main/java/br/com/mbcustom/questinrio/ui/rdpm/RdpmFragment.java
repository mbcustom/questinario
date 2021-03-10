package br.com.mbcustom.questinrio.ui.rdpm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.List;

import br.com.mbcustom.questinrio.Questao;
import br.com.mbcustom.questinrio.QuestaoActivity;
import br.com.mbcustom.questinrio.QuestoesActivity2;
import br.com.mbcustom.questinrio.R;

public class RdpmFragment extends Fragment {


    int i=0;

    private RdpmViewModel rdpmViewModel;

    private GroupAdapter adapter;

    private String uid;



   // private Questao questao;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rdpmViewModel =
                ViewModelProviders.of( this ).get( RdpmViewModel.class );
        View root = inflater.inflate( R.layout.fragment_home, container, false );

        Intent intent = new Intent(getActivity(), QuestaoActivity.class);

        intent.putExtra( "classificacao", "RDPM" );
        intent.putExtra( "evento", "responder" );

        startActivity(intent);

//        fetchCategoria();


        return root;
    }


    private void alert(String msg) {
        Toast.makeText( getActivity(), msg, Toast.LENGTH_SHORT ).show();
    }


}
