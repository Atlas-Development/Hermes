package dev.atlasmc.hermes.model.config.runtime;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;

import java.util.ArrayList;
import java.util.Collection;

public abstract class ChannelConfiguration {
    @Getter
    private final Collection<String> receivingNamedChannelNames;
    /**
     * this needs to be kept up to date manually
     */
    @Setter
    @Getter
    private Audience currentAudience;

    public ChannelConfiguration() {
        this.receivingNamedChannelNames = new ArrayList<>();
    }

    public void addReceivingNamedChannelName(final String name) {
        receivingNamedChannelNames.add(name);
    }
}
