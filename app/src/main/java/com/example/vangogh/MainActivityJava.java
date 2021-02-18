package com.example.vangogh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vangogh.media.config.VanGogh;
import com.vangogh.media.itf.OnMediaResult;
import com.vangogh.media.models.MediaItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainActivityJava extends AppCompatActivity {


    private Button btn;

    private TextView media_tv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_java);
        btn = findViewById(R.id.btn);
        media_tv = findViewById(R.id.media_tv);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VanGogh.INSTANCE.getMedia(true).startForResult(MainActivityJava.this).setOnMediaResult(new OnMediaResult() {
                    @Override
                    public void onResult(@NotNull List<MediaItem> mediaList) {
                       for(int i = 0;i< mediaList.size();i++){
                           media_tv.append(mediaList.get(i).getCompressPath()+"\n");
                       }

                    }
                });

            }
        });

    }
}