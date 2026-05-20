package coffeemunchingmonkeys.tools.lomimp;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import coffeemunchingmonkeys.tools.lomimp.core.*;
import coffeemunchingmonkeys.tools.lomimp.bricks.*;

/**
 *
 * Lomimp
 * Lomimp
 *
 * @author motrance42@gmail.com*
 * 
 * @version 2.0.2
 * @since 2026-05-15
 **/
public class Lomimp extends javax.swing.JFrame {
    //Fields
    private static final long serialVersionUID = 1L;
    private Boolean running = false;
    private Boolean fetchLatestNumbers = false;
    private Boolean stats = false;
    private Game game;
    private LogProvider log;
    private Settings settings;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFetch;
    private javax.swing.JComboBox<Game> cbGameSelector;
    private javax.swing.JButton btStart;
    private javax.swing.JCheckBox cbFetch;
    private javax.swing.JCheckBox cbStat;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.GroupLayout layout;
    private Boolean firstRun = true;

    public Lomimp() {
        initComponents();
        this.firstRun = true;
        log = new LogProvider(jTextArea1);
        this.settings = new Settings(log);

        Integer logLevel = settings.getLogLevel();
        if(logLevel != null) {
            log.setLogLevel(LogLevel.values()[logLevel]);
            log.writeDebug("Log level set to: " + logLevel);
        }

        String txt = "Lomimp[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + "]";
        log.writeInfo(txt);
    }

    private void initLomimp() {
        if(!running) {
            running = true;
            if(!this.firstRun) {
                jTextArea1.setText("");
            }
            
            cbFetch.setEnabled(false);
            cbStat.setEnabled(false);
            btStart.setEnabled(false);
            cbGameSelector.setEnabled(false);

            log.writeInfo("Game: " + game.displayName());
        }
    }

    /** 
     * @return boolean
     */
    private boolean fetchNumbers() {
        fetchLatestNumbers = cbFetch.isSelected();
        log.writeDebug("Fetching latest drawn numbers: " + fetchLatestNumbers);
        stats = cbStat.isSelected();
        log.writeDebug("Statistic: " + stats);

        GameControl gameControl = new GameControl(this.log, this.settings);
        Boolean fetchNumbersFromWeb = cbFetch.isSelected();
        boolean success = gameControl.fetch(game, stats, fetchNumbersFromWeb);
        running = false;

        cbFetch.setEnabled(true);
        cbStat.setEnabled(true);
        btStart.setEnabled(true);
        cbGameSelector.setEnabled(true);

        if(firstRun) {
            this.firstRun = false;
        }
        return success;
    }

    /**
     *
     */
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        lblFetch = new javax.swing.JLabel();
        cbGameSelector = new javax.swing.JComboBox<>();
        btStart = new javax.swing.JButton();
        cbFetch = new javax.swing.JCheckBox();
        cbStat = new javax.swing.JCheckBox();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(java.awt.Color.white);

        lblFetch.setIcon(new javax.swing.ImageIcon("lucky.png"));

        cbFetch.setText("Fetch latest numbers");
        cbStat.setText("Stats");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setBorder(null);
        jScrollPane1.setViewportView(jTextArea1);
        jScrollPane1.setBorder(null);

        btStart.setText("Start");
        btStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btStartActionPerformed(evt);
            }
        });

        cbGameSelector.setModel(new javax.swing.DefaultComboBoxModel<>(Game.values()));
        cbGameSelector.setSelectedIndex(1);
        cbGameSelector.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbGameSelectorItemStateChanged(evt);
            }
        });

        game = (Game)cbGameSelector.getSelectedItem();

        layout = new javax.swing.GroupLayout(getContentPane());

        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(lblFetch)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                                        .addComponent(cbGameSelector, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btStart, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbFetch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbStat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap())
        );

        getContentPane().setVisible(true);
        getContentPane().setBackground(new java.awt.Color(255, 255, 255));

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(lblFetch, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 215, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(cbGameSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btStart)
                                        .addComponent(cbFetch)
                                        .addComponent(cbStat))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1)
                                .addContainerGap())
        );

        pack();
    }

    /** 
     * @param evt
     */
    private void btStartActionPerformed(java.awt.event.ActionEvent evt) {
        initLomimp();

        new javax.swing.SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return fetchNumbers();
            }

            @Override
            protected void done() {
                try {
                    get(); // retrieve result or rethrow exception
                } catch (Exception ex) {
                    log.writeInfo("Error during fetch: " + ex.getMessage());
                }
            }
        }.execute();
    }

    /** 
     * @param evt
     */
    private void cbGameSelectorItemStateChanged(java.awt.event.ItemEvent evt) {
        game = (Game) cbGameSelector.getSelectedItem();
    }
}
