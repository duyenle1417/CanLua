package com.example.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AddCustomerDialog extends AppCompatDialogFragment {
    EditText editText_name;
    EditText editText_phone;
    AddCustomerDialogListener listener;

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
                        if (name.length() > 0 && phone.length() > 0) {
                            listener.AddItemView(name, phone);
                        } else {
                            Toast.makeText(view.getContext(), "Hãy nhập đầy đủ cả hai khung nhập!", Toast.LENGTH_SHORT).show();
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
            listener = (AddCustomerDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement AddCustomerDialogListener");
        }
    }

    public interface AddCustomerDialogListener {
        void AddItemView(String name, String phone);
    }
}
