package cafeteria.vista;

import cafeteria.controlador.PrincipalController;
import cafeteria.modelo.Barista;
import cafeteria.modelo.estructuras.ListaCircular;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;

public class BaristasPanel extends JPanel {
    private final PrincipalController controller;

    private final JTextField txtNombre, txtApellido, txtEspecialidad, txtTelefono;
    private final JTextField txtBuscar;

    private final JTable tabla;
    private final DefaultTableModel modeloTabla;

    private final JButton btnSiguienteBarista;

    public BaristasPanel(PrincipalController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 15));
        setBackground(new Color(245, 240, 230));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setBackground(new Color(245, 240, 230));

        panelBusqueda.add(new JLabel("Buscar por nombre:"));
        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelBusqueda.add(txtBuscar);

        JButton btnBuscar = createStyledButton("Buscar", new Color(181, 101, 29));
        btnBuscar.addActionListener(e -> buscarBaristas());
        panelBusqueda.add(btnBuscar);

        JButton btnLimpiar = createStyledButton("Limpiar", new Color(160, 82, 45));
        btnLimpiar.addActionListener(e -> limpiarBusqueda());
        panelBusqueda.add(btnLimpiar);

        JPanel panelEntrada = new JPanel(new GridLayout(4, 2, 10, 12));
        panelEntrada.setBackground(new Color(245, 240, 230));
        panelEntrada.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addLabelAndComponent(panelEntrada, "Nombre:", txtNombre = new JTextField());
        addLabelAndComponent(panelEntrada, "Apellido:", txtApellido = new JTextField());
        addLabelAndComponent(panelEntrada, "Especialidad:", txtEspecialidad = new JTextField());
        addLabelAndComponent(panelEntrada, "Teléfono:", txtTelefono = new JTextField());

        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 8, 8));
        panelBotones.setBackground(new Color(245, 240, 230));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnAgregar = createStyledButton("Agregar", new Color(139, 69, 19));
        JButton btnActualizar = createStyledButton("Modificar", new Color(160, 82, 45));
        btnSiguienteBarista = createStyledButton("Siguiente Barista", new Color(181, 101, 29));
        JButton btnEliminar = createStyledButton("Eliminar", new Color(128, 0, 0));

        btnAgregar.addActionListener(this::agregarBarista);
        btnActualizar.addActionListener(this::actualizarBarista);
        btnSiguienteBarista.addActionListener(this::mostrarSiguienteBarista);
        btnEliminar.addActionListener(this::eliminarBarista);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnSiguienteBarista);
        panelBotones.add(btnEliminar);

        String[] columnas = {"ID", "Nombre Completo", "Especialidad", "Teléfono"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        styleTable(tabla);

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(),
            "Lista de Baristas",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(111, 78, 55)
        ));

        tabla.getSelectionModel().addListSelectionListener(this::cargarDatosSeleccionados);

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 15));
        panelSuperior.setBackground(new Color(245, 240, 230));
        panelSuperior.add(panelBusqueda, BorderLayout.NORTH);
        panelSuperior.add(panelEntrada, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);

        actualizarTabla();
    }

    private void addLabelAndComponent(JPanel panel, String labelText, JComponent component) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(label);

        component.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        component.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 190, 180)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(component);
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(200, 190, 180));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(111, 78, 55));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
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

    private void limpiarBusqueda() {
        txtBuscar.setText("");
        actualizarTabla();
    }

    private void mostrarSiguienteBarista(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un Barista", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idActual = (int) modeloTabla.getValueAt(fila, 0);
        Barista actual = controller.buscarBarista(idActual);
        if (actual == null) return;

        ListaCircular lista = controller.getListaCircularBaristas();
        Barista siguiente = (Barista) lista.siguiente(actual);

        String recibo = "<html><div style='font-family:Segoe UI; text-align:center;'>"
                       + "<div style='background:#6F4E37; color:white; padding:10px; border-radius:5px 5px 0 0;'>"
                       + "<h2 style='margin:0;'>SIGUIENTE BARISTA</h2></div>"
                       + "<div style='background:#F5F0E6; padding:15px; text-align:left; border-radius:0 0 5px 5px;'>"
                       + "<p><b>Barista Actual:</b> " + actual.getNombreCompleto() + " (" + actual.getEspecialidad() + ")</p>"
                       + "<hr style='border:0; border-top:1px solid #ddd;'>"
                       + (siguiente != null
                       ? "<p><b>Siguiente Barista:</b> " + siguiente.getNombreCompleto() + " (" + siguiente.getEspecialidad() + ")</p>"
                       : "<p>✔ No hay más baristas en la lista.</p>")
                       + "</div></div></html>";

        JOptionPane.showMessageDialog(this, recibo, "Siguiente Barista", JOptionPane.INFORMATION_MESSAGE);

        if (siguiente != null) {
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                if (modeloTabla.getValueAt(i, 0).equals(siguiente.getId())) {
                    tabla.setRowSelectionInterval(i, i);
                    break;
                }
            }
        }
    }

    private void buscarBaristas() {
        String filtro = txtBuscar.getText().toLowerCase();
        modeloTabla.setRowCount(0);

        for (Barista b : controller.getBaristas()) {
            if (b.getNombreCompleto().toLowerCase().contains(filtro)) {
                modeloTabla.addRow(new Object[]{b.getId(), b.getNombreCompleto(), b.getEspecialidad(), b.getTelefono()});
            }
        }
    }

    private void agregarBarista(ActionEvent e) {
        controller.agregarBarista(
            txtNombre.getText(),
            txtApellido.getText(),
            txtEspecialidad.getText(),
            txtTelefono.getText()
        );

        actualizarTabla();
        limpiarCampos();

        JOptionPane.showMessageDialog(this,
            "<html><div style='text-align:center;'><h3 style='color:#8B4513;'>Barista Agregado</h3>"
            + "<p>Se ha registrado un nuevo barista.</p></div></html>",
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarBarista(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            controller.actualizarBarista(
                id,
                txtNombre.getText(),
                txtApellido.getText(),
                txtEspecialidad.getText(),
                txtTelefono.getText()
            );

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(this,
                "<html><div style='text-align:center;'><h3 style='color:#6F4E37;'>Barista Modificado</h3>"
                + "<p>Se han actualizado los datos del barista.</p></div></html>",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione el barista a modificar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarBarista(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            String nombre = modeloTabla.getValueAt(fila, 1).toString();

            if (JOptionPane.showConfirmDialog(this,
                "<html><b>¿Está seguro de eliminar al barista?</b><br><br>"
                + "ID: " + id + "<br>"
                + "Nombre: " + nombre + "</html>",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {

                controller.eliminarBarista(id);
                actualizarTabla();
                limpiarCampos();

                JOptionPane.showMessageDialog(this,
                    "<html><div style='text-align:center;'><h3 style='color:#8B0000;'>Barista Eliminado</h3>"
                    + "<p>Se ha eliminado el barista.</p></div></html>",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un barista para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarDatosSeleccionados(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String[] partes = modeloTabla.getValueAt(fila, 1).toString().split(" ", 2);
                txtNombre.setText(partes[0]);
                txtApellido.setText(partes.length > 1 ? partes[1] : "");
                txtEspecialidad.setText(modeloTabla.getValueAt(fila, 2).toString());
                txtTelefono.setText(modeloTabla.getValueAt(fila, 3).toString());
            }
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);

        for (Barista b : controller.getBaristas()) {
            modeloTabla.addRow(new Object[]{
                b.getId(),
                b.getNombreCompleto(),
                b.getEspecialidad(),
                b.getTelefono()
            });
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtEspecialidad.setText("");
        txtTelefono.setText("");
        txtBuscar.setText("");
    }
}
