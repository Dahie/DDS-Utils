package de.danielsenff.badds.view.GridBagConstraints;

import java.awt.Insets;

/*
 * ### Not yet documented. ###
 * @author Jake Miles
 */

public class FieldConstraints extends GBConstraints {

    public FieldConstraints (int gridwidth, int gridheight, int fill, double weightx, double weighty) {
        super (gridwidth, gridheight, fill, NORTHWEST, weightx, weighty, new Insets (5, 2, 5, 5));
    }

}
