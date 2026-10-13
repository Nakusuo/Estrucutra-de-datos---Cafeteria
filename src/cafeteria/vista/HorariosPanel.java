package cafeteria.vista;

import cafeteria.controlador.PrincipalController;
import cafeteria.modelo.estructuras.Grafo;
import cafeteria.modelo.Pedido;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

public class HorariosPanel extends JPanel {
    private PrincipalController controller;
    private JTable tablaHorarios;
    private DefaultTableModel modeloHorarios;
    private final JSpinner spinnerFecha;
    private final String[] nombresDias = {"Lunes ", "Martes ", "Miércoles ", "Jueves ", "Viernes ", "Sábado ", "Domingo "};

    public HorariosPanel(PrincipalController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(10, 15));
        setBackground(new Color(245, 245, 240)); // fondo crema cafetería
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.setBackground(new Color(245, 245, 240));

        panelSuperior.add(new JLabel("Semana a mostrar:"));
        spinnerFecha = new JSpinner(new SpinnerDateModel());
        spinnerFecha.setEditor(new JSpinner.DateEditor(spinnerFecha, "dd/MM/yyyy"));
        styleSpinner(spinnerFecha);
        panelSuperior.add(spinnerFecha);

        JButton btnCambiarSemana = createStyledButton("Cambiar Semana", new Color(111, 78, 55)); // marrón cafecito
        btnCambiarSemana.addActionListener(e -> cambiarSemana());
        panelSuperior.add(btnCambiarSemana);

        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        legendPanel.setBackground(new Color(245, 245, 240));

        JLabel greenBox = new JLabel();
        greenBox.setOpaque(true);
        greenBox.setBackground(new Color(212, 193, 157)); // beige cafetería
        greenBox.setBorder(BorderFactory.createLineBorder(new Color(111, 78, 55)));
        greenBox.setPreferredSize(new Dimension(15, 15));
        legendPanel.add(greenBox);
        legendPanel.add(new JLabel("Disponible"));

        JLabel redBox = new JLabel();
        redBox.setOpaque(true);
        redBox.setBackground(new Color(193, 110, 86)); // marrón rojizo cafetería
        redBox.setBorder(BorderFactory.createLineBorder(new Color(111, 78, 55)));
        redBox.setPreferredSize(new Dimension(15, 15));
        legendPanel.add(redBox);
        legendPanel.add(new JLabel("Ocupado"));

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(new Color(245, 245, 240));
        topContainer.add(panelSuperior);
        topContainer.add(legendPanel);
        add(topContainer, BorderLayout.NORTH);

        String[] columnas = new String[8];
        columnas[0] = "Hora";
        System.arraycopy(nombresDias, 0, columnas, 1, 7);

        modeloHorarios = new DefaultTableModel(columnas, 8) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaHorarios = new JTable(modeloHorarios);
        styleTable(tablaHorarios);

        JScrollPane scrollTabla = new JScrollPane(tablaHorarios);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(),
            "Horario Semanal",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(111, 78, 55)
        ));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(245, 245, 240));

        JButton btnActualizar = createStyledButton("Actualizar Horario", new Color(111, 78, 55));
        JButton btnVisualizarGrafo = createStyledButton("Visualizar Relaciones", new Color(90, 58, 42));

        btnActualizar.addActionListener(e -> actualizarHorario());
        btnVisualizarGrafo.addActionListener(this::mostrarGrafoRelaciones);

        panelBotones.add(btnActualizar);
        panelBotones.add(btnVisualizarGrafo);

        add(scrollTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        spinnerFecha.setValue(controller.getSemanaActual());
        actualizarFechasEncabezados();
        cargarHorarios();
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        spinner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 200, 180)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(34);
        table.setShowGrid(true);
        table.setGridColor(new Color(224, 202, 162)); // beige claro

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(111, 78, 55));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (column == 0) {
                    c.setBackground(new Color(230, 220, 210)); // beige muy claro
                    c.setForeground(Color.BLACK);
                } else {
                    if (value != null && "        OCUPADO".equals(value.toString())) {
                        c.setBackground(new Color(193, 110, 86)); // marrón rojizo claro
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(212, 193, 157)); // beige disponible
                        c.setForeground(Color.BLACK);
                    }
                }
                if (value != null && !value.toString().isEmpty()) {
                    setToolTipText("Haz clic para ver detalles del pedido");
                } else {
                    setToolTipText(null);
                }

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
        return button;
    }

    private void cambiarSemana() {
        controller.setSemanaActual((Date) spinnerFecha.getValue());
        actualizarFechasEncabezados();
    }

    private void actualizarFechasEncabezados() {
        String[] fechas = controller.getFechasSemana();
        JTableHeader header = tablaHorarios.getTableHeader();
        TableColumnModel columnModel = header.getColumnModel();

        for (int i = 1; i <= 7; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setHeaderValue(nombresDias[i-1] + "\n" + fechas[i-1]);
        }
        header.repaint();
    }

    private void cargarHorarios() {
        for (int i = 0; i < 8; i++) {
            int startHour = 8 + i;
            int endHour = startHour + 1;
            String intervalo = String.format("     %02d:00  -  %02d:00", startHour, endHour);
            modeloHorarios.setValueAt(intervalo, i, 0);
        }

        for (int dia = 0; dia < 7; dia++) {
            for (int hora = 0; hora < 8; hora++) {
                if (controller.estaOcupado(dia, hora)) {
                    modeloHorarios.setValueAt("        OCUPADO", hora, dia + 1);
                } else {
                    modeloHorarios.setValueAt("", hora, dia + 1);
                }
            }
        }
    }

    private void actualizarHorario() {
        cargarHorarios();
        JOptionPane.showMessageDialog(this,
            "Horario actualizado con éxito",
            "Actualización",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarGrafoRelaciones(ActionEvent e) {
        Grafo grafo = new Grafo();

        for (Pedido pedido : controller.getPedidos()) {
            grafo.agregarRelacion(pedido.getBarista(), pedido.getCliente());
        }

        StringBuilder relacionesText = new StringBuilder();
        relacionesText.append("=======================================\n");
        relacionesText.append(" 📎RELACIONES ENTRE BARISTA Y CLIENTE\n");
        relacionesText.append("=======================================\n\n");
        relacionesText.append(grafo.imprimirGrafo());

        JTextArea textArea = new JTextArea(relacionesText.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Relaciones Barista-Cliente",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
