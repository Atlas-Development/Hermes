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
import dev.atlasmc.hermes.constant.FileConstants;
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
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.HeaderMode;

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

        //try to get the config file
        assert dataFolder != null;
        final File file = new File(dataFolder.toString(), FileConstants.hermesConfigFileName);
        this.configuration = readConfig(file);
        this.channelManager = new ChannelManager(configuration, proxy, logger, playerConfigurations);
        registerServerChannels();
    }

    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        this.luckPerms = LuckPermsProvider.get();
        final EventBus luckPermsEventbus = luckPerms.getEventBus();
        luckPermsEventbus.subscribe(this, UserDataRecalculateEvent.class, this::onLuckPermsRecalculate);

        logger.info("Hermes has been initialized.");
        final CommandFeedback commandFeedback = configuration.getMessageConfig().getCommandFeedback();
        final CommandAliases commandAliases = configuration.getCommandAliases();

        //register commands
        final CommandManager commandManager = proxy.getCommandManager();
        final BrigadierCommand helpBrigadierCommand = HermesCommand.createBrigadierCommand(commandFeedback.getHermesCommandFeedback());
        final CommandMeta helpCommandMeta = commandManager.metaBuilder(helpBrigadierCommand).aliases(commandAliases.getHermesCommandAliases()).plugin(this).build();
        commandManager.register(helpCommandMeta, helpBrigadierCommand);

        //message command
        final BrigadierCommand privateMessageBrigadierCommand = PrivateMessageCommand.createBrigadierCommand(proxy, channelManager, logger, commandFeedback.getPrivateMessageCommandFeedback());
        final CommandMeta privateMessageCommandMeta = commandManager.metaBuilder(privateMessageBrigadierCommand).aliases(commandAliases.getPrivateMessageCommandAliases()).plugin(this).build();
        commandManager.register(privateMessageCommandMeta, privateMessageBrigadierCommand);

        //reply command
        final BrigadierCommand replyBrigadierCommand = PrivateMessageReplyCommand.createBrigadierCommand(proxy, logger, commandFeedback.getPrivateMessageReplyCommandFeedback(), channelManager);
        final CommandMeta replyCommandMeta = commandManager.metaBuilder(replyBrigadierCommand).aliases(commandAliases.getPrivateMessageReplyCommandAliases()).plugin(this).build();
        commandManager.register(replyCommandMeta, replyBrigadierCommand);

        //socialSpy command
        final BrigadierCommand socialSpyBrigadierCommand = SocialSpyCommand.createBrigadierCommand(channelManager, commandFeedback.getSocialSpyCommandFeedback());
        final CommandMeta socialSpyCommandMeta = commandManager.metaBuilder(socialSpyBrigadierCommand).aliases(commandAliases.getSocialSpyCommandAliases()).plugin(this).build();
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

    /**
     * Tries to read the specified file and creates the file/directory if it does not exist yet.
     * @param file the file to read.
     * @return the config file that was read or created. returns null if an error occurred in the process.
     */
    private HermesConfig readConfig(final File file) {
        //create a new config file if none was found
        if(!file.exists()){
            this.logger.warn("No config found for Hermes. Creating a new config file in the path: \"{}\".", file.getAbsolutePath());
            return createConfigFile(file);
        }
        //read the existing config file
        try{
            return deserializeConfig(file);
        } catch (ConfigurateException e) {
            this.logger.error("Something went wrong while reading the config file at: \"{}\".\ndetails:\n{}", file.getAbsolutePath(), e.toString());
            return null;
        }
    }

    private HermesConfig deserializeConfig(final File file) throws ConfigurateException {
        final Path path = Path.of(file.getAbsolutePath());
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(path)
                .build();
        final CommentedConfigurationNode node = loader.load();
        return node.get(HermesConfig.class);
    }

    private HermesConfig serializeConfig(final File file, final HermesConfig config) throws ConfigurateException {
        final Path path = Path.of(file.getAbsolutePath());

        ConfigurationOptions configurationOptions = ConfigurationOptions
                .defaults()
                .header("===========================================================\n" +
                        "This is an automatically generated config file.\n" +
                        "Generated for config version \"" + HermesConfig.configurationVersion + "\"\n" +
                        "===========================================================");

        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(path)
                .prettyPrinting(true)
                .headerMode(HeaderMode.PRESERVE)
                .defaultOptions(configurationOptions)
                .build();

        final CommentedConfigurationNode node = loader.load();
        node.set(HermesConfig.class, config);
        loader.save(node);
        return config;
    }

    /**
     * Writes the config to the specified file.
     * @param file the file to write to. Creates a new file/directory if it does not exist yet.
     * @return The config object that was written to the file or null if the creation wasn't successful.
     */
    private HermesConfig createConfigFile(final File file) {
        //create file
        try{
            if(!file.getParentFile().exists() && !file.getParentFile().mkdir()) {
                this.logger.error("could not create config directory");
                return null;
            }
            if(!file.createNewFile()) {
                this.logger.error("could not create config file");
                return null;
            }
        } catch (IOException e) {
            this.logger.error("error while creating config file");
            return null;
        }

        final HermesConfig config = new HermesConfig();
        try{
            this.serializeConfig(file, config);
        } catch (ConfigurateException e) {
            this.logger.warn("something went wrong while trying to write to the config file...\ndetails:\n{}", e.getMessage());
        }
        return config;
    }
}
