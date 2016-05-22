package ninja.taskbook.business.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import ninja.taskbook.R;
import ninja.taskbook.model.entity.TaskEntity;
import ninja.taskbook.util.helper.Helper;

//----------------------------------------------------------------------------------------------------
public class GroupTaskLineFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    private LineChartView chartView;
    private LineChartData chartData;

    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 12;

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    List<TaskEntity> mGroupTaskItems = null;

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_task_line, container, false);

        chartView = (LineChartView)rootView.findViewById(R.id.chart_view);
        chartView.setOnValueTouchListener(new ValueTouchListener());
        chartView.setViewportCalculationEnabled(false);
        generateValues();
        generateData();
        resetViewport();

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    void setGroupTaskItems(List<TaskEntity> groupTaskItems) {
        mGroupTaskItems = groupTaskItems;
        generateValues();
        if (chartView != null) {
            generateData();
            resetViewport();
        }
    }

    //----------------------------------------------------------------------------------------------------
    private void generateValues() {
        for (TaskEntity entity : mGroupTaskItems) {
            try {
                JSONObject beginningJsonData = new JSONObject(entity.taskBeginning);
                Calendar beginingCalendar = Helper.stringToCalendar(beginningJsonData.getString("date") + " " + beginningJsonData.getString("time"));
                JSONObject deadlineJsonData = new JSONObject(entity.taskDeadline);
                Calendar deadlineCalendar = Helper.stringToCalendar(deadlineJsonData.getString("date") + " " + deadlineJsonData.getString("time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 100f;
            }
        }
    }

    //----------------------------------------------------------------------------------------------------
    private void generateData() {
        List<Line> lines = new ArrayList<>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(ValueShape.CIRCLE);
            line.setCubic(false);
            line.setFilled(false);
            line.setHasLabels(false);
            line.setHasLabelsOnlyForSelected(false);
            line.setHasLines(true);
            line.setHasPoints(true);
            lines.add(line);
        }

        chartData = new LineChartData(lines);

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("Axis X");
        axisY.setName("Axis Y");
        chartData.setAxisXBottom(axisX);
        chartData.setAxisYLeft(axisY);

        chartData.setBaseValue(Float.NEGATIVE_INFINITY);
        chartView.setLineChartData(chartData);

    }

    private void resetViewport() {
        final Viewport viewPort = new Viewport(chartView.getMaximumViewport());
        viewPort.bottom = 0;
        viewPort.top = 100;
        viewPort.left = 0;
        viewPort.right = numberOfPoints - 1;
        chartView.setMaximumViewport(viewPort);
        chartView.setCurrentViewport(viewPort);
    }

    //----------------------------------------------------------------------------------------------------
    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }
    }
}
