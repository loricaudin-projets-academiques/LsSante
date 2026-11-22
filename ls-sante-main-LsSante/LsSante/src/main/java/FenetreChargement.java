import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class FenetreChargement extends JDialog {
    public FenetreChargement(JFrame fenetreCible, String titre, String message){
        super(fenetreCible, titre);
        this.setLocation((fenetreCible.getX() + fenetreCible.getWidth()) / 2, (fenetreCible.getY() + fenetreCible.getHeight()) / 2);
        this.setSize(500, 200);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.setContentPane(this.creerPanel(message));
        
    }

    JPanel panel;
    JLabel labelMessage;
    JProgressBar barreDeChargement;

    private JPanel creerPanel(String message){
        panel = (JPanel)this.getContentPane();
        panel.setLayout(new GridLayout(2, 1, 50, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        labelMessage = new JLabel(message);
        panel.add(labelMessage);
        
        barreDeChargement = new JProgressBar();
        barreDeChargement.setIndeterminate(true);
        panel.add(barreDeChargement);
        
        return panel;
    }
}
