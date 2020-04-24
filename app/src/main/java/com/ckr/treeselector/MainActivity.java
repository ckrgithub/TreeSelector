package com.ckr.treeselector;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * @author ckr
 */
public class MainActivity extends AppCompatActivity {
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mainFragment = MainFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.main_container, mainFragment, MainFragment.class.getSimpleName())
                    .commit();
        } else {
            fragmentManager.beginTransaction().show(mainFragment = (MainFragment) fragmentManager.findFragmentByTag(MainFragment.class.getSimpleName()))
                    .commit();
        }
    }
}
