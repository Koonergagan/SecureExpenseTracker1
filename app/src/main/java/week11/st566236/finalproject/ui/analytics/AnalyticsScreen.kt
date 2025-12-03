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
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        when (val resource = uiState.listResource) {
            is Resource.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${resource.message}")
                }
            }
            is Resource.Success -> {
                val expenses = resource.data
                if (expenses.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No expenses to show")
                    }
                } else {
                    // Pie chart for categories
                    PieChartView(expenses)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bar chart for last 7 days
                    WeeklyBarChart(expenses)
                }
            }
        }
    }
}

@Composable
fun PieChartView(expenses: List<Expense>) {
    val pieEntries: List<PieEntry> = remember(expenses) {
        val grouped = expenses.groupBy { it.category }
        grouped.map { (category, list) ->
            PieEntry(list.sumOf { it.amount }.toFloat(), category)
        }
    }

    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false
                isRotationEnabled = true
                legend.isEnabled = true
            }
        },
        update = { pieChart ->
            val dataSet = PieDataSet(pieEntries, "").apply {
                colors = listOf(
                    Color.rgb(244, 67, 54),
                    Color.rgb(33, 150, 243),
                    Color.rgb(76, 175, 80),
                    Color.rgb(255, 193, 7),
                    Color.rgb(156, 39, 176)
                )
                valueTextSize = 14f
            }
            pieChart.data = PieData(dataSet)
            pieChart.invalidate()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    )
}

@Composable
fun WeeklyBarChart(expenses: List<Expense>) {
    val context = LocalContext.current

    // Calculate total spent per day for the last 7 days
    val calendar = Calendar.getInstance()
    val today = calendar.timeInMillis
    val sevenDaysAgo = today - 6 * 24 * 60 * 60 * 1000L

    val dailyTotals = (0..6).map { dayOffset ->
        val dayStart = sevenDaysAgo + dayOffset * 24 * 60 * 60 * 1000L
        val dayEnd = dayStart + 24 * 60 * 60 * 1000L
        expenses.filter { it.date in dayStart until dayEnd }.sumOf { it.amount }.toFloat()
    }

    val daysLabels = (0..6).map { dayOffset ->
        calendar.timeInMillis = sevenDaysAgo + dayOffset * 24 * 60 * 60 * 1000L
        SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time) // Mon, Tue, etc.
    }

    val entries = dailyTotals.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }

    AndroidView(
        factory = { ctx ->
            BarChart(ctx).apply {
                description.isEnabled = false
                setDrawGridBackground(false)
                setFitBars(true)
                setDrawValueAboveBar(true)

                axisRight.isEnabled = false
                axisLeft.setDrawGridLines(false)
                axisLeft.axisLineColor = Color.BLACK
                axisLeft.textColor = Color.BLACK
                axisLeft.textSize = 12f

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    valueFormatter = IndexAxisValueFormatter(daysLabels)
                    granularity = 1f
                    textColor = Color.BLACK
                    textSize = 12f
                }

                legend.isEnabled = false
                animateY(800)
            }
        },
        update = { chart ->
            val dataSet = BarDataSet(entries, "").apply {
                color = Color.rgb(33, 150, 243) // modern accent color
                valueTextSize = 12f
                valueTextColor = Color.BLACK
            }
            chart.data = BarData(dataSet).apply { barWidth = 0.5f }
            chart.invalidate()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    )
}