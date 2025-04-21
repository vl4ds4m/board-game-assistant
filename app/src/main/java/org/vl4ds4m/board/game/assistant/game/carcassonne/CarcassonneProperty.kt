package org.vl4ds4m.board.game.assistant.game.carcassonne

import androidx.annotation.StringRes
import org.vl4ds4m.board.game.assistant.R

enum class CarcassonneProperty(
    @get:StringRes
    val localizedStringRes: Int
) {
    CITY(R.string.game_carcassonne_property_city),
    FIELD(R.string.game_carcassonne_property_field),
    CLOISTER(R.string.game_carcassonne_property_cloister),
    ROAD(R.string.game_carcassonne_property_road)
}
