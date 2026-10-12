# PlayableCraft (Playable Craft) — Android Application

PlayableCraft is an intelligent, AI-augmented pedagogical platform built for educators, schools, tutors, and students. Tailored for academic boards such as **SEBA**, **NCERT**, **CBSE**, and **ICSE**, it provides tools to generate interactive simulations, structured lesson plans, Bloom's taxonomy assessments, concept mind maps, and interactive story lines.

For the complete architectural overview, feature documentation, and file-by-file breakdown of all 78 files in this project, please refer to:

👉 **[PROJECT_DOCUMENTATION.md](PROJECT_DOCUMENTATION.md)**

---

## Quick Summary

- **Architecture:** MVVM + Clean Architecture (Data, Session, Navigation, Shell, UI Features)
- **UI Framework:** 100% Jetpack Compose with Material 3
- **Theme:** Dark Blue Cosmic / Sci-Tech Design System (`#0A1122`)
- **Responsive Layout:** Adaptive shell with permanent dual-pane sidebar on displays $\ge 600\,\text{dp}$ (Tablets & Android TV) and drawer navigation on mobile phones.
- **Language:** Kotlin 100%
- **Target SDK:** Android 36 (Compile SDK: 37, Min SDK: 29)

---

## Core Features

1. **Lesson Plan Hub:** AI-generated lesson plans based on Bloom's Taxonomy with time-block plans and differentiation notes.
2. **Concept Mind Maps:** Recursive tree concept graphs with interactive Canvas pan/zoom visualizer.
3. **PlayableCraft AI Simulations:** Interactive STEM simulation catalog with custom Canvas thumbnail visualizers.
4. **Assessment & MCQ Generator:** Question set generator with RAG context support.
5. **StoryLine Narrative Learning:** Scenario-based modular narrative lessons.
6. **Teacher Notes & Delivery Scheduling:** Contextual notes scratchpad and batch delivery calendar.
7. **Teacher Context & Prompt Engineering:** Video tutorials and weak vs. better prompt guides.
8. **Multi-Role Authentication:** Role-tailored logins for Teachers, Schools, Tutors (Application ID), and Students.
