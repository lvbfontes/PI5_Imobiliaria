package br.com.lvbfontes.piimobiliaria.Corretor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.lvbfontes.piimobiliaria.R;

public class VisualizarImovelActivity extends AppCompatActivity {

    private DateFormat dateFormat;
    private Date date;

    private Button btnEditarImovel, btnExcluirImovel;
    private String idImovel, urlImagem, tipoImovel, numeroComodos, preco, tipoContrato, area;

    private TextView txtVisualizarTipo, txtVisualizarComodos, txtVisualizarValor, txtVisualizarContrato, txtVisualizarArea;

    private DatabaseReference mDatabase, mDatabaseLog;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_imovel);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        date = new Date();

        tipoImovel = getIntent().getStringExtra("tipoImovel");
        numeroComodos = getIntent().getStringExtra("numeroComodos");
        preco = getIntent().getStringExtra("preco");
        tipoContrato = getIntent().getStringExtra("tipoContrato");
        area = getIntent().getStringExtra("area");

        btnEditarImovel = (Button) findViewById(R.id.btnEditarImovel);
        btnExcluirImovel = (Button) findViewById(R.id.btnExcluirImovel);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Imovel");
        mDatabaseLog = FirebaseDatabase.getInstance().getReference().child("Log");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        btnExcluirImovel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(idImovel).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference newLog = mDatabaseLog.push();
                            newLog.child("Data").setValue(dateFormat.format(date));
                            newLog.child("ImovelId").setValue(idImovel);
                            newLog.child("Operacao").setValue("Exclusão");
                            newLog.child("UserId").setValue(mCurrentUser.getUid());
                            newLog.child("tipoImovel").setValue(tipoImovel);
                            newLog.child("numeroComodos").setValue(numeroComodos);
                            newLog.child("preco").setValue(preco);
                            newLog.child("tipoContrato").setValue(tipoContrato);
                            newLog.child("area").setValue(area);

                            startActivity(new Intent(VisualizarImovelActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            Toast.makeText(VisualizarImovelActivity.this, "Erro ao excluir", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        idImovel = getIntent().getStringExtra("idImovel");

        mDatabase.child(idImovel).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    urlImagem = dataSnapshot.child("Imagem").getValue().toString();
                    setImage(VisualizarImovelActivity.this, urlImagem);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        txtVisualizarTipo = (TextView) findViewById(R.id.txtVisualizarTipo);
        txtVisualizarComodos = (TextView) findViewById(R.id.txtVisualizarComodos);
        txtVisualizarValor = (TextView) findViewById(R.id.txtVisualizarValor);
        txtVisualizarContrato = (TextView) findViewById(R.id.txtVisualizarContrato);
        txtVisualizarArea = (TextView) findViewById(R.id.txtVisualizarArea);

        txtVisualizarTipo.setText(tipoImovel);
        txtVisualizarComodos.setText(numeroComodos);
        txtVisualizarValor.setText(preco);
        txtVisualizarContrato.setText(tipoContrato);
        txtVisualizarArea.setText(area);

    }

    private void setImage(final Context context, final String image) {

        final ImageView imgVisualizarImovel = (ImageView) findViewById(R.id.imgVisualizarImovel);

        Picasso.with(context).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imgVisualizarImovel,
                new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(image).into(imgVisualizarImovel);
                    }
                });

    }
}
