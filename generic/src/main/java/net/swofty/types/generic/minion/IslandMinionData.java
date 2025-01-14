package net.swofty.types.generic.minion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.SharedInstance;
import net.swofty.types.generic.entity.MinionEntityImpl;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeMinionData;
import net.swofty.types.generic.user.SkyBlockIsland;

import java.util.*;

@Getter
public class IslandMinionData {
    private final SkyBlockIsland island;
    private final List<IslandMinion> minions = new ArrayList<>();

    public IslandMinionData(SkyBlockIsland island) {
        this.island = island;
    }

    public IslandMinion initializeMinion(
            Pos position, MinionRegistry minion, ItemAttributeMinionData.MinionData previousData) {
        IslandMinion islandMinion = new IslandMinion(
                UUID.randomUUID(), position, minion,
                previousData.tier(), new ArrayList<>(),
                previousData.generatedResources(),
                System.currentTimeMillis(), null,
                new MinionHandler.InternalMinionTags());
        minions.add(islandMinion);
        return islandMinion;
    }

    public void spawn(IslandMinion minion) {
        minion.spawnMinion(getIsland().getSharedInstance().join());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class IslandMinion {
        private final UUID minionUUID;
        private final Pos position;
        private final MinionRegistry minion;
        private final int tier;
        private List<MaterialQuantifiable> itemsInMinion;
        private int generatedItems;
        private long lastAction;
        private MinionEntityImpl minionEntity;
        private MinionHandler.InternalMinionTags internalMinionTags;

        public void spawnMinion(SharedInstance instance) {
            minionEntity = new MinionEntityImpl(this, minion.asSkyBlockMinion());
            minionEntity.setInstance(instance, position.add(0.5, 0, 0.5));
        }

        public void removeMinion() {
            internalMinionTags.onMinionDespawn(this);
            minionEntity.remove();
        }

        public void addItem(SkyBlockItem item) {
            setGeneratedItems(getGeneratedItems() + item.getAmount());

            if (itemsInMinion.stream().anyMatch(materialQuantifiable -> materialQuantifiable.getMaterial() == item.getAttributeHandler().getItemTypeAsType())) {
                itemsInMinion.stream()
                        .filter(materialQuantifiable -> materialQuantifiable.getMaterial() == item.getAttributeHandler().getItemTypeAsType())
                        .findFirst()
                        .get()
                        .setAmount(itemsInMinion.stream()
                                .filter(materialQuantifiable -> materialQuantifiable.getMaterial() == item.getAttributeHandler().getItemTypeAsType())
                                .findFirst()
                                .get().getAmount() + item.getAmount());
                return;
            }

            itemsInMinion.add(new MaterialQuantifiable(item.getAttributeHandler().getItemTypeAsType(), item.getAmount()));
        }

        public SkyBlockItem asSkyBlockItem() {
            SkyBlockItem toReturn = new SkyBlockItem(getMinion().getItemType());
            toReturn.getAttributeHandler().setMinionData(new ItemAttributeMinionData.MinionData(
                    getTier(),
                    getGeneratedItems()
            ));
            return toReturn;
        }

        public Map<String, Object> serialize() {
            List<String> itemsInMinionAsString = new ArrayList<>();
            itemsInMinion.forEach(item -> {
                itemsInMinionAsString.add(item.getMaterial().name() + "," + item.getAmount());
            });

            Map<String, Object> data = new HashMap<>();
            data.put("position", position.blockX() + "," + position.blockY() + "," + position.blockZ());
            data.put("itemsInMinion", itemsInMinionAsString);
            data.put("minion", minion.name());
            data.put("lastAction", lastAction);
            data.put("tier", tier);
            data.put("generatedItems", generatedItems);
            data.put("minionUUID", minionUUID.toString());
            return data;
        }

        public static IslandMinion deserialize(Map<String, Object> data) {
            List<MaterialQuantifiable> itemsInMinion = new ArrayList<>();
            ((List<String>) data.get("itemsInMinion")).forEach(item -> {
                itemsInMinion.add(new MaterialQuantifiable(
                        ItemType.valueOf(item.split(",")[0]),
                        Integer.parseInt(item.split(",")[1])
                ));
            });

            return new IslandMinion(
                    UUID.fromString(data.get("minionUUID").toString()),
                    new Pos(
                            Integer.parseInt(data.get("position").toString().split(",")[0]),
                            Integer.parseInt(data.get("position").toString().split(",")[1]),
                            Integer.parseInt(data.get("position").toString().split(",")[2])
                    ),
                    MinionRegistry.valueOf(data.get("minion").toString()),
                    (int) data.get("tier"),
                    itemsInMinion,
                    (int) data.get("generatedItems"),
                    System.currentTimeMillis(),
                    null,
                    new MinionHandler.InternalMinionTags()
            );
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();

        minions.forEach(minion -> {
            data.put(minion.minionUUID.toString(), minion.serialize());
        });

        return data;
    }

    public static IslandMinionData deserialize(Map<String, Object> data, SkyBlockIsland island) {
        IslandMinionData islandMinionData = new IslandMinionData(island);
        data.forEach((uuid, minionData) -> {
            islandMinionData.minions.add(IslandMinion.deserialize((Map<String, Object>) minionData));
        });
        return islandMinionData;
    }
}
