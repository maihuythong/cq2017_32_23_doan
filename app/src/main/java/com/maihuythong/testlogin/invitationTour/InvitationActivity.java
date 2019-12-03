package com.maihuythong.testlogin.invitationTour;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationActivity extends AppCompatActivity {

    private Invitation invitation[];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invitation);



        String s = LoginActivity.token;
        if(s == null){
            SharedPreferences sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            s = sf.getString("login_access_token", "");
        }


        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.getInvitation(s,1,20).enqueue(new Callback<ShowInvitationReq>() {
            @Override
            public void onResponse(Call<ShowInvitationReq> call, Response<ShowInvitationReq> response) {
                if(response.code() == 200){
                    invitation = response.body().getInvitations();

                    ArrayList<Invitation> arrInv = new ArrayList<>();
                    ListView lvInvitation = findViewById(R.id.invitation_tour);

                    for(int i = 0; i<invitation.length; i++){
                        arrInv.add(invitation[i]);
                    }
                    AdapterInvitation adapterInvitation =
                            new AdapterInvitation(InvitationActivity.this,R.layout.custom_invitation_list, arrInv);
//                    lvInvitation.setItemsCanFocus(false);
                    lvInvitation.setAdapter(adapterInvitation);
                }
            }

            @Override
            public void onFailure(Call<ShowInvitationReq> call, Throwable t) {

            }
        });

    }
}
