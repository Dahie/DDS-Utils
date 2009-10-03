package de.danielsenff.dropps.util;
import javax.swing.JTextArea;

public class TextAreaLabel extends JTextArea
{
	public TextAreaLabel(String text)
	{
		super(text);
		setOpaque(false);
		setBorder(null);
		setFocusable(false);
	}
}