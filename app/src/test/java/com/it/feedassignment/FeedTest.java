package com.it.feedassignment;

import com.it.feedassignment.api.FeedsService;
import com.it.feedassignment.models.JsonResponse;
import com.it.feedassignment.repositories.FeedRepository;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertTrue;

public class FeedTest {
    @Test
    public void getFeedList(){
    FeedRepository feedRepository=new FeedRepository();
    FeedsService feedsService=feedRepository.getFeedService();
   feedsService.getFeeds().enqueue(new Callback<JsonResponse>() {
        @Override
        public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
            if (response.body() != null) {
                assertTrue(true);
            }else{
                assertTrue(false);
            }
        }

        @Override
        public void onFailure(Call<JsonResponse> call, Throwable t) {

        }
    });

    }

}
