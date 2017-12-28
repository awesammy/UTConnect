package utconnect.samapplab.com.utconnect;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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

    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference refServices = rootRef.child("ShopService");


    HashMap<String,Object> serviceData = new HashMap<String,Object>();

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#800080")));



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Going to Google Map", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        refServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                String address = dataSnapshot.child("UTM Second Cup").child("address").getValue(String.class);
                //Snackbar.make(findViewById(R.id.main_content),"Database loading...", Snackbar.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),dataSnapshot.getValue().toString(),Toast.LENGTH_LONG).show();

                serviceData = (HashMap<String, Object>) dataSnapshot.getValue();

            }

            @Override
            public void onCancelled(DatabaseError error){

            }
        });

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";



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
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1){
                View rootView = inflater.inflate(R.layout.fragment_viewservice, container, false);

                //List items
                ArrayList<String> services = new ArrayList<>();

                for (int i=0; i < 50; i++){
                    services.add("Add");

                }


                ListAdapter adapter = new CustomServiceAdaptor(getContext(),services,"Service Name","2.0","food","3353 Mississauga Road","4m");
                ListView listView = (ListView) rootView.findViewById(R.id.listview_service);
                listView.setAdapter(adapter);


                return rootView;
            }
            else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2){
                View rootView = inflater.inflate(R.layout.fragment_viewme, container, false);

                String[] services = {"One","Two","Three","Four","Five","Five"};
                ListAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,services);
                //ListView listView = (ListView) rootView.findViewById(R.id.listview_me);
                //listView.setAdapter(adapter);

                return rootView;
            }
            else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3){
                View rootView = inflater.inflate(R.layout.fragment_viewme, container, false);

                String[] services = {"One","Two","Three","Four","Five","Seven"};
                ListAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,services);
                //ListView listView = (ListView) rootView.findViewById(R.id.listview_me);
                //listView.setAdapter(adapter);

                return rootView;
            }
            else{
                View rootView = inflater.inflate(R.layout.fragment_viewservice, container, false);
                return rootView;
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
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Services";
                case 1:
                    return "About";
            }
            return null;
        }
    }
}


class CustomServiceAdaptor extends ArrayAdapter<String> {

    CustomServiceAdaptor(Context context, ArrayList<String> services, String serviceName,String serviceNumReport,String serviceType
    ,String serviceLocAddress,String serviceLastUpdate){
        super(context,R.layout.service_layout_item,services);

        this.serviceName = serviceName;
        this.serviceNumReport = serviceNumReport;
        this.serviceType = serviceType;
        this.serviceLocAddress = serviceLocAddress;
        this.serviceLastUpdate = serviceLastUpdate;
    }
    String serviceName = "";
    String serviceNumReport = "";
    String serviceType = "";
    String serviceLocAddress = "";
    String serviceLastUpdate = "";

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.service_layout_item,parent,false);
        }
        TextView textViewTitle = (TextView) convertView.findViewById(R.id.textView_item_service_title);
        TextView textViewServiceStatus = (TextView) convertView.findViewById(R.id.textView_service_statusCrowed);
        TextView textViewServiceDoorStatus = (TextView) convertView.findViewById(R.id.textView_service_statusDoor);
        TextView textViewServiceSpace = (TextView) convertView.findViewById(R.id.textView_service_space);
        TextView textViewServiceLocation = (TextView) convertView.findViewById(R.id.textView_service_address);
        TextView textViewServiceTime = (TextView) convertView.findViewById(R.id.textView_service_time);


        //Setup views
        textViewTitle.setText(this.serviceName);
        textViewServiceStatus.setText(this.serviceNumReport);
        textViewServiceLocation.setText(this.serviceLocAddress);
        textViewServiceTime.setText("Last Update: "+ serviceLastUpdate);

        switch (this.serviceNumReport){
            case "0.0":
                textViewServiceDoorStatus.setText("Closed");
                textViewServiceDoorStatus.setTextColor(Color.RED);
            case "1.0":
                textViewServiceDoorStatus.setText("Open");
                textViewServiceDoorStatus.setTextColor(Color.GREEN);
            case "2.0":
                textViewServiceDoorStatus.setText("Open");
                textViewServiceDoorStatus.setTextColor(Color.GREEN);
            case "3.0":
                textViewServiceDoorStatus.setText("Moderate");
                textViewServiceDoorStatus.setTextColor(Color.GREEN);
            case "4.0":
                textViewServiceDoorStatus.setText("Packed");
                textViewServiceDoorStatus.setTextColor(Color.YELLOW);
            case "5.0":
                textViewServiceDoorStatus.setText("Full");
                textViewServiceDoorStatus.setTextColor(Color.RED);
            default:
                System.out.print("default");
        }

        Button button_submit_feedback = (Button) convertView.findViewById(R.id.button_service_submit_feedback);

        button_submit_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Clicked Feedback",Toast.LENGTH_LONG).show();
            }
        });


        int totalHeight = textViewTitle.getHeight() + textViewServiceStatus.getHeight() + textViewServiceLocation.getHeight() + textViewServiceTime.getHeight() + button_submit_feedback.getHeight() + 200;

        convertView.setMinimumHeight(totalHeight);
        return convertView;
    }


}
