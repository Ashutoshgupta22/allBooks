package com.aspark.allbooks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

import android.content.Context;
import android.widget.TextView;

import com.aspark.allbooks.Activity.ResetPasswordActivity;
import com.aspark.allbooks.Network.NetworkRequest;
import com.google.errorprone.annotations.DoNotMock;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Mock
    Context mContext;
    @Mock
    TextView mTextView;
    NetworkRequest networkRequest = new NetworkRequest();
    List<String> list = new ArrayList<>();

    @Before
    public void innit() {


        list.add("horror");
        list.add("romance");



    }

    @Test
    public void testCheckIfEmailRegistered() {

//        assertEquals(null,networkRequest.youMayLike(list,mTextView));

//        assertThrows()

    }


}