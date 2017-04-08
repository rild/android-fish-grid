package rimp.rild.com.android.android_blocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private GameView mGameView;
    private Button mButtonUp;
    private Button mButtonLeft;
    private Button mButtonRight;
    private Button mButtonDown;

    private TextView mTextLogs;
    private ImageView mButtonDelete;
    private LinearLayout mLogMessageContainer;
    private ImageView mButtonDropUpDwon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGameView = (GameView) findViewById(R.id.main_game);

        mButtonRight = (Button) findViewById(R.id.button_right);
        mButtonLeft = (Button) findViewById(R.id.button_left);
        mButtonUp = (Button) findViewById(R.id.button_up);
        mButtonDown = (Button) findViewById(R.id.button_down);
        mTextLogs = (TextView) findViewById(R.id.main_massages);
        mButtonDelete = (ImageView) findViewById(R.id.button_delete);
        mLogMessageContainer = (LinearLayout) findViewById(R.id.main_log_message_container);
        mButtonDropUpDwon = (ImageView) findViewById(R.id.button_drop_up_and_down);

        ViewGroup.LayoutParams params = mLogMessageContainer.getLayoutParams();
        params.height = 0;
        mLogMessageContainer.setLayoutParams(params);

        mButtonDropUpDwon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = mButtonDropUpDwon.getTag().toString();
                if (tag.equals("icon_array_drop_down")) {
                    mButtonDropUpDwon.setImageResource(R.drawable.icon_array_drop_up);
                    mButtonDropUpDwon.setTag("icon_array_drop_up");

//                    ViewGroup.LayoutParams params = mLogMessageContainer.getLayoutParams();
//                    params.height = (int) getResources().getDimension(R.dimen.main_log_message_container_opened); // opened
//                    mLogMessageContainer.setLayoutParams(params);

//                    ResizeAnimation resizeAnimation = new ResizeAnimation(
//                            mLogMessageContainer,
//                            (int) getResources().getDimension(R.dimen.main_log_message_container_opened),
//                            mLogMessageContainer.getLayoutParams().height
//                    );
//                    resizeAnimation.setDuration(300);
//                    mLogMessageContainer.startAnimation(resizeAnimation);

                    ResizeAnimation.expand(mLogMessageContainer);

                } else { // icon_array_drop_up
                    mButtonDropUpDwon.setImageResource(R.drawable.icon_array_drop_down);
                    mButtonDropUpDwon.setTag("icon_array_drop_down");

//                    ViewGroup.LayoutParams params = mLogMessageContainer.getLayoutParams();
//                    params.height = (int) getResources().getDimension(R.dimen.main_log_message_container_closed); // closed
//                    mLogMessageContainer.setLayoutParams(params);

//                    ResizeAnimation resizeAnimation = new ResizeAnimation(
//                            mLogMessageContainer,
//                            0,
//                            (int) getResources().getDimension(R.dimen.main_log_message_container_opened)
//                    );
//                    resizeAnimation.setDuration(300);
//                    mLogMessageContainer.startAnimation(resizeAnimation);

                    ResizeAnimation.collapse(mLogMessageContainer);
                }
            }
        });
        mTextLogs.setText(LogMessageManager.getAllLogs());
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogMessageManager.clear();
                mTextLogs.setText(LogMessageManager.getAllLogs());
            }
        });
        mGameView.setOnLogsUpdateListener(new GameView.OnLogsUpdateListener() {
            @Override
            public void onLogsUpdate() {
                mTextLogs.setText(LogMessageManager.getAllLogs());
            }
        });

        mButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameView.movePlayer(GameView.Direction.Right);
            }
        });
        mButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameView.movePlayer(GameView.Direction.Left);
            }
        });
        mButtonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameView.movePlayer(GameView.Direction.Up);
            }
        });
        mButtonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameView.movePlayer(GameView.Direction.Down);
            }
        });
    }
}
