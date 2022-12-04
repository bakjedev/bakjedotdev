package me.bakje.bakjedev.bakjedev.module.render;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.event.events.EntityRenderEvent;
import me.bakje.bakjedev.bakjedev.event.events.WorldRenderEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.NumberSetting;
import me.bakje.bakjedev.bakjedev.util.render.Renderer;
import me.bakje.bakjedev.bakjedev.util.render.WorldRenderer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Nametags extends Mod {
    public NumberSetting size = new NumberSetting("Size", 1, 5, 2, 1);


    private ExecutorService uuidExecutor;
    private Map<UUID, Future<String>> uuidFutures = new HashMap<>();

    private Queue<UUID> uuidQueue = new ArrayDeque<>();
    private Map<UUID, String> uuidCache = new HashMap<>();
    private Set<UUID> failedUUIDs = new HashSet<>();
    private long lastLookup = 0;
    public Nametags() {
        super("Nametags", "custom nametags", Category.RENDER, true);
        addSetting(size);
    }
    //ok so this module basically steals your future account and gives me all the revenue! learned how to do it from bleach
    @Override
    public void onDisable() {
        uuidQueue.clear();
        failedUUIDs.clear();
        uuidExecutor.shutdownNow();
        uuidFutures.clear();

        Map<UUID, String> cacheCopy = new HashMap<>(uuidCache);
        uuidCache.clear();

        cacheCopy.forEach((u, s) -> {
            if (!s.startsWith("\u00a7c")) uuidCache.put(u, s);
        });
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        uuidExecutor = Executors.newFixedThreadPool(4);
    }

    @Override
    public void onTick() {
        for (Entry<UUID, Future<String>> f: new HashMap<>(uuidFutures).entrySet()) {
            if (f.getValue().isDone()) {
                try {
                    String s = f.getValue().get();
                    uuidCache.put(f.getKey(), s);

                    uuidFutures.remove(f.getKey());
                } catch (InterruptedException | ExecutionException ignored) {

                }
            }
        }
        if (!uuidQueue.isEmpty() && System.currentTimeMillis() - lastLookup > 1000) {
            lastLookup=System.currentTimeMillis();
            addUUIDFuture(uuidQueue.poll());
        }
        super.onTick();
    }

    @BakjeSubscribe
    public void onLivingLabelRender(EntityRenderEvent.Single.Label event) {
        if (event.getEntity() instanceof PlayerEntity) {
            event.setCancelled(true);
        }
    }

    @BakjeSubscribe
    public void onWorldRender(WorldRenderEvent.Post event) {
        for (Entity entity: mc.world.getEntities()) {
            if (entity == mc.player || entity.hasPassenger(mc.player) || mc.player.hasPassenger(entity)) {
                continue;
            }

            Vec3d rPos = entity.getPos().subtract(Renderer.getInterpolationOffset(entity)).add(0, entity.getHeight() + 0.25, 0);

            if (entity instanceof PlayerEntity) {
                double scale = Math.max(this.size.getValue() * (mc.cameraEntity.distanceTo(entity) / 20), 1);

                List<Text> lines = getPlayerLines((PlayerEntity) entity);
                drawLines(rPos.x, rPos.y, rPos.z, scale, lines);
            }
        }
    }

    private void drawLines(double x, double y, double z, double scale, List<Text> lines) {
        double offset = lines.size() * 0.25 * scale;

        for (Text t: lines) {
            WorldRenderer.drawText(t, x, y + offset, z, scale, true);
            offset -= 0.25 * scale;
        }
    }

    private void drawItems(double x, double y, double z, double scale, List<ItemStack> items) {
        double lscale = scale * 0.4;

        for (int i = 0; i < items.size(); i++) {
            drawItem(x, y, z, i + 0.5 - items.size() / 2d, 0, lscale, items.get(i));
        }
    }

    private void drawItem(double x, double y, double z, double offX, double offY, double scale, ItemStack item) {
        if (item.isEmpty())
            return;

        WorldRenderer.drawGuiItem(x, y, z, offX * scale, offY * scale, scale, item);

        double w = mc.textRenderer.getWidth("x" + item.getCount()) / 52d;
        WorldRenderer.drawText(Text.literal("x" + item.getCount()),
                x, y, z, (offX - w) * scale, (offY - 0.07) * scale, scale * 1.75, false);

        int c = 0;
        for (Entry<Enchantment, Integer> m : EnchantmentHelper.get(item).entrySet()) {
            String text = I18n.translate(m.getKey().getName(2).getString());

            if (text.isEmpty())
                continue;

            text = text.replaceFirst("Curse of (.)", "C$1");

            String subText = text.substring(0, Math.min(text.length(), 2)) + m.getValue();

            WorldRenderer.drawText(Text.literal(subText).styled(s -> s.withColor(TextColor.fromRgb(m.getKey().isCursed() ? 0xff5050 : 0xffb0e0))),
                    x, y, z, (offX + 0.02) * scale, (offY + 0.75 - c * 0.34) * scale, scale * 1.4, false);
            c--;
        }
    }

    private List<ItemStack> getMainEquipment(Entity e) {
        List<ItemStack> list = Lists.newArrayList(e.getItemsEquipped());
        list.add(list.remove(1));
        return list;
    }

    public List<Text> getPlayerLines(PlayerEntity player) {
        List<Text> lines = new ArrayList<>();
        List<Text> mainText = new ArrayList<>();

        PlayerListEntry playerEntry = mc.player.networkHandler.getPlayerListEntry(player.getGameProfile().getId());

        if (playerEntry != null) { // Ping
            mainText.add(Text.literal(playerEntry.getLatency() + "ms").formatted(Formatting.GRAY));
        }


        mainText.add(((MutableText) player.getName()).formatted(Formatting.WHITE));

        mainText.add(getHealthText(player));


        if (!mainText.isEmpty())
            lines.add(Texts.join(mainText, Text.literal(" ")));

        return lines;
    }

    private Text getHealthText(LivingEntity e) {
        int totalHealth = (int) (e.getHealth() + e.getAbsorptionAmount());
        return Text.literal(Integer.toString(totalHealth)).styled(s -> s.withColor(getHealthColor(e)));
    }

    private int getHealthColor(LivingEntity entity) {
        if (entity.getHealth() + entity.getAbsorptionAmount() > entity.getMaxHealth()) {
            return Formatting.YELLOW.getColorValue();
        } else {
            return MathHelper.hsvToRgb((entity.getHealth() + entity.getAbsorptionAmount()) / (entity.getMaxHealth() * 3), 1f, 1f);
        }
    }

    private void addUUIDFuture(UUID uuid) {
        uuidFutures.put(uuid, uuidExecutor.submit(() -> {
            try {
                String url = "https://api.mojang.com/user/profiles/" + uuid.toString().replace("-", "") + "/names";
                String response = Resources.toString(new URL(url), StandardCharsets.UTF_8);
                Bakjedev.logger.info("bruh uuid time: " + url);

                JsonElement json = JsonParser.parseString(response);

                if (!json.isJsonArray()) {
                    Bakjedev.logger.error("[Nametags] Invalid Owner UUID: " + uuid);
                    return "\u00a7c[Invalid]";
                }

                JsonArray ja = json.getAsJsonArray();

                return ja.get(ja.size() - 1).getAsJsonObject().get("name").getAsString();
            } catch (IOException e) {
                Bakjedev.logger.error("[Nametags] Error Getting Owner UUID: " + uuid);
                return "\u00a7c[Error]";
            }
        }));
    }
}