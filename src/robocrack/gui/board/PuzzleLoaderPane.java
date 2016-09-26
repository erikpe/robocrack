package robocrack.gui.board;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import robocrack.engine.board.BoardModel;
import robocrack.engine.program.ProgramModel;
import robocrack.io.Loader;

@SuppressWarnings("serial")
public class PuzzleLoaderPane extends JComponent implements ActionListener
{
    private final static int SPACING = 3;

    private final ProgramModel programModel;
    private final BoardModel boardModel;

    private final JLabel label;
    private final JTextField textField;
    private final JButton loadButton;

    public PuzzleLoaderPane(final ProgramModel programModel,
            final BoardModel boardModel)
    {
        this.programModel = programModel;
        this.boardModel = boardModel;

        this.label = new JLabel("Load from web, enter puzzle id: ");
        this.textField = new JTextField("363", 10);
        this.loadButton = new JButton("Load");

        initialize();
    }

    private void initialize()
    {
        int height = 0;

        height = Math.max(height, label.getPreferredSize().height);
        height = Math.max(height, textField.getPreferredSize().height);
        height = Math.max(height, loadButton.getPreferredSize().height);

        int xBounds = 0;

        xBounds = addComponent(xBounds, height, label);
        xBounds = addComponent(xBounds, height, textField);
        xBounds = addComponent(xBounds, height, loadButton);

        setPreferredSize(new Dimension(xBounds, height));

        textField.addActionListener(this);
        loadButton.addActionListener(this);
    }

    private int addComponent(final int xBounds, final int height,
            final JComponent comp)
    {
        add(comp);
        final Dimension compDim = comp.getPreferredSize();
        comp.setBounds(xBounds + SPACING, (height - compDim.height) / 2,
                compDim.width, compDim.height);
        return xBounds + SPACING+ compDim.width;
    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (e.getSource() != loadButton && e.getSource() != textField)
        {
            return;
        }

        final int id = Integer.valueOf(textField.getText().trim());
        Loader.loadFromWeb(id, boardModel, programModel);
    }
}
