package studio.dreamys.prometheus.mixin.com.lunarclient.websocket.cosmetic.v2;

import com.lunarclient.websocket.cosmetic.v2.LoginResponse;
import com.lunarclient.websocket.cosmetic.v2.Outfit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import studio.dreamys.prometheus.util.FileUtils;

import java.util.Collections;
import java.util.List;

@Mixin(LoginResponse.class)
public class MixinLoginResponse {
    @Shadow
    private boolean hasAllCosmeticsFlag_;

    @Shadow
    private boolean artistTools_;

    @Shadow
    private List<Outfit> outfits_;

    @Inject(method = "<init>(Lcom/google/protobuf/GeneratedMessageV3$Builder;Lcom/lunarclient/websocket/cosmetic/v2/LoginResponse$1;)V", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        hasAllCosmeticsFlag_ = true;
        artistTools_ = true;
        System.out.println("[Prometheus] Patched login response.");

        Outfit outfit = FileUtils.read();
        outfits_ = Collections.singletonList(outfit);
        System.out.println("[Prometheus] Applied saved outfit to login response.");
    }
}
