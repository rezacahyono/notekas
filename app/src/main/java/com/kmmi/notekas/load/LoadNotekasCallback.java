package com.kmmi.notekas.load;

import com.kmmi.notekas.model.Notekas;

import java.util.ArrayList;

public interface LoadNotekasCallback {
    void preExecute();

    void postExecute(ArrayList<Notekas> notekas);
}
