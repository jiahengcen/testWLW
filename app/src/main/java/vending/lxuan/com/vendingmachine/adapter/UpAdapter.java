package vending.lxuan.com.vendingmachine.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vending.lxuan.com.vendingmachine.R;
import vending.lxuan.com.vendingmachine.dao.VendingDao;
import vending.lxuan.com.vendingmachine.model.DataModel;
import vending.lxuan.com.vendingmachine.model.UpModel;

/**
 * Created by apple
 * 18/5/9
 */

public class UpAdapter extends BaseAdapter {
    private List<DataModel> model;
    private final VendingDao dao;
    // private String[] allProductNumber;
    // private String[] allProductCount;

    public UpAdapter(List<DataModel> model) {
        this.model = model;
        dao = new VendingDao();
    }

//    public UpAdapter(List<String> allProductNumber, List<String> allProductCount) {
//        this.allProductNumber = allProductNumber;
//        this.allProductCount = allProductCount;
//        dao = new VendingDao();
//    }

    @Override
    public int getCount() {
        if (model == null) return 0;
        return model.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final Context ctx = viewGroup.getContext();
        View v;
//        if (allProductNumber == null || allProductNumber.length != getCount()) {
//            allProductNumber = new String[getCount()];
//        }
//        if (allProductCount == null || allProductCount.length != getCount()) {
//            allProductCount = new String[getCount()];
//        }
        if (view == null) {
            v = LayoutInflater.from(ctx).inflate(R.layout.adapter_up, viewGroup, false);
        } else {
            v = view;
        }
        TextView id = v.findViewById(R.id.tv_id);
        final EditText no = v.findViewById(R.id.et_no);
        final EditText numb = v.findViewById(R.id.et_numb);
        Button button = v.findViewById(R.id.btn_commit);
        final DataModel dataModel = model.get(position);
        no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        numb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        if (VendingDao.checkNo(model.get(position).productNo)) {
            no.setText(model.get(position).productNo);
        } else {
            no.setText("");
        }
        if (VendingDao.checkShowNumb(model.get(position).productCount)) {
            numb.setText(model.get(position).productCount);
        } else {
            numb.setText("");
        }
        id.setText(model.get(position)._id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String productNo = no.getText().toString();
                final String productCount = numb.getText().toString();
                if (!VendingDao.checkNo(productNo)) {
                    Toast.makeText(ctx, "编号错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!VendingDao.checkNumb(productCount)) {
                    Toast.makeText(ctx, "数量错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                DataModel dataModel = model.get(position);
                dataModel.productNo = no.getText().toString();
                dataModel.productCount = numb.getText().toString();
                if (dao.selectListInfo(String.valueOf(position + 1)) == null) {
                    dao.addListInfo(String.valueOf(position + 1), productNo, productCount);
                } else {
                    dao.updateListInfo(String.valueOf(position + 1), productNo, productCount);
                }
                Toast.makeText(ctx, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
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
