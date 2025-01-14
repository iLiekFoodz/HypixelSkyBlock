package net.swofty.types.generic.command.commands;

import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.mongodb.OrbDatabase;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "deleteorb",
        description = "Deletes orbs at the player's location.",
        usage = "/removeorb",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class RemoveOrbCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            new OrbDatabase().removeOrbs(((SkyBlockPlayer) sender).getPosition(), 1.5);
        });
    }
}
