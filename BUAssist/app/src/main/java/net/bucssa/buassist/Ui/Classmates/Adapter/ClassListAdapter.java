package net.bucssa.buassist.Ui.Classmates.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bucssa.buassist.Bean.Classmate.Class;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.ClassDetailActivity;

import java.util.List;

/**
 * Created by KimuraShin on 17/7/29.
 */

public class ClassListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Class> mDatas;
    private LayoutInflater mInflater;

    public ClassListAdapter(Context context, List<Class> data) {
        this.mContext = context;
        this.mDatas = data;
        this.mInflater = LayoutInflater.from(context);
    }

    public void clearData() {
        this.mDatas.clear();
        notifyDataSetChanged();
    }

    public void addData(int position, List<Class> data) {
        if (data != null && data.size() > 0) {
            mDatas.addAll(data);
            notifyDataSetChanged();
        }
    }
    public void addData(List<Class> data) {
        addData(0, data);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Class classItem = mDatas.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_class_list, null);
            viewHolder.rootView = (LinearLayout) convertView.findViewById(R.id.rootView);
            viewHolder.classGroup = (TextView) convertView.findViewById(R.id.tv_classGroup);
            viewHolder.classCode = (TextView) convertView.findViewById(R.id.tv_classCode);
            viewHolder.professor = (TextView) convertView.findViewById(R.id.tv_professor);
            viewHolder.studentCount = (TextView) convertView.findViewById(R.id.tv_studentCount);
            viewHolder.groupCount = (TextView) convertView.findViewById(R.id.tv_groupCount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.classGroup.setText(classItem.getClassGroup());
        viewHolder.classCode.setText(classItem.getClassCode());
        viewHolder.professor.setText(classItem.getProfessorName());
        viewHolder.studentCount.setText(String.valueOf(classItem.getStudentCount()));
        viewHolder.groupCount.setText(String.valueOf(classItem.getGroupCount()));
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClassDetailActivity.class);
                intent.putExtra("Class", classItem);
                ((Activity) mContext).startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHolder{
        LinearLayout rootView;
        TextView classGroup;
        TextView classCode;
        TextView professor;
        TextView studentCount;
        TextView groupCount;
    }
}

