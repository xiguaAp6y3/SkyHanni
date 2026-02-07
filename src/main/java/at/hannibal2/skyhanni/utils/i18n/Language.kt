package at.hannibal2.skyhanni.utils.i18n

/**
 * Supported languages for SkyHanni.
 * Maps to Minecraft language codes (lowercase format like "en_us", "zh_cn").
 */
enum class Language(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val mcCodes: List<String>, // Minecraft language codes that map to this language
) {
    ENGLISH("en_US", "English", "English", listOf("en_us", "en_gb", "en_au", "en_nz", "en_ca")),
    CHINESE_SIMPLIFIED("zh_CN", "Chinese (Simplified)", "简体中文", listOf("zh_cn")),
    CHINESE_TRADITIONAL("zh_TW", "Chinese (Traditional)", "繁體中文", listOf("zh_tw", "zh_hk"));

    companion object {
        fun fromCode(code: String): Language {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }

        /**
         * Map a Minecraft language code (e.g., "zh_cn") to our Language enum.
         * Falls back to ENGLISH if no match found.
         */
        fun fromMinecraftCode(mcCode: String): Language {
            val lowerCode = mcCode.lowercase()
            return entries.find { language ->
                language.mcCodes.any { it.equals(lowerCode, ignoreCase = true) }
            } ?: ENGLISH
        }
    }

    override fun toString(): String = nativeName
}
