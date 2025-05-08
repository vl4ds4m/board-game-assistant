package org.vl4ds4m.board.game.assistant.game.monopoly

import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.game.log.GameAction

fun monopolyPlayersCashChangedAction(
    senderId: PID,
    receiverId: PID,
    amount: Int
) = GameAction(
    type = MONOPOLY_CHANGE_PLAYERS_CASH,
    data = mapOf(
        MONOPOLY_SENDER_ID to senderId.toString(),
        MONOPOLY_RECEIVER_ID to receiverId.toString(),
        MONOPOLY_AMOUNT to amount.toString()
    )
)

val GameAction.changesMonopolyPlayersCash: Boolean
    get() = type == MONOPOLY_CHANGE_PLAYERS_CASH

val GameAction.senderId: PID?
    get() = data[MONOPOLY_SENDER_ID]?.toIntOrNull()

val GameAction.receiverId: PID?
    get() = data[MONOPOLY_RECEIVER_ID]?.toIntOrNull()

val GameAction.monopolyAmount: Int?
    get() = data[MONOPOLY_AMOUNT]?.toIntOrNull()

private const val MONOPOLY_CHANGE_PLAYERS_CASH = "monopoly_change_players_cash"
private const val MONOPOLY_SENDER_ID = "monopoly_sender_id"
private const val MONOPOLY_RECEIVER_ID = "monopoly_receiver_id"
private const val MONOPOLY_AMOUNT = "monopoly_cash_amount"
