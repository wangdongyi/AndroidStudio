package com.gizwits.opensource.appkit.ControlModule;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.opensource.appkit.CommonModule.MyGizWifiDevice;
import com.gizwits.opensource.appkit.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import wdy.business.event.MessageEvent;
import wdy.business.listen.NoDoubleClickListener;
import wdy.business.util.CodeUtil;
import wdy.business.util.MaterialUtil;
import wdy.business.util.StatusBarUtil;
import wdy.business.view.CircleSeekBar;

import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.clean;
import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.selected1;
import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.selected2;
import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.selected3;
import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.selected4;
import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.selected5;
import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.selected6;
import static com.gizwits.opensource.appkit.ControlModule.SelectedWeekActivity.selected7;

public class GosDeviceControlActivity extends GosControlModuleBaseActivity {

    /**
     * 设备列表传入的设备变量
     */
    private GizWifiDevice mDevice;
    private Button button;
    private Spinner nice_spinner;
    private TextView textView_time;
    private ImageView imageView_close;
    private boolean isEdit = false;//是否编辑
    private Animator animator;
    private int dip50;
    private TimePickerDialog dpd;
    private AppCompatImageView imageView_back;
    private AppCompatImageView imageView_back_close;
    private TextView textView_title;
    private AppCompatImageView imageView_right;
    private TextView textView_keller;
    private TextView textView_type;
    private LinearLayout single_layout;
    private TextView left_double_textView;
    private TextView left_double_hint;
    private LinearLayout left_double_layout;
    private TextView right_double_textView;
    private TextView right_double_hint;
    private LinearLayout right_double_layout;
    private LinearLayout main_double_layout;
    private TextView left_one_textView;
    private TextView left_one_hint;
    private LinearLayout left_one_layout;
    private TextView right_one_textView;
    private TextView right_one_hint;
    private LinearLayout right_one_layout;
    private TextView left_two_textView;
    private TextView left_two_hint;
    private LinearLayout left_two_layout;
    private TextView right_two_textView;
    private TextView right_two_hint;
    private LinearLayout right_two_layout;
    private TextView left_three_textView;
    private TextView left_three_hint;
    private LinearLayout left_three_layout;
    private TextView right_three_textView;
    private TextView right_three_hint;
    private LinearLayout right_three_layout;
    private LinearLayout main_six_layout;
    private TextView textView_start_time;
    private RelativeLayout start_time_layout;
    private TextView textView_end;
    private TextView textView_end_time;
    private RelativeLayout end_time_layout;
    private TextView textView_repeat;
    private RelativeLayout repeatlayout;
    private LinearLayout layout_automatic;
    private TextView textView_qualified;
    private TextView textView_qualified_time;
    private RelativeLayout qualified_layout;
    private LinearLayout main_qualified_layout;
    private CircleSeekBar circle_seekBar;
    private TextView textView_num;
    private ImageView imageView_jian;
    private ImageView imageView_jia;
    private ImageView imageView_open_button;
    private RelativeLayout close_layout;
    private ImageView waringImage;
    private boolean rightOpen = true;
    private int starHour, starMinute, endHour, endMinute, timingHour, timingMinute;
    private Calendar mCalendar;
    private int selectedPosition;
    private int optionType = 0;
    private NestedScrollView nestedScrollView;
    private Switch switch1;
    private Switch switch2;
    private Switch switch3;
    private Switch switch4;
    private Switch switch5;
    private Switch switch6;
    private Switch switch7;
    private Switch switch8;
    private Switch switch9;
    private Switch switch10;
    private TextView close_button;
    private Switch switch11;
    private TextView temp_textView;
    private TextView textView_qualified_left;
    private TextView textView_qualified_time_left;
    private RelativeLayout qualified_layout_left;
    private TextView textView_qualified_right;
    private TextView textView_qualified_time_right;
    private RelativeLayout qualified_layout_right;
    private LinearLayout qualified_layout_double;
    private boolean userDone = false;
    private ArrayList<ItemViewBean> viewBeanArrayList = new ArrayList<>();

    private enum handler_key {
        //更新界面
        UPDATE_UI,
        UPDATE,
        DISCONNECT,
    }

    private Runnable mRunnable = new Runnable() {
        public void run() {
            if (isDeviceCanBeControlled()) {
                progressDialog.cancel();
            } else {
                toastDeviceNoReadyAndExit();
            }
        }
    };

    /**
     * The handler.
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler_key key = handler_key.values()[msg.what];
            switch (key) {
                case UPDATE_UI:
                    if (!isEdit)
                        updateUI();
                    break;
                case UPDATE:
                    userDone = false;
                    isEdit = false;
                    initAnimation();
                    getStatusOfDevice();
                    break;
                case DISCONNECT:
                    toastDeviceDisconnectAndExit();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_device_control);
        StatusBarUtil.setStatusBarDark(getWindow(), false);
        initDevice();
        initView();
        getStatusOfDevice();
        initDate();
        initEvent();
    }

    private void initDate() {
        dip50 = CodeUtil.dip2px(this, 50);
    }

    private void initView() {
        button = (Button) findViewById(R.id.button);
        nice_spinner = (Spinner) findViewById(R.id.nice_spinner);
        textView_time = (TextView) findViewById(R.id.textView_time);
        imageView_close = (ImageView) findViewById(R.id.imageView_close);
        imageView_back = (AppCompatImageView) findViewById(R.id.imageView_back);
        imageView_back_close = (AppCompatImageView) findViewById(R.id.imageView_back_close);
        textView_title = (TextView) findViewById(R.id.textView_title);
        imageView_right = (AppCompatImageView) findViewById(R.id.imageView_right);
        textView_keller = (TextView) findViewById(R.id.textView_keller);
        textView_type = (TextView) findViewById(R.id.textView_type);
        single_layout = (LinearLayout) findViewById(R.id.single_layout);
        left_double_textView = (TextView) findViewById(R.id.left_double_textView);
        left_double_hint = (TextView) findViewById(R.id.left_double_hint);
        left_double_layout = (LinearLayout) findViewById(R.id.left_double_layout);
        right_double_textView = (TextView) findViewById(R.id.right_double_textView);
        right_double_hint = (TextView) findViewById(R.id.right_double_hint);
        right_double_layout = (LinearLayout) findViewById(R.id.right_double_layout);
        main_double_layout = (LinearLayout) findViewById(R.id.main_double_layout);
        left_one_textView = (TextView) findViewById(R.id.left_one_textView);
        left_one_hint = (TextView) findViewById(R.id.left_one_hint);
        left_one_layout = (LinearLayout) findViewById(R.id.left_one_layout);
        right_one_textView = (TextView) findViewById(R.id.right_one_textView);
        right_one_hint = (TextView) findViewById(R.id.right_one_hint);
        right_one_layout = (LinearLayout) findViewById(R.id.right_one_layout);
        left_two_textView = (TextView) findViewById(R.id.left_two_textView);
        left_two_hint = (TextView) findViewById(R.id.left_two_hint);
        left_two_layout = (LinearLayout) findViewById(R.id.left_two_layout);
        right_two_textView = (TextView) findViewById(R.id.right_two_textView);
        right_two_hint = (TextView) findViewById(R.id.right_two_hint);
        right_two_layout = (LinearLayout) findViewById(R.id.right_two_layout);
        left_three_textView = (TextView) findViewById(R.id.left_three_textView);
        left_three_hint = (TextView) findViewById(R.id.left_three_hint);
        left_three_layout = (LinearLayout) findViewById(R.id.left_three_layout);
        right_three_textView = (TextView) findViewById(R.id.right_three_textView);
        right_three_hint = (TextView) findViewById(R.id.right_three_hint);
        right_three_layout = (LinearLayout) findViewById(R.id.right_three_layout);
        main_six_layout = (LinearLayout) findViewById(R.id.main_six_layout);
        textView_start_time = (TextView) findViewById(R.id.textView_start_time);
        start_time_layout = (RelativeLayout) findViewById(R.id.start_time_layout);
        textView_end = (TextView) findViewById(R.id.textView_end);
        textView_end_time = (TextView) findViewById(R.id.textView_end_time);
        end_time_layout = (RelativeLayout) findViewById(R.id.end_time_layout);
        textView_repeat = (TextView) findViewById(R.id.textView_repeat);
        repeatlayout = (RelativeLayout) findViewById(R.id.repeatlayout);
        layout_automatic = (LinearLayout) findViewById(R.id.layout_automatic);
        textView_qualified = (TextView) findViewById(R.id.textView_qualified);
        textView_qualified_time = (TextView) findViewById(R.id.textView_qualified_time);
        qualified_layout = (RelativeLayout) findViewById(R.id.qualified_layout);
        main_qualified_layout = (LinearLayout) findViewById(R.id.main_qualified_layout);
        circle_seekBar = (CircleSeekBar) findViewById(R.id.circle_seekBar);
        textView_num = (TextView) findViewById(R.id.textView_num);
        imageView_jian = (ImageView) findViewById(R.id.imageView_jian);
        imageView_jia = (ImageView) findViewById(R.id.imageView_jia);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        imageView_open_button = (ImageView) findViewById(R.id.imageView_open_button);
        close_layout = (RelativeLayout) findViewById(R.id.close_layout);
        switch1 = (Switch) findViewById(R.id.switch1);
        switch2 = (Switch) findViewById(R.id.switch2);
        switch3 = (Switch) findViewById(R.id.switch3);
        switch4 = (Switch) findViewById(R.id.switch4);
        switch5 = (Switch) findViewById(R.id.switch5);
        switch6 = (Switch) findViewById(R.id.switch6);
        switch7 = (Switch) findViewById(R.id.switch7);
        switch8 = (Switch) findViewById(R.id.switch8);
        switch9 = (Switch) findViewById(R.id.switch9);
        switch10 = (Switch) findViewById(R.id.switch10);
        switch11 = (Switch) findViewById(R.id.switch11);
        waringImage = (ImageView) findViewById(R.id.waringImage);
        close_button = (TextView) findViewById(R.id.close_button);
        textView_qualified_left = (TextView) findViewById(R.id.textView_qualified_left);
        textView_qualified_time_left = (TextView) findViewById(R.id.textView_qualified_time_left);
        qualified_layout_left = (RelativeLayout) findViewById(R.id.qualified_layout_left);
        textView_qualified_right = (TextView) findViewById(R.id.textView_qualified_right);
        textView_qualified_time_right = (TextView) findViewById(R.id.textView_qualified_time_right);
        qualified_layout_right = (RelativeLayout) findViewById(R.id.qualified_layout_right);
        qualified_layout_double = (LinearLayout) findViewById(R.id.qualified_layout_double);
        temp_textView = (TextView) findViewById(R.id.temp_textView);
        buildItem(left_one_layout, left_one_textView, switch4, true);
        buildItem(left_two_layout, left_two_textView, switch6, false);
        buildItem(left_three_layout, left_three_textView, switch8, false);
        buildItem(right_one_layout, right_one_textView, switch5, false);
        buildItem(right_two_layout, right_two_textView, switch7, false);
        buildItem(right_three_layout, right_three_textView, switch9, false);
    }

    private void buildItem(LinearLayout linearLayout, TextView textView, Switch switch1, Boolean b) {
        ItemViewBean itemViewBean = new ItemViewBean();
        itemViewBean.setaSwitch(switch1);
        itemViewBean.setLinearLayout(linearLayout);
        itemViewBean.setTextView(textView);
        itemViewBean.setSelected(b);
        viewBeanArrayList.add(itemViewBean);
    }

    //关机动画
    private void initAnimation() {
        if (isEdit) {
            isEditType();
        } else {
            isShowType();
        }
    }

    private void initEvent() {
        setTitle(getDeviceName());
        setRightImage(true);
        clean();
        initClick();
        initSpinner();
        initSeekBar();
    }

    private void isEditType() {
        close_layout.setVisibility(View.GONE);
        imageView_right.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
    }

    private void isShowType() {
        close_layout.setVisibility(View.VISIBLE);
        imageView_right.setVisibility(View.INVISIBLE);
        button.setVisibility(View.GONE);
    }

    private void initClick() {
        imageView_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        imageView_back_close.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        MaterialUtil.MaterialBigRound(button);
        imageView_right.setVisibility(View.INVISIBLE);
        close_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myToast("想修改请先点右上角解锁");
            }
        });
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand();
            }
        });
        imageView_right.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                isChange();
                if (userDone) {
                    showAlertDialog("您有修改没有提交，是否提交？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            userDone = false;
                            sendCommand();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            isEdit = false;
                            initAnimation();
                            getStatusOfDevice();
                        }
                    });
                } else {
                    isEdit = false;
                    initAnimation();
                    getStatusOfDevice();
                }
            }
        });
        imageView_open_button.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                userDone = false;
                isEdit = true;
                initAnimation();
            }
        });
        imageView_jian.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circle_seekBar.getCurProcess() > 0) {
                    userDone = true;
                    circle_seekBar.setCurProcess(circle_seekBar.getCurProcess() - 1);
                }

            }
        });
        imageView_jia.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circle_seekBar.getCurProcess() < 40) {
                    userDone = true;
                    circle_seekBar.setCurProcess(circle_seekBar.getCurProcess() + 1);
                }
            }
        });
        start_time_layout.setOnClickListener(new NoDoubleClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onNoDoubleClick(View v) {
                if (switch11.isChecked()) {
                    TimeShow(starHour, starMinute, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            userDone = true;
                            starHour = hourOfDay;
                            starMinute = minute;
                            textView_start_time.setText((starHour < 10 ? "0" + starHour : starHour) + ":" + (starMinute < 10 ? "0" + starMinute : starMinute));
                        }
                    });
                } else {
                    myToast("设备关机中不能修改");
                }

            }
        });
        end_time_layout.setOnClickListener(new NoDoubleClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onNoDoubleClick(View v) {
                if (switch11.isChecked()) {
                    TimeShow(endHour, endMinute, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            userDone = true;
                            endHour = hourOfDay;
                            endMinute = minute;
                            textView_end_time.setText((endHour < 10 ? "0" + endHour : endHour) + ":" + (endMinute < 10 ? "0" + endMinute : endMinute));
                        }
                    });
                } else {
                    myToast("设备关机中不能修改");
                }

            }
        });
        qualified_layout.setOnClickListener(new NoDoubleClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onNoDoubleClick(View v) {
                if (switch11.isChecked()) {
                    TimeShow(timingHour, timingMinute, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            userDone = true;
                            timingHour = hourOfDay;
                            timingMinute = minute;
                            textView_qualified_time.setText((timingHour < 10 ? "0" + timingHour : timingHour) + ":" + (timingMinute < 10 ? "0" + timingMinute : timingMinute));
                        }
                    });
                } else {
                    myToast("设备关机中不能修改");
                }


            }
        });
        repeatlayout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (switch11.isChecked()) {
                    userDone = true;
                    Intent intent = new Intent(GosDeviceControlActivity.this, SelectedWeekActivity.class);
                    startActivity(intent);
                } else {
                    myToast("设备关机中不能修改");
                }

            }
        });
        qualified_layout_left.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (switch11.isChecked()) {
                    //双温区-左-定时时间
                    TimeShow(timingHour, timingMinute, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            userDone = true;
                            timingHour = hourOfDay;
                            timingMinute = minute;
                            textView_qualified_time_left.setText((timingHour < 10 ? "0" + timingHour : timingHour) + ":" + (timingMinute < 10 ? "0" + timingMinute : timingMinute));
                        }
                    });
                } else {
                    myToast("设备关机中不能修改");
                }

            }
        });
        qualified_layout_right.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (switch11.isChecked()) {
                    //双温区-右-定时时间
                    TimeShow(Hour_double, Minutes_double, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            userDone = true;
                            Hour_double = hourOfDay;
                            Minutes_double = minute;
                            textView_qualified_time_right.setText((Hour_double < 10 ? "0" + Hour_double : Hour_double) + ":" + (Minutes_double < 10 ? "0" + Minutes_double : Minutes_double));
                        }
                    });
                } else {
                    myToast("设备关机中不能修改");
                }

            }
        });
        left_double_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = 0;
                left_double_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                left_double_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                right_double_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_double_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                circle_seekBar.setCurProcess(baseBean.getList().get(0).getNum() - 20);
                if (switch2.isChecked()) {
                    left_double_layout.setBackgroundColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.selected_color));
                    circle_seekBar.setEnabled(true);
                    imageView_jian.setClickable(true);
                    imageView_jia.setClickable(true);
                } else {
                    left_double_layout.setBackgroundColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.gray));
                    circle_seekBar.setEnabled(false);
                    imageView_jian.setClickable(false);
                    imageView_jia.setClickable(false);
                }
                if (!switch3.isChecked()) {
                    right_double_layout.setBackgroundColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.gray));
                } else {
                    right_double_layout.setBackgroundColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                }
            }
        });
        right_double_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = 1;
                circle_seekBar.setCurProcess(baseBean.getList().get(1).getNum() - 20);
                right_double_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                right_double_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                left_double_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_double_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                if (switch3.isChecked()) {
                    right_double_layout.setBackgroundColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.selected_color));
                    circle_seekBar.setEnabled(true);
                    imageView_jian.setClickable(true);
                    imageView_jia.setClickable(true);
                } else {
                    right_double_layout.setBackgroundColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.gray));
                    circle_seekBar.setEnabled(false);
                    imageView_jian.setClickable(false);
                    imageView_jia.setClickable(false);
                }
                if (!switch2.isChecked()) {
                    left_double_layout.setBackgroundColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.gray));
                } else {
                    left_double_layout.setBackgroundResource(R.color.white);
                }
            }
        });
        for (int i = 0; i < viewBeanArrayList.size(); i++) {
            final ItemViewBean bean = viewBeanArrayList.get(i);
            final int finalI = i;
            bean.getLinearLayout().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = finalI;
                    for (int j = 0; j < viewBeanArrayList.size(); j++) {
                        if (finalI == j) {
                            viewBeanArrayList.get(finalI).setSelected(true);
                        } else {
                            viewBeanArrayList.get(j).setSelected(false);
                        }
                    }
                    selectedSix();
                }
            });
            bean.getaSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //6温区
                    userDone = true;
                    if (bean.isSelected()) {
                        if (isChecked) {
                            circle_seekBar.setEnabled(true);
                            imageView_jian.setClickable(true);
                            imageView_jia.setClickable(true);
                        } else {
                            circle_seekBar.setEnabled(false);
                            imageView_jian.setClickable(false);
                            imageView_jia.setClickable(false);
                        }
                    }
                    selectedSix();
                }
            });
        }
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //单独开关
                if (type == 0) {
                    userDone = true;
                    if (isChecked) {
                        single_layout.setBackgroundColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                        circle_seekBar.setEnabled(true);
                        imageView_jian.setClickable(true);
                        imageView_jia.setClickable(true);
                    } else {
                        single_layout.setBackgroundColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.gray));
                        circle_seekBar.setEnabled(false);
                        imageView_jian.setClickable(false);
                        imageView_jia.setClickable(false);
                    }
                }
            }
        });
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //双开关
                userDone = true;
                if (isChecked) {
                    if (selectedPosition == 0) {
                        left_double_layout.setBackgroundColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.selected_color));
                    } else {
                        left_double_layout.setBackgroundColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                    }
                    circle_seekBar.setEnabled(true);
                    imageView_jian.setClickable(true);
                    imageView_jia.setClickable(true);
                } else {
                    left_double_layout.setBackgroundColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.gray));
                    circle_seekBar.setEnabled(false);
                    imageView_jian.setClickable(false);
                    imageView_jia.setClickable(false);
                }
            }
        });
        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //双开关
                userDone = true;
                if (isChecked) {
                    if (selectedPosition == 1) {
                        right_double_layout.setBackgroundColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.selected_color));
                    } else {
                        right_double_layout.setBackgroundColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                    }
                    circle_seekBar.setEnabled(true);
                    imageView_jian.setClickable(true);
                    imageView_jia.setClickable(true);
                } else {
                    right_double_layout.setBackgroundColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.gray));
                    circle_seekBar.setEnabled(false);
                    imageView_jian.setClickable(false);
                    imageView_jia.setClickable(false);
                }
            }
        });
        switch11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDone = true;
                closeAllSwitch(isChecked);
            }
        });
    }

    //设置标题
    private void setTitle(String title) {
        textView_title.setText(title);
    }

    //设置开关
    private void setRightImage(Boolean open) {
        if (open) {
            imageView_right.setImageResource(R.mipmap.open);
            rightOpen = true;
        } else {
            imageView_right.setImageResource(R.mipmap.close);
            rightOpen = false;
        }

    }

    //下选框
    private void initSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_parent);

        adapter.add("手动");

        adapter.add("自动");

        adapter.add("定时");

        adapter.setDropDownViewResource(R.layout.spinner_item);

        nice_spinner.setAdapter(adapter);

        nice_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (baseBean == null) {
                    return;
                }
                optionType = position;
                if (baseBean.getType() != optionType) {
                    userDone = true;
                }
                switch (position) {
                    case 0:
                        //手动
                        textView_keller.setVisibility(View.VISIBLE);
                        switch10.setVisibility(View.VISIBLE);
                        layout_automatic.setVisibility(View.GONE);
                        main_qualified_layout.setVisibility(View.GONE);
                        qualified_layout_double.setVisibility(View.GONE);
                        if (baseBean.getManualBean() != null) {
                            if (baseBean.getAllSwitch())
                                switch10.setChecked(baseBean.getManualBean().getKillSwitch());
                            else
                                switch10.setChecked(false);
                        }
                        break;
                    case 1:
                        textView_keller.setVisibility(View.INVISIBLE);
                        switch10.setVisibility(View.INVISIBLE);
                        layout_automatic.setVisibility(View.VISIBLE);
                        main_qualified_layout.setVisibility(View.GONE);
                        qualified_layout_double.setVisibility(View.GONE);
                        AutomaticBean automaticBean = baseBean.getAutomaticBean();
                        if (automaticBean != null) {
                            getWeek(automaticBean.getWeek());
                            starHour = automaticBean.getStarHour();
                            starMinute = automaticBean.getStarMinute();
                            endHour = automaticBean.getEndHour();
                            endMinute = automaticBean.getEndMinute();
                            textView_start_time.setText((starHour < 10 ? "0" + starHour : starHour) + ":" + (starMinute < 10 ? "0" + starMinute : starMinute));
                            textView_end_time.setText((endHour < 10 ? "0" + endHour : endHour) + ":" + (endMinute < 10 ? "0" + endMinute : endMinute));
                        }
                        break;
                    case 2:
                        textView_keller.setVisibility(View.INVISIBLE);
                        switch10.setVisibility(View.INVISIBLE);
                        layout_automatic.setVisibility(View.GONE);
                        TimingBean timingBean = baseBean.getTimingBean();
                        if (timingBean != null) {
                            timingHour = timingBean.getHour();
                            timingMinute = timingBean.getMinute();
                            if (type == 1) {
                                qualified_layout_double.setVisibility(View.VISIBLE);
                                main_qualified_layout.setVisibility(View.GONE);
                                int hourR = timingBean.getHourRight();
                                int minR = timingBean.getMinuteRight();
                                textView_qualified_time_left.setText((timingHour < 10 ? "0" + timingHour : timingHour) + ":" + (timingMinute < 10 ? "0" + timingMinute : timingMinute));
                                textView_qualified_time_right.setText((hourR < 10 ? "0" + hourR : hourR) + ":" + (minR < 10 ? "0" + minR : minR));
                            } else {
                                qualified_layout_double.setVisibility(View.GONE);
                                main_qualified_layout.setVisibility(View.VISIBLE);
                                textView_qualified_time.setText((timingHour < 10 ? "0" + timingHour : timingHour) + ":" + (timingMinute < 10 ? "0" + timingMinute : timingMinute));
                            }
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        nice_spinner.setSelection(0);
    }

    //选中数字
    @SuppressLint("ClickableViewAccessibility")
    private void initSeekBar() {
        circle_seekBar.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    nestedScrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    nestedScrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        circle_seekBar.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onChanged(CircleSeekBar seekbar, int curValue) {
                setSelectedNum(curValue + 20);
            }
        });
    }

    //显示选中数字
    @SuppressLint("SetTextI18n")
    private void setSelectedNum(int value) {
        if (value >= 20)
            textView_num.setText(value + "");
        else
            textView_num.setText(20 + "");
        switch (type) {
            case 0:
                baseBean.getList().get(0).setNum(value);
                break;
            case 1:
                switch (selectedPosition) {
                    case 0:
                        baseBean.getList().get(0).setNum(value);
                        break;
                    case 1:
                        baseBean.getList().get(1).setNum(value);
                        break;
                }
                break;
            case 2:
                for (int i = 0; i < baseBean.getList().size(); i++) {
                    if (selectedPosition == i) {
                        baseBean.getList().get(i).setNum(value);
                    }
                }
        }
    }

    private void initType(int position) {
        switch (position) {
            case 0:
                textView_type.setText("单温区");
                single_layout.setVisibility(View.VISIBLE);
                main_double_layout.setVisibility(View.GONE);
                main_six_layout.setVisibility(View.GONE);
                break;
            case 1:
                textView_type.setText("双温区");
                single_layout.setVisibility(View.GONE);
                main_double_layout.setVisibility(View.VISIBLE);
                main_six_layout.setVisibility(View.GONE);
                break;
            case 2:
                textView_type.setText("多温区");
                single_layout.setVisibility(View.GONE);
                main_double_layout.setVisibility(View.GONE);
                main_six_layout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initDevice() {
        Intent intent = getIntent();
        mDevice = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        mDevice.setListener(gizWifiDeviceListener);
        Log.i("Apptest", mDevice.getDid());
    }

    private String getDeviceName() {
        if (TextUtils.isEmpty(mDevice.getAlias())) {
            return mDevice.getProductName();
        }
        return mDevice.getAlias();
    }

    private void selectedSix() {
        switch (selectedPosition) {
            case 0:
                if (switch4.isChecked())
                    left_one_layout.setBackgroundResource(R.color.selected_color);
                else
                    left_one_layout.setBackgroundResource(R.color.gray);
                left_one_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                left_one_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                if (switch4.isChecked()) {
                    circle_seekBar.setEnabled(true);
                    imageView_jian.setClickable(true);
                    imageView_jia.setClickable(true);
                } else {
                    circle_seekBar.setEnabled(false);
                    imageView_jian.setClickable(false);
                    imageView_jia.setClickable(false);
                }
                if (switch5.isChecked())
                    right_one_layout.setBackgroundResource(R.color.white);
                else
                    right_one_layout.setBackgroundResource(R.color.gray);
                right_one_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_one_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch6.isChecked())
                    left_two_layout.setBackgroundResource(R.color.white);
                else
                    left_two_layout.setBackgroundResource(R.color.gray);
                left_two_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_two_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch7.isChecked())
                    right_two_layout.setBackgroundResource(R.color.white);
                else
                    right_two_layout.setBackgroundResource(R.color.gray);
                right_two_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_two_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch8.isChecked())
                    left_three_layout.setBackgroundResource(R.color.white);
                else
                    left_three_layout.setBackgroundResource(R.color.gray);
                left_three_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_three_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch9.isChecked())
                    right_three_layout.setBackgroundResource(R.color.white);
                else
                    right_three_layout.setBackgroundResource(R.color.gray);
                right_three_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_three_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));
                break;
            case 1:
                if (switch4.isChecked())
                    left_one_layout.setBackgroundResource(R.color.white);
                else
                    left_one_layout.setBackgroundResource(R.color.gray);
                left_one_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_one_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch5.isChecked())
                    right_one_layout.setBackgroundResource(R.color.selected_color);
                else
                    right_one_layout.setBackgroundResource(R.color.gray);
                right_one_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                right_one_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                if (switch5.isChecked()) {
                    circle_seekBar.setEnabled(true);
                    imageView_jian.setClickable(true);
                    imageView_jia.setClickable(true);
                } else {
                    circle_seekBar.setEnabled(false);
                    imageView_jian.setClickable(false);
                    imageView_jia.setClickable(false);
                }
                if (switch6.isChecked())
                    left_two_layout.setBackgroundResource(R.color.white);
                else
                    left_two_layout.setBackgroundResource(R.color.gray);
                left_two_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_two_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch7.isChecked())
                    right_two_layout.setBackgroundResource(R.color.white);
                else
                    right_two_layout.setBackgroundResource(R.color.gray);
                right_two_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_two_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch8.isChecked())
                    left_three_layout.setBackgroundResource(R.color.white);
                else
                    left_three_layout.setBackgroundResource(R.color.gray);
                left_three_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_three_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch9.isChecked())
                    right_three_layout.setBackgroundResource(R.color.white);
                else
                    right_three_layout.setBackgroundResource(R.color.gray);
                right_three_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_three_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));
                break;
            case 2:
                if (switch4.isChecked())
                    left_one_layout.setBackgroundResource(R.color.white);
                else
                    left_one_layout.setBackgroundResource(R.color.gray);
                left_one_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_one_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_gray));

                if (switch5.isChecked())
                    right_one_layout.setBackgroundResource(R.color.white);
                else
                    right_one_layout.setBackgroundResource(R.color.gray);
                right_one_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_one_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch6.isChecked())
                    left_two_layout.setBackgroundResource(R.color.selected_color);
                else
                    left_two_layout.setBackgroundResource(R.color.gray);
                left_two_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                left_two_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                if (switch6.isChecked()) {
                    circle_seekBar.setEnabled(true);
                    imageView_jian.setClickable(true);
                    imageView_jia.setClickable(true);
                } else {
                    circle_seekBar.setEnabled(false);
                    imageView_jian.setClickable(false);
                    imageView_jia.setClickable(false);
                }
                if (switch7.isChecked())
                    right_two_layout.setBackgroundResource(R.color.white);
                else
                    right_two_layout.setBackgroundResource(R.color.gray);
                right_two_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_two_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch8.isChecked())
                    left_three_layout.setBackgroundResource(R.color.white);
                else
                    left_three_layout.setBackgroundResource(R.color.gray);
                left_three_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_three_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch9.isChecked())
                    right_three_layout.setBackgroundResource(R.color.white);
                else
                    right_three_layout.setBackgroundResource(R.color.gray);
                right_three_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_three_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));
                break;
            case 3:
                if (switch4.isChecked())
                    left_one_layout.setBackgroundResource(R.color.white);
                else
                    left_one_layout.setBackgroundResource(R.color.gray);
                left_one_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_one_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_gray));

                if (switch5.isChecked())
                    right_one_layout.setBackgroundResource(R.color.white);
                else
                    right_one_layout.setBackgroundResource(R.color.gray);
                right_one_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_one_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch6.isChecked())
                    left_two_layout.setBackgroundResource(R.color.white);
                else
                    left_two_layout.setBackgroundResource(R.color.gray);
                left_two_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_two_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch7.isChecked())
                    right_two_layout.setBackgroundResource(R.color.selected_color);
                else
                    right_two_layout.setBackgroundResource(R.color.gray);
                right_two_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                right_two_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                if (switch7.isChecked()) {
                    circle_seekBar.setEnabled(true);
                    imageView_jian.setClickable(true);
                    imageView_jia.setClickable(true);
                } else {
                    circle_seekBar.setEnabled(false);
                    imageView_jian.setClickable(false);
                    imageView_jia.setClickable(false);
                }
                if (switch8.isChecked())
                    left_three_layout.setBackgroundResource(R.color.white);
                else
                    left_three_layout.setBackgroundResource(R.color.gray);
                left_three_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_three_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch9.isChecked())
                    right_three_layout.setBackgroundResource(R.color.white);
                else
                    right_three_layout.setBackgroundResource(R.color.gray);
                right_three_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_three_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));
                break;
            case 4:
                if (switch4.isChecked())
                    left_one_layout.setBackgroundResource(R.color.white);
                else
                    left_one_layout.setBackgroundResource(R.color.gray);
                left_one_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_one_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_gray));

                if (switch5.isChecked())
                    right_one_layout.setBackgroundResource(R.color.white);
                else
                    right_one_layout.setBackgroundResource(R.color.gray);
                right_one_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_one_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch6.isChecked())
                    left_two_layout.setBackgroundResource(R.color.white);
                else
                    left_two_layout.setBackgroundResource(R.color.gray);
                left_two_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_two_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch7.isChecked())
                    right_two_layout.setBackgroundResource(R.color.white);
                else
                    right_two_layout.setBackgroundResource(R.color.gray);
                right_two_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_two_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch8.isChecked())
                    left_three_layout.setBackgroundResource(R.color.selected_color);
                else
                    left_three_layout.setBackgroundResource(R.color.gray);
                left_three_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                left_three_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                if (switch8.isChecked()) {
                    circle_seekBar.setEnabled(true);
                    imageView_jian.setClickable(true);
                    imageView_jia.setClickable(true);
                } else {
                    circle_seekBar.setEnabled(false);
                    imageView_jian.setClickable(false);
                    imageView_jia.setClickable(false);
                }
                if (switch9.isChecked())
                    right_three_layout.setBackgroundResource(R.color.white);
                else
                    right_three_layout.setBackgroundResource(R.color.gray);
                right_three_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_three_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));
                break;
            case 5:
                if (switch4.isChecked())
                    left_one_layout.setBackgroundResource(R.color.white);
                else
                    left_one_layout.setBackgroundResource(R.color.gray);
                left_one_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_one_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_gray));

                if (switch5.isChecked())
                    right_one_layout.setBackgroundResource(R.color.white);
                else
                    right_one_layout.setBackgroundResource(R.color.gray);
                right_one_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_one_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch6.isChecked())
                    left_two_layout.setBackgroundResource(R.color.white);
                else
                    left_two_layout.setBackgroundResource(R.color.gray);
                left_two_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_two_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch7.isChecked())
                    right_two_layout.setBackgroundResource(R.color.white);
                else
                    right_two_layout.setBackgroundResource(R.color.gray);
                right_two_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                right_two_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch8.isChecked())
                    left_three_layout.setBackgroundResource(R.color.white);
                else
                    left_three_layout.setBackgroundResource(R.color.gray);
                left_three_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_black));
                left_three_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.text_grey));

                if (switch9.isChecked())
                    right_three_layout.setBackgroundResource(R.color.selected_color);
                else
                    right_three_layout.setBackgroundResource(R.color.gray);
                if (switch9.isChecked()) {
                    circle_seekBar.setEnabled(true);
                    imageView_jian.setClickable(true);
                    imageView_jia.setClickable(true);
                } else {
                    circle_seekBar.setEnabled(false);
                    imageView_jian.setClickable(false);
                    imageView_jia.setClickable(false);
                }
                right_three_textView.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                right_three_hint.setTextColor(ContextCompat.getColor(GosDeviceControlActivity.this, R.color.white));
                break;
        }
    }

    private void TimeShow(int hour, int minute, TimePickerDialog.OnTimeSetListener listener) {
        mCalendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(GosDeviceControlActivity.this, listener, hour, minute, true);
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        // 退出页面，取消设备订阅
        mDevice.setSubscribe(false);
        mDevice.setListener(null);
    }

    /**
     * Description:根据保存的的数据点的值来更新UI
     */
    @SuppressLint("SetTextI18n")
    protected void updateUI() {
        changeSwitch();//总开关
        changeMode();//模式
        changeTemperature();//修改温度
        waringLayout();
    }

    private void waringLayout() {
//    "0表示无故障
//    1表示高温报警
//    2表示短路报警
//    3表示断路报警
//    4表示参数设置错误报警"
        switch (Waring) {
            case 0:
                waringImage.setVisibility(View.GONE);
                break;
            default:
                waringImage.setVisibility(View.VISIBLE);
                Glide.with(GosDeviceControlActivity.this).load(R.drawable.waring).into(waringImage);
                break;
        }
        waringImage.post(new Runnable() {
            @Override
            public void run() {
                MyGizWifiDevice myGizWifiDevice = new MyGizWifiDevice();
                myGizWifiDevice.setWaring(Waring);
                myGizWifiDevice.setOpen(switch1Selected);
                myGizWifiDevice.setGizWifiDevice(mDevice);
                EventBus.getDefault().post(new MessageEvent("刷新", myGizWifiDevice));
            }
        });
        NotificationUtil.getInstance(GosDeviceControlActivity.this).sendNotification(this, Waring);
        MyGizWifiDevice myGizWifiDevice = new MyGizWifiDevice();
        myGizWifiDevice.setWaring(Waring);
        myGizWifiDevice.setOpen(switch1Selected);
        myGizWifiDevice.setGizWifiDevice(mDevice);
        EventBus.getDefault().post(new MessageEvent("刷新", myGizWifiDevice));
    }

    //总开关
    private void closeAllSwitch(boolean check) {
        if (!check) {
            switch1.setChecked(false);
            switch2.setChecked(false);
            switch3.setChecked(false);
            switch4.setChecked(false);
            switch5.setChecked(false);
            switch6.setChecked(false);
            switch7.setChecked(false);
            switch8.setChecked(false);
            switch9.setChecked(false);
            switch10.setChecked(false);
            switch1.setEnabled(false);
            switch2.setEnabled(false);
            switch3.setEnabled(false);
            switch4.setEnabled(false);
            switch5.setEnabled(false);
            switch6.setEnabled(false);
            switch7.setEnabled(false);
            switch8.setEnabled(false);
            switch9.setEnabled(false);
            switch10.setEnabled(false);
            nice_spinner.setEnabled(false);
        } else {
            switch1.setEnabled(true);
            switch2.setEnabled(true);
            switch3.setEnabled(true);
            switch4.setEnabled(true);
            switch5.setEnabled(true);
            switch6.setEnabled(true);
            switch7.setEnabled(true);
            switch8.setEnabled(true);
            switch9.setEnabled(true);
            switch10.setEnabled(true);
            switch1.setChecked(switch2Selected);
            switch2.setChecked(switch2Selected);
            switch3.setChecked(switch5Selected);
            switch4.setChecked(switch2Selected);
            switch5.setChecked(switch5Selected);
            switch6.setChecked(switch3Selected);
            switch7.setChecked(switch6Selected);
            switch8.setChecked(switch4Selected);
            switch9.setChecked(switch7Selected);
            switch10.setChecked(switch8Selected);
            nice_spinner.setEnabled(true);
        }
    }

    /**
     * Description:页面加载后弹出等待框，等待设备可被控制状态回调，如果一直不可被控，等待一段时间后自动退出界面
     */
    private void getStatusOfDevice() {
        // 设备是否可控
        if (isDeviceCanBeControlled()) {
            // 可控则查询当前设备状态
            mDevice.getDeviceStatus();
        } else {
            // 显示等待栏
            progressDialog.show();
            if (mDevice.isLAN()) {
                // 小循环10s未连接上设备自动退出
                mHandler.postDelayed(mRunnable, 10000);
            } else {
                // 大循环20s未连接上设备自动退出
                mHandler.postDelayed(mRunnable, 20000);
            }
        }
    }

    //修改总开关状态
    private void changeSwitch() {
        switch11.setChecked(baseBean.getAllSwitch());//总开关状态
        if (switch11.isChecked()) {
            switch1.setEnabled(true);
            switch2.setEnabled(true);
            switch3.setEnabled(true);
            switch4.setEnabled(true);
            switch5.setEnabled(true);
            switch6.setEnabled(true);
            switch7.setEnabled(true);
            switch8.setEnabled(true);
            switch9.setEnabled(true);
            switch10.setEnabled(true);
            nice_spinner.setEnabled(true);
        } else {
            switch1.setEnabled(false);
            switch2.setEnabled(false);
            switch3.setEnabled(false);
            switch4.setEnabled(false);
            switch5.setEnabled(false);
            switch6.setEnabled(false);
            switch7.setEnabled(false);
            switch8.setEnabled(false);
            switch9.setEnabled(false);
            switch10.setEnabled(false);
            nice_spinner.setEnabled(false);
        }
    }

    //修改模式状态
    @SuppressLint("SetTextI18n")
    private void changeMode() {
        nice_spinner.setSelection(baseBean.getType());
        switch (baseBean.getType()) {
            case 0:
                //手动
                textView_keller.setVisibility(View.VISIBLE);
                switch10.setVisibility(View.VISIBLE);
                layout_automatic.setVisibility(View.GONE);
                main_qualified_layout.setVisibility(View.GONE);
                qualified_layout_double.setVisibility(View.GONE);
                if (baseBean.getManualBean() == null) {
                    return;
                }
                if (baseBean.getAllSwitch())
                    switch10.setChecked(baseBean.getManualBean().getKillSwitch());
                else
                    switch10.setChecked(false);
                break;
            case 1:
                textView_keller.setVisibility(View.INVISIBLE);
                switch10.setVisibility(View.INVISIBLE);
                layout_automatic.setVisibility(View.VISIBLE);
                main_qualified_layout.setVisibility(View.GONE);
                qualified_layout_double.setVisibility(View.GONE);
                AutomaticBean automaticBean = baseBean.getAutomaticBean();
                if (automaticBean == null) {
                    return;
                }
                getWeek(automaticBean.getWeek());
                starHour = automaticBean.getStarHour();
                starMinute = automaticBean.getStarMinute();
                endHour = automaticBean.getEndHour();
                endMinute = automaticBean.getEndMinute();
                textView_start_time.setText((starHour < 10 ? "0" + starHour : starHour) + ":" + (starMinute < 10 ? "0" + starMinute : starMinute));
                textView_end_time.setText((endHour < 10 ? "0" + endHour : endHour) + ":" + (endMinute < 10 ? "0" + endMinute : endMinute));
                break;
            case 2:
                textView_keller.setVisibility(View.INVISIBLE);
                switch10.setVisibility(View.INVISIBLE);
                layout_automatic.setVisibility(View.GONE);
                TimingBean timingBean = baseBean.getTimingBean();
                if (timingBean == null) {
                    return;
                }

                timingHour = timingBean.getHour();
                timingMinute = timingBean.getMinute();
                if (type == 1) {
                    qualified_layout_double.setVisibility(View.VISIBLE);
                    main_qualified_layout.setVisibility(View.GONE);
                    int hourR = timingBean.getHourRight();
                    int minR = timingBean.getMinuteRight();
                    textView_qualified_time_left.setText((timingHour < 10 ? "0" + timingHour : timingHour) + ":" + (timingMinute < 10 ? "0" + timingMinute : timingMinute));
                    textView_qualified_time_right.setText((hourR < 10 ? "0" + hourR : hourR) + ":" + (minR < 10 ? "0" + minR : minR));
                } else {
                    qualified_layout_double.setVisibility(View.GONE);
                    main_qualified_layout.setVisibility(View.VISIBLE);
                    textView_qualified_time.setText((timingHour < 10 ? "0" + timingHour : timingHour) + ":" + (timingMinute < 10 ? "0" + timingMinute : timingMinute));
                }
                break;
        }
    }

    //修改温度
    @SuppressLint("SetTextI18n")
    private void changeTemperature() {
        initType(type);
        switch (type) {
            case 0:
                temp_textView.setText("当前温度："+TempRealLeft1+"℃");
                TemperatureBean temperatureBean = baseBean.getList().get(0);
                if (baseBean.getAllSwitch()) {
                    switch1.setChecked(temperatureBean.getSelected());
                } else {
                    switch1.setChecked(false);
                }
                circle_seekBar.setCurProcess(temperatureBean.getNum() - 20);
                break;
            case 1:
                TemperatureBean tL = baseBean.getList().get(0);
                TemperatureBean tR = baseBean.getList().get(1);
                left_double_textView.setText(TempRealLeft1 + "℃");
                right_double_textView.setText(TempRealRight1 + "℃");
                if (baseBean.getAllSwitch()) {
                    switch2.setChecked(tL.getSelected());
                    switch3.setChecked(tR.getSelected());
                } else {
                    switch2.setChecked(false);
                    switch3.setChecked(false);
                }
                if (selectedPosition == 0) {
                    circle_seekBar.setCurProcess(tL.getNum() - 20);
                } else {
                    circle_seekBar.setCurProcess(tR.getNum() - 20);
                }
                break;
            case 2:
                for (int i = 0; i < viewBeanArrayList.size(); i++) {
                    ItemViewBean viewBean = viewBeanArrayList.get(i);
                    TemperatureBean tBS = baseBean.getList().get(i);
                    viewBean.getTextView().setText(temperatureArrayList.get(i) + "℃");
                    if (baseBean.getAllSwitch()) {
                        viewBean.getaSwitch().setChecked(tBS.getSelected());
                    } else {
                        viewBean.getaSwitch().setChecked(false);
                    }
                    if (viewBean.isSelected()) {
                        if (viewBean.getaSwitch().isChecked()) {
                            circle_seekBar.setEnabled(true);
                            imageView_jian.setClickable(true);
                            imageView_jia.setClickable(true);
                        } else {
                            circle_seekBar.setEnabled(false);
                            imageView_jian.setClickable(false);
                            imageView_jia.setClickable(false);
                        }
                    }

                }
        }
    }

    private void isChange() {
        switch (type) {
            case 0:
                if (baseBean.getList().get(0).getNum() != Temp_Left1) {
                    //温度不同时才上传
                    userDone = true;
                }
                break;
            case 1:

                if (baseBean.getList().get(0).getNum() != Temp_Left1) {
                    userDone = true;
                }
                if (baseBean.getList().get(1).getNum() != Temp_Right1) {
                    userDone = true;
                }
                break;
            case 2:
                if (baseBean.getList().get(0).getNum() != Temp_Left1) {
                    userDone = true;
                }
                if (baseBean.getList().get(1).getNum() != Temp_Left2) {
                    userDone = true;
                }
                if (baseBean.getList().get(2).getNum() != Temp_Left3) {
                    userDone = true;
                }
                if (baseBean.getList().get(3).getNum() != Temp_Right1) {
                    userDone = true;
                }
                if (baseBean.getList().get(4).getNum() != Temp_Right2) {
                    userDone = true;
                }
                if (baseBean.getList().get(5).getNum() != Temp_Right3) {
                    userDone = true;
                }
                break;
        }

    }

    /**
     * 发送指令,下发单个数据点的命令可以用这个方法
     * <p>
     * <h3>注意</h3>
     * <p>
     * 下发多个数据点命令不能用这个方法多次调用，一次性多次调用这个方法会导致模组无法正确接收消息，参考方法内注释。
     * </p>
     */
    private void sendCommand() {
        int sn = 5;
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
        hashMap.put(KEY_DATA14, switch11.isChecked());//总开关。关闭的时候其它字段都不提交
        if (switch11.isChecked()) {
            switch (type) {
                case 0:
                    hashMap.put(KEY_DATA15, switch1.isChecked());//单开关
                    if (switch1.isChecked() && baseBean.getList().get(0).getNum() != Temp_Left1) {
                        //温度不同时才上传
                        hashMap.put(KEY_DATA, baseBean.getList().get(0).getNum());
                    }
                    break;
                case 1:
                    hashMap.put(KEY_DATA15, switch2.isChecked());//左上温区开关
                    TemperatureBean tL = baseBean.getList().get(0);
                    if (switch2.isChecked() && tL.getNum() != Temp_Left1) {
                        hashMap.put(KEY_DATA, tL.getNum());

                    }
                    TemperatureBean tR = baseBean.getList().get(1);
                    hashMap.put(KEY_DATA18, switch3.isChecked());//右上温区开关
                    if (switch3.isChecked() && tR.getNum() != Temp_Right1) {
                        hashMap.put(KEY_DATA3, tR.getNum());
                    }
                    break;
                case 2:
                    hashMap.put(KEY_DATA15, switch4.isChecked());//左上温区开关
                    if (switch4.isChecked() && baseBean.getList().get(0).getNum() != Temp_Left1) {
                        hashMap.put(KEY_DATA, baseBean.getList().get(0).getNum());
                    }
                    hashMap.put(KEY_DATA16, switch6.isChecked());//左中温区开关
                    if (switch6.isChecked() && baseBean.getList().get(1).getNum() != Temp_Left2) {
                        hashMap.put(KEY_DATA1, baseBean.getList().get(1).getNum());
                    }
                    hashMap.put(KEY_DATA17, switch8.isChecked());//左下温区开关
                    if (switch8.isChecked() && baseBean.getList().get(2).getNum() != Temp_Left3) {
                        hashMap.put(KEY_DATA2, baseBean.getList().get(2).getNum());
                    }
                    hashMap.put(KEY_DATA18, switch5.isChecked());//右上温区开关
                    if (switch5.isChecked() && baseBean.getList().get(3).getNum() != Temp_Right1) {
                        hashMap.put(KEY_DATA3, baseBean.getList().get(3).getNum());
                    }
                    hashMap.put(KEY_DATA19, switch7.isChecked());//右中温区开关
                    if (switch7.isChecked() && baseBean.getList().get(4).getNum() != Temp_Right2) {
                        hashMap.put(KEY_DATA4, baseBean.getList().get(4).getNum());
                    }
                    hashMap.put(KEY_DATA20, switch9.isChecked());//右下温区开关
                    if (switch9.isChecked() && baseBean.getList().get(5).getNum() != Temp_Right3) {
                        hashMap.put(KEY_DATA5, baseBean.getList().get(5).getNum());
                    }
                    break;
            }
            switch (optionType) {
                case 0:
                    hashMap.put(KEY_DATA21, switch10.isChecked());//一键杀菌
                    break;
                case 1:
                    if (starHour + starMinute + endHour + endMinute == 0) {
                        myToast("时间不能为空");
                        return;
                    }
                    hashMap.put(KEY_DATA6, starHour);
                    hashMap.put(KEY_DATA7, starMinute);
                    hashMap.put(KEY_DATA8, endHour);
                    hashMap.put(KEY_DATA9, endMinute);
                    hashMap.put(KEY_DATA12, getWeek());
                    break;
                case 2:
                    if (type == 1) {
                        if (switch2.isChecked()) {
                            if (timingHour + timingMinute == 0) {
                                myToast("限定时间不能为空");
                                return;
                            }
                            hashMap.put(KEY_DATA10, timingHour);
                            hashMap.put(KEY_DATA11, timingMinute);
                        }
                        if (switch3.isChecked()) {
                            if (Hour_double + Minutes_double == 0) {
                                myToast("限定时间不能为空");
                                return;
                            }
                            hashMap.put(KEY_DATA23, Hour_double);
                            hashMap.put(KEY_DATA24, Minutes_double);
                        }
                    } else {
                        if (timingHour + timingMinute == 0) {
                            myToast("限定时间不能为空");
                            return;
                        }
                        hashMap.put(KEY_DATA10, timingHour);
                        hashMap.put(KEY_DATA11, timingMinute);
                    }

                    break;
            }
            hashMap.put(KEY_DATA22, optionType);
        }
        mDevice.write(hashMap, sn);
    }

    private int getWeek() {
        long week = 0;
        if (selected1) {
            week = week + 1;
        }
        if (selected2) {
            week = week + 10;
        }
        if (selected3) {
            week = week + 100;
        }
        if (selected4) {
            week = week + 1000;
        }
        if (selected5) {
            week = week + 10000;
        }
        if (selected6) {
            week = week + 100000;
        }
        if (selected7) {
            week = week + 1000000;
        }
        int back = 0;
        String binaryString = week + "";
        back = Integer.parseInt(binaryString, 2);
        return back;
    }

    private void getWeek(int week) {
        String value = Integer.toBinaryString(week);
        int length = 7 - value.length();
        for (int i = 0; i < length; i++) {
            value = "0" + value;
        }
        for (int i = 0; i < value.length(); i++) {
            switch (i) {
                case 0:
                    if (String.valueOf(value.charAt(i)).endsWith("1")) {
                        selected7 = true;
                    } else {
                        selected7 = false;
                    }
                    break;
                case 1:
                    if (String.valueOf(value.charAt(i)).endsWith("1")) {
                        selected6 = true;
                    } else {
                        selected6 = false;
                    }
                    break;
                case 2:
                    if (String.valueOf(value.charAt(i)).endsWith("1")) {
                        selected5 = true;
                    } else {
                        selected5 = false;
                    }
                    break;
                case 3:
                    if (String.valueOf(value.charAt(i)).endsWith("1")) {
                        selected4 = true;
                    } else {
                        selected4 = false;
                    }
                    break;
                case 4:
                    if (String.valueOf(value.charAt(i)).endsWith("1")) {
                        selected3 = true;
                    } else {
                        selected3 = false;
                    }
                    break;
                case 5:
                    if (String.valueOf(value.charAt(i)).endsWith("1")) {
                        selected2 = true;
                    } else {
                        selected2 = false;
                    }
                    break;
                case 6:
                    if (String.valueOf(value.charAt(i)).endsWith("1")) {
                        selected1 = true;
                    } else {
                        selected1 = false;
                    }
                    break;

            }
        }
        switch (value.length()) {

        }
    }

    private boolean isDeviceCanBeControlled() {
        return mDevice.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceControlled;
    }

    private void toastDeviceNoReadyAndExit() {
        Toast.makeText(this, "设备无响应，请检查设备是否正常工作", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void toastDeviceDisconnectAndExit() {
        Toast.makeText(GosDeviceControlActivity.this, "连接已断开", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 展示设备硬件信息
     *
     * @param hardwareInfo
     */
    private void showHardwareInfo(String hardwareInfo) {
        String hardwareInfoTitle = "设备硬件信息";
        new AlertDialog.Builder(this).setTitle(hardwareInfoTitle).setMessage(hardwareInfo).setPositiveButton(R.string.besure, null).show();
    }

    /*
     * 获取设备硬件信息回调
     */
    @Override
    protected void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, String> hardwareInfo) {
        super.didGetHardwareInfo(result, device, hardwareInfo);
        StringBuffer sb = new StringBuffer();
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            myToast("获取设备硬件信息失败：" + result.name());
        } else {
            sb.append("Wifi Hardware Version:" + hardwareInfo.get(WIFI_HARDVER_KEY) + "\r\n");
            sb.append("Wifi Software Version:" + hardwareInfo.get(WIFI_SOFTVER_KEY) + "\r\n");
            sb.append("MCU Hardware Version:" + hardwareInfo.get(MCU_HARDVER_KEY) + "\r\n");
            sb.append("MCU Software Version:" + hardwareInfo.get(MCU_SOFTVER_KEY) + "\r\n");
            sb.append("Wifi Firmware Id:" + hardwareInfo.get(WIFI_FIRMWAREID_KEY) + "\r\n");
            sb.append("Wifi Firmware Version:" + hardwareInfo.get(WIFI_FIRMWAREVER_KEY) + "\r\n");
            sb.append("Product Key:" + "\r\n" + hardwareInfo.get(PRODUCT_KEY) + "\r\n");

            // 设备属性
            sb.append("Device ID:" + "\r\n" + mDevice.getDid() + "\r\n");
            sb.append("Device IP:" + mDevice.getIPAddress() + "\r\n");
            sb.append("Device MAC:" + mDevice.getMacAddress() + "\r\n");
        }
        showHardwareInfo(sb.toString());
    }

    /*
     * 设置设备别名和备注回调
     */
    @Override
    protected void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
        super.didSetCustomInfo(result, device);
    }

    /*
     * 设备状态改变回调，只有设备状态为可控才可以下发控制命令
     */
    @Override
    protected void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
        super.didUpdateNetStatus(device, netStatus);
        if (netStatus == GizWifiDeviceNetStatus.GizDeviceControlled) {
            mHandler.removeCallbacks(mRunnable);
            progressDialog.cancel();
        } else {
            mHandler.sendEmptyMessage(handler_key.DISCONNECT.ordinal());
        }
    }

    /*
     * 设备上报数据回调，此回调包括设备主动上报数据、下发控制命令成功后设备返回ACK
     */
    @Override
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
        super.didReceiveData(result, device, dataMap, sn);
        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
            if (dataMap.get("data") != null) {
                getDataFromReceiveDataMap(dataMap);
                mHandler.sendEmptyMessage(handler_key.UPDATE_UI.ordinal());
            } else {
                myToast("提交成功");
                mHandler.sendEmptyMessage(handler_key.UPDATE.ordinal());
            }
        }
    }

}