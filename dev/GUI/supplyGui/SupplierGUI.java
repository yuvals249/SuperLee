package GUI.supplyGui;

import GUI.MainGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SupplierGUI extends JPanel {
    private MainGUI mainGUI;
    private JPanel mainPanel;
    private AddSupplierPanel addSupplierPanel;
    private EditSupplierPanel editSupplierPanel;
    private DeleteSupplierPanel deleteSupplierPanel;

    public SupplierGUI(MainGUI mainGUI)throws IOException  {
        this.mainGUI = mainGUI;
        setLayout(new BorderLayout());

        // Create main panel
        Image background = null;
        try {
            background = ImageIO.read(getClass().getResource("/GUI/pictures/background.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Image finalBackground = background;
        mainPanel = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(finalBackground, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("<html>Responsible for supplier relations <br> Please select option :</html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.setLayout(new FlowLayout());

        // Create button panel
        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);

        // Create buttons
        JButton addSupplierButton = createButton("Add Supplier","/GUI/pictures/new-supplier.jpg");
        JButton editSupplierButton = createButton("Edit Supplier","/GUI/pictures/update.jpg");
        JButton deleteSupplierButton = createButton("Delete Supplier","/GUI/pictures/delete-supplier.jpg");

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addSupplierButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(editSupplierButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(deleteSupplierButton);
        buttonPanel.add(Box.createHorizontalGlue());


        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(backButton);

        // Add button panel to the main panel
        mainPanel.add(Box.createVerticalStrut(120)); // Adjust the spacing as needed
        mainPanel.add(buttonPanel,BorderLayout.CENTER);


        mainPanel.add(Box.createVerticalStrut(200));
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);


        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners for the buttons
        addSupplierButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAddSupplierPanel();
            }
        });

        editSupplierButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openEditSupplierPanel();
            }
        });

        deleteSupplierButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openDeleteSupplierPanel();
            }
        });

        backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainGUI.showMainPanel();
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
        Image image = ImageIO.read(getClass().getResource(imagePath));
        Image small_image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(small_image);
        imageLabel.setIcon(imageIcon);
        imageLabel.setBounds(0,0,width,height);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        buttonPanel.add(imageLabel, BorderLayout.CENTER);

        // Create text label
        Font buttonFont = new Font("Tahoma", Font.BOLD, 16);
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
        button.setMargin(new Insets(0, 0, 0, 0)); // Set the margin to zer

        return button;
    }

    private void openAddSupplierPanel() {
        mainPanel.setVisible(false);

        if (addSupplierPanel == null) {
            addSupplierPanel = new AddSupplierPanel(this);
            addSupplierPanel.setPreferredSize(mainPanel.getSize());
            addSupplierPanel.setMaximumSize(mainPanel.getMaximumSize());
            addSupplierPanel.setMinimumSize(mainPanel.getMinimumSize());
            addSupplierPanel.setSize(mainPanel.getSize());
        }

        add(addSupplierPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void openEditSupplierPanel() {
        mainPanel.setVisible(false);

        if (editSupplierPanel == null) {
            editSupplierPanel = new EditSupplierPanel(this);
            editSupplierPanel.setPreferredSize(mainPanel.getSize());
            editSupplierPanel.setMaximumSize(mainPanel.getMaximumSize());
            editSupplierPanel.setMinimumSize(mainPanel.getMinimumSize());
            editSupplierPanel.setSize(mainPanel.getSize());
        }

        add(editSupplierPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void openDeleteSupplierPanel() {
        mainPanel.setVisible(false);

        if (deleteSupplierPanel == null) {
            deleteSupplierPanel = new DeleteSupplierPanel(this);
            deleteSupplierPanel.setPreferredSize(mainPanel.getSize());
            deleteSupplierPanel.setMaximumSize(mainPanel.getMaximumSize());
            deleteSupplierPanel.setMinimumSize(mainPanel.getMinimumSize());
            deleteSupplierPanel.setSize(mainPanel.getSize());
        }

        add(deleteSupplierPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    public void showDefaultPanelFromChild() {
        mainPanel.setVisible(true);
        removeCurrentChildPanel();
        revalidate();
        repaint();
    }

    private void removeCurrentChildPanel() {
        if (addSupplierPanel != null && addSupplierPanel.isShowing()) {
            remove(addSupplierPanel);
        } else if (editSupplierPanel != null && editSupplierPanel.isShowing()) {
            remove(editSupplierPanel);
        } else if (deleteSupplierPanel != null && deleteSupplierPanel.isShowing()) {
            remove(deleteSupplierPanel);
        }
    }

}