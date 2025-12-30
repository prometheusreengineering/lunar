package studio.dreamys.prometheus.mixin.com.lunarclient.websocket.badge.v1;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.lunarclient.websocket.badge.v1.BadgeService;
import com.lunarclient.websocket.badge.v1.EquipBadgeRequest;
import com.lunarclient.websocket.badge.v1.EquipBadgeResponse;
import com.lunarclient.websocket.badge.v1.LoginRequest;
import com.lunarclient.websocket.badge.v1.LoginResponse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import studio.dreamys.prometheus.util.FileUtils;

import java.util.logging.Level;

import static studio.dreamys.prometheus.util.FileUtils.logger;

@Mixin(value = BadgeService.Stub.class, remap = false)
public class MixinBadgeService$Stub {
    @Overwrite
    public void login(RpcController rpcController, LoginRequest loginRequest, RpcCallback<LoginResponse> rpcCallback) {
        logger.log(Level.INFO, "Patched badge service login request.");

        //load equipped badge from file
        int badgeId = FileUtils.readBadge();

        //create our own response
        LoginResponse response = LoginResponse.newBuilder()
                .setHasAllBadgesFlag(true)
                .setEquippedBadgeId(badgeId)
                .build();

        //let lunar handle the rest
        rpcCallback.run(response);
    }

    @Overwrite
    public void equipBadge(RpcController rpcController, EquipBadgeRequest equipBadgeRequest, RpcCallback<EquipBadgeResponse> rpcCallback) {
        logger.log(Level.INFO, "Patched badge service equip badge request.");

        //save equipped badge to file
        FileUtils.writeBadge(equipBadgeRequest.getBadgeId());
    }
}
