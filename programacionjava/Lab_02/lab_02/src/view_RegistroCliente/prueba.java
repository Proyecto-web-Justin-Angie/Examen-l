package view_RegistroCliente;
import javax.swing.*;

public class prueba {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    //Establecer el Look and Feel del sistema
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Crear y mostrar el formulario
                new frm_RegistroCliente();
            }
        });
    }
}
