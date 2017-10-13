package com.tsg.xutil.activity;

import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.tsg.xutil.R;
import com.tsg.xutil.base.BaseActivity;
import com.tsg.xutil.constant.RequestApi;
import com.tsg.xutil.util.SPNameUtil;
import com.tsg.xutil.util.T;
import com.tsg.xutil.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class TestHostActivity extends BaseActivity implements View.OnClickListener {

    private PopupWindow mPopWindow;
    private TextView text;
    private EditText hostEdit, hostPortEdit;
    private List list;
    private Button button;
    private Spinner http;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reSetContentView(R.layout.activity_test_host);
        initView();
    }

    private void initView() {
        text = (TextView) findViewById(R.id.test_text);
        http = (Spinner) findViewById(R.id.test_host_http);
        hostEdit = (EditText) findViewById(R.id.test_host);
        hostPortEdit = (EditText) findViewById(R.id.test_host_port);
        hostEdit = (EditText) findViewById(R.id.test_host);
        button = (Button) findViewById(R.id.sure);
        button.setOnClickListener(null);
        button.setBackgroundColor(getResources().getColor(R.color.gray));
        showPopupWindow();
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPopWindow.isShowing()) {
                    //显示PopupWindow
                    mPopWindow.showAsDropDown(text);
                } else {
                    mPopWindow.dismiss();
                }
            }
        });
        hostEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 10) {
                    button.setOnClickListener(TestHostActivity.this);
                    button.setBackgroundResource(R.drawable.bt_pressed_bg_gray_circle);
                } else {
                    button.setOnClickListener(null);
                    button.setBackgroundColor(getResources().getColor(R.color.gray));
                }
            }
        });

        String getHostString = SPNameUtil.getStringData(this, "host");
        if (TextUtil.stringIsNull(getHostString)) {
            setActionBarTitle("接口1");
            text.setText("接口1");
        } else {
            RequestApi.host = getHostString;
            setActionBarTitle(getHostString);
            text.setText(getHostString);
            List myList = RequestApi.initList();
            for (int i = 0; i < myList.size(); i++) {
                if (getHostString.equals(myList.get(i))) {
                    text.setText((CharSequence) list.get(i));
                    setActionBarTitle((String) list.get(i));
                    break;
                }
            }
        }
    }

    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_popup_layout, null);
        mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //设置各个控件的点击响应
        ListView listView = (ListView) contentView.findViewById(R.id.popup_listView);
        list = new ArrayList();
        list.add("接口1");
        list.add("接口2");
        list.add("接口3");
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPopWindow.dismiss();
                switch (position) {
                    case 0: {//测试环境
                        RequestApi.host = (String) RequestApi.initList().get(position);
                        SPNameUtil.putStringData(TestHostActivity.this, "host", RequestApi.host);
                        text.setText("" + list.get(position));
                        setActionBarTitle("" + list.get(position));
                        T.showLong(TestHostActivity.this, "设置成功：“接口1”：" + RequestApi.host);
                        break;
                    }
                    case 1: {//Demo环境
                        RequestApi.host = (String) RequestApi.initList().get(position);
                        SPNameUtil.putStringData(TestHostActivity.this, "host", RequestApi.host);
                        text.setText("" + list.get(position));
                        setActionBarTitle("" + list.get(position));
                        T.showLong(TestHostActivity.this, "设置成功：“接口2”：" + RequestApi.host);
                        break;
                    }
                    case 2: {//公网联调环境
                        RequestApi.host = (String) RequestApi.initList().get(position);
                        SPNameUtil.putStringData(TestHostActivity.this, "host", RequestApi.host);
                        text.setText("" + list.get(position));
                        setActionBarTitle("" + list.get(position));
                        T.showLong(TestHostActivity.this, "设置成功：“接口3”：" + RequestApi.host);
                        break;
                    }
                }
            }
        });
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new PaintDrawable());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure: {
                String hostString = http.getSelectedItem().toString() + hostEdit.getText().toString() + ":" + hostPortEdit.getText().toString();
                //String hostString = hostEdit.getText().toString();
                text.setText(hostString);
                setActionBarTitle(hostString);
                RequestApi.host = hostString;
                T.showLong(this, "设置成功" + hostString);
                SPNameUtil.putStringData(TestHostActivity.this, "host", RequestApi.host);
                List myList = RequestApi.initList();
                for (int i = 0; i < myList.size(); i++) {
                    if (hostString.equals(myList.get(i))) {
                        text.setText((CharSequence) list.get(i));
                        setActionBarTitle((String) list.get(i));
                        break;
                    }
                }
            }
        }
    }
}
