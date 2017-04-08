package rimp.rild.com.android.android_blocks;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by rild on 2017/04/08.
 */

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    static final long FPS = 30;
    static final long FRAME_TIME = 1000 / FPS;

    private final int SCREEN_ROW = 5;
    private final int SCREEN_COLUMN = 4;
    private final int MAP_ROW = 10;
    private final int MAP_COLUMN = 7;

    private int screenWidth, screenHeight;
    private SurfaceHolder surfaceHolder;
    private Thread thread;

    private Map map;
    private GamePannel[][] screenPannels = new GamePannel[SCREEN_ROW][SCREEN_COLUMN];
    private int screenRow0OnMap;
    private int screenColum0OnMap;

    private Player player;

    private Bitmap fishImg;
    private Bitmap rockImg;
    private Bitmap smallFishImg;

    OnLogsUpdateListener onLogsUpdateListener;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        getHolder().addCallback(this); // add SurfaceHolder.Callback methods callback

        Resources res = getResources();
        fishImg = BitmapFactory.decodeResource(res, R.drawable.fish);
        rockImg = BitmapFactory.decodeResource(res, R.drawable.rock);
        smallFishImg = BitmapFactory.decodeResource(res, R.drawable.small_fish);


        // create pannels
        for (int i = 0; i < SCREEN_ROW; i++) {
            for (int j = 0; j < SCREEN_COLUMN; j++) {
                screenPannels[i][j] = new GamePannel();
            }
        }


        screenRow0OnMap = 5;
        screenColum0OnMap = 0;

        player = new Player(2, 1);
        map = new Map(MAP_ROW, MAP_COLUMN);
        map.createMap();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        screenWidth = width;
        screenHeight = height;
        Log.d("ScreenInfo", "width=" + width + ", h=" + height);
        int w = screenWidth / SCREEN_COLUMN;
        int h = screenHeight / SCREEN_ROW;

        // fill pannel with concrete x, y in screen
        for (int i = 0; i < SCREEN_ROW; i++) {
            for (int j = 0; j < SCREEN_COLUMN; j++) {
                screenPannels[i][j].screenX = j * w;
                screenPannels[i][j].screenY = i * h;
            }
        }

        // resize bitmap
        fishImg = Bitmap.createScaledBitmap(fishImg, w, h, false);
        rockImg = Bitmap.createScaledBitmap(rockImg, w, h, false);
        smallFishImg = Bitmap.createScaledBitmap(smallFishImg, w, h, false);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread = null;
    }

    public void setOnLogsUpdateListener(OnLogsUpdateListener onLogsUpdateListener) {
        this.onLogsUpdateListener = onLogsUpdateListener;
    }

    private void onCollide(int row, int column, GameObjectKind object) {
        switch (object) {
            case ROCK:
                LogMessageManager.add("Player", "Prohibited Area");
                onLogsUpdateListener.onLogsUpdate();
                break;
            case SMALL_FISH:
                map.removeSmallFishAt(row, column);
                LogMessageManager.add("Player", "got small fish");
                onLogsUpdateListener.onLogsUpdate();
        }
    }

    public void movePlayer(Direction dir) {
        int nextWoldRow;
        int nextWoldColumn;

        switch (dir) {
            case Up:
                if (player.row > 0) { // fish can move to
                    nextWoldRow = player.row + screenRow0OnMap - 1;
                    nextWoldColumn = player.column + screenColum0OnMap;

                    if (map.hasRockcAt(nextWoldRow, nextWoldColumn)) {
//                        LogMessageManager.add("Player", "Prohibited Area");
//                        onLogsUpdateListener.onLogsUpdate();
                        onCollide(nextWoldRow, nextWoldColumn, GameObjectKind.ROCK);
                        break;
                    }

                    if (map.hasSmallFishAt(nextWoldRow, nextWoldColumn)) {
//                        map.removeSmallFishAt(nextWoldRow, nextWoldColumn);
//                        LogMessageManager.add("Player", "got small fish");
//                        onLogsUpdateListener.onLogsUpdate();
                        onCollide(nextWoldRow, nextWoldColumn, GameObjectKind.SMALL_FISH);
                    }

                    if (player.row == 1) { // try to go to edge of screen
                        if (screenRow0OnMap == 0) {
                            player.up();
//                            player.row--;
                        } else mapShift(Direction.Up);
                    } else {
//                        player.row--;
                        player.up();
                    }
                }

                break;
            case Left:
                if (player.column > 0) { // fish can move to
                    nextWoldRow = player.row + screenRow0OnMap;
                    nextWoldColumn = player.column + screenColum0OnMap - 1;

                    if (map.hasRockcAt(nextWoldRow, nextWoldColumn)) {
                        onCollide(nextWoldRow, nextWoldColumn, GameObjectKind.ROCK);
                        break;
                    }
                    if (map.hasSmallFishAt(nextWoldRow, nextWoldColumn)) {
                        onCollide(nextWoldRow, nextWoldColumn, GameObjectKind.SMALL_FISH);
                    }

                    if (player.column == 1) { // try to go to edge of screen
                        if (screenColum0OnMap == 0) {
                            //                            player.column--;
                            player.left();
                        } else mapShift(Direction.Left);
                    } else {
//                        player.column--;
                        player.left();
                    }
                }
                break;
            case Right:
                if (player.column < SCREEN_COLUMN - 1) { // fish can move to
                    nextWoldRow = player.row + screenRow0OnMap;
                    nextWoldColumn = player.column + screenColum0OnMap + 1;

                    if (map.hasRockcAt(nextWoldRow, nextWoldColumn)) {
                        onCollide(nextWoldRow, nextWoldColumn, GameObjectKind.ROCK);
                        break;
                    }
                    if (map.hasSmallFishAt(nextWoldRow, nextWoldColumn)) {
                        onCollide(nextWoldRow, nextWoldColumn, GameObjectKind.SMALL_FISH);
                    }

                    if (player.column == SCREEN_COLUMN - 2) { // try to go to edge of screen
                        if (screenColum0OnMap == MAP_COLUMN - SCREEN_COLUMN) {
//                            player.column++;
                            player.right();
                        }
                        else mapShift(Direction.Right);
                    } else {
//                        player.column++;
                        player.right();
                    }
                }
                break;
            case Down:
                if (player.row < SCREEN_ROW - 1) { // fish can move to
                    nextWoldRow = player.row + screenRow0OnMap + 1;
                    nextWoldColumn = player.column + screenColum0OnMap;

                    if (map.hasRockcAt(nextWoldRow, nextWoldColumn)) {
                        onCollide(nextWoldRow, nextWoldColumn, GameObjectKind.ROCK);
                        break;
                    }
                    if (map.hasSmallFishAt(nextWoldRow, nextWoldColumn)) {
                        onCollide(nextWoldRow, nextWoldColumn, GameObjectKind.SMALL_FISH);
                    }

                    if (player.row == SCREEN_ROW - 2) { // try to go to edge of screen
                        if (screenRow0OnMap == MAP_ROW - SCREEN_ROW) {
//                            player.row++;
                            player.down();
                        }
                        else mapShift(Direction.Down);
                    } else {
//                        player.row++;
                        player.down();
                    }
                }
                break;
        }
    }

    private void mapShift(Direction dir) {
        switch (dir) {
            case Up:
                if (screenRow0OnMap > 0)
                    screenRow0OnMap--;
                break;
            case Left:
                if (screenColum0OnMap > 0)
                    screenColum0OnMap--;
                break;
            case Right:
                if (screenColum0OnMap < MAP_COLUMN - SCREEN_COLUMN)
                    screenColum0OnMap++;
                break;
            case Down:
                if (screenRow0OnMap < MAP_ROW - SCREEN_ROW)
                    screenRow0OnMap++;
                break;
        }
    }

    @Override
    public void run() {
        while (thread != null) {

            Canvas canvas = surfaceHolder.lockCanvas();

//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawColor(Color.WHITE);


            for (int i = 0; i < SCREEN_ROW; i++) {
                for (int j = 0; j < SCREEN_COLUMN; j++) {
                    if (map.hasRockcAt(i + screenRow0OnMap, j + screenColum0OnMap))
                        canvas.drawBitmap(rockImg, screenPannels[i][j].screenX, screenPannels[i][j].screenY, null);
                    if (map.hasSmallFishAt(i + screenRow0OnMap, j + screenColum0OnMap))
                        canvas.drawBitmap(smallFishImg, screenPannels[i][j].screenX, screenPannels[i][j].screenY, null);
                }
            }

            canvas.drawBitmap(fishImg,
                    screenPannels[player.row][player.column].screenX,
                    screenPannels[player.row][player.column].screenY,
                    null);


            surfaceHolder.unlockCanvasAndPost(canvas);
            try {
                Thread.sleep(FRAME_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Player {
        int row, column;

        public Player(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public void up() {
            row--;
        }

        public void left() {
            column--;
        }

        public void right() {
            column++;
        }

        public void down() {
            row++;
        }
    }

    class Map {
        GameObjectKind[][] mapObjects;
        int row, column;

        public Map(int row, int column) {
            this.row = row;
            this.column = column;
            mapObjects = new GameObjectKind[row][column];
        }

        public void createMap() {
            mapObjects[1][0] = GameObjectKind.ROCK;
            mapObjects[1][1] = GameObjectKind.ROCK;
            mapObjects[1][2] = GameObjectKind.ROCK;

            mapObjects[3][3] = GameObjectKind.ROCK;
            mapObjects[3][4] = GameObjectKind.ROCK;
            mapObjects[3][5] = GameObjectKind.ROCK;
            mapObjects[3][6] = GameObjectKind.ROCK;

            mapObjects[5][0] = GameObjectKind.ROCK;
            mapObjects[5][1] = GameObjectKind.ROCK;
            mapObjects[6][1] = GameObjectKind.SMALL_FISH;

            mapObjects[7][0] = GameObjectKind.ROCK;
            mapObjects[7][2] = GameObjectKind.ROCK;

            mapObjects[8][3] = GameObjectKind.SMALL_FISH;
        }

        public boolean hasSomethingAt(int row, int column) {
            return mapObjects[row][column] != null;
        }

        public boolean hasSmallFishAt(int row, int column) {
            return mapObjects[row][column] == GameObjectKind.SMALL_FISH;
        }

        public boolean hasRockcAt(int row, int column) {
            return mapObjects[row][column] == GameObjectKind.ROCK;
        }

        public void removeSmallFishAt(int row, int column) {
            mapObjects[row][column] = null;
        }
    }

    class GamePannel {
        int screenX, screenY;

    }

    enum GameObjectKind {
        FISH, ROCK, SMALL_FISH;
    }

    enum Direction {
        Left,
        Up,
        Right,
        Down,
    }

    interface OnLogsUpdateListener {
        void onLogsUpdate();
    }
}
