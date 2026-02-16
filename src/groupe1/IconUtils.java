package groupe1;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * Utilitaires pour créer des icônes SVG-like en Java
 */
public class IconUtils {

    /**
     * Crée une icône de tableau de bord
     */
    public static Icon createDashboardIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);

                int gridSize = size / 3;
                int gap = 2;

                // 4 carrés formant une grille
                g2.fillRoundRect(x, y, gridSize, gridSize, 3, 3);
                g2.fillRoundRect(x + gridSize + gap, y, gridSize, gridSize, 3, 3);
                g2.fillRoundRect(x, y + gridSize + gap, gridSize, gridSize, 3, 3);
                g2.fillRoundRect(x + gridSize + gap, y + gridSize + gap, gridSize, gridSize, 3, 3);
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    /**
     * Crée une icône de groupe/utilisateurs
     */
    public static Icon createUsersIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f));

                // Trois silhouettes
                int headSize = size / 4;

                // Personne 1 (gauche)
                g2.drawOval(x + 2, y + 2, headSize, headSize);
                g2.drawArc(x, y + headSize, headSize + 4, headSize, 0, -180);

                // Personne 2 (centre)
                g2.drawOval(x + size / 2 - headSize / 2, y, headSize, headSize);
                g2.drawArc(x + size / 2 - headSize / 2 - 2, y + headSize - 2, headSize + 4, headSize, 0, -180);

                // Personne 3 (droite)
                g2.drawOval(x + size - headSize - 2, y + 2, headSize, headSize);
                g2.drawArc(x + size - headSize - 4, y + headSize, headSize + 4, headSize, 0, -180);
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    /**
     * Crée une icône de collecte/argent
     */
    public static Icon createMoneyIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f));

                // Cercle extérieur
                g2.drawOval(x + 2, y + 2, size - 4, size - 4);

                // Symbole dollar/franc
                int centerX = x + size / 2;
                int centerY = y + size / 2;

                g2.setFont(new Font("Segoe UI", Font.BOLD, size / 2));
                FontMetrics fm = g2.getFontMetrics();
                String symbol = "FG";
                int textX = centerX - fm.stringWidth(symbol) / 2;
                int textY = centerY + fm.getAscent() / 2;
                g2.drawString(symbol, textX, textY);
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    /**
     * Crée une icône de compte/portefeuille
     */
    public static Icon createWalletIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f));

                // Rectangle arrondi (portefeuille)
                g2.drawRoundRect(x + 2, y + size / 4, size - 4, size / 2, 5, 5);

                // Rabat supérieur
                Path2D flap = new Path2D.Double();
                flap.moveTo(x + 2, y + size / 4);
                flap.lineTo(x + 2, y + 5);
                flap.quadTo(x + size / 2, y + 2, x + size - 2, y + 5);
                flap.lineTo(x + size - 2, y + size / 4);
                g2.draw(flap);

                // Bouton
                g2.fillOval(x + size - 8, y + size / 2 - 2, 4, 4);
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    /**
     * Crée une icône de paramètres/engrenage
     */
    public static Icon createSettingsIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);

                int centerX = x + size / 2;
                int centerY = y + size / 2;
                int outerRadius = size / 2 - 2;
                int innerRadius = size / 4;

                // Engrenage simplifié (cercle avec dents)
                Path2D gear = new Path2D.Double();
                int teeth = 8;

                for (int i = 0; i < teeth; i++) {
                    double angle1 = (i * 2 * Math.PI / teeth);
                    double angle2 = ((i + 0.5) * 2 * Math.PI / teeth);

                    int x1 = centerX + (int) (outerRadius * Math.cos(angle1));
                    int y1 = centerY + (int) (outerRadius * Math.sin(angle1));
                    int x2 = centerX + (int) (innerRadius * Math.cos(angle2));
                    int y2 = centerY + (int) (innerRadius * Math.sin(angle2));

                    if (i == 0) {
                        gear.moveTo(x1, y1);
                    } else {
                        gear.lineTo(x1, y1);
                    }
                    gear.lineTo(x2, y2);
                }
                gear.closePath();

                g2.fill(gear);

                // Trou central
                g2.setColor(c.getBackground());
                g2.fillOval(centerX - innerRadius / 2, centerY - innerRadius / 2, innerRadius, innerRadius);
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    /**
     * Crée une icône de profil/utilisateur
     */
    public static Icon createUserIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f));

                int headSize = size / 3;
                int centerX = x + size / 2;

                // Tête
                g2.drawOval(centerX - headSize / 2, y + 4, headSize, headSize);

                // Corps (arc)
                g2.drawArc(x + 4, y + headSize, size - 8, size - headSize - 4, 0, -180);
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    /**
     * Crée une icône de flèche vers le haut (tendance positive)
     */
    public static Icon createArrowUpIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f));

                int centerX = x + size / 2;

                // Ligne verticale
                g2.drawLine(centerX, y + size - 2, centerX, y + 4);

                // Pointe de flèche
                g2.drawLine(centerX, y + 4, centerX - 4, y + 8);
                g2.drawLine(centerX, y + 4, centerX + 4, y + 8);
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    /**
     * Crée une icône de flèche vers le bas (tendance négative)
     */
    public static Icon createArrowDownIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f));

                int centerX = x + size / 2;

                // Ligne verticale
                g2.drawLine(centerX, y + 2, centerX, y + size - 4);

                // Pointe de flèche
                g2.drawLine(centerX, y + size - 4, centerX - 4, y + size - 8);
                g2.drawLine(centerX, y + size - 4, centerX + 4, y + size - 8);
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    /**
     * Crée une icône de poignée de main (prêts/accords)
     */
    public static Icon createHandshakeIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f));

                // Forme simplifiée de deux mains se rejoignant
                g2.drawArc(x + 2, y + size / 2 - 4, size - 4, size / 2, 0, 180);
                g2.drawArc(x + 2, y + size / 2 - 4, size - 4, size / 2, 0, -180);
                g2.drawLine(x + 4, y + size / 2, x + size - 4, y + size / 2);
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }
}
