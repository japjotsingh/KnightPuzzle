/**
 * Created by lawzoom on 3/23/17.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
/* One of the tricky things you have to figure out is how to have
 * the controls in the control panel talk to the KnightsTourPanel.
 * I know you'll figure out a way.  DON'T USE STATIC METHODS!!!!!
 */

public class KnightsTourControlPanel extends JPanel {

    public KnightsTourControlPanel(int w, int h) {
        this.setPreferredSize(new Dimension(w, h));
        this.setBackground(Color.orange);
    }

    /* Add all the buttons and sliders used to control this Knight's tour.
     * It is best if you allow the placement of the components to be handled
     * by a layout manager.  You can find out lots about layouts if you google
     * it!  You can also bind key events to the buttons and sliders, as well
     */
    private void setUpButtonsAndSliders() {
//
    }
}

