package com.gizwits.opensource.appkit.ControlModule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gizwits.opensource.appkit.R;

import wdy.business.listen.NoDoubleClickListener;
import wdy.business.util.StatusBarUtil;

/**
 * 作者：王东一
 * 创建时间：2018/1/1.
 * 选择重复
 */

public class SelectedWeekActivity extends FragmentActivity {
    private AppCompatImageView imageView_back;
    private TextView textView_title;
    private AppCompatImageView imageView_right;
    private ImageView imageView_1;
    private RelativeLayout layout_1;
    private ImageView imageView_2;
    private RelativeLayout layout_2;
    private ImageView imageView_3;
    private RelativeLayout layout_3;
    private ImageView imageView_4;
    private RelativeLayout layout_4;
    private ImageView imageView_5;
    private RelativeLayout layout_5;
    private ImageView imageView_6;
    private RelativeLayout layout_6;
    private ImageView imageView_7;
    private RelativeLayout layout_7;
    public static boolean selected1 = false, selected2 = false, selected3 = false, selected4 = false, selected5 = false, selected6 = false, selected7 = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_week);
        StatusBarUtil.setStatusBarDark(getWindow(),false);
        initView();
        setTitle();
        initClick();
    }

    private void initView() {
        imageView_back = (AppCompatImageView) findViewById(R.id.imageView_back);
        textView_title = (TextView) findViewById(R.id.textView_title);
        imageView_right = (AppCompatImageView) findViewById(R.id.imageView_right);
        imageView_1 = (ImageView) findViewById(R.id.imageView_1);
        layout_1 = (RelativeLayout) findViewById(R.id.layout_1);
        imageView_2 = (ImageView) findViewById(R.id.imageView_2);
        layout_2 = (RelativeLayout) findViewById(R.id.layout_2);
        imageView_3 = (ImageView) findViewById(R.id.imageView_3);
        layout_3 = (RelativeLayout) findViewById(R.id.layout_3);
        imageView_4 = (ImageView) findViewById(R.id.imageView_4);
        layout_4 = (RelativeLayout) findViewById(R.id.layout_4);
        imageView_5 = (ImageView) findViewById(R.id.imageView_5);
        layout_5 = (RelativeLayout) findViewById(R.id.layout_5);
        imageView_6 = (ImageView) findViewById(R.id.imageView_6);
        layout_6 = (RelativeLayout) findViewById(R.id.layout_6);
        imageView_7 = (ImageView) findViewById(R.id.imageView_7);
        layout_7 = (RelativeLayout) findViewById(R.id.layout_7);
    }

    private void initClick() {
        imageView_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        layout_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected1) {
                    selected1 = false;
                    imageView_1.setVisibility(View.GONE);
                } else {
                    selected1 = true;
                    imageView_1.setVisibility(View.VISIBLE);
                }
            }
        });
        layout_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected2) {
                    selected2 = false;
                    imageView_2.setVisibility(View.GONE);
                } else {
                    selected2 = true;
                    imageView_2.setVisibility(View.VISIBLE);
                }
            }
        });
        layout_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected3) {
                    selected3 = false;
                    imageView_3.setVisibility(View.GONE);
                } else {
                    selected3 = true;
                    imageView_3.setVisibility(View.VISIBLE);
                }
            }
        });
        layout_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected4) {
                    selected4 = false;
                    imageView_4.setVisibility(View.GONE);
                } else {
                    selected4 = true;
                    imageView_4.setVisibility(View.VISIBLE);
                }
            }
        });
        layout_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected5) {
                    selected5 = false;
                    imageView_5.setVisibility(View.GONE);
                } else {
                    selected5 = true;
                    imageView_5.setVisibility(View.VISIBLE);
                }
            }
        });
        layout_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected6) {
                    selected6 = false;
                    imageView_6.setVisibility(View.GONE);
                } else {
                    selected6 = true;
                    imageView_6.setVisibility(View.VISIBLE);
                }
            }
        });
        layout_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected7) {
                    selected7 = false;
                    imageView_7.setVisibility(View.GONE);
                } else {
                    selected7 = true;
                    imageView_7.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setTitle() {
        textView_title.setText("重复");
        if (selected1) {
            imageView_1.setVisibility(View.VISIBLE);
        } else {
            imageView_1.setVisibility(View.GONE);
        }
        if (selected2) {
            imageView_2.setVisibility(View.VISIBLE);
        } else {
            imageView_2.setVisibility(View.GONE);
        }
        if (selected3) {
            imageView_3.setVisibility(View.VISIBLE);
        } else {
            imageView_3.setVisibility(View.GONE);
        }
        if (selected4) {
            imageView_4.setVisibility(View.VISIBLE);
        } else {
            imageView_4.setVisibility(View.GONE);
        }
        if (selected5) {
            imageView_5.setVisibility(View.VISIBLE);
        } else {
            imageView_5.setVisibility(View.GONE);
        }
        if (selected6) {
            imageView_6.setVisibility(View.VISIBLE);
        } else {
            imageView_6.setVisibility(View.GONE);
        }
        if (selected7) {
            imageView_7.setVisibility(View.VISIBLE);
        } else {
            imageView_7.setVisibility(View.GONE);
        }
    }

    public static void clean() {
        selected1 = false;
        selected2 = false;
        selected3 = false;
        selected4 = false;
        selected5 = false;
        selected6 = false;
        selected7 = false;

    }
}
