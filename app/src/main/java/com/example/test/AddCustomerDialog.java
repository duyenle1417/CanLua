package com.example.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddCustomerDialog extends AppCompatDialogFragment {
    HomeActivity context;
    TextInputLayout inputName, inputPhone;
    EditText editText_name;
    EditText editText_phone;
    Button btnAdd;
    ArrayList<Customer> arrayList;
    //SQLiteDatabase sqLiteDatabase;
    AddCustomerListener listener;

    public AddCustomerDialog(HomeActivity context, ArrayList<Customer> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_customer_add, null);

        editText_name = view.findViewById(R.id.edittext_tenKH_dialog);
        editText_phone = view.findViewById(R.id.edittext_sdt_dialog);


        builder.setView(view)
                .setTitle("Nhập thông tin khách hàng")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String name = editText_name.getText().toString();
                        final String phone = editText_phone.getText().toString();
                        if(ValidateData(name)) {
                            AddCustomer(name, phone);
                            listener.getCustomerAll();
                            Toast.makeText(context, "Đã thêm "+ name +" vào danh sách", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Không được để tên trống!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            // Instantiate the EditDateDialogListener so we can send events to the host
            listener = (AddCustomerListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString() + " must implement EditDateDialogListener");
        }
    }

    private boolean ValidateData(String name) {
        if (name.length() > 0)
            return true;
        else {
            editText_name.setError("Không được để trống!");
            editText_name.requestFocus();
            return false;
        }
    }

    private void AddCustomer(String name, String phone) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = dateFormat.format(Calendar.getInstance().getTime());//time lấy khi ấn OK

        DatabaseHelper helper = new DatabaseHelper(getContext(), DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();

        String ADD_CUSTOMER = null;

        if(phone.length() > 0)
            ADD_CUSTOMER = "INSERT INTO " + DatabaseContract.CustomerTable.TABLE_NAME + " ('"+ DatabaseContract.CustomerTable.COLUMN_TENKH +"', '"+ DatabaseContract.CustomerTable.COLUMN_SDT +"', '"+ DatabaseContract.CustomerTable.COLUMN_TIMESTAMP +"') "
                    +"VALUES ('"+ name +"', '"+ phone +"', '"+ date +"');";
        else
            ADD_CUSTOMER = "INSERT INTO " + DatabaseContract.CustomerTable.TABLE_NAME + " ('"+ DatabaseContract.CustomerTable.COLUMN_TENKH +"', '"+ DatabaseContract.CustomerTable.COLUMN_SDT +"', '"+ DatabaseContract.CustomerTable.COLUMN_TIMESTAMP +"') "
                    +"VALUES ('"+ name +"', '---', '"+ date +"');";
        db.execSQL(ADD_CUSTOMER);

    }

    public interface AddCustomerListener {
        //void ApplyChange(String name, String phone);
        void getCustomerAll();
    }

}
