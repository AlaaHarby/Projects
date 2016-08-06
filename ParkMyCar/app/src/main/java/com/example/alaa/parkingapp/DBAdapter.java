package com.example.alaa.parkingapp;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Alaa on 4/20/2016.
 */
public class DBAdapter {

    private DatabaseReference mDB;

    public DBAdapter() {

        FirebaseDatabase fDB = FirebaseDatabase.getInstance();
        mDB = fDB.getReference();
    }
/*
    public void addUser(User user) {
        mDB.child(DBContract.USER_DB).child(user.getEmail()).setValue(user);
    }

    public Single<Boolean> authUser(final String email, final String password) {


        return Single.create(new Single.OnSubscribe<Boolean>() {
            @Override
            public void call(final SingleSubscriber<? super Boolean> singleSubscriber) {
                mDB.child(DBContract.USER_DB).child(email).orderByChild("email")
                    .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        singleSubscriber.onSuccess(password.equals(user.getPassword()));
                    }

                    @Override
                    public void onCancelled(DatabaseError firebaseError) {

                    }
                });
            }
        });

    }*/

    public Observable<Parking> getMarkers() {

        return Observable.create(new Observable.OnSubscribe<Parking>() {
            @Override
            public void call(final Subscriber<? super Parking> subscriber) {
                mDB.child(DBContract.PARKING_DB).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot parking: dataSnapshot.getChildren())
                            subscriber.onNext(parking.getValue(Parking.class));

                        subscriber.onCompleted();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void addParking(Parking parking){
        mDB.child(DBContract.PARKING_DB).child(parking.getParkingID()).setValue(parking);
    }
}
