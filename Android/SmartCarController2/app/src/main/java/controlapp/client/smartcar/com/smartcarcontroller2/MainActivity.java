package controlapp.client.smartcar.com.smartcarcontroller2;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import controlapp.client.smartcar.com.smartcarcontroller2.fragment.ConnectFragment;
import controlapp.client.smartcar.com.smartcarcontroller2.fragment.ElectromyogramFragment;
import controlapp.client.smartcar.com.smartcarcontroller2.fragment.JoystickFragment;
import controlapp.client.smartcar.com.smartcarcontroller2.fragment.VoiceFragment;
import controlapp.client.smartcar.com.smartcarcontroller2.thread.ClientThread;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private ClientThread clientThread;

    private int currentFragmentId = R.id.nav_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new ConnectFragment()).commit();

        clientThread = new ClientThread(this, mhandler);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (id == R.id.nav_camera) {
            fragmentTransaction.replace(R.id.content_frame, ConnectFragment.newInstance("connect","connect"));
        } else if (id == R.id.nav_gallery) {
            fragmentTransaction.replace(R.id.content_frame, JoystickFragment.newInstance("joystick","joystick"));
        } else if (id == R.id.nav_slideshow) {
            fragmentTransaction.replace(R.id.content_frame, VoiceFragment.newInstance("voice","voice"));
        } else if (id == R.id.nav_manage) {
            fragmentTransaction.replace(R.id.content_frame, ElectromyogramFragment.newInstance("gram","gram"));
        } else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        fragmentTransaction.commit();

        return true;
    }

    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public ClientThread getClient() {
        return clientThread;
    }
}
