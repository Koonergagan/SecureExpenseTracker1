package week11.st566236.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.FirebaseApp

import week11.st566236.finalproject.navigation.AppNavGraph
import week11.st566236.finalproject.ui.theme.SecureExpenseTrackerTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Firebase services
        FirebaseApp.initializeApp(this)

        // Set the Compose UI content
        setContent {
            SecureExpenseTrackerTheme {

                // Launch the navigation graph which handles all app screens
                AppNavGraph()
            }
        }
    }
}
