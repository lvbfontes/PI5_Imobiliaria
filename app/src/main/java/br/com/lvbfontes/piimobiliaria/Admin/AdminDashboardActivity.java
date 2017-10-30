package br.com.lvbfontes.piimobiliaria.Admin;

//admin@imobiliaria.com
//admin01

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import br.com.lvbfontes.piimobiliaria.Acesso.LoginActivity;
import br.com.lvbfontes.piimobiliaria.Corretor.DashboardActivity;
import br.com.lvbfontes.piimobiliaria.Corretor.PostActivity;
import br.com.lvbfontes.piimobiliaria.IdiomasActivity;
import br.com.lvbfontes.piimobiliaria.R;
import br.com.lvbfontes.piimobiliaria.SobreActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button btnCadastroCorretores;
    private Button btnCadastroImoveis;
    private Button btnConsultaImoveis;
    private Button btnRelatorios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lerIdioma();
        setContentView(R.layout.activity_admin_dashboard);

        mAuth = FirebaseAuth.getInstance();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        });

        btnCadastroImoveis = (Button) findViewById(R.id.btnCadastroImoveis);
        btnCadastroImoveis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPostActivity = new Intent(AdminDashboardActivity.this, PostActivity.class);
                startActivity(intentPostActivity);
            }
        });

        btnCadastroCorretores = (Button) findViewById(R.id.btnCadastroCorretores);
        btnCadastroCorretores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCadastroCorretor = new Intent(AdminDashboardActivity.this, CadastroCorretorActivity.class);
                startActivity(intentCadastroCorretor);
            }
        });

        btnConsultaImoveis = (Button) findViewById(R.id.btnConsultaImoveis);
        btnConsultaImoveis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDashboardActivity = new Intent(AdminDashboardActivity.this, DashboardActivity.class);
                startActivity(intentDashboardActivity);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_idioma) {
            Intent intent = new Intent(AdminDashboardActivity.this, IdiomasActivity.class);
            startActivityForResult(intent, 1);
        }

        if (item.getItemId() == R.id.action_logout) {
            logout();
        }

        if (item.getItemId() == R.id.action_sobre) {
            startActivity(new Intent(AdminDashboardActivity.this, SobreActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
    }

    private void lerIdioma() {
        SharedPreferences preferences = getSharedPreferences("imobiliaria", MODE_APPEND);
        String idioma = preferences.getString("idioma", "pt");

        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);

        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
