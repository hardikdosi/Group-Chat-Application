/*
 *
 * @author Hardik
 */
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;

import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private JTextArea text;
    private JTextField clientMessage;
    private JScrollPane chatHistoryClient;
    private JButton sendClient;

    public Client() throws IOException {
        initializeGUI();
    }

    private String getServerIPAddress() {
        return JOptionPane.showInputDialog(
                this,
                "Enter IP Address of the Server:",
                "Welcome",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private String getClientName() {
        return JOptionPane.showInputDialog(
                this,
                "Choose a screen name:",
                "Username",
                JOptionPane.QUESTION_MESSAGE);
    }

    private String getClientPassword() {
        return JOptionPane.showInputDialog(
                this,
                "Enter Password:",
                "Password",
                JOptionPane.WARNING_MESSAGE);
    }
    
    private void wrongPasswordPopUp() {
        JOptionPane.showMessageDialog(this, "Wrong Password !", null, JOptionPane.ERROR_MESSAGE);
    }

    public void begin() throws IOException {
        socket = new Socket(getServerIPAddress(), 4444);
      
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        out.println("DETATCH_NOW");

        String clientName = getClientName();
        String password = getClientPassword();
        
        while (!("iiita123".equals(password) || "iiita".equals(password) || "placements2016".equals(password))) {
            wrongPasswordPopUp();
            password = getClientPassword();
        }
        this.setTitle("Welcome " + clientName);

        out.println(clientName);
        clientMessage.setEditable(true);

        while (true) {
            String line = in.readLine();
            text.append(line + "\n");
        }
    }

    public static void main(String args[]) throws Exception {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        Client client = new Client();
        client.begin();
    }

    public void initializeGUI() {
        text = new JTextArea();
        text.setColumns(20);
        text.setRows(5);
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setFont(new Font("Arial", Font.BOLD, 14));

        chatHistoryClient = new JScrollPane();
        chatHistoryClient.setViewportView(text);
        chatHistoryClient.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Auto Scroll Feature
        chatHistoryClient.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });
        //Feature end here

        clientMessage = new JTextField();
        clientMessage.setFont(new Font("Arial MT", Font.PLAIN, 12));
        clientMessage.setEditable(false);
        clientMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = clientMessage.getText();
                clientMessage.setText(null);
                if (msg.length() == 0) {
                    return;
                }
                out.println(msg);
            }
        });

        sendClient = new JButton("Send");
        sendClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 if ((e.getSource() == sendClient)) {
                    String msg = clientMessage.getText();
                    clientMessage.setText(null);
                    if (msg.length() == 0) {
                        return;
                    }
                    out.println(msg);
                }
            }
        });

        // Frame Attributes
        this.setSize(400, 491);
        this.setVisible(true);
        this.setTitle("Client");
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(chatHistoryClient);
        add(clientMessage);
        add(sendClient);

        // Layout Code :
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(chatHistoryClient)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(clientMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(sendClient, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(chatHistoryClient, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(clientMessage)
                                .addComponent(sendClient, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                        .addContainerGap())
        );

        this.pack();
    }
}
