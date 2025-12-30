package studio.dreamys.prometheus.mixin.com.lunarclient.websocket.emote.v1;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.lunarclient.websocket.emote.v1.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import studio.dreamys.prometheus.util.FileUtils;

import java.util.List;

@Mixin(value = EmoteService.Stub.class, remap = false)
public class MixinEmoteService$Stub {
    @Overwrite
    public void login(RpcController rpcController, LoginRequest loginRequest, RpcCallback<LoginResponse> rpcCallback) {
        System.out.println("[Prometheus] Patched emote service login request.");

        //load saved equipped emotes from file
        List<EquippedEmote> equippedEmotes = FileUtils.readEquippedEmotes();

        //create our own response
        LoginResponse response = LoginResponse.newBuilder()
                .setHasAllEmotesFlag(true)
                .addAllEquippedEmotes(equippedEmotes)
                .build();

        //let lunar handle the rest
        rpcCallback.run(response);
    }

    @Overwrite
    public void useEmote(RpcController rpcController, UseEmoteRequest useEmoteRequest, RpcCallback<UseEmoteResponse> rpcCallback) {
        System.out.println("[Prometheus] Patched emote service use emote request.");

        //create our own response
        UseEmoteResponse response = UseEmoteResponse.newBuilder()
                .setStatus(UseEmoteResponse.Status.STATUS_OK)
                .build();

        //let lunar handle the rest
        rpcCallback.run(response);
    }

    @Overwrite
    public void updateEquippedEmotes(RpcController rpcController, UpdateEquippedEmotesRequest updateEquippedEmotesRequest, RpcCallback<UpdateEquippedEmotesResponse> rpcCallback) {
        System.out.println("[Prometheus] Patched emote service update equipped emotes request.");

        //save equipped emotes to file
        FileUtils.writeEquippedEmotes(updateEquippedEmotesRequest.getEquippedEmotesList());
    }
}
