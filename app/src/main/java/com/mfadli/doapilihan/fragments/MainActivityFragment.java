package com.mfadli.doapilihan.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.mfadli.doapilihan.DoaPilihanApp;
import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.activities.MainActivity;
import com.mfadli.doapilihan.adapter.MainAdapter;
import com.mfadli.doapilihan.adapter.MainRecyclerClickListener;
import com.mfadli.doapilihan.adapter.MainRecyclerTouchListener;
import com.mfadli.doapilihan.data.repo.DoaDataRepo;
import com.mfadli.doapilihan.event.GeneralEvent;
import com.mfadli.doapilihan.event.RxBus;
import com.mfadli.doapilihan.model.BGPattern;
import com.mfadli.doapilihan.model.DoaDetail;
import com.mfadli.utils.BitmapCacher;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String ARG_DOA_DETAILS = "DoaDetails";
    private MainAdapter mMainAdapter;
    private OnMainFragmentItemClickListener mItemClickListener;
    private RxBus mRxBus;
    private CompositeSubscription mSubscription;
    private DoaDataRepo mDoaRepo;
    private List<DoaDetail> mDoaDetailList;

    @Bind(R.id.recycler_main_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.fragment_main)
    RelativeLayout mLayout;

    public MainActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment MainActivityFragment.
     */
    public static MainActivityFragment newInstance() {
        MainActivityFragment fragment = new MainActivityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxBus = ((DoaPilihanApp) DoaPilihanApp.getContext()).getRxBusSingleton();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        DoaPilihanApp app = (DoaPilihanApp) DoaPilihanApp.getContext();

        mDoaRepo = new DoaDataRepo();
        mDoaDetailList = mDoaRepo.getAllDoa();

        configureRecyclerView(app.getBgPattern());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        //super.onCreateOptionsMenu(menu, inflater);

        final MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        mSubscription = new CompositeSubscription();

        // subscribe to SuccessSaveFontSize
        mSubscription
                .add(mRxBus.toObserverable()
                        .subscribe(event -> {
                            if (event instanceof GeneralEvent.SuccessIabSetup) {
                                GeneralEvent.SuccessIabSetup ev = (GeneralEvent.SuccessIabSetup) event;

                            } else if (event instanceof GeneralEvent.SuccessSaveBGPattern) {
                                GeneralEvent.SuccessSaveBGPattern ev = (GeneralEvent.SuccessSaveBGPattern) event;
                                mMainAdapter.setBgPattern(ev.getBgPattern());
                            }
                        }));
    }

    @Override
    public void onStop() {
        super.onStop();
        mSubscription.unsubscribe();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Setup RecyclerView, related adapter and Touch Listener.
     * First time check if pattern image is cached or not, if not yet,
     * Cache it tu {@link BitmapCacher}
     *
     * @param bgPattern BGPattern
     */
    private void configureRecyclerView(BGPattern bgPattern) {

        Bitmap bitmap = BitmapCacher.getCacheBitmap(bgPattern.getName());
        if (bitmap == null) {
            BitmapCacher.cacheBitmap(bgPattern.getDrawable(), bgPattern.getName());
        }

        mMainAdapter = new MainAdapter(getContext(), mDoaDetailList, bgPattern);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mMainAdapter);
        mRecyclerView.addOnItemTouchListener(new MainRecyclerTouchListener(getContext(), mRecyclerView, new MainRecyclerClickListener() {
            @Override
            public void onClick(View view, int position) {
                FrameLayout titleFrame = (FrameLayout) view.findViewById(R.id.detail_title_frame);
                DoaDetail doaDetail = mMainAdapter.getItem(position);

                if (mItemClickListener != null) {
                    mItemClickListener.onMainFragmentItemClick(doaDetail.getId() - 1, titleFrame);
                }

            }

            @Override
            public void onLongClick(View view, int position) {
                //Toast.makeText(getContext(), "long click", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainFragmentItemClickListener) {
            mItemClickListener = (OnMainFragmentItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnMainFragmentItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mItemClickListener = null;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<DoaDetail> filteredList = filter(mDoaDetailList, query);
        mMainAdapter.animateTo(filteredList);
        mRecyclerView.scrollToPosition(0);

        return true;
    }

    /**
     * Create a filtered list of Doa Details from search query.
     *
     * @param list  List<DoaDetail> Original list
     * @param query String Text query
     * @return List<DoaDetail> Filtered list
     */
    private List<DoaDetail> filter(List<DoaDetail> list, String query) {
        query = query.toLowerCase();

        final List<DoaDetail> filteredModelList = new ArrayList<>();
        for (DoaDetail doaDetail : list) {
            final String text = doaDetail.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(doaDetail);
            }
        }
        return filteredModelList;
    }

    /**
     * Convinient function to scroll RecycleView to position specified.
     *
     * @param position int
     */
    public void scrollToPosition(int position) {
        mRecyclerView.scrollToPosition(position);
    }

    /**
     * Refresh the list layout if BGPattern is changed.
     */
    public void refresh() {
        DoaPilihanApp app = (DoaPilihanApp) DoaPilihanApp.getContext();
        mMainAdapter.setBgPattern(app.getBgPattern());
    }

    /**
     * Interface to communicate with MainActivity
     */
    public interface OnMainFragmentItemClickListener {
        void onMainFragmentItemClick(int position, FrameLayout titleFrame);
    }
}
