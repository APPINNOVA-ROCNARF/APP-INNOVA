package com.rocnarf.rocnarf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rocnarf.rocnarf.adapters.ImageImpulsacionRecyclerViewAdapter;
import com.rocnarf.rocnarf.models.ImageImpulsacion;

import java.util.ArrayList;

public class VisitasImpulsadorasResultadoActivity extends AppCompatActivity {
    Button mCargar;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitas_impulsadoras_resultado);

        mCargar = (Button)findViewById(R.id.bt_cargar_activity_visitas_impulsadoras_resultado);
        mCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(takePicture, 0);//zero can be replaced with any action code
//
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            }
        });
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);


        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(gridLayoutManager);
        ArrayList imageUrlList = prepareData();
        ImageImpulsacionRecyclerViewAdapter dataAdapter = new ImageImpulsacionRecyclerViewAdapter(getApplicationContext(), imageUrlList);
        recyclerView.setAdapter(dataAdapter);

    }

    private ArrayList prepareData() {

// here you should give your image URLs and that can be a link from the Internet
        String imageUrls[] = {
                "https://www.diariofarma.com/wp-content/uploads/2018/11/Reunion-con-el-equipo-de-la-farmacia-213x142.jpg",
                "http://i2.wp.com/www.in-storemedia.com/wp-content/uploads/sites/40/2015/05/post4.jpg?resize=573%2C381"
                };

        ArrayList imageUrlList = new ArrayList<>();
        for (int i = 0; i < imageUrls.length; i++) {
            ImageImpulsacion imageUrl = new ImageImpulsacion();
            imageUrl.setImageUrl(imageUrls[i]);
            imageUrlList.add(imageUrl);
        }
        return imageUrlList;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Toast.makeText(this, selectedImage.toString(), Toast.LENGTH_LONG).show();
                    //imageview.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Toast.makeText(this, selectedImage.toString(), Toast.LENGTH_LONG).show();

                    //imageview.setImageURI(selectedImage);
                }
                break;
        }
    }
}
