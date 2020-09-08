package com.example.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends AppCompatActivity {
    public static final  String EXTRA_DRINKID ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        int drinkId = getIntent().getIntExtra(EXTRA_DRINKID,0);//Extras().get(EXTRA_DRINKID);

        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("DRINKS", new String[] {"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id=?",
                    new String[] {Integer.toString(drinkId)}, null,
                    null,
                    null);
            TextView name = (TextView) findViewById(R.id.name);
            TextView description = (TextView) findViewById(R.id.description);
            ImageView photo = (ImageView) findViewById(R.id.photo);
            CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
            if(cursor.moveToFirst()){
                name.setText(cursor.getString(0));
                description.setText(cursor.getString(1));
                photo.setImageResource(cursor.getInt(2));
                photo.setContentDescription(cursor.getString(1));
                favorite.setChecked(cursor.getInt(3)==1);
            }
            cursor.close();
            db.close();
        } catch (SQLiteException exc) {
            Toast toast = Toast.makeText(this, "Database unavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onFavoriteClicked(View view) {
        int drinkId = getIntent().getIntExtra(EXTRA_DRINKID,0);//Extras().get(EXTRA_DRINKID);
        CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("FAVORITE", favorite.isChecked());

        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
            db.update("DRINKS", drinkValues, "_id = ?", new String[] {Integer.toString(drinkId)});
            db.close();
        } catch (SQLiteException exc) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
