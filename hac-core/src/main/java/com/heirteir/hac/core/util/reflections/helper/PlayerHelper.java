package com.heirteir.hac.core.util.reflections.helper;

import com.heirteir.hac.api.util.reflections.Reflections;
import com.heirteir.hac.api.util.reflections.types.WrappedConstructor;
import com.heirteir.hac.api.util.reflections.types.WrappedField;
import com.heirteir.hac.api.util.reflections.types.WrappedMethod;
import com.heirteir.hac.api.util.reflections.version.ServerVersion;
import com.heirteir.hac.util.logging.Log;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public final class PlayerHelper {

    private static final int ELYTRA_FLYING_FLAG = 7;
    /* Player - Get */
    private WrappedMethod getHandle;
    private WrappedField playerConnection;
    private WrappedMethod sendPacket;
    /* Player - Elytra */
    private WrappedMethod getFlag;
    /* Player - World */
    private WrappedMethod getWorld;
    /* Player - Position */
    private WrappedConstructor blockPositionConstructor;

    public PlayerHelper(Reflections reflections) {
        try {
            /* Player - Get */
            this.getHandle = reflections.getCBClass("entity.CraftEntity").getMethod("getHandle");
            this.playerConnection = reflections.getNMSClass("EntityPlayer").getFieldByName("playerConnection");
            this.sendPacket = reflections.getNMSClass("PlayerConnection").getMethod("sendPacket", reflections.getNMSClass("Packet").getRaw());

            /* Player - Elytra */
            if (reflections.getVersion().greaterThanOrEqual(ServerVersion.NINE_R1)) {
                this.getFlag = reflections.getNMSClass("Entity").getMethod("getFlag", int.class);
            }

            /* Player - World */
            this.getWorld = reflections.getNMSClass("Entity").getMethod("getWorld");

            /* Player - Position */
            this.blockPositionConstructor = reflections.getNMSClass("BlockPosition").getConstructor(double.class, double.class, double.class);
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            Log.INSTANCE.reportFatalError(e);
        }
    }

    @Nullable
    public Object getBlockPosition(Player player) {
        Object output;
        try {
            output = this.blockPositionConstructor != null ? this.blockPositionConstructor.newInstance(Object.class, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()) : null;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            output = null;
            Log.INSTANCE.reportFatalError(e);
        }
        return output;
    }

    /**
     * Get the NMS world object for a player
     *
     * @param player The Bukkit Player
     * @return NMS world object
     */
    public Object getWorld(Player player) {
        Object output;
        try {
            output = this.getWorld.invoke(Object.class, this.getEntityPlayer(player));
        } catch (InvocationTargetException | IllegalAccessException e) {
            output = null;
            Log.INSTANCE.reportFatalError(e);
        }
        return output;
    }

    /**
     * Returns whether or not the specified player is flying using Elytra.
     *
     * @param player The Bukkit Player
     * @return true if player is flying with elytra false if not;
     */
    public boolean isElytraFlying(Player player) {
        boolean output;
        try {
            output = this.getFlag != null && this.getFlag.invoke(Boolean.class, this.getEntityPlayer(player), PlayerHelper.ELYTRA_FLYING_FLAG);
        } catch (InvocationTargetException | IllegalAccessException e) {
            output = false;
            Log.INSTANCE.reportFatalError(e);
        }
        return output;
    }

    /**
     * Convert a player object to an EntityPlayer object.
     *
     * @param player The Bukkit Player
     * @return An EntityPlayer object.
     */
    public Object getEntityPlayer(Player player) {
        Object output;
        try {
            output = this.getHandle.invoke(Object.class, player);
        } catch (InvocationTargetException | IllegalAccessException e) {
            output = null;
            Log.INSTANCE.reportFatalError(e);
        }
        return output;
    }

    /**
     * Retrieves the PlayerConnection from the player.
     *
     * @param player The Bukkit Player
     * @return A PlayerConnection object.
     */
    public Object getPlayerConnection(Player player) {
        Object output;
        try {
            output = this.playerConnection.get(Object.class, this.getEntityPlayer(player));
        } catch (IllegalAccessException e) {
            output = null;
            Log.INSTANCE.reportFatalError(e);
        }
        return output;
    }

    /**
     * Sends a packet to the specified player object.
     *
     * @param player The Bukkit Player
     * @param packet The Packet object
     */
    public void sendPacket(Player player, Object packet) {
        try {
            this.sendPacket.invoke(Object.class, this.playerConnection.get(Object.class, this.getEntityPlayer(player)), packet);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Log.INSTANCE.reportFatalError(e);
        }
    }
}
