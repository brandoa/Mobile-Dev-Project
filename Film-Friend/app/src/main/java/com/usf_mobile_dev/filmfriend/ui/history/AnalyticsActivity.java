package com.usf_mobile_dev.filmfriend.ui.history;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.usf_mobile_dev.filmfriend.Movie;
import com.usf_mobile_dev.filmfriend.MovieListing;
import com.usf_mobile_dev.filmfriend.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnalyticsActivity extends AppCompatActivity {

    private AnalyticsViewModel analyticsViewModel;
    private List<Integer> decadeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        analyticsViewModel =
                new ViewModelProvider(this).get(AnalyticsViewModel.class);
        TextView totalMovies = findViewById(R.id.totalMovies);
        TextView averageRating = findViewById(R.id.averageRating);
        NumberFormat formatter = new DecimalFormat("#0.0");
        decadeList = new ArrayList<>();
        BarChart barChart = (BarChart) findViewById(R.id.movies_by_decade_chart);
        barChart.setTouchEnabled(false);
        List<BarEntry> entries = new ArrayList<>();
        Context context = this;


        analyticsViewModel.getAllMovies().observe(this, new Observer<List<MovieListing>>() {
            @Override
            public void onChanged(@Nullable final List<MovieListing> movies) {

                analyticsViewModel.setStatistics(Objects.requireNonNull(movies));
                decadeList = analyticsViewModel.getDecadeList();
                totalMovies.setText(String.valueOf(analyticsViewModel.getTotalMovies()));
                averageRating.setText(String.valueOf(formatter.format(analyticsViewModel.getAverageRating())));
                entries.clear();
                for(int i = 0; i < decadeList.size(); i++)
                {
                    Log.d("BAR_CHART", "adding entry");
                    entries.add(new BarEntry(i,analyticsViewModel.getDecadeCount(decadeList.get(i))));
                }
                /*entries.add(new BarEntry(0f, 30f));
                entries.add(new BarEntry(1f, 80f));
                entries.add(new BarEntry(2f, 60f));
                entries.add(new BarEntry(3f, 50f));*/
                BarDataSet set = new BarDataSet(entries, "Movies by Decade");

                set.setColors(new int[] {R.color.purple_500}, context);
                BarData data = new BarData(set);
                data.setValueTextSize((float)18.0);
                data.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getBarLabel(BarEntry barEntry) {

                        int value = (int)barEntry.getY();
                        Log.d("BAR_CHART", super.getBarLabel(barEntry));
                        return String.format("%d",value);
                    }
                });
                barChart.setData(data);
                barChart.getDescription().setEnabled(false);
                barChart.getLegend().setEnabled(false);
                barChart.setExtraBottomOffset(10);

                XAxis x_axis = barChart.getXAxis();
                x_axis.setPosition(XAxis.XAxisPosition.BOTTOM);
                x_axis.setDrawGridLines(false);
                x_axis.setGranularity(1);
                x_axis.setTextSize(16);
                //x_axis.setDrawLabels(true);
                Log.d("BAR_CHART", "formatting label");
                x_axis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        Log.d("BAR_CHART", "returning label");
                        return String.valueOf(decadeList.get((int)value));
                    }
                });

                YAxis left_axis = barChart.getAxisLeft();
                left_axis.setGranularity(1);
                left_axis.setAxisMinimum(0);
                left_axis.setTextSize(12);

                YAxis right_axis = barChart.getAxisRight();
                right_axis.setEnabled(false);

                barChart.setFitBars(true);
                barChart.invalidate();
            }
        });

        /*PieChart pie = (PieChart) findViewById(R.id.genre_pie_chart);
        PieChart test = (PieChart) findViewById(R.id.test_pie_chart);
        pie.setTouchEnabled(false);
        test.setTouchEnabled(false);

        List<PieEntry> entries = new ArrayList<>();
        PieEntry x = new PieEntry(30.8f, "Blue");

        entries.add(new PieEntry(18.5f, "Green"));
        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));

        PieDataSet set = new PieDataSet(entries, "Election Results");

        set.setColors(new int[] {R.color.green, R.color.yellow, R.color.red, R.color.blue}, this);

        PieData data = new PieData(set);
        data.setValueTextSize((float) 16.0);
        pie.setData(data);
        test.setData(data);*/


    }
}
