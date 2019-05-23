package com.example.treesapv2new.view;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DecimalFormat;

import com.example.treesapv2new.R;
import com.example.treesapv2new.control.PrefManager;
import com.example.treesapv2new.identify.IdentificationMethod;
import com.example.treesapv2new.identify.IdentificationMethodList;
import com.example.treesapv2new.model.SettingsOption;

public class Settings extends AppCompatActivity {
    float pref;
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if(PrefManager.getBoolean("scheme", false)) {
                setTheme(R.style.AltTheme);
            }
            LayoutInflater inflater = getLayoutInflater();
            layout = (LinearLayout) inflater.inflate(R.layout.activity_settings, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            addContentView(layout, params);

            for (Class id : IdentificationMethodList.getList()) {
                IdentificationMethod method = null;
                try {
                    method = (IdentificationMethod) id.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (method.getPreferences() == null) {
                    continue;
                }
                LinearLayout titleLayout = new LinearLayout(this);
                LinearLayout titleRow = (LinearLayout) inflater.inflate(R.layout.title_row, layout, false);
                TextView title = titleRow.findViewById(R.id.title1);
                titleRow.removeView(title);
                String[] words = method.getClass().getSimpleName().split("_");
                String name;
                name = words[0];
                for (int i = 1; i < words.length; i++) {
                    name += " " + words[i];
                }
                title.setText(name);
                title.setTextSize(18);
                title.setTypeface(Typeface.DEFAULT_BOLD);
                titleLayout.addView(title);
                layout.addView(titleLayout);
                for (String setting : method.getPreferences()) {
                    final String[] parts = setting.split(" \\| ");
                    String questionText = parts[0];
                    final String key = parts[1];
                    SettingsOption option = SettingsOption.parse(parts[2]);
                    if (option.equals(SettingsOption.SPINNER)) {
                        LinearLayout spinnerLayout = new LinearLayout(this);
                        LinearLayout sl = (LinearLayout) inflater.inflate(R.layout.spinner_layout, layout, false);
                        TextView question = sl.findViewById(R.id.spinnerText);
                        sl.removeView(question);
                        final Spinner spinner = sl.findViewById(R.id.spinner);
                        sl.removeView(spinner);
                        question.setText(questionText);
                        question.setTextSize(20);
                        int resource = getResources().getIdentifier(parts[3], "array", this.getPackageName());
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, resource, R.layout.spinner_format);
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(Math.round(PrefManager.getFloat(key, pref)));
                        spinner.post(new Runnable() {
                            @Override
                            public void run() {
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        pref = (float) position;
                                        PrefManager.putFloat(key, pref);
                                        spinner.setSelection(position);
                                    }
                                        @Override
                                        public void onNothingSelected (AdapterView< ? > parent){

                                        }
                                    });
                            }
                        });

                        spinnerLayout.addView(question);
                        spinnerLayout.addView(spinner);
                        layout.addView(spinnerLayout);
                        continue;
                    } else if (option.equals(SettingsOption.EDIT_TEXT)) {
                        final LinearLayout textLayout = new LinearLayout(this);
                        LinearLayout editTextLayout = (LinearLayout) inflater.inflate(R.layout.edit_text_layout, layout, false);
                        TextView question = editTextLayout.findViewById(R.id.editTextTitle);
                        editTextLayout.removeView(question);
                        final EditText input = editTextLayout.findViewById(R.id.editText);
                        editTextLayout.removeView(input);
                        question.setText(questionText);
                        question.setTextSize(20);
                        input.setTextSize(20);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        if (Boolean.parseBoolean(parts[3])) {
                            input.setText(String.valueOf(PrefManager.getFloat(key, Float.valueOf(parts[4]))));
                        } else {
                            input.setText(PrefManager.getString(key, parts[4]));
                        }
                        textLayout.addView(question);
                        textLayout.addView(input);
                        input.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if(s.toString().equals("")){

                                }
                                else {
                                    PrefManager.putFloat(key, Float.valueOf(s.toString()));
                                }
                            }
                        });
                        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                if (actionId == EditorInfo.IME_ACTION_DONE) {
                                    String result = input.getText().toString();
                                    if (Boolean.parseBoolean(parts[3])) {
                                        try {
                                            DecimalFormat df = new DecimalFormat("0.0");
                                            float distance = Float.parseFloat(result);
                                            PrefManager.putFloat(key, distance);
                                            input.setText(df.format(distance));
                                        }catch (NumberFormatException e){
                                            final AlertDialog.Builder a_builder = new AlertDialog.Builder(Settings.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                                            a_builder.setMessage("This input was not recognized as a number. Please change your ").setCancelable(false)
                                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            AlertDialog alert = a_builder.create();
                                            alert.setTitle("Invalid Number");
                                            alert.show();

                                        }
                                    } else {
                                        PrefManager.putString(key, result);
                                        input.setText(result);
                                    }
                                }
                                return false;
                            }
                        });

                        layout.addView(textLayout);
                        continue;
                    } else {
                        LinearLayout switchLayout = new LinearLayout(this);
                        LinearLayout sl = (LinearLayout) inflater.inflate(R.layout.switch_layout, layout, false);
                        TextView question = sl.findViewById(R.id.switchText);
                        sl.removeView(question);
                        final Switch switch1 = sl.findViewById(R.id.switchToggle);
                        sl.removeView(switch1);
                        question.setText(questionText);
                        question.setTextSize(20);
                        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(switch1.isChecked()) {
                                    PrefManager.putBoolean(key, true);
                                } else if(!switch1.isChecked()) {
                                    PrefManager.putBoolean(key,false);
                                }
                            }
                        });
                        if(PrefManager.getBoolean(key, true)) {
                            switch1.setChecked(true);
                        }
                        switchLayout.addView(question);
                        switchLayout.addView(switch1);
                        layout.addView(switchLayout);
                        continue;
                    }

                }
            }
        }
    }
