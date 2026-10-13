
package cafeteria.vista;

import cafeteria.controlador.PrincipalController;
import cafeteria.modelo.Cliente;
import cafeteria.modelo.estructuras.ArbolBinario;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ClientesPanel extends JPanel {
    private final PrincipalController controller;
    private final JTextField txtNombre, txtApellido, txtEdad, txtTelefono;
    private final JTextField txtBuscar;
    private final JTable tabla;
    private DefaultTableModel modeloTabla;
    private ArbolBinario arbolCliente;

    public ClientesPanel(PrincipalController controller) {
        this.controller = controller;
        this.arbolCliente = new ArbolBinario();
        setLayout(new BorderLayout(10, 15));
        setBackground(new Color(245, 240, 230));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setBackground(new Color(245, 240, 230));

        panelBusqueda.add(new JLabel("Búsqueda por ID:"));
        txtBuscar = new JTextField(18);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelBusqueda.add(txtBuscar);

        JButton btnBuscar = createStyledButton("Buscar", new Color(139, 69, 19));
        btnBuscar.addActionListener(this::buscarPorIdArbol);
        panelBusqueda.add(btnBuscar);

        JButton btnLimpiar = createStyledButton("Limpiar", new Color(160, 82, 45));
        btnLimpiar.addActionListener(e -> actualizarTabla());
        panelBusqueda.add(btnLimpiar);

        JPanel panelEntrada = new JPanel(new GridLayout(4, 2, 10, 12));
        panelEntrada.setBackground(new Color(245, 240, 230));
        panelEntrada.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addLabelAndComponent(panelEntrada, "Nombre:", txtNombre = new JTextField());
        addLabelAndComponent(panelEntrada, "Apellido:", txtApellido = new JTextField());
        addLabelAndComponent(panelEntrada, "Edad:", txtEdad = new JTextField());
        addLabelAndComponent(panelEntrada, "Teléfono:", txtTelefono = new JTextField());

        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 8, 8));
        panelBotones.setBackground(new Color(245, 240, 230));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnAgregar = createStyledButton("Agregar", new Color(205, 133, 63));
        JButton btnActualizar = createStyledButton("Modificar", new Color(160, 82, 45));
        JButton btnPorEdad = createStyledButton("Mostrar por Edad", new Color(210, 105, 30));
        JButton btnEliminar = createStyledButton("Eliminar", new Color(165, 42, 42));

        btnAgregar.addActionListener(this::agregarCliente);
        btnActualizar.addActionListener(this::actualizarCliente);
        btnPorEdad.addActionListener(this::mostrarPorEdad);
        btnEliminar.addActionListener(this::eliminarCliente);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnPorEdad);
        panelBotones.add(btnEliminar);

        String[] columnas = {"ID", "Nombre Completo", "Edad", "Teléfono"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        styleTable(tabla);

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(),
            "Lista de Clientes",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(160, 82, 45)
        ));

        tabla.getSelectionModel().addListSelectionListener(e -> cargarDatosSeleccionados());

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 15));
        panelSuperior.setBackground(new Color(245, 240, 230));
        panelSuperior.add(panelBusqueda, BorderLayout.NORTH);
        panelSuperior.add(panelEntrada, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);

        reconstruirArbol();
        actualizarTabla();
    }

    private void addLabelAndComponent(JPanel panel, String labelText, JComponent component) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(label);

        component.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        component.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 180, 140)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(component);
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 220, 200));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(160, 82, 45));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (column == 2) {
                    try {
                        int edad = Integer.parseInt(value.toString());
                        if (edad < 18) {
                            c.setBackground(new Color(255, 248, 220));
                        } else if (edad >= 18 && edad <= 60) {
                            c.setBackground(new Color(245, 222, 179));
                        } else {
                            c.setBackground(new Color(210, 180, 140));
                        }
                        c.setForeground(Color.BLACK);
                    } catch (NumberFormatException e) {
                    }
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }

                if (isSelected) {
                    c.setBackground(new Color(139, 69, 19));
                    c.setForeground(Color.WHITE);
                }

                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setIconTextGap(10);
        return button;
    }

    private void reconstruirArbol() {
        arbolCliente = new ArbolBinario();
        for (Cliente c : controller.getClientes()) {
            arbolCliente.insertar(c);
        }
    }

    private void buscarPorIdArbol(ActionEvent e) {
        modeloTabla.setRowCount(0);
        try {
            int id = Integer.parseInt(txtBuscar.getText());
            if (arbolCliente.buscar(id)) {
                Cliente c = controller.buscarCliente(id);
                modeloTabla.addRow(new Object[]{c.getId(), c.getNombreCompleto(), c.getEdad(), c.getTelefono()});
            } else {
                JOptionPane.showMessageDialog(this,
                    "Cliente no encontrado en el árbol",
                    "Buscar",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarPorEdad(ActionEvent e) {
        modeloTabla.setRowCount(0);

        List<Cliente> lista = new ArrayList<>();
        for (Cliente c : controller.getClientes()) lista.add(c);
        lista.sort((a, b) -> Integer.compare(a.getEdad(), b.getEdad()));

        modeloTabla.addRow(new Object[]{"-- Clientes Menores de Edad --", "", "", ""});
        for (Cliente c : lista) {
            if (c.getEdad() < 18) {
                modeloTabla.addRow(new Object[]{c.getId(), c.getNombreCompleto(), c.getEdad(), c.getTelefono()});
            }
        }

        modeloTabla.addRow(new Object[]{"-- Clientes Mayores de Edad (18-60) --", "", "", ""});
        for (Cliente c : lista) {
            if (c.getEdad() >= 18 && c.getEdad() <= 60) {
                modeloTabla.addRow(new Object[]{c.getId(), c.getNombreCompleto(), c.getEdad(), c.getTelefono()});
            }
        }

        modeloTabla.addRow(new Object[]{"-- Clientes Tercera Edad (>60) --", "", "", ""});
        for (Cliente c : lista) {
            if (c.getEdad() > 60) {
                modeloTabla.addRow(new Object[]{c.getId(), c.getNombreCompleto(), c.getEdad(), c.getTelefono()});
            }
        }
    }

    private void agregarCliente(ActionEvent e) {
        try {
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            int edad = Integer.parseInt(txtEdad.getText());
            String telefono = txtTelefono.getText();

            controller.agregarCliente(nombre, apellido, edad, telefono);
            reconstruirArbol();
            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(this,
                "<html><div style='text-align:center;'><h3 style='color:#8B4513;'>Cliente Agregado</h3>"
                + "<p>Se ha registrado un nuevo cliente.</p></div></html>",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Edad debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarCliente(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila >= 0 && modeloTabla.getValueAt(fila, 0) instanceof Integer) {
            try {
                int id = (int) modeloTabla.getValueAt(fila, 0);
                String nombre = txtNombre.getText();
                String apellido = txtApellido.getText();
                int edad = Integer.parseInt(txtEdad.getText());
                String telefono = txtTelefono.getText();

                controller.actualizarCliente(id, nombre, apellido, edad, telefono);
                reconstruirArbol();
                actualizarTabla();
                limpiarCampos();

                JOptionPane.showMessageDialog(this,
                    "<html><div style='text-align:center;'><h3 style='color:#A0522D;'>Cliente Modificado</h3>"
                    + "<p>Se han actualizado los datos del cliente.</p></div></html>",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Edad debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente a modificar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarCliente(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila >= 0 && modeloTabla.getValueAt(fila, 0) instanceof Integer) {
            int id = (int) modeloTabla.getValueAt(fila, 0);

            if (JOptionPane.showConfirmDialog(this,
                    "<html><b>¿Está seguro de eliminar al cliente?</b><br><br>"
                    + "ID: " + id + "<br>"
                    + "Nombre: " + tabla.getValueAt(fila, 1) + "</html>",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {

                controller.eliminarCliente(id);
                reconstruirArbol();
                actualizarTabla();
                limpiarCampos();

                JOptionPane.showMessageDialog(this,
                    "<html><div style='text-align:center;'><h3 style='color:#CD5C5C;'>Cliente Eliminado</h3>"
                    + "<p>Se ha eliminado el cliente.</p></div></html>",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente válido para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarDatosSeleccionados() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0 && modeloTabla.getValueAt(fila, 0) instanceof Integer) {
            String[] partes = tabla.getValueAt(fila, 1).toString().split(" ", 2);
            txtNombre.setText(partes[0]);
            txtApellido.setText(partes.length > 1 ? partes[1] : "");
            txtEdad.setText(tabla.getValueAt(fila, 2).toString());
            txtTelefono.setText(tabla.getValueAt(fila, 3).toString());
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Cliente c : controller.getClientes()) {
            modeloTabla.addRow(new Object[]{c.getId(), c.getNombreCompleto(), c.getEdad(), c.getTelefono()});
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtEdad.setText("");
        txtTelefono.setText("");
        txtBuscar.setText("");
    }
}
