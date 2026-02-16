package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatPanel extends JPanel {

    private JPanel messageArea;
    private JTextField inputField;
    private JScrollPane scrollPane;

    public ChatPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleUtils.PRIMARY_BLUE);
        header.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel title = new JLabel("Support TontinePro");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);
        
        // Minimize Button (optional, but we'll control visibility from outside usually)
        
        add(header, BorderLayout.NORTH);

        // Messages Area
        messageArea = new JPanel();
        messageArea.setLayout(new BoxLayout(messageArea, BoxLayout.Y_AXIS));
        messageArea.setBackground(new Color(249, 250, 251)); // Very light gray
        messageArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);

        // Input Area
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        inputField = StyleUtils.createModernTextField();
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
        
        JButton sendBtn = StyleUtils.createModernButton("Envoyer", StyleUtils.ACCENT_GOLD);
        sendBtn.setPreferredSize(new Dimension(80, 40));
        sendBtn.addActionListener(e -> sendMessage());
        
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendBtn, BorderLayout.EAST);
        
        add(inputPanel, BorderLayout.SOUTH);
        
        // Welcome Message
        addReceivedMessage("Bonjour ! Comment pouvons-nous vous aider aujourd'hui ?");
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            addSentMessage(text);
            inputField.setText("");
            
            // Simulate Auto-Reply
            Timer t = new Timer(1500, e -> {
                addReceivedMessage("Merci pour votre message. Un agent vous répondra bientôt.");
            });
            t.setRepeats(false);
            t.start();
        }
    }

    private void addSentMessage(String text) {
        JPanel bubble = createBubble(text, true);
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.add(bubble, BorderLayout.EAST);
        messageArea.add(row);
        messageArea.add(Box.createRigidArea(new Dimension(0, 10)));
        scrollToBottom();
    }

    private void addReceivedMessage(String text) {
        JPanel bubble = createBubble(text, false);
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.add(bubble, BorderLayout.WEST);
        messageArea.add(row);
        messageArea.add(Box.createRigidArea(new Dimension(0, 10)));
        scrollToBottom();
    }
    
    private void scrollToBottom() {
        messageArea.revalidate();
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    private JPanel createBubble(String text, boolean isSent) {
        JPanel bubble = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        bubble.setLayout(new BorderLayout());
        
        // Colors
        if (isSent) {
            bubble.setBackground(StyleUtils.PRIMARY_BLUE);
        } else {
            bubble.setBackground(Color.WHITE);
            bubble.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        }

        JLabel label = new JLabel("<html><body style='width: 150px;'>" + text + "</body></html>");
        label.setFont(StyleUtils.FONT_BODY);
        label.setForeground(isSent ? Color.WHITE : StyleUtils.TEXT_DARK);
        label.setBorder(new EmptyBorder(8, 12, 8, 12));
        
        bubble.add(label, BorderLayout.CENTER);
        
        // Timestamp
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        timeLabel.setForeground(isSent ? new Color(200, 200, 255) : Color.GRAY);
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        timeLabel.setBorder(new EmptyBorder(0, 0, 5, 8));
        bubble.add(timeLabel, BorderLayout.SOUTH);
        
        return bubble;
    }
}
