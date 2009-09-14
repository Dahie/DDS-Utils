/**
 * 
 */
package de.danielsenff.radds.util;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ListSelectionModel;

/**
 * @author danielsenff
 *
 */
public class SelectionHelper {

	/**
	 * Returns an {@link Collection} with all indecies that are selected in the inputted 
	 * {@link ListSelectionModel}.
	 * @param lsm ListSelectionModel 
	 * @return Collection with selected indecies
	 */
	public static Collection<Integer> getSelectedIndecies(final ListSelectionModel lsm) {

        Collection<Integer> selectedIndecies = new ArrayList<Integer>();
        
        if (!lsm.isSelectionEmpty()) {
        	
            // Find out which indexes are selected.
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
        
            /*System.out.println("Event for indexes "
            + firstIndex + " - " + lastIndex
            + "; selected indexes:");*/
            
            for (int i = minIndex; i <= maxIndex; i++) {
                if (lsm.isSelectedIndex(i)) {
                    selectedIndecies.add(i);
                }
            }
        }
		return selectedIndecies;
	}
	
}
