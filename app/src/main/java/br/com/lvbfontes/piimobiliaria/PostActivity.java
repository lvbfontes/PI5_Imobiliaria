package br.com.lvbfontes.piimobiliaria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private String[] arraySpinner;
    private ImageButton mSelecionarImagem;
    private static final int REQUEST_GALERIA = 1;
    private EditText mPostTipoImovel, mPostComodos, mPostValor, mPostArea;
    private Button btnPost;
    private Spinner s;
    private Uri imageUri = null;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Imovel");
        mProgress = new ProgressDialog(this);

        mPostTipoImovel = (EditText) findViewById(R.id.edtTipoImovel);
        mPostComodos = (EditText) findViewById(R.id.edtComodos);
        mPostValor = (EditText) findViewById(R.id.edtValor);
        mPostArea = (EditText) findViewById(R.id.edtArea);
        btnPost = (Button) findViewById(R.id.btnPostFirebase);

        String Aluguel = getResources().getString(R.string.btnAluguel);
        String Compra = getResources().getString(R.string.btnCompra);
        this.arraySpinner = new String[] {Aluguel, Compra};
        s = (Spinner) findViewById(R.id.spinnerDashboardContrato);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        s.setAdapter(adapter);

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

        mProgress.setMessage("Fazendo upload...");
        mProgress.show();

        final String tipoImovel, comodos, valor, area, contrato;
        tipoImovel = mPostTipoImovel.getText().toString().trim();
        comodos = mPostComodos.getText().toString().trim();
        valor = mPostValor.getText().toString().trim();
        area = mPostArea.getText().toString().trim();
        contrato = s.getSelectedItem().toString().trim();

        if(!TextUtils.isEmpty(tipoImovel) && !TextUtils.isEmpty(comodos) && !TextUtils.isEmpty(valor) && !TextUtils.isEmpty(area) && !TextUtils.isEmpty(contrato) && imageUri != null) {
            StorageReference filepath = mStorage.child("Fotos_Imovel").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabase.push();
                    newPost.child("Tipo").setValue(tipoImovel);
                    newPost.child("Comodos").setValue(comodos);
                    newPost.child("Valor").setValue(valor);
                    newPost.child("Contrato").setValue(contrato);
                    newPost.child("Imagem").setValue(downloadUrl.toString());
                    //newPost.child("UserId").setValue(FirebaseAuth.getCurrentUser());

                    mProgress.dismiss();
                    Toast.makeText(PostActivity.this, "Dados inseridos!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PostActivity.this, DashboardActivity.class));
                }
            });
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
