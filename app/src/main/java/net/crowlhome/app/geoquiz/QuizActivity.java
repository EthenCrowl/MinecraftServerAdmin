package net.crowlhome.app.geoquiz;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetSocketAddress;

public class QuizActivity extends Activity {

    private Button mQueryButton;
    private Button mRefreshButton;
    private ImageButton mNextImageButton;
    private ImageButton mBackImageButton;
    private TextView mQuestionTextView;

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

    private void updatePlayerListText() {
        mQuestionTextView.setText(onlinePlayers[mCurrentIndex]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQueryButton = (Button) findViewById(R.id.query_button);
        mQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetPlayers().execute(onlinePlayers);
            }
        });

        mRefreshButton = (Button) findViewById(R.id.refresh_button);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePlayerListText();
            }
        });

        mNextImageButton = (ImageButton) findViewById(R.id.next_image_button);
        mNextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % onlinePlayers.length;
                updatePlayerListText();
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
                updatePlayerListText();
            }
        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);


        new GetPlayers().execute(onlinePlayers);

        if (onlinePlayers == null || onlinePlayers.length == 0) {
            onlinePlayers = new String[1];
            onlinePlayers[0] = "Empty";
        }

        updatePlayerListText();
    }

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
}
