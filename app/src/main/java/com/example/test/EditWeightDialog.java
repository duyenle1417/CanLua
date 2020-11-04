package com.example.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

public class EditWeightDialog extends AppCompatDialogFragment {

    EditText editText_weight;
    TextView textView_preview;
    EditWeightDialogListener listener;
    String[] arr;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_weight_editor,null);

        editText_weight = view.findViewById(R.id.edit_weight_dialog_item);
        textView_preview = view.findViewById(R.id.textview_weight_preview_dialog_item);

        Bundle bundle = getArguments();
        arr= bundle.getString("weight").split("#");
        textView_preview.setText(arr[1]);//hiển thị giá trị cũ

        builder.setView(view)
                .setTitle("Nhập số ký bao lúa")
                .setNegativeButton("hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newWeight = (String) textView_preview.getText().toString();
                        listener.ApplyChange(arr[0], newWeight);
                    }
                });

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
                    textView_preview.setText(AddFloatingPoint(weight));
                } else
                    textView_preview.setText(arr[1]);
                editText_weight.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return  builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditWeightDialogListener) context;
        } catch (ClassCastException e) {
            throw new  ClassCastException(context.toString() +
                    "must implement EditWeightDialogListener");
        }
    }

    private String AddFloatingPoint(String input) {
        if (input.length() == 3) {
            return input.substring(0, 2) + '.' + input.substring(2);
        }
        return input + ".0";
    }
    private void IsZero() {
        int number = Integer.parseInt(editText_weight.getText().toString());
        if (number == 0)
            editText_weight.setText("");
    }

    public interface EditWeightDialogListener{
        void ApplyChange(String position, String weight);
    }
}
