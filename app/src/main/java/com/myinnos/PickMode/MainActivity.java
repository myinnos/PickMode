package com.myinnos.PickMode;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.myinnos.PickMode.adapters.ViewPagerAdapter;
import com.myinnos.PickMode.gallery.GalleryPickerFragment;
import com.myinnos.PickMode.models.SourceType;
import com.myinnos.PickMode.modules.PermissionModule;
import com.myinnos.PickMode.photo.CapturePhotoFragment;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by myinnos on 07/09/17.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mMainTabLayout)
    TabLayout mMainTabLayout;
    @BindView(R.id.mMainViewPager)
    ViewPager mMainViewPager;

    @BindString(R.string.tab_gallery)
    String _tabGallery;
    @BindString(R.string.tab_photo)
    String _tabPhoto;
    @BindString(R.string.tab_video)
    String _tabVideo;

    private HashSet<SourceType> mSourceTypeSet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        PermissionModule permissionModule = new PermissionModule(this);
        permissionModule.checkPermissions();

        mSourceTypeSet.add(SourceType.Gallery);
        mSourceTypeSet.add(SourceType.Photo);
        mSourceTypeSet.add(SourceType.Video);


        final ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getListFragment());
        mMainViewPager.setAdapter(pagerAdapter);

        mMainTabLayout.addOnTabSelectedListener(getViewPagerOnTabSelectedListener());
        mMainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mMainTabLayout));
        mMainViewPager.setCurrentItem(2);

    }

    private TabLayout.ViewPagerOnTabSelectedListener getViewPagerOnTabSelectedListener() {
        return new TabLayout.ViewPagerOnTabSelectedListener(mMainViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }
        };
    }

    private ArrayList<Fragment> getListFragment() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        if (mSourceTypeSet.contains(SourceType.Gallery)) {
            fragments.add(GalleryPickerFragment.newInstance());
            mMainTabLayout.addTab(mMainTabLayout.newTab().setText(_tabGallery));
        }

        if (mSourceTypeSet.contains(SourceType.Photo)) {
            fragments.add(CapturePhotoFragment.newInstance());
            mMainTabLayout.addTab(mMainTabLayout.newTab().setText(_tabPhoto));
        }

        if (mSourceTypeSet.contains(SourceType.Video)) {
            //fragments.add(CaptureVideoFragment.newInstance());
            mMainTabLayout.addTab(mMainTabLayout.newTab().setText(_tabVideo));
        }

        return fragments;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
