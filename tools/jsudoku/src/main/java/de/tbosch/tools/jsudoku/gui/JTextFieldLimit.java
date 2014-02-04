/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tbosch.tools.jsudoku.gui;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author Thomas Bosch
 */
public class JTextFieldLimit extends JTextField {

    private int limit;
    private final int i;
    private final int j;

    public JTextFieldLimit(int limit, int i, int j) {
        this.limit = limit;
        this.i = i;
        this.j = j;
    }

    @Override
    protected Document createDefaultModel() {
        return new LimitDocument();
    }

    private class LimitDocument extends PlainDocument {

        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null) {
                return;
            }

            if ((getLength() + str.length()) <= limit && NumberUtils.isNumber(str)) {
                super.insertString(offset, str, attr);
            }
        }
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}
