package com.it.feedassignment.roomdb;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = { FeedModel.class }, version = 1)
public abstract class FeedDatabase extends RoomDatabase {

    public abstract FeedDao getFeedDao();
    private static FeedDatabase feedDatabase;

    public static FeedDatabase getInstance(Context context) {
        if (null == feedDatabase) {
            feedDatabase = buildDatabaseInstance(context);
        }
        return feedDatabase;
    }

    private static FeedDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                FeedDatabase.class,
                "Feed_Database")
                .allowMainThreadQueries().build();
    }

    public void cleanUp(){
        feedDatabase = null;
    }


}
