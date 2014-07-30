package com.androidsx.rateme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidsx.libraryrateme.R;

public class DialogRateMe extends DialogFragment {
    private static final String TAG = DialogRateMe.class.getSimpleName();

    private static final String MARKET_CONSTANT = "market://details?id=";
    private static final String GOOGLE_PLAY_CONSTANT = "http://play.google.com/store/apps/details?id=";
    
    //Indentify TitleDivider
    private String RESOURCE_NAME = "titleDivider";
    private String DEFAULT_TYPE_RESOURCE = "id";
    private String DEFAULT_PACKAGE = "android";
    
    private String appPackageName;
    private View mView;
    private View tView;
    private View confirDialogView;
    private Button close;
    private RatingBar ratingBar;
    private LayerDrawable stars;
    private Button rateMe;
    private Button noThanks;
    private Button share;
    private boolean goToMail;
    private String email;
    private boolean showShareButton;
    private int titleColor = Color.WHITE;
    private int titleBackgroundColor = Color.BLACK;
    private int dialogColor = Color.WHITE;
    private int lineDividerColor = Color.GRAY;
    private int textColor = Color.WHITE;
    private int logoResId = R.drawable.icono;
    private int rateButtonBackgroundColor = Color.BLACK;
    private int rateButtonTextColor = Color.WHITE;
    /**
     * Public empty constructor
     */
    public DialogRateMe(Context ctx) {
        appPackageName = ctx.getApplicationContext().getPackageName();
    }
    
    public DialogRateMe setEmail(String email) {
        this.email = email;
        return this;
    }
    
    public DialogRateMe setShowShareButton(boolean showShareButton) {
        this.showShareButton = showShareButton;
        return this;
    }
    
    public DialogRateMe setGoToMail(boolean goToMail) {
        this.goToMail = goToMail;
        return this;
    }
    
    public DialogRateMe setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }
    
    public DialogRateMe setTitleBackgroundColor(int titleBackgroundColor) {
        this.titleBackgroundColor = titleBackgroundColor;
        return this;
    }
    
    public DialogRateMe setDialogColor(int dialogColor) {
        this.dialogColor = dialogColor;
        return this;
    }
    
    public DialogRateMe setLineDividerColor(int lineDividerColor) {
        this.lineDividerColor = lineDividerColor;
        return this;
    }
    
    public DialogRateMe setLogoResourceId(int logoResId) {
        this.logoResId = logoResId;
        return this;
    }
    
    public DialogRateMe setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }
    
    public DialogRateMe setRateButtonBackgroundColor(int rateButtonBackgroundColor) {
        this.rateButtonBackgroundColor = rateButtonBackgroundColor;
        return this;
    }
    
    public DialogRateMe setRateButtonTextColor(int rateButtonTextColor) {
        this.rateButtonTextColor = rateButtonTextColor;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initializeUiFields();
        Log.d(TAG, "initialize correctly all the components");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating >= 4.0) {
                    rateMe.setVisibility(View.VISIBLE);
                    noThanks.setVisibility(View.GONE);
                } else {
                    noThanks.setVisibility(View.VISIBLE);
                    rateMe.setVisibility(View.GONE);
                }
            }
        });
        configureButtons();
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                RateMeDialogTimer.clearSharedPreferences(getActivity());
                Log.d(TAG, "clear preferences");

            }
        });
        share.setVisibility(showShareButton ? View.VISIBLE : View.GONE);
        share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(shareApp(appPackageName));
                Log.d(TAG, "share App");
            }
        });

        return builder.setView(mView).setCustomTitle(tView).setCancelable(false).create();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        final int titleDividerId = getResources().getIdentifier(RESOURCE_NAME,DEFAULT_TYPE_RESOURCE , DEFAULT_PACKAGE);
        final View titleDivider = getDialog().findViewById(titleDividerId);
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(lineDividerColor);
        }
    }

    private void initializeUiFields() {
        //Main Dialog
        mView = getActivity().getLayoutInflater().inflate(R.layout.library, null);
        tView = getActivity().getLayoutInflater().inflate(R.layout.title, null);
        close = (Button) tView.findViewById(R.id.buttonClose);
        share = (Button) tView.findViewById(R.id.buttonShare);
        rateMe = (Button) mView.findViewById(R.id.buttonRateMe);
        noThanks = (Button) mView.findViewById(R.id.buttonThanks);
        ratingBar = (RatingBar) mView.findViewById(R.id.ratingBar);
        stars = (LayerDrawable) ratingBar.getProgressDrawable();
        mView.setBackgroundColor(dialogColor);
        tView.setBackgroundColor(titleBackgroundColor);
        ((TextView) tView.findViewById(R.id.title)).setTextColor(titleColor);
        ((ImageView) mView.findViewById(R.id.picture)).setImageResource(logoResId);
        ((TextView) mView.findViewById(R.id.phraseCenter)).setTextColor(textColor);
        rateMe.setBackgroundColor(rateButtonBackgroundColor);
        rateMe.setTextColor(rateButtonTextColor);
        noThanks.setBackgroundColor(rateButtonBackgroundColor);
        noThanks.setTextColor(rateButtonTextColor);
        
        //Confirmation Dialog
        confirDialogView = getActivity().getLayoutInflater().inflate(R.layout.confirmationtitledialog, null);
        confirDialogView.setBackgroundColor(dialogColor);
        ((TextView) confirDialogView.findViewById(R.id.confirmDialogTitle)).setTextColor(textColor);
        ((ImageView) confirDialogView.findViewById(R.id.iconConfirmDialog)).setImageResource(logoResId);
    }

    private void configureButtons() {
        rateMe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rateApp();
                Log.d(TAG, "go to Google Play Store for Rate-Me");
                RateMeDialogTimer.setOptOut(getActivity(), true);
            }
        });

        noThanks.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goToMail) {
                    confirmGoToMailDialog(getArguments()).show();
                    Log.d(TAG, "got to Mail for explain what is the problem");
                }else{
                    dismiss();
                }
            }
        });
    }

    private void rateApp() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_CONSTANT + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_CONSTANT + appPackageName)));
        }
    }

    private void goToMail() {
        final String subject = getResources().getString(R.string.subject_email);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        try {
            startActivity(Intent.createChooser(intent, ""));
        } catch (android.content.ActivityNotFoundException ex) {
            rateApp();
        }
    }

    private Dialog confirmGoToMailDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCustomTitle(confirDialogView).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                goToMail();
                dismiss();
            }
        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });
        return builder.create();

    }

    private Intent shareApp(String appPackageName) {
        Intent shareApp = new Intent();
        shareApp.setAction(Intent.ACTION_SEND);
        try {
            shareApp.putExtra(Intent.EXTRA_TEXT, MARKET_CONSTANT + appPackageName);
        } catch (android.content.ActivityNotFoundException anfe) {
            shareApp.putExtra(Intent.EXTRA_TEXT, GOOGLE_PLAY_CONSTANT + appPackageName);
        }
        shareApp.setType("text/plain");
        return shareApp;
    }

}
