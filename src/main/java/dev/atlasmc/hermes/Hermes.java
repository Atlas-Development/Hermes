package dev.atlasmc.hermes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.inject.Inject;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.atlasmc.hermes.command.PrivateMessageReplyCommand;
import dev.atlasmc.hermes.command.SocialSpyCommand;
import dev.atlasmc.hermes.listener.LuckPermsUserDataRecalculateListener;
import dev.atlasmc.hermes.listener.PlayerJoinedListener;
import dev.atlasmc.hermes.listener.PlayerLeftListener;
import dev.atlasmc.hermes.model.channel.ChannelManager;
import dev.atlasmc.hermes.model.config.HermesConfig;
import dev.atlasmc.hermes.model.config.runtime.PlayerConfiguration;
import lombok.Getter;
import dev.atlasmc.hermes.command.HermesCommand;
import dev.atlasmc.hermes.command.PrivateMessageCommand;
import dev.atlasmc.hermes.listener.PlayerChatListener;
import net.kyori.adventure.audience.Audience;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Plugin(id = "hermes", name = "Hermes", version = Hermes.version)
public class Hermes {
    public static final String version = "1.0-SNAPSHOT";

    @Getter
    private final Logger logger;

    @Getter
    private final ProxyServer proxy;

    @Getter
    private LuckPerms luckPerms;

    @Getter
    private final ChannelManager channelManager;

    @Getter
    private final Map<Audience, PlayerConfiguration> playerConfigurations;

    @Getter
    private final HermesConfig configuration;

    @Getter
    private final Path dataFolder;

    @Inject
    public Hermes(ProxyServer proxy, Logger logger, @DataDirectory Path dataFolder) {
        this.logger = logger;
        this.proxy = proxy;
        this.playerConfigurations = new HashMap<>();
        this.dataFolder = dataFolder;
        this.configuration = readConfig();
        this.channelManager = new ChannelManager(configuration, proxy, logger, playerConfigurations);
        registerServerChannels();
    }

    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        this.luckPerms = LuckPermsProvider.get();
        final EventBus luckPermsEventbus = luckPerms.getEventBus();
        luckPermsEventbus.subscribe(this, UserDataRecalculateEvent.class, this::onLuckPermsRecalculate);

        logger.info("Hermes has been initialized.");

        //register commands
        final CommandManager commandManager = proxy.getCommandManager();
        final BrigadierCommand helpBrigadierCommand = HermesCommand.createBrigadierCommand(configuration.getCommandFeedbackMessages());
        final CommandMeta helpCommandMeta = commandManager.metaBuilder(helpBrigadierCommand).plugin(this).build();
        commandManager.register(helpCommandMeta, helpBrigadierCommand);

        //message command
        //final BrigadierCommand privateMessageBrigadierCommand = PrivateMessageCommand.createBrigadierCommand(proxy, messageEndpointManager, logger, configuration.getMessageFormats());
        final BrigadierCommand privateMessageBrigadierCommand = PrivateMessageCommand.createBrigadierCommand(proxy, channelManager, logger, configuration.getCommandFeedbackMessages());
        final CommandMeta privateMessageCommandMeta = commandManager.metaBuilder(privateMessageBrigadierCommand).aliases("msg", "tell").plugin(this).build();
        commandManager.register(privateMessageCommandMeta, privateMessageBrigadierCommand);

        //reply command
        final BrigadierCommand replyBrigadierCommand = PrivateMessageReplyCommand.createBrigadierCommand(proxy, logger, configuration.getCommandFeedbackMessages(), channelManager);
        final CommandMeta replyCommandMeta = commandManager.metaBuilder(replyBrigadierCommand).aliases("r").plugin(this).build();
        commandManager.register(replyCommandMeta, replyBrigadierCommand);

        //socialSpy command
        final BrigadierCommand socialSpyBrigadierCommand = SocialSpyCommand.createBrigadierCommand(channelManager, configuration.getCommandFeedbackMessages());
        final CommandMeta socialSpyCommandMeta = commandManager.metaBuilder(replyBrigadierCommand).aliases("sspy").plugin(this).build();
        commandManager.register(socialSpyCommandMeta, socialSpyBrigadierCommand);

        //register events
        proxy.getEventManager().register(this, new PlayerChatListener(proxy, logger, configuration, channelManager, playerConfigurations));
        proxy.getEventManager().register(this, new PlayerJoinedListener(playerConfigurations, channelManager, luckPerms, proxy, configuration));
        proxy.getEventManager().register(this, new PlayerLeftListener(channelManager, configuration, playerConfigurations));
    }


    public void onLuckPermsRecalculate(final UserDataRecalculateEvent event){
        LuckPermsUserDataRecalculateListener.onLuckPermsUserDataRecalculate(event, playerConfigurations);
    }

    /**
     * Register named channels for each connected server to {@link #channelManager}.
     */
    private void registerServerChannels() {
        Collection<RegisteredServer> registeredServers = proxy.getAllServers();
        for(RegisteredServer server : registeredServers) {
            channelManager.registerServerChannel(server.getServerInfo().getName());
        }
    }

    private HermesConfig readConfig() {
        //get file
        boolean configFileAvailable = true;
        assert dataFolder != null;
        final File file = new File(dataFolder.toString(), "config.yml");
        if(!file.exists())
            configFileAvailable = initConfigFile(file);
        if(!configFileAvailable) {
            logger.error("could not get config file");
            return new HermesConfig(Hermes.version);
        }

        ObjectMapper mapper = new YAMLMapper();
        try{
            return mapper.readValue(file, HermesConfig.class);
        } catch (IOException e) {
            logger.error("error while reading config file: " + e.getMessage());
            return new HermesConfig(Hermes.version);
        }
    }

    private boolean initConfigFile(final File file) {
        //create file
        try{
            if(!file.getParentFile().exists() && !file.getParentFile().mkdir()) {
                logger.error("could not create config directory");
                return false;
            }
            if(!file.createNewFile()) {
                logger.error("could not create config file");
                return false;
            }
        } catch (IOException e) {
            logger.error("error while creating config file");
            return false;
        }

        //create default config
        HermesConfig config = new HermesConfig(Hermes.version);
        config.setDefaults();

        YAMLMapper mapper = new YAMLMapper();
        try {
            mapper.writeValue(file, config);
        } catch (IOException e) {
            logger.error("error while initializing config file");
            return false;
        }

        return true;
    }
}
