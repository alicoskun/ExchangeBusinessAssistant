package ali.myemail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.independentsoft.exchange.Service;

public class Login extends ActionBarActivity {

    EditText etExchange;
    EditText etEmail;
    EditText etPassword;
    TextView tvError;
    Button btnLogin;
    Button btnChangeCre;
    SharedPreferences preferences;//preferences referansý
    SharedPreferences.Editor editor; //preferences editor nesnesi referansý .prefernces nesnesine veri ekleyip cýkarmak için


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etExchange = (EditText) findViewById(R.id.etExchange);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvError = (TextView) findViewById(R.id.tvError);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnChangeCre = (Button) findViewById(R.id.btnChangeCre);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);  // Preferences objesi oluþturuluyor
        editor = preferences.edit();                                        // Ayný þekilde editor nesnesi oluþturuluyor


        try {
            btnChangeCre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etExchange.setText("https://outlook.office365.com/EWS/Exchange.asmx");
                    etEmail.setText("email@provider.com");
                    etPassword.setText("password");
                }
            });

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkIfFieldEmpty(etExchange) && checkIfFieldEmpty(etEmail) && checkIfFieldEmpty(etPassword)){
                        Service service = new Service(etExchange.getText().toString(),
                                etEmail.getText().toString(),
                                etPassword.getText().toString());

                        editor.putString("Exchange", etExchange.getText().toString());
                        editor.putString("Email", etEmail.getText().toString());
                        editor.putString("Password", etPassword.getText().toString());
                        editor.commit();

                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(intent);
                    }
            }});
        }
        catch (Exception ex) {
                Log.v("Inception", ":" + ex.getMessage());
                tvError.setText(ex.getMessage().toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    /*
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

            try {
                final Service service = new Service(etExchange.getText().toString(),
                        etEmail.getText().toString(),
                        etPassword.getText().toString());

                service.findFolder(StandardFolder.INBOX);
                runOnUiThread(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("Service", (Serializable) service);
                        startActivity(intent);
                    }
                });
            } catch (final ServiceException ex) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.v("Inception", ":" + ex.getMessage());
                        tvError.setText(ex.getMessage().toString());
                    }
                });
            }

            return returnValue;
        }
    }*/
}
