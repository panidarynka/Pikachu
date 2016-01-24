package app.daryna.pikachu;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button pica = (Button)findViewById(R.id.pika);
        Button picachu = (Button)findViewById(R.id.pikachu);
        pica.setOnClickListener(this);
        picachu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pika:
                MediaPlayer mp1 = MediaPlayer.create(this, R.raw.picapica);
                mp1.start();
                break;
            case R.id.pikachu:
                MediaPlayer mp2 = MediaPlayer.create(this, R.raw.picacu);
                mp2.start();
                break;

        }

    }
}
