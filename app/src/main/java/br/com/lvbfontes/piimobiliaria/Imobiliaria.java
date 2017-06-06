package br.com.lvbfontes.piimobiliaria;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Imobiliaria extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
