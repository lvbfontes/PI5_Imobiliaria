package br.com.lvbfontes.piimobiliaria.pesquisaImovel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.lvbfontes.piimobiliaria.R;

public class TipoImovelActivity extends AppCompatActivity {

    private ListView listaTipoImovel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_tipo_imovel);

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
}
