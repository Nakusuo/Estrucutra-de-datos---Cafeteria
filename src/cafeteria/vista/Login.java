package cafeteria.vista;

import cafeteria.controlador.PrincipalController;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Login extends JFrame {
    private int intentosFallidos = 0;
    private final PrincipalController controller;

    public Login(PrincipalController controller) {
        this.controller = controller;
        setTitle("Acceso al Sistema - Cafetería");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 600, 450, 30, 30));
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 240, 230));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(logoLabel, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("CAFETERÍA CENTRAL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(160, 82, 45));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel loginLabel = new JLabel("INICIO DE SESIÓN", SwingConstants.CENTER);
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginLabel.setForeground(new Color(139, 69, 19));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(loginLabel, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createInputLabel("Usuario:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        JTextField txtUsuario = createInputField();
        txtUsuario.setPreferredSize(new Dimension(300, txtUsuario.getPreferredSize().height));
        formPanel.add(txtUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createInputLabel("Contraseña:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        JPasswordField txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 180, 140)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        txtContrasena.setPreferredSize(new Dimension(300, txtContrasena.getPreferredSize().height));
        formPanel.add(txtContrasena, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton btnLogin = createStyledButton("INICIAR SESIÓN", new Color(160, 82, 45), 220, 45);
        JButton btnSalir = createStyledButton("SALIR", new Color(165, 42, 42), 150, 40);

        btnLogin.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String contrasena = new String(txtContrasena.getPassword());

            if (validarCredenciales(usuario, contrasena)) {
                btnLogin.setBackground(new Color(139, 69, 19));
                btnLogin.setText("ACCESO CONCEDIDO");

                Timer timer = new Timer(1000, evt -> {
                    abrirVentanaPrincipal();
                    dispose();
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                intentosFallidos++;
                btnLogin.setBackground(new Color(165, 42, 42));
                btnLogin.setText("CREDENCIALES INVÁLIDAS");

                Timer timer = new Timer(1500, evt -> {
                    btnLogin.setBackground(new Color(160, 82, 45));
                    btnLogin.setText("INICIAR SESIÓN");

                    if (intentosFallidos >= 3) {
                        JOptionPane.showMessageDialog(this,
                            "<html><div style='text-align:center;'><h3 style='color:#8B0000;'>ACCESO BLOQUEADO</h3>"
                            + "<p>Demasiados intentos fallidos. Saliendo del sistema...</p></div></html>",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        System.exit(0);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

        btnSalir.addActionListener(e -> System.exit(0));

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnSalir);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color color1 = new Color(245, 240, 230);
                Color color2 = new Color(255, 248, 220);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                g2d.setColor(new Color(210, 180, 140, 40));
                g2d.setStroke(new BasicStroke(2));
                for (int i = 0; i < getWidth(); i += 40) {
                    g2d.drawLine(i, 0, i, getHeight());
                }
                for (int i = 0; i < getHeight(); i += 40) {
                    g2d.drawLine(0, i, getWidth(), i);
                }
            }
        };
        background.setLayout(new BorderLayout());
        background.add(mainPanel, BorderLayout.CENTER);

        add(background);
    }

    private JLabel createInputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(160, 82, 45));
        return label;
    }

    private JTextField createInputField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 180, 140)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, height));
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setIconTextGap(10);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private boolean validarCredenciales(String usuario, String contrasena) {
        return "grupo04".equals(usuario) && "grupo04".equals(contrasena);
    }

    private void abrirVentanaPrincipal() {
        PrincipalPanel principal = new PrincipalPanel(controller);
        principal.setVisible(true);
    }
}
