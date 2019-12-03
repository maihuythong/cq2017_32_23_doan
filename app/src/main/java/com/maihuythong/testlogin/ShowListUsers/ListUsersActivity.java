package com.maihuythong.testlogin.ShowListUsers;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListUsersActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private User[] usersArray;

    private ListView lvUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        lvUsers = (ListView) findViewById(R.id.lv_users);


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_user, menu);

        MenuItem menuItem= menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
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

                    ArrayList<User> arrUser = new ArrayList<>();
                    for (int i = 0; i < usersArray.length; i++) {
                        arrUser.add(usersArray[i]);
                    }
                    UsersAdapter usersAdapter = new UsersAdapter(ListUsersActivity.this, R.layout.user_layout, arrUser);
                    lvUsers.setAdapter(usersAdapter);
                }
                if (response.code()==500)
                {
                    Toast.makeText(ListUsersActivity.this,"Server error on adding member to a tour", Toast.LENGTH_LONG).show();
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

                    ArrayList<User> arrUser = new ArrayList<>();
                    for (int i = 0; i < usersArray.length; i++) {
                        arrUser.add(usersArray[i]);
                    }
                    UsersAdapter usersAdapter = new UsersAdapter(ListUsersActivity.this, R.layout.user_layout, arrUser);
                    lvUsers.setAdapter(usersAdapter);
                }
                if (response.code()==500)
                {
                    Toast.makeText(ListUsersActivity.this,"Server error on adding member to a tour", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserReq> call, Throwable t) {
            }
        });
        return true;
    }
}
