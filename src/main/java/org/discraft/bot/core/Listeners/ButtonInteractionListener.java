package org.discraft.bot.core.Listeners;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.discraft.bot.commands.control;

public class ButtonInteractionListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("refresh")) {
            control ctrl = new control();
            ctrl.handleButtonClick(event);
        }
    }
}

