package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppTab
import com.example.ui.AyurvedaViewModel
import com.example.ui.components.AyurBottomNav
import com.example.ui.components.AyurMedicineDetailDialog
import com.example.ui.components.AyurTopHeader
import com.example.ui.screens.CatalogueScreen
import com.example.ui.screens.HealthInsightsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PrakritiProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NaturalBackground

class MainActivity : ComponentActivity() {

  private val viewModel: AyurvedaViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        AyurvedaApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun AyurvedaApp(
  viewModel: AyurvedaViewModel,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(uiState.snackbarMessage) {
    uiState.snackbarMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.clearSnackbar()
    }
  }

  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .background(NaturalBackground),
    containerColor = NaturalBackground,
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    topBar = {
      AyurTopHeader(
        userName = "Arjun",
        searchQuery = uiState.searchQuery,
        onSearchQueryChanged = { query ->
          viewModel.onSearchQueryChanged(query)
          if (query.isNotEmpty() && uiState.currentTab != AppTab.LIBRARY) {
            viewModel.setTab(AppTab.LIBRARY)
          }
        }
      )
    },
    bottomBar = {
      AyurBottomNav(
        currentTab = uiState.currentTab,
        onTabSelected = { viewModel.setTab(it) }
      )
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (uiState.currentTab) {
        AppTab.HOME -> HomeScreen(
          uiState = uiState,
          onLogVitalityDose = { viewModel.logDailyVitalityDose() },
          onSelectMedicine = { viewModel.selectMedicine(it) },
          onToggleHabit = { viewModel.toggleHabit(it) },
          onToggleDose = { viewModel.toggleDose(it) },
          onAddHydration = { viewModel.addHydration(it) },
          onNavigateToLibrary = { viewModel.setTab(AppTab.LIBRARY) },
          onNavigateToInsights = { viewModel.setTab(AppTab.INSIGHTS) }
        )

        AppTab.LIBRARY -> CatalogueScreen(
          uiState = uiState,
          onCategorySelected = { viewModel.onCategorySelected(it) },
          onDoshaSelected = { viewModel.onDoshaSelected(it) },
          onSelectMedicine = { viewModel.selectMedicine(it) },
          onAddToRoutine = { viewModel.addMedicineToDailyRoutine(it) }
        )

        AppTab.INSIGHTS -> HealthInsightsScreen(
          uiState = uiState,
          onUpdateDosha = { dosha, value -> viewModel.updateDoshaLevel(dosha, value) },
          onAddHydration = { viewModel.addHydration(it) },
          onToggleHabit = { viewModel.toggleHabit(it) },
          onToggleDose = { viewModel.toggleDose(it) }
        )

        AppTab.PROFILE -> PrakritiProfileScreen(
          uiState = uiState,
          onAnswerQuestion = { qId, dosha -> viewModel.answerPrakriti(qId, dosha) }
        )
      }
    }
  }

  // Detailed Modal Dialog when a medicine is selected
  uiState.selectedMedicine?.let { med ->
    AyurMedicineDetailDialog(
      medicine = med,
      onDismiss = { viewModel.selectMedicine(null) },
      onAddToRoutine = { viewModel.addMedicineToDailyRoutine(it) }
    )
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MyApplicationTheme { Greeting("Android") }
}

