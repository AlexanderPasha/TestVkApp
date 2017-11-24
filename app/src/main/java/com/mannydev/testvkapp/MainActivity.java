package com.mannydev.testvkapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.model.VKScopes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {
    public static final String TOKEN = "aToken";
    public static final String APP_MEM = "app";

    String[] scope = new String[]{VKScopes.WALL, VKScopes.PHOTOS, VKScopes.FRIENDS, VKScopes.VIDEO};
    @BindView(R.id.button)
    Button button;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(APP_MEM, MODE_PRIVATE);


    }

    @Override
    protected void onResume() {
        if(VKSdk.isLoggedIn()){
            Intent intent = new Intent(MainActivity.this, WallActivity.class);
            startActivity(intent);
        }
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Intent intent = new Intent(MainActivity.this, WallActivity.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TOKEN, res.accessToken);
                editor.apply();
                startActivity(intent);
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(MainActivity.this,"Login failed",Toast.LENGTH_LONG).show();
            }

        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        VKSdk.login(MainActivity.this, scope);
    }
}
