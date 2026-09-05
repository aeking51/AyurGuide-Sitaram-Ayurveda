package com.example

import com.example.data.model.AppUser
import com.example.data.model.DoshaType
import com.example.data.model.UserRole
import com.example.data.model.UserStatus
import com.example.ui.AyurvedaViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testDefaultUsersExistWithDistinctRoles() {
    val vm = AyurvedaViewModel()
    val state = vm.uiState.value

    assertTrue(state.allUsers.isNotEmpty())
    val roles = state.allUsers.map { it.role }.toSet()
    assertTrue(roles.contains(UserRole.ADMIN))
    assertTrue(roles.contains(UserRole.PRACTITIONER))
    assertTrue(roles.contains(UserRole.PATIENT))
  }

  @Test
  fun testSwitchUserChangesActiveRole() {
    val vm = AyurvedaViewModel()
    val practitioner = vm.uiState.value.allUsers.first { it.role == UserRole.PRACTITIONER }

    vm.switchUser(practitioner)

    assertEquals(practitioner.id, vm.uiState.value.currentUser.id)
    assertEquals(UserRole.PRACTITIONER, vm.uiState.value.currentUser.role)
    assertTrue(vm.uiState.value.auditLogs.any { it.actionType == "USER_SWITCH" })
  }

  @Test
  fun testUpdateStockGeneratesAuditLog() {
    val vm = AyurvedaViewModel()
    val med = vm.uiState.value.allMedicines.first()
    val initialStock = med.stockUnits

    vm.updateMedicineStock(med.id, initialStock + 20)

    val updatedMed = vm.uiState.value.allMedicines.first { it.id == med.id }
    assertEquals(initialStock + 20, updatedMed.stockUnits)
    assertTrue(vm.uiState.value.auditLogs.any { it.actionType == "STOCK_UPDATE" })
  }

  @Test
  fun testUpdateUserRole() {
    val vm = AyurvedaViewModel()
    val patient = vm.uiState.value.allUsers.first { it.role == UserRole.PATIENT }

    vm.updateUserRole(patient.id, UserRole.PRACTITIONER)

    val updated = vm.uiState.value.allUsers.first { it.id == patient.id }
    assertEquals(UserRole.PRACTITIONER, updated.role)
    assertTrue(vm.uiState.value.auditLogs.any { it.actionType == "ROLE_CHANGE" || it.actionType == "ROLE_UPDATE" })
  }

  @Test
  fun testLoginWithValidCredentials() {
    val vm = AyurvedaViewModel()
    val patient = vm.uiState.value.allUsers.first { it.role == UserRole.PATIENT }

    val success = vm.login(patient.email, patient.password)
    assertTrue(success)
    assertTrue(vm.uiState.value.isAuthenticated)
    assertEquals(patient.id, vm.uiState.value.currentUser.id)
    assertTrue(vm.uiState.value.auditLogs.any { it.actionType == "LOGIN_SUCCESS" })
  }

  @Test
  fun testLoginWithInvalidPasswordFails() {
    val vm = AyurvedaViewModel()
    val patient = vm.uiState.value.allUsers.first { it.role == UserRole.PATIENT }

    val success = vm.login(patient.email, "wrong_password_xyz")
    org.junit.Assert.assertFalse(success)
    assertNotNull(vm.uiState.value.authErrorMessage)
  }

  @Test
  fun testSignupCreatesNewUserAndAuthenticates() {
    val vm = AyurvedaViewModel()
    val initialUserCount = vm.uiState.value.allUsers.size

    val success = vm.signup(
      name = "Devika Sharma",
      email = "devika.s@ayurveda.in",
      pass = "healthy123",
      role = UserRole.PATIENT,
      prakriti = DoshaType.VATA,
      designation = "Yoga Practitioner"
    )

    assertTrue(success)
    assertTrue(vm.uiState.value.isAuthenticated)
    assertEquals(initialUserCount + 1, vm.uiState.value.allUsers.size)
    assertEquals("Devika Sharma", vm.uiState.value.currentUser.name)
    assertEquals(DoshaType.VATA, vm.uiState.value.currentUser.prakriti)
    assertTrue(vm.uiState.value.auditLogs.any { it.actionType == "USER_REGISTERED" })
  }

  @Test
  fun testForgotPasswordOtpGenerationAndReset() {
    val vm = AyurvedaViewModel()
    val user = vm.uiState.value.allUsers.first()

    val requestSuccess = vm.requestPasswordReset(user.email)
    assertTrue(requestSuccess)
    assertEquals(com.example.ui.AuthMode.OTP_RESET, vm.uiState.value.authMode)
    val otp = vm.uiState.value.generatedOtpCode
    assertNotNull(otp)
    assertEquals(6, otp!!.length)

    // Complete reset with valid OTP
    val resetSuccess = vm.completePasswordReset(user.email, otp, "brandNewPass789")
    assertTrue(resetSuccess)
    assertEquals(com.example.ui.AuthMode.LOGIN, vm.uiState.value.authMode)

    // Verify user can now log in with the new password
    val loginWithNewPass = vm.login(user.email, "brandNewPass789")
    assertTrue(loginWithNewPass)
  }

  @Test
  fun testLogoutSetsUnauthenticated() {
    val vm = AyurvedaViewModel()
    assertTrue(vm.uiState.value.isAuthenticated)

    vm.logout()

    org.junit.Assert.assertFalse(vm.uiState.value.isAuthenticated)
    assertEquals(com.example.ui.AuthMode.LOGIN, vm.uiState.value.authMode)
    assertTrue(vm.uiState.value.auditLogs.any { it.actionType == "LOGOUT" })
  }

  @Test
  fun testCatalogueDatabaseLoadingFlow() {
    val vm = AyurvedaViewModel()
    // Initial fetch triggers loading
    vm.refreshCatalogue()
    assertTrue(vm.uiState.value.isCatalogueLoading)
  }
}
