package br.com.lvbfontes.piimobiliaria.Corretor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import br.com.lvbfontes.piimobiliaria.R;

public class PostActivity extends AppCompatActivity {

    private final String contratoAluguel = "Locação";
    private final String contratoCompra = "Venda";

    private String[] arraySpinner;
    private Spinner spinnerPost;

    private static final int REQUEST_GALERIA = 1;

    private ImageButton mSelecionarImagem;
    private EditText mPostTipoImovel, mPostComodos, mPostValor, mPostArea;
    private Button btnPost;
    private Uri imageUri = null;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private ProgressDialog mProgress;

    private String aluguel;
    private String compra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();

        mCurrentUser = mAuth.getCurrentUser();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(mCurrentUser.getUid());


        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Imovel");
        mProgress = new ProgressDialog(this);

        mPostTipoImovel = (EditText) findViewById(R.id.edtTipoImovel);
        mPostComodos = (EditText) findViewById(R.id.edtComodos);
        mPostValor = (EditText) findViewById(R.id.edtValor);
        mPostArea = (EditText) findViewById(R.id.edtArea);
        btnPost = (Button) findViewById(R.id.btnPostFirebase);

        aluguel = getResources().getString(R.string.btnAluguel);
        compra = getResources().getString(R.string.btnCompra);

        this.arraySpinner = new String[] {aluguel, compra};
        spinnerPost = (Spinner) findViewById(R.id.spinnerDashboardContrato);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        spinnerPost.setAdapter(adapter);

        mSelecionarImagem = (ImageButton) findViewById(R.id.imgButtonImagem);
        mSelecionarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGaleria = new Intent(Intent.ACTION_GET_CONTENT);
                intentGaleria.setType("image/*");
                startActivityForResult(intentGaleria, REQUEST_GALERIA);
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }

    private void startPosting() {

        mProgress.setMessage(getApplicationContext().getResources().getString(R.string.progressUpload));

        final String tipoImovel, comodos, valor, area, contrato;
        tipoImovel = mPostTipoImovel.getText().toString().trim();
        comodos = mPostComodos.getText().toString().trim();
        valor = mPostValor.getText().toString().trim();
        area = mPostArea.getText().toString().trim();
        //contrato = spinnerPost.getSelectedItem().toString().trim();

        if (spinnerPost.getSelectedItemPosition() == 0) {
            contrato = contratoAluguel;
        } else {
            contrato = contratoCompra;
        }

        if(!TextUtils.isEmpty(tipoImovel) && !TextUtils.isEmpty(comodos) && !TextUtils.isEmpty(valor) && !TextUtils.isEmpty(area) && !TextUtils.isEmpty(contrato) && imageUri != null) {
            mProgress.show();
            StorageReference filepath = mStorage.child("Fotos_Imovel").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = mDatabase.push();

                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newPost.child("Tipo").setValue(tipoImovel);
                            newPost.child("Comodos").setValue(comodos);
                            newPost.child("Valor").setValue(valor);
                            newPost.child("Contrato").setValue(contrato);
                            newPost.child("Imagem").setValue(downloadUrl.toString());
                            newPost.child("Area").setValue(area);
                            newPost.child("UserId").setValue(mCurrentUser.getUid());
                            newPost.child("Username").setValue(dataSnapshot.child("nome").getValue())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()) {
                                                Toast.makeText(PostActivity.this, "Dados inseridos!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(PostActivity.this, DashboardActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(PostActivity.this, "Erro ao inserir dados", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mProgress.dismiss();

                    //startActivity(new Intent(PostActivity.this, DashboardActivity.class));
                }
            });
        } else {
            Toast.makeText(PostActivity.this, R.string.toastCamposEmpty, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GALERIA && resultCode == RESULT_OK) {
            imageUri = data.getData();
            mSelecionarImagem.setImageURI(imageUri);
        }
    }
}
