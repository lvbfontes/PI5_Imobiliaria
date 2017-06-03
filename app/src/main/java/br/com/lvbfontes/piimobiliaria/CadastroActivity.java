package br.com.lvbfontes.piimobiliaria;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        EditText edtNome = (EditText) findViewById(R.id.edtNome);
        EditText edtSobrenome = (EditText) findViewById(R.id.edtSobrenome);
        final EditText edtSenha = (EditText) findViewById(R.id.edtSenha);
        final EditText edtRepeteSenha = (EditText) findViewById(R.id.edtRepeteSenha);

        Button btnConfirmaCadastro = (Button) findViewById(R.id.btnConfirmaCadastro);

        btnConfirmaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSenha.getText().toString().equals(edtRepeteSenha.getText().toString())) {
                    //continuar cadastro
                } else {
                    Toast.makeText(CadastroActivity.this, R.string.toastSenhaDiferente, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
