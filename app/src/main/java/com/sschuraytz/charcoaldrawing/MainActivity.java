package com.sschuraytz.charcoaldrawing;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity
        implements VoiceListener {

    private DrawingView drawingView;
    private SaveDrawing saveDrawing;
    private FloatingActionButton fab;
    private VoiceCommands voiceCommands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideSystemUI();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveDrawing = new SaveDrawing(getApplicationContext());
        drawingView = findViewById(R.id.canvas);
        setUpVoiceCommands();
        setUpFAB();
    }

    public void setUpVoiceCommands() {
        voiceCommands = new VoiceCommands(this);
        voiceCommands.setListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    public void hideSystemUI()
    {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN  //hide status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setUpFAB() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            voiceCommands.checkVoicePermissions();
            voiceCommands.listenToUserCommand();
            //show user that mic is active
            fab.setColorFilter(Color.RED);
        });
    }

    public void updateFABUI() {
        fab.setImageResource(R.drawable.ic_mic_foreground);
    }

    public void charcoalCommand() {
        drawingView.setDrawingMode();
    }

    public void eraserCommand() {
        drawingView.setEraseMode();
    }

    public boolean undoCommand() {
        return drawingView.undo();
    }

    public boolean redoCommand() {
        return drawingView.redo();
    }

    public void createNewCanvasCommand() {
        drawingView.createNewCanvas();
    }

    public void updateDrawingThickness(int radius) { drawingView.setRadius(radius); }

    public void saveDrawing() {
        SaveDialogFragment saveDialog = new SaveDialogFragment();
        saveDialog.show(getSupportFragmentManager(), "save");
        saveDialog.setCancelable(false);
        //could allow user to choose jpeg or png
        saveDrawing.saveBitmap(getApplicationContext(), drawingView.undoRedo.getCurrentBitmap(), Bitmap.CompressFormat.JPEG,"image/jpeg", "user input");
        // drawingView.save();
    }
    public void help() {
        HelpDialogFragment helpDialog = new HelpDialogFragment();
        helpDialog.show(getSupportFragmentManager(), "help");
    };
}
