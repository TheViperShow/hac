package com.heretere.hac.core.proxy.player.data.player.executors;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.Priority;
import com.heretere.hac.api.events.packets.wrapper.clientside.EntityActionPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.data.player.PlayerData;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public final class PlayerDataEntityActionExecutor extends AbstractPacketEventExecutor<EntityActionPacket> {

    public PlayerDataEntityActionExecutor(String identifier) {
        super(
                Priority.PROCESS_1,
                identifier,
                HACAPI.getInstance().getPacketReferences().getClientSide().getEntityAction()
        );
    }

    @Override
    public boolean execute(@NotNull HACPlayer player, @NotNull EntityActionPacket packet) {
        PlayerData data = player.getDataManager().getData(PlayerData.class);

        switch (packet.getAction()) {
            case START_SNEAKING:
                data.getCurrent().setSneaking(true);
                break;
            case STOP_SNEAKING:
                data.getCurrent().setSneaking(false);
                break;
            case START_SPRINTING:
                data.getCurrent().setSprinting(true);
                break;
            case STOP_SPRINTING:
                data.getCurrent().setSprinting(false);
                break;
            case START_FALL_FLYING:
                data.getCurrent().setElytraFlying(true);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onStop(@NotNull HACPlayer player, @NotNull EntityActionPacket packet) {
        throw new NotImplementedException("Updater Class.");
    }
}
