import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuInterface extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private ManageTeamAndPlayer manageTeamAndPlayer;

    public MenuInterface() {
        setTitle("Menu Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);

        manageTeamAndPlayer = new ManageTeamAndPlayer(this);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel menuPanel = new JPanel(new GridLayout(3, 1));
        JButton teamManagementButton = new JButton("create or manage teams and players");
        JButton otherButton1 = new JButton("create or search games data");
        JButton otherButton2 = new JButton("search or analyze players carrer data");

        teamManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manageTeamAndPlayer.setVisible(true);
                MenuInterface.this.setVisible(false);
            }
        });

        menuPanel.add(teamManagementButton);
        menuPanel.add(otherButton1);
        menuPanel.add(otherButton2);

        cardPanel.add(menuPanel, "Menu");

        add(cardPanel);

        cardLayout.show(cardPanel, "Menu");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MenuInterface().setVisible(true);
            }
        });
    }
}
