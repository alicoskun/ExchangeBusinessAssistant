package ali.myemail;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ali on 28.11.2015.
 */
public class CustomBaseAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<EmailMessage> mailList;

    public CustomBaseAdapter(Context context, ArrayList<EmailMessage> mailList){

        inflater = (LayoutInflater) LayoutInflater.from(context);
        this.mailList = mailList;
    }

    @Override
    public int getCount() {
        return mailList.size();
    }

    @Override
    public EmailMessage getItem(int position) {
        return mailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item, null);

        ImageView ivIsRead = (ImageView) convertView.findViewById(R.id.ivIsRead);
        TextView tvFrom = (TextView) convertView.findViewById(R.id.tvFrom);
        TextView tvSubject = (TextView) convertView.findViewById(R.id.tvSubject);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);

        EmailMessage message = mailList.get(position);

        ivIsRead.setImageResource(R.drawable.read);
        tvFrom.setText(message.getFrom());
        tvSubject.setText(message.getSubject().length() > 30 ? message.getSubject().substring(0, 30) + "..." : message.getSubject());
        tvDate.setText(message.getReceivedTime().substring(0, 6));

        if (!message.getIsRead()) {
            ivIsRead.setImageResource(R.drawable.unread);
            tvFrom.setTypeface(null, Typeface.BOLD);
            tvSubject.setTypeface(null, Typeface.BOLD);
        }

        return convertView;
    }
}
