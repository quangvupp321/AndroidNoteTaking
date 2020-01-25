package com.example.notetaking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class NoteAdapter extends BaseAdapter implements declareList {
    private Context context;
    private int layout;
    private ArrayList<Note> noteList;
    private ArrayList<Note> arrayList;

    public NoteAdapter(Context context, int layout, ArrayList<Note> noteList) {
        this.context = context;
        this.layout = layout;
        this.noteList = noteList;
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(noteList);
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView txtheader;
        TextView txtdate;
        TextView txttag;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //ViewHolder:
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            holder = new ViewHolder();
            //ánh xạ view:
            holder.txtheader = (TextView) view.findViewById(R.id.textViewHeader);
            holder.txtdate = (TextView) view.findViewById(R.id.textViewDate);
            holder.txttag = (TextView) view.findViewById(R.id.textViewTag);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //Gán giá trị
        Note note = noteList.get(i);

        holder.txtheader.setText(note.getHeader());
        holder.txtdate.setText("Date modified: " + note.getDate());
        if (note.getTag().length() == 0) {
            holder.txttag.setText("Tag: None");
        } else {
            holder.txttag.setText("Tag: " + note.getTag());
        }

        return view;
    }

    public void filter(String text) {
        text = text.toLowerCase(Locale.getDefault());
        noteList.clear();
        if (text.length() == 0) {
            noteList.addAll(arrayList);
        } else {
            for (Note note : arrayList) {
                if (note.getHeader()
                        .toLowerCase(Locale.getDefault())
                        .contains(text) ||
                    note.getTag()
                        .toLowerCase(Locale.getDefault())
                        .contains(text) ||
                    note.getContent()
                        .toLowerCase(Locale.getDefault())
                        .contains(text) ||
                    note.getDate()
                        .toLowerCase(Locale.getDefault())
                        .contains(text))
                {
                    noteList.add(note);
                }
            }
        }
        Collections.reverse(noteList);
        notifyDataSetChanged();
    }
}
