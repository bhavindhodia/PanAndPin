package com.android.panpin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileFragment extends Fragment {
    private static String TAG = ProfileFragment.class.getSimpleName();
    private View root;
    private TextView nameTitle;
    private TextView emailTitle;
    private FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseUser = Paper.book("user").read("FirebaseUser");

        setProfile();


        return root;
    }

    private void setProfile() {
        String profilePic = Paper.book("user").read("FirebaseUserProfile");
        ImageView profileImage = root.findViewById(R.id.profileImg);
        nameTitle = root.findViewById(R.id.profileTitle);
        emailTitle = root.findViewById(R.id.profileTitle2);
        nameTitle.setText(firebaseUser.getDisplayName());
        emailTitle.setText(firebaseUser.getEmail());
        Button logout = root.findViewById(R.id.logoutBtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });

        RequestOptions glideOptions = new RequestOptions()
                .circleCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(getApplicationContext())
                .load(profilePic)
                .apply(glideOptions)
                .placeholder(R.drawable.fakeuser)
                .error(R.drawable.fakeuser)
                .into(profileImage);

    }
}