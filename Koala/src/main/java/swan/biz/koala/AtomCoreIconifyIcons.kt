package swan.biz.koala

import com.joanzapata.iconify.Icon

/**
 * Created by stephen on 18-3-30.
 */
enum class AtomCoreIconifyIcons constructor(character: Char) : Icon {

    ATOM_CORE_BACK('\ue697'),

    ATOM_CORE_CATEGORY('\ue699'),

    ATOM_CORE_VIEW_CATEGORY('\ue6b4'),

    ATOM_CORE_SHARE('\ue71d'),

    ATOM_CORE_DOWNLOAD('\ue714'),

    ATOM_CORE_FAVORITE_HEART('\ue7ce'),

    ATOM_CORE_CLOCK('\ue6bb'),

    ;

    var character: Char = 0.toChar()

    init {
        this.character = character
    }

    override fun key(): String {
//        return name.replace('_', '-')
        return name
    }

    override fun character(): Char {
        return character
    }
}