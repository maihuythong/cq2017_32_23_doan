package com.maihuythong.testlogin.ShowListUsers;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListUsersActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SharedPreferences sf;
    private User[] usersArray;
    private ListView lvUsers;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        lvUsers = (ListView) findViewById(R.id.lv_users);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.finish_adding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_user, menu);

        MenuItem menuItem= menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        EditText textSearch =(EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        textSearch.setTextColor(Color.WHITE);

        textSearch.setHint("Enter friend name to search");
        textSearch.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.friend_serch_hint_text_color));
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        String userInput = query.toLowerCase();
        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.getListUsers(userInput, 1, 10).enqueue(new Callback<UserReq>() {
            @Override
            public void onResponse(Call<UserReq> call, Response<UserReq> response) {

                if (response.code() == 200) {
                    usersArray = response.body().getUsers();

                    final ArrayList<User> arrUser = new ArrayList<>();
                    for (int i = 0; i < usersArray.length; i++) {
                        arrUser.add(usersArray[i]);
                    }
                    UsersAdapter usersAdapter = new UsersAdapter(ListUsersActivity.this, R.layout.user_layout, arrUser);
                    lvUsers.setAdapter(usersAdapter);

                    lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            User newUser;
                            newUser = arrUser.get(position);
                            SendInvitation(newUser);
                        }
                    });

                }
                if (response.code()==500)
                {
                    Toast.makeText(ListUsersActivity.this,"Server error on getting tour invitation list", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<UserReq> call, Throwable t) {
            }

        });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();

        APIService mAPIService = ApiUtils.getAPIService();

        mAPIService.getListUsers(userInput, 1, 10).enqueue(new Callback<UserReq>() {
            @Override
            public void onResponse(Call<UserReq> call, Response<UserReq> response) {

                if (response.code() == 200) {
                    usersArray = response.body().getUsers();

                    final ArrayList<User> arrUser = new ArrayList<>();
                    for (int i = 0; i < usersArray.length; i++) {
                        arrUser.add(usersArray[i]);
                    }
                    UsersAdapter usersAdapter = new UsersAdapter(ListUsersActivity.this, R.layout.user_layout, arrUser);
                    lvUsers.setAdapter(usersAdapter);

                    lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            User newUser;
                            newUser = arrUser.get(position);
                            hideInputKeyboard();
                            SendInvitation(newUser);
                        }
                    });
                }
                if (response.code()==500)
                {
                    Toast.makeText(ListUsersActivity.this,"Server error on getting tour invitation list", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserReq> call, Throwable t) {
            }
        });
        return true;
    }

    private void SendInvitation(User user){
        APIService mAPIService = ApiUtils.getAPIService();
        String s;
        s = LoginActivity.token;
        if(s == null){
            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            s = sf.getString("login_access_token", "");
        }
        long tourId = getIntent().getLongExtra("tourId",0);
        Long userId = user.getID();
        mAPIService.senInvation(s,tourId,userId.toString(),false).enqueue(new Callback<SendInvationRes>() {
            @Override
            public void onResponse(Call<SendInvationRes> call, Response<SendInvationRes> response) {
                if(response.code()==200) {
//                    MyFirebaseService myFirebaseService = new MyFirebaseService();
//                    myFirebaseService
                    Toast.makeText(ListUsersActivity.this, "Send invitation " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
                if(response.code() == 500)
                    Toast.makeText(ListUsersActivity.this,"Server error on adding member to a tour!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<SendInvationRes> call, Throwable t) {
                Toast.makeText(ListUsersActivity.this,"Can not send!", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void hideInputKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(ListUsersActivity.this,"Cant hide keyboard!", Toast.LENGTH_LONG).show();
        }
    }
}
