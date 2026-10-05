package com.example.model

enum class VideoCreationMode(val title: String, val description: String, val iconName: String) {
  TEXT_TO_VIDEO("Text to Video", "Generate cinematic video from prompt description", "text"),
  SCRIPT_TO_VIDEO("Script to Video", "Turn written screenplay into visual scenes", "description"),
  STORY_TO_VIDEO("Story to Video", "Transform multi-part narrative into full video", "auto_stories"),
  IMAGE_TO_VIDEO("Image to Video", "Animate static visual images into live footage", "image"),
  TALKING_CHARACTER("Talking Character", "Realistic talking avatars with speech lip-sync", "record_voice_over"),
  ADVERT_VIDEO("Advert Video", "High-converting commercial & marketing videos", "campaign"),
  EDUCATIONAL_VIDEO("Educational Video", "Visual tutorials and step-by-step explainers", "school"),
  YOUTUBE_VIDEO("YouTube Video", "Full-length 16:9 cinematic YouTube productions", "smart_display"),
  TIKTOK_REELS("TikTok/Reels Video", "Fast-paced viral 9:16 vertical short videos", "vertical_shades"),
  SHORT_FILM("Short Film", "Deep narrative cinematic films with dialogue", "movie"),
  DOCUMENTARY("Documentary", "Historical & factual investigative documentaries", "history_edu"),
  PRODUCT_PROMO("Product Promotion", "Clean 3D lighting and luxury product showcases", "shopping_bag"),
  MOTIVATIONAL("Motivational Video", "Emotional life journeys with inspiring piano", "psychology"),
  NEWS_STYLE("News Style Video", "Broadcast studio style with headline tickers", "newspaper"),
  PODCAST_CLIPS("Podcast Clips", "Dynamic speaking waveforms & highlight reels", "mic")
}

enum class VisualStyle(val title: String) {
  PHOTOREALISTIC_CINEMATIC("Photorealistic Cinematic"),
  PHOTOREALISTIC("Photorealistic"),
  CINEMATIC("Cinematic"),
  DOCUMENTARY("Documentary"),
  COMMERCIAL("Commercial"),
  LUXURY("Luxury"),
  MUSIC_VIDEO("Music Video"),
  FILM("Film"),
  VINTAGE_FILM("Vintage Film"),
  FUTURISTIC("Futuristic"),
  ANIME("Anime"),
  ANIMATION_3D("3D Animation")
}

enum class LightingStyle(val title: String) {
  NATURAL("Natural"),
  GOLDEN_HOUR("Golden Hour"),
  STUDIO("Studio"),
  NIGHT("Night"),
  NEON("Neon"),
  DRAMATIC("Dramatic"),
  SOFT("Soft"),
  HIGH_CONTRAST("High Contrast")
}

enum class CameraType(val title: String) {
  PRO_CINEMA("Professional cinema camera"),
  DSLR("DSLR look"),
  SMARTPHONE("Smartphone"),
  DRONE("Drone")
}

enum class CameraShot(val title: String) {
  ESTABLISHING("Establishing shot"),
  WIDE("Wide shot"),
  MEDIUM("Medium shot"),
  CLOSE_UP("Close-up"),
  EXTREME_CLOSE_UP("Extreme close-up"),
  OVER_THE_SHOULDER("Over-the-shoulder"),
  LOW_ANGLE("Low angle"),
  HIGH_ANGLE("High angle"),
  DRONE_SHOT("Drone shot"),
  TRACKING_SHOT("Tracking shot")
}

enum class CameraMovement(val title: String) {
  PAN("Pan"),
  TILT("Tilt"),
  ZOOM("Zoom"),
  DOLLY("Dolly"),
  TRACKING("Tracking"),
  HANDHELD("Handheld"),
  STATIC("Static"),
  CRANE("Crane"),
  DRONE("Drone")
}

enum class AspectRatioFormat(val label: String, val ratioText: String, val width: Int, val height: Int) {
  YOUTUBE("YouTube", "16:9", 1920, 1080),
  YOUTUBE_SHORTS("YouTube Shorts", "9:16", 1080, 1920),
  TIKTOK("TikTok", "9:16", 1080, 1920),
  INSTAGRAM_REELS("Instagram Reels", "9:16", 1080, 1920),
  INSTA_POST("Instagram/Facebook", "1:1", 1080, 1080),
  CINEMATIC_ULTRAWIDE("Cinematic", "21:9", 2560, 1080)
}

enum class VoiceGender(val label: String) {
  MALE("Male"),
  FEMALE("Female"),
  YOUTHFUL("Youthful"),
  MATURE("Mature"),
  DEEP("Deep"),
  ENERGETIC("Energetic"),
  CALM("Calm"),
  PROFESSIONAL("Professional"),
  EMOTIONAL("Emotional")
}

enum class VoiceAccent(val label: String, val localeTag: String) {
  SOUTH_AFRICAN("South African English", "en-ZA"),
  NIGERIAN("Nigerian English", "en-NG"),
  BRITISH("British English", "en-GB"),
  AMERICAN("American English", "en-US"),
  AUSTRALIAN("Australian English", "en-AU"),
  INDIAN("Indian English", "en-IN"),
  NEUTRAL("Neutral English", "en")
}

enum class VoiceStyle(val label: String) {
  NORMAL("Normal"),
  EXCITED("Excited"),
  SERIOUS("Serious"),
  INSPIRATIONAL("Inspirational"),
  SAD("Sad"),
  DRAMATIC("Dramatic"),
  FRIENDLY("Friendly"),
  DOCUMENTARY("Documentary"),
  NEWS("News"),
  STORYTELLING("Storytelling")
}

enum class MusicCategory(val label: String, val defaultMood: String) {
  CINEMATIC("Cinematic", "Orchestral build"),
  INSPIRATIONAL("Inspirational", "Soft emotional piano building to crescendo"),
  CORPORATE("Corporate", "Modern upbeat tech pulse"),
  EMOTIONAL("Emotional", "Gentle acoustic strings & solo cello"),
  SUSPENSE("Suspense", "Dark ambient drones with ticking pulse"),
  ACTION("Action", "High-energy drums and brass hits"),
  HORROR("Horror", "Eerie strings and dissonant chords"),
  ROMANTIC("Romantic", "Warm piano melody and slow rhythm"),
  HIP_HOP("Hip Hop", "Boom bap groove with deep 808"),
  AFROBEATS("Afrobeats", "Uplifting log drums and acoustic guitar"),
  ELECTRONIC("Electronic", "Futuristic synthwave arpeggios"),
  AMBIENT("Ambient", "Calm atmospheric soundscape"),
  DOCUMENTARY("Documentary", "Thoughtful minimalist bells and pads"),
  COMEDY("Comedy", "Playful pizzicato strings and marimba")
}

enum class CaptionStyle(val label: String, val fontStyle: String, val defaultColor: Long, val defaultBg: Long) {
  YOUTUBE("YouTube", "Bold Sans", 0xFFFFFFFF, 0xCC000000),
  TIKTOK("TikTok", "Heavy Pop", 0xFFFFE600, 0x00000000),
  PROFESSIONAL("Professional", "Clean Sans", 0xFFE2E8F0, 0xAA0F172A),
  MINIMAL("Minimal", "Light Serif", 0xFFFFFFFF, 0x00000000),
  BOLD("Bold", "Ultra Heavy", 0xFFFF3366, 0xDD111827),
  KARAOKE("Karaoke", "Gradient Glow", 0xFF38BDF8, 0x88000000),
  CINEMATIC("Cinematic", "Elegant Tracking", 0xFFF1F5F9, 0x00000000)
}

enum class ExportResolution(val label: String, val description: String) {
  HD_720P("720p", "Fast generation, smaller file"),
  FHD_1080P("1080p", "Full HD cinematic clarity"),
  UHD_4K("4K Ultra HD", "Master studio sharpness")
}

enum class ExportQuality(val label: String) {
  STANDARD("Standard (Fast)"),
  HIGH("High Quality"),
  MAXIMUM("Maximum (Lossless Bitrate)")
}

enum class GenerationStep(val title: String, val description: String) {
  PREPARING_SCRIPT("Preparing Script", "Writing professional screenplay & emotional dialogue"),
  CREATING_CHARACTERS("Creating Characters", "Building locked appearance & facial profiles"),
  CREATING_STORYBOARD("Creating Storyboard", "Planning camera angles, shots & durations"),
  GENERATING_SCENES("Generating Scene Footage", "Rendering photorealistic cinematic visuals"),
  GENERATING_VOICE("Generating Voice-Over", "Synthesizing authentic accents & character dialogue"),
  ADDING_MUSIC("Adding Music & Ducking", "Selecting soundtrack & balancing audio ducking"),
  ADDING_SFX("Adding Sound Effects", "Layering realistic footsteps, city ambience & effects"),
  ADDING_CAPTIONS("Adding Auto Captions", "Syncing animated subtitles to vocal tracks"),
  FINAL_RENDER("Rendering Video", "Combining clips with transitions and color grade"),
  COMPLETED("Video Ready", "Ready to preview, export, or generate YouTube package"),
  FAILED("Generation Failed", "Encountered an issue")
}
