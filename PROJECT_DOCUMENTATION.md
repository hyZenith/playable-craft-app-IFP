# PlayableCraft (Playable Craft) — Comprehensive Project Documentation

> **Version:** 1.0  
> **Platform:** Android (Phone, Tablet, Large Displays & Android TV)  
> **Language:** Kotlin (100%)  
> **UI Toolkit:** Jetpack Compose (Material 3)  
> **Target SDK:** Android 36 (compileSdk: 37, minSdk: 29)  
> **Architecture:** Modern Android MVVM (Model-View-ViewModel) + Unidirectional Data Flow (UDF) + Repository Pattern  

---

## Table of Contents
1. [Project Overview & Mission](#1-project-overview--mission)
2. [High-Level Architecture & Tech Stack](#2-high-level-architecture--tech-stack)
3. [UI Design System & Theming](#3-ui-design-system--theming)
4. [Complete Project File Structure](#4-complete-project-file-structure)
5. [Comprehensive File-by-File Reference](#5-comprehensive-file-by-file-reference)
   - [5.1 Root Build & Configuration Files](#51-root-build--configuration-files)
   - [5.2 App Module Build & Manifest](#52-app-module-build--manifest)
   - [5.3 Android Resources](#53-android-resources)
   - [5.4 Core Application & Session](#54-core-application--session)
   - [5.5 Navigation & App Shell](#55-navigation--app-shell)
   - [5.6 Common UI & Theming](#56-common-ui--theming)
   - [5.7 Data Models (`data/model`)](#57-data-models-datamodel)
   - [5.8 Repositories & In-Memory Data Sources (`data/repo`)](#58-repositories--in-memory-data-sources-datarepo)
   - [5.9 Feature Presentation Modules (`ui/*`)](#59-feature-presentation-modules-ui)
     - [Authentication: Login & Signup](#authentication-login--signup)
     - [Lesson Planning Hub](#lesson-planning-hub)
     - [MCQ (Multiple Choice Questions) Generator](#mcq-multiple-choice-questions-generator)
     - [PlayableCraft AI Simulations](#playablecraft-ai-simulations)
     - [StoryLine Narrative Learning](#storyline-narrative-learning)
     - [Concept Mind Map & Interactive Canvas Visualizer](#concept-mind-map--interactive-canvas-visualizer)
     - [Teacher Notes & Scratchpad](#teacher-notes--scratchpad)
     - [Schedule & Delivery Management](#schedule--delivery-management)
     - [Teacher Context & Prompt Engineering](#teacher-context--prompt-engineering)
     - [User Profile & Settings](#user-profile--settings)
     - [Notification Center](#notification-center)
   - [5.10 Testing Suite](#510-testing-suite)
6. [Key Feature Workflows](#6-key-feature-workflows)
7. [Build & Execution Guide](#7-build--execution-guide)

---

## 1. Project Overview & Mission

**PlayableCraft** is an intelligent, AI-augmented pedagogical platform designed specifically for educators, tutors, schools, and students. Tailored for Indian and global academic boards (such as **SEBA**, **NCERT**, **CBSE**, and **ICSE**), the platform empowers teachers to create, manage, and deliver interactive and curriculum-aligned learning materials.

### Core Capabilities:
- **AI-Powered Lesson Planning:** Generates comprehensive lesson plans structured around Bloom's Taxonomy, complete with classroom time blocks, common misconceptions, differentiated learning instructions, and board-work blueprints.
- **Interactive Concept Mind Maps:** Generates multi-level concept graphs with custom pan/zoom canvas rendering and collapsible hierarchical nodes.
- **PlayableCraft AI Simulations:** Catalogs and previews STEM interactive simulations (physics orbital mechanics, chemistry molecular structures, wave optics).
- **Assessment & MCQ Generation:** Produces contextual multiple-choice question sets powered by connected syllabus documentation (RAG).
- **Interactive StoryLines:** Crafts narrative-based educational scenarios broken into pedagogical acts.
- **Delivery Scheduling:** Organizes and tracks content delivery to specific batches/cohorts (e.g., *Class 10 - Batch A*).
- **Teacher Context & Prompt Engineering:** Equips teachers with best practices, rule guides (weak vs. strong prompts), and instructional walkthrough videos.
- **Multi-Role Authentication:** Supports distinct roles (`Teacher`, `School`, `Tutor`, `Student`) with specialized login requirements (such as Application ID login for Tutors, and UDISE code/certificate verification for Schools).

---

## 2. High-Level Architecture & Tech Stack

The application strictly adheres to official Android Architecture Guidelines with Clean Architecture separation:

```
┌─────────────────────────────────────────────────────────────┐
│                     UI Layer (Compose)                      │
│   Screen Composable (Stateless) + Route Composable (State)   │
└──────────────────────────────▲──────────────────────────────┘
                               │ StateFlow / Compose State
┌──────────────────────────────┴──────────────────────────────┐
│                      ViewModel Layer                        │
│            Holds UiState, handles user actions              │
└──────────────────────────────▲──────────────────────────────┘
                               │ Coroutines / Suspend functions
┌──────────────────────────────┴──────────────────────────────┐
│                      Repository Layer                       │
│    Data Abstraction Interfaces + Fake / Remote Implementations  │
└──────────────────────────────▲──────────────────────────────┘
                               │
┌──────────────────────────────┴──────────────────────────────┐
│                         Data Models                         │
│               Domain entities, DTOs, Enums                  │
└─────────────────────────────────────────────────────────────┘
```

### Key Libraries & Dependencies:
- **Jetpack Compose (BOM 2024.09.03):** 100% declarative UI toolkit.
- **Material 3 (`androidx.compose.material3`):** Dark-mode themed components and tokens.
- **Material Icons Extended:** Extended icon sets for navigation, syllabus boards, and status tags.
- **Navigation Compose (`androidx.navigation:navigation-compose:2.8.2`):** Type-safe destination routing and nested NavHosts.
- **Lifecycle Runtime & ViewModel Compose (2.8.6 / 2.11.0):** ViewModel lifecycle retention and state hoisting.
- **Kotlin Coroutines (`kotlinx-coroutines`):** Asynchronous operations and non-blocking simulations.
- **Adaptive Screen Handling:** Supports mobile drawer navigation and permanent dual-pane sidebars on displays $\ge 600\,\text{dp}$ (tablets, foldables, and large screen displays/Android TV).

---

## 3. UI Design System & Theming

The application implements a custom **Deep Dark Blue / Sci-Tech Cosmic Theme**:

| Token Name | Hex Value | Purpose |
| :--- | :--- | :--- |
| `Bg` | `#0A1122` | Root dark navy canvas background |
| `FieldFill` | `#0E1730` | Input field, chip, and card surface fill |
| `FieldStroke` | `#2A3552` | 1px border stroke for inputs, cards, and dividers |
| `TextPrimary` | `#FFFFFF` | Primary headings, prominent titles, and active icons |
| `TextSecondary` | `#98A2BC` | Subtitles, body descriptions, secondary text |
| `TextLabel` | `#8C96B0` | Uppercase section labels, counters, metadata chips |
| `TextPlaceholder` | `#5E6985` | Input placeholder text, disabled icons |
| `Accent` | `#4466FF` | Primary action buttons, selected radio states |
| `AccentLink` | `#5E82FF` | Text links, active breadcrumb items, glowing highlights |
| `StatusGreen` | `#1FD17B` | Success states, active delivery status badges |
| `ErrorBorder` / `ErrorText` | `#FF5C6C` / `#FF7B7B` | Form validation and error banners |

### Decorative Backdrop (`PageBackdrop.kt`)
Pages feature an ambient canvas backdrop with two subtle radial glowing geometric forms:
1. **Upper-Right Ring:** Low-opacity concentric navy ring (`#1B2E6B` / `#2A44A0`).
2. **Bottom-Left Glowing Disc:** Radial gradient disc fading from deep cobalt (`#0E1D52`) to indigo (`#1A3AA0`) with an outlined accent border.

---

## 4. Complete Project File Structure

```
MyApplication/
├── .gitignore
├── build.gradle.kts
├── gradle.properties
├── local.properties
├── settings.gradle.kts
├── PROJECT_DOCUMENTATION.md
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
└── app/
    ├── .gitignore
    ├── build.gradle.kts
    ├── proguard-rules.pro
    └── src/
        ├── androidTest/java/com/example/myapplication/
        │   └── ExampleInstrumentedTest.kt
        ├── test/java/com/example/myapplication/
        │   └── ExampleUnitTest.kt
        └── main/
            ├── AndroidManifest.xml
            ├── res/
            │   ├── values/
            │   │   ├── colors.xml
            │   │   ├── strings.xml
            │   │   └── themes.xml
            │   ├── values-v29/
            │   │   └── themes.xml
            │   └── xml/
            │       ├── backup_rules.xml
            │       └── data_extraction_rules.xml
            └── java/com/example/myapplication/
                ├── MainActivity.kt
                ├── session/
                │   └── SessionViewModel.kt
                ├── navigation/
                │   ├── Routes.kt
                │   └── AppNavHost.kt
                ├── shell/
                │   ├── AppShell.kt
                │   └── SidebarContent.kt
                ├── data/
                │   ├── model/
                │   │   ├── AuthModels.kt
                │   │   ├── DeliveryModels.kt
                │   │   ├── LessonPlanModels.kt
                │   │   ├── McqModels.kt
                │   │   ├── MindMapModels.kt
                │   │   ├── NoteModels.kt
                │   │   ├── NotificationModels.kt
                │   │   ├── ProfileModels.kt
                │   │   ├── SimulationModels.kt
                │   │   ├── StoryLineModels.kt
                │   │   └── TeacherContextModels.kt
                │   └── repo/
                │       ├── AuthRepository.kt
                │       ├── DeliveryRepository.kt
                │       ├── LessonPlanRepository.kt
                │       ├── McqRepository.kt
                │       ├── MindMapRepository.kt
                │       ├── NoteRepository.kt
                │       ├── NotificationRepository.kt
                │       ├── ProfileRepository.kt
                │       ├── SimulationRepository.kt
                │       ├── StoryLineRepository.kt
                │       └── TeacherContextRepository.kt
                └── ui/
                    ├── common/
                    │   └── PageBackdrop.kt
                    ├── theme/
                    │   ├── Color.kt
                    │   ├── Theme.kt
                    │   └── Type.kt
                    ├── login/
                    │   ├── LoginScreen.kt
                    │   └── LoginViewModel.kt
                    ├── signup/
                    │   ├── SignUpScreen.kt
                    │   └── SignUpViewModel.kt
                    ├── lessonplan/
                    │   ├── LessonPlansScreen.kt
                    │   ├── LessonPlansViewModel.kt
                    │   ├── CreateLessonPlanScreen.kt
                    │   └── CreateLessonPlanViewModel.kt
                    ├── mcq/
                    │   ├── McqScreen.kt
                    │   └── McqViewModel.kt
                    ├── ai/
                    │   ├── SimulationScreen.kt
                    │   └── SimulationViewModel.kt
                    ├── storyline/
                    │   ├── StoryLineScreen.kt
                    │   └── StoryLineViewModel.kt
                    ├── mindmap/
                    │   ├── MindMapScreen.kt
                    │   ├── MindMapViewModel.kt
                    │   ├── MindMapViewScreen.kt
                    │   └── MindMapViewViewModel.kt
                    ├── notes/
                    │   ├── NotesScreen.kt
                    │   └── NotesViewModel.kt
                    ├── schedule/
                    │   ├── ScheduleScreen.kt
                    │   └── ScheduleViewModel.kt
                    ├── teachercontext/
                    │   ├── TeacherContextScreen.kt
                    │   └── TeacherContextViewModel.kt
                    ├── profile/
                    │   ├── ProfileScreen.kt
                    │   └── ProfileViewModel.kt
                    └── notifications/
                        ├── NotificationsScreen.kt
                        └── NotificationsViewModel.kt
```

---

## 5. Comprehensive File-by-File Reference

### 5.1 Root Build & Configuration Files

| File Path | Description & Purpose |
| :--- | :--- |
| `build.gradle.kts` | Top-level build script configuring project-wide Gradle plugins (Android Application plugin and Jetpack Compose compiler plugin) using `apply false`. |
| `settings.gradle.kts` | Declares plugin repositories (`google()`, `mavenCentral()`), dependency resolution management repositories, the root project name (`"My Application"`), and includes the `:app` module. |
| `gradle.properties` | Configures JVM execution arguments (`-Xmx2048m`), enables AndroidX (`android.useAndroidX=true`), and toggles Kotlin code style settings. |
| `local.properties` | Machine-specific SDK location file containing the path to the Android SDK (`sdk.dir`). |
| `.gitignore` | Ignores Gradle cache directories (`.gradle/`), build output folders (`build/`), IDE workspace configurations (`.idea/`, `*.iml`), and OS system files (`Thumbs.db`, `.DS_Store`). |
| `gradle/wrapper/gradle-wrapper.properties` | Defines the Gradle wrapper distribution version (Gradle 8.13.0) and download URL. |

---

### 5.2 App Module Build & Manifest

| File Path | Description & Purpose |
| :--- | :--- |
| `app/build.gradle.kts` | Main application Gradle configuration. Defines namespace `com.example.myapplication`, `compileSdk = 37`, `minSdk = 29`, `targetSdk = 36`, versioning (1.0), Java 11 compatibility, Compose build features, and core AndroidX, Compose BOM, Navigation, Material 3, and test dependencies. |
| `app/proguard-rules.pro` | Specifies R8/Proguard code shrinking and obfuscation rules for production release builds. |
| `app/.gitignore` | Module-specific ignore file ensuring the `/build` folder is ignored by git. |
| `app/src/main/AndroidManifest.xml` | Application manifest. Requests `android.permission.INTERNET`, declares `android.software.leanback` and `touchscreen` compatibility (Android TV support), defines application backup rules, and sets `MainActivity` as the launcher activity for both standard and Leanback launchers. |

---

### 5.3 Android Resources

| File Path | Description & Purpose |
| :--- | :--- |
| `app/src/main/res/values/strings.xml` | String resource definitions containing `app_name = "My Application"`. |
| `app/src/main/res/values/colors.xml` | Traditional XML color definitions (`black`, `white`, standard accent shades). |
| `app/src/main/res/values/themes.xml` | Base XML style `Theme.MyApplication` inheriting from `Theme.Material3.DayNight.NoActionBar`. |
| `app/src/main/res/values-v29/themes.xml` | API 29+ specific theme overrides managing transparent system bar color styles. |
| `app/src/main/res/xml/backup_rules.xml` | Defines legacy full-backup inclusion and exclusion configurations. |
| `app/src/main/res/xml/data_extraction_rules.xml` | Android 12+ cloud backup and device-to-device migration extraction rules. |

---

### 5.4 Core Application & Session

| File Path | Description & Purpose |
| :--- | :--- |
| `app/src/main/java/com/example/myapplication/MainActivity.kt` | The single activity host for the entire app. Configures edge-to-edge rendering with transparent status and navigation bars, sets Compose content, and wraps execution in `MyApplicationTheme` and `AppNavHost`. |
| `app/src/main/java/com/example/myapplication/session/SessionViewModel.kt` | Activity-scoped ViewModel surviving screen navigation. Retains in-memory user authentication state (`user: LoginResponse?`), token credentials, and the `keepSignedIn` user preference. Handles `onLoggedIn()` and `onLoggedOut()`. |

---

### 5.5 Navigation & App Shell

| File Path | Description & Purpose |
| :--- | :--- |
| `app/src/main/java/com/example/myapplication/navigation/Routes.kt` | Defines top-level navigation routes (`LOGIN`, `SIGNUP`, `MAIN`), sidebar menu models (`SidebarItem`, `SidebarSection`), and the `rolePrefix()` function which generates role-scoped URL paths (e.g., `teacher/plan-lesson`, `school/notes`). Pre-configures sidebar navigation organized into `CREATE`, `MANAGE`, `GUIDE`, and `ACCOUNT`. |
| `app/src/main/java/com/example/myapplication/navigation/AppNavHost.kt` | Root Compose `NavHost` managing the top-level backstack. Controls transition logic from `LOGIN` $\to$ `MAIN`, redirects to `SIGNUP`, and automatically navigates back to login upon session expiration or logout. |
| `app/src/main/java/com/example/myapplication/shell/AppShell.kt` | Responsive app container that dynamically adapts to screen size: operates as a modal navigation drawer on mobile phones and expands into a permanent dual-pane sidebar on screens $\ge 600\,\text{dp}$ (tablets, foldables, Android TV). Hosts the internal nested `NavHost` for all features, the top header bar with branding, notification bell, profile avatar, and fallback `PlaceholderScreen`. |
| `app/src/main/java/com/example/myapplication/shell/SidebarContent.kt` | Renders the sidebar content. Displays the "Playable Craft" wordmark, categorized navigation rows with custom TV remote/D-pad focus states, user profile summary, and logout button. |

---

### 5.6 Common UI & Theming

| File Path | Description & Purpose |
| :--- | :--- |
| `app/src/main/java/com/example/myapplication/ui/common/PageBackdrop.kt` | High-performance custom Canvas component rendering atmospheric background glow circles, gradients, and subtle stroke rings behind pages. |
| `app/src/main/java/com/example/myapplication/ui/theme/Color.kt` | Defines the unified design system color palette: cosmic navy background, card fills, 1px stroke borders, text hierarchy, accent blue, status green, and error reds. |
| `app/src/main/java/com/example/myapplication/ui/theme/Theme.kt` | Material 3 Compose theme wrapper. Maps application color tokens into a DarkColorScheme and applies custom typography and shapes. |
| `app/src/main/java/com/example/myapplication/ui/theme/Type.kt` | Configures typography styles for clean sans-serif and monospace text, defining font weights, letter spacing, and line heights for headings and body content. |

---

### 5.7 Data Models (`data/model`)

| File Path | Description & Purpose |
| :--- | :--- |
| `AuthModels.kt` | Authentication requests (`EmailLoginRequest`, `ApplicationIdLoginRequest`) and response payloads (`LoginResponse`, `MeResponse`). |
| `DeliveryModels.kt` | Models representing scheduled or sent educational deliveries (`DeliveryItem`, `DeliveryContentType`, `DeliveryStatus`). |
| `LessonPlanModels.kt` | Comprehensive lesson plan domain model: Bloom's taxonomy objectives (`LessonObjective`), classroom `TimeBlock`, `DifferentiationNotes`, syllabus metadata, `LessonPlanSummary`, and generation request payload `GenerateLessonPlanRequest`. |
| `McqModels.kt` | Models for Multiple Choice Question sets (`McqSetSummary`, `McqFilter`), including question count, options count, format description, and RAG status. |
| `MindMapModels.kt` | Hierarchical recursive tree data structures (`MindMapNode`), detailed mind map view model (`MindMapDetail`), card summary model (`MindMapSummary`), and filter criteria (`MindMapFilter`). |
| `NoteModels.kt` | Models for study notes and teacher memos (`NoteItem`, `NoteStatus`) with screen context tagging and formatted timestamps. |
| `NotificationModels.kt` | Models for alerts and notifications (`NotificationItem`, `NotificationType`: SYSTEM, SIMULATION, LESSON_PLAN, MCQ, DELIVERY). |
| `ProfileModels.kt` | User identity entity (`UserProfile`) containing name, email, role, join date, avatar URL, and dynamic two-letter initials calculation for avatars. |
| `SimulationModels.kt` | Metadata representation for interactive simulations (`SimulationSummary`, `SimulationVisualType`: MOLECULE, PHYSICS_ORBIT, WAVE_OPTICS, GENERIC). |
| `StoryLineModels.kt` | Narrative module representation (`StoryLineSummary`) storing syllabus board, subject, grade level, and act counts. |
| `TeacherContextModels.kt` | Pedagogical reference models: instructional video walkthroughs (`VideoWalkthrough`, `VideoBadgeType`), prompt engineering guides (`PromptGuide`), and comparative rules (`PromptRule`) detailing weak vs. better teacher prompts. |

---

### 5.8 Repositories & In-Memory Data Sources (`data/repo`)

| File Path | Description & Purpose |
| :--- | :--- |
| `AuthRepository.kt` | Interface `AuthRepository` and mock `FakeAuthRepository` simulating network latency, role validation, and mock JWT credentials. |
| `DeliveryRepository.kt` | Repository interface and fake implementation managing scheduled classroom deliveries, status transitions, and cohort tracking. |
| `LessonPlanRepository.kt` | Interface and mock implementation managing lesson plans: pre-loaded sample plans (e.g., *SEBA Class 10 Light Reflection and Refraction*), simulated AI plan generation, and filtering by board/subject. |
| `McqRepository.kt` | Interface and mock implementation for generating and querying MCQ sets across Science, Math, Social Science, and Language with RAG context indicators. |
| `MindMapRepository.kt` | Interface and mock implementation managing multi-level recursive concept trees (e.g., *Class 10 Mathematics* containing Real Numbers, Polynomials, Quadratic Equations, and Trigonometry). |
| `NoteRepository.kt` | Interface and mock implementation managing teacher notes CRUD operations, tab switching (Active vs. Archived), and page context associations. |
| `NotificationRepository.kt` | Interface and mock implementation providing system and generation notifications, with unread badge counters and "mark as read" capabilities. |
| `ProfileRepository.kt` | Repository interface and mock implementation managing teacher profile information and settings persistence. |
| `SimulationRepository.kt` | Interface and mock implementation providing interactive physics, chemistry, and biology simulation cards with visual thumbnail styles. |
| `StoryLineRepository.kt` | Interface and mock implementation managing interactive educational storytelling modules. |
| `TeacherContextRepository.kt` | Interface and mock implementation supplying instructional videos and actionable prompt engineering templates and rules. |

---

### 5.9 Feature Presentation Modules (`ui/*`)

#### Authentication: Login & Signup
- **`ui/login/LoginViewModel.kt`**: State holder managing multi-role login selection (`Teacher`, `School`, `Tutor`, `Student`). Supports email-based authentication and Application ID authentication for tutors. Performs client-side validation, password visibility toggling, and dispatches requests to `AuthRepository`.
- **`ui/login/LoginScreen.kt`**: Cosmic-themed login UI. Displays role selector chips, dynamic input fields (Email or Application ID), password toggle, "Keep signed in" checkbox, submit button, and links to registration.
- **`ui/signup/SignUpViewModel.kt`**: Manages registration state for institutions, tutors, and students. Validates 10-digit phone numbers, 11-digit UDISE codes for schools, simulates OTP email/phone verification, and handles document uploads.
- **`ui/signup/SignUpScreen.kt`**: Registration form composable featuring role selection, verification buttons with inline spinners, institution type dropdown, file picker launcher for certificates, and terms acceptance.

#### Lesson Planning Hub
- **`ui/lessonplan/LessonPlansViewModel.kt`**: Loads lesson plans, manages board and subject filter chips, search query state, and exposes categorized plan counts.
- **`ui/lessonplan/LessonPlansScreen.kt`**: Catalog screen displaying lesson plan cards with syllabus badges, duration, Bloom's objectives overview, feasibility tags (Simulation, MCQ, Notes), search bar, and "Create Lesson Plan" action.
- **`ui/lessonplan/CreateLessonPlanViewModel.kt`**: Manages the multi-field AI generation form: Syllabus Board (`SEBA`, `NCERT`, `CBSE`, `ICSE`), Grade, Subject, Chapter, Topic, and Duration. Simulates generation and exposes created plan IDs.
- **`ui/lessonplan/CreateLessonPlanScreen.kt`**: Interactive generation form. Features dropdown selectors, duration slider, quick suggestion chips, and "Generate Plan" action.

#### MCQ (Multiple Choice Questions) Generator
- **`ui/mcq/McqViewModel.kt`**: Manages assessment sets, subject tab selection (`All`, `Science`, `Math`, `Social Science`, `Language`), search filter queries, and set counts.
- **`ui/mcq/McqScreen.kt`**: MCQ library screen presenting set cards with question count badges, options count, format descriptions, RAG-enabled indicators, and "Generate MCQ" / "Connect Documents" header actions.

#### PlayableCraft AI Simulations
- **`ui/ai/SimulationViewModel.kt`**: Manages simulation catalog state, subject category tabs (`All`, `Physics`, `Chemistry`, `Biology`), and search filters.
- **`ui/ai/SimulationScreen.kt`**: Interactive simulation hub. Implements custom Jetpack Compose Canvas thumbnail previews for scientific phenomena (atomic orbital rings, molecule nodes, wave interference patterns), search input, and "New Simulation" action.

#### StoryLine Narrative Learning
- **`ui/storyline/StoryLineViewModel.kt`**: Manages interactive story modules, search filtering, and category selection.
- **`ui/storyline/StoryLineScreen.kt`**: StoryLine catalog screen showcasing narrative card tiles with act count badges, uppercase syllabus breadcrumbs, and "Create StoryLine" action.

#### Concept Mind Map & Interactive Canvas Visualizer
- **`ui/mindmap/MindMapViewModel.kt`**: Manages mind map list view, board/subject filters, and search query state.
- **`ui/mindmap/MindMapScreen.kt`**: Grid/list of concept maps displaying node metrics, depth levels, and syllabus breadcrumbs.
- **`ui/mindmap/MindMapViewViewModel.kt`**: Manages state for an individual open mind map, loading the recursive tree hierarchy and tracking expanded/collapsed node IDs.
- **`ui/mindmap/MindMapViewScreen.kt`**: Advanced interactive canvas visualizer. Implements multi-touch gestures (`detectTransformGestures`) for smooth pan and zoom, renders bezier branch connecting curves on Canvas, displays custom interactive node pills, and provides full-screen toggle and collapse/expand all controls.

#### Teacher Notes & Scratchpad
- **`ui/notes/NotesViewModel.kt`**: Manages teacher notes state, Active vs. Archived tab switching, note creation dialog, context tagging, and note deletion/archival.
- **`ui/notes/NotesScreen.kt`**: Clean note-taking workspace. Displays notes grouped by page context (e.g., Lesson Plans, Simulations, General), note composition modal dialog, and status management.

#### Schedule & Delivery Management
- **`ui/schedule/ScheduleViewModel.kt`**: Manages delivery calendar items, status filtering (`All`, `Scheduled`, `Sent`, `Completed`), and batch assignments.
- **`ui/schedule/ScheduleScreen.kt`**: Classroom delivery manager. Displays scheduled content cards (Simulations, MCQs, Lesson Plans) linked to target cohorts with status pill badges.

#### Teacher Context & Prompt Engineering
- **`ui/teachercontext/TeacherContextViewModel.kt`**: Manages pedagogical training content across two primary tabs: Video Walkthroughs and Prompt Engineering Rules.
- **`ui/teachercontext/TeacherContextScreen.kt`**: Professional development center. Presents video tutorial cards ("Start Here", "Everyday" badges) and detailed prompt engineering comparative cards contrasting *Weak Prompts* with *Better Prompts* alongside pedagogical explanations.

#### User Profile & Settings
- **`ui/profile/ProfileViewModel.kt`**: Manages user profile information, contact details, account role display, and preferences toggles.
- **`ui/profile/ProfileScreen.kt`**: Profile screen featuring circular avatar with dynamic initials, user metadata cards, quick navigation shortcuts to all app sections, notification toggles, and logout action.

#### Notification Center
- **`ui/notifications/NotificationsViewModel.kt`**: Manages incoming notifications, filter tabs (`All`, `Unread`, `Simulations`, `Deliveries`), unread counts, and "Mark All as Read" actions.
- **`ui/notifications/NotificationsScreen.kt`**: Notification feed displaying categorized notifications with timestamps, unread highlight markers, and type-specific icon indicators.

---

### 5.10 Testing Suite

| File Path | Description & Purpose |
| :--- | :--- |
| `app/src/test/java/com/example/myapplication/ExampleUnitTest.kt` | Local JVM unit test template verifying basic addition assertions. |
| `app/src/androidTest/java/com/example/myapplication/ExampleInstrumentedTest.kt` | Instrumented test running on an Android device or emulator, verifying context package name matching `com.example.myapplication`. |

---

## 6. Key Feature Workflows

### 6.1 Authentication & Multi-Role Onboarding
1. User opens the app at the `login` route.
2. Selects role (`Teacher`, `School`, `Tutor`, `Student`).
   - If `Tutor` is selected, the input field changes to **Application ID**.
   - For other roles, **Email** is requested.
3. Upon submitting credentials:
   - Success: `SessionViewModel` stores the user and JWT token; navigation transitions to `main`, popping `login` off the backstack.
4. If a new user chooses "Create account":
   - Directed to `signup` route with fields for role, phone verification, school UDISE code (11 digits), and affiliation certificate upload.

### 6.2 Responsive Navigation & Shell
- On phones in portrait mode ($< 600\,\text{dp}$), the screen presents a top bar with a hamburger menu button that opens a `ModalNavigationDrawer`.
- On tablets, foldables, and large displays ($\ge 600\,\text{dp}$), the UI automatically presents a permanent 280dp dark sidebar alongside the content area.
- Sidebar menu navigation is organized into four logical clusters:
  - **CREATE:** Plan Lesson, Generate MCQ, PlayableCraft AI, StoryLine, Mind Map
  - **MANAGE:** Notes, Schedule
  - **GUIDE:** Teacher Context
  - **ACCOUNT:** Profile, Notifications

### 6.3 Deep Canvas Mind Map Visualizer
1. User opens a mind map from the Mind Map list.
2. `MindMapViewScreen` loads the hierarchical tree structure.
3. Canvas calculates positions for root, category, and leaf nodes, connecting them with smooth curved lines.
4. User can pan across the graph and pinch-to-zoom using touch gestures, or use the "Expand All" / "Collapse All" actions to inspect complex syllabus concepts.

---

## 7. Build & Execution Guide

### Prerequisites:
- **Android Studio:** Ladybug (2024.2.1+) or newer.
- **JDK:** Version 11 or higher.
- **Android SDK:** Platform 36/37 installed.

### Command Line Build:
Run the following Gradle commands in the project root:

```bash
# Clean the project
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Install on connected device or emulator
./gradlew installDebug
```

---

