package studio.dreamys.prometheus.mixin.com.lunarclient.websocket.cosmetic.v1;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.lunarclient.websocket.cosmetic.v1.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import studio.dreamys.prometheus.util.FileUtils;
import studio.dreamys.prometheus.util.ReflectionUtils;

import java.util.List;

@Mixin(value = CosmeticService.Stub.class, remap = false)
public class MixinCosmeticService$Stub {
    @Overwrite
    public void login(RpcController rpcController, LoginRequest loginRequest, RpcCallback<LoginResponse> rpcCallback) {
        //build our fake response
        LoginResponse loginResponse = LoginResponse.newBuilder().setHasAllCosmeticsFlag(true).build();
        CustomizableCosmeticSettings settings = CustomizableCosmeticSettings.newBuilder().setClothCloak(true).build();

        //set our equipped cosmetics from file
        List<EquippedCosmetic> equippedCosmetics = FileUtils.read();
        ReflectionUtils.set(settings, "equippedCosmetics_", equippedCosmetics);
        ReflectionUtils.set(loginResponse, "settings_", settings);

        //let lunar handle the rest
        rpcCallback.run(loginResponse);
    }

    @Inject(method = "updateCosmeticSettings", at = @At("TAIL"))
    public void updateCosmeticSettings(RpcController rpcController, UpdateCosmeticSettingsRequest updateCosmeticSettingsRequest, RpcCallback<UpdateCosmeticSettingsResponse> rpcCallback, CallbackInfo ci) {
        //get equipped cosmetics from the update request (triggered every wardrobe change)
        List<EquippedCosmetic> equippedCosmetics = updateCosmeticSettingsRequest.getSettings().getEquippedCosmeticsList();

        //remove metadata fields (ex: pets) which cause adapter issues for gson
        equippedCosmetics.forEach(equippedCosmetic -> ReflectionUtils.set(equippedCosmetic.getMetadata(), "fields_", null));

        //save
        FileUtils.write(equippedCosmetics);
    }
}
