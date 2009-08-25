package de.danielsenff.badds.view.GridBagConstraints;


/*
 * ### Not yet documented. ###
 * @author Jake Miles
 */

public class MiddleFieldConstraintsNoFill extends FieldConstraints {

    /**
     * gridwidth = 1, gridheight = 1, fill = NONE, weightx = weighty = 0.0.
     * Useful for placing a combo box or check box in the middle of a row.
     */
    public MiddleFieldConstraintsNoFill () {
        super (1, 1, NONE, 0.0, 0.0);
    }

}
