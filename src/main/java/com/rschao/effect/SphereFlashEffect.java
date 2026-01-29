package com.rschao.effect;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.ParticleEffect;
import de.slikey.effectlib.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Random;

public class SphereFlashEffect extends ParticleEffect {
    // campos de control
    private int tickCounter = 0;
    private final int expandTicks = 10;   // ~0.5s (10 ticks)
    private final int waitTicks = 0;     // 0.5s espera entre fases
    private final int shrinkTicks = 10;   // ~0.5s
    private final int scatterTicks = 10;  // ~0.5s dispersión
    private final double maxRadius = 1.3;
    private final Random random = new Random();

    public SphereFlashEffect(EffectManager effectManager) {
        super(effectManager);
        this.particle = ParticleUtil.getParticle("SPELL_WITCH");
        // ajustar iteraciones al total de fases y periodo a 1 tick (expansión + espera + contracción + espera + dispersión)
        this.iterations = expandTicks + waitTicks + shrinkTicks + waitTicks + scatterTicks;
        this.period = 1;
    }

    @Override
    public void onRun() {
        Location center = this.getLocation().clone();
        int currentTick = tickCounter++;

        int phaseExpandEnd = expandTicks;
        int phaseExpandWaitEnd = phaseExpandEnd + waitTicks;
        int phaseShrinkEnd = phaseExpandWaitEnd + shrinkTicks;
        int phaseShrinkWaitEnd = phaseShrinkEnd + waitTicks;
        // Fase de expansión
        if (currentTick < phaseExpandEnd) {
            double progress = (double)(currentTick + 1) / expandTicks; // 0..1
            double radius = progress * maxRadius;
            if (radius > 0) {
                drawSphereSurface(center, radius);
            }
            return;
        }
        // Espera entre expansión y contracción (0.5s) - no mostrar partículas
        if (currentTick < phaseExpandWaitEnd) {
            return;
        }
        // Fase de contracción
        if (currentTick < phaseShrinkEnd) {
            int t = currentTick - phaseExpandWaitEnd;
            double progress = 1.0 - (double)(t + 1) / shrinkTicks; // 1..0
            double radius = Math.max(0.0, progress * maxRadius);
            if (radius > 0) {
                drawSphereSurface(center, radius);
            }
            return;
        }
        // Espera entre contracción y dispersión (0.5s) - no mostrar partículas
        if (currentTick < phaseShrinkWaitEnd) {
            return;
        }
        // Fase de dispersión aleatoria dentro del radio máximo
        int particlesPerTick = 60;
        for (int i = 0; i < particlesPerTick; i++) {
            double u = random.nextDouble();
            double costheta = 2.0 * random.nextDouble() - 1.0;
            double sintheta = Math.sqrt(1.0 - costheta * costheta);
            double phi = 2.0 * Math.PI * random.nextDouble();
            double r = maxRadius * Math.cbrt(u); // raíz cúbica para uniforme en volumen
            double x = r * sintheta * Math.cos(phi);
            double y = r * costheta;
            double z = r * sintheta * Math.sin(phi);
            Vector vec = new Vector(x, y, z);
            this.display(this.particle, center.clone().add(vec));
        }
    }

    // dibuja la superficie de una esfera con paso angular para controlar densidad
    private void drawSphereSurface(Location center, double radius) {
        double step = Math.PI / 6.0; // ~30 grados; ajustar para más/menos densidad
        for (double theta = 0; theta <= Math.PI; theta += step) {
            double sinTheta = Math.sin(theta);
            double cosTheta = Math.cos(theta);
            for (double phi = 0; phi < 2.0 * Math.PI; phi += step) {
                double x = radius * sinTheta * Math.cos(phi);
                double y = radius * cosTheta;
                double z = radius * sinTheta * Math.sin(phi);
                Vector vec = new Vector(x, y, z);
                this.display(this.particle, center.clone().add(vec));
            }
        }
    }
}
