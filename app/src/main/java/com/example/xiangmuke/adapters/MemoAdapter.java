package com.example.xiangmuke.adapters;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xiangmuke.R;
import com.example.xiangmuke.models.Memo;

import java.util.List;

public class MemoAdapter extends BaseAdapter {
    private Context context;
    private List<Memo> memoList;

    public MemoAdapter(Context context, List<Memo> memoList) {
        this.context = context;
        this.memoList = memoList;
    }

    @Override
    public int getCount() {
        return memoList.size();
    }

    @Override
    public Object getItem(int position) {
        return memoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_memo, parent, false);
            holder.titleTextView = convertView.findViewById(R.id.tv_title);
            holder.categoryTextView = convertView.findViewById(R.id.tv_category);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Memo memo = memoList.get(position);
        if (memo!= null) {
            holder.titleTextView.setText(memo.getTitle());
            holder.categoryTextView.setText(memo.getCategory());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView categoryTextView;
    }
}