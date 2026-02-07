package at.hannibal2.skyhanni.utils.i18n

/**
 * A collection of commonly used translation keys.
 * This provides a centralized place to define translation keys
 * and avoid magic strings throughout the codebase.
 */
object TranslationKeys {

    // SkyHanni prefixes
    const val PREFIX = "skyhanni.prefix"
    const val DEBUG_PREFIX = "skyhanni.debug.prefix"
    const val ERROR_PREFIX = "skyhanni.error.prefix"

    // Config categories
    object Config {
        object Category {
            const val ABOUT = "config.category.about"
            const val ABOUT_DESC = "config.category.about.desc"
            const val GUI = "config.category.gui"
            const val GUI_DESC = "config.category.gui.desc"
            const val GARDEN = "config.category.garden"
            const val GARDEN_DESC = "config.category.garden.desc"
            const val CRIMSON_ISLE = "config.category.crimsonIsle"
            const val CRIMSON_ISLE_DESC = "config.category.crimsonIsle.desc"
            const val RIFT = "config.category.rift"
            const val RIFT_DESC = "config.category.rift.desc"
            const val FISHING = "config.category.fishing"
            const val FISHING_DESC = "config.category.fishing.desc"
            const val MINING = "config.category.mining"
            const val MINING_DESC = "config.category.mining.desc"
            const val FORAGING = "config.category.foraging"
            const val FORAGING_DESC = "config.category.foraging.desc"
            const val HUNTING = "config.category.hunting"
            const val HUNTING_DESC = "config.category.hunting.desc"
            const val COMBAT = "config.category.combat"
            const val COMBAT_DESC = "config.category.combat.desc"
            const val SLAYER = "config.category.slayer"
            const val SLAYER_DESC = "config.category.slayer.desc"
            const val DUNGEON = "config.category.dungeon"
            const val DUNGEON_DESC = "config.category.dungeon.desc"
            const val INVENTORY = "config.category.inventory"
            const val INVENTORY_DESC = "config.category.inventory.desc"
            const val EVENTS = "config.category.events"
            const val EVENTS_DESC = "config.category.events.desc"
            const val SKILL_PROGRESS = "config.category.skillProgress"
            const val SKILL_PROGRESS_DESC = "config.category.skillProgress.desc"
            const val CHAT = "config.category.chat"
            const val CHAT_DESC = "config.category.chat.desc"
            const val MISC = "config.category.misc"
            const val MISC_DESC = "config.category.misc.desc"
            const val DEV = "config.category.dev"
            const val DEV_DESC = "config.category.dev.desc"
        }

        object Gui {
            const val LANGUAGE = "config.gui.language"
            const val LANGUAGE_DESC = "config.gui.language.desc"
        }

        object About {
            const val CURRENT_VERSION = "config.about.currentVersion"
            const val CURRENT_VERSION_DESC = "config.about.currentVersion.desc"
            const val CHECK_FOR_UPDATES = "config.about.checkForUpdates"
            const val CHECK_FOR_UPDATES_DESC = "config.about.checkForUpdates.desc"
            const val AUTO_UPDATES = "config.about.autoUpdates"
            const val AUTO_UPDATES_DESC = "config.about.autoUpdates.desc"
            const val UPDATE_STREAM = "config.about.updateStream"
            const val UPDATE_STREAM_DESC = "config.about.updateStream.desc"
        }
    }

    // Chat messages
    object Chat {
        const val USER_ERROR = "chat.userError"
        const val DEBUG = "chat.debug"
        const val INFO = "chat.info"
        const val WARNING = "chat.warning"
    }

    // Commands
    object Command {
        const val HELP = "command.help"
        const val CONFIG = "command.config"
        const val GUI = "command.gui"
        const val RELOAD = "command.reload"
    }

    // Messages
    object Message {
        const val LANGUAGE_CHANGED = "message.languageChanged"
        const val LANGUAGE_RELOAD_REQUIRED = "message.languageReloadRequired"
    }
}
