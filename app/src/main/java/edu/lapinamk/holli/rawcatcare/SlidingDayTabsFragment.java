/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package edu.lapinamk.holli.rawcatcare;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* Class based on a sample of the Android Open Source Project */
public class SlidingDayTabsFragment extends Fragment
{

    OnDaySelectedListener mDayListener;
    private SlidingTabLayout mSlidingTabLayout;

    private ViewPager mViewPager;

    private String[] daysOfWeek;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    @Override
    public void onAttach(Activity activity)
    {

        super.onAttach(activity);
        try
        {
            mDayListener = (OnDaySelectedListener) activity;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement OnDaySelectedListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        String day = PreferencesHelper.getCurrentDay(view.getContext());

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new DayPagerAdapter());

        if(day != null)
            mViewPager.setCurrentItem(PreferencesHelper.getWeekdayNumber(day));


        daysOfWeek = view.getResources().getStringArray(R.array.days_of_week);



        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);


        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                mDayListener.onDaySelected(position, daysOfWeek[position]);


            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

    }

    public interface OnDaySelectedListener
    {
        void onDaySelected(int index, String day);
    }

    class DayPagerAdapter extends PagerAdapter
    {
        @Override
        public int getCount()
        {
            return 7;
        }


        @Override
        public boolean isViewFromObject(View view, Object o)
        {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return daysOfWeek[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {

            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }

    }
}
