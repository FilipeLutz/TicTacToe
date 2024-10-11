package com.stu25956.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stu25956.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                TicTacToeGame()
            }
        }
    }
}

@Composable
fun TicTacToeGame() {
    var boardState by remember { mutableStateOf(List(3) { MutableList(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = winner?.let { "Player: $it won!" } ?: "Current Player: $currentPlayer",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TicTacToeBoard(boardState = boardState, onSquareClick = { row, col ->
            if (boardState[row][col].isEmpty() && winner == null) {
                boardState = boardState.toMutableList().apply {
                    this[row] = this[row].toMutableList().apply {
                        this[col] = currentPlayer
                    }
                }
                winner = checkWinner(boardState)
                if (winner == null) {
                    currentPlayer = if (currentPlayer == "X") "O" else "X"
                }
            }
        })

        if (winner != null) {
            Button(
                onClick = {
                    boardState = List(3) { MutableList(3) { "" } }
                    currentPlayer = "X"
                    winner = null
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Play Again")
            }
        }
    }
}

@Composable
fun TicTacToeBoard(boardState: List<List<String>>, onSquareClick: (Int, Int) -> Unit) {
    Column {
        for (i in 0..2) {
            Row {
                for (j in 0..2) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                            .background(Color.Gray)
                            .clickable { onSquareClick(i, j) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = boardState[i][j],
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }
            }
        }
    }
}

fun checkWinner(board: List<List<String>>): String? {
    // Check rows
    for (i in 0..2) {
        if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0].isNotEmpty()) {
            return board[i][0]
        }
    }

    // Check columns
    for (j in 0..2) {
        if (board[0][j] == board[1][j] && board[1][j] == board[2][j] && board[0][j].isNotEmpty()) {
            return board[0][j]
        }
    }

    // Check diagonals
    if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0].isNotEmpty()) {
        return board[0][0]
    }
    if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2].isNotEmpty()) {
        return board[0][2]
    }

    // No winner
    if (board.flatten().all { it.isNotEmpty() }) {
        return "No one"
    }

    return null
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TicTacToeTheme {
        TicTacToeGame()
    }
}