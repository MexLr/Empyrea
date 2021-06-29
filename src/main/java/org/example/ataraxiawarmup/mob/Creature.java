package org.example.ataraxiawarmup.mob;

public interface Creature extends Mob {

    /**
     * Tells this Creature to set the specified Mob as its target.
     *
     * @param target - New Mob to target, or null to clear the target.
     */
    public void setTarget(Mob target);

    /**
     * Gets the current target of this Creature
     *
     * @return - Current target of the creature, or null if there is none.
     */
    public Mob getTarget();
}
