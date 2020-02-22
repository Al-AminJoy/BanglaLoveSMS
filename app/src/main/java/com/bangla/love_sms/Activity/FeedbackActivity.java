package com.bangla.love_sms.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangla.love_sms.R;

public class FeedbackActivity extends AppCompatActivity {

    private EditText feedbackTxt;
    private Button sendFeedbackBn;

    private TextView tvCatagoryName;
    private ImageView ivToolbarBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //Variable Initiation
        feedbackTxt = findViewById(R.id.feedback_edtxt_id);
        sendFeedbackBn = findViewById(R.id.send_feedback_bn_id);
        tvCatagoryName = findViewById(R.id.tvToolbarCatagoryTitleId);
        ivToolbarBack = findViewById(R.id.ivToolbarOtherBackId);

        //Send Feedback : Button Handler : Toolbar
        toolBar();
        sendFeedbackBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }

    /**
     * ToolBar Handler
     */
    private void toolBar(){
        tvCatagoryName.setText(getResources().getString(R.string.feedback_title));
        ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Feed Method : Send Using Email
     */
    private void send() {
        String receiver="kamrul.jaman26@gmail.com";
        String[] recevername=receiver.split(",");
        String subject="Bangla Love SMS";
        String message=feedbackTxt.getText().toString();
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,recevername);
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose App For Send"));
    }
}
