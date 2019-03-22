package edu.quinnipiac.ser210.tictactoepart2;
/*
    Author: Kevin Sangurima
    Class: SER210
    Instructor: Prof. Ruby Elkharboutly
    Date: 02/14/2019
    Description: This class is the background that runs the entire game, it makes the buttons clickable and receives information
    from the main activity.

 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Board extends AppCompatActivity implements View.OnClickListener, ITicTacToe {

    private static final int ROWS = 3, COLS = 3;
    private Button[][] board = new Button[ROWS][COLS];
    private int playerWins, computerWins;
    private String playerOneName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        LinearLayout f1 = new LinearLayout(this);
        f1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        setContentView(R.layout.activity_board);
        // links the reset button and gives it the function to reset the game
        Button reset = findViewById(R.id.resetButton);
        reset.setVisibility(View.GONE);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBoard();
                playerWins = 0;
                computerWins = 0;
                updateScore(playerWins, computerWins);
            }
        });
        // Links all the buttons to their respective location in the 2d array
        board[0][0] = findViewById(R.id.button0);
        board[0][1] = findViewById(R.id.button1);
        board[0][2] = findViewById(R.id.button2);
        board[1][0] = findViewById(R.id.button3);
        board[1][1] = findViewById(R.id.button4);
        board[1][2] = findViewById(R.id.button5);
        board[2][0] = findViewById(R.id.button6);
        board[2][1] = findViewById(R.id.button7);
        board[2][2] = findViewById(R.id.button8);

        // Runs this segment if nothing was stored
        if (savedInstanceState == null) {
            updateScore(playerWins, computerWins);
            greetPlayer();
        }
        // Restores the values saved when the activity is created again
        else {
            //Restores the number of wins for each the player and the computer
            playerWins = savedInstanceState.getInt("playerScoreKey");
            computerWins = savedInstanceState.getInt("computerKey");
            updateScore(playerWins, computerWins);
            greetPlayer();
            //Restores the moves of the game
            board[0][0].setText(savedInstanceState.getString("Button0Text"));
            board[0][1].setText(savedInstanceState.getString("Button1Text"));
            board[0][2].setText(savedInstanceState.getString("Button2Text"));
            board[1][0].setText(savedInstanceState.getString("Button3Text"));
            board[1][1].setText(savedInstanceState.getString("Button4Text"));
            board[1][2].setText(savedInstanceState.getString("Button5Text"));
            board[2][0].setText(savedInstanceState.getString("Button6Text"));
            board[2][1].setText(savedInstanceState.getString("Button7Text"));
            board[2][2].setText(savedInstanceState.getString("Button8Text"));

        }
        //Sets all the buttons to get on click listeners, and
        //Sets the color of the player and the computer red or blue and sets their background if there was information restored.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j].setOnClickListener(this);
                //Restores all the players background and color of cross
                if (board[i][j].getText().toString().equals("X")) {
                    board[i][j].setTextColor(Color.parseColor("#3F51B5"));
                    board[i][j].setBackgroundResource(R.drawable.xbackground);
                }
                //Restores all the players background and color of nought
                if (board[i][j].getText().toString().equals("O")) {
                    board[i][j].setTextColor(Color.parseColor("#ff0000"));
                    board[i][j].setBackgroundResource(R.drawable.obackground);
                }
            }
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        //Saves the ints for the wins of both player and computer
        savedInstanceState.putInt("PlayerScoreKey", getPlayerScore());
        savedInstanceState.putInt("ComputerKey", getComputerScore());
        //Saves the moves of the game when the activity is finalized
        savedInstanceState.putString("Button0Text", board[0][0].getText().toString());
        savedInstanceState.putString("Button1Text", board[0][1].getText().toString());
        savedInstanceState.putString("Button2Text", board[0][2].getText().toString());
        savedInstanceState.putString("Button3Text", board[1][0].getText().toString());
        savedInstanceState.putString("Button4Text", board[1][1].getText().toString());
        savedInstanceState.putString("Button5Text", board[1][2].getText().toString());
        savedInstanceState.putString("Button6Text", board[2][0].getText().toString());
        savedInstanceState.putString("Button7Text", board[2][1].getText().toString());
        savedInstanceState.putString("Button8Text", board[2][2].getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onClick(View v) {
        Button buttonClicked = (Button) v;
        //Stops the player to place a move if the space is occupied
        if (!buttonClicked.getText().toString().equals("")) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
            Toast.makeText(this, "Spot occupied, please select another location",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (buttonClicked.getText().toString().equals("")) {
            // Set the text on the button to X
            buttonClicked.setText(CROSS);
            // Set text color
            buttonClicked.setTextColor(Color.parseColor("#3F51B5"));
            buttonClicked.setBackgroundResource(R.drawable.xbackground);
        }
        int currentState = checkForWinner();
        if ((currentState != CROSS_WON) && (currentState != TIE)) {
            setMove(2, getComputerMove());
        }
        //Checks for winner, and ends the game is there is a win or a loss
        if (checkForWinner() != PLAYING) {
            if (checkForWinner() == CROSS_WON) {
                playerWins++;
                updateScore(playerWins, computerWins);
                Toast.makeText(this, "You Won!!!",
                        Toast.LENGTH_LONG).show();
                dialogBox();
                Button reset = findViewById(R.id.resetButton);
                reset.setVisibility(View.VISIBLE);
            } else if (checkForWinner() == NOUGHT_WON) {
                computerWins++;
                updateScore(playerWins, computerWins);
                Toast.makeText(this, "Computer Won!!!",
                        Toast.LENGTH_LONG).show();
                dialogBox();
                Button reset = findViewById(R.id.resetButton);
                reset.setVisibility(View.VISIBLE);
            } else if (checkForWinner() == TIE) {
                Toast.makeText(this, "It's a Tie",
                        Toast.LENGTH_LONG).show();
                dialogBox();
                Button reset = findViewById(R.id.resetButton);
                reset.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * clear board and set current player
     */
    public Board() {


    }

    // This method clears the board, by setting all the buttons background to a white image
    @Override
    public void clearBoard() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col].setText(""); //Clears the board
                board[row][col].setBackgroundResource(R.drawable.b);
            }
        }
    }

    @Override
    public void setMove(int player, int location) {
        //checks if the location is valid, and if not sets the checker to invalid.
        if (!board[location / 3][location % 3].getText().toString().equals("")) {
            return;
        } else {
            // Sets the computers move and places the background image and the color of the computer
            board[location / 3][location % 3].setTextColor(Color.parseColor("#ff0000"));
            board[location / 3][location % 3].setText(NOUGHT);
            board[location / 3][location % 3].setBackgroundResource(R.drawable.obackground);
        }

    }

    @Override
    public int getComputerMove() {

        for (int i = 0; i < 3; i++) {
            // Check for Vertical Block
            if ((board[0][i].getText().toString().equals(CROSS)) && (board[1][i].getText().toString().equals(CROSS)) && (board[2][i].getText().toString().equals(EMPTY)))
                return 6 + i;
            else if ((board[0][i].getText().toString().equals(CROSS)) && (board[1][i].getText().toString().equals(EMPTY)) && (board[2][i].getText().toString().equals(CROSS)))
                return 3 + i;
            else if ((board[0][i].getText().toString().equals(EMPTY)) && (board[1][i].getText().toString().equals(CROSS)) && (board[2][i].getText().toString().equals(CROSS)))
                return i;

                // Check for Horizontal Block
            else if ((board[i][0].getText().toString().equals(CROSS)) && (board[i][1].getText().toString().equals(CROSS)) && (board[i][2].getText().toString().equals(EMPTY)))
                return 2 + (i * 3);
            else if ((board[i][0].getText().toString().equals(CROSS)) && (board[i][1].getText().toString().equals(EMPTY)) && (board[i][2].getText().toString().equals(CROSS)))
                return 1 + (i * 3);
            else if ((board[i][0].getText().toString().equals(EMPTY)) && (board[i][1].getText().toString().equals(CROSS)) && (board[i][2].getText().toString().equals(CROSS)))
                return (i * 3);
        }
        // Check for Diagonal Block
        for (int i = 0; i < 3; i += 2) {
            if ((board[i][0].getText().toString().equals(CROSS)) && (board[1][1].getText().toString().equals(EMPTY)) && (board[2 - i][2].getText().toString().equals(CROSS)))
                return 4;
            else if ((board[i][0].getText().toString().equals(EMPTY)) && (board[1][1].getText().toString().equals(CROSS)) && (board[2 - i][2].getText().toString().equals(CROSS)))
                return (i * 3);
            else if ((board[i][0].getText().toString().equals(CROSS)) && (board[1][1].getText().toString().equals(CROSS)) && (board[2 - i][2].getText().toString().equals(EMPTY)))
                return 8 - (i * 3);
        }
        // If there is nothing to block it takes the middle spot if not already taken
        // Selects the middle because it opens more possibilities for victory
        if (board[1][1].getText().toString().equals(EMPTY)) {
            return 4;
        }
        // Randomizes an Int for the opponents location if there is nothing to block and if the middle is taken
        Random rand = new Random();
        int value = rand.nextInt(9);
        // Checks if the space is empty if not randomizes a new number
        while (!board[value / 3][value % 3].getText().toString().equals(EMPTY)) {
            value = rand.nextInt(9);
        }
        return value;
    }

    @Override
    public int checkForWinner() {

        for (int i = 0; i < 3; i++) {
            // Check for Vertical Win
            if ((board[0][i].getText().toString().equals(CROSS)) && (board[1][i].getText().toString().equals(CROSS)) && (board[2][i].getText().toString().equals(CROSS)))
                return CROSS_WON;
            else if ((board[0][i].getText().toString().equals(NOUGHT)) && (board[1][i].getText().toString().equals(NOUGHT)) && (board[2][i].getText().toString().equals(NOUGHT)))
                return NOUGHT_WON;

                // Check for Horizontal Win
            else if ((board[i][0].getText().toString().equals(CROSS)) && (board[i][1].getText().toString().equals(CROSS)) && (board[i][2].getText().toString().equals(CROSS)))
                return CROSS_WON;
            else if ((board[i][0].getText().toString().equals(NOUGHT)) && (board[i][1].getText().toString().equals(NOUGHT)) && (board[i][2].getText().toString().equals(NOUGHT)))
                return NOUGHT_WON;
        }
        // Check for Diagonal Win
        for (int i = 0; i < 3; i += 2) {
            if ((board[i][0].getText().toString().equals(CROSS)) && (board[1][1].getText().toString().equals(CROSS)) && (board[2 - i][2].getText().toString().equals(CROSS)))
                return CROSS_WON;
            else if ((board[i][0].getText().toString().equals(NOUGHT)) && (board[1][1].getText().toString().equals(NOUGHT)) && (board[2 - i][2].getText().toString().equals(NOUGHT)))
                return NOUGHT_WON;
        }
        // Check for Tie
        if ((!board[0][0].getText().toString().equals(EMPTY)) && (!board[0][1].getText().toString().equals(EMPTY)) && (!board[0][2].getText().toString().equals(EMPTY)) &&
                (!board[1][0].getText().toString().equals(EMPTY)) && (!board[1][1].getText().toString().equals(EMPTY)) && (!board[1][2].getText().toString().equals(EMPTY)) &&
                (!board[2][0].getText().toString().equals(EMPTY)) && (!board[2][1].getText().toString().equals(EMPTY)) && (!board[2][2].getText().toString().equals(EMPTY)))
            return TIE;

        else return PLAYING;
    }

    //This method updates the score every time the player or the computer wins
    public void updateScore(int playerScore, int computerScore) {
        Intent intent = getIntent();
        playerOneName = intent.getExtras().getString("name");
        TextView name = findViewById(R.id.player1);
        TextView opponent = findViewById(R.id.player2);
        name.setText(playerOneName + ": " + playerScore);
        opponent.setText("Computer: " + computerScore);
        Button reset = findViewById(R.id.resetButton);
        reset.setVisibility(View.GONE);
    }

    //This method greets the player and is called whenever the activity starts, It changes the text of the textview
    public void greetPlayer() {
        TextView greeting = findViewById(R.id.greeting);
        greeting.setText("Tic Tac Toe! Welcome " + playerOneName);
    }

    //This shows a dialog box at the end that lets the player play again or quit and go back to the main activity
    public void dialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Game Over");
        builder.setMessage("Would you like to play again?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                clearBoard();
                dialog.dismiss();
                Button reset = findViewById(R.id.resetButton);
                reset.setVisibility(View.GONE);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                clearBoard();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // Getters for the scores
    public int getPlayerScore() {
        return playerWins;
    }

    public int getComputerScore() {
        return computerWins;
    }
}

