package info.androidhive.sqlite.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.model.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private Context context;
    private List<Note> notesList;
    private OnNoteListener onNoteListener;

    public interface OnNoteListener {
        void onUpdateClick(int position);
        void onDeleteClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, registNo, phone, email, timestamp;
        public Button btnUpdate, btnDelete;

        public MyViewHolder(View view) {
            super(view);
            nama = view.findViewById(R.id.nama);
            registNo = view.findViewById(R.id.regist_no);
            phone = view.findViewById(R.id.phone);
            email = view.findViewById(R.id.email);
            timestamp = view.findViewById(R.id.timestamp);
            btnUpdate = view.findViewById(R.id.btn_update);
            btnDelete = view.findViewById(R.id.btn_delete);
        }
    }

    public NotesAdapter(Context context, List<Note> notesList, OnNoteListener onNoteListener) {
        this.context = context;
        this.notesList = notesList;
        this.onNoteListener = onNoteListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Note note = notesList.get(position);
        holder.nama.setText(note.getNama());
        holder.registNo.setText(String.valueOf(note.getRegistNo()));
        holder.phone.setText(String.valueOf(note.getPhone()));
        holder.email.setText(note.getEmail());
        holder.timestamp.setText(formatDate(note.getTimestamp()));

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNoteListener.onUpdateClick(position);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNoteListener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}
