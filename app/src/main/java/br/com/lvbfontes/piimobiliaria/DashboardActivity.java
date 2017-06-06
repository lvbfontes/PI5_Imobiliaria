package br.com.lvbfontes.piimobiliaria;

import android.content.Context;
import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import br.com.lvbfontes.piimobiliaria.Modelo.Imovel;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView mListaDashboard;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsuarios;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(DashboardActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Imovel");
        mDatabaseUsuarios = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        mDatabaseUsuarios.keepSynced(true);

        mListaDashboard = (RecyclerView) findViewById(R.id.listaDashboard);
        mListaDashboard.setHasFixedSize(true);
        mListaDashboard.setLayoutManager(new LinearLayoutManager(this));

        checkUserExists();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<Imovel, DashboardViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Imovel, DashboardViewHolder>(
                Imovel.class, R.layout.dashboard_row, DashboardViewHolder.class, mDatabase
        ) {
            @Override
            protected void populateViewHolder(DashboardViewHolder viewHolder, Imovel model, int position) {
                viewHolder.setTipoImovel(model.getTipo());
                viewHolder.setContrato(model.getContrato());
                viewHolder.setComodos(model.getComodos());
                viewHolder.setPreco(model.getValor());
                viewHolder.setArea(model.getArea());
                viewHolder.setImage(getApplicationContext(), model.getImagem());
            }
        };

        mListaDashboard.setAdapter(firebaseRecyclerAdapter);
    }

    private void checkUserExists() {

        if(mAuth.getCurrentUser() != null) {

            final String userId = mAuth.getCurrentUser().getUid();

            mDatabaseUsuarios.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild(userId)) {

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

        private void setImage(Context context, String image) {
            ImageView dashImage = (ImageView) mView.findViewById(R.id.dashImage);
            Picasso.with(context).load(image).into(dashImage);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(DashboardActivity.this, PostActivity.class));
        }

        if (item.getItemId() == R.id.action_logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
    }
}
