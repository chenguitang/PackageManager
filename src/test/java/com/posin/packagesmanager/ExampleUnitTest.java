package com.posin.packagesmanager;

import com.google.gson.Gson;
import com.posin.packagesmanager.bean.PackagesMessage;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
//        gsonTest();
        myTest();
    }

    private void myTest() {
        for (int i = 0; i < 4; i++) {
            for (int j = 5; j > 0; j--) {
                if (i == j) {
                    continue;
                }
                System.out.println("j=" + j);
            }
            System.out.println("i=" + i);
            System.out.println("=================================");
        }
    }

    public void gsonTest() {
        Gson gson = new Gson();
        ArrayList<PackagesMessage.DisabledBean> listDisableBeans = new ArrayList<>();
        PackagesMessage.DisabledBean disabledBean = new
                PackagesMessage.DisabledBean("com.posin.install", "com.posin.install.mainactivity");
        listDisableBeans.add(disabledBean);
        PackagesMessage packagesMessage = new PackagesMessage("user", listDisableBeans);

        String mJsonMessage = gson.toJson(packagesMessage);
        System.out.println("mJsonMessage: " + mJsonMessage);
    }
}