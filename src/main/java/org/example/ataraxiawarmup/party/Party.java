package org.example.ataraxiawarmup.party;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.example.ataraxiawarmup.player.CustomPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Party {

    public static final Map<CustomPlayer, Party> PARTY_LEADER_MAP = new HashMap<>();

    private CustomPlayer leader;
    private final List<CustomPlayer> members = new ArrayList<>();

    public Party(CustomPlayer leader) {
        this.leader = leader;
        leader.setParty(this);
    }

    public void create() {
        PARTY_LEADER_MAP.put(leader, this);
    }

    public void addPlayer(CustomPlayer player) {
        this.members.add(player);
        player.setParty(this);
        for (CustomPlayer customPlayer : getAllPlayers()) {
            customPlayer.getPlayer().sendMessage(ChatColor.AQUA + player.getPlayer().getName() + " joined the party!");
        }
    }

    public void kickPlayer(CustomPlayer player) {
        if (members.contains(player)) {
            for (CustomPlayer customPlayer : getAllPlayers()) {
                customPlayer.getPlayer().sendMessage(ChatColor.AQUA + player.getPlayer().getName() + " left the party.");
            }
            this.members.remove(player);
        }
        if (leader.equals(player)) {
            for (CustomPlayer customPlayer : getAllPlayers()) {
                customPlayer.getPlayer().sendMessage(ChatColor.AQUA + player.getPlayer().getName() + " left the party.");
            }
            if (members.size() == 0) {
                disband();
                return;
            }
            transferLeader(members.get(0));
            members.remove(player);
        }
        player.setParty(null);
    }

    public void invitePlayer(CustomPlayer player) {
        if (!this.members.contains(player)) {
            for (CustomPlayer customPlayer : getAllPlayers()) {
                customPlayer.getPlayer().sendMessage(ChatColor.AQUA + player.getPlayer().getName() + " was invited to the party!");
            }
            player.invite(this);
        }
    }

    public void disband() {
        PARTY_LEADER_MAP.remove(this.leader);
        for (CustomPlayer customPlayer : getAllPlayers()) {
            customPlayer.getPlayer().sendMessage(ChatColor.AQUA + "The party was disbanded.");
            customPlayer.setParty(null);
            if (customPlayer.equals(leader)) {
                leader = null;
            } else {
                members.remove(customPlayer);
            }
        }
    }

    public void transferLeader(CustomPlayer newLeader) {
        if (this.members.contains(newLeader)) {
            if (newLeader != this.leader) {
                for (CustomPlayer customPlayer : getAllPlayers()) {
                    customPlayer.getPlayer().sendMessage(ChatColor.AQUA + "The party was transferred to " + newLeader.getPlayer().getName() + " by " + leader.getPlayer().getName() + "!");
                }
                this.members.add(this.leader);
                this.leader = newLeader;
                this.members.remove(newLeader);
            }
        }
    }

    public void displayMembers() {

    };

    public CustomPlayer getLeader() {
        return leader;
    }

    public List<CustomPlayer> getMembers() {
        return members;
    }

    public List<CustomPlayer> getAllPlayers() {
        List<CustomPlayer> membersClone = new ArrayList<>();
        membersClone.addAll(members);
        membersClone.add(this.leader);
        return membersClone;
    }

}
