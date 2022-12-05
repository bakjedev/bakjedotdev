package me.bakje.bakjedev.bakjedev.util;


import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

public class FriendManager {
    private Set<String> friends = new TreeSet<>();

    public FriendManager() {
    }

    public FriendManager(Collection<String> names) {
        friends.addAll(names);
    }

    public void add(Entity entity) {
        if (entity instanceof PlayerEntity)
            add(entity.getName().getString());
    }

    public void add(String name) {
        name = Formatting.strip(name).toLowerCase(Locale.ENGLISH);

        if (!name.isEmpty()) {
            friends.add(name);
        }
    }

    public void addAll(Collection<String> names) {
        names.forEach(this::add);
    }

    public void remove(Entity entity) {
        if (entity instanceof PlayerEntity)
            remove(entity.getName().getString());
    }

    public void remove(String name) {
        name = Formatting.strip(name).toLowerCase(Locale.ENGLISH);

        if (!name.isEmpty()) {
            friends.remove(name);
        }
    }

    public void removeAll(Collection<String> names) {
        names.forEach(this::remove);
    }

    public boolean has(Entity entity) {
        if (entity instanceof PlayerEntity)
            return has(entity.getName().getString());

        return false;
    }

    public boolean has(String name) {
        name = Formatting.strip(name).toLowerCase(Locale.ENGLISH);

        if (!name.isEmpty()) {
            return friends.contains(name);
        }

        return false;
    }

    public Set<String> getFriends() {
        return friends;
    }
}
