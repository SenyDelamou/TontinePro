package groupe1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

/**
 * Utilitaires pour créer des animations fluides dans l'application
 */
public class AnimationUtils {

    // Durées d'animation standard (en millisecondes)
    public static final int DURATION_FAST = 200;
    public static final int DURATION_NORMAL = 300;
    public static final int DURATION_SLOW = 500;

    // FPS pour les animations
    private static final int FPS = 60;
    private static final int FRAME_DELAY = 1000 / FPS;

    /**
     * Easing Functions pour des animations naturelles
     */
    public static class Easing {
        public static double easeInOut(double t) {
            return t < 0.5 ? 2 * t * t : -1 + (4 - 2 * t) * t;
        }

        public static double easeOut(double t) {
            return t * (2 - t);
        }

        public static double easeIn(double t) {
            return t * t;
        }

        public static double linear(double t) {
            return t;
        }

        public static double easeOutBounce(double t) {
            if (t < (1 / 2.75)) {
                return 7.5625 * t * t;
            } else if (t < (2 / 2.75)) {
                return 7.5625 * (t -= (1.5 / 2.75)) * t + 0.75;
            } else if (t < (2.5 / 2.75)) {
                return 7.5625 * (t -= (2.25 / 2.75)) * t + 0.9375;
            } else {
                return 7.5625 * (t -= (2.625 / 2.75)) * t + 0.984375;
            }
        }
    }

    /**
     * Anime un compteur numérique de start à end
     */
    public static void animateCounter(JLabel label, int start, int end, int duration, String suffix) {
        Timer timer = new Timer(FRAME_DELAY, null);
        final long startTime = System.currentTimeMillis();

        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                double progress = Math.min(1.0, (double) elapsed / duration);
                double easedProgress = Easing.easeOut(progress);

                int currentValue = (int) (start + (end - start) * easedProgress);
                label.setText(currentValue + suffix);

                if (progress >= 1.0) {
                    timer.stop();
                }
            }
        });

        timer.start();
    }

    /**
     * Anime l'opacité d'un composant (fade in/out)
     */
    public static void fadeIn(JComponent component, int duration) {
        component.setVisible(true);
        animateOpacity(component, 0.0f, 1.0f, duration);
    }

    public static void fadeOut(JComponent component, int duration, Runnable onComplete) {
        animateOpacity(component, 1.0f, 0.0f, duration, () -> {
            component.setVisible(false);
            if (onComplete != null)
                onComplete.run();
        });
    }

    private static void animateOpacity(JComponent component, float start, float end, int duration) {
        animateOpacity(component, start, end, duration, null);
    }

    private static void animateOpacity(JComponent component, float start, float end, int duration,
            Runnable onComplete) {
        Timer timer = new Timer(FRAME_DELAY, null);
        final long startTime = System.currentTimeMillis();

        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                double progress = Math.min(1.0, (double) elapsed / duration);
                double easedProgress = Easing.easeInOut(progress);

                float currentOpacity = (float) (start + (end - start) * easedProgress);
                component.setOpaque(currentOpacity >= 1.0f);
                component.repaint();

                if (progress >= 1.0) {
                    timer.stop();
                    if (onComplete != null)
                        onComplete.run();
                }
            }
        });

        timer.start();
    }

    /**
     * Anime une valeur de start à end et appelle le consumer à chaque frame
     */
    public static void animate(double start, double end, int duration, Consumer<Double> onUpdate, Runnable onComplete) {
        Timer timer = new Timer(FRAME_DELAY, null);
        final long startTime = System.currentTimeMillis();

        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                double progress = Math.min(1.0, (double) elapsed / duration);
                double easedProgress = Easing.easeInOut(progress);

                double currentValue = start + (end - start) * easedProgress;
                onUpdate.accept(currentValue);

                if (progress >= 1.0) {
                    timer.stop();
                    if (onComplete != null)
                        onComplete.run();
                }
            }
        });

        timer.start();
    }

    /**
     * Crée une animation de pulsation continue
     */
    public static Timer createPulseAnimation(JComponent component, int duration) {
        Timer timer = new Timer(FRAME_DELAY, null);
        final long startTime = System.currentTimeMillis();

        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                double progress = (elapsed % duration) / (double) duration;

                // Pulsation: 0 -> 1 -> 0
                double pulse = Math.sin(progress * Math.PI * 2);
                component.putClientProperty("pulseValue", pulse);
                component.repaint();
            }
        });

        return timer;
    }

    /**
     * Anime le déplacement d'un composant
     */
    public static void slideIn(JComponent component, int startX, int startY, int endX, int endY, int duration) {
        component.setLocation(startX, startY);
        component.setVisible(true);

        Timer timer = new Timer(FRAME_DELAY, null);
        final long startTime = System.currentTimeMillis();

        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                double progress = Math.min(1.0, (double) elapsed / duration);
                double easedProgress = Easing.easeOut(progress);

                int currentX = (int) (startX + (endX - startX) * easedProgress);
                int currentY = (int) (startY + (endY - startY) * easedProgress);
                component.setLocation(currentX, currentY);

                if (progress >= 1.0) {
                    timer.stop();
                }
            }
        });

        timer.start();
    }

    /**
     * Crée un effet de brillance qui traverse le composant
     */
    public static Timer createShineEffect(JComponent component, int duration, int interval) {
        Timer timer = new Timer(interval, new ActionListener() {
            private long animationStart = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (animationStart == 0) {
                    animationStart = System.currentTimeMillis();
                }

                long elapsed = System.currentTimeMillis() - animationStart;
                if (elapsed >= duration) {
                    animationStart = 0;
                }

                component.repaint();
            }
        });

        return timer;
    }
}
