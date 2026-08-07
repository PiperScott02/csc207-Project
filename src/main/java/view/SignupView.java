package view;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.BorderFactory;
import javax.swing.Box;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

/**
 * The View for the Signup Use Case.
 */
public class SignupView extends JPanel implements PropertyChangeListener {
    private final String viewName = "sign up";

    private final SignupViewModel signupViewModel;
    private final JTextField usernameInputField = new JTextField(15);
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(15);
    private final SignupController signupController;

    private final JButton signUp;
    private final JButton toLogin;

    public SignupView(SignupController controller, SignupViewModel signupViewModel) {

        this.signupController = controller;
        this.signupViewModel = signupViewModel;
        signupViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("PortfolioPilot");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel subtitle = new JLabel("Create Your Account");
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel usernameLabel =
                new JLabel(SignupViewModel.USERNAME_LABEL);
        usernameLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 16)
        );

        final JLabel passwordLabel =
                new JLabel(SignupViewModel.PASSWORD_LABEL);
        passwordLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 16)
        );

        final JLabel repeatPasswordLabel =
                new JLabel(SignupViewModel.REPEAT_PASSWORD_LABEL);
        repeatPasswordLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 16)
        );

        usernameInputField.setPreferredSize(
                new Dimension(320, 35)
        );
        usernameInputField.setFont(
                new Font("SansSerif", Font.PLAIN, 16)
        );

        passwordInputField.setPreferredSize(
                new Dimension(320, 35)
        );
        passwordInputField.setFont(
                new Font("SansSerif", Font.PLAIN, 16)
        );

        repeatPasswordInputField.setPreferredSize(
                new Dimension(320, 35)
        );
        repeatPasswordInputField.setFont(
                new Font("SansSerif", Font.PLAIN, 16)
        );

        final LabelTextPanel usernameInfo =
                new LabelTextPanel(
                        usernameLabel,
                        usernameInputField
                );

        final LabelTextPanel passwordInfo =
                new LabelTextPanel(
                        passwordLabel,
                        passwordInputField
                );

        final LabelTextPanel repeatPasswordInfo =
                new LabelTextPanel(
                        repeatPasswordLabel,
                        repeatPasswordInputField
                );

        final JPanel buttons = new JPanel();

        toLogin = new JButton(
                SignupViewModel.TO_LOGIN_BUTTON_LABEL
        );
        toLogin.setFont(
                new Font("SansSerif", Font.PLAIN, 15)
        );
        toLogin.setPreferredSize(
                new Dimension(120, 38)
        );

        signUp = new JButton(
                SignupViewModel.SIGNUP_BUTTON_LABEL
        );
        signUp.setFont(
                new Font("SansSerif", Font.BOLD, 15)
        );
        signUp.setPreferredSize(
                new Dimension(120, 38)
        );

        buttons.add(signUp);
        buttons.add(toLogin);

        signUp.addActionListener(
                // This creates an anonymous subclass of ActionListener and instantiates it.
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(signUp)) {
                            final SignupState currentState = signupViewModel.getState();

                            signupController.execute(
                                    currentState.getUsername(),
                                    currentState.getPassword(),
                                    currentState.getRepeatPassword()
                            );
                        }
                    }
                }
        );

        toLogin.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        signupController.switchToLoginView();
                    }
                }
        );

        addUsernameListener();
        addPasswordListener();
        addRepeatPasswordListener();

        final JPanel signupPanel = new JPanel();
        signupPanel.setLayout(
                new BoxLayout(signupPanel, BoxLayout.Y_AXIS)
        );

        signupPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Sign Up"),
                        BorderFactory.createEmptyBorder(
                                25, 40, 25, 40
                        )
                )
        );

        signupPanel.setPreferredSize(
                new Dimension(520, 430)
        );

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        repeatPasswordInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttons.setAlignmentX(Component.CENTER_ALIGNMENT);

        signupPanel.add(title);
        signupPanel.add(Box.createVerticalStrut(8));

        signupPanel.add(subtitle);
        signupPanel.add(Box.createVerticalStrut(25));

        signupPanel.add(usernameInfo);
        signupPanel.add(Box.createVerticalStrut(15));

        signupPanel.add(passwordInfo);
        signupPanel.add(Box.createVerticalStrut(15));

        signupPanel.add(repeatPasswordInfo);
        signupPanel.add(Box.createVerticalStrut(25));

        signupPanel.add(buttons);

        final JPanel outerPanel =
                new JPanel(new GridBagLayout());

        outerPanel.add(signupPanel);

        this.setLayout(new BorderLayout());
        this.add(outerPanel, BorderLayout.CENTER);
    }

    private void addUsernameListener() {
        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                signupViewModel.setState(currentState);
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
    }

    private void addPasswordListener() {
        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                signupViewModel.setState(currentState);
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
    }

    private void addRepeatPasswordListener() {
        repeatPasswordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setRepeatPassword(new String(repeatPasswordInputField.getPassword()));
                signupViewModel.setState(currentState);
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
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final SignupState state = (SignupState) evt.getNewValue();
        if (state.getUsernameError() != null) {
            JOptionPane.showMessageDialog(this, state.getUsernameError());
        }
    }

    public String getViewName() {
        return viewName;
    }
}
