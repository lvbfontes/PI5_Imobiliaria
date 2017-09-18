package br.com.lvbfontes.piimobiliaria.Corretor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import br.com.lvbfontes.piimobiliaria.Cadastro.FinalizaCadastroActivity;
import br.com.lvbfontes.piimobiliaria.Acesso.LoginActivity;
import br.com.lvbfontes.piimobiliaria.IdiomasActivity;
import br.com.lvbfontes.piimobiliaria.Modelo.Imovel;
import br.com.lvbfontes.piimobiliaria.R;
import br.com.lvbfontes.piimobiliaria.SobreActivity;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView mListaDashboard;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsuarios;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lerIdioma();
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(DashboardActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Imovel");
        mDatabaseUsuarios = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        mDatabaseUsuarios.keepSynced(true);
        mDatabase.keepSynced(true);

        mListaDashboard = (RecyclerView) findViewById(R.id.listaDashboard);
        mListaDashboard.setHasFixedSize(true);
        mListaDashboard.setLayoutManager(new LinearLayoutManager(this));

        checkIfUserExists();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            this.setContentView(R.layout.activity_dashboard);
            recreate();
        } catch (Exception e) {
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<Imovel, DashboardViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Imovel, DashboardViewHolder>(
                Imovel.class, R.layout.dashboard_row, DashboardViewHolder.class, mDatabase
        ) {
            @Override
            protected void populateViewHolder(DashboardViewHolder viewHolder, final Imovel modelo, int position) {

                final String idImovel = getRef(position).getKey();

                viewHolder.setTipoImovel(modelo.getTipo());
                viewHolder.setContrato(modelo.getContrato());
                viewHolder.setComodos(modelo.getComodos());
                viewHolder.setPreco(modelo.getValor());
                viewHolder.setArea(modelo.getArea());
                viewHolder.setImage(getApplicationContext(), modelo.getImagem());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TextView tipoImovel = (TextView) v.findViewById(R.id.dashTipoImovel);
                        TextView tipoContrato = (TextView) v.findViewById(R.id.dashContrato);
                        TextView preco = (TextView) v.findViewById(R.id.dashPreco);
                        TextView numeroComodos = (TextView) v.findViewById(R.id.dashComodos);
                        TextView area = (TextView) v.findViewById(R.id.dashArea);
                        ImageView imagemImovel = (ImageView) v.findViewById(R.id.dashImage);


                        Intent visualizarImovelIntent = new Intent(DashboardActivity.this, VisualizarImovelActivity.class);

                        visualizarImovelIntent.putExtra("idImovel", idImovel);
                        visualizarImovelIntent.putExtra("tipoImovel", tipoImovel.getText().toString());
                        visualizarImovelIntent.putExtra("tipoContrato", tipoContrato.getText().toString());
                        visualizarImovelIntent.putExtra("preco", preco.getText().toString());
                        visualizarImovelIntent.putExtra("numeroComodos", numeroComodos.getText().toString());
                        visualizarImovelIntent.putExtra("area", area.getText().toString());
                        //visualizarImovelIntent.putExtra("imagem", imagemImovel.getDrawable().toString());
                        visualizarImovelIntent.putExtra("imagem", imagemImovel.getId());

                        startActivity(visualizarImovelIntent);

                    }
                });
            }
        };

        mListaDashboard.setAdapter(firebaseRecyclerAdapter);
    }

    private void checkIfUserExists() {

        if (mAuth.getCurrentUser() != null) {

            final String userId = mAuth.getCurrentUser().getUid();

            mDatabaseUsuarios.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(userId)) {

                        Intent FinalizaCadastroIntent = new Intent(DashboardActivity.this, FinalizaCadastroActivity.class);
                        FinalizaCadastroIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(FinalizaCadastroIntent);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_idioma) {
            Intent intent = new Intent(DashboardActivity.this, IdiomasActivity.class);
            startActivityForResult(intent, 1);
        }

        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(DashboardActivity.this, PostActivity.class));
        }

        if (item.getItemId() == R.id.action_logout) {
            logout();
        }

        if (item.getItemId() == R.id.action_sobre) {
            startActivity(new Intent(DashboardActivity.this, SobreActivity.class));
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

    private static class DashboardViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public DashboardViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setTipoImovel(String tipoImovel) {
            TextView dashTipoImovel = (TextView) mView.findViewById(R.id.dashTipoImovel);
            dashTipoImovel.setText(tipoImovel);
        }

        private void setContrato(String contrato) {
            TextView dashContrato = (TextView) mView.findViewById(R.id.dashContrato);
            dashContrato.setText(contrato);
        }

        private void setComodos(String comodos) {
            TextView dashComodos = (TextView) mView.findViewById(R.id.dashComodos);
            dashComodos.setText(comodos);
        }

        private void setPreco(String preco) {
            TextView dashPreco = (TextView) mView.findViewById(R.id.dashPreco);
            dashPreco.setText(preco);
        }

        private void setArea(String area) {
            TextView dashArea = (TextView) mView.findViewById(R.id.dashArea);
            dashArea.setText(area);
        }

        private void setImage(final Context context, final String image) {

            final ImageView dashImage = (ImageView) mView.findViewById(R.id.dashImage);

            //Picasso.with(context).load(image).into(dashImage);

            Picasso.with(context).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(dashImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(context).load(image).into(dashImage);

                }
            });

        }
    }
}

