package studio.dreamys.prometheus.mixin.com.lunarclient.websocket.cosmetic.v2;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.lunarclient.websocket.cosmetic.v2.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import studio.dreamys.prometheus.util.FileUtils;

@Mixin(value = CosmeticService.Stub.class, remap = false)
public class MixinCosmeticService$Stub {
    @Overwrite
    public void login(RpcController rpcController, LoginRequest loginRequest, RpcCallback<LoginResponse> rpcCallback) {
        System.out.println("[Prometheus] Patched cosmetic service login request.");

        //load saved outfit from file
        Outfit outfit = FileUtils.readOutfit();

        //create our own response
        LoginResponse response = LoginResponse.newBuilder()
                .setHasAllCosmeticsFlag(true)
                .setArtistTools(true)
                .addOutfits(outfit)
                .setOutfitTree(OutfitTree.newBuilder().setDefaultOutfitId(outfit.getId()).build())
                .build();

        //let lunar handle the rest
        rpcCallback.run(response);
    }

    @Overwrite
    public void updateOutfit(RpcController rpcController, UpdateOutfitRequest updateOutfitRequest, RpcCallback<UpdateOutfitRequest> rpcCallback) {
        System.out.println("[Prometheus] Patched cosmetic service update outfit request.");

        //save outfit to file
        FileUtils.writeOutfit(updateOutfitRequest.getOutfit());
    }
}
