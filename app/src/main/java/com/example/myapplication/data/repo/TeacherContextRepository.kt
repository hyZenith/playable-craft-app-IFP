package com.example.myapplication.data.repo

import com.example.myapplication.data.model.PromptGuide
import com.example.myapplication.data.model.PromptRule
import com.example.myapplication.data.model.VideoBadgeType
import com.example.myapplication.data.model.VideoWalkthrough
import kotlinx.coroutines.delay

/**
 * Repository interface providing teacher instructional content, videos, and prompting tips.
 */
interface TeacherContextRepository {
    /**
     * Retrieves video walkthrough guides.
     */
    suspend fun getVideos(): Result<List<VideoWalkthrough>>

    /**
     * Retrieves prompt engineering principles and examples.
     */
    suspend fun getPromptGuides(): Result<List<PromptGuide>>

    /**
     * Retrieves the six foundational prompt engineering rules comparing weak vs better inputs.
     */
    suspend fun getPromptRules(): Result<List<PromptRule>>
}

/**
 * In-memory repository implementation simulating pedagogical walkthrough data.
 */
class FakeTeacherContextRepository : TeacherContextRepository {

    private val videos = listOf(
        VideoWalkthrough(
            id = "vid_001",
            title = "Your first simulation, end to end",
            description = "Pick a chapter, write a focus line, and read the verification badge before you put it on the board.",
            featureCategory = "PlayableCraft AI",
            badge = VideoBadgeType.START_HERE,
            duration = "6:20",
            isPlayable = false
        ),
        VideoWalkthrough(
            id = "vid_002",
            title = "What the Focus field actually changes",
            description = "Why a focus line is scoped to the chapter, and why asking for something the chapter does not teach refuses instead of guessing.",
            featureCategory = "PlayableCraft AI",
            badge = VideoBadgeType.START_HERE,
            duration = "4:45",
            isPlayable = false
        ),
        VideoWalkthrough(
            id = "vid_003",
            title = "Planning a 45-minute period from the textbook",
            description = "Using From Textbook mode so the plan is grounded in your board's chapter rather than general knowledge.",
            featureCategory = "Lesson Plan",
            badge = VideoBadgeType.EVERYDAY,
            duration = "5:10",
            isPlayable = false
        ),
        VideoWalkthrough(
            id = "vid_004",
            title = "Building a paper across several chapters",
            description = "Chapter selection, difficulty mix, and when to turn the per-question timer on.",
            featureCategory = "MCQ",
            badge = VideoBadgeType.EVERYDAY,
            duration = "5:55",
            isPlayable = false
        ),
        VideoWalkthrough(
            id = "vid_005",
            title = "StoryLine acts, and when illustrations are worth it",
            description = "Choosing act count, and what the opt-in image step adds to your credit spend.",
            featureCategory = "StoryLine",
            badge = VideoBadgeType.EVERYDAY,
            duration = "7:30",
            isPlayable = false
        ),
        VideoWalkthrough(
            id = "vid_006",
            title = "Revision mind maps from a chapter set",
            description = "Choosing depth so branches stay useful instead of sprawling.",
            featureCategory = "Mind Map",
            badge = VideoBadgeType.EVERYDAY,
            duration = "4:05",
            isPlayable = false
        )
    )

    private val prompts = listOf(
        PromptGuide(
            id = "prompt_001",
            title = "Pinpoint Focus for Simulations",
            category = "PlayableCraft AI",
            bestPractice = "Always constrain the focus line to variables explicitly mentioned in the syllabus chapter to keep generative physics or reactions grounded.",
            examplePrompt = "Focus on the relationship between voltage, current, and resistance in Ohm's Law without introducing AC circuits."
        ),
        PromptGuide(
            id = "prompt_002",
            title = "Lesson Plan Time Distribution",
            category = "Lesson Plan",
            bestPractice = "Specify standard bell schedule intervals (e.g. 45 min) to force AI timeblocks into Hook (5m), Direct Instruction (15m), Guided Practice (15m), and Exit Ticket (10m).",
            examplePrompt = "Structure a 45-minute period on Chemical Equations with a hands-on balancing activity in the second block."
        ),
        PromptGuide(
            id = "prompt_003",
            title = "MCQ Misconception Traps",
            category = "MCQ",
            bestPractice = "Ask for distractors based on documented student misconceptions rather than arbitrary wrong numbers.",
            examplePrompt = "Generate 10 MCQs on Newton's 3rd Law where wrong choices represent common confusion with balanced forces."
        )
    )

    private val promptRules = listOf(
        PromptRule(
            id = "rule_01",
            index = "01",
            title = "Ask for one idea, not a whole chapter",
            subtitle = "A simulation shows one mechanism well or four badly. If your focus line contains 'and', it is probably two simulations.",
            weakText = "Explain light — reflection, refraction, lenses and the human eye",
            betterText = "Show how the angle of incidence equals the angle of reflection on a plane mirror",
            whyExplanation = "Why: The Planner has one scene and one set of variables to work with. A four-topic request produces a crowded scene where no single relationship is readable from the back of a classroom."
        ),
        PromptRule(
            id = "rule_02",
            index = "02",
            title = "Name what the student should be able to change",
            subtitle = "Say which quantity the slider controls. That is the difference between a diagram and a simulation.",
            weakText = "Show a pendulum",
            betterText = "Show a pendulum where changing the string length changes the time period",
            whyExplanation = "Why: Variables come from the Planner's scene plan. If you do not name one, you tend to get a fixed animation with nothing for a student to manipulate."
        ),
        PromptRule(
            id = "rule_03",
            index = "03",
            title = "Stay inside the chapter you selected",
            subtitle = "The focus line is checked against the chapter you picked. Asking for something the chapter does not teach is refused rather than answered.",
            weakText = "Selected Class 10 'Light', asked for lens-maker's formula derivation",
            betterText = "Selected Class 10 'Light', asked for image formation by a concave mirror at different object distances",
            whyExplanation = "Why: The focus line is verified against textbook bounds to guarantee curriculum fidelity and prevent exam hallucination."
        ),
        PromptRule(
            id = "rule_04",
            index = "04",
            title = "Give the numbers you want on screen",
            subtitle = "If your class works with specific resistance, focal lengths, or masses, put them in the focus line so presets load with those values.",
            weakText = "Current with a resistor and battery",
            betterText = "A 12V DC circuit with 4Ω and 6Ω resistors in series, showing ammeter and voltmeter readings",
            whyExplanation = "Why: The generator defaults to round textbook numbers, but exact values keep the simulation identical to your chalkboard example."
        ),
        PromptRule(
            id = "rule_05",
            index = "05",
            title = "State the misconception you want students to test",
            subtitle = "Simulations work best when students can deliberately break things or verify counter-intuitive rules.",
            weakText = "Falling objects of different masses",
            betterText = "Drop a feather and a bowling ball in normal air vs vacuum chamber to test if heavier falls faster",
            whyExplanation = "Why: Explicitly stating the misconception sets up dual-state comparison switches inside the simulation canvas."
        ),
        PromptRule(
            id = "rule_06",
            index = "06",
            title = "Keep StoryLine acts focused on tension and discovery",
            subtitle = "StoryLine structures lessons across narrative acts. State the historical obstacle or dilemma clearly.",
            weakText = "A story about discovery of electricity",
            betterText = "Galvani's frog leg twitch debate with Volta, leading to the creation of the voltaic pile",
            whyExplanation = "Why: Real historical conflict creates narrative momentum and anchors technical principles in human curiosity."
        )
    )

    override suspend fun getVideos(): Result<List<VideoWalkthrough>> {
        delay(300)
        return Result.success(videos)
    }

    override suspend fun getPromptGuides(): Result<List<PromptGuide>> {
        delay(200)
        return Result.success(prompts)
    }

    override suspend fun getPromptRules(): Result<List<PromptRule>> {
        delay(200)
        return Result.success(promptRules)
    }
}

