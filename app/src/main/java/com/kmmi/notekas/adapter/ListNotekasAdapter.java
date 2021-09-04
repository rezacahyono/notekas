package com.kmmi.notekas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.kmmi.notekas.R;
import com.kmmi.notekas.model.Notekas;
import com.kmmi.notekas.ui.detail.DetailNoteFragmentDirections;
import com.kmmi.notekas.ui.home.HomeFragmentDirections;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ListNotekasAdapter extends RecyclerView.Adapter<ListNotekasAdapter.ListNotekasViewHolder> {
    private final ArrayList<Notekas> listNotekas = new ArrayList<>();
    private final int isFrom;

    public ListNotekasAdapter(int isFrom) {
        this.isFrom = isFrom;
    }


    @NonNull
    @Override
    public ListNotekasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notekas_list, parent, false);
        return new ListNotekasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListNotekasViewHolder holder, int position) {
        holder.bind(listNotekas.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (isFrom == 1) {
                HomeFragmentDirections.ActionHomeFragmentToAddNoteFragment toAddNoteFragmentFromHome = HomeFragmentDirections.actionHomeFragmentToAddNoteFragment();
                toAddNoteFragmentFromHome.setPosition(position);
                toAddNoteFragmentFromHome.setNote(listNotekas.get(position));
                toAddNoteFragmentFromHome.setIsBack(isFrom);
                Navigation.findNavController(holder.itemView).navigate(toAddNoteFragmentFromHome);
            } else if (isFrom == 2) {
                DetailNoteFragmentDirections.ActionDetailNoteFragmentToAddNoteFragment toAddNoteFragmentFromDetail = DetailNoteFragmentDirections.actionDetailNoteFragmentToAddNoteFragment();
                toAddNoteFragmentFromDetail.setPosition(position);
                toAddNoteFragmentFromDetail.setNote(listNotekas.get(position));
                toAddNoteFragmentFromDetail.setIsBack(isFrom);
                Navigation.findNavController(holder.itemView).navigate(toAddNoteFragmentFromDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNotekas.size();
    }

    public void setListNotekas(ArrayList<Notekas> listNotekas) {
        if (listNotekas.size() > 0) {
            this.listNotekas.clear();
        }
        this.listNotekas.addAll(listNotekas);
        notifyDataSetChanged();
    }

    public ArrayList<Notekas> getListNotekas() {
        return this.listNotekas;
    }

    public void addItem(Notekas notekas) {
        this.listNotekas.add(notekas);
        notifyItemInserted(listNotekas.size() - 1);
    }

    public void updateItem(int position, Notekas notekas) {
        listNotekas.set(position, notekas);
        notifyItemChanged(position, notekas);
    }

    public void removeItem(int position) {
        this.listNotekas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.listNotekas.size());
    }

    public static class ListNotekasViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTypeInput, tvDate, tvTitle, tvDescription, tvAmount;

        public ListNotekasViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTypeInput = itemView.findViewById(R.id.tv_type_input);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvAmount = itemView.findViewById(R.id.tv_amount);
        }

        public void bind(Notekas notekas) {
            Locale localeID = new Locale("in", "ID");
            NumberFormat formatRp = NumberFormat.getCurrencyInstance(localeID);
            if (notekas.getTypeInput().equals("Pemasukan")) {
                tvTypeInput.setTextColor(itemView.getResources().getColor(R.color.green));
                tvAmount.setTextColor(itemView.getResources().getColor(R.color.green));
                tvAmount.setText(itemView.getResources().getString(R.string.amount_income, (formatRp.format(notekas.getAmount()))));
            } else {
                tvAmount.setText(itemView.getResources().getString(R.string.amount_expenditure, (formatRp.format(notekas.getAmount()))));
                tvTypeInput.setTextColor(itemView.getResources().getColor(R.color.red));

                tvAmount.setTextColor(itemView.getResources().getColor(R.color.red));
            }
            tvTypeInput.setText(notekas.getTypeInput());
            tvTitle.setText(notekas.getTitle());
            tvDescription.setText(notekas.getDescription());
            tvDate.setText(notekas.getDate());
        }
    }
}
