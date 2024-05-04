package com.example.driodquest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "QuestActivity";
    private static final String KEY_INDEX = "index";
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() вызван");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() вызван");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() вызван");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() вызван");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() вызван");
    }
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestonTextView.setText(question);
    }
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue =
                mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsDeceiter) {
            messageResId = R.string.judgment_toast;
        } else {
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }}
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
private Button mTrueButton;
private Button mFalseButton;
private ImageButton mNextButton;
private ImageButton mBackButton;
private Button mDeceitButton;
private TextView mQuestonTextView;
    private Queston[] mQuestionBank = new Queston[] {
            new Queston(R.string.question_android, true),
            new Queston(R.string.question_linear, false),
            new Queston(R.string.question_service, false),
            new Queston(R.string.question_res, true),
            new Queston(R.string.question_manifest, true),
            new Queston(R.string.question_tut, false),
            new Queston(R.string.question_mm, false),
            new Queston(R.string.question_tt, false),
            new Queston(R.string.question_ff, false),
            new Queston(R.string.question_pp, false),
    };
    private int mCurrentIndex = 0;
    private boolean mIsDeceiter;

    private static final int REQUEST_CODE_DECEIT = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Log.d(TAG, "onCreate(Bundle) вызван");
        setContentView(R.layout.activity_main);
        mQuestonTextView = (TextView)findViewById(R.id.question_text_view);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkAnswer(false);

            }
            

        });


        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsDeceiter = false;
                updateQuestion();
            }


        });
        updateQuestion();
        mBackButton = (ImageButton)findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        mDeceitButton = (Button)findViewById(R.id.deceit_button);
        mDeceitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// Запуск DeceitActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex]
                        .isAnswerTrue();
                Intent i = DeceitActivity.newIntent(MainActivity.this,
                        answerIsTrue);
                startActivityForResult(i, REQUEST_CODE_DECEIT);
            }
        });
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        updateQuestion();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_DECEIT) {
            if (data == null) {
                return;
            }mIsDeceiter = DeceitActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

}

