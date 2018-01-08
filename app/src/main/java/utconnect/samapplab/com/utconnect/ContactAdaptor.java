package utconnect.samapplab.com.utconnect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.security.Permission;
import java.util.List;

/**
 * Created by samchen on 2018-01-06.
 */

public class ContactAdaptor extends BaseAdapter {

    private Context mContext;
    private List<Contact> contactList;


    String contactID;
    String contactAddress;
    String contactEmail;
    String contactPhone;
    String contactWebsite;

    public ContactAdaptor(Context mContext, List<Contact> contactList) {
        this.contactList = contactList;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.contact_layout_item, null);

        TextView titleView = (TextView) v.findViewById(R.id.textView_contact_title);
        TextView campusView = (TextView) v.findViewById(R.id.textView_contact_campus);
        TextView typeView = (TextView) v.findViewById(R.id.textView_contact_type);


        final TextView emailView = (TextView) v.findViewById(R.id.textView_contact_email_address);
        final TextView phoneView = (TextView) v.findViewById(R.id.textView_contact_phone_number);
        final TextView websiteView = (TextView) v.findViewById(R.id.textView_contact_website);
        Button buttonCall = (Button) v.findViewById(R.id.button_contact_call_phone);
        Button buttonEmail = (Button) v.findViewById(R.id.button_contact_send_email);
        Button buttonWebsite = (Button) v.findViewById(R.id.button_contact_visit_website);


        Button buttonAction = (Button) v.findViewById(R.id.button_contact_add_favourite);
        buttonAction.setVisibility(View.GONE);


        LinearLayout emailLayout = (LinearLayout) v.findViewById(R.id.linearLayout_contact_email);
        LinearLayout phoneLayout = (LinearLayout) v.findViewById(R.id.linearLayout_contact_phone);
        LinearLayout webLayout = (LinearLayout) v.findViewById(R.id.linearLayout_contact_website);


        //Information Display
        String contactCampus = contactList.get(position).getContactCampus();
        String contactType = contactList.get(position).getContactType();
        final String contactName = contactList.get(position).getContactName();

        //At backend
        contactID = contactList.get(position).getContactID();
        contactAddress = contactList.get(position).getContactAddress();
        contactEmail = contactList.get(position).getContactEmail();
        contactPhone = contactList.get(position).getContactPhone();
        contactWebsite = contactList.get(position).getContactWebURL();


        titleView.setText(contactName);
        campusView.setText(contactCampus);
        typeView.setText(contactType);

        if (contactPhone.equals("none")) {
            phoneLayout.setVisibility(View.GONE);
        } else {
            phoneView.setText(contactPhone);
        }
        if (contactWebsite.equals("none")) {
            webLayout.setVisibility(View.GONE);
        } else {
            websiteView.setText(contactWebsite);
        }
        if (contactEmail.equals("none")) {
            emailLayout.setVisibility(View.GONE);
        } else {
            emailView.setText(contactEmail);
        }


        //Button Action
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phoneView.getText().toString().equals("")) {
                    final Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneView.getText().toString()));

                    android.support.v7.app.AlertDialog.Builder aBuilder = new android.support.v7.app.AlertDialog.Builder(mContext);
                    aBuilder.setTitle("Calling Number?");
                    aBuilder.setMessage("You about to call " + contactName + " at\n" + phoneView.getText().toString());
                    aBuilder.setCancelable(true)
                            .setPositiveButton("Call Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        mContext.startActivity(intent);
                                        return;
                                    }
                                    else{
                                        Toast.makeText(mContext,"Unable to call, please revise the permission setting",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    android.support.v7.app.AlertDialog dialog = aBuilder.create();
                    dialog.show();


                }
                else{
                    Toast.makeText(mContext,"Unable to call, number not found",Toast.LENGTH_LONG).show();
                }
            }
        });
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailView.getText().toString().equals("")){
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+emailView.getText().toString()));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    mContext.startActivity(Intent.createChooser(intent, "Send Email"));
                }

            }
        });
        buttonWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse(websiteView.getText().toString()));
                Intent chooser = Intent.createChooser(i,contactID);
                mContext.startActivity(chooser);
            }
        });





        return v;
    }
}
