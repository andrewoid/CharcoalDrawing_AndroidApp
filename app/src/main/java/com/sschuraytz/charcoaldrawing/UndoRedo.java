package com.sschuraytz.charcoaldrawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import java.util.Stack;

public class UndoRedo {

    protected Stack<Bitmap> currentStack = new Stack<>();
    private Stack<Bitmap> undoneStack = new Stack<>();
    private Canvas bitmapCanvas;

    public UndoRedo() {
        onSizeChanged(1, 1);
    }

    //ensure we always have a bitmap to display
    protected void onSizeChanged(int width, int height) {
        currentStack.clear();
        undoneStack.clear();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        currentStack.push(bitmap);
        bitmapCanvas = new Canvas(bitmap);
        //this prevents saved drawings from displaying black charcoal on default (black) background
        bitmapCanvas.drawColor(Color.WHITE);
    }

    public void addBitmap() {
        Bitmap currentTop = currentStack.peek();
        Bitmap newBitmap = Bitmap.createBitmap(currentTop, 0, 0, currentTop.getWidth(), currentTop.getHeight());
        currentStack.push(newBitmap);
        bitmapCanvas = new Canvas(newBitmap);
        //after undo, if draw new line, do not let user redo
        undoneStack.clear();
    }

    public Bitmap getCurrentBitmap() {
        return currentStack.peek();
    }

    public boolean undo() {
        if (currentStack.size() > 1) {
            undoneStack.push(currentStack.pop());
            bitmapCanvas = new Canvas(currentStack.peek());
            return true;
        }
        return false;
    }

    public boolean redo() {
        if (undoneStack.size() > 0 ) {
            currentStack.push(undoneStack.pop());
            return true;
        }
        return false;
    }

    public void createNewCanvas() {
        int width = getCurrentBitmap().getWidth();
        int height = getCurrentBitmap().getHeight();
        onSizeChanged(width, height);
    }

    public Canvas getBitmapCanvas() {
        return bitmapCanvas;
    }

}
