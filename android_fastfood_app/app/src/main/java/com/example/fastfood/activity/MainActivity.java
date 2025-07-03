package com.example.fastfood.activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.fastfood.fragment.SettingFragment;

import com.example.fastfood.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav_view);

        if (savedInstanceState == null) {
            loadFragment(new HomeActivity());
            bottomNavView.setSelectedItemId(R.id.navigation_home);
        }
        if (savedInstanceState == null) {
            loadFragment(new SettingFragment());
            bottomNavView.setSelectedItemId(R.id.navigation_settings);
        }
        bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home) {
                    selectedFragment = new HomeActivity();
                } else if (itemId == R.id.navigation_orders) {
                    // selectedFragment = new OrdersFragment(); // TODO: Tạo Fragment này
                } else if (itemId == R.id.navigation_settings) {
                    selectedFragment = new SettingFragment(); // TODO: Tạo Fragment này
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
    }
}