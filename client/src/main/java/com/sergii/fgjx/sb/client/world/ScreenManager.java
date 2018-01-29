package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.client.rendering.Shader;

public class ScreenManager {

    private final MenuScreen introScreen;
    private final MainMenuScreen mainMenuScreen;
    private final LobbyMenuScreen lobbyScreen;
    private final SessionsMenuScreen sessionsScreen;
    private final WaitingRoomScreen waitingRoomScreen;
    private final MenuScreen roundIntroScreen;
    private final MenuScreen roundOutroScreen;
    private final MenuScreen victoryScreen;
    private final MenuScreen gameOverScreen;
    private final BattleScreen battleScreen;

    private final MenuController menuController;

//@foramtter:off


    private static final String INTRO_TEXT =
            " -+ SPC BRT0 +- " +
            "                " +
            " > Launch the   " +
            "   torpedoes!!! " +
            "                " +
            " Wait. what?! < " +
            "  Why should we " +
            " touch the bur- " +
            " ritos???       " +
            "                " +
            " Whatever. An < " +
            " order is an    " +
            " I guess        ";

    private static final String MAIN_MENU =
            " -+ SPC BRT0 +- " +
            "                " +
            "   Main  Menu   " +
            "================" +
            "1. Join Lobby   " +
            "                " +
            "                " +
            "                " +
            "ESC. Exit         " ;

    private static final String LOBBY_MENU_TEXT =
            " -+ SPC BRT0 +- " +
            "                " +
            "      Lobby     " +
            "================" +
            "1. Join Game    " +
            "  Games: %1$5s  " +
            "ESC. Main menu    ";
    private static final String SESSIONS_TEXT =
            " -+ SPC BRT0 +- " +
            "                " +
            "  Active Games  " +
            "================" +

            "%1$11s     " +
            "%2$11s     " +
            "%3$11s     " +
            "%4$11s     " +

            "ESC. to Lobby   " ;

    private static final String WAITING_SCREEN =
            " -+ SPC BRT0 +- " +
            "                " +
            "  Waiting room  " +
            "================" +
            "    Players:    " +

            "%1$11s     " +
            "%2$11s     " +
            "%3$11s     " +
            "%4$11s     ";

    private static final String ROUND_INTRO =
            " -+ SPC BRT0 +- " +
            "                " +
            "+--------------+" +
            "| ROUND  %1$5s |" +
            "+--------------+" +
            "     fight!     ";
    private static final String ROUND_OUTRO =
            " -+ SPC BRT0 +- " +
            "                " +
            " +------------+ " +
            " |  ROUND  1  | " +
            " +------------+ " +
            " Statistics:    " +
            " Damage dealt:  " +
            "   435343       " +
            " Damage endured:" +
            "   231          " +
            " ...            ";
    private static final String VICTORY_TEXT =
            " -+ SPC BRT0 +- " +
            "                " +
            " +------------+ " +
            " |  VICTORY   | " +
            " +------------+ " +
            "                " +
            " You've managed " +
            " to destroy ene-" +
            " my armada. Home" +
            " is safe now! ";
    private static final String GAME_OVER_1 =
            " -+ SPC BRT0 +- " +
            "                " +
            " +------------+ " +
            " | GAME  OVER | " +
            " +------------+ " +
            "                " +
            " Ouf fleet is   " +
            " virtually dest-" +
            " royed. Home is " +
            " doomed to slav-" +
            " ery and destru-" +
            " ction..." ;

    private static final String GAME_OVER_2 =
            " -+ SPC BRT0 +- " +
            "                " +
            " +------------+ " +
            " | GAME  OVER | " +
            " +------------+ " +
            "                " +
            " White peace was" +
            " signed. You per" +
            " sonally are hel" +
            " d responsible f" +
            " or dishonor.   " ;
//@foramtter:on

    public ScreenManager(World world) {
        introScreen = new MenuScreen(INTRO_TEXT);
        lobbyScreen = new LobbyMenuScreen(LOBBY_MENU_TEXT);
        sessionsScreen = new SessionsMenuScreen(SESSIONS_TEXT);
        waitingRoomScreen = new WaitingRoomScreen(WAITING_SCREEN, world);
        roundIntroScreen = new RoundIntroScreen(ROUND_INTRO, world);
        roundOutroScreen = new MenuScreen(ROUND_OUTRO);
        victoryScreen = new MenuScreen(VICTORY_TEXT);
        gameOverScreen = new MenuScreen(GAME_OVER_1);

        mainMenuScreen = new MainMenuScreen(MAIN_MENU, world);

        battleScreen = new BattleScreen();

        menuController = new MenuController(world);
    }

    public void updateSessions(String...sessions) {
        sessionsScreen.updateScreen(sessions);
        lobbyScreen.updateScreen(sessions.length);
    }

    public void updatePlayers(String...players) {
        waitingRoomScreen.updateScreen(players);
    }

    public void update(float delta, GameState state) {
        menuController.updateState(state);
        switch (state) {

            case INTRO:
                introScreen.update(delta);
                break;
            case MAIN_MENU:
                mainMenuScreen.update(delta);
                break;
            case LOBBY:
                lobbyScreen.update(delta);
                break;
            case SESSIONS:
                sessionsScreen.update(delta);
                break;
            case SESSION_WAITING:
                waitingRoomScreen.update(delta);
                break;
            case ROUND_INTRO:
                roundIntroScreen.update(delta);
                break;
            case SELECT_WEAPON:
                battleScreen.update(delta);
                break;
            case ATTACK:
                battleScreen.update(delta);
                break;
            case DEFEND:
                battleScreen.update(delta);
                break;
            case MANEUVER:
                battleScreen.update(delta);
                break;
            case ROUND_OUTRO:
                roundOutroScreen.update(delta);
                break;
            case VICTORY:
                victoryScreen.update(delta);
                break;
            case GAME_OVER:
                gameOverScreen.update(delta);
                break;
        }
    }

    public void render(Shader shader, GameState state) {
        switch (state) {

            case INTRO:
                introScreen.render(shader);
                break;
            case MAIN_MENU:
                mainMenuScreen.render(shader);
                break;
            case LOBBY:
                lobbyScreen.render(shader);
                break;
            case SESSIONS:
                sessionsScreen.render(shader);
                break;
            case SESSION_WAITING:
                waitingRoomScreen.render(shader);
                break;
            case ROUND_INTRO:
                roundIntroScreen.render(shader);
                break;
            case SELECT_WEAPON:
                battleScreen.renderWeapon(shader);
                break;
            case ATTACK_TRANSMISSION:
                battleScreen.renderAttackTransmission(shader);
                break;
            case ATTACK:
                battleScreen.renderAttack(shader);
                break;
            case DEFEND:
                battleScreen.renderDefenceTransmission(shader);
                break;
            case DEFEND_TRANSMISSION:
                battleScreen.renderDefence(shader);
                break;
            case MANEUVER:
                battleScreen.render(shader);
                break;
            case ROUND_OUTRO:
                roundOutroScreen.render(shader);
                break;
            case VICTORY:
                victoryScreen.render(shader);
                break;
            case GAME_OVER:
                gameOverScreen.render(shader);
                break;
        }
    }

}
