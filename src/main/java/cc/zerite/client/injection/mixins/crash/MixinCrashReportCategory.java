package cc.zerite.client.injection.mixins.crash;

import net.minecraft.crash.CrashReportCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CrashReportCategory.class)
public class MixinCrashReportCategory {

    @Shadow private StackTraceElement[] stackTrace;

    /**
     * @author asbyth
     * @reason dont kno lol
     */
    @Overwrite
    public int getPrunedStackTrace(int size) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        if (stackTraceElements.length <= 0) {
            return 0;
        } else {
            int len = stackTraceElements.length - 3 - size;

            if (len <= 0) len = stackTraceElements.length;
            stackTraceElements = new StackTraceElement[len];

            System.arraycopy(stackTraceElements, stackTraceElements.length - len, stackTraceElements, 0, stackTraceElements.length);
            return stackTrace.length;
        }
    }
}
