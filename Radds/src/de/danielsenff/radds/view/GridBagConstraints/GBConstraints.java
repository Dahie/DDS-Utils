package de.danielsenff.radds.view.GridBagConstraints;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/*
 * ### Not yet documented. ###
 * @author Jake Miles
 */

public class GBConstraints extends GridBagConstraints {

    /**
     * @param gridx
     * @param gridy
     * @param gridwidth
     * @param gridheight
     * @param fill
     * @param anchor
     * @param weightx
     * @param weighty
     * @param insets
     */
    public GBConstraints (final int gridx, final int gridy, final int gridwidth, final int gridheight, final int fill, final int anchor, final double weightx, final double weighty, final Insets insets) {
        super();
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;
        this.fill = fill;
        this.anchor = anchor;
        this.weightx = weightx;
        this.weighty = weighty;
        this.insets = insets;
    }

    public GBConstraints (int gridwidth, int gridheight, int fill, int anchor, double weightx, double weighty, Insets insets) {
        this (RELATIVE, RELATIVE, gridwidth, gridheight, fill, anchor, weightx, weighty, insets);
    }

    public GBConstraints (int gridwidth, int gridheight, int fill, int anchor, Insets insets) {
        this (gridwidth, gridheight, fill, anchor, 0.0, 0.0, insets);
    }

    public GBConstraints (int gridwidth, int gridheight, int fill, int anchor, double weightx, double weighty) {
        this (gridwidth, gridheight, fill, anchor, weightx, weighty, new Insets (5, 5, 5, 5));
    }

    public GBConstraints (int gridwidth, int gridheight, int fill, int anchor) {
        this (gridwidth, gridheight, fill, anchor, 0.0, 0.0);
    }

    // getters and setters for the otherwise public constraints fields

    public int getGridX () {
        return gridx;
    }

    public void setGridX (int gridx) {
        this.gridx = gridx;
    }

    public int getGridY () {
        return gridy;
    }

    public void setGridY (int gridy) {
        this.gridy = gridy;
    }

    public int getGridwidth () {
        return gridwidth;
    }

    public void setGridwidth (int gridwidth) {
        this.gridwidth = gridwidth;
    }

    public int getGridHeight () {
        return gridheight;
    }

    public void setGridHeight (int gridheight) {
        this.gridheight = gridheight;
    }

    public int getFill () {
        return fill;
    }

    public void setFill (int fill) {
        this.fill = fill;
    }

    public int getAnchor () {
        return anchor;
    }

    public void setAnchor (int anchor) {
        this.anchor = anchor;
    }

    public double getWeightX () {
        return weightx;
    }

    public void setWeightX (double weightx) {
        this.weightx = weightx;
    }

    public double getWeightY () {
        return weighty;
    }

    public void setWeightY (double weighty) {
        this.weighty = weighty;
    }

    public Insets getInsets () {
        return insets;
    }

    public void setInsets (Insets insets) {
        this.insets = insets;
    }

}
