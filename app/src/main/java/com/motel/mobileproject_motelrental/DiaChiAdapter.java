package com.motel.mobileproject_motelrental;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DiaChiAdapter extends ArrayAdapter<DiaChi> {
    private  String hint;
    private  boolean isSelected;
    public void SetSelect(boolean bool){
        isSelected = bool;
    }
    public DiaChiAdapter(@NonNull Context context, int resource, @NonNull List<DiaChi> objects, String hint) {
        super(context, resource, objects);
        this.hint = hint;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected, parent, false);
        }
        TextView tvSelected = convertView.findViewById(R.id.tvSelectItem);
        DiaChi diaChi = this.getItem(position);
        if(diaChi!=null && isSelected){
            tvSelected.setText(diaChi.getName());
        }
        else{
            tvSelected.setHint(hint);
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        TextView tvItem = convertView.findViewById(R.id.tvItem);
        DiaChi diaChi = this.getItem(position);
        if(diaChi != null){
            tvItem.setText(diaChi.getName());
        }
        return convertView;
    }
}
