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

    private TextView mTextCollisionCount;
    private TextView mTextFishCount;

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

        mTextCollisionCount = (TextView) findViewById(R.id.main_text_collision_count);
        mTextFishCount = (TextView) findViewById(R.id.main_text_fish_count);

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

                    HeightAnimation hanime = new HeightAnimation(mLogMessageContainer, 0, 150);
                    hanime.setDuration(300);
                    mLogMessageContainer.startAnimation(hanime);

                } else { // icon_array_drop_up
                    mButtonDropUpDwon.setImageResource(R.drawable.icon_array_drop_down);
                    mButtonDropUpDwon.setTag("icon_array_drop_down");

                    HeightAnimation hanime = new HeightAnimation(mLogMessageContainer, 150, 0);
                    hanime.setDuration(300);
                    mLogMessageContainer.startAnimation(hanime);
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
                mTextCollisionCount.setText(String.valueOf(mGameView.collisionCount));
                mTextFishCount.setText(String.valueOf(mGameView.fishCount));
            }
        });
        mButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameView.movePlayer(GameView.Direction.Left);
                mTextCollisionCount.setText(String.valueOf(mGameView.collisionCount));
                mTextFishCount.setText(String.valueOf(mGameView.fishCount));
            }
        });
        mButtonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameView.movePlayer(GameView.Direction.Up);
                mTextCollisionCount.setText(String.valueOf(mGameView.collisionCount));
                mTextFishCount.setText(String.valueOf(mGameView.fishCount));
            }
        });
        mButtonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameView.movePlayer(GameView.Direction.Down);
                mTextCollisionCount.setText(String.valueOf(mGameView.collisionCount));
                mTextFishCount.setText(String.valueOf(mGameView.fishCount));
            }
        });
    }
}
