package br.com.lvbfontes.piimobiliaria;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

public class IdiomasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idiomas);
    }

    public void voltar(View v) {
        setResult(Activity.RESULT_OK, null);
        finish();
    }

    public void idiomaEN (View v) {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);

        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("imobiliaria", MODE_PRIVATE).edit();
        editor.putString("idioma", "en");
        editor.commit();

        recreate();

    }

    public void idiomaPT (View v) {
        Locale locale = new Locale("pt");
        Locale.setDefault(locale);

        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("imobiliaria", MODE_PRIVATE).edit();
        editor.putString("idioma", "pt");
        editor.commit();

        recreate();

    }

    public void idiomaES (View v) {
        Locale locale = new Locale("es");
        Locale.setDefault(locale);

        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("imobiliaria", MODE_PRIVATE).edit();
        editor.putString("idioma", "es");
        editor.commit();

        recreate();

    }

    public void idiomaFR (View v) {
        Locale locale = new Locale("fr");
        Locale.setDefault(locale);

        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("imobiliaria", MODE_PRIVATE).edit();
        editor.putString("idioma", "fr");
        editor.commit();

        recreate();

    }

}
