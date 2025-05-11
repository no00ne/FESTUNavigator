package com.example.festunavigator.data;

import android.app.Application;

public class App extends Application {
    public static final String DATABASE_NAME = "graph_db";
    public static final String DATABASE_DIR = "database/graph.db";
    private static App instance;
    private static com.example.festunavigator.data.data_source.GraphDatabase database;
    private static com.example.festunavigator.data.repository.GraphRepositoryImpl graphRepository;

    public static App getInstance() {
        return instance;
    }

    public static com.example.festunavigator.data.data_source.GraphDatabase getDatabase() {
        return database;
    }

    public static com.example.festunavigator.data.repository.GraphRepositoryImpl getGraphRepository() {
        return graphRepository;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 初始化数据库
        database = androidx.room.Room.databaseBuilder(this,
                        com.example.festunavigator.data.data_source.GraphDatabase.class,
                        DATABASE_NAME)
                .createFromAsset(DATABASE_DIR)
                .allowMainThreadQueries()
                .build();
        // 初始化仓库
        graphRepository = new com.example.festunavigator.data.repository.GraphRepositoryImpl(database);
    }
}
