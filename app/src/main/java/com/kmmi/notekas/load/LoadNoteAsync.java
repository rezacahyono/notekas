package com.kmmi.notekas.load;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;

import com.kmmi.notekas.db.notekashelper.NotekasHelper;
import com.kmmi.notekas.model.Notekas;
import com.kmmi.notekas.utils.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadNoteAsync {
    private final WeakReference<Context> weakContext;
    private final WeakReference<LoadNotekasCallback> weakCallback;

    public LoadNoteAsync(Context context, LoadNotekasCallback callback) {
        weakContext = new WeakReference<>(context);
        weakCallback = new WeakReference<>(callback);
    }

    public void executeNoteskasAll(int idUser) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        weakCallback.get().preExecute();
        executor.execute(() -> {
            Context context = weakContext.get();
            NotekasHelper notekasHelper = NotekasHelper.getInstance(context);
            notekasHelper.open();
            Cursor dataCursor = notekasHelper.queryByIdUser(String.valueOf(idUser));
            ArrayList<Notekas> notekas = MappingHelper.mapCursorToArrayList(dataCursor);
            notekasHelper.close();
            handler.post(() -> weakCallback.get().postExecute(notekas));
        });
    }

    public void executeIncome(int idUser) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        weakCallback.get().preExecute();
        executor.execute(() -> {
            Context context = weakContext.get();
            NotekasHelper notekasHelper = NotekasHelper.getInstance(context);
            notekasHelper.open();
            Cursor dataCursor = notekasHelper.getIncome(String.valueOf(idUser));
            ArrayList<Notekas> notekas = MappingHelper.mapCursorToArrayList(dataCursor);
            notekasHelper.close();
            handler.post(() -> weakCallback.get().postExecute(notekas));
        });
    }

    public void executeExpenditure(int idUser) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        weakCallback.get().preExecute();
        executor.execute(() -> {
            Context context = weakContext.get();
            NotekasHelper notekasHelper = NotekasHelper.getInstance(context);
            notekasHelper.open();
            Cursor dataCursor = notekasHelper.getExpenditure(String.valueOf(idUser));
            ArrayList<Notekas> notekas = MappingHelper.mapCursorToArrayList(dataCursor);
            notekasHelper.close();
            handler.post(() -> weakCallback.get().postExecute(notekas));
        });
    }
}
