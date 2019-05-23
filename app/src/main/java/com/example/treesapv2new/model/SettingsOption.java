package com.example.treesapv2new.model;

public enum SettingsOption {
    SPINNER, EDIT_TEXT, ON_OFF_SWITCH;

    @Override
    public String toString() {
        return super.toString();
    }
    public static SettingsOption parse(String string) {
        String spinner = SettingsOption.SPINNER.toString();
        if(string.equals(SettingsOption.SPINNER.toString())) {
            return SPINNER;
        } else if(string.equals(SettingsOption.EDIT_TEXT.toString())) {
            return EDIT_TEXT;
        } else if(string.equals(SettingsOption.ON_OFF_SWITCH.toString())) {
            return ON_OFF_SWITCH;
        }
        return null;
    }
}
