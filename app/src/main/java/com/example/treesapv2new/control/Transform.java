package com.example.treesapv2new.control;

public class Transform {

    public static String ChangeName(String commonName){
        if (commonName.contains("-")) {
            String[] array = commonName.split("-");
            commonName = array[1] + " " + array[0];
        }
        String[] array = commonName.split(" ");
        if(array.length==3&&array[1].length()<2){
            array = new String[]{array[0], array[2]};
        }
        if(array.length==4){
            array = new String[]{array[0], array[1], array[3]};
        }
        if (array.length>1&&array[array.length - 1].toCharArray()[0] < 97) {
            char[] chars = array[array.length - 1].toCharArray();
            chars[0] = (char) (chars[0] + 32);
            String last = String.valueOf(chars);
            array[array.length - 1] = last;
            if (array.length > 2) {
                if (array[array.length - 2].toCharArray()[0] < 97) {
                    char[] chars1 = array[array.length - 2].toCharArray();
                    chars1[0] = (char) (chars1[0] + 32);
                    String last1 = String.valueOf(chars1);
                    array[array.length - 2] = last1;
                }
            }
            commonName = "";
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals("")) {
                    continue;
                }
                if (i > array.length - 2) {
                    commonName += array[i];
                } else {
                    commonName += array[i] + " ";
                }
            }
        }
        if(commonName.equals("Little leaf linden")){
            commonName = "Littleleaf linden";
        }
        if(commonName.equals("Common honeylocust")){
            commonName = "Honeylocust";
        }
        if(commonName.equals("Crimson king maple")){
            commonName = "Norway maple";
        }
        if(commonName.equals("Japanese flowering crabapple")){
            commonName="Japanese flower crabapple";
        }
        if(commonName.equals("Flowering cherry")) {
            commonName = "Japanese flowering cherry";
        }
        if(commonName.equals("Colorado blue spruce")){
            commonName= "Blue spruce";
        }
        if(commonName.equals("Eastern redcedar")){
            commonName = "Eastern red cedar";
        }
        return commonName;
    }
}
