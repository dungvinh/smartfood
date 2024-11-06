package com.sel.smartfood.ui.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sel.smartfood.R;
import com.sel.smartfood.viewmodel.AdminViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentStatistics extends Fragment {
    private TextView numberOfTransTv;
    private PieChart numTransPc;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_admin_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        numberOfTransTv = view.findViewById(R.id.tv_number_transactions);
        numTransPc = view.findViewById(R.id.pc_num_trans);

        numTransPc.setRotationEnabled(false);
        numTransPc.setDescription(null);
        numTransPc.setExtraOffsets(5, 10, 5, 5);
        numTransPc.setDragDecelerationFrictionCoef(0.99f);
        numTransPc.setDrawHoleEnabled(true);
        numTransPc.setHoleColor(Color.WHITE);
        numTransPc.setTransparentCircleRadius(62);

        AdminViewModel adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        adminViewModel.getAllTransHistories();
        adminViewModel.getTranHistories().observe(getViewLifecycleOwner(), transHistories -> {
            if (transHistories != null){
                numberOfTransTv.setText(String.valueOf(transHistories.size()));
            }
        });

        adminViewModel.getNumTypeTrans().observe(getViewLifecycleOwner(), arr ->{
            if (arr != null) {
                ArrayList<PieEntry> values = new ArrayList<>();
                values.add(new PieEntry(arr[0], 0));
                values.add(new PieEntry(arr[1], 1));
                PieDataSet dataSet = new PieDataSet(values, "Giao dịch");
                dataSet.setSliceSpace(3);
                dataSet.setSelectionShift(5);
                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                PieData data = new PieData(dataSet);
                data.setValueTextSize(10);
                data.setValueTextColor(Color.YELLOW);
                numTransPc.setData(data);
                int[] colors = ColorTemplate.JOYFUL_COLORS;
                Legend legend = numTransPc.getLegend();
                LegendEntry labelEntryA = new LegendEntry();
                labelEntryA.label = "Rút tiền";
                labelEntryA.formColor = colors[0];
                LegendEntry labelEntryB = new LegendEntry();
                labelEntryB.label = "Nạp tiền";
                labelEntryB.formColor = colors[1];
                legend.setCustom(Arrays.asList(labelEntryA, labelEntryB));
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                legend.setOrientation(Legend.LegendOrientation.VERTICAL);
                legend.setXEntrySpace(10);
                legend.setYEntrySpace(0);
                numTransPc.invalidate();
            }
        });
    }
}
