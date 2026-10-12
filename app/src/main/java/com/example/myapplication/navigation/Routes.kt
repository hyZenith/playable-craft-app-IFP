package com.example.myapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.School
import androidx.compose.ui.graphics.vector.ImageVector

/** Top-level destinations (outside the sidebar shell). */
object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val MAIN = "main"
}

data class SidebarItem(
    val label: String,
    val icon: ImageVector,
    val route: String          // e.g. "teacher/plan-lesson"  ->  playablecraft.live/teacher/plan-lesson
)

data class SidebarSection(
    val title: String,
    val items: List<SidebarItem>
)

/** Role -> URL prefix. LoginResponse.role is "TEACHER" / "TUTOR" / "SCHOOL" / "STUDENT". */
fun rolePrefix(role: String?): String = when (role?.uppercase()) {
    "SCHOOL" -> "school"
    "TUTOR" -> "tutor"
    "STUDENT" -> "student"
    else -> "teacher"
}

/**
 * The sidebar. Same items for every role for now;
 * branch on [prefix] here later if roles need different pages.
 */
fun sidebarSections(prefix: String): List<SidebarSection> = listOf(
    SidebarSection(
        "CREATE",
        listOf(
            SidebarItem("Plan Lesson", Icons.AutoMirrored.Outlined.Assignment, "$prefix/plan-lesson"),
            SidebarItem("Generate MCQ", Icons.Outlined.Checklist, "$prefix/mcq"),
            SidebarItem("PlayableCraft AI", Icons.Outlined.AutoAwesome, "$prefix/ai"),
            SidebarItem("StoryLine", Icons.AutoMirrored.Outlined.MenuBook, "$prefix/storyline"),
            SidebarItem("Mind Map", Icons.Outlined.AccountTree, "$prefix/mind-map")
        )
    ),
    SidebarSection(
        "MANAGE",
        listOf(
            SidebarItem("Notes", Icons.Outlined.Description, "$prefix/notes"),
            SidebarItem("Schedule", Icons.Outlined.CalendarMonth, "$prefix/schedule")
        )
    ),
    SidebarSection(
        "GUIDE",
        listOf(
            SidebarItem("Teacher Context", Icons.Outlined.School, "$prefix/teacher-context")
        )
    ),
    SidebarSection(
        "ACCOUNT",
        listOf(
            SidebarItem("Profile", Icons.Outlined.AccountCircle, "$prefix/profile"),
            SidebarItem("Notifications", Icons.Outlined.Notifications, "$prefix/notifications")
        )
    )
)
