package br.com.lvbfontes.piimobiliaria.pesquisaImovel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.lvbfontes.piimobiliaria.R;

public class ContratoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_contrato);

        Button btnAluguel = (Button) findViewById(R.id.btnAluguel);
        Button btnCompra = (Button) findViewById(R.id.btnCompra);

        btnAluguel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContratoActivity.this, TipoImovelActivity.class);
                startActivity(intent);
            }
        });

        btnCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContratoActivity.this, TipoImovelActivity.class);
                startActivity(intent);
            }
        });


    }
}
