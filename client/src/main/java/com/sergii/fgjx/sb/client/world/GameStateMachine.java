package com.sergii.fgjx.sb.client.world;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameStateMachine {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private GameState state;

    public GameStateMachine() {
        this.state = GameState.INTRO;
    }

    public GameState getState() {
        return state;
    }

    public void nextState() {
        nextState(GameState.ANY);
    }

    public void win() {
        state = GameState.VICTORY;
    }
    public void lost() {
        state = GameState.GAME_OVER;
    }
    public void nextState(GameState next) {
        switch (state) {

            case INTRO:
                state = GameState.MAIN_MENU;
                break;
            case MAIN_MENU:
                switch (next) {
                    case LOBBY:
                        state = GameState.LOBBY;
                        break;
                    case EXIT:
                        state = GameState.EXIT;
                        break;
                    default:
                        break;
                }
                break;
            case LOBBY:
                state = GameState.SESSIONS;
                switch (next) {
                    case MAIN_MENU:
                        state = GameState.MAIN_MENU;
                        break;
                    case SESSIONS:
                        state = GameState.SESSIONS;
                        break;
                    case SESSION_WAITING:
                        state = GameState.SESSION_WAITING;
                        break;
                    default:
                        break;
                }
                break;
            case SESSIONS:
                switch (next) {
                    case SESSION_WAITING:
                        state = GameState.SESSION_WAITING;
                        break;
                    case LOBBY:
                        state = GameState.LOBBY;
                        break;
                    default:
                }
                break;
            case SESSION_WAITING:
                switch (next) {
                    case ROUND_INTRO:
                        state = GameState.ROUND_INTRO;
                        break;
                    case LOBBY:
                        state = GameState.LOBBY;
                        break;
                    default:
                }
                break;
            case ROUND_INTRO:
                switch (next) {
                    case ROUND_OUTRO:
                        state = GameState.ROUND_OUTRO;
                        break;
                    case SELECT_WEAPON:
                        state = GameState.SELECT_WEAPON;
                        break;
                    case ATTACK:
                        state = GameState.ATTACK;
                        break;
                    default:
                }
                break;
            case SELECT_WEAPON:
                switch (next) {
                    case ROUND_OUTRO:
                        state = GameState.ROUND_OUTRO;
                        break;
                    case ATTACK_TRANSMISSION:
                        state = GameState.ATTACK_TRANSMISSION;
                        break;
                    case SELECT_WEAPON:
                        state = GameState.SELECT_WEAPON;
                        break;
                    default:
                }
                break;
            case ATTACK_TRANSMISSION:
                switch (next) {
                    case ATTACK:
                        state = GameState.ATTACK;
                        break;
                    default:
                }
                break;
            case DEFEND_TRANSMISSION:
                switch (next) {
                    case DEFEND:
                        state = GameState.DEFEND;
                        break;
                    default:
                }
                break;
            case ATTACK:
                switch (next) {
                    case DEFEND_TRANSMISSION:
                        state = GameState.DEFEND_TRANSMISSION;
                        break;
                    case SELECT_WEAPON:
                        state = GameState.SELECT_WEAPON;
                        break;
                    default:
                }
                break;
            case DEFEND:
                switch (next) {
                    case ROUND_OUTRO:
                        state = GameState.ROUND_OUTRO;
                        break;
                    default:
                }
                break;
            case MANEUVER:
                break;
            case ROUND_OUTRO:
                switch (next) {
                    case VICTORY:
                        state = GameState.VICTORY;
                        break;
                    case GAME_OVER:
                        state = GameState.GAME_OVER;
                        break;
                    default:
                }
                break;
            case VICTORY:
                state = GameState.MAIN_MENU;
                break;
            case GAME_OVER:
                state = GameState.MAIN_MENU;
                break;
        }
    }
}
