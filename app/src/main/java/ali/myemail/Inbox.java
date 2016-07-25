package ali.myemail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.renderscript.Matrix4f;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.independentsoft.exchange.FindItemResponse;
import com.independentsoft.exchange.Item;
import com.independentsoft.exchange.Message;
import com.independentsoft.exchange.MessagePropertyPath;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.StandardFolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Inbox extends MainActivity{

    private ListView lvMessages;
    private ArrayList<EmailMessage> mailList;
    private CustomBaseAdapter adapter;
    private String exchange;
    private String email;
    private String password;
    private ProgressDialog progress;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_inbox);
        getLayoutInflater().inflate(R.layout.activity_inbox, frameLayout);

        lvMessages = (ListView) findViewById(R.id.lvMessage);
        mailList = new ArrayList<EmailMessage>();
        registerForContextMenu(lvMessages);

        lvMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmailMessage message = mailList.get(position);

                Intent intent = new Intent(getApplicationContext(), EmailDetail.class);
                intent.putExtra("From", message.getFrom());
                intent.putExtra("Cc", message.getCc());
                intent.putExtra("Subject", message.getSubject());
                intent.putExtra("Date", message.getReceivedTime());
                intent.putExtra("Body", message.getBody());
                startActivity(intent);
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(this);  // Preferences objesi olþturuluyor
        editor = preferences.edit();

        if (preferences != null) {
            exchange = preferences.getString("Exchange", "");
            email = preferences.getString("Email", "");
            password = preferences.getString("Password", "");
        }

        progress = ProgressDialog.show(this, "Loading", "Wait while loading...");
        new MyAsyncTask(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lvMessage) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(mailList.get(info.position).getSubject());
            String[] menuItems = getResources().getStringArray(R.array.ContextMenu);

            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.ContextMenu);
        String menuItemName = menuItems[menuItemIndex];
        // DELETE

        return true;
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String>{

        Context context;
        public MyAsyncTask(Context context){
            this.context = context;
        }
        @Override
        protected String doInBackground(String... params) {
            String str = postData(params);
            return str;
        }
        protected void onPostExecute(String result){

        }
        protected void onProgressUpdate(Integer... progress){

        }

        public String postData(String valueIWantToSend[]) {

            String returnValue = "";

            try {
                Service service = new Service(exchange, email, password);
                FindItemResponse response = service.findItem(StandardFolder.INBOX, MessagePropertyPath.getAllPropertyPaths());

                for (Item item : response.getItems()){
                    if (item instanceof Message){
                        EmailMessage message = new EmailMessage();
                        message.setIsRead(((Message) item).isRead());
                        message.setTo(((Message) item).getDisplayTo());
                        message.setFrom((((Message) item).getFrom() != null) ? ((Message) item).getFrom().getName() : "");
                        message.setCc(((Message) item).getDisplayCc());
                        message.setSubject(item.getSubject());
                        message.setBody(item.getBodyPlainText());
                        message.setReceivedTime(new SimpleDateFormat("dd.MMM.yyyy HH:mm").format(((Message) item).getReceivedTime()));
                        mailList.add(message);
                    }
                }

                adapter = new CustomBaseAdapter(context, mailList);

                // Farklý sýnýftan ana activity sýnýfýnýn view'ine müdahalede
                // bulunulamadýðý için runOnUiThread metodunu kullanýlýyor
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lvMessages.setAdapter(adapter);
                    }
                    //returnValue = inboxFolder.getDisplayName();
                });
                progress.dismiss();
            }
            catch (ServiceException ex)
            {
                Log.w("ServiceException", ":" + ex.getMessage());
            }

            return returnValue;
        }
    }
}
