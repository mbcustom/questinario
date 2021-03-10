package br.com.mbcustom.questinrio.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import br.com.mbcustom.questinrio.LoginActivity;
import br.com.mbcustom.questinrio.R;

public class LogoutFragment extends Fragment {

    private LogoutViewModel logoutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logoutViewModel =
                ViewModelProviders.of( this ).get( LogoutViewModel.class );
        View root = inflater.inflate( R.layout.fragment_home, container, false );

        FirebaseAuth.getInstance().signOut();
        verifyAuthentication();

        return root;
    }

    private void verifyAuthentication() {
        if (FirebaseAuth.getInstance().getUid() == null) {
            Intent intent = new Intent( getActivity(), LoginActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }

}
