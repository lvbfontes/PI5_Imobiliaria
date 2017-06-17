package br.com.lvbfontes.piimobiliaria.Cadastro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.lvbfontes.piimobiliaria.R;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private EditText edtRepeteSenha;
    private Button btnConfirmaCadastro;

    private FirebaseAuth mAuth;
    //private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //mAuth.addAuthStateListener(mAuthListener);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        mProgress = new ProgressDialog(this);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        edtRepeteSenha = (EditText) findViewById(R.id.edtRepeteSenha);
        btnConfirmaCadastro = (Button) findViewById(R.id.btnConfirmaCadastro);

        btnConfirmaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSenha.getText().toString().equals(edtRepeteSenha.getText().toString())) {
                    startRegister();
                } else {
                    Toast.makeText(CadastroActivity.this, R.string.toastSenhaDiferente, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startRegister() {

        final String nome = edtNome.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();

        if(!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(senha) && !TextUtils.isEmpty(email)) {

            mProgress.setMessage(getApplicationContext().getResources().getString(R.string.progressRegistro));
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()) {

                        String userId = mAuth.getCurrentUser().getUid().toString();
                        DatabaseReference currentUserDb = mDatabase.child(userId);
                        currentUserDb.child("nome").setValue(nome);
                        currentUserDb.child("image").setValue("default");

                        mProgress.dismiss();

                        Intent dashboardIntent = new Intent(CadastroActivity.this, FinalizaCadastroActivity.class);
                        dashboardIntent.putExtra("nome", nome);
                        dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(dashboardIntent);

                    } else {

                        Toast.makeText(CadastroActivity.this, "Erro task Successful", Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();

                    }
                }
            });
        } else {
            Toast.makeText(CadastroActivity.this, R.string.toastCamposEmpty, Toast.LENGTH_SHORT).show();
        }
    }
}
