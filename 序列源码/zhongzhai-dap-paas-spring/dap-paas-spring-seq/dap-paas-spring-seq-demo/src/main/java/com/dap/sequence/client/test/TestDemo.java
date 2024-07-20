package com.dap.sequence.client.test;

import com.dap.sequence.client.utils.DatePlaceholderUtil;
import com.dap.sequence.client.utils.PlaceholderRuleUtil;

import java.util.Date;

/**
 * @description:
 * @author: zhangce
 * @create: 7/6/2024 10:56 PM
 **/
public class TestDemo {


    public static void main(String[] args) {
//        test1();
//        test2();
//        test3();
//        test4();
//        test5();
//        test6();
        test7();
    }

    private static void test7() {
        String result = DatePlaceholderUtil.datePlaceholderHandle(
                "bbbb{2,yyyyMMdd}cccc{2,yyyyMMddHHmmss}dddd{2,yyyy-MM-dd}eee{2,yyyy/MM/dd}fff{2,MM/dd/yyyy}", new Date());
        System.out.println(result);
    }

    private static void test6() {
        String result = DatePlaceholderUtil.datePlaceholderHandle(
                "bbbb{2,yyyyMMdd}cccc{2,yyyyMMddHHmmss}dddd{2,yyyy-MM-dd}eee{2,yyyy/MM/dd}", new Date());
        System.out.println(result);
    }

    private static void test5() {
        String result = DatePlaceholderUtil.datePlaceholderHandle(
                "bbbb{2,yyyyMMdd}cccc{2,yyyyMMddHHmmss}dddd{2,yyyy-MM-dd}", new Date());
        System.out.println(result);
    }

    private static void test4() {
        String result = DatePlaceholderUtil.datePlaceholderHandle("bbbb{2,yyyyMMdd}cccc{2,yyyyMMddHHmmss}", new Date());
        System.out.println(result);
    }

    private static void test3() {
        String result = DatePlaceholderUtil.datePlaceholderHandle("bbbb{2,yyyyMMdd}cccc", new Date());
        System.out.println(result);
    }

    private static void test1() {
        String templateStr="bbb{0,}aaa";
        String[] params = new String[]{"ccceeee", "ddd"};
        String result1 = PlaceholderRuleUtil.placeholderHandle(templateStr, params);
        System.out.println(result1);
    }

    private static void test2() {
        String templateStr="bbb{1,1}aaa";
        String[] params = new String[]{"ccceeee", "ddd"};
        String result1 = PlaceholderRuleUtil.placeholderHandle(templateStr, params);
        System.out.println(result1);
    }

}
