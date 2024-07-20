package com.dap.paas.console.basic.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecResult {
    public static final String SUCCESS = "0";
    public static final String ERROR = "1";


    private String status;
    private String result;
}
