package com.motel.mobileproject_motelrental.Custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CustomDialog {
    public static void showConfirmationDialog(Context context, int iconResId, String title, String message, boolean isSingleChoice, ConfirmationDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(iconResId);

        // Nếu loại là 1 lựa chọn
        if (isSingleChoice) {
            builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onOKClicked();
                    dialog.dismiss();
                }
            });
        } else { // Nếu loại là 2 lựa chọn
            builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onOKClicked();
                    // Xử lý khi nhấn nút OK
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onCancelClicked();
                    dialog.dismiss();
                }
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
