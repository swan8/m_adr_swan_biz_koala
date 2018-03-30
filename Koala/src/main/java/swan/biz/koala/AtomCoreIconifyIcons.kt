package swan.biz.koala

import com.joanzapata.iconify.Icon

/**
 * Created by stephen on 18-3-30.
 */
enum class AtomCoreIconifyIcons constructor(character: Char) : Icon {

    ATOM_CORE_BACK('\ue697'),

    ATOM_CORE_CATEGORY('\ue699'),

    ;

    var character: Char = 0.toChar()

    init {
        this.character = character
    }

    override fun key(): String {
        return name.replace('_', '-')
    }

    override fun character(): Char {
        return character
    }
}