package me.bakje.bakjedev.bakjedev.event.events;

import me.bakje.bakjedev.bakjedev.event.Event;

public class EntityControlEvent extends Event {
    private Boolean canBeControlled;

    public Boolean canBeControlled() {
        return canBeControlled;
    }

    public void setControllable(Boolean canBeControlled) {
        this.canBeControlled = canBeControlled;
    }
}
