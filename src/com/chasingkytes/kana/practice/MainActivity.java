package com.chasingkytes.kana.practice;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Menu for Kana Practice game. 
 * @author Amy Lim
 *
 */
public class MainActivity extends Activity implements OnItemSelectedListener, OnClickListener, OnCheckedChangeListener {

	int questionMode, answerMode, rows, columns;
	Spinner sQuestion, sAnswer, sRows, sColumns;
	boolean soundOn, showTimer, showScore;
	CheckBox cbSound, cbTimer, cbScore;
	Button bStart;
	TextView tvChoice;
	int numOfChoices;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//set up spinners and adapters
		sQuestion = (Spinner) findViewById(R.id.sQuestion);
		ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(this, R.array.modeArray, android.R.layout.simple_spinner_item);
		modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sQuestion.setAdapter(modeAdapter);
		questionMode = KanaBubble.HIRAGANA_MODE;
		sQuestion.setSelection(questionMode);
		sQuestion.setOnItemSelectedListener(this);

		sAnswer = (Spinner) findViewById(R.id.sAnswer);
		sAnswer.setAdapter(modeAdapter);
		answerMode = KanaBubble.ROMAJI_MODE;
		sAnswer.setSelection(answerMode);
		sAnswer.setOnItemSelectedListener(this);

		sRows = (Spinner) findViewById(R.id.sRows);
		ArrayAdapter<CharSequence> rowAdapter = ArrayAdapter.createFromResource(this, R.array.rowArray, android.R.layout.simple_spinner_item);
		rowAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sRows.setAdapter(rowAdapter);
		rows = 2;
		sRows.setSelection(rows - 1);
		sRows.setOnItemSelectedListener(this);
		
		sColumns = (Spinner) findViewById(R.id.sColumns);
		ArrayAdapter<CharSequence> colAdapter = ArrayAdapter.createFromResource(this, R.array.columnArray, android.R.layout.simple_spinner_item);
		colAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sColumns.setAdapter(colAdapter);
		columns = 3;
		sColumns.setSelection(columns - 1);
		sColumns.setOnItemSelectedListener(this);
		
		//set up the buttons
		cbSound = (CheckBox) findViewById(R.id.cbSound);
		soundOn = true;
		cbSound.setChecked(soundOn);
		cbSound.setOnCheckedChangeListener(this);
		
		cbTimer = (CheckBox) findViewById(R.id.cbTimer);
		showTimer = true;
		cbTimer.setChecked(showTimer);
		cbTimer.setOnCheckedChangeListener(this);
		
		cbScore = (CheckBox) findViewById(R.id.cbScore);
		showScore = true;
		cbScore.setChecked(showScore);
		cbScore.setOnCheckedChangeListener(this);
		
		//set up start button
		bStart = (Button) findViewById(R.id.bStart);
		bStart.setOnClickListener(this);
		
		//set up the textview with the number of choices
		tvChoice = (TextView) findViewById(R.id.tvChoice);
		setNumOfChoices();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
		switch(parent.getId()) {
		case R.id.sQuestion:
			//set the mode of the question based off of the user's choice
			questionMode = pos;
			break;
		case R.id.sAnswer:
			//set the mode of the answer based off of the user's choice
			answerMode = pos;
			break;
		case R.id.sRows:
			//set the number of rows based off of the user's choice
			rows = pos + 1;
			setNumOfChoices();
			break;
		case R.id.sColumns:
			//set the number of columns based off of the user's choice
			columns = pos + 1;
			setNumOfChoices();
			break;
		}
	}

	public void onNothingSelected(AdapterView<?> parent) {
		
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch(buttonView.getId()) {
		case R.id.cbSound:
			//toggles the sound on/off
			soundOn = isChecked;
			break;
		case R.id.cbTimer:
			//toggles the timer on/off
			showTimer = isChecked;
			break;
		case R.id.cbScore:
			//toggles the score on/off
			showScore = isChecked;
			break;
		}
	}
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.bStart:
			//set up the game with the users options
			Bundle basket = new Bundle();
			basket.putInt("question", questionMode);
			basket.putInt("answer", answerMode);
			basket.putInt("rows", rows);
			basket.putInt("columns", columns);
			basket.putBoolean("sound", soundOn);
			basket.putBoolean("timer", showTimer);
			basket.putBoolean("score", showScore);
			Intent a = new Intent(MainActivity.this, GFXSurface.class);
			a.putExtras(basket);
			startActivity(a);
			break;
		}
	}

	/**
	 * Calculates how many KanaBubble objects will be on the game grid and 
	 * displays it in a TextView for the user to see
	 */
	private void setNumOfChoices() {
		numOfChoices = rows * columns;
		tvChoice.setText("(" + Integer.toString(numOfChoices) + " choices)");
	}
}
