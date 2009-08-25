package de.danielsenff.radds.view.GridBagConstraints;


/*
 * ### Not yet documented. ###
 * @author Jake Miles
 */

public class RemainderConstraintsNoFill extends FieldConstraints {

    /**
     * gridwidth = REMAINDER, gridheight = 1, fill = NONE, weightx = weighty = 0.0.
     * Useful for placing a combo box or check box at the end of a row.
     */
    public RemainderConstraintsNoFill () {
        super (REMAINDER, 1, NONE, 0.0, 0.0);
    }

}
