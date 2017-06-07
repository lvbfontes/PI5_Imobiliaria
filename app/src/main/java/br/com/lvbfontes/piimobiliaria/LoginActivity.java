package br.com.lvbfontes.piimobiliaria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import br.com.lvbfontes.piimobiliaria.pesquisaImovel.ContratoActivity;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnIdioma;
    private Button btnCadastro;
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnRecuperarSenha;

    private ProgressDialog mProgress;
    private DatabaseReference mDatabaseUsuarios;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lerIdioma();
        setContentView(R.layout.activity_login);

        mProgress = new ProgressDialog(LoginActivity.this);

        mAuth = FirebaseAuth.getInstance();

        mDatabaseUsuarios = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        mDatabaseUsuarios.keepSynced(true);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnIdioma = (Button) findViewById(R.id.btnIdioma);
        btnCadastro = (Button) findViewById(R.id.btnCadastro);
        btnRecuperarSenha = (Button) findViewById(R.id.btnRecuperarSenha);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        btnRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RecuperarSenhaActivity.class);
                startActivity(intent);
            }
        });

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
                //Intent intent = new Intent (LoginActivity.this, ContratoActivity.class);
                //startActivity(intent);
            }
        });

        btnIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, IdiomasActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void checkLogin() {
        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(senha)) {

            mProgress.setMessage(getApplicationContext().getResources().getString(R.string.progressLogin));
            mProgress.show();

            mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()) {
                        mProgress.dismiss();
                        checkUserExists();
                    } else {
                        mProgress.dismiss();
                        Toast.makeText(LoginActivity.this, R.string.toastSenhaIncorreta, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, R.string.toastCamposEmpty, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserExists() {

        final String userId = mAuth.getCurrentUser().getUid();

        final DatabaseReference funcao = FirebaseDatabase.getInstance().getReference();

        final String funcaoUsuario = funcao.child("Usuarios").child("funcao").toString();

        mDatabaseUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userId)) {

                    if (funcaoUsuario.equals("corretor")) {

                        Intent dashboardIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                        dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(dashboardIntent);

                    } else {

                        Intent contratoIntent = new Intent(LoginActivity.this, ContratoActivity.class);
                        contratoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(contratoIntent);

                    }


                } else {

                    Intent finalizaCadastroIntent = new Intent(LoginActivity.this, FinalizaCadastroActivity.class);
                    finalizaCadastroIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(finalizaCadastroIntent);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            this.setContentView(R.layout.activity_login);
            recreate();
        }
        catch (Exception e) {
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
}

