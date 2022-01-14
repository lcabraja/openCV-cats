/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.jndi;

import java.util.Hashtable;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

/**
 *
 * @author daniel.bele
 */
public class InitialDirContextCloseable extends InitialDirContext implements AutoCloseable{

    public InitialDirContextCloseable(Hashtable<?, ?> environment) throws NamingException {
        super(environment);
    }
    
    // InitialContext has a method close() that throws NamingException (subclass of Exception) so compiler
    // took it for granted as close() throws Exception from AutoCloseable
    // We couldn't have done it with Closeable because it's close() method throws IOException, and NamingException is not it's subclass
    // try with resources expects AutoCloseable (Closeable is it's IO version as subclass)
    // LISKOV:
    //    - superclass method declares an exception, subclass overridden method:
    //    - can declare same, subclass exception or no exception
    //    - cannot declare parent exception
    // since close in AutoCloseable throws Exception, close of InitialContext throws subtype -> NamingException -> it can be done
    //    @Override
    //    public void close() throws NamingException {
    //        //System.out.println("Miroslav Klose");
    //        super.close();
    //    }
}
