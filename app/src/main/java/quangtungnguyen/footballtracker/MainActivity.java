package quangtungnguyen.footballtracker;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;

import quangtungnguyen.footballtracker.GoogleMap.NearbyStadium;
import quangtungnguyen.footballtracker.utils.TeamIconCallback;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the adapter that will return a fragment for each of the five
        // primary sections of the activity.

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        Home.getTeamIconUrls(this, "http://api.football-data.org/v2/competitions/2021/teams", new TeamIconCallback() {
            @Override
            public void onReturnTeamInfo(HashMap<String, ArrayList<String>> teamInfo) {
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), teamInfo);
                mViewPager.setAdapter(mSectionsPagerAdapter);
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


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

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        HashMap<String, ArrayList<String>> fullTeamInfo;

        public SectionsPagerAdapter(FragmentManager fm, HashMap<String, ArrayList<String>> teamInfo) {
            super(fm);
            this.fullTeamInfo = teamInfo;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putSerializable("data", this.fullTeamInfo);
            switch (position) {
                case 0:
                    Home home = new Home();
                    home.setArguments(args);
                    return home;
                case 1:
                    MyTeam myTeam = new MyTeam();
                    myTeam.setArguments(args);
                    return myTeam;
                case 2:
                    Tables tables = new Tables();
                    tables.setArguments(args);
                    return tables;

                case 3:
                    NearbyStadium stadium = new NearbyStadium();
                    stadium.setArguments(args);
                    return stadium;

            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
