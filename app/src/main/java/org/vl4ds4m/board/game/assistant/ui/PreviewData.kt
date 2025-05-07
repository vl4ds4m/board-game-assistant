package org.vl4ds4m.board.game.assistant.ui

import org.vl4ds4m.board.game.assistant.States
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.currentPlayerChangedAction
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.playerStateChangedAction

private val initialTime: Long = System.currentTimeMillis()

private const val day: Long = 24 * 60 * 60 * 1000L

private val loop = 401 .. 404

val detailedGameSessionPreview = GameSession(
    completed = false,
    type = SimpleOrdered,
    name = "Party 93",
    players = listOf(
        145 to Player(
            name = "Abc",
            state = PlayerState(120, mapOf())
        ),
        22 to Player(
            name = "Def",
            presence = Player.Presence.FROZEN,
            state = PlayerState(36, mapOf())
        ),
        78 to Player(
            name = "Yummy",
            state = PlayerState(83, mapOf())
        ),
        365 to Player(
            name = "Foo",
            state = PlayerState(154, mapOf())
        ),
        65 to Player(
            name = "Abrewsdf",
            presence = Player.Presence.REMOVED,
            state = PlayerState(56567, mapOf())
        )
    ) + buildList {
        for ((i, id) in loop.withIndex()) {
            val p = id to Player(
                name = "Looper ${i + 1}",
                state = PlayerState.Empty
            )
            add(p)
        }
    },
    users = mapOf(
        22 to User(
            netDevId = "09gkndnl32n",
            self = false,
            name = "Definitive"
        ),
        78 to User(
            netDevId = "idjfgsp9dfisd",
            self = true,
            name = "Dev 34"
        ),
        365 to User(
            netDevId = "n54lisf",
            self = false,
            name = "FootPrint"
        )
    ),
    currentPid = 22,
    nextNewPid = 506,
    startTime = initialTime + 40_000 - day,
    stopTime = initialTime + 45_000 - day,
    duration = 2_000,
    timeout = true,
    secondsUntilEnd = 157,
    actions = buildList {
        for (id in loop) {
            val a = currentPlayerChangedAction(
                ids = States(
                    prev = id,
                    next = if (id == loop.last) 145 else id + 1
                )
            )
            add(a)
        }
    } + listOf(
        playerStateChangedAction(
            id = 145,
            states = States(
                prev = PlayerState(73, mapOf()),
                next = PlayerState(120, mapOf())
            )
        ),
        currentPlayerChangedAction(
            ids = States(
                prev = 145,
                next = 22
            )
        )
    ),
    currentActionPosition = 0
)

val gameSessionsPreview: List<GameSession> = listOf(
    GameSession(
        completed = false,
        type = Free,
        name = "Poker Counts 28",
        players = listOf(
            1 to Player(
                name = "Bar",
                state = PlayerState(1220, mapOf())
            ),
            2 to Player(
                name = "Conf",
                state = PlayerState(376, mapOf())
            ),
            3 to Player(
                name = "Leak",
                state = PlayerState(532, mapOf())
            )
        ),
        users = mapOf(),
        currentPid = 2,
        nextNewPid = 10,
        startTime = initialTime + 20_000,
        stopTime = initialTime + 35_000,
        duration = 15_000,
        timeout = false,
        secondsUntilEnd = 0,
        actions = listOf(),
        currentActionPosition = 0
    ),
    GameSession(
        type = Free,
        completed = true,
        name = "Fast 65",
        players = listOf(
            1 to Player(
                name = "Opd 34",
                state = PlayerState(12, mapOf())
            ),
            2 to Player(
                name = "Noid oi Els",
                presence = Player.Presence.REMOVED,
                state = PlayerState(537, mapOf())
            ),
            3 to Player(
                name = "Leak",
                state = PlayerState(353, mapOf())
            ),
            4 to Player(
                name = "Ao sdf",
                state = PlayerState(7632, mapOf())
            )
        ),
        users = mapOf(
            3 to User(
                netDevId = "sdf",
                self = true,
                name = "Tio 89"
            )
        ),
        currentPid = 2,
        nextNewPid = 7,
        startTime = initialTime + 30_000 - 5 * day,
        stopTime = initialTime + 40_000 - 5 * day,
        duration = 6_340,
        timeout = false,
        secondsUntilEnd = 0,
        actions = listOf(),
        currentActionPosition = 0
    ),
    detailedGameSessionPreview,
    GameSession(
        type = SimpleOrdered,
        completed = true,
        name = "Imaginarium 74",
        players = listOf(
            1 to Player(
                name = "Bar",
                state = PlayerState(12, mapOf())
            ),
            2 to Player(
                name = "Conf",
                presence = Player.Presence.REMOVED,
                state = PlayerState(37, mapOf())
            ),
            3 to Player(
                name = "Leak",
                state = PlayerState(53, mapOf())
            ),
            4 to Player(
                name = "Flick",
                state = PlayerState(32, mapOf())
            )
        ),
        users = mapOf(),
        currentPid = 3,
        nextNewPid = 10,
        startTime = initialTime + 30_000 - day,
        stopTime = initialTime + 40_000 - day,
        duration = 4_000,
        timeout = false,
        secondsUntilEnd = 0,
        actions = listOf(),
        currentActionPosition = 0
    )
)

val sessionsInfoPreview: List<GameSessionInfo> = gameSessionsPreview
    .mapIndexed { i, s ->
        GameSessionInfo(
            id = i.inc().toString(),
            completed = s.completed,
            type = s.type,
            name = s.name,
            startTime = s.startTime,
            stopTime = s.stopTime
        )
    }
