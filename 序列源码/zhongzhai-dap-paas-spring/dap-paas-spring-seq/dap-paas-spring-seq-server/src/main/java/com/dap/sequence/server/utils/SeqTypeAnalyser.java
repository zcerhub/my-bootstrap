package com.dap.sequence.server.utils;


import java.util.regex.Pattern;


public class SeqTypeAnalyser {

    private static final String NUM_REG = "^[1-9]\\d*$";

    public static boolean checkNum(String str){
        Pattern r = Pattern.compile(NUM_REG);
        return r.matcher(str).matches();
    }


}
