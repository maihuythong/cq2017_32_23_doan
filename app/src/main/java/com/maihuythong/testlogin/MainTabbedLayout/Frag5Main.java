package com.maihuythong.testlogin.MainTabbedLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.material.snackbar.Snackbar;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.invitationTour.AdapterInvitation;
import com.maihuythong.testlogin.invitationTour.Invitation;
import com.maihuythong.testlogin.invitationTour.ShowInvitationReq;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.userInfo.GetVerifyCodeRes;
import com.maihuythong.testlogin.userInfo.InputVerifyCodeActivity;
import com.maihuythong.testlogin.userInfo.UpdateUserInfoActivity;
import com.maihuythong.testlogin.userInfo.UpdateUserInfoRes;
import com.maihuythong.testlogin.userInfo.UserInfoRes;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag5Main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag5Main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag5Main extends Fragment {
    private ImageView avatarView;
    private AutoCompleteTextView fullNameView;
    private AutoCompleteTextView emailView;
    private AutoCompleteTextView phoneView;
    private EditText dobView;
    private RadioGroup genderGroup;

    private Date dobUser;
    private String fullNameUser;
    private String emailUser;
    private String phoneNumberUser;
    private String avatarUser;
    private long genderUser;

    public ProgressDialog mProgressDialog;

    private TextView userIdView;
    private TextView genderView;

    UserInfoRes UserInfo;

    private View mView;
    private AppCompatActivity mActivity;
    private OnFragmentInteractionListener mListener;

    public Frag5Main() {
        // Required empty public constructor
    }

    public static Frag5Main newInstance() {
        Frag5Main fragment = new Frag5Main();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_update_user_info, container, false);
        mActivity = ((AppCompatActivity)getActivity());

        Toolbar toolbar = mView.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);

        userIdView = (TextView)mView.findViewById(R.id.userInfo_id);
        genderView = (TextView)mView.findViewById(R.id.genderInfo_user);
        avatarView = (ImageView)mView.findViewById(R.id.avatar_user);
        avatarView = (ImageView)mView.findViewById(R.id.avatar_view_up);
        fullNameView = (AutoCompleteTextView)mView.findViewById(R.id.full_name_up);
        emailView = (AutoCompleteTextView)mView.findViewById(R.id.email_up);
        phoneView = (AutoCompleteTextView)mView.findViewById(R.id.phone_number_up);
        dobView = (EditText)mView.findViewById(R.id.dob_up);
        genderGroup = (RadioGroup)mView.findViewById(R.id.radio_group_gender_up);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);

        GetUserInfo();

        Button LogOutButton = (Button)mView.findViewById(R.id.logout_button);
        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
            }
        });

        Button VerifyEmail =(Button)mView.findViewById(R.id.verify_email);

        VerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerifyCode("email");
            }
        });

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.radio_male_up)
                    genderUser=0;
                if(checkedId == R.id.radio_female_up)
                    genderUser=1;
                else genderUser = 0;
            }
        });

        dobView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDOB();
            }
        });


        Button submitInfo = (Button)mView.findViewById(R.id.submit_update_user_info);
        submitInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullNameUser= fullNameView.getText().toString();
                emailUser = emailView.getText().toString();
                phoneNumberUser = phoneView.getText().toString();
                if(TextUtils.isEmpty(fullNameUser)){
                    Toast.makeText(getContext(), "Input full name information!", Toast.LENGTH_LONG).show();
                } else {
                    hideInputKeyboard();
                    SubmitUpdateInfoUser(fullNameUser, emailUser, phoneNumberUser, genderUser, dobUser);
                }
            }
        });

        return mView;
    }


    private void GetUserInfo(){
        String token = GetTokenLoginAccess();
        APIService mAPIService = ApiUtils.getAPIService();

        mAPIService.getUserInfo(token).enqueue(new Callback<UserInfoRes>() {
            @Override
            public void onResponse(Call<UserInfoRes> call, Response<UserInfoRes> response) {
                if(response.code()==200) {
                    Toast.makeText(getContext(), "Get info success", Toast.LENGTH_LONG).show();
                    UserInfo = response.body();
                    if(!Objects.isNull(UserInfo.getAvatar()))
                        Picasso.get().load(UserInfo.getAvatar()).into(avatarView);

                    if(!Objects.isNull(UserInfo.getID()))
                        userIdView.setText(String.valueOf(UserInfo.getID()));
                    if(!Objects.isNull(UserInfo.getEmail()))
                        emailView.setText(UserInfo.getEmail());
                    if(!Objects.isNull(UserInfo.getFullName()))
                        fullNameView.setText(UserInfo.getFullName());
                    if(!Objects.isNull(UserInfo.getPhone()))
                        phoneView.setText(UserInfo.getPhone());
                    if(!Objects.isNull(UserInfo.getGender()))
                        if(UserInfo.getGender()==0)
                            genderView.setText("Nam");
                        else genderView.setText("Nữ");
                    if(!Objects.isNull(UserInfo.getDob()))
                        dobView.setText(UserInfo.getDob());

                }

                if(response.code()==401)
                    Toast.makeText(getContext(),"No authorization token was found", Toast.LENGTH_LONG).show();

                if(response.code()==503)
                    Toast.makeText(getContext(),"Server error on creating user", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UserInfoRes> call, Throwable t) {
                Toast.makeText(getContext(),"Error get information user", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void LogOut(){
        mProgressDialog.show();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(getContext().getString(R.string.saved_access_token));
        editor.remove(getContext().getString(R.string.saved_access_token_time));
        editor.commit();
        LogOutFaceBook();
        // Open LoginActivity
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getContext().startActivity(intent);
        mActivity.finish();
        mProgressDialog.hide();
    }


    private void LogOutFaceBook(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        AccessToken.setCurrentAccessToken(null);
    }


    private void getVerifyCode(final String typeVerify){
        APIService mAPIService = ApiUtils.getAPIService();

        mAPIService.getVerify(UserInfo.getID(),typeVerify).enqueue(new Callback<GetVerifyCodeRes>() {
            @Override
            public void onResponse(Call<GetVerifyCodeRes> call, Response<GetVerifyCodeRes> response) {
                if(response.code()==200) {
//                    Toast.makeText(UpdateUserInfoActivity.this, "Verify was sent, please check email to get verify code!", Toast.LENGTH_LONG).show();
                    Snackbar.make(mView.findViewById(android.R.id.content).getRootView(),
                            "Verify was sent, please check email to get verify code!", Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), InputVerifyCodeActivity.class);
                    intent.putExtra("userId",UserInfo.getID());
                    intent.putExtra("typeVerify",typeVerify);
                    startActivity(intent);
                }

                if(response.code()==400) {
                    String message = "Send failed!";
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        message = jObjError.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(),"Error "+ message, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<GetVerifyCodeRes> call, Throwable t) {

            }
        });
    }


    private void chooseDOB(){
        final Calendar calendar = Calendar.getInstance();
        //Get current date and time
        int dayCr = calendar.get(Calendar.DATE);
        int monthCr = calendar.get(Calendar.MONTH);
        int yearCr = calendar.get(Calendar.YEAR);

        //Take time from DatePicker into calendar
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(year,month,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dobView.setText(simpleDateFormat.format(calendar.getTime()));
                try {
                    dobUser =  simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        },yearCr,monthCr,dayCr);

        datePickerDialog.show();
    }


    private void hideInputKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(getContext(),"Cant hide keyboard!", Toast.LENGTH_LONG).show();
        }
    }


    void SubmitUpdateInfoUser(String fullNameUser,String emailUser,String phoneNumberUser,long genderUser,Date dobUser){

        String token = GetTokenLoginAccess();
        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.EditInfo(token,fullNameUser,emailUser,phoneNumberUser,genderUser,dobUser).enqueue(new Callback<UpdateUserInfoRes>() {
            @Override
            public void onResponse(Call<UpdateUserInfoRes> call, Response<UpdateUserInfoRes> response) {
                if(response.code()==200) {
//                    Toast.makeText(UpdateUserInfoActivity.this, "Update information success", Toast.LENGTH_LONG).show();
                    Snackbar.make(mView.findViewById(android.R.id.content),
                            "Update infomation successfully", Snackbar.LENGTH_LONG).show();
//                    Intent intent = new Intent(UpdateUserInfoActivity.this,UserInfoActivity.class);
//                    startActivity(intent);
                }
                if(response.code()==400)
                    Toast.makeText(getContext(), "Invalid params", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UpdateUserInfoRes> call, Throwable t) {

                Toast.makeText(getContext(), "Failed update!!", Toast.LENGTH_LONG).show();

            }
        });

    }


    private String GetTokenLoginAccess(){
        SharedPreferences sf;
        String token;
        token = LoginActivity.token;
        if(token== null){
            sf = mActivity.getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }
        return token;
    }











    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
