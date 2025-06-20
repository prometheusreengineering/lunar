package studio.dreamys.prometheus.mixin.com.lunarclient.websocket.cosmetic.v2;

//import com.lunarclient.websocket.cosmetic.v2.*;
import com.lunarclient.websocket.cosmetic.v2.LoginResponse;
import com.lunarclient.websocket.cosmetic.v2.CosmeticService;
import com.lunarclient.websocket.cosmetic.v2.EquippedCosmetic;
import com.lunarclient.websocket.cosmetic.v2.Outfit;
//import com.lunarclient.websocket.cosmetic.v2.CustomizableCosmeticSettings;
import com.lunarclient.websocket.cosmetic.v2.LoginRequest;
import com.lunarclient.websocket.cosmetic.v2.UpdateOutfitRequest;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import studio.dreamys.prometheus.util.v2.FileUtils;
import studio.dreamys.prometheus.util.ReflectionUtils;

import java.util.List;

@Mixin(value = CosmeticService.Stub.class, remap = false)
public class MixinCosmeticService$Stub {
    @Overwrite
    public void login(RpcController rpcController, LoginRequest loginRequest, RpcCallback<LoginResponse> rpcCallback) {
        System.out.println("[Prometheus] Intercepted login request to load equipped cosmetics...");
        //build our fake response
        LoginResponse loginResponse = LoginResponse.newBuilder().setHasAllCosmeticsFlag(true).build();
        EquippedCosmetic settings = EquippedCosmetic.newBuilder().setClothCloak(true).build();

        //set our equipped cosmetics from file
        List<EquippedCosmetic> equippedCosmetics = FileUtils.read();
        Outfit equippedOutfit = Outfit.newBuilder()
                .addAllCosmetics(equippedCosmetics)
                .build();
        List<Outfit> outfits = loginResponse.getOutfitsList();
        outfits.add(equippedOutfit);
        ReflectionUtils.set(loginResponse, "outfits_", outfits);
        System.out.println("[Prometheus] Loaded " + equippedCosmetics.size() + " equipped cosmetics from file.");

        //let lunar handle the rest
        rpcCallback.run(loginResponse);
    }

    @Inject(method = "updateOutfit", at = @At("TAIL"))
    public void updateOutfit(RpcController rpcController, UpdateOutfitRequest updateOutfitRequest, RpcCallback<UpdateOutfitRequest> rpcCallback, CallbackInfo ci) {
        System.out.println("[Prometheus] Updating equipped cosmetics...");
        //get equipped cosmetics from the update request (triggered every wardrobe change)
        List<EquippedCosmetic> equippedCosmetics = updateOutfitRequest.getOutfit().getCosmeticsList();

        //remove metadata fields (ex: pets) which cause adapter issues for gson
        equippedCosmetics.forEach(equippedCosmetic -> ReflectionUtils.set(equippedCosmetic.getGeckolibMetadata(), "fields_", null));

        //save
        FileUtils.write(equippedCosmetics);
        System.out.println("[Prometheus] Saved " + equippedCosmetics.size() + " equipped cosmetics to file.");
    }
}
