package com.motel.mobileproject_motelrental.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.motel.mobileproject_motelrental.Interface.OnItemClickListener;
import com.motel.mobileproject_motelrental.Item.ChipItem;
import com.motel.mobileproject_motelrental.Item.TagItem;
import com.motel.mobileproject_motelrental.R;
import com.nex3z.flowlayout.FlowLayout;

import java.util.List;

public class TagAdapter {
    private List<TagItem> tagItemList;
    private OnItemClickListener listener;

    public TagAdapter(List<TagItem> tagItemList) {
        this.tagItemList = tagItemList;
    }

    public void attachToFlowLayout(FlowLayout flowLayout) {
        LayoutInflater inflater = LayoutInflater.from(flowLayout.getContext());

        for (int i = 0; i < tagItemList.size(); i++) {
            final int position = i; // Đặt final để sử dụng trong listener
            TagItem tagItem = tagItemList.get(i);
            View view = inflater.inflate(R.layout.layout_tien_ich, flowLayout, false);
            TextView tag = view.findViewById(R.id.name);
            tag.setText(tagItem.getTagText());
            flowLayout.addView(view);
        }
    }
}
