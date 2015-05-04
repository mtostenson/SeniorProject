package com.mt523.backtalk.util;

import java.util.Locale;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.mt523.backtalk.R;

public class SettingsMenuAdapter extends ArrayAdapter<String> {

    private Context context;
    private static final String[] settings = { "Vibration", "Sound" };

    public SettingsMenuAdapter(Context context) {
        super(context, R.layout.settings_menu_item, settings);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.settings_menu_item, parent,
                    false);
        }
        TextView label = (TextView) convertView
                .findViewById(R.id.setting_label);
        CheckBox checkBox = (CheckBox) convertView
                .findViewById(R.id.settings_checkbox);
        checkBox.setChecked(BTFX.getSetting(settings[position]
                .toLowerCase(Locale.ENGLISH)));
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                BTFX.changeSetting(
                        settings[position].toLowerCase(Locale.ENGLISH),
                        isChecked);
            }
        });
        label.setText(settings[position].toUpperCase(Locale.ENGLISH));
        return convertView;
    }

}
