package bcalendargroups;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GroupsEventDialog {
    Context context;

    public static final int TYPE_START = 1;
    public static final int TYPE_END = 2;
    public static final int TYPE_PUSH = 3;

    EditText title;
    EditText info;
    Button start_date;
    Button end_date;
    Button push_date;
    Button start_time;
    Button end_time;
    Button push_time;
    Spinner color;
    Spinner category;
    Spinner country;
    Spinner city;
    Button file;
    int location = 33345;

    String file_path = "";

    int categor = 1;
    LinearLayout root;
    ScrollView root_scroll;
    int color_res = Color.RED;

    public GroupsEventDialog(Context context) {
        this.context = context;
        title = new EditText(context);
        info = new EditText(context);
        start_date = new Button(context);
        end_date = new Button(context);
        push_date = new Button(context);
        color = new Spinner(context);
        category = new Spinner(context);
        country = new Spinner(context);
        city = new Spinner(context);
        file = new Button(context);
        root = new LinearLayout(context);
        start_time = new Button(context);
        end_time = new Button(context);
        push_time = new Button(context);
        root_scroll = new ScrollView(context);

        start_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ShowDatePicker(TYPE_START);
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ShowDatePicker(TYPE_END);
            }
        });

        push_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ShowDatePicker(TYPE_PUSH);
            }
        });

        start_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ShowTimePicker(TYPE_START);
            }
        });

        end_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ShowTimePicker(TYPE_END);
            }
        });

        push_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ShowTimePicker(TYPE_PUSH);
            }
        });

        file.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

        setText();
    }

    public View GetView() {
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, 10);
        params.setMargins(5, 5, 5, 5);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        title.setHint("������� �������� �������");
        // title.setLayoutParams(params);
        root.addView(title);
        TextView text = new TextView(context);
        text.setText("���� ������ �������");
        text.setLayoutParams(params);
        root.addView(text);
        LinearLayout lin_horiz = new LinearLayout(context);
        lin_horiz.setOrientation(LinearLayout.HORIZONTAL);
        start_time.setLayoutParams(params);
        start_date.setLayoutParams(params);
        lin_horiz.addView(start_date);
        lin_horiz.addView(start_time);
        root.addView(lin_horiz);
        text = new TextView(context);
        text.setText("���� ���������� �������");
        text.setLayoutParams(params);
        root.addView(text);
        lin_horiz = new LinearLayout(context);
        lin_horiz.setOrientation(LinearLayout.HORIZONTAL);
        end_date.setLayoutParams(params);
        end_time.setLayoutParams(params);
        lin_horiz.addView(end_date);
        lin_horiz.addView(end_time);
        root.addView(lin_horiz);
        lin_horiz = new LinearLayout(context);
        lin_horiz.setOrientation(LinearLayout.HORIZONTAL);
        text = new TextView(context);
        text.setText("���� �����������");
        text.setLayoutParams(params);
        root.addView(text);
        push_date.setLayoutParams(params);
        push_time.setLayoutParams(params);
        lin_horiz.addView(push_date);
        lin_horiz.addView(push_time);
        root.addView(lin_horiz);
        info.setHint("��������� ����������");
        // info.setLayoutParams(params);
        root.addView(info);
        text = new TextView(context);
        text.setText("���������");
        text.setLayoutParams(params);
        root.addView(text);

//		ArrayList<Integer> itemsCategory = new ArrayList<Integer>();
//		AdapterCategoryPicker adapter = new AdapterCategoryPicker(
//				MainActivity.getInstance(), R.layout.category_pivker_item,
//				itemsCategory);
//		for (int i = 1; i <= 5; i++) {
//			itemsCategory.add(i);
//		}
//		category.setAdapter(adapter);
//		adapter.notifyDataSetChanged();

        category.setSelection(0);

        category.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                categor = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        root.addView(category);

        text = new TextView(context);
        text.setText("���� �������");
        text.setLayoutParams(params);
        root.addView(text);

        ArrayList<String> arrayColors = new ArrayList<String>();
        arrayColors.add("Red");
        arrayColors.add("Blue");
        arrayColors.add("Yelow");
        ArrayAdapter<String> adapter_color = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, arrayColors);
        color.setAdapter(adapter_color);
        // color.setLayoutParams(params);
        color.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                switch (position) {
                    case 0:
                        color_res = Color.RED;
                        break;
                    case 1:
                        color_res = Color.BLUE;
                        break;
                    case 2:
                        color_res = Color.YELLOW;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        root.addView(color);
        text = new TextView(context);
        text.setText("�����");
        text.setLayoutParams(params);
        root.addView(text);
        root.addView(country);
        root.addView(city);
        text = new TextView(context);
        text.setText("����");
        text.setLayoutParams(params);
        root.addView(text);
        root.addView(file);
        lin_horiz = new LinearLayout(context);
        lin_horiz.setOrientation(LinearLayout.HORIZONTAL);
        root.addView(lin_horiz);
        root_scroll.addView(root);
        return root_scroll;
    }

    public void ShowView() {
        AlertDialog d = new AlertDialog.Builder(context).create();
        d.setView(GetView());
        d.show();
    }

    public void setText() {

        String start_date_text = FormatDate(getNowYear()) + "-"
                + FormatDate(getNowMounth()) + "-" + FormatDate(getNowDay())
                + " " + FormatDate(getNowHour()) + ":"
                + FormatDate(getNowMinute());

        String end_date_text = MilisecToDate(StringToMilisec(start_date_text) + 3600000);
        String push_date_text = MilisecToDate(StringToMilisec(start_date_text) - 15 * 60000);
        end_date.setText(end_date_text.substring(0, 10));
        start_date.setText(start_date_text.substring(0, 10));
        push_date.setText(push_date_text.substring(0, 10));
        end_time.setText(end_date_text.substring(11));
        start_time.setText(start_date_text.substring(11));
        push_time.setText(push_date_text.substring(11));

    }

    public static int getNowYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public static int getNowMounth() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public static int getNowDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public static int getNowHour() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public static int getNowMinute() {
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        return Integer.parseInt(sdf.format(new Date()));
    }

    static public String FormatDate(int zn) {
        if (zn < 10)
            return "0" + zn;
        return zn + "";
    }

    public long StringToMilisec(String DateInString) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long res = 0;
        String DateInMilisec = "";
        try {
            Date date;
            try {
                date = formatDate.parse(DateInString);
                DateInMilisec = "" + date.getTime();
                res = Long.parseLong(DateInMilisec);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public String MilisecToDate(long DaateInMilisec) {
        long i = DaateInMilisec;
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(i);
        String s = formatDate.format(date);
        return s;
    }

    public void ShowDatePicker(final int type) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        final DatePicker picker = new DatePicker(context);

        dialog.setView(picker);

        dialog.setButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                String date = FormatDate(picker.getYear()) + "-"
                        + FormatDate(picker.getMonth() + 1) + "-"
                        + FormatDate(picker.getDayOfMonth());

                if (type == TYPE_START) {
                    start_date.setText(date);
                }

                if (type == TYPE_END) {
                    end_date.setText(date);
                }

                if (type == TYPE_PUSH) {
                    push_date.setText(date);
                }

            }
        });
        dialog.show();
    }

    public void ShowTimePicker(final int type) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        final TimePicker picker = new TimePicker(context);

        dialog.setView(picker);

        dialog.setButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                String time = FormatDate(picker.getCurrentHour()) + ":"
                        + FormatDate(picker.getCurrentMinute());

                if (type == TYPE_START) {
                    start_time.setText(time);
                }

                if (type == TYPE_END) {
                    end_time.setText(time);
                }

                if (type == TYPE_PUSH) {
                    push_time.setText(time);
                }

            }
        });

        dialog.show();

    }

    public int getColor() {
        return color_res;
    }

    public int getStartDateYear() {
        return Integer
                .parseInt(start_date.getText().toString().substring(0, 4));
    }

    public int getStartDateMonth() {
        return Integer
                .parseInt(start_date.getText().toString().substring(5, 7));
    }

    public int getStartDateDay() {
        return Integer.parseInt(start_date.getText().toString()
                .substring(8, 10));
    }

    public int getStartDateHour() {
        return Integer
                .parseInt(start_time.getText().toString().substring(0, 2));
    }

    public int getStartDateMinute() {
        return Integer
                .parseInt(start_time.getText().toString().substring(3, 5));
    }

    public int getEndDateYear() {
        return Integer.parseInt(end_date.getText().toString().substring(0, 4));
    }

    public int getEndDateMonth() {
        return Integer.parseInt(end_date.getText().toString().substring(5, 7));
    }

    public int getEndDateDay() {
        return Integer.parseInt(end_date.getText().toString().substring(8, 10));
    }

    public int getEndDateHour() {
        return Integer.parseInt(end_time.getText().toString().substring(0, 2));
    }

    public int getEndDateMinute() {
        return Integer.parseInt(end_time.getText().toString().substring(3, 5));
    }

    public String getPushTime() {
        return push_date.getText().toString() + " "
                + push_time.getText().toString();
    }

    public int getLocation() {
        return location;
    }

    public String getTitle() {
        return title.getText().toString();
    }

    public String getInfo() {
        return info.getText().toString();
    }

    public String getFile() {
        return file_path;
    }

    public int getCategory() {
        return categor;
    }

    public Map<String, Object> getData() {
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        jsonMap.put("title", String.valueOf(getTitle()));
        jsonMap.put("color", String.valueOf(getColor()));
        jsonMap.put("start_date_day", String.valueOf(getStartDateYear()));
        jsonMap.put("start_date_mounth", String.valueOf(getStartDateMonth()));
        jsonMap.put("start_date_year", String.valueOf(getStartDateDay()));
        jsonMap.put("start_date_hour", String.valueOf(getStartDateHour()));
        jsonMap.put("start_date_minute", String.valueOf(getStartDateMinute()));

        jsonMap.put("end_date_year", String.valueOf(getEndDateYear()));
        jsonMap.put("end_date_mounth", String.valueOf(getEndDateMonth()));
        jsonMap.put("end_date_day", String.valueOf(getEndDateDay()));
        jsonMap.put("end_date_hour", String.valueOf(getEndDateHour()));
        jsonMap.put("end_date_minute", String.valueOf(getEndDateMinute()));

        jsonMap.put("time_push", String.valueOf(getPushTime()));
        jsonMap.put("category", String.valueOf(getCategory()));
        jsonMap.put("location", String.valueOf(getLocation()));
        jsonMap.put("info", String.valueOf(getInfo()));
        jsonMap.put("file_path", String.valueOf(getFile()));

        jsonMap.put("id_group", "");

        return jsonMap;
    }

}
