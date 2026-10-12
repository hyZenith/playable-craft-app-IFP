package com.example.myapplication.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.repo.FakeLessonPlanRepository
import com.example.myapplication.data.repo.FakeMcqRepository
import com.example.myapplication.data.repo.FakeSimulationRepository
import com.example.myapplication.data.repo.FakeStoryLineRepository
import com.example.myapplication.data.repo.FakeDeliveryRepository
import com.example.myapplication.data.repo.FakeMindMapRepository
import com.example.myapplication.data.repo.FakeNoteRepository
import com.example.myapplication.data.repo.FakeNotificationRepository
import com.example.myapplication.data.repo.FakeProfileRepository
import com.example.myapplication.data.repo.FakeTeacherContextRepository
import com.example.myapplication.navigation.SidebarSection
import com.example.myapplication.navigation.rolePrefix
import com.example.myapplication.navigation.sidebarSections
import com.example.myapplication.ui.ai.NewSimulationRoute
import com.example.myapplication.ui.ai.SimulationRoute
import com.example.myapplication.ui.ai.SimulationViewModel
import com.example.myapplication.ui.ai.SimulationViewerRoute
import com.example.myapplication.ui.common.PageBackdrop
import com.example.myapplication.ui.lessonplan.CreateLessonPlanRoute
import com.example.myapplication.ui.lessonplan.CreateLessonPlanViewModel
import com.example.myapplication.ui.lessonplan.LessonPlanDetailRoute
import com.example.myapplication.ui.lessonplan.LessonPlansRoute
import com.example.myapplication.ui.lessonplan.LessonPlansViewModel
import com.example.myapplication.ui.mcq.CreateMcqRoute
import com.example.myapplication.ui.mcq.McqDetailRoute
import com.example.myapplication.ui.mcq.McqRoute
import com.example.myapplication.ui.mcq.McqViewModel
import com.example.myapplication.ui.mindmap.GenerateMindMapRoute
import com.example.myapplication.ui.mindmap.MindMapRoute
import com.example.myapplication.ui.mindmap.MindMapViewModel
import com.example.myapplication.ui.mindmap.MindMapViewRoute
import com.example.myapplication.ui.mindmap.MindMapViewViewModel
import com.example.myapplication.ui.notes.NotesRoute
import com.example.myapplication.ui.notes.NotesViewModel
import com.example.myapplication.ui.notifications.NotificationsRoute
import com.example.myapplication.ui.notifications.NotificationsViewModel
import com.example.myapplication.ui.profile.ProfileRoute
import com.example.myapplication.ui.profile.ProfileViewModel
import com.example.myapplication.ui.schedule.ScheduleRoute
import com.example.myapplication.ui.schedule.ScheduleViewModel
import com.example.myapplication.ui.storyline.CreateStoryLineRoute
import com.example.myapplication.ui.storyline.StoryLineReaderRoute
import com.example.myapplication.ui.storyline.StoryLineRoute
import com.example.myapplication.ui.storyline.StoryLineViewModel
import com.example.myapplication.ui.teachercontext.TeacherContextRoute
import com.example.myapplication.ui.teachercontext.TeacherContextViewModel
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.launch

/** Screens this wide (dp) and up get a permanent sidebar: tablets and the 65" TV. */
private const val PERMANENT_SIDEBAR_MIN_WIDTH_DP = 600

private val TopBarButtonBg = Color(0xFF15213D)
private val AvatarBg = Color(0xFF1E2B4D)

/**
 * Primary root entry composable that owns and orchestrates the inner NavHost.
 */
@Composable
fun MainRoute(
    user: LoginResponse,
    onLogout: () -> Unit
) {
    val prefix = rolePrefix(user.role)
    val sections = remember(prefix) { sidebarSections(prefix) }
    val items = remember(sections) { sections.flatMap { it.items } }
    val planLessonRoute = "$prefix/plan-lesson"

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Highlight the sidebar item even on sub-pages (e.g. "<role>/plan-lesson/new").
    val selectedRoute = items.firstOrNull {
        currentRoute == it.route || currentRoute?.startsWith(it.route + "/") == true
    }?.route

    val sharedLessonPlanRepo = remember { FakeLessonPlanRepository(withSamples = true) }
    val sharedMcqRepo = remember { FakeMcqRepository(withSamples = true) }
    val sharedSimulationRepo = remember { FakeSimulationRepository(withSamples = true) }
    val sharedStoryLineRepo = remember { FakeStoryLineRepository(withSamples = true) }
    val sharedMindMapRepo = remember { FakeMindMapRepository(withSamples = true) }

    val navigateTopLevel: (String) -> Unit = { route ->
        if (currentRoute != route) {
            val popped = navController.popBackStack(route, inclusive = false)
            if (!popped) {
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = false
                    }
                    launchSingleTop = true
                    restoreState = false
                }
            }
        }
    }

    AppShell(
        user = user,
        sections = sections,
        selectedRoute = selectedRoute,
        onNavigate = navigateTopLevel,
        onNotificationsClick = { navigateTopLevel("$prefix/notifications") },
        onProfileClick = { navigateTopLevel("$prefix/profile") },
        onLogout = onLogout
    ) {
        NavHost(navController = navController, startDestination = planLessonRoute) {

            val mcqRoute = "$prefix/mcq"

            // Lesson Plans feature routes
            composable(planLessonRoute) {
                val vm: LessonPlansViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            LessonPlansViewModel(sharedLessonPlanRepo)
                        }
                    }
                )
                LessonPlansRoute(
                    viewModel = vm,
                    onCreateClick = { navController.navigate("$planLessonRoute/new") },
                    onPlanClick = { id -> navController.navigate("$planLessonRoute/view/$id") }
                )
            }
            composable("$planLessonRoute/new") {
                val vm: CreateLessonPlanViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            CreateLessonPlanViewModel(sharedLessonPlanRepo)
                        }
                    }
                )
                CreateLessonPlanRoute(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack() },
                    onPlanGenerated = { planId -> navController.navigate("$planLessonRoute/view/$planId") },
                    onNotesClick = { navController.navigate("$prefix/notes") }
                )
            }
            composable("$planLessonRoute/create") {
                val vm: CreateLessonPlanViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            CreateLessonPlanViewModel(sharedLessonPlanRepo)
                        }
                    }
                )
                CreateLessonPlanRoute(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack() },
                    onPlanGenerated = { planId -> navController.navigate("$planLessonRoute/view/$planId") },
                    onNotesClick = { navController.navigate("$prefix/notes") }
                )
            }
            composable("$planLessonRoute/view/{planId}") { entry ->
                val id = entry.arguments?.getString("planId").orEmpty()
                LessonPlanDetailRoute(
                    planId = id,
                    repository = sharedLessonPlanRepo,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // MCQ Sets feature routes
            composable(mcqRoute) {
                val vm: McqViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            McqViewModel(sharedMcqRepo)
                        }
                    }
                )
                McqRoute(
                    viewModel = vm,
                    onGenerateMcqClick = { navController.navigate("$mcqRoute/new") },
                    onConnectDocsClick = { navController.navigate("$mcqRoute/new") },
                    onMcqSetClick = { id -> navController.navigate("$mcqRoute/view/$id") }
                )
            }
            composable("$mcqRoute/new") {
                CreateMcqRoute(
                    repository = sharedMcqRepo,
                    onBackClick = { navController.popBackStack() },
                    onMcqGenerated = { setId -> navController.navigate("$mcqRoute/view/$setId") }
                )
            }
            composable("$mcqRoute/view/{setId}") { entry ->
                val id = entry.arguments?.getString("setId").orEmpty()
                McqDetailRoute(
                    setId = id,
                    repository = sharedMcqRepo,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // PlayableCraft AI feature routes
            val aiRoute = "$prefix/ai"
            composable(aiRoute) {
                val vm: SimulationViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            SimulationViewModel(sharedSimulationRepo)
                        }
                    }
                )
                SimulationRoute(
                    viewModel = vm,
                    onNewSimulationClick = { navController.navigate("$aiRoute/new") },
                    onSimulationClick = { id -> navController.navigate("$aiRoute/view/$id") },
                    onNotesClick = { navController.navigate("$aiRoute/notes") }
                )
            }
            composable("$aiRoute/new") {
                NewSimulationRoute(
                    repository = sharedSimulationRepo,
                    onCancelClick = { navController.popBackStack() },
                    onSimulationGenerated = { simId -> navController.navigate("$aiRoute/view/$simId") }
                )
            }
            composable("$aiRoute/view/{simId}") { entry ->
                val id = entry.arguments?.getString("simId").orEmpty()
                SimulationViewerRoute(
                    simId = id,
                    repository = sharedSimulationRepo,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable("$aiRoute/notes") {
                PlaceholderScreen("Study Notes", "$aiRoute/notes")
            }

            // StoryLine feature routes
            val storylineRoute = "$prefix/storyline"
            composable(storylineRoute) {
                val vm: StoryLineViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            StoryLineViewModel(sharedStoryLineRepo)
                        }
                    }
                )
                StoryLineRoute(
                    viewModel = vm,
                    onNewStoryClick = { navController.navigate("$storylineRoute/new") },
                    onStoryLineClick = { id -> navController.navigate("$storylineRoute/view/$id") },
                    onNotesClick = { navController.navigate("$storylineRoute/notes") }
                )
            }
            composable("$storylineRoute/new") {
                CreateStoryLineRoute(
                    repository = sharedStoryLineRepo,
                    onBackClick = { navController.popBackStack() },
                    onStoryGenerated = { storyId -> navController.navigate("$storylineRoute/view/$storyId") },
                    onNotesClick = { navController.navigate("$storylineRoute/notes") }
                )
            }
            composable("$storylineRoute/view/{storyId}") { entry ->
                val id = entry.arguments?.getString("storyId").orEmpty()
                StoryLineReaderRoute(
                    storyId = id,
                    repository = sharedStoryLineRepo,
                    onBackClick = { navController.popBackStack() },
                    onNotesClick = { navController.navigate("$storylineRoute/notes") }
                )
            }
            composable("$storylineRoute/notes") {
                PlaceholderScreen("StoryLine Notes", "$storylineRoute/notes")
            }

            // Mind Map feature routes
            val mindMapRoute = "$prefix/mind-map"
            composable(mindMapRoute) {
                val vm: MindMapViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            MindMapViewModel(sharedMindMapRepo)
                        }
                    }
                )
                MindMapRoute(
                    viewModel = vm,
                    onGenerateMindMapClick = { navController.navigate("$mindMapRoute/new") },
                    onMindMapClick = { id -> navController.navigate("$mindMapRoute/view/$id") }
                )
            }
            composable("$mindMapRoute/new") {
                GenerateMindMapRoute(
                    repository = sharedMindMapRepo,
                    onBackClick = { navController.popBackStack() },
                    onMindMapGenerated = { mapId -> navController.navigate("$mindMapRoute/view/$mapId") },
                    onNotesClick = { navController.navigate("$prefix/notes") }
                )
            }
            composable("$mindMapRoute/view/{mapId}") { entry ->
                val id = entry.arguments?.getString("mapId").orEmpty()
                val vm: MindMapViewViewModel = viewModel(
                    key = id,
                    factory = viewModelFactory {
                        initializer {
                            MindMapViewViewModel(sharedMindMapRepo, id)
                        }
                    }
                )
                MindMapViewRoute(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Teacher Context feature routes
            val teacherContextRoute = "$prefix/teacher-context"
            composable(teacherContextRoute) {
                val vm: TeacherContextViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            TeacherContextViewModel(FakeTeacherContextRepository())
                        }
                    }
                )
                TeacherContextRoute(
                    viewModel = vm,
                    onNotesClick = { navController.navigate("$prefix/notes") }
                )
            }

            // Notes feature routes
            val notesRoute = "$prefix/notes"
            composable(notesRoute) {
                val vm: NotesViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            NotesViewModel(FakeNoteRepository())
                        }
                    }
                )
                NotesRoute(viewModel = vm)
            }

            // Schedule / Deliveries feature routes
            val scheduleRoute = "$prefix/schedule"
            composable(scheduleRoute) {
                val vm: ScheduleViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            ScheduleViewModel(FakeDeliveryRepository())
                        }
                    }
                )
                ScheduleRoute(viewModel = vm)
            }

            // Profile feature routes
            val profileRoute = "$prefix/profile"
            composable(profileRoute) {
                val vm: ProfileViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            ProfileViewModel(
                                FakeProfileRepository(
                                    initialProfile = com.example.myapplication.data.model.UserProfile(
                                        fullName = user?.name?.ifBlank { "Anurag Bezboruah" } ?: "Anurag Bezboruah",
                                        email = user?.email?.ifBlank { "businessandanurag@gmail.com" } ?: "businessandanurag@gmail.com",
                                        role = user?.role?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "Teacher",
                                        memberSince = "Sep 2024"
                                    )
                                )
                            )
                        }
                    }
                )
                ProfileRoute(
                    viewModel = vm,
                    onNavigateToRoute = navigateTopLevel,
                    onLogout = onLogout
                )
            }

            // Notifications feature routes
            val notificationsRoute = "$prefix/notifications"
            composable(notificationsRoute) {
                val vm: NotificationsViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            NotificationsViewModel(FakeNotificationRepository())
                        }
                    }
                )
                NotificationsRoute(viewModel = vm)
            }

            // Fallback for remaining sidebar items
            items.filter {
                it.route != planLessonRoute &&
                    it.route != mcqRoute &&
                    it.route != aiRoute &&
                    it.route != storylineRoute &&
                    it.route != mindMapRoute &&
                    it.route != teacherContextRoute &&
                    it.route != notesRoute &&
                    it.route != scheduleRoute &&
                    it.route != profileRoute &&
                    it.route != notificationsRoute
            }.forEach { item ->
                composable(item.route) { PlaceholderScreen(item.label, item.route) }
            }
        }
    }
}

// Shell container providing responsive sidebar, top bar, and decorated page backdrop.

@Composable
fun AppShell(
    user: LoginResponse?,
    sections: List<SidebarSection>,
    selectedRoute: String?,
    onNavigate: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogout: () -> Unit,
    content: @Composable () -> Unit
) {
    val permanent = LocalConfiguration.current.screenWidthDp >= PERMANENT_SIDEBAR_MIN_WIDTH_DP
    val initial = user?.name?.trim()?.firstOrNull()?.uppercase() ?: "U"

    if (permanent) {
        Row(Modifier.fillMaxSize().background(Bg)) {
            SidebarContent(
                user = user,
                sections = sections,
                selectedRoute = selectedRoute,
                onItemClick = { onNavigate(it.route) },
                onLogout = onLogout,
                modifier = Modifier.width(280.dp).fillMaxHeight()
            )
            Box(Modifier.width(1.dp).fillMaxHeight().background(FieldStroke))
            Box(Modifier.weight(1f).fillMaxHeight()) {
                PageBackdrop()
                Column(Modifier.fillMaxSize()) {
                    TopBar(
                        initial = initial,
                        showMenuAndBrand = false,
                        onMenuClick = {},
                        onNotificationsClick = onNotificationsClick,
                        onProfileClick = onProfileClick
                    )
                    Box(Modifier.weight(1f).fillMaxWidth()) { content() }
                }
            }
        }
    } else {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = Color(0xFF050A18),
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    modifier = Modifier.width(300.dp)
                ) {
                    SidebarContent(
                        user = user,
                        sections = sections,
                        selectedRoute = selectedRoute,
                        onItemClick = {
                            scope.launch { drawerState.close() }
                            onNavigate(it.route)
                        },
                        onLogout = {
                            scope.launch { drawerState.close() }
                            onLogout()
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        ) {
            Box(Modifier.fillMaxSize().background(Bg)) {
                PageBackdrop()
                Column(Modifier.fillMaxSize()) {
                    TopBar(
                        initial = initial,
                        showMenuAndBrand = true,
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onNotificationsClick = onNotificationsClick,
                        onProfileClick = onProfileClick
                    )
                    Box(Modifier.weight(1f).fillMaxWidth()) { content() }
                }
            }
        }
    }
}

// Menu button + wordmark on the left, bell + avatar on the right.
@Composable
private fun TopBar(
    initial: String,
    showMenuAndBrand: Boolean,
    onMenuClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(72.dp)
            .padding(horizontal = 20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showMenuAndBrand) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(TopBarButtonBg)
                        .clickable(onClick = onMenuClick)
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = "Open menu", tint = TextPrimary)
                }
                Spacer(Modifier.width(16.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = TextPrimary)) { append("Playable ") }
                        withStyle(SpanStyle(color = AccentLink)) { append("Craft") }
                    },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = Color(0xFFB8C2E8),
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AvatarBg)
                    .clickable(onClick = onProfileClick)
            ) {
                Text(initial, color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// Temporary page body. Shows the URL-style route so you can see navigation working.
@Composable
fun PlaceholderScreen(title: String, route: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("/$route", color = AccentLink, fontSize = 15.sp, fontFamily = FontFamily.Monospace)
        Spacer(Modifier.height(8.dp))
        Text("This page is coming next.", color = TextSecondary, fontSize = 14.sp)
    }
}
