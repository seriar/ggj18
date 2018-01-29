package com.sergii.fgjx.sb.client.world;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.sergii.fgjx.sb.client.io.KeyCallback;
import com.sergii.fgjx.sb.client.io.KeyboardControl;
import org.lwjgl.glfw.GLFW;

import java.util.Map;
import java.util.Set;

public class MenuController {

    private GameState state;
    private final KeyboardControl control;
    private final World world;

    private Map<GameState, Set<KeyCallback>> keyMappings;

    public MenuController(World world) {
        state = GameState.INTRO;
        this.control = world.getKeyboardControl();
        this.world = world;
        generateKeyMap(world);
        unregisterKeys(state);
        registerKeys(state);
    }

    private void generateKeyMap(World world) {
        keyMappings = ImmutableMap.<GameState, Set<KeyCallback>>builder()
        .put(GameState.INTRO, ImmutableSet.<KeyCallback>of(
                new SimpleMenuButton(GLFW.GLFW_KEY_SPACE, GLFW.GLFW_PRESS, world, GameState.ANY)))
        .put(GameState.MAIN_MENU, ImmutableSet.<KeyCallback>of(
                new RequestMenuButton(GLFW.GLFW_KEY_1, GLFW.GLFW_PRESS, world, GameState.LOBBY),
                new SimpleMenuButton(GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_PRESS, world, GameState.EXIT)
                ))
        .put(GameState.LOBBY, ImmutableSet.<KeyCallback>of(
                new RequestMenuButton(GLFW.GLFW_KEY_1, GLFW.GLFW_PRESS, world, GameState.SESSIONS),
                new SimpleMenuButton(GLFW.GLFW_KEY_2, GLFW.GLFW_PRESS, world, GameState.SESSION_WAITING),
                new SimpleMenuButton(GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_PRESS, world, GameState.MAIN_MENU)
                ))
        .put(GameState.SESSIONS, ImmutableSet.<KeyCallback>of(
                new JoinRequestMenuButton(GLFW.GLFW_KEY_1, GLFW.GLFW_PRESS, world, GameState.SESSION_WAITING, 0),
                new JoinRequestMenuButton(GLFW.GLFW_KEY_2, GLFW.GLFW_PRESS, world, GameState.SESSION_WAITING, 1),
                new JoinRequestMenuButton(GLFW.GLFW_KEY_3, GLFW.GLFW_PRESS, world, GameState.SESSION_WAITING, 2),
                new JoinRequestMenuButton(GLFW.GLFW_KEY_4, GLFW.GLFW_PRESS, world, GameState.SESSION_WAITING, 3),
                new SimpleMenuButton(GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_PRESS, world, GameState.LOBBY)
                ))
        .put(GameState.SESSION_WAITING, ImmutableSet.<KeyCallback>of(
                new SimpleMenuButton(GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_PRESS, world, GameState.LOBBY),
                new SimpleMenuButton(GLFW.GLFW_KEY_SPACE, GLFW.GLFW_PRESS, world, GameState.ROUND_INTRO)
                ))
        .put(GameState.ROUND_INTRO, ImmutableSet.<KeyCallback>of(
                new SimpleMenuButton(GLFW.GLFW_KEY_SPACE, GLFW.GLFW_PRESS, world, GameState.SELECT_WEAPON)
                ))
        .put(GameState.SELECT_WEAPON, ImmutableSet.<KeyCallback>of(
                new WeaponRequestMenuButton(GLFW.GLFW_KEY_1, GLFW.GLFW_PRESS, world, GameState.ATTACK_TRANSMISSION, 1),
                new WeaponRequestMenuButton(GLFW.GLFW_KEY_2, GLFW.GLFW_PRESS, world, GameState.ATTACK_TRANSMISSION, 2),
                new WeaponRequestMenuButton(GLFW.GLFW_KEY_3, GLFW.GLFW_PRESS, world, GameState.ATTACK_TRANSMISSION, 3)
                ))
        .put(GameState.ATTACK_TRANSMISSION, ImmutableSet.<KeyCallback>of(
                new TransmissionButtonLessButton(GLFW.GLFW_KEY_SPACE, GLFW.GLFW_PRESS, world, GameState.ATTACK)
                ))
        .put(GameState.ATTACK, ImmutableSet.<KeyCallback>of(
                new SendCodeButton(GLFW.GLFW_KEY_SPACE, GLFW.GLFW_PRESS, world, GameState.DEFEND_TRANSMISSION),
                new AddCharButton(world)
                ))
        .put(GameState.DEFEND_TRANSMISSION, ImmutableSet.<KeyCallback>of(
                new TransmissionButtonLessButton(GLFW.GLFW_KEY_SPACE, GLFW.GLFW_PRESS, world, GameState.DEFEND)
                ))
        .put(GameState.DEFEND, ImmutableSet.<KeyCallback>of(
                new SendCodeButton(GLFW.GLFW_KEY_SPACE, GLFW.GLFW_PRESS, world, GameState.ROUND_OUTRO),
                new AddCharButton(world)
        ))
        .put(GameState.ROUND_OUTRO, ImmutableSet.<KeyCallback>of(
                new SimpleMenuButton(GLFW.GLFW_KEY_SPACE, GLFW.GLFW_PRESS, world, GameState.ROUND_INTRO),
                new SimpleMenuButton(GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_PRESS, world, GameState.GAME_OVER)
                ))
        .put(GameState.GAME_OVER, ImmutableSet.<KeyCallback>of(
                new SimpleMenuButton(GLFW.GLFW_KEY_SPACE, GLFW.GLFW_PRESS, world, GameState.MAIN_MENU)
                ))
        .put(GameState.VICTORY, ImmutableSet.<KeyCallback>of(
                new SimpleMenuButton(GLFW.GLFW_KEY_SPACE, GLFW.GLFW_PRESS, world, GameState.MAIN_MENU)
                ))
        .build();
    }

    public void updateState(GameState state) {
        if (state != this.state) {
            unregisterKeys(this.state);
            registerKeys(state);
            this.state = state;
        }
    }

    private void unregisterKeys(GameState state) {
        Set<KeyCallback> val = keyMappings.getOrDefault(state, ImmutableSet.of());
        val.forEach(control::unregisterCallback);
    }

    private void registerKeys(GameState state) {
        keyMappings.getOrDefault(state, ImmutableSet.of())
                .forEach(control::registerCallback);
        keyMappings.getOrDefault(state, ImmutableSet.of())
                .stream().filter(p -> p instanceof ButtonLessButton)
                .forEach(b -> ((ButtonLessButton)b).trigger());
    }

}
