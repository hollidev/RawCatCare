package edu.lapinamk.holli.rawcatcare;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Henna Olli in 04/2015.
 */
public class ChartBuilder extends HorizontalBarChart
{

    private HorizontalBarChart chart;
    private LinearLayout.LayoutParams mChartParams;

    public ChartBuilder(Context context)
    {
        super(context);

        // initializing the size parameters to be used for each chart, width matches parent view and height is 200dp
        mChartParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 200);
    }

    // this method builds charts
    public LinearLayout build(HashMap<String, Double> needs, HashMap<String, Double> gets, LinearLayout container)
    {
        // nutrient names will be the x-values of the chart
        ArrayList<String> nutrientNames = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.nutrients)));
        ArrayList<String> readableNutrientNames = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.readable_nutrients)));

        // looping through all the nutrient names and making a chart for each one
        for (int i = 0; i < nutrientNames.size(); i++)
        {
            try
            {
                chart = new HorizontalBarChart(getContext());

                /*
                 a list of one holding the nutrient name has to be made because
                 the constructor of BarData does not accept a single String
                 */
                ArrayList<String> nameList = new ArrayList<>(1);
                nameList.add(nutrientNames.get(i));

                // getting the value of the nutrient and casting to float throws a NullPointerException if the value is missing
                double needVal = needs.get(nutrientNames.get(i));
                double getVal = gets.get(nutrientNames.get(i));
                float needFloat = (float) needVal;
                float getFloat = (float) getVal;

                // if the mcg value is over 10000, show it as milligrams instead
                // this step is optional
                if (needFloat > 10000 || getFloat > 10000)
                {
                    needFloat = needFloat / 1000;
                    getFloat = getFloat / 1000;
                }

                // these arraylists will hold the entire 1 entries that will go in the chart
                ArrayList<BarEntry> needEntryList = new ArrayList<>(1);
                ArrayList<BarEntry> getEntryList = new ArrayList<>(1);
                needEntryList.add(new BarEntry(needFloat, 0));
                getEntryList.add(new BarEntry(getFloat, 0));

                // making datasets out of the lists of BarEntries and assigning a String to be shown in the legend of the chart
                BarDataSet neededNutrientSet = new BarDataSet(needEntryList, "optimal");
                BarDataSet gottenNutrientSet = new BarDataSet(getEntryList, "in menu");


                neededNutrientSet.setColor(Color.GREEN);
                gottenNutrientSet.setColor(Color.BLUE);

                ArrayList<BarDataSet> chartDataSet = new ArrayList<>();
                chartDataSet.add(neededNutrientSet);
                chartDataSet.add(gottenNutrientSet);

                // finally, declaring and initializing the actual BarData object
                // adding the datasets can also be done with chartData.addDataset()
                BarData chartData = new BarData(nameList, chartDataSet);

                // there will be minimal space between the groups
                chartData.setGroupSpace(0);

                chart.setData(chartData);

                styleChart();

                chart.setLayoutParams(mChartParams);

                /*
                in addition to adding the chart to the view, it is prepended with a subtitle showing the name of the nutrient
                 */
                TextView subtitleView = new TextView(getContext());
                subtitleView.setTextAppearance(getContext(), R.style.Base_TextAppearance_AppCompat_Subhead);
                subtitleView.setText(readableNutrientNames.get(i));
                container.addView(subtitleView);
                container.addView(chart);

                // after the chart, a line is added to increase clarity
                View separator = new View(getContext());
                separator.setBackgroundColor(Color.GRAY);
                separator.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));

                container.addView(separator);


            } catch (NullPointerException e) // catches any missing values
            {
                e.printStackTrace();
            }
        }

        return container;

    }

    private void styleChart()
    {
        /*
        attempting to remove all gridlines from the chart and the axes
        does not actually remove all the lines for some reason
         */
        chart.setDrawBorders(false);
        chart.setDrawGridBackground(false);
        chart.setDescription("");

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLimitLinesBehindData(false);
        xAxis.setDrawLabels(false);

        YAxis yAxisR = chart.getAxisRight();
        yAxisR.setDrawAxisLine(false);
        yAxisR.setDrawGridLines(false);
        yAxisR.setShowOnlyMinMax(true);
        yAxisR.setDrawLimitLinesBehindData(false);
        yAxisR.setDrawTopYLabelEntry(false);
        yAxisR.setEnabled(false);
        yAxisR.setLabelCount(0);

        YAxis yAxisL = chart.getAxisRight();
        yAxisL.setDrawAxisLine(false);
        yAxisL.setDrawGridLines(false);
        yAxisL.setShowOnlyMinMax(true);
        yAxisL.setDrawLimitLinesBehindData(false);
        yAxisL.setDrawTopYLabelEntry(false);
        yAxisL.setLabelCount(0);
        yAxisL.setEnabled(false);
    }
}
