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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddCustomerDialog extends AppCompatDialogFragment {
    EditText editText_name;
    EditText editText_phone;
    SQLiteDatabase sqLiteDatabase;
    AddCustomerListener listener;

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
                        String name = editText_name.getText().toString();
                        String phone = editText_phone.getText().toString();
                        if (ValidateData(name)) {
                            AddCustomer(name, phone);//DB
                            listener.ApplyChange(name, phone);
                        } else {
                            Toast.makeText(view.getContext(), "Lỗi! Không được để trống tên.", Toast.LENGTH_SHORT).show();
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
        sqLiteDatabase = helper.getWritableDatabase();

        //
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.CustomerTable.COLUMN_TENKH, name);
        if (phone.length() > 0)
            cv.put(DatabaseContract.CustomerTable.COLUMN_SDT, phone);
        else
            cv.put(DatabaseContract.CustomerTable.COLUMN_SDT, "---");
        cv.put(DatabaseContract.CustomerTable.COLUMN_TIMESTAMP, date);


        //
        sqLiteDatabase.insert(DatabaseContract.CustomerTable.TABLE_NAME, null, cv);
    }

    public interface AddCustomerListener {
        void ApplyChange(String name, String phone);
    }
}
