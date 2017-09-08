package com.myinnos.PickMode.gallery;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myinnos.PickMode.R;
import com.myinnos.PickMode.gallery.modules.LoadMoreModule;
import com.myinnos.PickMode.gallery.modules.LoadMoreModuleDelegate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by myinnos on 07/09/17.
 */

public class GalleryPickerFragment extends Fragment implements LoadMoreModuleDelegate {

    @BindView(R.id.mGalleryRecyclerView)
    RecyclerView mGalleryRecyclerView;

    private static final String EXTENSION_JPG = ".jpg";
    private static final String EXTENSION_JPEG = ".jpeg";
    private static final String EXTENSION_PNG = ".png";
    private static final int MARGING_GRID = 2;
    private static final int RANGE = 20;

    private LoadMoreModule mLoadMoreModule = new LoadMoreModule();
    private GridAdapter mGridAdapter;
    private ArrayList<File> mFiles;
    private boolean isLoading = false;
    private int mOffset;
    private boolean isFirstLoad = true;

    public static GalleryPickerFragment newInstance() {
        return new GalleryPickerFragment();
    }

    private void initViews() {
        if (isFirstLoad) {
            mGridAdapter = new GridAdapter(getContext());
        }
        mGalleryRecyclerView.setAdapter(mGridAdapter);
        mGalleryRecyclerView.setHasFixedSize(true);
        mGalleryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mGalleryRecyclerView.addItemDecoration(addItemDecoration());
        mLoadMoreModule.LoadMoreUtils(mGalleryRecyclerView, this, getContext());
        mOffset = 0;
        fetchMedia();
    }

    private RecyclerView.ItemDecoration addItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view,
                                       RecyclerView parent, RecyclerView.State state) {
                outRect.left = MARGING_GRID;
                outRect.right = MARGING_GRID;
                outRect.bottom = MARGING_GRID;
                if (parent.getChildLayoutPosition(view) >= 0 && parent.getChildLayoutPosition(view) <= 3) {
                    outRect.top = MARGING_GRID;
                }
            }
        };
    }

    private void fetchMedia() {
        mFiles = new ArrayList<>();
        File dirDownloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        parseDir(dirDownloads);
        File dirDcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        parseDir(dirDcim);
        File dirPictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        parseDir(dirPictures);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            File dirDocuments = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            parseDir(dirDocuments);
        }

        if (mFiles.size() > 0) {
            mGridAdapter.setItems(getRangePets());
        }
        isFirstLoad = false;
    }

    private List<File> getRangePets() {
        if (mOffset < mFiles.size()) {
            if ((mOffset + RANGE) < mFiles.size()) {
                return mFiles.subList(mOffset, mOffset + RANGE);
            } else if ((mOffset + RANGE) >= mFiles.size()) {
                return mFiles.subList(mOffset, mFiles.size());
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    private void parseDir(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            parseFileList(files);
        }
    }

    private void parseFileList(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                if (!file.getName().toLowerCase().startsWith(".")) {
                    parseDir(file);
                }
            } else {
                if (file.getName().toLowerCase().endsWith(EXTENSION_JPG)
                        || file.getName().toLowerCase().endsWith(EXTENSION_JPEG)
                        || file.getName().toLowerCase().endsWith(EXTENSION_PNG)) {
                    mFiles.add(file);
                }
            }
        }
    }

    private void loadNext() {
        if (!isLoading) {
            isLoading = true;
            mOffset += RANGE;
            List<File> files = new ArrayList<>();
            files.addAll(getRangePets());
            if (files.size() > 0) {
                mGridAdapter.addItems(files, mGridAdapter.getItemCount());
            }
            isLoading = false;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_gallery_view, container, false);
        ButterKnife.bind(this, v);
        initViews();
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void shouldLoadMore() {
        loadNext();
    }
}
