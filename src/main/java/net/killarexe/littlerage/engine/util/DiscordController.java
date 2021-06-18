package net.killarexe.littlerage.engine.util;

import club.minnced.discord.rpc.*;

public class DiscordController {

    private String state;
    private String details;
    private String largeImageText;
    private String smallImageText;
    private String applicationId;
    private String steamId;
    private DiscordRPC rpc = DiscordRPC.INSTANCE;
    private DiscordRichPresence presence = new DiscordRichPresence();
    private DiscordEventHandlers handlers = new DiscordEventHandlers();
    private Logger logger = new Logger(getClass());

    public DiscordController(String details, String state, String largeImageText, String smallImageText, String applicationId, String steamId) {
        this.state = state;
        this.details = details;
        this.largeImageText = largeImageText;
        this.smallImageText = smallImageText;
        this.applicationId = applicationId;
        this.steamId = steamId;


        handlers.ready = (user) -> logger.info("Discord RPC Ready!");

        if(applicationId != null) {
            rpc.Discord_Initialize(applicationId, handlers, true, steamId);
        }else{
            rpc.Discord_Initialize("848204033520304188", handlers, true, steamId);
        }

        presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
        presence.endTimestamp = presence.startTimestamp + 20;
        presence.details = details;
        presence.state = state;
        presence.largeImageText = largeImageText;
        presence.smallImageText = smallImageText;
        rpc.Discord_UpdatePresence(presence);
        logger.info("Discord RPC started:\n" +
                " Settings:\n" +
                " details:" + details + "\n state: " + state + "\n largeImageText: " + largeImageText + "\n smallImageText: " + smallImageText);

        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    rpc.Discord_Shutdown();
                    break;
                }
            }
        }, "RPC-Callback-Handler");
        thread.start();
    }

    public void stop(){
        handlers.ready = null;
        rpc.Discord_ClearPresence();
        rpc.Discord_Shutdown();
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        logger.debug("Set State to: " + state);
        this.state = state;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        logger.debug("Set Details to: " + details);
        this.details = details;
    }

    public String getLargeImageText() {
        return largeImageText;
    }

    public void setLargeImageText(String largeImageText) {
        logger.debug("Set LagreImageText to: " + largeImageText);
        this.largeImageText = largeImageText;
    }

    public String getSmallImageText() {
        return smallImageText;
    }

    public void setSmallImageText(String smallImageText) {
        logger.debug("Set SmallImageText to: " + smallImageText);
        this.smallImageText = smallImageText;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getSteamId() {
        return steamId;
    }

}
