
package portscan_linux;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NmapGUI extends JFrame {
    private JTextField commandTextField;
    private JTextArea outputTextArea;
    private JProgressBar progressBar;

    public NmapGUI() {
        setTitle("Port Scan Tool");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        commandTextField = new JTextField(30);
        JButton runButton = new JButton("Run TCP Scan");
        JButton IpButton = new JButton("What's my IP");
        JButton MultButton = new JButton("Run multiple scans");
        outputTextArea = new JTextArea();
        
        outputTextArea.setEditable(false);
            
        progressBar = new JProgressBar();

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputPanel.add(new JLabel("IP : "));
        inputPanel.add(commandTextField);
        inputPanel.add(runButton);
        inputPanel.add(IpButton);
        inputPanel.add(MultButton);

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runNmapCommand();
            }
        });

        IpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runIpConfigCommand();
            }
        });

   
        MultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Perform long-running task here
                        runMultipleScans();
                        return null;
                    }

                    @Override
                    protected void done() {
                        // Task finished, update UI if needed
                    }
                };

                worker.execute(); // Start the SwingWorker
            }
        });
    
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(progressBar, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }


    private void runNmapCommand() {
        String command = commandTextField.getText();

        try {
            Process process = new ProcessBuilder("nmap", "-v", "-Pn", command).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            Thread outputThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String line;
                        outputTextArea.setText("");
                        while ((line = reader.readLine()) != null) {
                            outputTextArea.append(line + "\n");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            outputThread.start();

            process.waitFor();
            outputThread.join();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            outputTextArea.setText("Error executing command: " + ex.getMessage());
        }
    }

    private void runIpConfigCommand() {
        try {
            Process process = new ProcessBuilder("ifconfig", "-a").start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            outputTextArea.setText("");

            while ((line = reader.readLine()) != null) {
                outputTextArea.append(line + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            outputTextArea.setText("Error executing command: " + ex.getMessage());
        }
    }

    private void runMultipleScans() {
        String baseIP = commandTextField.getText();

        try {
            progressBar.setValue(0);
            progressBar.setMaximum(255); // Setting maximum value for progress bar

            for (int i = 1; i <= 255; i++) {
                String currentIP = baseIP + "." + i;
                Process process = new ProcessBuilder("nmap", "-v", "-Pn", currentIP).start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                outputTextArea.append("\n\nScanning IP: " + currentIP + "\n");
                while ((line = reader.readLine()) != null) {
                    outputTextArea.append(line + "\n");
                    System.out.println(line);
                }

                progressBar.setValue(i);
                System.out.print(i);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            outputTextArea.setText("Error executing command: " + ex.getMessage());
        }
    }
}
