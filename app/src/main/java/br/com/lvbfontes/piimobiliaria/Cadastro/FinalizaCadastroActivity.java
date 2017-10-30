package br.com.lvbfontes.piimobiliaria.Cadastro;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import br.com.lvbfontes.piimobiliaria.Corretor.DashboardActivity;
import br.com.lvbfontes.piimobiliaria.R;
import br.com.lvbfontes.piimobiliaria.pesquisaImovel.ContratoActivity;

public class FinalizaCadastroActivity extends AppCompatActivity {

    private ImageButton imgButtonProfilePic;
    private EditText edtNome;
    private EditText edtSobrenome;
    private Button btnFinalizaCadastro;

    private final String funcaoCorretor = "corretor";
    private final String funcaoCliente = "cliente";
    //private Spinner spinnerCadastro;
    private String[] arraySpinner;
    String usuarioCorretor, usuarioCliente, emailBundleIntent;

    private Intent intent;
    private Bundle b;

    private Uri mImageUri;

    private static final int GALLERY_REQUEST = 1;

    private DatabaseReference mDatabaseUsuarios;
    private FirebaseAuth mAuth;
    private StorageReference mStorageImage;

    private ProgressDialog mProgress;
    private final String emailImobiliaria = "@imobiliaria.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finaliza_cadastro);

        mProgress = new ProgressDialog(FinalizaCadastroActivity.this);

        //pega referencia root/usuarios na database
        mDatabaseUsuarios = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        //instancia de autenticacao do firebase
        mAuth = FirebaseAuth.getInstance();
        //referencia de storage root/Profile_Images
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_Images");

        //usuarioCorretor = getResources().getString(R.string.usuarioCorretor);
        usuarioCliente = getResources().getString(R.string.usuarioCliente);
        //this.arraySpinner = new String[] {usuarioCorretor, usuarioCliente};
        //spinnerCadastro = (Spinner) findViewById(R.id.spinnerFinalizaCadastro);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        //spinnerCadastro.setAdapter(adapter);

        imgButtonProfilePic = (ImageButton) findViewById(R.id.imgButtonProfilePic);
        edtNome = (EditText) findViewById(R.id.edtNome);
        edtSobrenome = (EditText) findViewById(R.id.edtSobrenome);
        btnFinalizaCadastro = (Button) findViewById(R.id.btnFinalizaCadastro);

        intent = getIntent();
        b = intent.getExtras();

        emailBundleIntent = b.get("email").toString();

        try {
            edtNome.setText(b.get("nome").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnFinalizaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalizaCadastro();

            }
        });

        imgButtonProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });
    }

    private void finalizaCadastro() {

        //pegar strings dos campos da activity
        final String nome = edtNome.getText().toString().trim();
        final String sobrenome = edtSobrenome.getText().toString().trim();
        final String funcao;

        if(emailBundleIntent.substring(emailBundleIntent.indexOf("@")).equals(emailImobiliaria)) {
            funcao = funcaoCorretor;
        } else {
            funcao = funcaoCliente;
        }

        //final String funcao = funcaoCliente;

        /*if(spinnerCadastro.getSelectedItemPosition() == 0) {
            funcao = funcaoCorretor;
        } else {
            funcao = funcaoCliente;
        }*/


        //userId recebe o ID do usuario criado no firebase
        final String userId = mAuth.getCurrentUser().getUid();

        if (!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(sobrenome) && mImageUri != null) {

            mProgress.setMessage(getApplicationContext().getResources().getString(R.string.progressFinalizaCadastro));
            mProgress.show();

            StorageReference filepath = mStorageImage.child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String downloadUri = taskSnapshot.getDownloadUrl().toString();

                    mDatabaseUsuarios.child(userId).child("nome").setValue(nome);
                    mDatabaseUsuarios.child(userId).child("sobrenome").setValue(sobrenome);
                    mDatabaseUsuarios.child(userId).child("image").setValue(downloadUri);
                    mDatabaseUsuarios.child(userId).child("funcao").setValue(funcao);

                    mProgress.dismiss();

                    mDatabaseUsuarios.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.child(userId).child("funcao").getValue().equals("corretor")) {

                                Intent dashboardIntent = new Intent(FinalizaCadastroActivity.this, DashboardActivity.class);
                                dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(dashboardIntent);

                            } else {

                                Intent contratoIntent = new Intent(FinalizaCadastroActivity.this, ContratoActivity.class);
                                contratoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(contratoIntent);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });

        } else {
            Toast.makeText(FinalizaCadastroActivity.this, R.string.toastCamposEmpty, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            // start picker to get image for cropping and then use the image in cropping activity
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();
                imgButtonProfilePic.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }

}
