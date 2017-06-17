package br.com.lvbfontes.piimobiliaria.pesquisaImovel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import br.com.lvbfontes.piimobiliaria.Acesso.LoginActivity;
import br.com.lvbfontes.piimobiliaria.R;
import br.com.lvbfontes.piimobiliaria.SobreActivity;

public class TipoImovelActivity extends AppCompatActivity {

    private ListView listaTipoImovel;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_tipo_imovel);

        mAuth = FirebaseAuth.getInstance();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(TipoImovelActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        });

        listaTipoImovel = (ListView) findViewById(R.id.listaTipoImovel);

        listaTipoImovel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View view, int position, long id) {
                if (position == 3) {
                    Intent intent = new Intent(TipoImovelActivity.this, AreaActivity.class);
                    startActivity(intent);
                } else if (position == 4){
                    Intent intent = new Intent(TipoImovelActivity.this, AreaActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(TipoImovelActivity.this, ComodosActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cliente, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(TipoImovelActivity.this, ContratoActivity.class));
        }

        if (item.getItemId() == R.id.action_logout) {
            logout();
        }

        if (item.getItemId() == R.id.action_sobre) {
            startActivity(new Intent(TipoImovelActivity.this, SobreActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() { mAuth.signOut();
    }
}
