package cafeteria.vista;

import cafeteria.controlador.PrincipalController;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Stack;

public class HistorialPanel extends JPanel {
    private final PrincipalController controller;
    private final JTable tablaHistorial;
    private final DefaultTableModel modeloHistorial;
    private final Stack<Object[]> historialBackup = new Stack<>();

    public HistorialPanel(PrincipalController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 15));
        setBackground(new Color(245, 245, 240)); // crema
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        String[] columnas = {"Fecha y Hora", "Operación"};
        modeloHistorial = new DefaultTableModel(columnas, 0);
        tablaHistorial = new JTable(modeloHistorial);
        styleTable(tablaHistorial);

        JScrollPane scrollTabla = new JScrollPane(tablaHistorial);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(),
            "Registro de Historial de Cafetería",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(111, 78, 55) // marrón café
        ));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(245, 245, 240));

        JButton btnActualizar = createStyledButton("Actualizar", new Color(111, 78, 55));
        btnActualizar.addActionListener(e -> {
            actualizarTabla();
            JOptionPane.showMessageDialog(
                this,
                "Historial actualizado con éxito",
                "Actualización",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        JButton btnLimpiar = createStyledButton("Limpiar Historial", new Color(166, 100, 62)); // marrón cacao
        btnLimpiar.addActionListener(e -> limpiarHistorial());

        JButton btnDeshacerLimpiar = createStyledButton("Deshacer Limpiar", new Color(180, 132, 85)); // marrón caramelo
        btnDeshacerLimpiar.addActionListener(this::deshacerLimpiarHistorial);

        panelBotones.add(btnActualizar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnDeshacerLimpiar);

        add(scrollTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        actualizarTabla();
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 210, 200)); // crema beige
        table.setSelectionBackground(new Color(210, 190, 160)); // crema oscuro
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(111, 78, 55));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                if (column == 1) {
                    String operacion = value.toString().toLowerCase();
                    if (operacion.contains("agreg")) {
                        label.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
                    } else if (operacion.contains("elimin") || operacion.contains("cancel")) {
                        label.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
                    } else if (operacion.contains("modif") || operacion.contains("actualiz")) {
                        label.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
                    } else if (operacion.contains("restaur") || operacion.contains("deshac")) {
                        label.setIcon(UIManager.getIcon("FileView.computerIcon"));
                    } else {
                        label.setIcon(null);
                    }
                } else {
                    label.setIcon(null);
                }
                if (isSelected) {
                    label.setBackground(new Color(111, 78, 55));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(table.getBackground());
                    label.setForeground(table.getForeground());
                }
                return label;
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

    private void actualizarTabla() {
        modeloHistorial.setRowCount(0);
        for (Object obj : controller.getHistorial()) {
            if (obj != null) {
                String[] partes = obj.toString().split(" - ", 2);
                if (partes.length == 2) {
                    modeloHistorial.addRow(new Object[]{partes[0], partes[1]});
                } else {
                    modeloHistorial.addRow(new Object[]{"", obj.toString()});
                }
            }
        }
    }

    private void deshacerLimpiarHistorial(ActionEvent e) {
        if (!historialBackup.isEmpty()) {
            Object[] backup = historialBackup.pop();
            controller.setHistorial(backup);
            actualizarTabla();
            JOptionPane.showMessageDialog(this,
                "<html><div style='text-align:center;'><h3 style='color:#6B4C3B;'>Historial Restaurado</h3>"
                + "<p>Registros recuperados: " + backup.length + "</p></div></html>",
                "Historial Restaurado",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(this,
                "No hay un historial recientemente limpiado",
                "Deshacer Limpiar Historial",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void limpiarHistorial() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "<html><b>¿Está seguro de limpiar todo el historial?</b><br><br>"
            + "Esta acción no se puede deshacer.</html>",
            "Confirmar limpieza",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            historialBackup.push(controller.getHistorial());
            controller.limpiarHistorial();
            actualizarTabla();
            JOptionPane.showMessageDialog(this,
                "<html><div style='text-align:center;'><h3 style='color:#A65C41;'>Historial Limpiado</h3>" +
                "<p>Se eliminaron todos los registros.</p>" +
                "<p>Puedes usar 'Deshacer Limpiar Historial' para recuperarlos.</p></div></html>",
                "Historial Vacío",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
