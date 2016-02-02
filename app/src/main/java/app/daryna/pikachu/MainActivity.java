package app.daryna.pikachu;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import com.felipecsl.gifimageview.library.GifImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener, OnGesturePerformedListener {

    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 4.0;
    private MediaPlayer mp1, mp2, mp3;
    private GifImageView mGifPika;
    InputStream stream;

    GestureLibrary gestureLibrary = null;
    GestureOverlayView gestureOverlayView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gestureOverlayView = (GestureOverlayView) findViewById(R.id.gestures);
        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gesture);
        gestureLibrary.load();
        gestureOverlayView.addOnGesturePerformedListener(this);
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mp1 = MediaPlayer.create(this, R.raw.picapica);
        mp2 = MediaPlayer.create(this, R.raw.picacu);
        mp3 = MediaPlayer.create(this, R.raw.picachuuuuuu2);

        mGifPika = (GifImageView)findViewById(R.id.gif_pika);
        try {
            stream = getAssets().open("g.gif");
            mGifPika.setBytes(readBytes(stream));
            mGifPika.startAnimation();
        } catch (IOException e) {
            Log.e(e.getMessage(), "error"+e);
        }



    }

    public static byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;

            mInitialized = true;
        } else {
            float deltaX = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);
            float deltaZ = Math.abs(mLastZ - z);
            if (deltaX < NOISE) deltaX = (float) 0.0;
            if (deltaY < NOISE) deltaY = (float) 0.0;
            if (deltaZ < NOISE) deltaZ = (float) 0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            if (!(mp1.isPlaying() || mp2.isPlaying())) {
                if (deltaX > deltaY && deltaX > NOISE) {
                    try {
                        stream = getAssets().open("t.gif");
                        mGifPika.setBytes(readBytes(stream));
                        mGifPika.startAnimation();
                    } catch (IOException e) {
                        Log.e(e.getMessage(), "error"+e);
                    }
                    mp2.start();
                    mp2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mGifPika.stopAnimation();
                        }

                    });
                } else if (deltaY > deltaX && deltaY > NOISE) {
                    try {
                        stream = getAssets().open("giphy.gif");
                        mGifPika.setBytes(readBytes(stream));
                        mGifPika.startAnimation();
                    } catch (IOException e) {
                        Log.e(e.getMessage(), "error"+e);
                    }
                    mp1.start();
                    mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mGifPika.stopAnimation();
                        }

                    });
                } else {
                    //ffffffff
                }
            }

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        // TODO Auto-generated method stub
        ArrayList<Prediction> prediction = gestureLibrary.recognize(gesture);
        if (prediction.size() > 0 && !(mp1.isPlaying() || mp2.isPlaying())) {
            try {
                stream = getAssets().open("pikachuuuu.gif");
                mGifPika.setBytes(readBytes(stream));
                mGifPika.startAnimation();
            } catch (IOException e) {
                Log.e(e.getMessage(), "error"+e);
            }
            mp3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mGifPika.stopAnimation();
                    mGifPika.setImageResource(R.drawable.thunderbolt);
                }

            });
            mp3.start();
        }
    }
}
