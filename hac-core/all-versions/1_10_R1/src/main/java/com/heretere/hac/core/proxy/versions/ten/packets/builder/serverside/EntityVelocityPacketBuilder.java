package com.heretere.hac.core.proxy.versions.ten.packets.builder.serverside;

import com.heretere.hac.api.events.packets.builder.AbstractPacketBuilder;
import com.heretere.hac.api.events.packets.wrapper.serverside.EntityVelocityPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.util.plugin.AbstractHACPlugin;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityVelocity;
import org.apache.commons.lang.reflect.FieldUtils;

import java.lang.reflect.Field;

public final class EntityVelocityPacketBuilder extends AbstractPacketBuilder<EntityVelocityPacket> {
    private final AbstractHACPlugin parent;
    /* Have to use reflections since fields are private and they're no getters :( */
    private final Field id;
    private final Field x;
    private final Field y;
    private final Field z;

    public EntityVelocityPacketBuilder(AbstractHACPlugin parent) {
        super(PacketPlayOutEntityVelocity.class);
        this.parent = parent;

        this.id = FieldUtils.getDeclaredField(PacketPlayOutEntityVelocity.class, "a", true);
        this.x = FieldUtils.getDeclaredField(PacketPlayOutEntityVelocity.class, "b", true);
        this.y = FieldUtils.getDeclaredField(PacketPlayOutEntityVelocity.class, "c", true);
        this.z = FieldUtils.getDeclaredField(PacketPlayOutEntityVelocity.class, "d", true);
    }

    @Override
    public EntityVelocityPacket create(HACPlayer player, Object packet) {
        PacketPlayOutEntityVelocity velocity = (PacketPlayOutEntityVelocity) packet;

        EntityVelocityPacket output;
        try {
            int idValue = (int) FieldUtils.readField(this.id, velocity);
            double xValue = (int) FieldUtils.readField(this.x, velocity);
            double yValue = (int) FieldUtils.readField(this.y, velocity);
            double zValue = (int) FieldUtils.readField(this.z, velocity);

            output = new EntityVelocityPacket(
                    idValue,
                    xValue / EntityVelocityPacket.CONVERSION,
                    yValue / EntityVelocityPacket.CONVERSION,
                    zValue / EntityVelocityPacket.CONVERSION
            );
        } catch (IllegalAccessException e) {
            output = null;

            this.parent.getLog().severe(e);
        }

        return output;
    }

    @Override
    public Class<EntityVelocityPacket> getWrappedClass() {
        return EntityVelocityPacket.class;
    }
}
