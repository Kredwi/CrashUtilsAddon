package ru.kredwi.crashutils.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class TradeSystemPacket implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("tradesystem", "backend");
    public static final StreamCodec<FriendlyByteBuf, TradeSystemPacket> CODEC = StreamCodec.ofMember(TradeSystemPacket::write, TradeSystemPacket::new);
    public static final CustomPacketPayload.Type<TradeSystemPacket> TYPE = new Type<>(ID);

    public TradeSystemPacket() {
    }

    public TradeSystemPacket(FriendlyByteBuf buf) {
        this();
    }

    public static void send() {
        ClientPlayNetworking.send(new TradeSystemPacket());
    }

    private void write(FriendlyByteBuf buf) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
