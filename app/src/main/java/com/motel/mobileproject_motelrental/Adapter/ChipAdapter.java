package com.motel.mobileproject_motelrental.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.motel.mobileproject_motelrental.Item.ChipItem;
import com.motel.mobileproject_motelrental.R;
import com.nex3z.flowlayout.FlowLayout;

import java.util.List;

public class ChipAdapter{
    private List<ChipItem> chipItemList;

    public ChipAdapter(List<ChipItem> chipItemList) {
        this.chipItemList = chipItemList;
    }

    public void attachToFlowLayout(FlowLayout flowLayout) {
        LayoutInflater inflater = LayoutInflater.from(flowLayout.getContext());

        for (ChipItem chipItem : chipItemList) {
            View view = inflater.inflate(R.layout.layout_dac_diem, flowLayout, false);
            TextView textView = view.findViewById(R.id.chip_dacdiem);
            textView.setText(chipItem.getChipText());
            flowLayout.addView(view);
        }
    }
}
