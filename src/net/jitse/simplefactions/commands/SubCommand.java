package net.jitse.simplefactions.commands;

import net.jitse.simplefactions.factions.Role;

/**
 * Created by Jitse on 25-6-2017.
 */
public abstract class SubCommand implements ICommand {

    private final Role role;

    public SubCommand(Role role) {
        this.role = role;
    }

    public Role getRole(){
        return this.role;
    }
}
