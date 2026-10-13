package cafeteria.vista;

import cafeteria.controlador.PrincipalController;
import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PrincipalPanel extends JFrame {
    private final PrincipalController controller;

    private final Color[] cardColors = {
        new Color(111, 78, 55),   // Café
        new Color(139, 115, 85),  // Oliva marrón
        new Color(224, 202, 162), // Beige suave
        new Color(245, 240, 220), // Fondo claro crema
        new Color(61, 43, 31)     // Marrón oscuro
    };

    private JPanel bienvenidaPanel;

    public PrincipalPanel(PrincipalController controller) {
        this.controller = controller;
        setTitle("Sistema de Pedidos - Cafetería Central");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel background = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                Color color1 = new Color(245, 240, 220);
                Color color2 = new Color(252, 243, 229);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(210, 200, 180, 40));
                g2d.setStroke(new BasicStroke(2));
                for (int i = 0; i < getWidth(); i += 40) {
                    g2d.drawLine(i, 0, i, getHeight());
                }
                for (int i = 0; i < getHeight(); i += 40) {
                    g2d.drawLine(0, i, getWidth(), i);
                }
            }
        };
        setContentPane(background);

        crearMenuBar();
        crearBienvenidaPanel();
    }

    private void crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(111, 78, 55));
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(61, 43, 31)));

        JLabel logo = new JLabel("CAFETERÍA CENTRAL");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logo.setForeground(Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 30));
        menuBar.add(logo);

        JMenu menuMantenimiento = createMenu("Mantenimiento", null);
        JMenuItem itemInicio = createMenuItem("Inicio", null);
        itemInicio.addActionListener(e -> mostrarBienvenida());
        menuMantenimiento.add(itemInicio);
        menuMantenimiento.addSeparator();

        JMenuItem itemPedidos = createMenuItem("Pedidos", null);
        JMenuItem itemClientes = createMenuItem("Clientes", null);
        JMenuItem itemBaristas = createMenuItem("Baristas", null);
        JMenuItem itemHorarios = createMenuItem("Horarios", null);
        JMenuItem itemHistorial = createMenuItem("Historial", null);

        itemPedidos.addActionListener(e -> abrirPanel(new PedidosPanel(controller)));
        itemClientes.addActionListener(e -> abrirPanel(new ClientesPanel(controller)));
        itemBaristas.addActionListener(e -> abrirPanel(new BaristasPanel(controller)));
        itemHorarios.addActionListener(e -> abrirPanel(new HorariosPanel(controller)));
        itemHistorial.addActionListener(e -> abrirPanel(new HistorialPanel(controller)));

        menuMantenimiento.add(itemPedidos);
        menuMantenimiento.add(itemClientes);
        menuMantenimiento.add(itemBaristas);
        menuMantenimiento.add(itemHorarios);
        menuMantenimiento.add(itemHistorial);

        JMenu menuHerramientas = createMenu("Herramientas", null);
        JMenuItem itemReportes = createMenuItem("Reportes", null);
        JMenuItem itemConfig = createMenuItem("Configuración", null);
        menuHerramientas.add(itemReportes);
        menuHerramientas.add(itemConfig);

        JMenu menuAyuda = createMenu("Ayuda", null);
        JMenuItem itemAcercaDe = createMenuItem("Acerca de", null);
        itemAcercaDe.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "<html><div style='text-align:center;'>"
            + "<h2 style='color:#6F4E37;'>Sistema de Gestión de Cafetería</h2>"
            + "<p>Versión 2.0</p>"
            + "<p>Desarrollado por: Grupo 4 :D</p>"
            + "<p>Curso Algoritmos y Estructura de Datos</p>"
            + "<p>© 2025 - Todos los derechos reservados</p></div></html>",
            "Acerca de",
            JOptionPane.INFORMATION_MESSAGE));
        menuAyuda.add(itemAcercaDe);

        JMenu menuSalir = createMenu("Salir", null);
        JMenuItem itemCerrarSesion = createMenuItem("Cerrar Sesión", null);
        itemCerrarSesion.addActionListener(e -> cerrarSesion());
        menuSalir.add(itemCerrarSesion);

        menuBar.add(menuMantenimiento);
        menuBar.add(menuHerramientas);
        menuBar.add(menuAyuda);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menuSalir);

        setJMenuBar(menuBar);
    }

    private JMenu createMenu(String text, String iconPath) {
        JMenu menu = new JMenu(text);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        menu.setBackground(new Color(111, 78, 55));
        menu.setForeground(Color.WHITE);
        menu.setOpaque(true);
        menu.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return menu;
    }

    private JMenuItem createMenuItem(String text, String iconPath) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setBackground(new Color(252, 243, 229));
        item.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(224, 202, 162));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(new Color(252, 243, 229));
            }
        });
        return item;
    }

    private void crearBienvenidaPanel() {
        bienvenidaPanel = new JPanel(new BorderLayout());
        bienvenidaPanel.setOpaque(false);
        bienvenidaPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel bienvenidaLabel = new JLabel("BIENVENIDO AL SISTEMA DE GESTIÓN DE CAFETERÍA");
        bienvenidaLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        bienvenidaLabel.setForeground(new Color(111, 78, 55));
        bienvenidaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bienvenidaLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        JLabel subtituloLabel = new JLabel("Seleccione una opción del menú para comenzar");
        subtituloLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtituloLabel.setForeground(new Color(90, 70, 50));
        subtituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 5, 20, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        cardsPanel.setMaximumSize(new Dimension(1000, 150));

        String[] cardTitles = {"PEDIDOS", "CLIENTES", "BARISTAS", "HORARIOS", "HISTORIAL"};

        for (int i = 0; i < 5; i++) {
            final int index = i;
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(new Color(
                cardColors[i].getRed(),
                cardColors[i].getGreen(),
                cardColors[i].getBlue(),
                200
            ));
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cardColors[i].darker(), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    card.setBackground(new Color(
                        cardColors[index].getRed(),
                        cardColors[index].getGreen(),
                        cardColors[index].getBlue(),
                        230
                    ));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    card.setBackground(new Color(
                        cardColors[index].getRed(),
                        cardColors[index].getGreen(),
                        cardColors[index].getBlue(),
                        200
                    ));
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    switch (index) {
                        case 0: abrirPanel(new PedidosPanel(controller)); break;
                        case 1: abrirPanel(new ClientesPanel(controller)); break;
                        case 2: abrirPanel(new BaristasPanel(controller)); break;
                        case 3: abrirPanel(new HorariosPanel(controller)); break;
                        case 4: abrirPanel(new HistorialPanel(controller)); break;
                    }
                }
            });

            JLabel iconLabel = new JLabel();
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel titleLabel = new JLabel(cardTitles[i], SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            card.add(iconLabel, BorderLayout.CENTER);
            card.add(titleLabel, BorderLayout.SOUTH);

            cardsPanel.add(card);
        }

        centerPanel.add(logoLabel);
        centerPanel.add(bienvenidaLabel);
        centerPanel.add(subtituloLabel);
        centerPanel.add(cardsPanel);

        bienvenidaPanel.add(centerPanel, BorderLayout.CENTER);
        add(bienvenidaPanel, BorderLayout.CENTER);
    }

    private void abrirPanel(JPanel panel) {
        getContentPane().removeAll();
        add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void mostrarBienvenida() {
        getContentPane().removeAll();
        add(bienvenidaPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void cerrarSesion() {
        this.dispose();
        Login login = new Login(controller);
        login.setVisible(true);
    }
}
