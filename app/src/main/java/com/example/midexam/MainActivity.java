package com.example.midexam;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    EditText etName, etId;
    CheckBox cbEnrolled;
    TextView tvResult;
    Button btnAdd, btnEdit, btnDelete, btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etId = findViewById(R.id.etId);
        cbEnrolled = findViewById(R.id.cbEnrolled);
        tvResult = findViewById(R.id.tvResult);

        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etId.getText().toString().trim();
                String name = etName.getText().toString().trim();
                int enrolled = cbEnrolled.isChecked() ? 1 : 0;

                if (id.isEmpty() || name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isInserted = dbHelper.insertData(id, name, enrolled);
                if (isInserted) {
                    Toast.makeText(MainActivity.this, "Data successfully added", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(MainActivity.this, "Failed! Student ID is already registered.", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etId.getText().toString().trim();
                String name = etName.getText().toString().trim();
                int enrolled = cbEnrolled.isChecked() ? 1 : 0;

                if (id.isEmpty() || name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter Student ID and Name to update", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isUpdated = dbHelper.updateData(id, name, enrolled);
                if (isUpdated) {
                    Toast.makeText(MainActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to update (ID not found)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etId.getText().toString().trim();

                if (id.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter Student ID to delete", Toast.LENGTH_SHORT).show();
                    return;
                }

                Integer deletedRows = dbHelper.deleteData(id);
                if (deletedRows > 0) {
                    Toast.makeText(MainActivity.this, "Data successfully deleted", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(MainActivity.this, "Student ID not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = dbHelper.getAllData();
                if (cursor.getCount() == 0) {
                    tvResult.setText("There is no data in the database.");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (cursor.moveToNext()) {
                    buffer.append("ID: ").append(cursor.getString(0)).append("\n");
                    buffer.append("Name: ").append(cursor.getString(1)).append("\n");
                    String status = cursor.getInt(2) == 1 ? "Enrolled" : "Not Enrolled";
                    buffer.append("Status: ").append(status).append("\n");
                }
                tvResult.setText(buffer.toString());
            }
        });
    }

    private void clearFields() {
        etName.getText().clear();
        etId.getText().clear();
        cbEnrolled.setChecked(false);
    }
}