package com.example.ratiu.atproject;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MeasurementViewModel extends AndroidViewModel {
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private LiveData<List<List<Integer>>> measurements=new MutableLiveData<>();
    private int[] limits=new int[3];

    public MeasurementViewModel(@NonNull Application application) {
        super(application);

        limits[0]=Integer.MAX_VALUE;
        limits[1]=Integer.MAX_VALUE;
        limits[2]=Integer.MAX_VALUE;

        ((MutableLiveData)measurements).setValue(new ArrayList<>());

        DatabaseReference reference=database.getReference("at/values");
        reference.keepSynced(true);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals("current"))
                    return;
                Integer newData=dataSnapshot.getValue(Integer.TYPE);
                List<List<Integer>> newlist=measurements.getValue();
                List<Integer> newline=new ArrayList<>();
                newline.add(Integer.parseInt(dataSnapshot.getKey()));
                newline.add(newData);
                newlist.add(newline);
                ((MutableLiveData)measurements).setValue(newlist);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals("current"))
                    return;
                Integer newData=dataSnapshot.getValue(Integer.TYPE);
                List<List<Integer>> newlist=measurements.getValue();
                List<Integer> newline=new ArrayList<>();
                newline.add(Integer.parseInt(dataSnapshot.getKey()));
                newline.add(newData);
                newlist.add(newline);
                ((MutableLiveData)measurements).setValue(newlist);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public LiveData<List<List<Integer>>> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(LiveData<List<List<Integer>>> measurements) {
        this.measurements = measurements;
    }

    public int[] getLimits() {
        return limits;
    }

    public void setLimits(int[] limits) {
        this.limits = limits;
    }
}
