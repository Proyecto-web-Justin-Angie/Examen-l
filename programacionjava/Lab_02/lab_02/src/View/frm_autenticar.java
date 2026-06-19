package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class frm_autenticar extends JFrame {

    private JPanel panelPrincipal;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnAceptar;
    private JButton btnCancelar;
    private JLabel lblUsuario;
    private JLabel lblPassword;
    private JLabel lblTitulo;
    private JLabel lblIcono;

    // Variable para guardar el estado de autenticación
    private boolean autenticado = false;

    // Credenciales de acceso
    private final String USUARIO_CORRECTO = "admin";
    private final String PASSWORD_CORRECTA = "secreta";

    public frm_autenticar() {
        // Configuración básica de la ventana
        setTitle("Autenticación de Usuario");
        setSize(400, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Inicializar componentes
        inicializarComponentes();
        
        // Hacer visible la ventana
        setVisible(true);
    }

    private void inicializarComponentes() {
        // Panel principal con BorderLayout
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel para la imagen y título con fondo azul claro
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(new Color(210, 230, 255));
        panelSuperior.setBorder(BorderFactory.createEtchedBorder());
        
        // Cargar la imagen de usuario y redimensionarla para hacerla más grande
        ImageIcon iconoOriginal = cargarIcono("page_user_dark.gif");
        ImageIcon iconoGrande = new ImageIcon(iconoOriginal.getImage().getScaledInstance(
                64, 64, Image.SCALE_SMOOTH));
        
        lblIcono = new JLabel(iconoGrande);
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Etiqueta del título
        lblTitulo = new JLabel("AUTENTICACIÓN DE USUARIOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(0, 0, 100));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Añadir imagen y título al panel superior
        panelSuperior.add(Box.createVerticalStrut(20));
        panelSuperior.add(lblIcono);
        panelSuperior.add(Box.createVerticalStrut(15));
        panelSuperior.add(lblTitulo);
        panelSuperior.add(Box.createVerticalStrut(20));
        
        // Panel de datos con borde y fondo claro
        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Datos de acceso"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panelDatos.setBackground(new Color(245, 245, 245));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Etiqueta Usuario
        lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelDatos.add(lblUsuario, gbc);
        
        // Campo de texto Usuario
        txtUsuario = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panelDatos.add(txtUsuario, gbc);
        
        // Etiqueta Contraseña
        lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panelDatos.add(lblPassword, gbc);
        
        // Campo de texto Contraseña
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panelDatos.add(txtPassword, gbc);
        
        // Panel de botones con borde y fondo degradado
        JPanel panelBotones = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setPaint(new GradientPaint(
                        0, 0, new Color(240, 240, 240),
                        0, getHeight(), new Color(220, 220, 220)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Botón Aceptar con mejor aspecto - con texto negro
        btnAceptar = new JButton("Aceptar");
        btnAceptar.setPreferredSize(new Dimension(100, 30));
        btnAceptar.setBackground(new Color(70, 130, 180));
        btnAceptar.setForeground(Color.BLACK);
        btnAceptar.setFont(new Font("Arial", Font.BOLD, 12));
        btnAceptar.setFocusPainted(false);
        
        // Botón Cancelar con mejor aspecto
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(100, 30));
        btnCancelar.setBackground(new Color(220, 220, 220));
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.setFocusPainted(false);
        
        // Agregar acciones a los botones
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificarCredenciales();
            }
        });
        
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Cerrar aplicación
            }
        });
        
        // Acción al presionar Enter en el campo de contraseña
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    verificarCredenciales();
                }
            }
        });
        
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        
        // Agregar paneles al panel principal
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelDatos, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        // Agregar panel principal al frame
        getContentPane().add(panelPrincipal);
    }
    
    /**
     * Método para cargar iconos de forma segura
     */
    private ImageIcon cargarIcono(String nombreArchivo) {
        // Lista de posibles rutas donde buscar
        String[] rutas = {
            "/resources/images.iconos/" + nombreArchivo,
            "resources/images.iconos/" + nombreArchivo,
            "/images.iconos/" + nombreArchivo,
            "/resources/" + nombreArchivo,
            "/" + nombreArchivo,
            nombreArchivo
        };
        
        // Intentar cargar desde las diferentes rutas
        for (String ruta : rutas) {
            try {
                // Intento 1: Cargar como recurso desde el classpath
                java.net.URL url = getClass().getResource(ruta);
                if (url != null) {
                    ImageIcon icono = new ImageIcon(url);
                    if (icono.getIconWidth() > 0) {
                        System.out.println("Icono cargado exitosamente desde: " + ruta);
                        return icono;
                    }
                }
                
                // Intento 2: Cargar como archivo
                File file = new File(ruta);
                if (file.exists()) {
                    ImageIcon icono = new ImageIcon(file.getAbsolutePath());
                    if (icono.getIconWidth() > 0) {
                        System.out.println("Icono cargado exitosamente desde archivo: " + file.getAbsolutePath());
                        return icono;
                    }
                }
            } catch (Exception e) {
                // Continuar con la siguiente ruta si hay error
                System.out.println("Error al intentar cargar desde " + ruta + ": " + e.getMessage());
            }
        }
        
        // Si no se pudo cargar el icono, devolver un icono vacío
        System.out.println("No se pudo cargar el icono: " + nombreArchivo);
        return new ImageIcon();
    }
    
    private void verificarCredenciales() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        
        if (usuario.equals(USUARIO_CORRECTO) && password.equals(PASSWORD_CORRECTA)) {
            JOptionPane.showMessageDialog(this, 
                    "Bienvenido al sistema", 
                    "Acceso correcto", 
                    JOptionPane.INFORMATION_MESSAGE);
            autenticado = true;
            dispose(); // Cerrar esta ventana para abrir el menú principal
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Credenciales incorrectas. Intente nuevamente.", 
                    "Error de acceso", 
                    JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtUsuario.requestFocus();
        }
    }
    
    /**
     * Método para comprobar si el usuario se autenticó correctamente
     * @return true si el usuario está autenticado, false en caso contrario
     */
    public boolean isAutenticado() {
        return autenticado;
    }
    
    /**
     * Método estático para verificar la autenticación en cualquier parte de la aplicación
     * @param parent Ventana padre para centrar el diálogo de autenticación
     * @return true si el usuario ya está autenticado, false si se muestra la ventana de autenticación
     */
    public static boolean verificarAutenticacion(JFrame parent) {
        // Esta implementación simplificada muestra la ventana de autenticación
        frm_autenticar auth = new frm_autenticar();
        auth.setLocationRelativeTo(parent);
        
        // Devolvemos false para indicar que se mostró la ventana de autenticación
        return false;
    }
}