package vending.lxuan.com.vendingmachine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vending.lxuan.com.vendingmachine.adapter.UpAdapter;
import vending.lxuan.com.vendingmachine.dao.VendingDao;
import vending.lxuan.com.vendingmachine.model.DataModel;
import vending.lxuan.com.vendingmachine.model.UpModel;

/**
 * Created by apple
 * 18/5/9
 */

public class FallingActivity extends Activity implements View.OnClickListener {
    private static final String PWD = "pass1234";
    private MyDialog dialog;
    private Button allRest;
    private Button allSave;
    private UpAdapter adapter;
    //一共
    public static final int PRODUCT_ALL_COUNT = 30;
    private List<DataModel> data;
    //所有产品编号
    //private List<String> allProductNumber;
    //private List<String> allProductCount;
    private VendingDao dao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        setContentView(R.layout.activity_falling);
        allRest = findViewById(R.id.reset);
        allRest.setOnClickListener(this);
        allSave = findViewById(R.id.save_all);
        allSave.setOnClickListener(this);
        //       UpModel model = new UpModel();
//        for (int i = 1; i < PRODUCT_ALL_COUNT + 1; i++) {
//            model.name.add(String.valueOf(i));
//        }
        dao = new VendingDao();
        data = dao.getAllProductNumberList();
        adapter = new UpAdapter(data);
        ListView listView = (ListView) findViewById(R.id.lv_up);
        listView.setAdapter(adapter);

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(!BuildConfig.IS_DEBUG){
            dialog = new MyDialog(this);
            dialog.setCancelable(false);
            dialog.show();
        }




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_all:
                dao.saveAll(data);
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.reset:
                dao.resetAll(data);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "清空成功", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    public class FullScreenDialog extends Dialog {

        public FullScreenDialog(Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

    }

    public class MyDialog extends FullScreenDialog {

        public MyDialog(Context context) {
            super(context);
            setContentView(R.layout.dialog_login);
            final EditText text = (EditText) findViewById(R.id.et_pas);
            findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (PWD.equals(text.getText().toString())) {
                        dismiss();
                    } else {
                        Toast.makeText(FallingActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }


}
