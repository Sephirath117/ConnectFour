package com.ritwik.karan.android.strategygame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;


public class Grid {
	
	public final int WIDTH = 7;
	public final int HEIGHT = 6;
	public static final String TAG = "Grid";
	
	private int[][] baseArray = new int[HEIGHT][WIDTH];
	private int player1Color;
	private int player2Color;
	
	public Grid(int mColor1, int mColor2) {
		player1Color = mColor1; 
		player2Color = mColor2;
		setBaseArray();
	}
	
	public void setBaseArray() {
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				baseArray[i][j] = 0;
			}
		}
	}
	
	//Render, depends on 2D Array
	public void drawGrid(Canvas canvas, Paint paint) {
		
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				getRektDrawRekt(canvas, paint, i,j);
			}
		}
		
	}
	
	//this is not called from the MainGame
	public void getRektDrawRekt(Canvas canvas, Paint paint, int i, int j) {
		
		int player = baseArray[i][j];
		int x = (int)((double)j * canvas.getWidth()/(WIDTH) + 0.5);
		int y = (int)((double)i * canvas.getHeight()/(HEIGHT) + 0.5);
		int x2width = (int)((double)(j+1) * canvas.getWidth()/(WIDTH) + 0.5);
		int y2width = (int)((double)(i+1) * canvas.getHeight()/(HEIGHT) + 0.5);

		
		if (player == 1) {
			paint.setColor(player1Color);
		} else if (player == 2) {
			paint.setColor(player2Color);
		} else {
			paint.setColor(Color.GRAY);
		}		
		
		Rect token = new Rect(x+2, y+2, x2width, y2width);
		canvas.drawRect(token, paint);
		
	}
	
	//row should be 0 the first time
	public void addToken(Canvas canvas, Paint paint, int Row, int Column, int playerNumber) {
		
		if (baseArray[0][Column] != 0)
			return;
				
		if (Row == HEIGHT - 2 && baseArray[Row+1][Column] == 0) { 
			baseArray[Row+1][Column] = playerNumber;
			animateDrop(canvas, paint, 0, Column, Row+1, playerNumber);
			return;
			
		} else if (baseArray[Row+1][Column] != 0){
			baseArray[Row][Column] = playerNumber;
			animateDrop(canvas, paint, 0, Column, Row, playerNumber);
			return;
			
		} else {
			baseArray[Row][Column] = 0;		
			addToken(canvas, paint, Row+1, Column, playerNumber);
		}
	}
	
	public int[][] getBaseArray() {
		return baseArray;
	}

	public void setBaseArray(int[][] baseArray) {
		this.baseArray = baseArray;
	}

	public void animateDrop(Canvas canvas, Paint paint, int RowOne, int ColumnOne, int finalRow, int playerNum) {
		
		Log.i(TAG, "animateCalled");
		
		int x = (int)((double)ColumnOne * canvas.getWidth()/(WIDTH) + 0.5);
		int y = (int)((double)RowOne * canvas.getHeight()/(HEIGHT) + 0.5);
		int xw = (int)((double)(ColumnOne+1) * canvas.getWidth()/(WIDTH) + 0.5);
		int yh = (int)((double)(RowOne+1) * canvas.getHeight()/(HEIGHT) + 0.5);
		
		int Finaly = (int)((double)finalRow * canvas.getHeight()/HEIGHT + 0.5);
		
		//Animation Loop
		int distanceToFinal = Finaly - y;
		
		while (distanceToFinal > 0) { 
			
			Rect rekt = new Rect(x+2, y+2, xw, yh);			
			if (playerNum == 1)
				paint.setColor(player1Color);
			else 
				paint.setColor(player2Color);	
			
			canvas.drawRect(rekt, paint);
			
			int C1 = 15;
			y += C1;
			yh += C1;
			distanceToFinal = Finaly-y;

		}
		
	} 

	public int getPlayer1Color() {
		return player1Color;
	}

	public void setPlayer1Color(int player1Color) {
		this.player1Color = player1Color;
	}

	public int getPlayer2Color() {
		return player2Color;
	}

	public void setPlayer2Color(int player2Color) {
		this.player2Color = player2Color;
	}
	
	
}
