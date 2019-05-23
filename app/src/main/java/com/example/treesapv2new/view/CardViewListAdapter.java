package com.example.treesapv2new.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.treesapv2new.MainActivity;
import com.example.treesapv2new.R;
import com.example.treesapv2new.model.BulletedListItem;

public class CardViewListAdapter
        extends RecyclerView.Adapter<CardViewListAdapter.CardHolder> {

    private Context parent;
    private List<BulletedListItem> itemList = new ArrayList<BulletedListItem>();
    private LayoutInflater mInflater ;

    public static class CardHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView icon;
        public TextView title;
        public TextView description;
        public View card;

        public CardHolder(View card) {
            super(card);

            this.card = card;

            icon = card.findViewById(R.id.item_card_view_icon);
            title = card.findViewById(R.id.item_card_view_name);
            description = card.findViewById(R.id.item_card_view_description);
        }
    }

    public CardViewListAdapter(Context context, List<BulletedListItem> list) {
        parent = context;
        itemList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItems(List<BulletedListItem> blis) {
        itemList = blis;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardViewListAdapter.CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View cardView = mInflater.inflate(R.layout.card_view_item, null);
        CardHolder vh = new CardHolder(cardView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CardViewListAdapter.CardHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (itemList.get(position).getName().startsWith("ID:")) {
            holder.icon.setImageResource(R.drawable.search);
        } else if (itemList.get(position).getName().startsWith("Data:")) {
            holder.icon.setImageResource(R.drawable.data);
        } else {
            holder.icon.setImageResource(R.drawable.display);
        }
        holder.title.setText(itemList.get(position).getName().substring(itemList.get(position).getName().indexOf(":")+1));
        holder.title.setTextColor(itemList.get(position).getNameColor());
        String desc = itemList.get(position).getLink();
        holder.description.setText(desc == null ? "" : desc.substring(desc.indexOf(":")+1));

        //holder.card.setOnClickListener(new CardViewAction(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class CardViewAction implements View.OnClickListener {

        int position = -1;

        public CardViewAction(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {

            BulletedListItem bli = itemList.get(position);
            String link = bli.getLink();
            if(link!=null) {
                if (link.startsWith("display:")) {

                    String msg = link.substring(link.indexOf(':') + 1);

                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(parent, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(parent);
                    }
                    builder.setTitle("Identifier Description")
                            .setMessage(msg)
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                } else if (link.startsWith("iddisplay:")) {

                    String msg = link.substring(link.indexOf(':') + 1);

                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(parent, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(parent);
                    }
                    builder.setTitle("Identifier Description")
                            .setMessage(msg)
                            .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ((MainActivity) parent).removeSelectedIDMethod();
                                }
                            }).setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(android.R.drawable.ic_dialog_info)
                            .show();

                } else if (link.startsWith("change:")) {
                    String subject = link.substring(link.indexOf(':') + 1);
                    Dialog dialog = new Dialog(parent);


                    if (subject.equals("id")) {
                        dialog.setContentView(R.layout.new_tree_id_dialog);
                        dialog.setTitle("Choose Tree Identifier");

                        Spinner choicesSpinner = (Spinner) dialog.findViewById(R.id.new_tree_id_list);
                        ArrayAdapter<String> cAdapter = new ArrayAdapter<String>(parent, R.layout.add_tree_id_choices_spinner);
                        cAdapter.setDropDownViewResource(R.layout.add_tree_id_choices_spinner);
                        if (itemList.size() > 0) {
                            for (BulletedListItem bli2 : itemList) {
                                cAdapter.add(bli2.getName());
                            }
                        }
                        choicesSpinner.setAdapter(cAdapter);
                    }

                    Button button;
                    button = (Button) dialog.findViewById(R.id.new_tree_id_choose_button);
                    //button.setOnClickListener(new createNewTaskAction(dialog));
                    button = (Button) dialog.findViewById(R.id.new_tree_id_cancel_button);
                    button.setOnClickListener(new CardViewListAdapter.cancelDialogAction(dialog));

                    dialog.show();
                } else if (link.startsWith("http:") || link.startsWith("https:")) {
                    String subject = link.substring(link.indexOf(':') + 1);

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    parent.startActivity(browserIntent);
                }
            }
        }

    }

    public class cancelDialogAction implements View.OnClickListener {
        Dialog parentDialog;
        public cancelDialogAction(Dialog dialog) {
            parentDialog = dialog;
        }
        public void onClick(View v) {
            parentDialog.dismiss();
        }
    }


}
