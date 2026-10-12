package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.repo.FakeAuthRepository
import com.example.myapplication.session.SessionViewModel
import com.example.myapplication.shell.MainRoute
import com.example.myapplication.ui.login.LoginCallbacks
import com.example.myapplication.ui.login.LoginRoute
import com.example.myapplication.ui.login.LoginViewModel
import com.example.myapplication.ui.signup.FakeSignUpRepository
import com.example.myapplication.ui.signup.SignUpRoute
import com.example.myapplication.ui.signup.SignUpViewModel

@Composable
fun AppNavHost() {
    val rootNav = rememberNavController()
    val session: SessionViewModel = viewModel()          // activity-scoped: survives screen changes

    NavHost(navController = rootNav, startDestination = Routes.LOGIN) {

        // ── Login ───────────────────────────────────────────────
        composable(Routes.LOGIN) {
            val vm: LoginViewModel = viewModel(
                factory = viewModelFactory { initializer { LoginViewModel(FakeAuthRepository()) } }
            )
            LoginRoute(
                viewModel = vm,
                callbacks = LoginCallbacks(
                    onCreateAccount = { rootNav.navigate(Routes.SIGNUP) },
                    onForgotPassword = { /* TODO */ }
                )
            ) { user, keepSignedIn ->
                session.onLoggedIn(user, keepSignedIn)
                rootNav.navigate(Routes.MAIN) {
                    popUpTo(Routes.LOGIN) { inclusive = true }   // Back must not return to login
                }
            }
        }

        // ── Sign up ─────────────────────────────────────────────
        composable(Routes.SIGNUP) {
            val vm: SignUpViewModel = viewModel(
                factory = viewModelFactory { initializer { SignUpViewModel(FakeSignUpRepository()) } }
            )
            SignUpRoute(
                viewModel = vm,
                onSignInClick = { rootNav.popBackStack() },
                onSignedUp = { rootNav.popBackStack() }       // TODO: verification screen
            )
        }

        // ── Main app (sidebar shell) ────────────────────────────
        composable(Routes.MAIN) {
            val user = session.user
            if (user == null) {
                // Logged out (or process restarted): go back to login.
                LaunchedEffect(Unit) {
                    rootNav.navigate(Routes.LOGIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                }
            } else {
                MainRoute(
                    user = user,
                    onLogout = { session.onLoggedOut() }      // the null check above routes to login
                )
            }
        }
    }
}
