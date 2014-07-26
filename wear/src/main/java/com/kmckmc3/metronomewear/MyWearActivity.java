package com.kmckmc3.metronomewear;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.view.CardFrame;
import android.support.wearable.view.CardScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.GridPagerAdapter;

import com.kmckmc3.metronomewear.R;

import java.util.ArrayList;
import java.util.List;

public class MyWearActivity extends Activity {

    private static final long vibLength = 50;
    private static final long vibrateBriefly = 50;

    private static Vibrator vib;
    private static boolean vibrating;
    public Intent vibIntent;
    private Button vibrateButton;
    private Button vibrateBpmIncrease;
    private Button vibrateBpmDecrease;
    private TextView mTextView;
    private TextView bpmLabelMain;
    private TextView bpmLabelControl;
    private long bpm;

    private View mainView;
    private View controlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_card_content);
        mTextView = (TextView) findViewById(R.id.text);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        /*
        setContentView(R.layout.rect_activity_my);
        vibrateButton = (Button) findViewById(R.id.vib_button);
        vibrateBpmIncrease = (AutoRepeatButton) findViewById(R.id.button_increase);
        vibrateBpmDecrease = (AutoRepeatButton) findViewById(R.id.button_decrease);
        bpmLabel = (TextView) findViewById(R.id.bpm_label);
        */



        setContentView(R.layout.main_square_dark);
        vibrateButton = (Button) findViewById(R.id.button_onoff_main_dark);
        bpmLabelMain = (TextView) findViewById(R.id.bpm_text_main_dark);

        setContentView(R.layout.speedcontrol_square_dark);
        bpmLabelControl = (TextView) findViewById(R.id.bpm_text_speedcontrol_dark);
        vibrateBpmIncrease = (Button) findViewById(R.id.button_increase_speedcontrol_dark);
        vibrateBpmDecrease = (Button) findViewById(R.id.button_decrease_speedcontrol_dark);


        // CardView
        final TextView tv1 = new TextView(this);
        tv1.setText("asdfefasdf");
        tv1.setTextColor(Color.BLUE);
        final TextView tv2 = new TextView(this);
        tv2.setText("asdfefasdf");
        tv2.setTextColor(Color.GREEN);
        final TextView tv3 = new TextView(this);
        tv3.setText("asdfefasdf");
        tv3.setTextColor(Color.RED);
        final TextView tv4 = new TextView(this);
        tv4.setText("asdfefasdf");
        tv4.setTextColor(Color.YELLOW);

        final CardFrame cf1 = new CardFrame(this);
        cf1.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));
        cf1.addView(tv1);
        final CardFrame cf2 = new CardFrame(this);
        cf2.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));
        cf2.addView(tv2);
        final CardFrame cf3 = new CardFrame(this);
        cf3.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));
        cf3.addView(tv3);
        final CardFrame cf4 = new CardFrame(this);
        cf4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));
        //cf4.addView(tv4);
        cf4.setBackgroundColor(Color.WHITE);

        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout relativeLayout =
                (RelativeLayout) inflater.inflate(R.layout.speedcontrol_square_dark, null, false);
        cf4.addView(relativeLayout);

        final List<List<CardFrame>> textViewList = new ArrayList<List<CardFrame>>();
        List<CardFrame> row1 = new ArrayList<CardFrame>();
        List<CardFrame> row2 = new ArrayList<CardFrame>();
        row1.add(cf1);
        row1.add(cf2);
        row2.add(cf3);
        row2.add(cf4);
        textViewList.add(row1);
        textViewList.add(row2);



        CardScrollView scrollView = new CardScrollView(this);
        CardFrame cardFrame = new CardFrame(this);
        cardFrame.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));


        scrollView.setLayoutParams(new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView.addView(cardFrame);

        GridViewPager gridViewPager = new GridViewPager(this);
        GridPagerAdapter gridPagerAdapter = new GridPagerAdapter() {
            @Override
            public int getRowCount() {
                return 2;
            }

            @Override
            public int getColumnCount(int i) {
                return 2;
            }

            @Override
            protected Object instantiateItem(ViewGroup viewGroup, int i, int i2) {
                CardFrame v = textViewList.get(i).get(i2);
                viewGroup.addView(v);
                return v;
            }

            @Override
            protected void destroyItem(ViewGroup viewGroup, int i, int i2, Object o) {
                viewGroup.removeView(textViewList.get(i).get(i2));
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }
        };
        gridViewPager.setAdapter(gridPagerAdapter);

        // Initialize variables
        vibrating = false;
        bpm = 1;
        bpmLabelMain.setText(bpm + " " + getString(R.string.bpm_text_label));
        bpmLabelControl.setText(bpm + " " + getString(R.string.bpm_text_label));

        // Initialize 'Start Vibrate' button
        vibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("MyWear", "Start Vibrate Button clicked!! -> " + vibrating);
                toggleVibrateButton();
            }
        });

        // Initialize Increase button
        vibrateBpmIncrease.setOnTouchListener(new RepeatListener(1000, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseSpeed();
            }
        }));

        // Initialize Decrease button
        vibrateBpmDecrease.setOnTouchListener(new RepeatListener(1000,50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseSpeed();
            }
        }));

        //setContentView(R.layout.main_square_dark);
        setContentView(gridViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("MyWear", "Starting onStart.");
    }

    public void toggleVibrateButton() {

        if (!vibrating) {
            vibrate(bpm, vibLength);
            vibrateButton.setText(R.string.vibrate_button_label_stop);

        } else {
            stopVibrate();
            vibrateButton.setText(R.string.vibrate_button_label_start);
        }
    }

    public void vibrate(long bpm, long vibLength) {
        Intent vibIntent = new Intent();

        long waitPerBpm = (60000 / bpm) - vibLength;
        long[] pattern = {0, vibLength, waitPerBpm};
        vib.vibrate(pattern, 0);
        vibrating = true;
        //vibrateCheck.setChecked(true);
    }

    public void stopVibrate() {
        vib.cancel();
        vibrating = false;
        //vibrateCheck.setChecked(false);
    }

    public void increaseSpeed() {
        if ((bpm <= 240) && (bpm > 10)) {
            bpm++;
            bpmLabelMain.setText(bpm + " " + getString(R.string.bpm_text_label));
            bpmLabelControl.setText(bpm + " " + getString(R.string.bpm_text_label));
        }
    }

    public void decreaseSpeed() {
        if ((bpm <= 240) && (bpm > 10)) {
            bpm--;
            bpmLabelMain.setText(bpm + " " + getString(R.string.bpm_text_label));
            bpmLabelControl.setText(bpm + " " + getString(R.string.bpm_text_label));
            Log.v("My","");
        }
    }
}
