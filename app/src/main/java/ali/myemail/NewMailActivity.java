package ali.myemail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.independentsoft.exchange.Body;
import com.independentsoft.exchange.ItemId;
import com.independentsoft.exchange.ItemInfoResponse;
import com.independentsoft.exchange.Mailbox;
import com.independentsoft.exchange.Message;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;


public class NewMailActivity extends MainActivity {

    String exchange;
    String email;
    String password;
    String sendResponse;
    EditText etFrom;
    EditText etTo;
    EditText etCc;
    EditText etSubject;
    EditText etBody;
    Button btnSend;
    Button btnCancel;
    SharedPreferences preferences;//preferences referans�
    SharedPreferences.Editor editor; //preferences editor nesnesi referans� .prefernces nesnesine veri ekleyip c�karmak i�in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_new_mail);
        getLayoutInflater().inflate(R.layout.activity_new_mail, frameLayout);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);  // Preferences objesi olu�turuluyor
        editor = preferences.edit();                                        // Ayn� �ekilde editor nesnesi olu�turuluyor

        if (preferences != null) {
            exchange = preferences.getString("Exchange", "");
            email = preferences.getString("Email", "");
            password = preferences.getString("Password", "");
        }

        etFrom = (EditText) findViewById(R.id.etFrom);
        etTo = (EditText) findViewById(R.id.etTo);
        etCc = (EditText) findViewById(R.id.etCc);
        etSubject = (EditText) findViewById(R.id.etSubject);
        etBody = (EditText) findViewById(R.id.etBody);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        etFrom.setText(email);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAsyncTask(getApplicationContext()).execute();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_mail, menu);
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

    private boolean checkIfFieldEmpty(EditText editText){
        if(TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError("Alan bos olamaz!");
            return false;
        }
        else
            return true;
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        Context context;
        public MyAsyncTask(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String s = postData(params);
            return s;
        }
        protected void onPostExecute(String result){

        }
        protected void onProgressUpdate(Integer... progress){

        }

        public String postData(String valueIWantToSend[]) {
            String returnValue = "";
            try{
                if (checkIfFieldEmpty(etTo)) {
                    final Service service = new Service(exchange, email, password);

                    Message message = new Message();
                    message.getToRecipients().add(new Mailbox(etTo.getText().toString()));
                    message.getCcRecipients().add(new Mailbox(etCc.getText().toString()));
                    message.setSubject(etSubject.getText().toString());
                    message.setBody(new Body(etBody.getText().toString()));

                    ItemInfoResponse response = service.send(message);
                    sendResponse = response.getMessage();

                    Intent intent = new Intent(getApplicationContext(), Inbox.class);
                    intent.putExtra("sendResponse", sendResponse);
                    startActivity(intent);
                }
            }catch (Exception ex){
                Log.v("Exception", ex.getMessage());
            }


            runOnUiThread(new Runnable() {
                public void run() {
                    checkIfFieldEmpty(etTo);
                }
            });

            return returnValue;
        }
    }
}
