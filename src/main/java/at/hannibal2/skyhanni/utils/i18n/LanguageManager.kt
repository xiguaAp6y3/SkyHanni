package at.hannibal2.skyhanni.utils.i18n

import at.hannibal2.skyhanni.api.event.HandleEvent
import at.hannibal2.skyhanni.SkyHanniMod
import at.hannibal2.skyhanni.config.commands.CommandCategory
import at.hannibal2.skyhanni.config.commands.CommandRegistrationEvent
import at.hannibal2.skyhanni.events.utils.PreInitFinishedEvent
import at.hannibal2.skyhanni.skyhannimodule.SkyHanniModule
import at.hannibal2.skyhanni.test.command.ErrorManager
import at.hannibal2.skyhanni.utils.ChatUtils
import at.hannibal2.skyhanni.utils.LorenzLogger
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.minecraft.client.Minecraft
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * Manages internationalization (i18n) for SkyHanni.
 * Provides translation functionality with fallback to English.
 * Language is automatically determined from Minecraft's language setting.
 */
@SkyHanniModule
object LanguageManager {

    private val logger = LorenzLogger("language_manager")
    private val gson = Gson()

    private var translations: MutableMap<String, String> = mutableMapOf()
    private var fallbackTranslations: MutableMap<String, String> = mutableMapOf()

    private var currentLanguage: Language = Language.ENGLISH
    private var initialized = false
    private var lastMcLanguage: String = ""

    /**
     * Initialize the language manager.
     * Loads saved language from config first for instant translations,
     * then syncs with Minecraft's current language setting.
     */
    @HandleEvent
    fun onPreInit(event: PreInitFinishedEvent) {
        loadFallbackTranslations()
        loadSavedLanguage()
        initialized = true
        logger.log("Language manager initialized with language: ${currentLanguage.code}")
    }

    /**
     * Get the current language.
     */
    fun getCurrentLanguage(): Language = currentLanguage

    /**
     * Load language from saved config for instant startup translations.
     * Falls back to detecting from Minecraft if no saved value.
     */
    private fun loadSavedLanguage() {
        val saved = SkyHanniMod.feature.gui.savedLanguageCode
        if (saved.isNotEmpty()) {
            currentLanguage = Language.fromCode(saved)
            lastMcLanguage = saved
            loadTranslationsForCurrentLanguage()
            logger.log("Loaded saved language from config: ${currentLanguage.code}")
        } else {
            detectAndLoadLanguage()
        }
    }

    /**
     * Save the current language code to config for persistence.
     */
    private fun saveLanguageToConfig() {
        SkyHanniMod.feature.gui.savedLanguageCode = currentLanguage.code
    }

    /**
     * Detect language from Minecraft settings and load translations.
     * Saves the result to config for next startup.
     * @return true if language changed, false otherwise.
     */
    fun detectAndLoadLanguage(): Boolean {
        val mcLanguage = getMinecraftLanguage()
        if (mcLanguage == lastMcLanguage && initialized) return false

        lastMcLanguage = mcLanguage
        currentLanguage = Language.fromMinecraftCode(mcLanguage)
        loadTranslationsForCurrentLanguage()
        saveLanguageToConfig()
        logger.log("Language detected from MC settings: $mcLanguage -> ${currentLanguage.code}")
        return true
    }

    /**
     * Called when the config GUI is about to open.
     * Syncs language with Minecraft settings and refreshes the GUI if needed.
     */
    fun syncOnConfigOpen() {
        if (!initialized) return
        if (detectAndLoadLanguage()) {
            refreshConfigGui()
        }
    }

    /**
     * Get Minecraft's current language setting.
     */
    private fun getMinecraftLanguage(): String {
        return try {
            Minecraft.getInstance().options.languageCode
        } catch (e: Exception) {
            ErrorManager.logErrorWithData(e, "Failed to get Minecraft language setting")
            "en_us"
        }
    }

    /**
     * Load the fallback (English) translations.
     */
    private fun loadFallbackTranslations() {
        fallbackTranslations = loadTranslationFile(Language.ENGLISH)
    }

    /**
     * Load translations for the current language.
     */
    private fun loadTranslationsForCurrentLanguage() {
        translations = if (currentLanguage == Language.ENGLISH) {
            fallbackTranslations.toMutableMap()
        } else {
            loadTranslationFile(currentLanguage)
        }
    }

    /**
     * Load a translation file for the given language.
     */
    private fun loadTranslationFile(language: Language): MutableMap<String, String> {
        val resourcePath = "/assets/skyhanni/lang/${language.code}.json"
        val result = mutableMapOf<String, String>()

        try {
            val resourceStream = LanguageManager::class.java.getResourceAsStream(resourcePath)
            if (resourceStream != null) {
                InputStreamReader(resourceStream, StandardCharsets.UTF_8).use { reader ->
                    val type = object : TypeToken<Map<String, String>>() {}.type
                    val loaded: Map<String, String> = gson.fromJson(reader, type)
                    result.putAll(loaded)
                }
                logger.log("Loaded ${result.size} translations for ${language.code}")
            } else {
                logger.log("Translation file not found: $resourcePath")
            }
        } catch (e: Exception) {
            ErrorManager.logErrorWithData(
                e,
                "Failed to load translation file",
                "resourcePath" to resourcePath,
                "language" to language.code,
            )
        }

        return result
    }

    /**
     * Get a translated string for the given key.
     * Falls back to English if key is not found in current language.
     * Falls back to the key itself if not found in any language.
     *
     * @param key The translation key
     * @param args Optional arguments for string formatting
     * @return The translated string
     */
    fun translate(key: String, vararg args: Any): String {
        val template = translations[key]
            ?: fallbackTranslations[key]
            ?: return key

        return if (args.isEmpty()) {
            template
        } else {
            try {
                String.format(template, *args)
            } catch (e: Exception) {
                ErrorManager.logErrorWithData(
                    e,
                    "Failed to format translation string",
                    "key" to key,
                    "template" to template,
                )
                template
            }
        }
    }

    /**
     * Check if a translation key exists.
     */
    fun hasKey(key: String): Boolean {
        return translations.containsKey(key) || fallbackTranslations.containsKey(key)
    }

    /**
     * Get all translation keys.
     */
    fun getAllKeys(): Set<String> {
        return translations.keys + fallbackTranslations.keys
    }

    /**
     * Reload translations for the current language.
     */
    fun reload() {
        loadFallbackTranslations()
        lastMcLanguage = "" // Force re-detection
        detectAndLoadLanguage()
        refreshConfigGui()
        logger.log("Translations reloaded")
    }

    /**
     * Sync language with Minecraft settings and refresh.
     */
    fun syncLanguage() {
        lastMcLanguage = "" // Force re-detection
        detectAndLoadLanguage()
        refreshConfigGui()
    }

    /**
     * Recreate the config GUI to apply translated text.
     */
    private fun refreshConfigGui() {
        if (!initialized) return
        try {
            SkyHanniMod.configManager.recreateConfig()
        } catch (e: Exception) {
            ErrorManager.logErrorWithData(e, "Failed to refresh config GUI after language change")
        }
    }

    /**
     * Register language-related commands.
     */
    @HandleEvent
    fun onCommandRegistration(event: CommandRegistrationEvent) {
        event.registerBrigadier("shlang") {
            description = "Show SkyHanni language info / 显示SkyHanni语言信息"
            category = CommandCategory.USERS_ACTIVE
            aliases = listOf("shlanguage")

            simpleCallback {
                val mcLang = getMinecraftLanguage()
                ChatUtils.chat("§e" + translate("command.shlang.header"))
                ChatUtils.chat("§7  " + translate("command.shlang.current", currentLanguage.nativeName, currentLanguage.code))
                ChatUtils.chat("§7  " + translate("command.shlang.mcLanguage", mcLang))
                ChatUtils.chat("§7  " + translate("command.shlang.followsMc"))
                ChatUtils.chat("§e" + translate("command.shlang.usage"))
            }

            literal("reload") {
                simpleCallback {
                    reload()
                    ChatUtils.chat("§a" + translate("command.shlang.reloaded"))
                    ChatUtils.chat("§7" + translate("command.shlang.currentLang", currentLanguage.nativeName))
                }
            }

            literal("sync") {
                simpleCallback {
                    syncLanguage()
                    ChatUtils.chat("§a" + translate("command.shlang.synced"))
                    ChatUtils.chat("§7" + translate("command.shlang.currentLang", currentLanguage.nativeName))
                }
            }
        }
    }
}
