package utconnect.samapplab.com.utconnect;

import android.*;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.*;

import org.w3c.dom.Text;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class TabbedActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    FloatingActionButton fab;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        //Disable Toolbar
        toolbar.setVisibility(View.GONE);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#800080")));


        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fAuth.getCurrentUser() != null){
                    Intent intent = new Intent(getApplicationContext(),AccountActivity.class);
                    startActivity(intent);
                }
                else{
                    handleLogin();
                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
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



    public void handleLogin(){
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(TabbedActivity.this);
        aBuilder.setTitle("Login/Register");
        aBuilder.setMessage("You can login or create your account with your UofT email and new password");
        aBuilder.setCancelable(true)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNeutralButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alertDialog = aBuilder.create();
        alertDialog.show();
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


        private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        private DatabaseReference refServices = rootRef.child("ShopService");
        private DatabaseReference refContacts = rootRef.child("Services");

        HashMap<String,Object> serviceData = new HashMap<String,Object>();
        HashMap<String,Object> contactData = new HashMap<String,Object>();

        ArrayList<Service> services = new ArrayList<>();
        ArrayList<Contact> contacts = new ArrayList<>();
        ListView listView;
        ListView contactListView;


        public PlaceholderFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1){
                final View rootView = inflater.inflate(R.layout.fragment_viewservice, container, false);

                final ArrayAdapter adapter = new CustomServiceAdaptor(getContext(),R.layout.service_layout_item,services);
                listView = (ListView) rootView.findViewById(R.id.listview_service);
                listView.setAdapter(adapter);




                final String campusSelected = "UTM";
                final Button button_changeCampus = (Button) rootView.findViewById(R.id.button_service_change_campus);
                button_changeCampus.setText(campusSelected);

                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                                && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() -
                                listView.getFooterViewsCount()) >= (adapter.getCount() - 1)) {
                            // Now your listview has hit the bottom
                            button_changeCampus.setVisibility(View.GONE);
                        }
                        else{
                            button_changeCampus.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    }
                });



                button_changeCampus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder aBuilder = new AlertDialog.Builder(getContext());
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        View layoutView = inflater.inflate(R.layout.layout_change_campus,null);

                        aBuilder.setView(layoutView);
                        final AlertDialog alertDialog = aBuilder.create();

                        Button button_campus_utm = (Button) layoutView.findViewById(R.id.button_alert_campus_utm);
                        Button button_campus_utsg = (Button) layoutView.findViewById(R.id.button_alert_campus_utsg);
                        Button button_campus_utsc = (Button) layoutView.findViewById(R.id.button_alert_campus_utsc);

                        button_campus_utm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                button_changeCampus.setText("UTM");
                                functionReloading(button_changeCampus,adapter);
                                alertDialog.cancel();

                            }
                        });
                        button_campus_utsg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                button_changeCampus.setText("UTSG");
                                functionReloading(button_changeCampus,adapter);
                                alertDialog.cancel();
                            }
                        });
                        button_campus_utsc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                button_changeCampus.setText("UTSC");
                                functionReloading(button_changeCampus,adapter);
                                alertDialog.cancel();
                            }
                        });

                        alertDialog.show();
                    }
                });

                refServices.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot){

                        serviceData = new HashMap<String,Object>();
                        serviceData = (HashMap<String, Object>) dataSnapshot.getValue();
                        System.out.println(serviceData.toString());
                        if (button_changeCampus != null && adapter != null){
                            functionReloading(button_changeCampus,adapter);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error){

                    }
                });


                return rootView;
            }
            else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3){
                View rootView = inflater.inflate(R.layout.fragment_viewme, container, false);


                TextView titleText = (TextView) rootView.findViewById(R.id.textView_me_title);
                TextView introText = (TextView) rootView.findViewById(R.id.textView_me_intro);
                Button buttonContact = (Button) rootView.findViewById(R.id.button_me_contactdev);
                Button buttonUpdate = (Button) rootView.findViewById(R.id.button_me_checkupdate);
                Button buttonRate = (Button) rootView.findViewById(R.id.button_me_rateus);



                introText.setText("UTConnect: Minimal Viable Product for Android." +
                        "\nVersion: 0.5.2\nFrom: UTConnect Team\n\nWe provide service crowd reports for " +
                        "services in every UofT campus.");
                //introText.setTextColor(Color.parseColor("##A9A9A9"));

                buttonContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder aBuilder = new AlertDialog.Builder(getContext());


                        aBuilder.setTitle("Contact Developer");
                        aBuilder.setMessage("Team UTConnect\n\nEmail: samapplab070@gmail.com");
                        aBuilder.setCancelable(true)
                                .setPositiveButton("Send Email", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"+"samapplab070@gmail.com"));
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Developer");
                                        intent.putExtra(Intent.EXTRA_TEXT, "Hello Team UTConnect\n...");

                                        startActivity(Intent.createChooser(intent, "Send Email"));

                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .setNeutralButton("More Information", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                                        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=utconnect.samapplab.com.utconnect"));
                                        Intent chooser = Intent.createChooser(i,"Go To Play Store");
                                        startActivity(chooser);

                                        dialog.cancel();
                                    }
                                });
                        AlertDialog dialog = aBuilder.create();
                        dialog.show();

                    }
                });
                buttonUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=utconnect.samapplab.com.utconnect"));
                        Intent chooser = Intent.createChooser(i,"Go To Play Store");
                        startActivity(chooser);

                    }
                });
                buttonRate.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=utconnect.samapplab.com.utconnect"));
                        Intent chooser = Intent.createChooser(i,"Go To Play Store");
                        startActivity(chooser);
                    }

                });



                return rootView;
            }
            else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2){
                View rootView = inflater.inflate(R.layout.fragment_viewcontacts, container, false);

                final String campusSelected = "UTM";
                final Button button_changeCampus = (Button) rootView.findViewById(R.id.button_contact_change_campus);
                button_changeCampus.setText(campusSelected);


                contactListView = (ListView) rootView.findViewById(R.id.listview_contacts);

                final ContactAdaptor contactAdaptor = new ContactAdaptor(getContext(),contacts);
                contactListView.setAdapter(contactAdaptor);

                contactListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                                && (contactListView.getLastVisiblePosition() - contactListView.getHeaderViewsCount() -
                                contactListView.getFooterViewsCount()) >= (contactAdaptor.getCount() - 1)) {
                            // Now your listview has hit the bottom
                            button_changeCampus.setVisibility(View.GONE);
                        }
                        else{
                            button_changeCampus.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    }
                });



                button_changeCampus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder aBuilder = new AlertDialog.Builder(getContext());
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        View layoutView = inflater.inflate(R.layout.layout_change_campus,null);

                        aBuilder.setView(layoutView);
                        final AlertDialog alertDialog = aBuilder.create();

                        Button button_campus_utm = (Button) layoutView.findViewById(R.id.button_alert_campus_utm);
                        Button button_campus_utsg = (Button) layoutView.findViewById(R.id.button_alert_campus_utsg);
                        Button button_campus_utsc = (Button) layoutView.findViewById(R.id.button_alert_campus_utsc);

                        button_campus_utm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                button_changeCampus.setText("UTM");
                                contactReloading(button_changeCampus,contactAdaptor);
                                alertDialog.cancel();

                            }
                        });
                        button_campus_utsg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                button_changeCampus.setText("UTSG");
                                contactReloading(button_changeCampus,contactAdaptor);
                                alertDialog.cancel();
                            }
                        });
                        button_campus_utsc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                button_changeCampus.setText("UTSC");
                                contactReloading(button_changeCampus,contactAdaptor);
                                alertDialog.cancel();
                            }
                        });

                        alertDialog.show();
                    }
                });



                refContacts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        contactData = new HashMap<String,Object>();
                        contactData = (HashMap<String, Object>) dataSnapshot.getValue();
                        contactReloading(button_changeCampus,contactAdaptor);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                return rootView;
            }
            else{
                View rootView = inflater.inflate(R.layout.fragment_viewservice, container, false);
                return rootView;
            }

        }

        private void functionReloading(final Button buttonChange, final ArrayAdapter adapter){
            services.clear();
            adapter.notifyDataSetChanged();

            for (Object ser : serviceData.values()) {
                HashMap<String, Object> serviceItem = (HashMap<String, Object>) ser;
                String sName = "";
                String sAddress = "";
                String sTime = "";
                String sCampus = "";
                String sStatusCode = "";
                String sType = "";
                String latitude = "";
                String longitude = "";
                String sidName = "";
                if (serviceItem.get("name") != null) {
                    sName = (String) serviceItem.get("name");
                }
                if (serviceItem.get("address") != null) {
                    sAddress = (String) serviceItem.get("address");
                }
                if (serviceItem.get("timestamp") != null) {
                    Long timestamp = (Long) serviceItem.get("timestamp");
                    Long secondTarget = timestamp / 1000;
                    Long secondNow = Calendar.getInstance().getTimeInMillis() / 1000;
                    Long timeDifference = secondNow - secondTarget;

                    if (timeDifference < 86400) {
                        sTime = "today";
                    } else if (timeDifference >= 86400 && timeDifference < 86400 * 2) {
                        sTime = "yesterday";
                    } else if (timeDifference >= 86400 * 2 && timeDifference < 86400 * 3) {
                        sTime = "2 days ago";
                    } else {
                        sTime = "more than 2 days";
                    }

                }
                if (serviceItem.get("campus") != null) {
                    sCampus = (String) serviceItem.get("campus");
                }
                if (serviceItem.get("number") != null) {
                    sStatusCode = (String) serviceItem.get("number");
                }
                if (serviceItem.get("type") != null) {
                    sType = (String) serviceItem.get("type");
                }
                if (serviceItem.get("location") != null) {
                    HashMap<String, String> location = (HashMap<String, String>) serviceItem.get("location");
                    if (location.get("latitude") != null) {
                        latitude = location.get("latitude");
                    }
                    if (location.get("longitude") != null) {
                        longitude = location.get("longitude");
                    }

                }
                if (serviceItem.get("idName") != null){
                    sidName = (String) serviceItem.get("idName");
                }
                if (buttonChange.getText().equals(sCampus)) {
                    Service serviceModel = new Service(sName, sTime, sType, sAddress,
                            "", sStatusCode, "",
                            longitude, latitude,sCampus,sidName);
                    services.add(serviceModel);
                }
            }

            if (services.isEmpty()){
                Snackbar.make(getView(),"Oops, we tried loading, but no service found here\nTap to reload...",Snackbar.LENGTH_LONG).setAction("", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        functionReloading(buttonChange,adapter);
                    }
                }).show();
            }

        }


        private void contactReloading(final Button button, final BaseAdapter adapter){
            contacts.clear();
            adapter.notifyDataSetChanged();
            for (String key: contactData.keySet()){
                HashMap<String,Object> eachContact = (HashMap<String, Object>) contactData.get(key);
                String contactID = key;

                String contactEmail = "";
                String contactPhone = "";
                String contactWebsite = "";
                String contactCategory = "";
                String contactCampus = "";

                if (eachContact.get("email") != null){
                    contactEmail = (String) eachContact.get("email");
                }
                if (eachContact.get("phone") != null){
                    contactPhone = (String) eachContact.get("phone");
                }
                if (eachContact.get("website") != null){
                    contactWebsite = (String) eachContact.get("website");
                }
                if (eachContact.get("category") != null){
                    contactCategory = (String) eachContact.get("category");
                }
                if (eachContact.get("campus") != null){
                    contactCampus = (String) eachContact.get("campus");
                }

                if (contactCampus.equals(button.getText().toString())){
                    Contact contact = new Contact(contactID,contactID,contactCategory,
                            "", "","",
                            contactEmail,contactPhone,contactWebsite,contactCampus);
                    contacts.add(contact);
                }

            }

            if (contacts.isEmpty()){
                Snackbar.make(getView(),"Oops, we tried loading, but no contact found here\nTap to reload...",Snackbar.LENGTH_LONG).setAction("", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contactReloading(button,adapter);
                    }
                }).show();
            }

        }


    }







    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Services";
                case 1:
                    return "Contacts";
                case 2:
                    return "About";
            }
            return null;
        }
    }
}


class CustomServiceAdaptor extends ArrayAdapter<Service> {

    private Context mContext;
    int mResource;
    CustomServiceAdaptor(Context context, int resource, ArrayList<Service> services){
        super(context,R.layout.service_layout_item,services);
        mContext = context;
        mResource = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final String name = getItem(position).getServiceName();
        String status = getItem(position).getServiceStatus();
        String statusCode = getItem(position).getServiceStatusCode();
        String time = getItem(position).getServiceTime();
        String address = getItem(position).getServiceAddress();
        String type = getItem(position).getServiceType();
        String imageurl = getItem(position).getServiceImageUrl();
        final String idName = getItem(position).getServiceID();

        final String longitude = getItem(position).getServiceLongitude();
        final String latitude = getItem(position).getServiceLatitude();
        final String campus = getItem(position).getServiceCampus();

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource,parent,false);
            //convertView = inflater.inflate(R.layout.service_layout_item,parent,false);
        }
        TextView textViewTitle = (TextView) convertView.findViewById(R.id.textView_item_service_title);
        TextView textViewServiceStatus = (TextView) convertView.findViewById(R.id.textView_service_statusCrowed);
        TextView textViewServiceDoorStatus = (TextView) convertView.findViewById(R.id.textView_service_statusDoor);
        TextView textViewServiceSpace = (TextView) convertView.findViewById(R.id.textView_service_space);
        TextView textViewServiceLocation = (TextView) convertView.findViewById(R.id.textView_service_address);
        TextView textViewServiceTime = (TextView) convertView.findViewById(R.id.textView_service_time);
        TextView textViewServiceCampus = (TextView) convertView.findViewById(R.id.textView_service_campus);



        //Setup views
        textViewTitle.setText(name);
        textViewServiceStatus.setText(statusCode);
        textViewServiceLocation.setText(address);
        textViewServiceCampus.setText(campus);

        textViewServiceTime.setText("Last Update: "+ time);

        if (statusCode.equals("0.0")){
            textViewServiceDoorStatus.setText("Closed");
            textViewServiceDoorStatus.setTextColor(Color.RED);
        }
        else if (statusCode.equals("1.0")){
            textViewServiceDoorStatus.setText("Open");
            textViewServiceDoorStatus.setTextColor(Color.GREEN);
        }
        else if (statusCode.equals("2.0")){
            textViewServiceDoorStatus.setText("Open");
            textViewServiceDoorStatus.setTextColor(Color.GREEN);
        }
        else if (statusCode.equals("3.0")){
            textViewServiceDoorStatus.setText("Normal");
            textViewServiceDoorStatus.setTextColor(Color.GREEN);
        }
        else if (statusCode.equals("4.0")){
            textViewServiceDoorStatus.setText("Moderate");
            textViewServiceDoorStatus.setTextColor(Color.YELLOW);
        }
        else if (statusCode.equals("5.0")){
            textViewServiceDoorStatus.setText("Busy");
            textViewServiceDoorStatus.setTextColor(Color.RED);
        }
        else{
            textViewServiceDoorStatus.setText("Unknown");
            textViewServiceDoorStatus.setTextColor(Color.BLACK);
            System.out.print("default");
        }

        Button button_submit_feedback = (Button) convertView.findViewById(R.id.button_service_submit_feedback);
        Button button_get_location = (Button) convertView.findViewById(R.id.button_service_goto_location);

        final String serviceName = name;
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference refServices = rootRef.child("ShopService");

        button_submit_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"Clicked Feedback",Toast.LENGTH_LONG).show();

                //showDialog("You are crowd reporting",serviceName);
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View layoutView = inflater.inflate(R.layout.layout_crowd_reporting,null);

                aBuilder.setView(layoutView);
                final AlertDialog alertDialog = aBuilder.create();

                TextView textView_introduct = (TextView) layoutView.findViewById(R.id.textView_alert_content);
                textView_introduct.setText("You are now crowd reporting the "+name+" at "+campus);

                Button button_close = (Button) layoutView.findViewById(R.id.button_alert_service0);
                Button button_one = (Button) layoutView.findViewById(R.id.button_alert_service1);
                Button button_two = (Button) layoutView.findViewById(R.id.button_alert_service2);
                Button button_three = (Button) layoutView.findViewById(R.id.button_alert_service3);
                Button button_four = (Button) layoutView.findViewById(R.id.button_alert_service4);
                Button button_five = (Button) layoutView.findViewById(R.id.button_alert_service5);

                button_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!idName.equals("")){

                            Random r = new Random();
                            int i1 = r.nextInt(999999999);
                            HashMap<String, Object> valueAdded = new HashMap<>();
                            String displayName = "Android User";
                            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                                displayName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                            }
                            valueAdded.put("from",displayName);
                            valueAdded.put("number","0.0");
                            valueAdded.put("timestamp",ServerValue.TIMESTAMP);

                            refServices.child(idName).child("number").setValue("0.0");
                            refServices.child(idName).child("timestamp").setValue(ServerValue.TIMESTAMP);
                            refServices.child(idName).child("logs").child(String.valueOf(i1)).setValue(valueAdded);
                        }
                        alertDialog.cancel();
                        Toast.makeText(mContext,"Thank you for reporting! :)You Are Awesome!!!",Toast.LENGTH_LONG).show();
                    }
                });

                button_one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!idName.equals("")){

                            Random r = new Random();
                            int i1 = r.nextInt(999999999);
                            HashMap<String, Object> valueAdded = new HashMap<>();
                            valueAdded.put("from","Android User");
                            valueAdded.put("number","1.0");
                            valueAdded.put("timestamp",ServerValue.TIMESTAMP);

                            refServices.child(idName).child("number").setValue("1.0");
                            refServices.child(idName).child("timestamp").setValue(ServerValue.TIMESTAMP);
                            refServices.child(idName).child("logs").child(String.valueOf(i1)).setValue(valueAdded);
                        }
                        alertDialog.cancel();
                        Toast.makeText(mContext,"Thank you for reporting! :)You Are Awesome!!!",Toast.LENGTH_LONG).show();
                    }
                });

                button_two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!idName.equals("")){

                            Random r = new Random();
                            int i1 = r.nextInt(999999999);
                            HashMap<String, Object> valueAdded = new HashMap<>();
                            valueAdded.put("from","Android User");
                            valueAdded.put("number","2.0");
                            valueAdded.put("timestamp",ServerValue.TIMESTAMP);

                            refServices.child(idName).child("number").setValue("2.0");
                            refServices.child(idName).child("timestamp").setValue(ServerValue.TIMESTAMP);
                            refServices.child(idName).child("logs").child(String.valueOf(i1)).setValue(valueAdded);
                        }
                        alertDialog.cancel();
                        Toast.makeText(mContext,"Thank you for reporting! :)You Are Awesome!!!",Toast.LENGTH_LONG).show();
                    }
                });

                button_three.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!idName.equals("")){

                            Random r = new Random();
                            int i1 = r.nextInt(999999999);
                            HashMap<String, Object> valueAdded = new HashMap<>();
                            valueAdded.put("from","Android User");
                            valueAdded.put("number","3.0");
                            valueAdded.put("timestamp",ServerValue.TIMESTAMP);

                            refServices.child(idName).child("number").setValue("3.0");
                            refServices.child(idName).child("timestamp").setValue(ServerValue.TIMESTAMP);
                            refServices.child(idName).child("logs").child(String.valueOf(i1)).setValue(valueAdded);
                        }
                        alertDialog.cancel();
                        Toast.makeText(mContext,"Thank you for reporting! :)You Are Awesome!!!",Toast.LENGTH_LONG).show();
                    }
                });

                button_four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!idName.equals("")){

                            Random r = new Random();
                            int i1 = r.nextInt(999999999);
                            HashMap<String, Object> valueAdded = new HashMap<>();
                            valueAdded.put("from","Android User");
                            valueAdded.put("number","4.0");
                            valueAdded.put("timestamp",ServerValue.TIMESTAMP);

                            refServices.child(idName).child("number").setValue("4.0");
                            refServices.child(idName).child("timestamp").setValue(ServerValue.TIMESTAMP);
                            refServices.child(idName).child("logs").child(String.valueOf(i1)).setValue(valueAdded);
                        }
                        alertDialog.cancel();
                        Toast.makeText(mContext,"Thank you for reporting! :)You Are Awesome!!!",Toast.LENGTH_LONG).show();
                    }
                });

                button_five.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!idName.equals("")){

                            Random r = new Random();
                            int i1 = r.nextInt(999999999);
                            HashMap<String, Object> valueAdded = new HashMap<>();
                            valueAdded.put("from","Android User");
                            valueAdded.put("number","5.0");
                            valueAdded.put("timestamp",ServerValue.TIMESTAMP);

                            refServices.child(idName).child("number").setValue("5.0");
                            refServices.child(idName).child("timestamp").setValue(ServerValue.TIMESTAMP);
                            refServices.child(idName).child("logs").child(String.valueOf(i1)).setValue(valueAdded);
                        }
                        alertDialog.cancel();
                        Toast.makeText(mContext,"Thank you for reporting! :)You Are Awesome!!!",Toast.LENGTH_LONG).show();
                    }
                });
                //View from alert





                alertDialog.show();

            }
        });


        button_get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!latitude.equals("") && !longitude.equals("")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("geo:"+latitude+","+longitude));
                    Intent chooser = Intent.createChooser(intent,"Load Map?");
                    mContext.startActivity(chooser);
                }
                else{
                    Toast.makeText(mContext,"Location not found",Toast.LENGTH_LONG).show();
                }

            }
        });


        //int totalHeight = textViewTitle.getHeight() + textViewServiceStatus.getHeight() + textViewServiceLocation.getHeight() + textViewServiceTime.getHeight() + button_submit_feedback.getHeight() + 200;

        //convertView.setMinimumHeight(totalHeight);
        return convertView;
    }


}
