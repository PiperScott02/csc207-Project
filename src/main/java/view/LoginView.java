package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

/**
 * The View for when the user is logging into the program.
 */
public class LoginView extends JPanel implements PropertyChangeListener {

    private final String viewName = "log in";
    private final LoginViewModel loginViewModel;

    private final JTextField usernameInputField = new JTextField(15);
    private final JLabel usernameErrorField = new JLabel();

    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JLabel passwordErrorField = new JLabel();

    private final JButton logIn;
    private final JButton cancel;
    private final LoginController loginController;

    public LoginView(LoginViewModel loginViewModel, LoginController controller) {

        this.loginController = controller;
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("PortfolioPilot");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel subtitle = new JLabel("Login to Your Account");
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        final JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        usernameInputField.setPreferredSize(new Dimension(320, 35));
        usernameInputField.setFont(new Font("SansSerif", Font.PLAIN, 16));

        passwordInputField.setPreferredSize(new Dimension(320, 35));
        passwordInputField.setFont(new Font("SansSerif", Font.PLAIN, 16));

        final LabelTextPanel usernameInfo =
                new LabelTextPanel(usernameLabel, usernameInputField);

        final LabelTextPanel passwordInfo =
                new LabelTextPanel(passwordLabel, passwordInputField);

        final JPanel buttons = new JPanel();

        logIn = new JButton("Log In");
        logIn.setFont(new Font("SansSerif", Font.BOLD, 15));
        logIn.setPreferredSize(new Dimension(120, 38));

        cancel = new JButton("Cancel");
        cancel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        cancel.setPreferredSize(new Dimension(120, 38));

        buttons.add(logIn);
        buttons.add(cancel);

        logIn.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(logIn)) {
                            final LoginState currentState = loginViewModel.getState();

                            loginController.execute(
                                    currentState.getUsername(),
                                    currentState.getPassword()
                            );
                        }
                    }
                }
        );

        cancel.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        loginController.switchToSignupView();
                    }
                }
        );

        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        final JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        loginPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Login"),
                        BorderFactory.createEmptyBorder(25, 40, 25, 40)
                )
        );

        loginPanel.setPreferredSize(new Dimension(500, 370));

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttons.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginPanel.add(title);
        loginPanel.add(Box.createVerticalStrut(8));

        loginPanel.add(subtitle);
        loginPanel.add(Box.createVerticalStrut(25));

        loginPanel.add(usernameInfo);
        loginPanel.add(Box.createVerticalStrut(5));
        loginPanel.add(usernameErrorField);

        loginPanel.add(Box.createVerticalStrut(15));

        loginPanel.add(passwordInfo);
        loginPanel.add(Box.createVerticalStrut(5));
        loginPanel.add(passwordErrorField);

        loginPanel.add(Box.createVerticalStrut(25));

        loginPanel.add(buttons);

        final JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.add(loginPanel);

        this.setLayout(new BorderLayout());
        this.add(outerPanel, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LoginState state = (LoginState) evt.getNewValue();
        setFields(state);
        usernameErrorField.setText(state.getLoginError());
    }

    private void setFields(LoginState state) {
        usernameInputField.setText(state.getUsername());
    }

    public String getViewName() {
        return viewName;
    }
}
