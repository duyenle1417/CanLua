package com.example.test;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class InputWeightActivity extends AppCompatActivity
        implements EditWeightDialog.EditWeightDialogListener {
    GridView gridView_weight;
    EditText editText_weight;
    TextView textView_weight_preview;
    Button button_add_weight;
    TextView textView_info_sumOfBag;
    TextView textView_info_sumOfWeight;
    TextView textView_money;
    List<Double> listWeight;
    GridViewWeightAdapter adapter;
    int sumOfBag = 0;
    double sumOfWeight = 0;
    double money = 0;
    int dongia = 0, trubi = 8;
    String FILE_NAME, name, phone, datejoin, dateCreate;
    Gson gson = new Gson();
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_weight_layout);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        dongia = intent.getIntExtra("dongia", 0);
        trubi = intent.getIntExtra("trubi", 8);
        datejoin = intent.getStringExtra("datejoin");
        dateCreate = intent.getStringExtra("dateCreate");
        dateCreate.replace(" ", "-");
        FILE_NAME = dateCreate + ".txt";

        getView();
        listWeight = new ArrayList<>();//khởi tạo mảng

        //đọc file JSON lưu vô mảng listWeight
        ReadJSONFile();

        //adapter cho gridview
        adapter = new GridViewWeightAdapter(InputWeightActivity.this, R.layout.gridview_input_weight_item, listWeight);
        gridView_weight.setAdapter(adapter);
        //tính tổng số ký lúa và hiện số bao lúa
        AddWeight();
        AddNumOfBag();

        //khi editText có nhập dữ liệu mới thì hiện textview có số thập phân
        editText_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText_weight.removeTextChangedListener(this);

                String weight = editText_weight.getText().toString();
                //kiểm tra có nhập số 0 đầu tiên không
                //nếu có thì xóa đi vd: 025 là lỗi
                if (weight.length() == 1)
                    IsZero();

                //nếu có 2 chữ số thì hiện textview preview
                if (weight.length() > 1) {
                    textView_weight_preview.setText(AddFloatingPoint(weight));
                    textView_weight_preview.setVisibility(View.VISIBLE);
                } else
                    textView_weight_preview.setVisibility(View.INVISIBLE);
                editText_weight.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //nhấn btn thì thêm vào gridview và thay đổi tổng số ký và số bao lúa
        button_add_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView_weight_preview.getVisibility() == View.VISIBLE) {//check người dùng có nhập dữ liệu rồi
                    listWeight.add(Double.parseDouble(textView_weight_preview.getText().toString()));
                    adapter.notifyDataSetChanged();
                    editText_weight.getText().clear();//reset lại về trống
                    //chỉnh thông tin info
                    AddWeight();
                    AddNumOfBag();
                    AddMoney();
                    SaveJSONFile();
                    updateHistory();
                }
            }
        });

        //open dialog để chỉnh sửa giá trị
        gridView_weight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                String weightSet = position + "#" + listWeight.get(position);
                bundle.putString("weight", weightSet);
                EditWeightDialog dialog = new EditWeightDialog();
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "dialog edit weight data");
            }
        });


        HideKeyboardWhenTouchOutsideEditText();//ẩn keyboard đi
    }

    private void ReadJSONFile() {
        FileInputStream fileInputStream;
        try {
            fileInputStream = openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String text;
            text = br.readLine();

            Type type = new TypeToken<ArrayList<Double>>() {
            }.getType();
            listWeight = gson.fromJson(text, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void SaveJSONFile() {
        String text = gson.toJson(listWeight);//chỉ lưu cân kg
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fileOutputStream.write(text.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //ẩn keyboard khi người dùng tap bên ngoài edittext
    //không áp dụng được khi tap gridview
    //vì nếu làm cả gridview thì scroll không được
    private void HideKeyboardWhenTouchOutsideEditText() {
        findViewById(R.id.input_weight_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
    }

    //kiểm tra nhập số 0 đầu tiên hay không vd 025 là sai
    private void IsZero() {
        int number = Integer.parseInt(editText_weight.getText().toString());
        if (number == 0)
            editText_weight.setText("");
    }

    //lấy số bao = list.size
    private void AddNumOfBag() {
        sumOfBag = Math.max(listWeight.size(), 0);
        textView_info_sumOfBag.setText("" + sumOfBag);
    }

    //lấy weight trong mảng cộng lại
    private void AddWeight() {
        sumOfWeight = 0;
        if (listWeight.size() != 0) {
            for (int i = 0; i < listWeight.size(); i++) {
                sumOfWeight += listWeight.get(i);
            }
        }
        //làm tròn vì double có lỗi hiển thị => kết quả đúng
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        decimalFormat.setRoundingMode(RoundingMode.UP);

        textView_info_sumOfWeight.setText(decimalFormat.format(sumOfWeight));
    }

    private void AddMoney() {
        money = dongia * (sumOfWeight - ((1.0 / trubi) * sumOfBag));

        //làm tròn vì double có lỗi hiển thị => kết quả đúng
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        decimalFormat.setRoundingMode(RoundingMode.UP);
        textView_money.setText(decimalFormat.format(money));
    }

    //lấy các view findViewById()
    private void getView() {
        gridView_weight = findViewById(R.id.gridview_input_weight);
        editText_weight = findViewById(R.id.editText_input_weight);
        textView_weight_preview = findViewById(R.id.textView_input_weight_preview);
        button_add_weight = findViewById(R.id.btn_input_weight);
        textView_info_sumOfBag = findViewById(R.id.info_lb_sum_bag);
        textView_info_sumOfWeight = findViewById(R.id.info_lb_sum_weight);
        textView_money = findViewById(R.id.money_tv_data);

        //toolbar
        Toolbar toolbar = findViewById(R.id.input_weight_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.OK:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InputWeightActivity.this, HistoryActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        intent.putExtra("date", datejoin);
        intent.putExtra("reload", "yes");
        InputWeightActivity.this.startActivity(intent);
        InputWeightActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        InputWeightActivity.this.startActivity(intent);
        this.finish();
    }

    private void updateHistory() {
        DatabaseHelper helper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        sqLiteDatabase = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        String tongBao, tongKG, thanhTien;
        tongBao = textView_info_sumOfBag.getText().toString();
        tongKG = textView_info_sumOfWeight.getText().toString();
        thanhTien = String.valueOf(money);
        cv.put(DatabaseContract.HistoryTable.COLUMN_TONGBAO, tongBao);
        cv.put(DatabaseContract.HistoryTable.COLUMN_TONGKG, tongKG);
        cv.put(DatabaseContract.HistoryTable.COLUMN_THANHTIEN, thanhTien);

        String UPDATE_HISTORY = "UPDATE " + DatabaseContract.HistoryTable.TABLE_NAME +
                " SET " + DatabaseContract.HistoryTable.COLUMN_TONGBAO + " = " + tongBao +
                ", " + DatabaseContract.HistoryTable.COLUMN_TONGKG + " = " + tongKG +
                ", " + DatabaseContract.HistoryTable.COLUMN_THANHTIEN + " = " + thanhTien +
                " WHERE " + DatabaseContract.HistoryTable.COLUMN_TIMESTAMP + " LIKE '%" + dateCreate + "%'" +
                " AND " + DatabaseContract.HistoryTable.COLUMN_DATEJOIN + " LIKE '%" + datejoin + "%';";
        sqLiteDatabase.execSQL(UPDATE_HISTORY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_input, menu);
        setTitle("");
        return true;
    }

    //thêm dấu chấm động vào số
    //người dùng chỉ cần nhập số cho nhanh
    private String AddFloatingPoint(String input) {
        if (input.length() == 3) {//35.5 hay 35.0
            return input.substring(0, 2) + '.' + input.substring(2);
        } else
            return input + ".0";
    }

    //cập nhật số ký đã sửa
    @Override
    public void ApplyChange(String position, String weight) {
        listWeight.set(Integer.parseInt(position), Double.parseDouble(weight));
        adapter.notifyDataSetChanged();
        //tính lại tổng số cân nặng
        AddWeight();
        AddMoney();
        SaveJSONFile();
        updateHistory();//cập nhật DB số bao và tổng kg lúa
    }
}