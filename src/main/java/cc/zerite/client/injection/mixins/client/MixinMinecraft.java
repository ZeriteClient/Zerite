package cc.zerite.client.injection.mixins.client;

import cc.zerite.client.event.ClientShutdownEvent;
import cc.zerite.client.event.ClientStartEvent;
import cc.zerite.client.event.EventBus;
import cc.zerite.client.event.GuiDisplayEvent;
import cc.zerite.client.game.gui.GuiZeriteMainMenu;
import cc.zerite.client.game.gui.SplashRenderer;
import cc.zerite.client.injection.bootstrap.ZeriteBootstrap;
import cc.zerite.client.injection.mixinsimp.client.MixinMinecraftImpl;
import cc.zerite.client.injection.mixinsimp.client.StatStringFormatterImpl;
import cc.zerite.client.util.ZeriteMetadata;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.PixelFormat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.List;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow public WorldClient theWorld;
    @Shadow public GameSettings gameSettings;
    @Shadow public GuiIngame ingameGUI;
    @Shadow private SoundHandler mcSoundHandler;
    @Shadow @Final public File mcDataDir;
    @Shadow @Final private List<IResourcePack> defaultResourcePacks;
    @Shadow @Final private DefaultResourcePack mcDefaultResourcePack;
    @Shadow protected abstract void startTimerHackThread();
    @Shadow public int displayWidth;
    @Shadow public int displayHeight;
    @Shadow @Final private static Logger logger;
    @Shadow protected abstract void setWindowIcon();
    @Shadow protected abstract void setInitialDisplayMode() throws LWJGLException;
    @Shadow private Framebuffer framebufferMc;
    @Shadow protected abstract void registerMetadataSerializers();
    @Shadow private ResourcePackRepository mcResourcePackRepository;
    @Shadow @Final private File fileResourcepacks;
    @Shadow @Final private IMetadataSerializer metadataSerializer_;
    @Shadow private IReloadableResourceManager mcResourceManager;
    @Shadow private LanguageManager mcLanguageManager;
    @Shadow public abstract void refreshResources();
    @Shadow private TextureManager renderEngine;
    @Shadow protected abstract void initStream();
    @Shadow private SkinManager skinManager;
    @Shadow @Final private File fileAssets;
    @Shadow @Final private MinecraftSessionService sessionService;
    @Shadow private ISaveFormat saveLoader;
    @Shadow private MusicTicker mcMusicTicker;
    @Shadow public FontRenderer fontRendererObj;
    @Shadow public abstract boolean isUnicode();
    @Shadow public FontRenderer standardGalacticFontRenderer;
    @Shadow public MouseHelper mouseHelper;
    @Shadow protected abstract void checkGLError(String message);
    @Shadow private TextureMap textureMapBlocks;
    @Shadow private ModelManager modelManager;
    @Shadow private RenderItem renderItem;
    @Shadow private RenderManager renderManager;
    @Shadow private ItemRenderer itemRenderer;
    @Shadow public EntityRenderer entityRenderer;
    @Shadow private BlockRendererDispatcher blockRenderDispatcher;
    @Shadow public RenderGlobal renderGlobal;
    @Shadow public GuiAchievement guiAchievement;
    @Shadow public EffectRenderer effectRenderer;
    @Shadow private String serverName;
    @Shadow private int serverPort;
    @Shadow private ResourceLocation mojangLogo;
    @Shadow public LoadingScreenRenderer loadingScreen;
    @Shadow private boolean fullscreen;
    @Shadow public abstract void toggleFullscreen();
    @Shadow public GuiScreen currentScreen;
    @Shadow public EntityPlayerSP thePlayer;
    @Shadow public abstract void setIngameNotInFocus();
    @Shadow public boolean skipRenderWorld;
    @Shadow public abstract void setIngameFocus();
    @Shadow long systemTime;
    @Shadow protected abstract void updateDisplayMode() throws LWJGLException;

    private MixinMinecraftImpl impl = new MixinMinecraftImpl((Minecraft) (Object) this);

    /**
     * @author Koding
     */
    @Overwrite
    private void startGame() throws LWJGLException {
        this.gameSettings = new GameSettings(Minecraft.getMinecraft(), this.mcDataDir);
        this.defaultResourcePacks.add(this.mcDefaultResourcePack);
        this.startTimerHackThread();

        if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0) {
            this.displayWidth = this.gameSettings.overrideWidth;
            this.displayHeight = this.gameSettings.overrideHeight;
        }

        logger.info("LWJGL Version: " + Sys.getVersion());
        this.setWindowIcon();
        this.setInitialDisplayMode();
        this.createDisplay();
        OpenGlHelper.initializeTextures();
        this.framebufferMc = new Framebuffer(this.displayWidth, this.displayHeight, true);
        this.framebufferMc.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);

        this.registerMetadataSerializers();
        this.mcResourcePackRepository = new ResourcePackRepository(this.fileResourcepacks,
                new File(this.mcDataDir, "server-resource-packs"), this.mcDefaultResourcePack,
                this.metadataSerializer_, this.gameSettings);
        this.mcResourceManager = new SimpleReloadableResourceManager(this.metadataSerializer_);
        this.mcLanguageManager = new LanguageManager(this.metadataSerializer_,
                this.gameSettings.language);
        this.mcResourceManager.registerReloadListener(this.mcLanguageManager);
        this.refreshResources();
        this.renderEngine = new TextureManager(this.mcResourceManager);
        this.mcResourceManager.registerReloadListener(this.renderEngine);
        this.drawSplashScreen(this.renderEngine);

        ZeriteBootstrap.INSTANCE.beginGameCreate();

        this.initStream();
        this.skinManager = new SkinManager(this.renderEngine, new File(this.fileAssets, "skins"),
                this.sessionService);
        this.saveLoader = new AnvilSaveConverter(new File(this.mcDataDir, "saves"));

        SplashRenderer.INSTANCE.updateData("Loading sounds");

        this.mcSoundHandler = new SoundHandler(this.mcResourceManager, this.gameSettings);
        this.mcResourceManager.registerReloadListener(this.mcSoundHandler);
        this.mcMusicTicker = new MusicTicker(Minecraft.getMinecraft());

        SplashRenderer.INSTANCE.updateData("Loading font renderer");

        this.fontRendererObj = new FontRenderer(this.gameSettings,
                new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);

        if (this.gameSettings.language != null) {
            this.fontRendererObj.setUnicodeFlag(this.isUnicode());
            this.fontRendererObj
                    .setBidiFlag(this.mcLanguageManager.isCurrentLanguageBidirectional());
        }

        this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings,
                new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
        this.mcResourceManager.registerReloadListener(this.fontRendererObj);
        this.mcResourceManager.registerReloadListener(this.standardGalacticFontRenderer);
        this.mcResourceManager.registerReloadListener(new GrassColorReloadListener());
        this.mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
        AchievementList.openInventory.setStatStringFormatter(new StatStringFormatterImpl());
        this.mouseHelper = new MouseHelper();
        this.checkGLError("Pre startup");
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7425);
        GlStateManager.clearDepth(1.0D);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.cullFace(1029);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        this.checkGLError("Startup");

        SplashRenderer.INSTANCE.updateData("Loading render engine");

        this.textureMapBlocks = new TextureMap("textures");
        this.textureMapBlocks.setMipmapLevels(this.gameSettings.mipmapLevels);
        this.renderEngine
                .loadTickableTexture(TextureMap.locationBlocksTexture, this.textureMapBlocks);
        this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        this.textureMapBlocks.setBlurMipmapDirect(false, this.gameSettings.mipmapLevels > 0);
        this.modelManager = new ModelManager(this.textureMapBlocks);
        this.mcResourceManager.registerReloadListener(this.modelManager);
        this.renderItem = new RenderItem(this.renderEngine, this.modelManager);
        this.renderManager = new RenderManager(this.renderEngine, this.renderItem);
        this.itemRenderer = new ItemRenderer(Minecraft.getMinecraft());
        this.mcResourceManager.registerReloadListener(this.renderItem);
        this.entityRenderer = new EntityRenderer(Minecraft.getMinecraft(), this.mcResourceManager);
        this.mcResourceManager.registerReloadListener(this.entityRenderer);
        this.blockRenderDispatcher = new BlockRendererDispatcher(
                this.modelManager.getBlockModelShapes(), this.gameSettings);
        this.mcResourceManager.registerReloadListener(this.blockRenderDispatcher);

        SplashRenderer.INSTANCE.updateData("Loading render global");

        this.renderGlobal = new RenderGlobal(Minecraft.getMinecraft());
        this.mcResourceManager.registerReloadListener(this.renderGlobal);
        this.guiAchievement = new GuiAchievement(Minecraft.getMinecraft());
        GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
        this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

        SplashRenderer.INSTANCE.updateData("Finalizing");

        this.checkGLError("Post startup");
        this.ingameGUI = new GuiIngame(Minecraft.getMinecraft());

        Display.setTitle("Zerite Client");
        EventBus.INSTANCE.post(new ClientStartEvent());
        ZeriteBootstrap.INSTANCE.beginClientInit();
        ((IMixinMinecraft) this).setEnableGLErrorChecking(false);

        if (this.serverName != null) {
            this.displayGuiScreen(
                    new GuiConnecting(new GuiMainMenu(), Minecraft.getMinecraft(), this.serverName,
                            this.serverPort));
        } else {
            this.displayGuiScreen(new GuiMainMenu());
        }

        this.renderEngine.deleteTexture(this.mojangLogo);
        this.mojangLogo = null;
        this.loadingScreen = new LoadingScreenRenderer(Minecraft.getMinecraft());

        if (this.gameSettings.fullScreen && !this.fullscreen) {
            this.toggleFullscreen();
        }

        try {
            Display.setVSyncEnabled(this.gameSettings.enableVsync);
        } catch (OpenGLException var2) {
            this.gameSettings.enableVsync = false;
            this.gameSettings.saveOptions();
        }

        this.renderGlobal.makeEntityOutlineShader();
    }

    /**
     * @author Koding
     */
    @Overwrite
    private void createDisplay() throws LWJGLException {
        Display.setResizable(true);
        Display.setTitle("Zerite [INITIALIZING] | " + ZeriteMetadata.getZeriteVersion());

        try {
            Display.create((new PixelFormat()).withDepthBits(24).withStencilBits(1));
        } catch (LWJGLException lwjglexception) {
            logger.error("Couldn't set pixel format", lwjglexception);

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignored) { }

            if (this.fullscreen) {
                this.updateDisplayMode();
            }

            Display.create();
        }
    }

    /**
     * @author Koding
     */
    @Overwrite
    private void drawSplashScreen(TextureManager textureManagerInstance) {
        SplashRenderer.INSTANCE.drawSplash(textureManagerInstance, false, 255);
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void shutdown(CallbackInfo ci) {
        ZeriteBootstrap.INSTANCE.beginClientShutdown();
        EventBus.INSTANCE.post(new ClientShutdownEvent());
    }

    /**
     * @author Koding
     */
    @Overwrite
    public void displayGuiScreen(GuiScreen guiScreenIn) {
        if (this.currentScreen != null) {
            this.currentScreen.onGuiClosed();
        }

        if ((guiScreenIn == null || guiScreenIn instanceof GuiMainMenu) && this.theWorld == null) {
            guiScreenIn = new GuiZeriteMainMenu();
        } else if (guiScreenIn == null && this.thePlayer.getHealth() <= 0.0F) {
            guiScreenIn = new GuiGameOver();
        }

        if (guiScreenIn instanceof GuiZeriteMainMenu) {
            this.gameSettings.showDebugInfo = false;
            this.ingameGUI.getChatGUI().clearChatMessages();
        }

        this.currentScreen = guiScreenIn;

        EventBus.INSTANCE.post(new GuiDisplayEvent(guiScreenIn));

        if (guiScreenIn != null) {
            this.setIngameNotInFocus();
            ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            guiScreenIn.setWorldAndResolution(Minecraft.getMinecraft(), i, j);
            this.skipRenderWorld = false;
        } else {
            this.mcSoundHandler.resumeSounds();
            this.setIngameFocus();
        }
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;updateCameraAndRender(FJ)V", shift = At.Shift.AFTER))
    private void runGameLoop(CallbackInfo ci) {
        impl.runGameLoop();
    }

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("RETURN"), cancellable = true)
    private void loadWorld(WorldClient worldClient, String message, CallbackInfo ci) {
        impl.loadWorld(worldClient, message);
        this.systemTime = 0; // clear system time so worlds load instantly when switching
    }

    /**
     * @param ci callback
     * @author asbyth
     * @reason properly reset fullscreen state so game can be maximized after toggling fullscreen
     */
    @Inject(method = "toggleFullscreen", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setFullscreen(Z)V", shift = At.Shift.BEFORE))
    private void toggleFullscreen(CallbackInfo ci) {
        impl.toggleFullscreen();
    }
}
