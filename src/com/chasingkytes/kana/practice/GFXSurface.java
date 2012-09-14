package com.chasingkytes.kana.practice;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Handles the interaction between the user and the game. 
 * 
 * User must find the KanaBubble that matches the target that is indicated below the game grid.
 * When the user successfully finds the target, a new game grid will regenerate with a different random target.
 * 
 * @author Amy Lim
 *
 */
public class GFXSurface extends Activity implements OnTouchListener {

	GFXSurfaceView ourSurfaceView;
	Bitmap green_bubble_bitmap;
	Bitmap red_bubble_bitmap;
	Bitmap blue_bubble_bitmap;
	KanaBubbleGrid myBubbleGrid;
	
	SoundPool sp;
	int pop = 0;
	int pop_fail = 0;
	
	long sleepEnd = 0;
	float sideMargin;
	float topMargin;

	int correct = 0;
	int wrong = 0;
	int questionNum = 1;

	int questionMode = 0;
	int answerMode = 2;
	int rows = 1;
	int columns = 4;
	boolean soundOn = true;
	boolean showTimer = true;
	boolean showScore = true;
	long startTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ourSurfaceView = new GFXSurfaceView(this);
		ourSurfaceView.setOnTouchListener(this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(ourSurfaceView);

		//set up the bubble bitmaps
		green_bubble_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bubble_green);
		red_bubble_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bubble_red);
		blue_bubble_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bubble_blue);

		//obtain values from bundle (user preferences from the menu (MainActivity.class))
		Bundle basket = getIntent().getExtras();
		questionMode = basket.getInt("question");
		answerMode = basket.getInt("answer");
		rows = basket.getInt("rows");
		columns = basket.getInt("columns");
		soundOn = basket.getBoolean("sound");
		showTimer = basket.getBoolean("timer");
		showScore = basket.getBoolean("score");
		
		//initialize the bubble grid
		myBubbleGrid = new KanaBubbleGrid(rows, columns);

		//start the timer
		startTime = SystemClock.uptimeMillis();

		//set up the sounds
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		pop = sp.load(this, R.raw.water_droplet, 1);
		pop_fail = sp.load(this, R.raw.ice_block, 1);
	}

	@Override
	protected void onPause() {
		super.onPause();
		ourSurfaceView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ourSurfaceView.resume();
	}

	public boolean onTouch(View v, MotionEvent event) {
		//will not process any touch events that happened while the thread was asleep
		//(prevents the user from queuing up many touch events)
		if(event.getEventTime() > sleepEnd) {
			int sound = myBubbleGrid.processGridTouch(event.getX(), event.getY(), sideMargin, topMargin);
			
			switch(sound) {
			case KanaBubbleGrid.TARGET_HIT:
				//play the appropriate sound if the user gets the answer correct
				if(soundOn && pop != 0)
					sp.play(pop, 1, 1, 0, 0, 1);
				
				//allows the user to see their correct answer for 1 second
				try {
					Thread.sleep(1000);	
					sleepEnd = SystemClock.uptimeMillis();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//new grid is regenerated
				myBubbleGrid = new KanaBubbleGrid(rows, columns);
				
				//increase the question number and the amount of the user's correct answers
				correct++;
				questionNum++;
				break;
				
			case KanaBubbleGrid.TARGET_MISS:
				//when the user hits the wrong bubble, play the appropriate sound and increase their wrong guess count
				if(soundOn && pop_fail != 0)
					sp.play(pop_fail, 1, 1, 0, 0, 1);
				wrong++;
				break;
			case KanaBubbleGrid.TARGET_NONE:
				break;
			}
		}
		return false;
	}

	/**
	 * Displays the graphics of the game.
	 * @author Amy Lim
	 */
	public class GFXSurfaceView extends SurfaceView implements Runnable {

		SurfaceHolder ourHolder;
		Thread ourThread = null;
		boolean isRunning = false;

		public GFXSurfaceView(Context context) {
			super(context);
			ourHolder = getHolder();		//tells us if the surface is valid and locks the canvas so that no one else can draw on it
		}

		public void pause() {
			isRunning = false;
			while(true) {
				try {
					ourThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			ourThread = null;
		}

		public void resume() {
			isRunning = true;
			ourThread = new Thread(this);	//will use the run() defined in this class
			ourThread.start();
		}

		public void run() {
			while(isRunning) {
				if(!ourHolder.getSurface().isValid())
					continue;

				//set up and lock canvas
				Canvas canvas = ourHolder.lockCanvas();

				//draw on the canvas
				canvas.drawColor(Color.BLACK);

				//display the bubble grid
				sideMargin = (canvas.getWidth() - myBubbleGrid.getWidth()) / 2;
				topMargin = (canvas.getHeight() - myBubbleGrid.getHeight()) / 4;
				for(int row = 0; row < myBubbleGrid.getMaxRows(); row++) {
					for(int column = 0; column < myBubbleGrid.getMaxColumns(); column++) {
						KanaBubble tempBubble = myBubbleGrid.getKanaBubble(row, column);

						switch(tempBubble.color) {
						case KanaBubble.GREEN:
							canvas.drawBitmap(green_bubble_bitmap, tempBubble.getX() + sideMargin, tempBubble.getY() + topMargin, null);
							break;
						case KanaBubble.RED:
							canvas.drawBitmap(red_bubble_bitmap, tempBubble.getX() + sideMargin, tempBubble.getY() + topMargin, null);
							break;
						case KanaBubble.BLUE:
							canvas.drawBitmap(blue_bubble_bitmap, tempBubble.getX() + sideMargin, tempBubble.getY() + topMargin, null);
							break;
						}

						Paint textPaint = new Paint();
						textPaint.setColor(Color.BLACK);
						textPaint.setTextAlign(Align.CENTER);
						textPaint.setTextSize(70);
						canvas.drawText(tempBubble.getKana(questionMode), tempBubble.getX()+50 + sideMargin, tempBubble.getY()+75 + topMargin, textPaint);	
					}
				}

				//display the target
				Paint targetPaint = new Paint();
				targetPaint.setColor(Color.WHITE);
				targetPaint.setTextAlign(Align.CENTER);
				targetPaint.setTextSize(70);
				canvas.drawText(myBubbleGrid.getTarget().getKana(answerMode), canvas.getWidth()/2, canvas.getHeight()-100, targetPaint);

				//display the question number
				Paint questionPaint = new Paint();
				questionPaint.setColor(Color.WHITE);
				questionPaint.setTextAlign(Align.CENTER);
				questionPaint.setTextSize(20);
				canvas.drawText("# " + Integer.toString(questionNum), canvas.getWidth()/2, 30, questionPaint);
				
				//display the timer
				if(showTimer) {
					Paint timerPaint = new Paint();
					timerPaint.setColor(Color.WHITE);
					timerPaint.setTextAlign(Align.LEFT);
					timerPaint.setTextSize(20);
					long endTime = SystemClock.uptimeMillis();
					long totalTimerSec = (endTime - startTime) / 1000;
					int timerMin = (int) (totalTimerSec / 60);
					int timerSec = (int) (totalTimerSec % 60);
					String secString = (timerSec < 10) ? "0" + Integer.toString(timerSec) : Integer.toString(timerSec);
					String time = Integer.toString(timerMin) + ":" + secString;
					canvas.drawText(time, 3, 30, timerPaint);
				}

				//display the score: number correct, number wrong, accuracy, and average guess per question
				if(showScore) {
					Paint scorePaint = new Paint();
					scorePaint.setColor(Color.WHITE);
					scorePaint.setTextAlign(Align.CENTER);
					scorePaint.setTextSize(20);
					float sectionSize = canvas.getWidth()/4;
					canvas.drawText("Correct: " + Integer.toString(correct), sectionSize, canvas.getHeight()-55, scorePaint);
					canvas.drawText("Wrong Guesses: " + Integer.toString(wrong), sectionSize*3, canvas.getHeight()-55, scorePaint);
					if(correct != 0 || wrong != 0) {
						//display accuracy
						float accuracy = (float) correct / (correct + wrong) * 100;
						String accString = Float.toString(accuracy);
						int decIndex1 = accString.indexOf(".");
						if(decIndex1 != -1)
							canvas.drawText("Accuracy: " + accString.substring(0, decIndex1) + "%", sectionSize, canvas.getHeight()-25, scorePaint);
						else
							canvas.drawText("Accuracy: " + accString + "%", sectionSize, canvas.getHeight()-25, scorePaint);
						
						//display avg guess per question
						float avg = (float) (correct + wrong) / correct;
						String avgString = Float.toString(avg);
						int decIndex2 = avgString.indexOf(".");
						if(decIndex2 != -1) {
							canvas.drawText("avg guesses", sectionSize*3, canvas.getHeight()-30, scorePaint);
							canvas.drawText("per question: " + avgString.substring(0, decIndex2+2), sectionSize*3 + 10, canvas.getHeight()-10, scorePaint);
						} else {
							canvas.drawText("avg guesses", sectionSize*3, canvas.getHeight()-30, scorePaint);
							canvas.drawText("per question: " + avgString, sectionSize*3 + 10, canvas.getHeight()-10, scorePaint);
						}
					}
					
				}

				//display the canvas
				ourHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
}
