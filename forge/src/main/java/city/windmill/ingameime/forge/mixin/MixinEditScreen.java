package city.windmill.ingameime.forge.mixin;

import city.windmill.ingameime.forge.ScreenEvents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import org.joml.Matrix4f;
import kotlin.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.reflect.Field;

@Mixin({Screen.class, AbstractSignEditScreen.class})
class MixinScreen {
    @Inject(method = "removed", at = @At("TAIL"))
    private void onRemove(CallbackInfo info) {
        ScreenEvents.INSTANCE.getEDIT_CLOSE().invoker().onEditClose(this);
    }
}

@Mixin({BookEditScreen.class, SignEditScreen.class})
class MixinEditScreen {
    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo info) {
        ScreenEvents.INSTANCE.getEDIT_OPEN().invoker().onEditOpen(this, new Pair<>(0, 0));
    }
}

@Mixin(BookEditScreen.class)
abstract class MixinBookEditScreen {
    @Inject(method = "renderCursor",
            at = @At(value = "INVOKE",
                    shift = At.Shift.BY,
                    by = 2,
                    target = "Lnet/minecraft/client/gui/screens/inventory/BookEditScreen;convertLocalToScreen(Lnet/minecraft/client/gui/screens/inventory/BookEditScreen$Pos2i;)Lnet/minecraft/client/gui/screens/inventory/BookEditScreen$Pos2i;")
    )
    private void onCaret_Book(GuiGraphics guiGraphics, BookEditScreen.Pos2i pos2i, boolean bl, CallbackInfo ci) {
        ScreenEvents.INSTANCE.getEDIT_CARET().invoker().onEditCaret(this, new Pair<>(pos2i.x, pos2i.y));
    }

    /*
    @Inject(method = "convertLocalToScreen",
            at = @At("TAIL"))
    private void onCaret_Book(Object pos2i, CallbackInfoReturnable<Object> cir) {
        try {
            Field pos2ix = pos2i.getClass().getDeclaredField("x");
            Field pos2iy = pos2i.getClass().getDeclaredField("y");
            pos2ix.setAccessible(true);
            pos2iy.setAccessible(true);
            ScreenEvents.INSTANCE.getEDIT_CARET().invoker().onEditCaret(this, new Pair<>((Integer) pos2ix.get(cir.getReturnValue()), (Integer) pos2iy.get(cir.getReturnValue())));
        } catch (Exception ignored) {

        }
    }
     */

    @Inject(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;IIIZ)I"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onCaret_Book(GuiGraphics arg, int i, int j, float f, CallbackInfo ci,
                              int k, FormattedCharSequence formattedCharSequence, int m, int n) {
        ScreenEvents.INSTANCE.getEDIT_CARET().invoker().onEditCaret(this, new Pair<>(
                k + 36 + (114 + n) / 2
                        - Minecraft.getInstance().font.width("_"),
                50
        ));
    }
}

@Mixin(AbstractSignEditScreen.class)
abstract class MixinEditSignScreen extends Screen {

    protected MixinEditSignScreen(Component p_i51108_1_) {
        super(p_i51108_1_);
    }

    @Inject(method = "renderSignText",
            at = {
                    @At(value = "INVOKE",
                            target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I",
                            ordinal = 1),
                    @At(value = "INVOKE",
                            target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V",
                            ordinal = 0)},
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onCaret_Sign(GuiGraphics arg, int i, int j, float f, CallbackInfo ci, float g, BlockState lv, boolean bl, boolean bl2, float h, MultiBufferSource.BufferSource lv2, float k, int l, int m, int n, int o, Matrix4f matrix4f, int p, String string, float q, int r, int s) {
        //s(23)->x,o(17)->y
        try {
            Field m03 = matrix4f.getClass().getDeclaredField("m03");
            Field m13 = matrix4f.getClass().getDeclaredField("m13");
            m03.setAccessible(true);
            m13.setAccessible(true);
            ScreenEvents.INSTANCE.getEDIT_CARET().invoker().onEditCaret(this, new Pair<>((Integer) m03.get(matrix4f) + s, (Integer) m13.get(matrix4f) + o));
        } catch (Exception ignored) {

        }
    }

    @Surrogate
    private void onCaret_Sign(GuiGraphics arg, int i, int j, float f, CallbackInfo ci, float g, BlockState lv, boolean bl, boolean bl2, float h, MultiBufferSource.BufferSource lv2, float k, int l, int m, int n, int o, Matrix4f matrix4f, int t, String string2, int u, int v) {
        //v(22)->x,o(17)->y
        try {
            Field m03 = matrix4f.getClass().getDeclaredField("m03");
            Field m13 = matrix4f.getClass().getDeclaredField("m13");
            m03.setAccessible(true);
            m13.setAccessible(true);
            ScreenEvents.INSTANCE.getEDIT_CARET().invoker().onEditCaret(this, new Pair<>((Integer) m03.get(matrix4f) + v, (Integer) m13.get(matrix4f) + o));
        } catch (Exception ignored) {

        }
    }
}