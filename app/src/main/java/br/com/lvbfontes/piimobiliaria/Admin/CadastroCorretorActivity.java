package br.com.lvbfontes.piimobiliaria.Admin;

import android.app.Application;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.lvbfontes.piimobiliaria.Cadastro.CadastroActivity;
import br.com.lvbfontes.piimobiliaria.R;

public class CadastroCorretorActivity extends AppCompatActivity {

    private EditText edtNome;
    private  EditText edtSobrenome;
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnRealizarCadastro;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_corretor);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid());

        mProgress = new ProgressDialog(this);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtSobrenome = (EditText) findViewById(R.id.edtSobrenome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        btnRealizarCadastro = (Button) findViewById(R.id.btnRealizarCadastro);
        btnRealizarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSenha.getText().toString().length() < 6) {
                    Toast.makeText(CadastroCorretorActivity.this, R.string.toastSenhaLength, Toast.LENGTH_LONG).show();
                } else {
                    startRegister();
                }
            }
        });

    }

    private void startRegister() {

        final String funcao = "corretor";
        final String nome = edtNome.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();
        final String sobrenome = edtSobrenome.getText().toString().trim();

        if(!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(sobrenome) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(senha)) {

            mProgress.setMessage(getApplicationContext().getString(R.string.progressRegistro));
            mProgress.show();

            final DatabaseReference newPost = mDatabase.push();

            mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        mProgress.dismiss();
                        Toast.makeText(CadastroCorretorActivity.this, R.string.toastCadastroRealizado, Toast.LENGTH_SHORT);
                        startActivity(new Intent(CadastroCorretorActivity.this, AdminDashboardActivity.class));
                        finish();
                    /*mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("funcao").setValue(funcao);
                            newPost.child("nome").setValue(nome);
                            newPost.child("sobrenome").setValue(sobrenome);
                            newPost.child("email").setValue(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(CadastroCorretorActivity.this, "OK, atualizar res.strings", Toast.LENGTH_SHORT);
                                        startActivity(new Intent(CadastroCorretorActivity.this, AdminDashboardActivity.class));
                                    } else {
                                        Toast.makeText(CadastroCorretorActivity.this, "Erro ao inserir, atualizar res.strings", Toast.LENGTH_SHORT);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });*/

                    }
                }
            });
        }


    }
}
