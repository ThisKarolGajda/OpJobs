package me.opkarol.effects;

import me.opkarol.opc.api.tools.runnable.OpRunnable;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleManager {

    public static void particleCircleSpellEffect(Player player) {
        Location location = player.getLocation();
        new OpRunnable(r -> {
            for (int degree = 0; degree < 360; degree++) {
                double radians = Math.toRadians(degree);
                double x = Math.cos(radians);
                double z = Math.sin(radians);
                location.add(x, 0, z);
                player.spawnParticle(Particle.SPELL, location, 3);
                location.subtract(x, 0, z);
            }
        }).runTaskAsynchronously();
    }
}
