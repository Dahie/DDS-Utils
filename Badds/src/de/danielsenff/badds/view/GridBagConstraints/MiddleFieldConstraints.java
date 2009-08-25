package de.danielsenff.badds.view.GridBagConstraints;


/*
 * ### Not yet documented. ###
 * @author Jake Miles
 */

public class MiddleFieldConstraints extends FieldConstraints {

    /**
     * gridwidth = 1, gridheight = 1, fill = HORIZONTAL, weightx = weighty = 0.0.
     * Useful for placing a text field in the middle of a row.
     */
    public MiddleFieldConstraints () {
        super (1, 1, HORIZONTAL, 0.0, 0.0);
    }

    /**
     * gridwidth = 1, fill = BOTH, weightx = weighty = 0.0.
     * Useful for placing a two-dimensional field in the middle of a row.
     */
    public MiddleFieldConstraints (int gridheight) {
        super (1, gridheight, BOTH, 0.0, 0.0);
    }

    /**
     * gridwidth = 1, gridheight = 1, fill = HORIZONTAL, weighty = 0.0.
     * Useful for placing a weighted text field in the middle of a row.
     */
    public MiddleFieldConstraints (double weightx) {
        super (1, 1, HORIZONTAL, weightx, 0.0);
    }

    /**
     * gridwidth = 1, fill = BOTH.
     * Useful for placing a weighted two-dimensional field in the middle of a row.
     */
    public MiddleFieldConstraints (int gridheight, double weightx, double weighty) {
        super (1, gridheight, BOTH, weightx, weighty);
    }

}
