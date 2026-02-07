package com.rschao.boss_battle.api;

import java.util.List;

public class BossPhase {
    public final int id;
    public final String world;
    public final double x, y, z;
    public final String kit;
    public final String minionKit;
    public final List<String> dialogue;
    public final String music;
    public final List<Integer> souls;

    public BossPhase(int id, String world, double x, double y, double z,
                     String kit, String minionKit, List<String> dialogue,
                     String music, List<Integer> souls) {
        this.id = id;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.kit = kit;
        this.minionKit = minionKit;
        this.dialogue = dialogue;
        this.music = music;
        this.souls = souls;
    }
}
