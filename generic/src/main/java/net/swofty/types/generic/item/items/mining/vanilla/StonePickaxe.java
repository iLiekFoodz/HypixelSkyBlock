package net.swofty.types.generic.item.items.mining.vanilla;

import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;
import net.swofty.types.generic.item.ReforgeType;

import java.util.List;

public class StonePickaxe implements CustomSkyBlockItem, MiningTool, ExtraRarityDisplay, Enchantable, Reforgable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.MINING_SPEED, 110D)
                .with(ItemStatistic.DAMAGE, 20D)
                .build();
    }

    @Override
    public int getBreakingPower() {
        return 2;
    }

    @Override
    public String getExtraRarityDisplay() {
        return " PICKAXE";
    }

    @Override
    public boolean showEnchantLores() {
        return true;
    }

    @Override
    public List<EnchantItemGroups> getEnchantItemGroups() {
        return List.of(EnchantItemGroups.PICKAXE, EnchantItemGroups.TOOLS);
    }

    @Override
    public ReforgeType getReforgeType() {
        return ReforgeType.PICKAXES;
    }
}

