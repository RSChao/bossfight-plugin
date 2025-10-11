package com.rschao.boss_battle;


public class BossPlayerInfo {
    public BossPlayerInfo(String name, int health, int[] souls, String[] fruits) {
        this.name = name;
        this.health = health;
        this.souls = souls;
        this.fruits = fruits;
    }
    public String name;
    public int health;
    public int[] souls = new int[2];
    public String[] fruits = new String[2];
}
