package br.com.lvbfontes.piimobiliaria.Acesso;

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
import com.google.firebase.auth.FirebaseAuth;

import br.com.lvbfontes.piimobiliaria.Corretor.DashboardActivity;
import br.com.lvbfontes.piimobiliaria.R;

public class RecuperarSenhaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private ProgressDialog mProgress;

    private Button btnEnviarEmail;
    private EditText edtEmail;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);

        mProgress.setMessage(getResources().getString(R.string.progressEnviandoEmail));

        edtEmail = (EditText) findViewById(R.id.edtEmailRecuperar);
        btnEnviarEmail = (Button) findViewById(R.id.btnEnviarRecuperacaoSenha);

        btnEnviarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = edtEmail.getText().toString().trim();

                if(!TextUtils.isEmpty(email)) {

                    mProgress.show();

                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        mProgress.dismiss();

                                        Toast.makeText(RecuperarSenhaActivity.this, R.string.toastEmailEnviado, Toast.LENGTH_LONG).show();;

                                        startActivity(new Intent(RecuperarSenhaActivity.this, LoginActivity.class));
                                    }
                                }
                            });

                } else {

                    Toast.makeText(RecuperarSenhaActivity.this, R.string.toastEmailEmpty, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
