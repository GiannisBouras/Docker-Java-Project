
package portscan_linux;

import javax.swing.SwingUtilities;

/**
 *
 * @author Giannis
 */
public class PortScan_Linux {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PasswordWindow passwordWindow = new PasswordWindow();
            }
        });
    }
    
}
