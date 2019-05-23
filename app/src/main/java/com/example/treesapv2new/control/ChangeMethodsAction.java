package com.example.treesapv2new.control;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.treesapv2new.MainActivity;
import com.example.treesapv2new.R;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.DataSourceList;
import com.example.treesapv2new.display.DisplayMethod;
import com.example.treesapv2new.display.DisplayMethodList;
import com.example.treesapv2new.identify.IdentificationMethod;
import com.example.treesapv2new.identify.IdentificationMethodList;

public class ChangeMethodsAction implements OnClickListener {

    Context parent;
    Dialog dialog;
    LinearLayout radioLayout;
    ArrayList<IdentificationMethod> idMethods = new ArrayList<>();
    ArrayList<DataSource> dataSources = new ArrayList<>();
    ArrayList<DisplayMethod> displays = new ArrayList<>();

    public ChangeMethodsAction(Context context) {
        this.parent = context;
    }

    @Override
    public void onClick(View view) {

        radioLayout = new LinearLayout(parent);

        dialog = new Dialog(parent, R.style.TreeSapDialog);

        dialog.setContentView(R.layout.add_method_dialog);
        dialog.setTitle(R.string.add_method_dialog_title);

        TextView box = (TextView) dialog.findViewById(R.id.addTreeIdentifier);
        box.setTypeface(Typeface.DEFAULT_BOLD);
        final RadioGroup idChoices = (RadioGroup) dialog.findViewById(R.id.add_tree_id_list);
        if (IdentificationMethodList.getList() != null) {
            for (Class idClass : IdentificationMethodList.getList()) {
                try {
                    idMethods.add(((IdentificationMethod) idClass.newInstance()));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            box.setVisibility(View.GONE);
        }

        if(!idMethods.isEmpty()) {
            for (IdentificationMethod method : idMethods) {
                RadioButton button = new RadioButton(parent);
                button.setText(method.getMethodName());
                button.setId(idMethods.indexOf(method));
                IdentificationMethod currentMethodUsed = ((MainActivity) parent).getSelectedIDMethod();
                if(currentMethodUsed != null) {
                    String methodName = currentMethodUsed.getMethodName();
                    button.setChecked(method.getMethodName().equals(methodName));
                }

                button.setTextSize(14);
                idChoices.addView(button, button.getId(), new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }

        box = (TextView) dialog.findViewById(R.id.addDataSourceIdentifier);
        box.setTypeface(Typeface.DEFAULT_BOLD);
        LinearLayout dataChoices= (LinearLayout) dialog.findViewById(R.id.add_data_source_list);
        if (DataSourceList.getList() != null) {
            for (Class idClass : DataSourceList.getList()) {
                try {
                    dataSources.add(((DataSource) idClass.newInstance()));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            box.setVisibility(View.GONE);
        }

        if(!dataSources.isEmpty()) {
            for (DataSource ds : dataSources) {
                CheckBox checkBox = new CheckBox(parent);
                checkBox.setText(ds.getSourceName());
                checkBox.setId(dataSources.indexOf(ds));
                List<DataSource> sources = ((MainActivity) parent).getSelectedDataSources();
                for(DataSource source : sources) {
                    if(ds.getSourceName().equals(source.getSourceName())) {
                        checkBox.setChecked(true);
                        break;
                    }
                }

                checkBox.setTextSize(14);
                dataChoices.addView(checkBox, checkBox.getId(), new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }


        box = (TextView) dialog.findViewById(R.id.addTreeDisplay);
        box.setTypeface(Typeface.DEFAULT_BOLD);
        RadioGroup displayChoices = (RadioGroup) dialog.findViewById(R.id.add_tree_display_list);
        if (DisplayMethodList.getList() != null) {
            for (Class idClass : DisplayMethodList.getList()) {
                try {
                    displays.add(((DisplayMethod) idClass.newInstance()));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            box.setVisibility(View.GONE);
        }

        if(!displays.isEmpty()) {
            for(DisplayMethod display : displays) {
                    RadioButton button = new RadioButton(parent);
                    button.setText(display.getMethodName());
                    button.setId(displays.indexOf(display));
                    DisplayMethod currentMethodUsed = ((MainActivity) parent).getSelectedDisplayMethod();
                    if(currentMethodUsed != null) {
                        String methodUsed = currentMethodUsed.getMethodName();
                        button.setChecked(display.getMethodName().equals(methodUsed));
                    }

                    button.setTextSize(14);
                    displayChoices.addView(button, button.getId(), new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }

        Button button;
        button = (Button) dialog.findViewById(R.id.add_method_choose_button);
        button.setOnClickListener(new ChangeTreeMethodDialogAction(dialog));
        button = (Button) dialog.findViewById(R.id.add_method_cancel_button);
        button.setOnClickListener(new CancelDialogAction(dialog));

        dialog.show();

    }

//    public class addIDMethodRadioButtonAction implements CompoundButton.OnCheckedChangeListener {
//        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//            Spinner spin = (Spinner) dialog.findViewById(R.id.add_tree_id_list);
//            TextView desc = (TextView) dialog.findViewById(R.id.add_tree_id_description);
//            if (checked) {
//                spin.setVisibility(View.VISIBLE);
//                desc.setVisibility(View.VISIBLE);
//            } else {
//                spin.setVisibility(View.GONE);
//                desc.setVisibility(View.GONE);
//            }
//        }
//    }
//
//    public class addDataSourceRadioButtonAction implements CompoundButton.OnCheckedChangeListener {
//        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//            Spinner spin = (Spinner) dialog.findViewById(R.id.add_data_source_list);
//            TextView desc = (TextView) dialog.findViewById(R.id.add_data_source_description);
//            if (checked) {
//                spin.setVisibility(View.VISIBLE);
//                desc.setVisibility(View.VISIBLE);
//            } else {
//                spin.setVisibility(View.GONE);
//                desc.setVisibility(View.GONE);
//            }
//        }
//    }
//
//    public class addDisplayMethodRadioButtonAction implements CompoundButton.OnCheckedChangeListener {
//        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//            Spinner spin = (Spinner) dialog.findViewById(R.id.add_tree_display_list);
//            TextView desc = (TextView) dialog.findViewById(R.id.add_tree_display_description);
//            if (checked) {
//                spin.setVisibility(View.VISIBLE);
//                desc.setVisibility(View.VISIBLE);
//            } else {
//                spin.setVisibility(View.GONE);
//                desc.setVisibility(View.GONE);
//            }
//        }
//    }


    public class CancelDialogAction implements OnClickListener {
        Dialog parentDialog;
        public CancelDialogAction(Dialog dialog) {
            parentDialog = dialog;
        }
        public void onClick(View v) {
            parentDialog.dismiss();
            idMethods.clear();
            dataSources.clear();
            displays.clear();
        }
    }

    public class ChangeTreeMethodDialogAction implements OnClickListener {
        Dialog parentDialog;

        public ChangeTreeMethodDialogAction(Dialog dialog) {
            parentDialog = dialog;
        }

        public void onClick(View v) {
            RadioGroup list;
            int selected;
            Class c;
                list = (RadioGroup) dialog.findViewById(R.id.add_tree_id_list);
                selected = list.getCheckedRadioButtonId();
                if(selected != -1) {
                        ((MainActivity)parent).removeSelectedIDMethod();
                        IdentificationMethod idm = idMethods.get(selected);
                        idm.setParent(parent);
                        ((MainActivity) parent).setSelectedIDMethod(idm, selected);
                }

                LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.add_data_source_list);
                int count = layout.getChildCount();
                ((MainActivity)parent).clearDataSources();
                PrefManager.putString("lastDataSources", "");
                for(int i = 0; i<count; i++) {
                   CheckBox button = (CheckBox) layout.getChildAt(i);
                    String getSources = PrefManager.getString("lastDataSources", "NOPE");
                   if(button.isChecked()) {
                       selected = button.getId();
                       DataSource ds = dataSources.get(selected);
                       ds.setParent(parent);
                       ((MainActivity) parent).addSelectedDataSource(ds, selected);
                   }
                }



            list = (RadioGroup) dialog.findViewById(R.id.add_tree_display_list);
            selected = list.getCheckedRadioButtonId();
            if(selected != -1) {
                DisplayMethod dm = displays.get(selected);
                dm.setParent(parent);
                ((MainActivity) parent).setSelectedDisplayMethod(dm, selected);
            }

            parentDialog.dismiss();
            idMethods.clear();
            dataSources.clear();
            displays.clear();
        }
    }


}
