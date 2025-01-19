package dev.atlasmc.hermes;

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
import dev.atlasmc.hermes.command.*;
import dev.atlasmc.hermes.listener.LuckPermsUserDataRecalculateListener;
import dev.atlasmc.hermes.listener.PlayerJoinedListener;
import dev.atlasmc.hermes.listener.PlayerLeftListener;
import dev.atlasmc.hermes.model.channel.ChannelManager;
import dev.atlasmc.hermes.model.config.CommandAliases;
import dev.atlasmc.hermes.model.config.HermesConfig;
import dev.atlasmc.hermes.model.config.messageConfig.CommandFeedback;
import dev.atlasmc.hermes.model.config.runtime.PlayerConfiguration;
import lombok.Getter;
import dev.atlasmc.hermes.listener.PlayerChatListener;
import net.kyori.adventure.audience.Audience;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import org.slf4j.Logger;

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
    private final Map<Audience, PlayerConfiguration> playerConfigurations = new HashMap<>();

    @Getter
    private final HermesConfig configuration;

    @Getter
    private final Path dataFolder;

    @Inject
    public Hermes(final ProxyServer proxy, final Logger logger, @DataDirectory final Path dataFolder) {
        this.logger = logger;
        this.proxy = proxy;
        this.dataFolder = dataFolder;

        //get a config file
        assert dataFolder != null;
        HermesConfig tmpConfig = HermesConfig.getConfig(dataFolder, logger);
        if(tmpConfig == null) {
            logger.error("Could not get or create a config file. This session will use a default config instead.");
            this.configuration = new HermesConfig();
        } else {
            this.configuration = HermesConfig.getConfig(dataFolder, logger);
        }

        this.channelManager = new ChannelManager(configuration, proxy, logger, playerConfigurations);
        this.registerServerChannels();
    }

    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        //register luckperms and luckperms events
        this.luckPerms = LuckPermsProvider.get();
        final EventBus luckPermsEventbus = luckPerms.getEventBus();
        luckPermsEventbus.subscribe(this, UserDataRecalculateEvent.class, (UserDataRecalculateEvent recalculateEvent) -> {
            LuckPermsUserDataRecalculateListener.onLuckPermsUserDataRecalculate(recalculateEvent, this.playerConfigurations);
        });

        logger.info("Hermes has been initialized.");

        // register commands
        final CommandFeedback commandFeedback = configuration.getMessageConfig().getCommandFeedback();
        final CommandAliases commandAliases = configuration.getCommandAliases();
        this.registerCommand(HermesCommand.createBrigadierCommand(commandFeedback.getHermesCommandFeedback()),
                commandAliases.getHermesCommandAliases());
        this.registerCommand(PrivateMessageCommand.createBrigadierCommand(proxy, channelManager, commandFeedback.getPrivateMessageCommandFeedback()),
                commandAliases.getPrivateMessageCommandAliases());
        this.registerCommand(PrivateMessageReplyCommand.createBrigadierCommand(proxy, commandFeedback.getPrivateMessageReplyCommandFeedback(), channelManager),
                commandAliases.getPrivateMessageReplyCommandAliases());
        this.registerCommand(SocialSpyCommand.createBrigadierCommand(channelManager, commandFeedback.getSocialSpyCommandFeedback()),
                commandAliases.getSocialSpyCommandAliases());

        // register events
        proxy.getEventManager().register(this, new PlayerChatListener(proxy, logger, configuration, channelManager, playerConfigurations));
        proxy.getEventManager().register(this, new PlayerJoinedListener(playerConfigurations, channelManager, luckPerms, proxy, configuration));
        proxy.getEventManager().register(this, new PlayerLeftListener(channelManager, configuration, playerConfigurations));
    }

    /**
     * Registers a command to the commandManager.
     * @param brigadierCommand the command to register.
     * @param commandAliases the aliases to register for this command.
     */
    private void registerCommand(final BrigadierCommand brigadierCommand, final String[] commandAliases) {
        final CommandManager commandManager = proxy.getCommandManager();
        final CommandMeta commandMeta = commandManager.metaBuilder(brigadierCommand).aliases(commandAliases).plugin(this).build();
        commandManager.register(commandMeta, brigadierCommand);
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
}
