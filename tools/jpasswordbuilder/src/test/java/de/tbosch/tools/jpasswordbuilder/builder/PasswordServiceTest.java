/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tbosch.tools.jpasswordbuilder.builder;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author thomas.bosch
 */
public class PasswordServiceTest {

    @Test
    public void testGetPasswort() {
        Assert.assertEquals("982afc80b932f8d", PasswordService.getPasswort("google.com"));
    }
}
