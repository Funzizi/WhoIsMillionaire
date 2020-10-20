package com.example.whoismillionaire.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.whoismillionaire.R;
import com.example.whoismillionaire.model.History;

import java.util.List;

// Custom Listview
public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<History> list;

    public HistoryAdapter(Context context, int layout, List<History> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    // Số dòng hiển thị trên Listview
    @Override
    public int getCount() {
        return (list.size() > 5) ? 5 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        public TextView tvRankItem;
        public TextView tvQuestionItem;
        public TextView tvTimeItem;
        public TextView tvDateItem;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            // Lấy context Vd : HistoryActivity
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // View chứa layout
            view = inflater.inflate(layout, null);

            viewHolder = new ViewHolder();
            viewHolder.tvRankItem = view.findViewById(R.id.tv_rank_item);
            viewHolder.tvQuestionItem = view.findViewById(R.id.tv_question_item);
            viewHolder.tvTimeItem = view.findViewById(R.id.tv_time_item);
            viewHolder.tvDateItem = view.findViewById(R.id.tv_date_item);
            // Giữ trạng thái ánh xạ
            view.setTag(viewHolder);
        }
        else viewHolder = (ViewHolder) view.getTag();
        // Gán giá trị
        History history = list.get(position);
        viewHolder.tvRankItem.setText(position + 1 + "");
        viewHolder.tvQuestionItem.setText(history.getNumberQuestion() + "");
        viewHolder.tvTimeItem.setText(history.getTime());
        viewHolder.tvDateItem.setText(history.getDate());
        return view;
    }
}
