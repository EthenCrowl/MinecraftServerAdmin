package net.crowlhome.app.geoquiz;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.net.InetSocketAddress;

public class QuizActivity extends Activity {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextImageButton;
    private ImageButton mBackImageButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
/**
    private String serverAddress = "162.244.164.131";
    private int queryPort = 30932;
    private int serverPort = 48439;
/**
    private InetSocketAddress queryAddress = new InetSocketAddress(serverAddress, queryPort);
    private InetSocketAddress address = new InetSocketAddress(serverAddress, serverPort);

    public Query query = new Query(queryAddress, address);
**/

    private String[] onlinePlayers;

    private int mCurrentIndex = 0;




    class GetPlayers extends AsyncTask<String, Void, String[]> {

        private Exception exception;


        protected String[] doInBackground(String... urls) {
            String serverAddress = "162.244.164.131";
            int queryPort = 30932;
            int serverPort = 48439;

            InetSocketAddress queryAddress = new InetSocketAddress(serverAddress, queryPort);
            InetSocketAddress address = new InetSocketAddress(serverAddress, serverPort);

            Query query = new Query(queryAddress, address);
            String[] onlinePlayers = new String[1];
            onlinePlayers[0] = "Empty";


            try {
                query.sendQuery();
                onlinePlayers = query.getOnlineUsernames();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (onlinePlayers == null || onlinePlayers.length == 0) {
                onlinePlayers = new String[1];
                onlinePlayers[0] = "Empty";
            }

            return onlinePlayers;
        }

        protected void onPostExecute(String[] result) {
            onlinePlayers = result;

        }


    }





    private void updateQuestion() {
        /**int question = mQuestionBank[mCurrentIndex].getTextResId();**/

        mQuestionTextView.setText(onlinePlayers[mCurrentIndex]);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mTrueButton = (Button) findViewById(R.id.query_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetPlayers().execute(onlinePlayers);
            }
        });

        mFalseButton = (Button) findViewById(R.id.refresh_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuestion();
            }
        });

        mNextImageButton = (ImageButton) findViewById(R.id.next_image_button);
        mNextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % onlinePlayers.length;
                updateQuestion();
            }
        });

        mBackImageButton = (ImageButton) findViewById(R.id.back_image_button);
        mBackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1);
                if (mCurrentIndex == -1) {
                    mCurrentIndex = (onlinePlayers.length - 1);
                }
                updateQuestion();
            }
        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);


        new GetPlayers().execute(onlinePlayers);

        if (onlinePlayers == null || onlinePlayers.length == 0) {
            onlinePlayers = new String[1];
            onlinePlayers[0] = "Empty";
        }

        updateQuestion();
    }
}
