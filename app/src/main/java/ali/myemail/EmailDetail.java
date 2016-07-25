package ali.myemail;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class EmailDetail extends MainActivity {

    TextView tvFrom;
    TextView tvCc;
    TextView tvSubject;
    TextView tvDate;
    TextView tvBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_email_detail);
        getLayoutInflater().inflate(R.layout.activity_email_detail, frameLayout);

        tvFrom = (TextView)findViewById(R.id.tvFrom);
        tvCc = (TextView)findViewById(R.id.tvCc);
        tvSubject = (TextView)findViewById(R.id.tvSubject);
        tvDate = (TextView)findViewById(R.id.tvDate);
        tvBody = (TextView)findViewById(R.id.tvBody);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            tvFrom.setText(extras.getString("From"));
            tvCc.setText(extras.getString("Cc"));
            tvSubject.setText(extras.getString("Subject"));
            tvDate.setText(extras.getString("Date"));
            tvBody.setText(extras.getString("Body"));

            getSupportActionBar().setTitle(extras.getString("Subject"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_email_detail, menu);
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
}
