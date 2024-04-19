package com.motel.mobileproject_motelrental.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.motel.mobileproject_motelrental.Interface.OnItemClickListener;
import com.motel.mobileproject_motelrental.Item.ChipItem;
import com.motel.mobileproject_motelrental.R;
import com.nex3z.flowlayout.FlowLayout;

import java.util.List;

public class ChipAdapter{
    private List<ChipItem> chipItemList;
    private OnItemClickListener listener;

    public ChipAdapter(List<ChipItem> chipItemList) {
        this.chipItemList = chipItemList;
    }

    public void setOnChipItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void attachToFlowLayout(FlowLayout flowLayout) {
        LayoutInflater inflater = LayoutInflater.from(flowLayout.getContext());

        for (int i = 0; i < chipItemList.size(); i++) {
            final int position = i; // Đặt final để sử dụng trong listener
            ChipItem chipItem = chipItemList.get(i);
            View view = inflater.inflate(R.layout.layout_dac_diem, flowLayout, false);
            Chip chip = view.findViewById(R.id.chip_dacdiem);
            chip.setText(chipItem.getChipText());
            flowLayout.addView(view);

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
