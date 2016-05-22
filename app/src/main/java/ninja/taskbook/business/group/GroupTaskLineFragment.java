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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
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
    List<TaskEntity> mGroupTaskItems = null;
    List<HashMap.Entry<String, List<Calendar>>> mTaskCalendarList;

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
        HashMap<String, List<Calendar>> taskCalendarMap = new HashMap<>();
        for (TaskEntity entity : mGroupTaskItems) {
            try {
                //JSONObject beginningJsonData = new JSONObject(entity.taskBeginning);
                //Calendar beginningCalendar = Helper.stringToCalendar(beginningJsonData.getString("date") + " " + beginningJsonData.getString("time"));
                JSONObject deadlineJsonData = new JSONObject(entity.taskDeadline);
                Calendar deadlineCalendar = Helper.stringToCalendar(deadlineJsonData.getString("date") + " " + deadlineJsonData.getString("time"));

                List<Calendar> taskCalendars = taskCalendarMap.get(deadlineJsonData.getString("date"));
                if (taskCalendars == null) {
                    taskCalendars = new ArrayList<>();
                    taskCalendarMap.put(deadlineJsonData.getString("date"), taskCalendars);
                }
                taskCalendars.add(deadlineCalendar);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mTaskCalendarList = new ArrayList<>(taskCalendarMap.entrySet());
        Collections.sort(mTaskCalendarList, new Comparator<HashMap.Entry<String, List<Calendar>>>() {
            public int compare(HashMap.Entry<String, List<Calendar>> object1, HashMap.Entry<String, List<Calendar>> object2) {
                return object1.getKey().compareTo(object2.getKey());
            }
        });
    }

    //----------------------------------------------------------------------------------------------------
    private void generateData() {
        // Line
        List<Line> lines = new ArrayList<>();
        List<PointValue> values = new ArrayList<>();
        for (int i = 0; i < mTaskCalendarList.size(); ++i) {
            values.add(new PointValue(i, mTaskCalendarList.get(i).getValue().size()));
        }
        Line line = new Line(values);
        line.setColor(ChartUtils.COLORS[0]);
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(false);
        line.setFilled(false);
        line.setHasLabels(false);
        line.setHasLabelsOnlyForSelected(false);
        line.setHasLines(true);
        line.setHasPoints(true);
        lines.add(line);

        // AxisX
        List<AxisValue> axisXValues = new ArrayList<>();
        for (int i = 0; i < mTaskCalendarList.size(); ++i) {
            AxisValue value = new AxisValue(i);
            value.setLabel(mTaskCalendarList.get(i).getKey());
            axisXValues.add(value);
        }
        Axis axisX = new Axis();
        axisX.setHasLines(true);
        axisX.setValues(axisXValues);
        axisX.setName("日期");

        // AxisY
        Axis axisY = new Axis();
        axisY.setName("数量");

        chartData = new LineChartData(lines);
        chartData.setAxisXBottom(axisX);
        chartData.setAxisYLeft(axisY);
        chartData.setBaseValue(1.f);
        chartView.setLineChartData(chartData);

    }

    private void resetViewport() {
        final Viewport viewPort = new Viewport(chartView.getMaximumViewport());
        viewPort.bottom = 0;
        viewPort.top = 5; // Todo
        viewPort.left = 0;
        viewPort.right = mTaskCalendarList.size();
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
        }
    }
}
