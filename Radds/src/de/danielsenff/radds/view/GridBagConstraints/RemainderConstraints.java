package de.danielsenff.radds.view.GridBagConstraints;


/*
 * ### Not yet documented. ###
 * @author Jake Miles
 */

public class RemainderConstraints extends FieldConstraints {

    /**
     * gridwidth = REMAINDER, gridheight = 1, fill = HORIZONTAL, weightx = weighty = 0.0.
     * Useful for placing a text field at the end of a row.
     */
    public RemainderConstraints () {
        super (REMAINDER, 1, HORIZONTAL, 0.0, 0.0);
    }

    /**
     * gridwidth = REMAINDER, fill = BOTH, weightx = weighty = 0.0.
     * Useful for placing a two-dimensional field at the end of a row.
     */
    public RemainderConstraints (int gridheight) {
        super (REMAINDER, gridheight, BOTH, 0.0, 0.0);
    }

    /**
     * gridwidth = REMAINDER, gridheight = 1, fill = HORIZONTAL, weighty = 0.0.
     * Useful for placing a weighted text field at the end of a row.
     */
    public RemainderConstraints (double weightx) {
        super (REMAINDER, 1, HORIZONTAL, weightx, 0.0);
    }

    /**
     * gridwidth = REMAINDER, fill = BOTH.
     * Useful for placing a weighted two-dimensional field at the end of a row.
     */
    public RemainderConstraints (int gridheight, double weightx, double weighty) {
        super (REMAINDER, gridheight, BOTH, weightx, weighty);
    }

}
