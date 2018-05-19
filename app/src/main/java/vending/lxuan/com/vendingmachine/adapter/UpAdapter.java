package vending.lxuan.com.vendingmachine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vending.lxuan.com.vendingmachine.R;
import vending.lxuan.com.vendingmachine.dao.VendingDao;
import vending.lxuan.com.vendingmachine.model.UpModel;

/**
 * Created by apple
 * 18/5/9
 */

public class UpAdapter extends BaseAdapter {
    private UpModel model;
    private final VendingDao dao;

    public UpAdapter(UpModel model) {
        this.model = model;
        dao = new VendingDao();
    }

    @Override
    public int getCount() {
        return model.name.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Context ctx = viewGroup.getContext();
        View v = LayoutInflater.from(ctx).inflate(R.layout.adapter_up, viewGroup, false);
        TextView id = v.findViewById(R.id.tv_id);
        final EditText no = v.findViewById(R.id.et_no);
        final EditText numb = v.findViewById(R.id.et_numb);
        Button button = v.findViewById(R.id.btn_commit);
        id.setText(model.name.get(i));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkNo(no.getText().toString())) {
                    Toast.makeText(ctx, "编号错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!checkNumb(numb.getText().toString())) {
                    Toast.makeText(ctx, "数量错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dao.selectListInfo(model.name.get(i)) == null) {
                    dao.addListInfo(model.name.get(i), no.getText().toString(), numb.getText().toString());
                } else {
                    dao.updateListInfo(model.name.get(i), no.getText().toString(), numb.getText().toString());
                }
                Toast.makeText(ctx, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    public boolean checkNo(String no) {
        return "12345".contains(no);
    }

    public boolean checkNumb(String numb) {
        return "1234567".contains(numb);
    }
}
