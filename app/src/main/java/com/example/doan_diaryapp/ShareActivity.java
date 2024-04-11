package com.example.doan_diaryapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class ShareActivity extends BaseActivity {

    ImageView imageView;
    Bitmap overlayBitmap;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private final String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private final int[] dayImages = {R.drawable.ic_circle1, R.drawable.ic_circle2, R.drawable.ic_circle3,
            R.drawable.ic_circle4, R.drawable.ic_circle5, R.drawable.ic_circle3, R.drawable.ic_circle3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_share);

        imageView = findViewById(R.id.myImageView);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_share));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CreateImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap icon = bitmapDrawable.getBitmap();
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "title");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);

            OutputStream outStream;
            try {
                if (uri != null) {
                    outStream = getContentResolver().openOutputStream(uri);
                    if (outStream != null) {
                        icon.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        outStream.close();
                    }
                }
            } catch (Exception e) {
                System.err.println(e.toString());
            }

            share.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(share, "Share Image"));
        }
        if (id == R.id.action_download) {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
                saveImage();
            }
            else {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ShareActivity.this,
                                new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    }, REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                else {
                    saveImage();
                }
            }
        }
        if (id == android.R.id.home) {
            Intent intent = new Intent(ShareActivity.this, ActivityNam.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage();
            }
        }
        else {
            Toast.makeText(ShareActivity.this, "Please provide required permission", Toast.LENGTH_SHORT).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void saveImage() {
        Uri images;
        ContentResolver contentResolver = getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }
        else {
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/*");
        Uri uri = contentResolver.insert(images, contentValues);

        try {
            if (uri != null) {
                OutputStream outputStream = contentResolver.openOutputStream(uri);
                if (outputStream != null) {
                    overlayBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();
                }

                Snackbar.make(findViewById(android.R.id.content), R.string.image_saved, BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Snackbar.make(findViewById(android.R.id.content), R.string.image_not_saved, BaseTransientBottomBar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    void CreateImage() {
        Calendar calendar = Calendar.getInstance();

        TextView monthYearTextView = findViewById(R.id.monthYearTextView);
        monthYearTextView.setText(new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.getTime()));

        GridLayout calendarGrid = findViewById(R.id.calendarGrid);
        for (int i = 0; i < 7; i++) {
            TextView dayNameTextView = new TextView(this);
            dayNameTextView.setText(dayNames[i]);
            dayNameTextView.setTextColor(getResources().getColor(R.color.gray));
            dayNameTextView.setTextSize(16);
            dayNameTextView.setGravity(Gravity.CENTER);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(i, 1f);
            params.bottomMargin = 25;
            dayNameTextView.setLayoutParams(params);

            calendarGrid.addView(dayNameTextView);
        }

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);

        List<Integer> imagesList = generateImagesList(firstDayOfMonth, daysInMonth);

        int index = 0;
        int margin = 14;
        for (int i = 0; i < imagesList.size(); i++) {
            ImageView dayImageView = new ImageView(this);
            dayImageView.setImageResource(imagesList.get(i));
            dayImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            int sizeInDp = 105;
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = sizeInDp;
            params.height = sizeInDp;
            params.columnSpec = GridLayout.spec(index, 1f);
            params.setMargins(margin, margin, margin, margin);
            dayImageView.setLayoutParams(params);

            calendarGrid.addView(dayImageView);

            index = (index + 1) % 7;
        }

        final ViewTreeObserver vto = calendarGrid.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = calendarGrid.getWidth();
                int height = calendarGrid.getHeight() + 150;

                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                calendarGrid.draw(canvas);

                ImageView imageView = findViewById(R.id.myImageView);

                Drawable drawable = imageView.getDrawable();

                Bitmap originalBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas drawableCanvas = new Canvas(originalBitmap);
                drawable.setBounds(0, 0, width, height);
                drawable.draw(drawableCanvas);

                overlayBitmap = Bitmap.createBitmap(width, height, originalBitmap.getConfig());
                Canvas overlayCanvas = new Canvas(overlayBitmap);

                overlayCanvas.drawBitmap(originalBitmap, 0, 0, null);

                String text = monthYearTextView.getText().toString();

                Paint paint = new Paint();
                Rect textBounds = new Rect();
                paint.getTextBounds(text, 0, text.length(), textBounds);
                int color = getResources().getColor(R.color.colorShareText);
                paint.setColor(color);
                paint.setTextSize(70);

                int monthYearTextViewX = (overlayBitmap.getWidth() - monthYearTextView.getWidth()) / 2;

                int calendarGridX = (overlayBitmap.getWidth() - width) / 2;

                overlayCanvas.drawText(text, monthYearTextViewX, 100, paint);

                overlayCanvas.drawBitmap(bitmap, calendarGridX, 140, null);

                imageView.setImageBitmap(overlayBitmap);

                calendarGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private List<Integer> generateImagesList(int firstDayOfMonth, int daysInMonth) {
        List<Integer> imagesList = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i < firstDayOfMonth; i++) {
            imagesList.add(0);
        }

        for (int i = 1; i <= daysInMonth; i++) {
            int randomImageIndex = random.nextInt(dayImages.length);
            imagesList.add(dayImages[randomImageIndex]);
        }

        return imagesList;
    }
}