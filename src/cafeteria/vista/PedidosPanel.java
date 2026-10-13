package cafeteria.vista;

import cafeteria.controlador.PrincipalController;
import cafeteria.modelo.Pedido;
import cafeteria.modelo.estructuras.ListaDoble;
import cafeteria.modelo.Barista;
import cafeteria.modelo.Cliente;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PedidosPanel extends JPanel {
    private final PrincipalController controller;
    private final JComboBox<String> cbClientes, cbBaristas, cbPrioridad;
    private final JSpinner spinnerFecha, spinnerHora;
    private final JTextArea txtDetalle;
    private final JTable tablaPedidos;
    private final DefaultTableModel modeloPedidos;
    private final JButton btnOrdenar;
    private final JTextField txtBuscarId;
    private final ListaDoble pedidosCancelados = new ListaDoble();

    public PedidosPanel(PrincipalController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(10, 15));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setBackground(new Color(245, 245, 245));

        panelBusqueda.add(new JLabel("Buscar por ID:"));
        txtBuscarId = new JTextField(18);
        txtBuscarId.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelBusqueda.add(txtBuscarId);

        JButton btnBuscarId = createStyledButton("Buscar");
        btnBuscarId.addActionListener(this::buscarPedidoPorId);
        panelBusqueda.add(btnBuscarId);

        JButton btnMostrarTodos = createStyledButton("Mostrar Todos");
        btnMostrarTodos.addActionListener(e -> {
            actualizarComboBoxes();
            actualizarTabla();
        });
        panelBusqueda.add(btnMostrarTodos);

        JPanel panelEntrada = new JPanel(new GridLayout(2, 2, 10, 12));
        panelEntrada.setBackground(new Color(245, 245, 245));
        panelEntrada.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addLabelAndComponent(panelEntrada, "Cliente:", cbClientes = new JComboBox<>());
        addLabelAndComponent(panelEntrada, "Barista:", cbBaristas = new JComboBox<>());

        panelEntrada.add(new JLabel("Fecha:"));
        spinnerFecha = new JSpinner(new SpinnerDateModel());
        spinnerFecha.setEditor(new JSpinner.DateEditor(spinnerFecha, "yyyy-MM-dd"));
        styleSpinner(spinnerFecha);
        panelEntrada.add(spinnerFecha);

        panelEntrada.add(new JLabel("Hora:"));
        spinnerHora = new JSpinner(new SpinnerDateModel());
        spinnerHora.setEditor(new JSpinner.DateEditor(spinnerHora, "HH:mm"));
        styleSpinner(spinnerHora);
        panelEntrada.add(spinnerHora);

        addLabelAndComponent(panelEntrada, "Prioridad:", cbPrioridad = new JComboBox<>(new String[]{"Baja", "Media", "Alta"}));

        panelEntrada.add(new JLabel("Detalle:"));
        txtDetalle = new JTextArea(2, 20);
        txtDetalle.setLineWrap(true);
        txtDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollDetalle = new JScrollPane(txtDetalle);
        scrollDetalle.setBorder(BorderFactory.createLineBorder(new Color(239, 235, 233)));
        panelEntrada.add(scrollDetalle);

        JPanel panelBotones = new JPanel(new GridLayout(1, 5, 8, 8));
        panelBotones.setBackground(new Color(245, 245, 245));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnRegistrar = createStyledButton("Registrar Pedido");
        JButton btnCancelar = createStyledButton("Cancelar Pedido");
        JButton btnDeshacer = createStyledButton("Deshacer Cancelación");
        btnOrdenar = createStyledButton("Ordenar por Prioridad");
        JButton btnRecibo = createStyledButton("Generar Recibo");

        btnRegistrar.addActionListener(this::registrarPedido);
        btnCancelar.addActionListener(this::cancelarPedido);
        btnDeshacer.addActionListener(this::deshacerCancelacion);
        btnOrdenar.addActionListener(e -> ordenarPedidosPorPrioridad());
        btnRecibo.addActionListener(this::generarRecibo);

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnDeshacer);
        panelBotones.add(btnOrdenar);
        panelBotones.add(btnRecibo);

        String[] columnasPedidos = {"ID", "Cliente", "Barista", "Fecha", "Hora", "Prioridad", "Detalle"};
        modeloPedidos = new DefaultTableModel(columnasPedidos, 0);
        tablaPedidos = new JTable(modeloPedidos);
        styleTable(tablaPedidos);

        JScrollPane scrollPedidos = new JScrollPane(tablaPedidos);
        scrollPedidos.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(),
            "Lista de Pedidos Registrados",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(109, 76, 65)
        ));

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 15));
        panelSuperior.setBackground(new Color(245, 245, 245));
        panelSuperior.add(panelBusqueda, BorderLayout.NORTH);
        panelSuperior.add(panelEntrada, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPedidos, BorderLayout.CENTER);

        cargarComboBoxes();
        actualizarTabla();
    }

    private void addLabelAndComponent(JPanel panel, String labelText, JComponent component) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(label);

        component.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        if (component instanceof JComboBox) {
            ((JComboBox<?>) component).setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    return this;
                }
            });
        }
        panel.add(component);
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        spinner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(239, 235, 233)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(239, 235, 233));
        table.setSelectionBackground(new Color(215, 204, 200));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(109, 76, 65));
        header.setForeground(Color.WHITE);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (column == 5) {
                    String prioridad = value.toString();
                    switch (prioridad) {
                        case "Alta":
                            c.setBackground(new Color(239, 154, 154));
                            break;
                        case "Media":
                            c.setBackground(new Color(255, 236, 179));
                            break;
                        case "Baja":
                            c.setBackground(new Color(200, 230, 201));
                            break;
                        default:
                            c.setBackground(table.getBackground());
                    }
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }

                if (isSelected) {
                    c.setBackground(new Color(188, 170, 164));
                    c.setForeground(Color.WHITE);
                }

                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(109, 76, 65));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(62, 39, 35), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void cargarComboBoxes() {
        actualizarComboBoxes();
    }

    private void actualizarComboBoxes() {
        cbClientes.removeAllItems();
        cbBaristas.removeAllItems();

        for (Cliente cliente : controller.getClientes()) {
            if (cliente != null) cbClientes.addItem(cliente.getNombreCompleto());
        }

        for (Barista barista : controller.getBaristas()) {
            if (barista != null) cbBaristas.addItem(barista.getNombreCompleto());
        }

        if (cbClientes.getItemCount() > 0) cbClientes.setSelectedIndex(0);
        if (cbBaristas.getItemCount() > 0) cbBaristas.setSelectedIndex(0);
    }

    private void registrarPedido(ActionEvent e) {
        try {
            String nombreCliente = (String) cbClientes.getSelectedItem();
            String nombreBarista = (String) cbBaristas.getSelectedItem();
            Date fecha = (Date) spinnerFecha.getValue();
            Date hora = (Date) spinnerHora.getValue();
            int prioridad = cbPrioridad.getSelectedIndex() + 1;
            String detalle = txtDetalle.getText();

            Cliente cliente = controller.buscarClientePorNombreCompleto(nombreCliente);
            Barista barista = controller.buscarBaristaPorNombreCompleto(nombreBarista);

            if (cliente != null && barista != null) {
                int nuevoId = controller.getPedidos().length + 1;

                SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");

                Pedido nuevoPedido = new Pedido(nuevoId, cliente, barista,
                        sdfFecha.format(fecha), sdfHora.format(hora), prioridad, detalle);

                controller.agregarPedido(nuevoPedido);
                actualizarTabla();
                limpiarCampos();

                String mensaje = "<html><div style='text-align:center;'><h3 style='color:#6D4C41;'>✓ Pedido Registrado</h3>"
                        + "<p>Cliente: " + cliente.getNombreCompleto() + "</p>"
                        + "<p>Barista: " + barista.getNombreCompleto() + "</p>"
                        + "<p>Fecha: " + nuevoPedido.getFecha() + " " + nuevoPedido.getHora() + "</p></div></html>";

                JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cliente o barista no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar pedido: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarPedido(ActionEvent e) {
        int filaSeleccionada = tablaPedidos.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int idPedido = (int) tablaPedidos.getValueAt(filaSeleccionada, 0);
            String cliente = tablaPedidos.getValueAt(filaSeleccionada, 1).toString();
            String fechaHora = tablaPedidos.getValueAt(filaSeleccionada, 3) + " " + tablaPedidos.getValueAt(filaSeleccionada, 4);

            int confirm = JOptionPane.showConfirmDialog(this,
                "<html><b>¿Está seguro de cancelar el pedido?</b><br><br>"
                + "ID: " + idPedido + "<br>"
                + "Cliente: " + cliente + "<br>"
                + "Fecha: " + fechaHora + "</html>",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                Pedido pedido = controller.buscarPedidoPorId(idPedido);
                if (pedido != null) {
                    controller.eliminarPedido(pedido);
                    pedidosCancelados.agregarAlFinal(pedido);
                    actualizarTabla();

                    JOptionPane.showMessageDialog(this,
                        "<html><div style='text-align:center;'><h3 style='color:#6D4C41;'>Pedido Cancelado</h3>"
                        + "<p>Puedes usar 'Deshacer Cancelación' para recuperarlo.</p></div></html>",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el pedido", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para cancelar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deshacerCancelacion(ActionEvent e) {
        if (pedidosCancelados.getTamaño() > 0) {
            Pedido pedido = pedidosCancelados.getPedido(pedidosCancelados.getTamaño() - 1);
            pedidosCancelados.eliminarAlFinal();

            if (pedido != null) {
                controller.agregarPedido(pedido);
                actualizarTabla();

                String mensaje = "<html><div style='text-align:center;'><h3 style='color:#6D4C41;'>Pedido Restaurado</h3>"
                        + "<p>ID: " + pedido.getId() + "</p>"
                        + "<p>Cliente: " + pedido.getCliente().getNombreCompleto() + "</p>"
                        + "<p>Fecha: " + pedido.getFecha() + " " + pedido.getHora() + "</p></div></html>";

                JOptionPane.showMessageDialog(this, mensaje, "Cancelación Deshecha", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "No hay pedidos cancelados recientes para restaurar",
                "Deshacer Cancelación",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscarPedidoPorId(ActionEvent e) {
        String idText = txtBuscarId.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID de pedido", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            Pedido pedido = controller.buscarPedidoPorId(id);
            modeloPedidos.setRowCount(0);

            if (pedido != null) {
                modeloPedidos.addRow(new Object[]{
                    pedido.getId(), pedido.getCliente().getNombreCompleto(),
                    pedido.getBarista().getNombreCompleto(), pedido.getFecha(),
                    pedido.getHora(), pedido.getPrioridadTexto(), pedido.getDetalle()});

                JOptionPane.showMessageDialog(this,
                    "<html><div style='text-align:center;'><h3 style='color:#6D4C41;'>Pedido Encontrado</h3>"
                    + "<p>" + pedido + "</p></div></html>",
                    "Resultados de Búsqueda",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se encontró pedido con ID: " + id,
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarRecibo(ActionEvent e) {
        int fila = tablaPedidos.getSelectedRow();
        if (fila >= 0) {
            int idPedido = (int) tablaPedidos.getValueAt(fila, 0);

            for (Object obj : controller.getPedidos()) {
                Pedido pedido = (Pedido) obj;
                if (pedido.getId() == idPedido) {
                    String recibo = "<html><div style='font-family:Segoe UI; width:350px; text-align:center;'>"
                            + "<div style='background:#6D4C41; color:white; padding:10px; border-radius:5px 5px 0 0;'>"
                            + "<h2 style='margin:0;'>☕ PEDIDO DE CAFETERÍA</h2></div>"
                            + "<div style='background:#F5F5F5; padding:15px; text-align:left; border-radius:0 0 5px 5px;'>"
                            + "<p><b>✔ Código Pedido:</b> " + pedido.getId() + "</p>"
                            + "<p><b>✔ Cliente:</b> " + pedido.getCliente().getNombreCompleto() + "</p>"
                            + "<p><b>✔ Barista:</b> " + pedido.getBarista().getNombreCompleto() + " (" + pedido.getBarista().getEspecialidad() + ")</p>"
                            + "<p><b>✔ Fecha:</b> " + pedido.getFecha() + "</p>"
                            + "<p><b>✔ Hora:</b> " + pedido.getHora() + "</p>"
                            + "<p><b>✔ Prioridad:</b> " + pedido.getPrioridadTexto() + "</p>"
                            + "<p><b>✔ Detalle:</b> " + pedido.getDetalle() + "</p>"
                            + "</div></div></html>";

                    JOptionPane.showMessageDialog(this, recibo, "Recibo de Pedido", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para generar el recibo", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void ordenarPedidosPorPrioridad() {
        modeloPedidos.setRowCount(0);

        for (Object obj : controller.getPedidosPrioridad()) {
            Pedido pedido = (Pedido) obj;
            modeloPedidos.addRow(new Object[]{
                pedido.getId(), pedido.getCliente().getNombreCompleto(),
                pedido.getBarista().getNombreCompleto(), pedido.getFecha(),
                pedido.getHora(), pedido.getPrioridadTexto(), pedido.getDetalle()});
        }
        btnOrdenar.setEnabled(false);
    }

    private void actualizarTabla() {
        modeloPedidos.setRowCount(0);

        for (Object obj : controller.getPedidos()) {
            Pedido pedido = (Pedido) obj;
            modeloPedidos.addRow(new Object[]{
                pedido.getId(), pedido.getCliente().getNombreCompleto(),
                pedido.getBarista().getNombreCompleto(), pedido.getFecha(),
                pedido.getHora(), pedido.getPrioridadTexto(), pedido.getDetalle()});
        }
        btnOrdenar.setEnabled(true);
    }

    private void limpiarCampos() {
        cbClientes.setSelectedIndex(0);
        cbBaristas.setSelectedIndex(0);
        spinnerFecha.setValue(new Date());
        spinnerHora.setValue(new Date());
        cbPrioridad.setSelectedIndex(0);
        txtDetalle.setText("");
    }
}
