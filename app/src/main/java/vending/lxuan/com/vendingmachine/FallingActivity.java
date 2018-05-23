package vending.lxuan.com.vendingmachine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import vending.lxuan.com.vendingmachine.adapter.UpAdapter;
import vending.lxuan.com.vendingmachine.model.UpModel;

/**
 * Created by apple
 * 18/5/9
 */

public class FallingActivity extends Activity {
    private static final String PWD = "pass1234";
    private MyDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_falling);

        UpModel model = new UpModel();
        for(int i = 1; i < 61; i++) {
            model.name.add(String.valueOf(i));
        }

        ListView listView = (ListView) findViewById(R.id.lv_up);
        listView.setAdapter(new UpAdapter(model));

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
        dialog = new MyDialog(this);
        dialog.setCancelable(false);
        dialog.show();
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
