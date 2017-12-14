package com.rent.steward.general.gui;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.rent.steward.R;

/**
 * Created by Corth1545617 on 2017/5/9.
 */

public class CustomDialog extends Dialog implements View.OnClickListener {

public interface CustomDialogClickEvent {
    void onCDClickOK();

    void onCDClickCancel();
}


    private boolean hasCancel = true;
    private boolean isDismiss = true;
    private String title, okStr = null, cancelStr = null;
    private Button But_dialog_ok, But_dialog_cancel;
    private TextView TV_dialog_title;
    private RelativeLayout RL_dialog_layout = null;
    private Space Space_dialog_button;

    private CustomDialogClickEvent mCustomDialogClickEvent;


    /**
     * Message
     */
    private TextView TV_message;

    /**
     * Custom View
     */
    private View contentView;
    /**
     * DataPicker
     */

    private DatePicker mDatePicker = null;

    /**
     * Create a Dialog window that uses the custom dialog .
     *
     * @param context   for set context
     * @param title     text of title
     * @param message   text of message
     * @param hasCancel is this dialog including cancel button
     */
    public CustomDialog(Context context, String title, String message, boolean hasCancel) {
        super(context);
        this.title = title;
        this.hasCancel = hasCancel;

        TV_message = new TextView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        TV_message.setLayoutParams(params);
        TV_message.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        TV_message.setGravity(Gravity.CENTER);
        TV_message.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        TV_message.setText(message);
    }


    /**
     * Create a Dialog window and set the ok & cancel string
     *
     * @param context
     * @param title
     * @param message
     * @param hasCancel
     * @param okStr
     * @param cancelStr
     */
    public CustomDialog(Context context, @NonNull String title, String message, boolean hasCancel,
                        @NonNull String okStr, @NonNull String cancelStr) {
        this(context, title, message, hasCancel);
        this.okStr = okStr;
        this.cancelStr = cancelStr;
    }

    /**
     * Set your custom view into this dialog.
     *
     * @param context
     * @param title
     * @param contentView
     * @param hasCancel
     * @param okStr
     * @param cancelStr
     */
    public CustomDialog(Context context, @NonNull String title, View contentView, boolean hasCancel,
                        @NonNull String okStr, @NonNull String cancelStr) {
        super(context);
        this.title = title;
        this.contentView = contentView;
        this.hasCancel = hasCancel;
        this.okStr = okStr;
        this.cancelStr = cancelStr;
    }

    /**
     * Set your custom view into this dialog.
     *
     * @param context
     * @param title
     * @param contentView
     * @param hasCancel
     * @param okStr
     * @param cancelStr
     * @param isDismiss
     */
    public CustomDialog(Context context, String title, View contentView, boolean hasCancel,
                        @NonNull String okStr, @NonNull String cancelStr, boolean isDismiss) {
        this(context, title, contentView, hasCancel, okStr, cancelStr);
        this.isDismiss = isDismiss;
    }

    /**
     * Using datepicker in this dialog .
     * If you will set current time you can send null in  year or monthOfYear or dayOfMonth ,
     * and you can set the OnDateChangedListener to get the date in datepicker.
     *
     * @param context
     * @param title
     * @param year        your year or null to get current time.
     * @param monthOfYear your monthOfYear or null to get current time.
     * @param dayOfMonth  your dayOfMonth or null to get current time.
     * @param listener    your datepicker callback or null to do nothing.
     */
    public CustomDialog(Context context, String title, @NonNull Integer year, @NonNull Integer monthOfYear,
                        @NonNull Integer dayOfMonth, @Nullable DatePicker.OnDateChangedListener listener) {
        super(context);
        this.title = title;
        this.cancelStr = context.getString(R.string.custom_dialog_clear);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LayoutInflater inflater = LayoutInflater.from(context);
            mDatePicker = (DatePicker) inflater.inflate(R.layout.custom_datepicker, null, false);
        } else {
            mDatePicker = new DatePicker(context);
            mDatePicker.setCalendarViewShown(false);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mDatePicker.setLayoutParams(params);
        mDatePicker.init(year, monthOfYear, dayOfMonth, listener);
    }

    /**
     * Use the default title.
     *
     * @param context
     * @param year        your year or null to get current time.
     * @param monthOfYear your monthOfYear or null to get current time.
     * @param dayOfMonth  your dayOfMonth or null to get current time.
     * @param listener    your datepicker callback or null to do nothing.
     */
    public CustomDialog(Context context, @NonNull Integer year, @NonNull Integer monthOfYear,
                        @NonNull Integer dayOfMonth, @Nullable DatePicker.OnDateChangedListener listener) {
        this(context, context.getResources().getString(R.string.date), year, monthOfYear, dayOfMonth, listener);
    }


    public void setCustomDialogClickEvent(CustomDialogClickEvent l) {
        mCustomDialogClickEvent = l;
    }

    /**
     * Because this dialog is no title , so you can't use weight to set view,If you will use weight ,
     * you will get screen & compute its size!
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        But_dialog_ok = (Button) findViewById(R.id.But_dialog_ok);
        But_dialog_ok.setOnClickListener(this);
        TV_dialog_title = (TextView) findViewById(R.id.TV_dialog_title);
        But_dialog_cancel = (Button) findViewById(R.id.But_dialog_cancel);
        Space_dialog_button = (Space) findViewById(R.id.Space_dialog_button);
        RL_dialog_layout = (RelativeLayout) findViewById(R.id.RL_dialog_layout);


        if (title != null) {
            TV_dialog_title.setText(title);
        }

        if (TV_message != null) {
            RL_dialog_layout.addView(TV_message);
        }

        if (contentView != null) {
            RL_dialog_layout.addView(contentView);
        }

        if (mDatePicker != null) {
            RL_dialog_layout.addView(mDatePicker);
        }

        if (okStr != null) {
            But_dialog_ok.setText(okStr);
        }

        if (hasCancel) {
            if (cancelStr != null) {
                But_dialog_cancel.setText(cancelStr);
            }
            But_dialog_cancel.setOnClickListener(this);
        } else {
            But_dialog_cancel.setVisibility(View.GONE);
            Space_dialog_button.setVisibility(View.GONE);
            But_dialog_ok.setGravity(Gravity.CENTER);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) But_dialog_ok.getLayoutParams();
            layoutParams.addRule(RelativeLayout.RIGHT_OF, 0);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            But_dialog_ok.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.But_dialog_ok) {
            if (mCustomDialogClickEvent != null) {
                mCustomDialogClickEvent.onCDClickOK();
            }
        } else if (v.getId() == R.id.But_dialog_cancel) {
            if (mCustomDialogClickEvent != null) {
                mCustomDialogClickEvent.onCDClickCancel();
            }
        }
        if (isDismiss) {
            dismiss();
        }
    }

}
