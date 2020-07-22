package com.it.feedassignment.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.it.feedassignment.FeedActivity;
import com.it.feedassignment.R;
import com.it.feedassignment.adapters.FeedsAdapter;
import com.it.feedassignment.models.Feed;
import com.it.feedassignment.roomdb.FeedDatabase;
import com.it.feedassignment.roomdb.FeedModel;
import com.it.feedassignment.viewmodels.FeedViewModel;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FeedsFragment extends Fragment {

    private FeedViewModel viewModel;
    private FeedsAdapter adapter;
    private FeedDatabase feedDatabase;
    private TextView tvNoMessage;
    private RecyclerView recyclerView;
    private List<Feed> feedList = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedDatabase = FeedDatabase.getInstance(getActivity());
        new RetrieveTask(this).execute();
        adapter = new FeedsAdapter();
        viewModel = ViewModelProviders.of(this).get(FeedViewModel.class);
        viewModel.init();
        viewModel.getJsonResponseLiveData().observe(this, jsonResponse -> {
            if (jsonResponse != null) {
                feedList=jsonResponse.getFeeds();
                if(feedList!=null && feedList.size()>0) {
                    if(tvNoMessage!=null){
                        tvNoMessage.setVisibility(View.GONE);
                    }
                    adapter.setResults(feedList);
                    new InsertTask(this, feedList).execute();
                }else{
                    //Show message
                    if(tvNoMessage!=null){
                        tvNoMessage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);
         recyclerView = view.findViewById(R.id.fragment_feeds);
        tvNoMessage = view.findViewById(R.id.tvNoMessage);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> {
            // Your code to refresh the list here.
            viewModel.init();
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return view;
    }

    private class InsertTask extends AsyncTask<Void,Void,Boolean> {

        private WeakReference<FeedsFragment> activityReference;
        private List<Feed> feedList;

        // only retain a weak reference
        InsertTask(FeedsFragment context, List<Feed> feeds) {
            activityReference = new WeakReference<FeedsFragment>(context);
            this.feedList = feeds;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            activityReference.get().feedDatabase.getFeedDao().deleteAll();
            for (int i = 0; i < feedList.size(); i++) {
                FeedModel feedModel=new FeedModel(feedList.get(i).getTitle(),feedList.get(i).getDescription(),feedList.get(i).getImageHref());
                activityReference.get().feedDatabase.getFeedDao().insert(feedModel);
            }
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {

        }

    }

    private class RetrieveTask extends AsyncTask<Void,Void,List<FeedModel>> {

        private WeakReference<FeedsFragment> activityReference;

        // only retain a weak reference to the activity
        RetrieveTask(FeedsFragment context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<FeedModel> doInBackground(Void... voids) {
            if (activityReference.get()!=null)
                return activityReference.get().feedDatabase.getFeedDao().getAllFeeds();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<FeedModel> feedModelList) {
            if (feedModelList!=null && feedModelList.size()>0 ){
                feedList.clear();
                for (int i = 0; i < feedModelList.size(); i++) {
                        Feed feed=new Feed(feedModelList.get(i).getTitle(),feedModelList.get(i).getDescription(),feedModelList.get(i).getImageHref());
                        feedList.add(feed);
                }
                if(adapter!=null) {
                    adapter.setResults(feedList);
                    recyclerView.setAdapter(adapter);
                }
            }else{
                viewModel.init();
            }
        }

    }


}
