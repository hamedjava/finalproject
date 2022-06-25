package com.example.notebook;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<NoteModel> noteModels;
    private Context context;
    private NoteDatabase myDatabase;

    public NoteAdapter(Context context,List<NoteModel> noteModels){
        this.noteModels=noteModels;
        this.context=context;
        myDatabase=new NoteDatabase(context);
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.note_row,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
        final NoteModel model=noteModels.get(position);
        holder.txtTitle.setText(model.getTitle());
        holder.txtDesc.setText(model.getDesc());
        holder.txtTime.setText(model.getTime());

        if(model.getRemember()==0){
            holder.imgClock.setVisibility(View.GONE);
            holder.txtTime.setVisibility(View.GONE);
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialog);
                TextView txtYes=(TextView)dialog.findViewById(R.id.txt_dialog_yes);
                TextView txtNo=(TextView)dialog.findViewById(R.id.txt_dialog_no);

                txtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDatabase.deleteRow(model.getId());
                        noteModels.remove(model);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,noteModels.size());
                        dialog.dismiss();
                    }
                });

                txtNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });


        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.edit_dialog);

                final EditText edtTitle=(EditText)dialog.findViewById(R.id.edt_editDialog_title);
                final EditText edtDesc=(EditText)dialog.findViewById(R.id.edt_editDialog_desc);
                Button btnUpdate=(Button)dialog.findViewById(R.id.btn_editDialog_update);
                Button btnCancel=(Button)dialog.findViewById(R.id.btn_editDialog_cancel);

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDatabase.updateRow(model.getId(),edtTitle.getText().toString(),edtDesc.getText().toString());
                        dialog.dismiss();
                        NoteModel noteModel=new NoteModel();
                        noteModel.setTitle(edtTitle.getText().toString());
                        noteModel.setDesc(edtDesc.getText().toString());
                        noteModel.setTime(model.getTime());
                        noteModels.add(position,noteModel);
                        notifyItemChanged(position);
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return noteModels.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle,txtDesc,txtTime;
        ImageView imgEdit,imgDelete,imgClock;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle=(TextView)itemView.findViewById(R.id.txt_noteRow_title);
            txtDesc=(TextView)itemView.findViewById(R.id.txt_noteRow_desc);
            txtTime=(TextView)itemView.findViewById(R.id.txt_noteRow_time);
            imgEdit=(ImageView)itemView.findViewById(R.id.img_noteRow_edit);
            imgDelete=(ImageView)itemView.findViewById(R.id.img_noteRow_delete);
            imgClock=(ImageView)itemView.findViewById(R.id.img_noteRow_clock);
        }
    }
}

