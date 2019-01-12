package com.example.anirudh.notes;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.util.NumberUtils;
import com.google.android.gms.flags.impl.DataUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

import javax.xml.transform.Templates;

public class Upload extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    Uri DocUri;
    String finalcomb;
    ProgressDialog progressDialog;
    TextView textView;
    String filename;
    Firebase myfirebase;
    String user;
String sname;
String file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        finalcomb = intent.getStringExtra("finalcomb");

        String url1="https://notes-d46cc.firebaseio.com/Subjects/"+finalcomb;
        // Toast.makeText(nav.this,url1,Toast.LENGTH_LONG).show();
        Firebase.setAndroidContext(getApplicationContext());
        myfirebase = new Firebase(url1);
        final List<String> subnames1 = new ArrayList<>();
        myfirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        String fname =
                                dsp.child("subject").getValue(String.class);

                        subnames1.add(fname);

                        Spinner areaSpinner = (Spinner) findViewById(R.id.spinner3);
                        final ArrayAdapter<String> backupAdapter = new ArrayAdapter<String>(
                                Upload.this, android.R.layout.simple_spinner_item, subnames1);
                        backupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        backupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        areaSpinner.setAdapter(backupAdapter);
                        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                sname = parent.getItemAtPosition(position).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    }


                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseStorage = FirebaseStorage.getInstance();
                }
                catch (Exception e)
                {
                    Toast.makeText(Upload.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }





            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
    });

    }

            public void onclickselectfile(View view)
    {
        if(ContextCompat.checkSelfPermission(Upload.this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
        {
            selectPdf();        }
        else
        {
            ActivityCompat.requestPermissions(Upload.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},9);

        }





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectPdf();
        }
        else
        {
            Toast.makeText(Upload.this,"Please Grant Permission",Toast.LENGTH_LONG).show();
        }
    }

    private void selectPdf() {



        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType( "*/*");
        startActivityForResult(intent, 86);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==86&&resultCode==RESULT_OK&& data!=null)

        {
            DocUri = data.getData();
            textView = (TextView) findViewById(R.id.textdisplay);
            final String extension = getMimeType(Upload.this, DocUri);
             file = DocUri.getLastPathSegment();
            if(file.contains(":"))
            {
                file=file.substring(file.indexOf(":")+1,file.length());
            }


            if(NumberUtils.isNumeric(file))
            {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
                final EditText etUsername = alertLayout.findViewById(R.id.et_username);



                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("File Name not Found.Please Enter a File Name");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        filename = etUsername.getText().toString()+"."+extension;
                        textView.setText(filename);

                                            }
                });
                AlertDialog dialog = alert.create();
                dialog.show();






            }





            if (file.contains("."))
            {


                filename=file;
                textView.setText(filename);


            }
            else
            {
                filename=file+"."+extension;
                textView.setText(filename);
            }


        }
        else
        {
            Toast.makeText(Upload.this,"Please Select a File",Toast.LENGTH_LONG).show();

        }
    }
    public void onclickUpload(View view)

    {
        if(DocUri!=null)
        {
            uploadPdf(DocUri);
        }
        else
        {
            Toast.makeText(Upload.this,"No FILE SELECTED",Toast.LENGTH_LONG).show();
        }





    }

    private void uploadPdf(Uri docUri) {
        try {
            progressDialog = new ProgressDialog(Upload.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Uploadingggg.... xD");
            progressDialog.setProgress(0);
            progressDialog.show();

            final StorageReference reference = firebaseStorage.getReference();
            reference.child("Notes").child(finalcomb).child(sname).child(filename).putFile(docUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            StorageReference storageRef = firebaseStorage.getReference();

// Create a reference with an initial file path and name

                            Log.w("url", "Notes/" + finalcomb + "/" + sname + "/" + filename);
                            storageRef.child("Notes/" + finalcomb + "/" + sname + "/" + filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.w("url", uri.toString());
                                    DatabaseReference databaseReference = firebaseDatabase.getReference();
                                    databaseReference.child("Subject").child(finalcomb).child(sname).child(filename.substring(0, filename.indexOf("."))).setValue(new url_mc(uri.toString()))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Upload.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();
                                                    progressDialog.hide();
                                                    Intent intent = new Intent(Upload.this, nav.class);
                                                    finish();
                                                    overridePendingTransition(R.xml.slide_in_left, R.xml.slide_out_right);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Upload.this, "Upload Failed", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                    databaseReference.child("files").child(finalcomb).child(sname).child(filename.substring(0, filename.indexOf("."))).setValue(new subject_mc(filename));

                                }

                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Upload.this, "failure", Toast.LENGTH_LONG).show();

                                        }
                                    });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Upload.this, "Upload Failed", Toast.LENGTH_LONG).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    long progress= 100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();

                    progressDialog.setProgress((int) progress);
                    if(progress>0 && progress<10)
                    {
                        progressDialog.setTitle("Uploading..\uD83D\uDE1E");
                    }
                    else if (progress>10 && progress<20)
                    {
                        progressDialog.setTitle("Uploading..\uD83D\uDE1F");
                    }
                    else if (progress>20 && progress<30)
                    {
                        progressDialog.setTitle("Uploading..\uD83D\uDE35 ");
                    }
                    else if (progress>30 && progress<40)
                    {
                        progressDialog.setTitle("Uploading..\uD83D\uDE31 ");
                    }
                    else if (progress>40 && progress<50)
                    {
                        progressDialog.setTitle("Uploading..\uD83D\uDE10");
                    }
                    else if (progress>50 && progress<60)
                    {
                        progressDialog.setTitle("Uploading..\uD83D\uDE11");
                    }
                    else if (progress>70 && progress<80)
                    {
                        progressDialog.setTitle("Uploading..\uD83D\uDE43 ");

                    }
                    else if (progress>80 && progress<90)
                    {
                        progressDialog.setTitle("Uploading..\uD83D\uDE0A ");

                    }

                    else if (progress>90 && progress<100)
                    {
                        progressDialog.setTitle("Uploading..\uD83D\uDE09 ");

                    }


                }
            });


        }
        catch (Exception e)
        {
            Toast.makeText(Upload.this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.xml.slide_in_left, R.xml.slide_out_right);
    }
    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }
}
