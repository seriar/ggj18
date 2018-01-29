package com.sergii.fgjx.sb.api.messages.responses;

public class ReportResponse extends Response {
    private final boolean victory;
    private final int health;
    private final int teamHealth;
    private final int ensdfemyHealth;
    private final int attackPerformance;
    private final int teamAttackPerformance;
    private final int enemyAttackPerformance;
    private final int defencePerformance;
    private final int teamDefencePerformance;
    private final int enemyDefencePerformance;

    public ReportResponse(String id, boolean victory, int health, int teamHealth, int ensdfemyHealth,
                          int attackPerformance, int teamAttackPerformance, int enemyAttackPerformance,
                          int defencePerformance, int teamDefencePerformance, int enemyDefencePerformance) {
        super(id);
        this.victory = victory;
        this.health = health;
        this.teamHealth = teamHealth;
        this.ensdfemyHealth = ensdfemyHealth;
        this.attackPerformance = attackPerformance;
        this.teamAttackPerformance = teamAttackPerformance;
        this.enemyAttackPerformance = enemyAttackPerformance;
        this.defencePerformance = defencePerformance;
        this.teamDefencePerformance = teamDefencePerformance;
        this.enemyDefencePerformance = enemyDefencePerformance;
    }

    public boolean isVictory() {
        return victory;
    }

    public int getHealth() {
        return health;
    }

    public int getTeamHealth() {
        return teamHealth;
    }

    public int getEnsdfemyHealth() {
        return ensdfemyHealth;
    }

    public int getAttackPerformance() {
        return attackPerformance;
    }

    public int getTeamAttackPerformance() {
        return teamAttackPerformance;
    }

    public int getEnemyAttackPerformance() {
        return enemyAttackPerformance;
    }

    public int getDefencePerformance() {
        return defencePerformance;
    }

    public int getTeamDefencePerformance() {
        return teamDefencePerformance;
    }

    public int getEnemyDefencePerformance() {
        return enemyDefencePerformance;
    }

    @Override
    public String toString() {
        return "ReportResponse{" +
                "victory=" + victory +
                ", health=" + health +
                ", teamHealth=" + teamHealth +
                ", ensdfemyHealth=" + ensdfemyHealth +
                ", attackPerformance=" + attackPerformance +
                ", teamAttackPerformance=" + teamAttackPerformance +
                ", enemyAttackPerformance=" + enemyAttackPerformance +
                ", defencePerformance=" + defencePerformance +
                ", teamDefencePerformance=" + teamDefencePerformance +
                ", enemyDefencePerformance=" + enemyDefencePerformance +
                '}';
    }
}
