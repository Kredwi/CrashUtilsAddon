package ru.kredwi.crashutils.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class AuthMeVelocityPayloadPacket implements CustomPacketPayload {
    public static final Minecraft mc = Minecraft.getInstance();
    public static final Identifier ID = Identifier.fromNamespaceAndPath("authmevelocity", "main");
    public static final StreamCodec<FriendlyByteBuf, AuthMeVelocityPayloadPacket> CODEC = StreamCodec.ofMember(AuthMeVelocityPayloadPacket::write, AuthMeVelocityPayloadPacket::new);
    public static final Type<AuthMeVelocityPayloadPacket> TYPE = new Type<>(ID);

    public AuthMeVelocityPayloadPacket() {
    }

    private AuthMeVelocityPayloadPacket(FriendlyByteBuf buf) {
        this();
    }

    public static void send() {
        mc.getConnection()
            .send(new ServerboundCustomPayloadPacket(new AuthMeVelocityPayloadPacket()));
    }

    private void write(FriendlyByteBuf buf) {
//        buf.writeByte(0);
        buf.writeUtf("LOGIN");
//        buf.writeByte(0);
        buf.writeUtf(mc.getGameProfile().name());
    }

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}
