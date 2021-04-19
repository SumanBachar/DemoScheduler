package com.example.demoscheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.demoscheduler.fragment.FragHome;
import com.example.demoscheduler.fragment.FragInfo;
import com.example.demoscheduler.fragment.FragNoti;

public class Dashboard extends AppCompatActivity {

    MeowBottomNavigation meowBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setViewReferences();
        bindEventHandlers();

        meowBottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_home));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_info));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_notification));

        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

            }
        });

        meowBottomNavigation.show(2,true);
        loadFragment(new FragInfo());

        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch(item.getId()){
                    case 1:
                        loadFragment(new FragHome());
                        break;
                    case 2:
                        loadFragment(new FragInfo());
                        break;
                    case 3:
                        loadFragment(new FragNoti());
                    default:
                        break;
                }
            }
        });

        meowBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });
    }

    private void setViewReferences(){
        meowBottomNavigation = findViewById(R.id.meow_bottom_nav);
    }

    private void bindEventHandlers(){}

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_dashboard,fragment).commit();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

}