package week11.st566236.finalproject.ui.analytics

import android.graphics.Color
import android.util.Log.i
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import week11.st566236.finalproject.data.model.Expense
import week11.st566236.finalproject.data.util.Resource

import week11.st566236.finalproject.viewModel.ExpensesViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AnalyticsScreen(viewModel: ExpensesViewModel = viewModel()) {

    // Collect UI state from ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Render screen based on state of expense list
        when (val resource = uiState.listResource) {

            // Show loading indicator while fetching expenses
            is Resource.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            // Show error message if fetch failed
            is Resource.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${resource.message}", color = androidx.compose.ui.graphics.Color.Red)
                }
            }

            // When expenses successfully loaded
            is Resource.Success -> {
                val expenses = resource.data
                if (expenses.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No expenses to show", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    // Pie Chart
                    androidx.compose.material3.Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        elevation = androidx.compose.material3.CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Spending by Category",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            PieChartView(expenses)
                        }
                    }

                    // Weekly Bar Chart
                    androidx.compose.material3.Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        elevation = androidx.compose.material3.CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Last 7 Days Spending",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            WeeklyBarChart(expenses)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PieChartView(expenses: List<Expense>) {

    // Convert expense list into PieEntry list for MPAndroidChart
    val pieEntries: List<PieEntry> = remember(expenses) {
        val grouped = expenses.groupBy { it.category }
        grouped.map { (category, list) ->
            PieEntry(list.sumOf { it.amount }.toFloat(), category)
        }
    }

    // Use AndroidView to embed MPAndroidChart inside Compose
    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false
                isRotationEnabled = true
                setUsePercentValues(true)
                legend.isEnabled = true
            }
        },
        update = { pieChart ->
            val dataSet = PieDataSet(pieEntries, "").apply {
                // Custom chart slice colours
                colors = listOf(
                    android.graphics.Color.rgb(244, 67, 54),
                    android.graphics.Color.rgb(33, 150, 243),
                    android.graphics.Color.rgb(76, 175, 80),
                    android.graphics.Color.rgb(255, 193, 7),
                    android.graphics.Color.rgb(156, 39, 176)
                )
                valueTextSize = 14f
            }

            // Assign new data and refresh
            pieChart.data = PieData(dataSet)
            pieChart.invalidate()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}

@Composable
fun WeeklyBarChart(expenses: List<Expense>) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    // Define 7-day window
    val today = calendar.timeInMillis
    val sevenDaysAgo = today - 6 * 24 * 60 * 60 * 1000L

    // Calculate daily totals for each of last 7 days
    val dailyTotals = (0..6).map { dayOffset ->
        val dayStart = sevenDaysAgo + dayOffset * 24 * 60 * 60 * 1000L
        val dayEnd = dayStart + 24 * 60 * 60 * 1000L
        // Sum amounts for this day
        expenses.filter { it.date in dayStart until dayEnd }.sumOf { it.amount }.toFloat()
    }

    // Labels for X-axis
    val daysLabels = (0..6).map { dayOffset ->
        calendar.timeInMillis = sevenDaysAgo + dayOffset * 24 * 60 * 60 * 1000L
        SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
    }

    // Convert totals to MPAndroidChart entries
    val entries = dailyTotals.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }

    AndroidView(
        factory = { ctx ->
            BarChart(ctx).apply {

                // Base chart configuration
                description.isEnabled = false
                setDrawGridBackground(false)
                setFitBars(true)
                setDrawValueAboveBar(true)

                // Y-axis
                axisRight.isEnabled = false
                axisLeft.setDrawGridLines(false)
                axisLeft.axisLineColor = android.graphics.Color.BLACK
                axisLeft.textColor = android.graphics.Color.BLACK
                axisLeft.textSize = 12f

                // X-axis
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    valueFormatter = IndexAxisValueFormatter(daysLabels)
                    granularity = 1f
                    textColor = android.graphics.Color.BLACK
                    textSize = 12f
                }

                legend.isEnabled = false
                animateY(800)
            }
        },
        update = { chart ->

            // Build bar dataset
            val dataSet = BarDataSet(entries, "").apply {
                color = android.graphics.Color.rgb(33, 150, 243)
                valueTextSize = 12f
                valueTextColor = android.graphics.Color.BLACK
            }
            // Apply dataset to bar chart
            chart.data = BarData(dataSet).apply { barWidth = 0.5f }
            chart.invalidate()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}
