package ali.myemail;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    protected DrawerLayout drawerLayout;
    private ListView listView;
    private String[] menuItems;
    private ActionBarDrawerToggle drawerListener;
    protected FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        frameLayout = (FrameLayout)findViewById(R.id.mainContent);
        menuItems = getResources().getStringArray(R.array.MenuItems);
        listView = (ListView) findViewById(R.id.drawerList);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuItems));
        listView.setOnItemClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerListener = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerOpened(android.view.View drawerView) {
                super.onDrawerOpened(drawerView);
                // Toast.makeText(MainActivity.this, "Drawer Opened", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDrawerClosed(android.view.View drawerView) {
                super.onDrawerClosed(drawerView);
                // Toast.makeText(MainActivity.this, "Drawer Closed", Toast.LENGTH_SHORT).show();
            }
        };

        drawerLayout.setDrawerListener(drawerListener);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerListener.setHomeAsUpIndicator(R.drawable.ic_drawer);
        drawerListener.syncState();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerListener.syncState();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
        openActivity(menuItems[position]);

        // Toast.makeText(this, menuItems[position] + " was selected ", Toast.LENGTH_LONG).show();
        selectItem(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerListener.onOptionsItemSelected(item)){
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerListener.onConfigurationChanged(newConfig);
    }

    private void selectItem(int position) {
        listView.setItemChecked(position, true);
        setTitle(menuItems[position]);
    }

    private void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void openActivity(String activityName) {
        switch(activityName){
            case "Eposta Oluştur":
                Intent intentNew = new Intent(this, NewMailActivity.class);
                startActivity(intentNew);
                break;
            case "Gelen Kutusu":
                Intent intentGelen = new Intent(this, Inbox.class);
                startActivity(intentGelen);
                break;
            case "Çıkış":
                finish();
                System.exit(0);
                break;
            default: break;
        }
    }
}
