import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.JTableHeader;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.sql.*;

public class GestionClientSav extends JFrame {
    private JTextField nomField, prenomField, telephoneField, numeroTicketField;
    private JTextArea reclamationArea;
    private DefaultTableModel tableModel;
    private JTextField dateField;
    private JComboBox<String> etatTicketComboBox;
    private Connection conn; // Connexion à la base de données MySQL

    private Timer timer;

    // Générez un numéro de ticket aléatoire
    Random random = new Random();


    private int genererNumeroTicketUnique() {
        int numeroTicket;
        do {
            numeroTicket = new Random().nextInt(1000) + 1;
        } while (ticketsTableContainsNumeroTicket(numeroTicket));

        return numeroTicket;
    }

    private boolean ticketsTableContainsNumeroTicket(int numeroTicket) {
        // Votre logique pour vérifier si le numéro de ticket existe déjà dans le tableau
        return false;
    }

    private JLabel createLogoLabel(int width, int height) {
        ImageIcon logoIcon = new ImageIcon("C:/Users/pavly/IdeaProjects/Application SAV/src/LogoMain.png");
        Image scaledImage = logoIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);
        JLabel logoLabel = new JLabel(scaledLogoIcon);
        return logoLabel;
    }


    public GestionClientSav() {
        setTitle("Formulaire de demande d'assistance");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        try {
            //étape 1: charger la classe de driver
            Class.forName("com.mysql.jdbc.Driver");

            //étape 2: créer l'objet de connexion
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetsav?characterEncoding=UTF-8", "root", "");

            // Initialisez l'objet tableModel ici
            String[] columnNames = {"Nom", "Prénom", "Téléphone", "Réclamation", "Date", "Numéro de ticket", "État du ticket"};
            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Rendre toutes les cellules du tableau non éditables
                    return true;
                }
            };

            //étape 3: créer l'objet statement
            Statement stmt = conn.createStatement();


            System.out.println("tes ok");

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("erreur");
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);



        JPanel formulairePanel = new JPanel(new BorderLayout(10, 10));
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        JPanel tablePanel = new JPanel(new BorderLayout());

        add(formulairePanel);
        int logoWidth = 120;
        int logoHeight = 120;
        JLabel logoLabel = createLogoLabel(logoWidth, logoHeight);
        formulairePanel.add(logoLabel, BorderLayout.NORTH);

        inputPanel.add(new JLabel("Nom :"));
        nomField = new JTextField(20);
        inputPanel.add(nomField);

        inputPanel.add(new JLabel("Prénom :"));
        prenomField = new JTextField(20);
        inputPanel.add(prenomField);

        inputPanel.add(new JLabel("Téléphone :"));
        telephoneField = new JTextField(20);
        inputPanel.add(telephoneField);

        inputPanel.add(new JLabel("Réclamation :"));
        reclamationArea = new JTextArea(5, 20);
        reclamationArea.setWrapStyleWord(true);
        reclamationArea.setLineWrap(true);
        inputPanel.add(new JScrollPane(reclamationArea));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());
        inputPanel.add(new JLabel("Date d'aujourd'hui :"));
        dateField = new JTextField(currentDate);
        dateField.setEditable(false);
        inputPanel.add(dateField);

        inputPanel.add(new JLabel("Numéro de ticket :"));
        numeroTicketField = new JTextField(20);
        numeroTicketField.setEditable(false); // Le champ numéro de ticket ne sera pas éditable manuellement
        inputPanel.add(numeroTicketField);

        etatTicketComboBox = new JComboBox<>(new String[]{"Ouvert", "En cours", "Fermé"});
        inputPanel.add(new JLabel("État du ticket :"));
        inputPanel.add(etatTicketComboBox);


        timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chargerDonneesDepuisBaseDeDonnees(); // Appelez la méthode de chargement des données
            }
        });
        timer.start(); // Démarrez le timer



        JButton envoyerButton = new JButton("Envoyer");
        envoyerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String telephone = telephoneField.getText();
                String reclamation = reclamationArea.getText();
                String date = dateField.getText();
                int numeroTicket = genererNumeroTicketUnique(); // Générez le numéro de ticket aléatoire
                numeroTicketField.setText(String.valueOf(numeroTicket)); // Mettez à jour le champ numéro de ticket
                String etatTicket = etatTicketComboBox.getSelectedItem().toString();

                // Insérez les données dans la table de la base de données
                try {
                    String query = "INSERT INTO dossier_sav (nom, prenom, telephone, reclamation, date, numero_ticket, etat_ticket) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, nom);
                    preparedStatement.setString(2, prenom);
                    preparedStatement.setString(3, telephone);
                    preparedStatement.setString(4, reclamation);
                    preparedStatement.setString(5, date);
                    preparedStatement.setInt(6, numeroTicket);
                    preparedStatement.setString(7, etatTicket);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'insertion des données : " + ex.getMessage(), "Erreur SQL", JOptionPane.ERROR_MESSAGE);
                }

                // Ajoutez les données dans le modèle de la table Swing
                String[] rowData = {nom, prenom, telephone, reclamation, date, String.valueOf(numeroTicket), etatTicket};
                tableModel.addRow(rowData);
            }
        });
        inputPanel.add(envoyerButton);

        formulairePanel.add(inputPanel, BorderLayout.CENTER);

        String[] columnNames = {"Nom", "Prénom", "Téléphone", "Réclamation", "Date", "Numéro de ticket", "État du ticket"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Rendre toutes les cellules du tableau non éditables
                return true;
            }




        };
        JTable tableau = new JTable(tableModel);

        JTableHeader header = tableau.getTableHeader();
        tablePanel.add(header, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(tableau);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        formulairePanel.add(tablePanel, BorderLayout.SOUTH);

        add(formulairePanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);




        // Ajoutez un bouton "Supprimer" à votre inputPanel
        JButton supprimerButton = new JButton("Supprimer");
        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableau.getSelectedRow();
                if (selectedRow != -1) { // Vérifiez si une ligne est sélectionnée
                    // Supprimez la ligne de la table Swing
                    tableModel.removeRow(selectedRow);

                    // Supprimez la ligne correspondante de la base de données (vous devrez implémenter cette partie)
                    // Assurez-vous d'ajuster la logique de suppression en fonction de votre propre schéma de base de données
                    int numeroTicket = Integer.parseInt(tableModel.getValueAt(selectedRow, 5).toString());
                    supprimerLigneDeLaBaseDeDonnees(numeroTicket);
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner une ligne à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
                }
            }
            private void supprimerLigneDeLaBaseDeDonnees(int numeroTicket) {
                try {
                    // Créez une requête SQL DELETE pour supprimer la ligne en fonction du numéro de ticket
                    String query = "DELETE FROM dossier_sav WHERE numero_ticket = ?";

                    // Créez une instruction SQL préparée
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, numeroTicket);

                    // Exécutez la requête de suppression
                    int rowsAffected = preparedStatement.executeUpdate();

                    // Vérifiez si la suppression a réussi
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "La ligne a été supprimée de la base de données avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossible de supprimer la ligne de la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }

                    // Fermez la déclaration
                    preparedStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de la suppression de la ligne de la base de données : " + ex.getMessage(), "Erreur SQL", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        inputPanel.add(supprimerButton);





        inputPanel.setLayout(new GridLayout(9, 2, 10, 10));
        // Ajoutez un bouton "Actualiser" pour mettre à jour les données du tableau depuis la base de données
        JButton actualiserButton = new JButton("Actualiser les donnés");
        actualiserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chargerDonneesDepuisBaseDeDonnees(); // Appelez la méthode de chargement des données
            }
        });
        inputPanel.add(actualiserButton);


        JButton mettreAJourButton = new JButton("Mettre à jour");
        mettreAJourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableau.getSelectedRow();
                if (selectedRow != -1) { // Vérifiez si une ligne est sélectionnée
                    // Récupérez le numéro de ticket de la ligne sélectionnée dans le modèle de tableau Swing
                    int numeroTicket = Integer.parseInt(tableModel.getValueAt(selectedRow, 5).toString());

                    // Récupérez les données de la ligne sélectionnée
                    String nom = tableModel.getValueAt(selectedRow, 0).toString();
                    String prenom = tableModel.getValueAt(selectedRow, 1).toString();
                    String telephone = tableModel.getValueAt(selectedRow, 2).toString();
                    String reclamation = tableModel.getValueAt(selectedRow, 3).toString();
                    String date = tableModel.getValueAt(selectedRow, 4).toString();
                    String etatTicket = tableModel.getValueAt(selectedRow, 6).toString();

                    // Mettez à jour les données dans la base de données
                    try {
                        String query = "UPDATE dossier_sav SET nom=?, prenom=?, telephone=?, reclamation=?, date=?, etat_ticket=? WHERE numero_ticket=?";
                        PreparedStatement preparedStatement = conn.prepareStatement(query);
                        preparedStatement.setString(1, nom);
                        preparedStatement.setString(2, prenom);
                        preparedStatement.setString(3, telephone);
                        preparedStatement.setString(4, reclamation);
                        preparedStatement.setString(5, date);
                        preparedStatement.setString(6, etatTicket);
                        preparedStatement.setInt(7, numeroTicket);
                        int rowsUpdated = preparedStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(null, "Les données ont été mises à jour avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                        }
                        preparedStatement.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour des données : " + ex.getMessage(), "Erreur SQL", JOptionPane.ERROR_MESSAGE);
                    }

                    // Mettez à jour la ligne correspondante dans le modèle de tableau Swing
                    tableModel.setValueAt(nom, selectedRow, 0);
                    tableModel.setValueAt(prenom, selectedRow, 1);
                    tableModel.setValueAt(telephone, selectedRow, 2);
                    tableModel.setValueAt(reclamation, selectedRow, 3);
                    tableModel.setValueAt(date, selectedRow, 4);
                    tableModel.setValueAt(etatTicket, selectedRow, 6);
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner une ligne à mettre à jour.", "Avertissement", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        inputPanel.add(mettreAJourButton);




        // Fermez la connexion à la base de données lors de la fermeture de l'application
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columnIndex = tableau.columnAtPoint(e.getPoint());
                if (columnIndex == 6) { // "État du ticket" est la 7e colonne (indice 6 en partant de 0)
                    afficherFenetreEtatTicket();
                }
            }
        });

        tablePanel.add(header, BorderLayout.NORTH);



    }
// Partie JFRAME ETAT TICKET
private void afficherFenetreEtatTicket() {
    JFrame fenetreEtatTicket = new JFrame("État du Ticket");

    // Créez une nouvelle JTable pour afficher les tickets
    String[] columnNames = {"Nom", "Prénom", "Téléphone", "Réclamation", "Date", "Numéro de ticket", "État du ticket"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    JTable tableauTickets = new JTable(tableModel);

    // Obtenez les données de la base de données et remplissez la JTable
    chargerDonneesTicketsDepuisBaseDeDonnees(tableModel);

    // Ajoutez la JTable à un JScrollPane pour permettre le défilement
    JScrollPane scrollPane = new JScrollPane(tableauTickets);

    // Ajoutez le JScrollPane à la nouvelle fenêtre
    fenetreEtatTicket.add(scrollPane);

    // Ajoutez un JLabel pour afficher le nombre de tickets en cours ou ouverts
    JLabel nombreTicketsLabel = new JLabel("Nombre de Tickets en cours ou ouverts : " + getNombreTicketsEnCoursOuverts());
    fenetreEtatTicket.add(nombreTicketsLabel, BorderLayout.SOUTH);

    // Ajoutez un bouton "Fermer" avec un gestionnaire d'événements pour fermer la fenêtre
    JButton fermerButton = new JButton("Fermer");


    fermerButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Fermez la fenêtre et libérez la mémoire
            fenetreEtatTicket.dispose();
        }
    });


    fenetreEtatTicket.add(fermerButton, BorderLayout.EAST);

    fenetreEtatTicket.setSize(600, 400);
    fenetreEtatTicket.setLocationRelativeTo(null);
    fenetreEtatTicket.setVisible(true);
}

    private int getNombreTicketsEnCoursOuverts() {
        int nombreTickets = 0;
        try {
            // Créez une requête SQL COUNT pour obtenir le nombre de tickets en cours ou ouverts
            String query = "SELECT COUNT(*) FROM dossier_sav WHERE etat_ticket IN ('Ouvert', 'En cours')";

            // Créez une instruction SQL
            Statement statement = conn.createStatement();

            // Exécutez la requête et obtenez le résultat
            ResultSet resultSet = statement.executeQuery(query);

            // Récupérez le résultat et affectez-le à la variable nombreTickets
            if (resultSet.next()) {
                nombreTickets = resultSet.getInt(1);
            }

            // Fermez le résultat et l'instruction
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération du nombre de tickets : " + ex.getMessage(), "Erreur SQL", JOptionPane.ERROR_MESSAGE);
        }
        return nombreTickets;
    }


    private void chargerDonneesTicketsDepuisBaseDeDonnees(DefaultTableModel tableModel) {
        try {
            // Créez une requête SQL SELECT pour récupérer les données des tickets dans les états "Ouvert" ou "En cours"
            String query = "SELECT nom, prenom, telephone, reclamation, date, numero_ticket, etat_ticket FROM dossier_sav WHERE etat_ticket IN ('Ouvert', 'En cours')";

            // Créez une instruction SQL
            Statement statement = conn.createStatement();

            // Exécutez la requête et obtenez un résultat (ensemble de résultats)
            ResultSet resultSet = statement.executeQuery(query);

            // Effacez toutes les lignes existantes dans le modèle de tableau Swing
            tableModel.setRowCount(0);

            // Parcourez les résultats et ajoutez chaque ligne au modèle de tableau Swing
            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String telephone = resultSet.getString("telephone");
                String reclamation = resultSet.getString("reclamation");
                String date = resultSet.getString("date");
                int numeroTicket = resultSet.getInt("numero_ticket");
                String etatTicket = resultSet.getString("etat_ticket");

                String[] rowData = {nom, prenom, telephone, reclamation, date, String.valueOf(numeroTicket), etatTicket};
                tableModel.addRow(rowData);
            }

            // Fermez le résultat et l'instruction
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des données : " + ex.getMessage(), "Erreur SQL", JOptionPane.ERROR_MESSAGE);
        }
    }





    // Méthode pour supprimer une ligne de la base de données
    private void supprimerLigneDeLaBaseDeDonnees(int numeroTicket) {
        // Implémentez la logique de suppression de la ligne correspondante dans votre base de données ici
        // Utilisez le numéro de ticket pour identifier la ligne à supprimer
    }

    private void chargerDonneesDepuisBaseDeDonnees() {
        try {
            // Créez une requête SQL SELECT pour récupérer les données de la table
            String query = "SELECT nom, prenom, telephone, reclamation, date, numero_ticket, etat_ticket FROM dossier_sav";

            // Créez une instruction SQL
            Statement statement = conn.createStatement();

            // Exécutez la requête et obtenez un résultat (ensemble de résultats)
            ResultSet resultSet = statement.executeQuery(query);

            // Effacez toutes les lignes existantes dans le tableau Swing
            tableModel.setRowCount(0);

            // Parcourez les résultats et ajoutez chaque ligne au modèle de tableau Swing
            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String telephone = resultSet.getString("telephone");
                String reclamation = resultSet.getString("reclamation");
                String date = resultSet.getString("date");
                int numeroTicket = resultSet.getInt("numero_ticket");
                String etatTicket = resultSet.getString("etat_ticket");

                String[] rowData = {nom, prenom, telephone, reclamation, date, String.valueOf(numeroTicket), etatTicket};
                tableModel.addRow(rowData);
            }

            // Fermez le résultat et l'instruction
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des données : " + ex.getMessage(), "Erreur SQL", JOptionPane.ERROR_MESSAGE);
        }
    }





    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GestionClientSav();
            }
        });
    }
}
