package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class InputCustomerInfoActivity extends AppCompatActivity {

    EditText editText_tengiong;
    EditText editText_dongia;
    EditText editText_trubi;
    EditText editText_tiencoc;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_customer_info_layout);
        getView();
    }

    private void getView() {

        editText_tengiong = findViewById(R.id.edittext_tenLua_info);
        editText_dongia = findViewById(R.id.edittext_dongia_info);
        editText_trubi = findViewById(R.id.edittext_trubi_info);
        editText_tiencoc = findViewById(R.id.edittext_tiencoc_info);

        //
        Toolbar toolbar = findViewById(R.id.info_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.input_info, menu);
        setTitle("");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.OK:
                Intent intent = new Intent(InputCustomerInfoActivity.this, InputWeightActivity.class);
                InputCustomerInfoActivity.this.startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
