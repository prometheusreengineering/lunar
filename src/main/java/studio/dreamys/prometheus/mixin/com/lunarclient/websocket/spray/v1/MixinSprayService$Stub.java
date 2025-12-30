package studio.dreamys.prometheus.mixin.com.lunarclient.websocket.spray.v1;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.lunarclient.websocket.spray.v1.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import studio.dreamys.prometheus.util.FileUtils;

import java.util.List;

@Mixin(value = SprayService.Stub.class, remap = false)
public class MixinSprayService$Stub {
    @Overwrite
    public void login(RpcController rpcController, LoginRequest loginRequest, RpcCallback<LoginResponse> rpcCallback) {
        System.out.println("[Prometheus] Patched spray service login request.");

        //load saved equipped sprays from file
        List<EquippedSpray> equippedSprays = FileUtils.readEquippedSprays();

        //create our own response
        LoginResponse response = LoginResponse.newBuilder()
                .setHasAllSpraysFlag(true)
                .addAllEquippedSprays(equippedSprays)
                .build();

        //let lunar handle the rest
        rpcCallback.run(response);
    }

    @Overwrite
    public void useSpray(RpcController rpcController, UseSprayRequest useSprayRequest, RpcCallback<UseSprayResponse> rpcCallback) {
        System.out.println("[Prometheus] Patched spray service use spray request.");

        //create our own response
        UseSprayResponse response = UseSprayResponse.newBuilder()
                .setStatus(UseSprayResponse.Status.STATUS_OK)
                .build();

        //let lunar handle the rest
        rpcCallback.run(response);
    }

    @Overwrite
    public void updateEquippedSprays(RpcController rpcController, UpdateEquippedSpraysRequest updateEquippedSpraysRequest, RpcCallback<UpdateEquippedSpraysResponse> rpcCallback) {
        System.out.println("[Prometheus] Patched spray service update equipped sprays request.");

        //save equipped sprays to file
        FileUtils.writeEquippedSprays(updateEquippedSpraysRequest.getEquippedSpraysList());
    }
}
