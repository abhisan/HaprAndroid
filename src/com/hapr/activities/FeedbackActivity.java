package com.hapr.activities;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import com.technotalkative.viewstubdemo.R;

import android.os.Bundle;

public class FeedbackActivity  extends DashBoardActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setHeader(getString(R.string.FeedbackActivityTitle), true, false);
        
    }
}
