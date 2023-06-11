package GUI;

import GUI.orderGui.StockManagerGUI;
import GUI.storeGui.StoreManagerGUI;
import GUI.supplyGui.SupplierGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainGUI extends JFrame{
    private JLayeredPane layeredPane;
    private JPanel mainPanel;
    private SupplierGUI supplierGUI;
    private StoreManagerGUI storeManagerGUI;
    private StockManagerGUI stockManagerGUI;
    public MainGUI() throws IOException {
        setTitle("SUPER LEE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);

        // Create layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new BorderLayout());
        getContentPane().add(layeredPane);
        Image background = ImageIO.read(getClass().getResource("/GUI/pictures/market.jpg"));
        // Create main panel
        mainPanel = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        /////
        mainPanel.setLayout(new BorderLayout());
        //////

        JLabel label1 = new JLabel("<html>Welcome to Super Lee store! <br>What is your role?</html>");
        label1.setHorizontalAlignment(SwingConstants.CENTER);
//        label1.setVerticalAlignment(SwingConstants.NORTH);
        Font font = new Font("Comic Sans MS", Font.BOLD, 24);
        label1.setFont(font);
        mainPanel.add(label1, BorderLayout.NORTH);
        mainPanel.setLayout(new FlowLayout());


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);

        // Create buttons
        JButton supplierManagerButton = createButton("suppliers Relation", "/GUI/pictures/supply.JPG");
        JButton storeManagerButton = createButton("Store Manager", "/GUI/pictures/store-manager.jpg");
        JButton stockManagerButton = createButton("Stock Manager", "/GUI/pictures/stock-manager.jpg");

        // Add buttons to the button panel
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(supplierManagerButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(storeManagerButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(stockManagerButton);
        buttonPanel.add(Box.createHorizontalGlue());

        // Add button panel to the main panel
        mainPanel.add(Box.createVerticalStrut(150)); // Adjust the spacing as needed

        mainPanel.add(buttonPanel,BorderLayout.CENTER);

        // Add main panel to the MainGUI panel
//        add(mainPanel, BorderLayout.CENTER);
//    }



        layeredPane.setLayer(mainPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(mainPanel);



        // Add action listeners
        supplierManagerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSupplierManager();
            }
        });
        storeManagerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openStoreManager();
            }
        });

        stockManagerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openStockManager();
            }
        });
    }
    private JButton createButton(String text, String imagePath) throws IOException {
        // Create button panel
        int width = 150;
        int height = 150;
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setLayout(new BorderLayout());
//        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove label margin

        // Create image label
        JLabel imageLabel = new JLabel();
        Image image =ImageIO.read(getClass().getResource(imagePath));
        Image small_image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(small_image);
        imageLabel.setIcon(imageIcon);
        imageLabel.setBounds(0,0,width,height);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        buttonPanel.add(imageLabel, BorderLayout.CENTER);

        // Create text label
        Font buttonFont = new Font("Comic Sans MS", Font.BOLD, 16);
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(buttonFont);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        buttonPanel.add(textLabel, BorderLayout.SOUTH);

        // Create button
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.add(buttonPanel, BorderLayout.CENTER);
        button.setFocusPainted(false);
        button.setVerticalAlignment(SwingConstants.TOP); // Adjust vertical alignment
        button.setVerticalTextPosition(SwingConstants.BOTTOM); // Adjust vertical text position
        button.setHorizontalTextPosition(SwingConstants.CENTER); // Adjust horizontal text position

        return button;
    }

    private void openSupplierManager() {
        if (supplierGUI == null) {
            supplierGUI = new SupplierGUI(this);
            layeredPane.add(supplierGUI, 1);
        }

        supplierGUI.setVisible(true);
        mainPanel.setVisible(false);
    }
    private void openStoreManager() {
        if (storeManagerGUI == null) {
            storeManagerGUI = new StoreManagerGUI(this);
            layeredPane.add(storeManagerGUI, 1);
        }

        storeManagerGUI.setVisible(true);
        mainPanel.setVisible(false);
    }

    private void openStockManager() {
        if (stockManagerGUI == null) {
            stockManagerGUI = new StockManagerGUI(this);
            layeredPane.add(stockManagerGUI, 1);
        }

        stockManagerGUI.setVisible(true);
        mainPanel.setVisible(false);
    }

    // Add methods to open other screens and handle back button

    public void showMainPanel() {
        mainPanel.setVisible(true);
        if (supplierGUI != null) {
            supplierGUI.setVisible(false);
        }
        if (storeManagerGUI != null) {
            storeManagerGUI.setVisible(false);
        }
        if (stockManagerGUI != null) {
            stockManagerGUI.setVisible(false);
        }
        // Hide other screens if necessary
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainGUI mainGUI = null;
                try {
                    mainGUI = new MainGUI();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                mainGUI.setVisible(true);
            }
        });
    }


}
