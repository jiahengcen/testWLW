package vending.lxuan.com.vendingmachine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vending.lxuan.com.vendingmachine.dao.VendingDao;
import vending.lxuan.com.vendingmachine.model.DataModel;


public class NewFallingActivity extends Activity implements View.OnClickListener {
    private static final String PWD = "pass1234";
    private MyDialog dialog;
    private Button allRest;
    private Button allSave;
    private List<DataModel> data;
    private ViewGroup listViewGroup;
    private VendingDao dao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_new_falling);
        listViewGroup = findViewById(R.id.list);
        allRest = findViewById(R.id.reset);
        allRest.setOnClickListener(this);
        allSave = findViewById(R.id.save_all);
        allSave.setOnClickListener(this);
        dao = new VendingDao();
        data = dao.getAllProductNumberList();
        initManyViews();
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (!BuildConfig.IS_DEBUG) {
            dialog = new MyDialog(this);
            dialog.setCancelable(false);
            dialog.show();
        }

    }

    private void initManyViews() {
        int i = 0;
        initOneView(findViewById(R.id.list1), data.get(i++));
        initOneView(findViewById(R.id.list2), data.get(i++));
        initOneView(findViewById(R.id.list3), data.get(i++));
        initOneView(findViewById(R.id.list4), data.get(i++));
        initOneView(findViewById(R.id.list5), data.get(i++));

        initOneView(findViewById(R.id.list11), data.get(i++));
        initOneView(findViewById(R.id.list12), data.get(i++));
        initOneView(findViewById(R.id.list13), data.get(i++));
        initOneView(findViewById(R.id.list14), data.get(i++));
        initOneView(findViewById(R.id.list15), data.get(i++));

        initOneView(findViewById(R.id.list21), data.get(i++));
        initOneView(findViewById(R.id.list22), data.get(i++));
        initOneView(findViewById(R.id.list23), data.get(i++));
        initOneView(findViewById(R.id.list24), data.get(i++));
        initOneView(findViewById(R.id.list25), data.get(i++));

        initOneView(findViewById(R.id.list31), data.get(i++));
        initOneView(findViewById(R.id.list32), data.get(i++));
        initOneView(findViewById(R.id.list33), data.get(i++));
        initOneView(findViewById(R.id.list34), data.get(i++));
        initOneView(findViewById(R.id.list35), data.get(i++));

        initOneView(findViewById(R.id.list41), data.get(i++));
        initOneView(findViewById(R.id.list42), data.get(i++));
        initOneView(findViewById(R.id.list43), data.get(i++));
        initOneView(findViewById(R.id.list44), data.get(i++));
        initOneView(findViewById(R.id.list45), data.get(i++));

        initOneView(findViewById(R.id.list51), data.get(i++));
        initOneView(findViewById(R.id.list52), data.get(i++));
        initOneView(findViewById(R.id.list53), data.get(i++));
        initOneView(findViewById(R.id.list54), data.get(i++));
        initOneView(findViewById(R.id.list55), data.get(i));
    }

    private void initOneView(View viewGroup, final DataModel dataModel) {
        //货道号
        TextView id = viewGroup.findViewById(R.id.tv_id);
        final EditText productNo = viewGroup.findViewById(R.id.et_no);
        final EditText productCount = viewGroup.findViewById(R.id.et_numb);
        Button button = viewGroup.findViewById(R.id.btn_commit);
        productNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            MyTextWatcher textWatcher;

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (textWatcher != null && !hasFocus) {
                    ((TextView) v).removeTextChangedListener(textWatcher);
                } else if (hasFocus) {
                    if (textWatcher == null) {
                        textWatcher = new MyTextWatcher(dataModel) {
                            @Override
                            public void afterTextChanged(Editable s) {
                                dataModel.productNo = s.toString();
                            }
                        };
                    }
                    ((TextView) v).addTextChangedListener(textWatcher);
                }

            }
        });
        productCount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            MyTextWatcher textWatcher;

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (textWatcher != null && !hasFocus) {
                    ((TextView) v).removeTextChangedListener(textWatcher);
                } else if (hasFocus) {
                    if (textWatcher == null) {
                        textWatcher = new MyTextWatcher(dataModel) {
                            @Override
                            public void afterTextChanged(Editable s) {
                                dataModel.productCount = s.toString();
                            }
                        };
                    }
                    ((TextView) v).addTextChangedListener(textWatcher);
                }

            }
        });
        if (VendingDao.checkNo(dataModel.productNo)) {
            productNo.setText(dataModel.productNo);
        } else {
            productNo.setText("");
        }
        if (VendingDao.checkShowNumb(dataModel.productCount)) {
            productCount.setText(dataModel.productCount);
        } else {
            productCount.setText("");
        }
        id.setText(dataModel._id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String productNoStr = productNo.getText().toString();
                final String productCountStr = productCount.getText().toString();
                if (!VendingDao.checkNo(productNoStr)) {
                    Toast.makeText(NewFallingActivity.this, "编号错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!VendingDao.checkNumb(productCountStr)) {
                    Toast.makeText(NewFallingActivity.this, "数量错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                //DataModel dataModel = model.get(position);
                dataModel.productNo = productNo.getText().toString();
                dataModel.productCount = productCount.getText().toString();
                if (dao.selectListInfo(dataModel._id) == null) {
                    dao.addListInfo(dataModel._id, productNoStr, productCountStr);
                } else {
                    dao.updateListInfo(dataModel._id, productNoStr, productCountStr);
                }
                Toast.makeText(NewFallingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });
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
                initManyViews();
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
                        Toast.makeText(NewFallingActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
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

    abstract class MyTextWatcher implements TextWatcher {
        DataModel dataModel;

        MyTextWatcher(DataModel dataModel) {
            this.dataModel = dataModel;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    }

}
