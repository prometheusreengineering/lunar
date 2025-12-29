package studio.dreamys.prometheus.mixin.com.lunarclient.websocket.cosmetic.v2;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.lunarclient.websocket.cosmetic.v2.CosmeticService;
import com.lunarclient.websocket.cosmetic.v2.UpdateOutfitRequest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import studio.dreamys.prometheus.util.FileUtils;

@Mixin(value = CosmeticService.Stub.class, remap = false)
public class MixinCosmeticService$Stub {
    @Inject(method = "updateOutfit", at = @At("TAIL"))
    public void updateOutfit(RpcController rpcController, UpdateOutfitRequest updateOutfitRequest, RpcCallback<UpdateOutfitRequest> rpcCallback, CallbackInfo ci) {
        FileUtils.write(updateOutfitRequest.getOutfit());
    }
}
