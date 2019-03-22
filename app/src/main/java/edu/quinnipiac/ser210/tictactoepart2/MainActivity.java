package edu.quinnipiac.ser210.tictactoepart2;
/*
    Author: Kevin Sangurima
    Class: SER210
    Instructor: Prof. Ruby Elkharboutly
    Date: 02/14/2019
    Description: This class is for the main activity that lets the player know what the game is about and sends the players name to the board activity
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout f1 = new ConstraintLayout(this);
        f1.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        setContentView(R.layout.activity_main);

        // Links the button and it gives it the ability to start the board activity while sending the players name
        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText playerName = (EditText) findViewById(R.id.playerName);
                String playerNameString = playerName.getText().toString();
                Intent i = new Intent(MainActivity.this, Board.class);
                i.putExtra("name", playerNameString);
                startActivity(i);
            }
        });
    }
}
