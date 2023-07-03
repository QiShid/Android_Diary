package com.example.diary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DiaryAdapter extends ArrayAdapter<Diary> {
    private int resourceId;
    public DiaryAdapter(Context context, int textViewResourceId, List<Diary> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Diary diary = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        TextView diaryTitleText = (TextView) view.findViewById(R.id.diary_title);
        if(diary.getTitle().equals("")){
            diaryTitleText.setText("diary"+(position+1));
        }else{
            diaryTitleText.setText(diary.getTitle());
        }
        return view;
    }
}
