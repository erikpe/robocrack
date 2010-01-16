package robocrack.gui.program;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import robocrack.engine.program.ProgramModel;
import robocrack.engine.program.ProgramModel.OpCode;

@SuppressWarnings("serial")
public class ProgramSettings extends JComponent implements ActionListener, Observer
{
    private static final int SPACING = 0;
    
    private final ProgramModel programModel;
    
    private final JCheckBox paintRedBox;
    private final JCheckBox paintGreenBox;
    private final JCheckBox paintBlueBox;
    
    private int preferredWidth = 0;
    
    public ProgramSettings(final ProgramModel programModel)
    {
        this.programModel = programModel;
        
        this.paintRedBox = new JCheckBox("Allow paint red");
        this.paintGreenBox = new JCheckBox("Allow paint green");
        this.paintBlueBox = new JCheckBox("Allow paint blue");
        
        initialize();
    }
    
    private void initialize()
    {
        int yBounds = addBox(paintRedBox, 0);
        yBounds = addBox(paintGreenBox, yBounds + SPACING);
        yBounds = addBox(paintBlueBox, yBounds + SPACING);
        
        setPreferredSize(new Dimension(preferredWidth, yBounds));
        
        programModel.addObserver(this);
        
        update();
    }
    
    private int addBox(final JCheckBox box, final int yBounds)
    {
        box.addActionListener(this);
        add(box);
        
        final int bWidth = box.getPreferredSize().width;
        final int bHeight = box.getPreferredSize().height;
        
        box.setBounds(0, yBounds, bWidth, bHeight);
        
        preferredWidth = Math.max(preferredWidth, bWidth);
        
        return yBounds + bHeight;
    }
    
    private void update()
    {
        paintRedBox.setSelected(programModel.isAllowed(OpCode.PAINT_RED));
        paintGreenBox.setSelected(programModel.isAllowed(OpCode.PAINT_GREEN));
        paintBlueBox.setSelected(programModel.isAllowed(OpCode.PAINT_BLUE));
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        if (observable instanceof ProgramModel && arg == null)
        {
            update();
        }
    }
    
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (!(e.getSource() instanceof JCheckBox))
        {
            return;
        }
        
        final JCheckBox box = (JCheckBox) e.getSource();
        
        if (box == paintRedBox)
        {
            programModel.setAllowed(OpCode.PAINT_RED, box.isSelected());
        }
        else if (box == paintGreenBox)
        {
            programModel.setAllowed(OpCode.PAINT_GREEN, box.isSelected());
        }
        else if (box == paintBlueBox)
        {
            programModel.setAllowed(OpCode.PAINT_BLUE, box.isSelected());
        }
    }
}
