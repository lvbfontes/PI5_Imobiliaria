package br.com.lvbfontes.piimobiliaria.Corretor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;

import br.com.lvbfontes.piimobiliaria.R;

public class VisualizarImovelActivity extends AppCompatActivity {

    ImageView imgVisualizarImovel;
    TextView txtVisualizarTipo, txtVisualizarComodos, txtVisualizarValor, txtVisualizarContrato, txtVisualizarArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_imovel);

        //String imagemPicassoPath = getIntent().getStringExtra("imagem");
        int idImagem = getIntent().getIntExtra("imagem", 0);

        try {
            //Picasso.with(this).load(imagemPicassoPath).into(imgVisualizarImovel);
            imgVisualizarImovel.setImageResource(idImagem);
        } catch (Exception e) {
            //Toast.makeText(this, getIntent().getStringExtra("imagem"), Toast.LENGTH_SHORT).show();
        }

        txtVisualizarTipo = (TextView) findViewById(R.id.txtVisualizarTipo);
        txtVisualizarComodos = (TextView) findViewById(R.id.txtVisualizarComodos);
        txtVisualizarValor = (TextView) findViewById(R.id.txtVisualizarValor);
        txtVisualizarContrato = (TextView) findViewById(R.id.txtVisualizarContrato);
        txtVisualizarArea = (TextView) findViewById(R.id.txtVisualizarArea);


        txtVisualizarTipo.setText(getIntent().getStringExtra("tipoImovel"));
        txtVisualizarComodos.setText(getIntent().getStringExtra("numeroComodos"));
        txtVisualizarValor.setText(getIntent().getStringExtra("preco"));
        txtVisualizarContrato.setText(getIntent().getStringExtra("tipoContrato"));
        txtVisualizarArea.setText(getIntent().getStringExtra("area"));

    }
}
