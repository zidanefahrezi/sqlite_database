package info.androidhive.sqlite.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.DatabaseHelper;
import info.androidhive.sqlite.database.model.Note;
import info.androidhive.sqlite.utils.MyDividerItemDecoration;

public class MainActivity extends AppCompatActivity implements NotesAdapter.OnNoteListener {
    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNotesView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noNotesView = findViewById(R.id.empty_notes_view);

        db = new DatabaseHelper(this);

        notesList.addAll(db.getAllNotes());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1);
            }
        });

        mAdapter = new NotesAdapter(this, notesList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_all) {
            showDeleteAllDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAllDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Notes");
        builder.setMessage("Are you sure you want to delete all notes?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllNotes();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void deleteAllNotes() {
        db.deleteAllNotes();
        notesList.clear();
        mAdapter.notifyDataSetChanged();
        toggleEmptyNotes();
    }

    private void createNote(String nama, int regist_no, int phone, String email) {
        long id = db.insertNote(nama, regist_no, phone, email);
        Note n = db.getNote(id);

        if (n != null) {
            notesList.add(0, n);
            mAdapter.notifyDataSetChanged();
            toggleEmptyNotes();
        }
    }

    private void updateNote(String nama, int regist_no, int phone, String email, int position) {
        Note n = notesList.get(position);
        n.setNama(nama);
        n.setRegistNo(regist_no);
        n.setPhone(phone);
        n.setEmail(email);

        db.updateNote(n);
        notesList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }

    private void deleteNote(int position) {
        db.deleteNote(notesList.get(position));
        notesList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    @Override
    public void onUpdateClick(int position) {
        showNoteDialog(true, notesList.get(position), position);
    }

    @Override
    public void onDeleteClick(int position) {
        deleteNote(position);
    }

    private void showNoteDialog(final boolean shouldUpdate, final Note note, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNama = view.findViewById(R.id.nama);
        final EditText inputRegistNo = view.findViewById(R.id.regist_no);
        final EditText inputPhone = view.findViewById(R.id.phone);
        final EditText inputEmail = view.findViewById(R.id.email);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        if (shouldUpdate && note != null) {
            inputNama.setText(note.getNama());
            inputRegistNo.setText(String.valueOf(note.getRegistNo()));
            inputPhone.setText(String.valueOf(note.getPhone()));
            inputEmail.setText(note.getEmail());
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // Intentionally left empty
                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputNama.getText().toString()) ||
                        TextUtils.isEmpty(inputRegistNo.getText().toString()) ||
                        TextUtils.isEmpty(inputPhone.getText().toString()) ||
                        TextUtils.isEmpty(inputEmail.getText().toString())) {
                    Toast.makeText(MainActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (shouldUpdate && note != null) {
                    updateNote(inputNama.getText().toString(),
                            Integer.parseInt(inputRegistNo.getText().toString()),
                            Integer.parseInt(inputPhone.getText().toString()),
                            inputEmail.getText().toString(), position);
                } else {
                    createNote(inputNama.getText().toString(),
                            Integer.parseInt(inputRegistNo.getText().toString()),
                            Integer.parseInt(inputPhone.getText().toString()),
                            inputEmail.getText().toString());
                }
            }
        });
    }

    private void toggleEmptyNotes() {
        if (db.getNotesCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }
}
