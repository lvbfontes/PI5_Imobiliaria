package br.com.lvbfontes.piimobiliaria.pesquisaImovel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.lvbfontes.piimobiliaria.R;

public class ComodosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comodos);

        Button btnPronto = (Button) findViewById(R.id.btnProntoComodos);

        btnPronto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComodosActivity.this, FaixaDePrecoActivity.class);
                startActivity(intent);
            }
        });
    }
}
