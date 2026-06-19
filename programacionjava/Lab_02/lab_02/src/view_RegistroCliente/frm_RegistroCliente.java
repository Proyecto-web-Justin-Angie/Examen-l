package view_RegistroCliente;

//===================================================================
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//===================================================================
public class  frm_RegistroCliente extends JFrame {
    //===================================================================
    private JPanel pnl_Principal;
    private JLabel lbl_Titulo;

    private JTextField txt_Nombre;
    private JTextField txt_Personas;
    private JTextField txt_Mesa;

    private JButton btn_Aceptar;
    private JButton btn_Cancelar;
    private JRadioButton RBtn_Si, RBtn_No;
    private JComboBox Mesero, Estadia;
    //===================================================================
    public frm_RegistroCliente() {
        // Configuración básica de la ventana
        setTitle("REGISTRO CLIENTE");
        setSize(200, 280);
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
        pnl_Principal = new JPanel();
        pnl_Principal.setLayout(new BorderLayout(10, 10));
        pnl_Principal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel del título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(210, 230, 255));
        lbl_Titulo = new JLabel("REGISTRO CLIENTE");
        lbl_Titulo.setFont(new Font("Arial", Font.BOLD, 25));
        panelTitulo.add(lbl_Titulo);

        // Panel de datos del cliente con TitledBorder
        JPanel panelDatosCliente = new JPanel(new GridBagLayout());
        panelDatosCliente.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Datos del cliente",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 14)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Primera fila: Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelDatosCliente.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txt_Nombre = new JTextField(40);
        panelDatosCliente.add(txt_Nombre, gbc);

        // Segunda fila: Cliente Habitual y Personas
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panelDatosCliente.add(new JLabel("Cliente habitual"), gbc);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        RBtn_Si = new JRadioButton("Sí");
        RBtn_No = new JRadioButton("No");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(RBtn_Si);
        grupo.add(RBtn_No);
        radioPanel.add(RBtn_Si);
        radioPanel.add(RBtn_No);

        gbc.gridx = 1;
        panelDatosCliente.add(radioPanel, gbc);

        gbc.gridx = 2;
        panelDatosCliente.add(new JLabel("#Personas:"), gbc);

        gbc.gridx = 3;
        txt_Personas = new JTextField(10);
        panelDatosCliente.add(txt_Personas, gbc);

        // Panel inferior para Mesero, Estancia y Mesa
        JPanel panelInferior = new JPanel(new GridBagLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbcInf = new GridBagConstraints();
        gbcInf.insets = new Insets(5, 5, 5, 5);
        gbcInf.fill = GridBagConstraints.HORIZONTAL;

        // Primera fila: Mesero y Estancia
        gbcInf.gridx = 0;
        gbcInf.gridy = 0;
        gbcInf.weightx = 0.0;
        panelInferior.add(new JLabel("Mesero:"), gbcInf);

        gbcInf.gridx = 1;
        gbcInf.weightx = 1.0;
        String[] meseros = {"Seleccione un mesero", "Morticia", "Merlina", "Tio Cosa"};
        Mesero = new JComboBox<>(meseros);
        Mesero.setPreferredSize(new Dimension(200, 25));
        panelInferior.add(Mesero, gbcInf);

        gbcInf.gridx = 2;
        gbcInf.weightx = 0.0;
        gbcInf.insets = new Insets(5, 15, 5, 5); // Más espacio a la izquierda
        panelInferior.add(new JLabel("Estancia:"), gbcInf);

        gbcInf.gridx = 3;
        gbcInf.weightx = 1.0;
        gbcInf.insets = new Insets(5, 5, 5, 5);
        String[] lugares = {"Seleccione lugar", "Las Brisas", "Caribe", "El Tesero", "Jardín"};
        Estadia = new JComboBox<>(lugares);
        Estadia.setPreferredSize(new Dimension(200, 25));
        panelInferior.add(Estadia, gbcInf);

        // Segunda fila: Mesa y Botones
        gbcInf.gridx = 0;
        gbcInf.gridy = 1;
        gbcInf.weightx = 0.0;
        panelInferior.add(new JLabel("#Mesa:"), gbcInf);

        gbcInf.gridx = 1;
        gbcInf.weightx = 0.3;
        txt_Mesa = new JTextField(10);
        panelInferior.add(txt_Mesa, gbcInf);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btn_Aceptar = new JButton("Aceptar");
        btn_Cancelar = new JButton("Cancelar");

        // Estilo de los botones
        Dimension buttonSize = new Dimension(100, 30);
        btn_Aceptar.setPreferredSize(buttonSize);
        btn_Cancelar.setPreferredSize(buttonSize);
        btn_Aceptar.setBackground(new Color(70, 130, 180));
        btn_Cancelar.setBackground(new Color(220, 220, 220));

        panelBotones.add(btn_Aceptar);
        panelBotones.add(btn_Cancelar);

        gbcInf.gridx = 2;
        gbcInf.gridwidth = 2;
        gbcInf.weightx = 1.0;
        gbcInf.anchor = GridBagConstraints.EAST;
        panelInferior.add(panelBotones, gbcInf);

        // Contenedor principal
        JPanel contenedorPrincipal = new JPanel(new BorderLayout(10, 10));
        contenedorPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contenedorPrincipal.add(panelTitulo, BorderLayout.NORTH);
        contenedorPrincipal.add(panelDatosCliente, BorderLayout.CENTER);
        contenedorPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // Agregar el contenedor principal al JFrame
        setContentPane(contenedorPrincipal);

        // Ajustar el tamaño de la ventana
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Agregar ActionListener para el botón Cancelar
        btn_Cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(
                        frm_RegistroCliente.this,
                        "¿Está seguro que desea cancelar el registro?",
                        "Confirmar Cancelación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (respuesta == JOptionPane.YES_OPTION) {
                    dispose(); // Cierra la ventana
                }
            }
        });

        // Agregar ActionListener para el botón Aceptar
        btn_Aceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarCampos()) {
                    guardarDatos();
                }
            }
        });
    }

    //===================================================================================
    private void limpiarCampos() {
        txt_Nombre.setText("");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(RBtn_Si);
        grupo.add(RBtn_No);
        grupo.clearSelection();
        txt_Personas.setText("");
        Mesero.setSelectedIndex(0);
        Estadia.setSelectedIndex(0);
        txt_Mesa.setText("");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private boolean validarCampos() {
        // Validar nombre
        if (txt_Nombre.getText().trim().isEmpty()) {
            mostrarError("Por favor ingrese el nombre del cliente");
            return false;
        }

        // Validar selección de cliente habitual
        if (!RBtn_Si.isSelected() && !RBtn_No.isSelected()) {
            mostrarError("Por favor seleccione si es cliente habitual");
            return false;
        }

        // Validar número de personas
        try {
            int personas = Integer.parseInt(txt_Personas.getText().trim());
            if (personas <= 0) {
                mostrarError("El número de personas debe ser mayor a 0");
                return false;
            }
            if (personas >= 101) {
                mostrarError("El número de personas seleccionado excede la capacidad del restaurante");
                return false;
            }
        } catch (NumberFormatException ex) {
            mostrarError("Por favor ingrese un número válido de personas");
            return false;
        }

        // Validar mesero
        if (Mesero.getSelectedIndex() == 0) {
            mostrarError("Por favor seleccione un mesero");
            return false;
        }

        // Validar estancia
        if (Estadia.getSelectedIndex() == 0) {
            mostrarError("Por favor seleccione una estancia");
            return false;
        }

        // Validar número de mesa
        try {
            int mesa = Integer.parseInt(txt_Mesa.getText().trim());
            if (mesa <= 0) {
                mostrarError("El número de mesa debe ser valido");
                return false;
            }
            if (mesa >= 20) {
                mostrarError("El número de mesa debe ser valido");
                return false;
            }
        } catch (NumberFormatException ex) {
            mostrarError("Por favor ingrese un número de mesa válido");
            return false;
        }

        return true;
    }

    private void guardarDatos() {
        try {
            String nombre = txt_Nombre.getText().trim();
            boolean clienteHabitual = RBtn_Si.isSelected();
            int numPersonas = Integer.parseInt(txt_Personas.getText().trim());
            String mesero = (String) Mesero.getSelectedItem();
            String estancia = (String) Estadia.getSelectedItem();
            int numMesa = Integer.parseInt(txt_Mesa.getText().trim());

            // Crear nueva instancia de cls_Clientes
            cls_Clientes cliente = new cls_Clientes(
                    nombre,
                    clienteHabitual,
                    numPersonas,
                    mesero,
                    estancia,
                    numMesa
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Usted ha registrado exitosamente",
                    "Registro Exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            //limpiar los campos después del registro
            limpiarCampos();

        } catch (Exception ex) {
            mostrarError("Error al guardar los datos: " + ex.getMessage());
        }
    }
}