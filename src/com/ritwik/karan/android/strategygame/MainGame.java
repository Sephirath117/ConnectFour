package com.ritwik.karan.android.strategygame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainGame extends View {
	
	private Paint mPaint = new Paint();
	private Grid mGameGrid = null;
	private boolean hasInitialized = false;
	private boolean mActionPerformed = false;
	private int actionX = 0;
	private int winner = 0;
	private boolean player1sturn = true;
	private int turn = 1;

	public MainGame(Context context) {
		this (context, null);
	}
	
	public MainGame(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public int processColumn(Canvas canvas, int x) {
		
		int eachArea = canvas.getWidth()/mGameGrid.WIDTH;
		return (int) ((double) x /eachArea);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		if (!hasInitialized) {
			mGameGrid = new Grid(Color.RED, Color.YELLOW);
			hasInitialized = true;
		}
		
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.WHITE);		
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mPaint);
		mGameGrid.drawGrid(canvas, mPaint);
		
		if (mActionPerformed) {
			
			int columnZone = processColumn(canvas, actionX);
			
			if(mGameGrid.getBaseArray()[0][columnZone] == 0) {
				player1sturn = !player1sturn;
				if (player1sturn) 
					turn = 1;
				else
					turn = 2;			
			}
			
			mGameGrid.addToken(canvas, mPaint, 0, columnZone, turn);
			mActionPerformed = false;
			
		
		}
		
		 if (checkForWin(1) || leftToRightDiagonalWinCheck(1) || rightToLeftDiagonalWinCheck(1)) {
			 winner = 1;
		 }
		 
		 if (checkForWin(2) || leftToRightDiagonalWinCheck(2) || rightToLeftDiagonalWinCheck(2)) {
			 winner = 2;
		 }

		 if (winner != 0) {
			 mPaint.setColor(Color.WHITE);
			 mPaint.setAlpha(200);
			 canvas.drawRect(0,  0, canvas.getWidth(), canvas.getHeight(), mPaint);	
			 mPaint.setColor(Color.BLUE);
			 mPaint.setTextSize(82);
			 canvas.drawText("Player " + winner + " wins!", canvas.getWidth()/3 - mPaint.getTextSize(), canvas.getHeight()/2, mPaint);
		 }
		
		try {
			Thread.sleep(25);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//recurse
		invalidate();
		}
	
	public boolean checkForWin(int player) {
		
		int[][] checkArray = mGameGrid.getBaseArray();
		
		int countX = 0;
		int countY = 0;
		
		for (int i = 1; i < mGameGrid.HEIGHT; i++) {
			for (int j = 1; j < mGameGrid.WIDTH; j++) {
				
				if (checkArray[i][j] == checkArray[i][j-1] && checkArray[i][j] == player){
					countX++;
					if (countX >= 3 || countY >= 3)
						return true;
				}
				else {
					countX = 0;
				}
			}
		}
		
		for (int j = 1; j < mGameGrid.WIDTH; j++) {
			for (int i = 1; i < mGameGrid.HEIGHT; i++) {
								
				if (checkArray[i][j] == checkArray[i-1][j] && checkArray[i][j] == player) {
					countY++;
					if (countX >= 3 || countY >= 3)
						return true;
				}
				else {
					countY = 0;
				}
			}
		}

		
		for (int zeroCount = 1; zeroCount < mGameGrid.WIDTH; zeroCount++) {
			if (checkArray[0][zeroCount] == checkArray[0][zeroCount-1] && checkArray[0][zeroCount] != 0) {
				countX++;
				if (countX >= 3 || countY >= 3)
					return true;
			}
			else
				countX = 0;
		}
		
		for (int op = 1; op < mGameGrid.HEIGHT; op++) { 
			if (checkArray[op][0] == checkArray[op-1][0] && checkArray[op][0] != 0) {
				countY++;
				if (countX >= 3 || countY >= 3)
					return true;
				}
			else
				countY = 0;
		}
		
		return false;
		
	}
	
	public boolean leftToRightDiagonalWinCheck(int player) {
		
		//specific hard coded
		int[] startRows = {0, 0, 0, 0, 1, 1};
		int[] startColumns = {0, 1, 2, 3, 0, 4};
		
		for (int rowi : startRows) {
			for (int coli : startColumns) {
				
				if (plusOneCheck(rowi, coli, player))
					return true;
			}
		}		
		return false;
	}
	
	public boolean plusOneCheck(int row, int column, int player) {
		
		int count = 0;
		int[][] array2D = mGameGrid.getBaseArray();
		
		while (row < mGameGrid.HEIGHT - 1 && column < mGameGrid.WIDTH - 1) {
			if (array2D[row][column] == array2D[row+1][column+1] && array2D[row][column] == player) 
				count++;
			else
				count = 0;
			
			if (count >= 3)
				return true;
			
			row++;
			column ++;
		}
		
		return false;
	}
	
	public boolean minusOneCheck(int row, int column, int player) {
		
		int count = 0;
		int[][] array2D = mGameGrid.getBaseArray();
		
		while (row < mGameGrid.HEIGHT - 1 && column > 0) {
			if (array2D[row][column] == array2D[row+1][column-1] && array2D[row][column] == player) 
				count++;
			else
				count = 0;
			
			if (count >= 3)
				return true;
			
			row++;
			column--;
		}
		
		return false;
	}
	
	public boolean rightToLeftDiagonalWinCheck(int player) {
		
		//specific hard coded
		int[] startRows = {0, 0, 0, 0, 1, 2};
		int[] startColumns = {6, 5, 4, 3, 6, 6};
		
		for (int rowi : startRows) {
			for (int coli : startColumns) {
				
				if (minusOneCheck(rowi, coli, player))
					return true;
			}
		}		
		return false;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		
		boolean one = true;
		
		if (event.getAction() == MotionEvent.ACTION_DOWN && one) {
			if (winner == 0) {
				mActionPerformed = true;
				actionX = (int)event.getX();
				one = false;
			}
			
		}
		
		return true;
	}
		
	}
	


