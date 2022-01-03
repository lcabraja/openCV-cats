/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import com.sun.jndi.fscontext.RefFSContext;
import com.sun.jndi.fscontext.RefFSContextFactory;
import hr.algebra.jndi.InitialDirContextCloseable;
import hr.algebra.utils.FileUtils.Extensions;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 *
 * @author lcabraja
 */
public class JNDIUtils {
    
    private static final String INITIAL_CONTEXT_FACTORY = RefFSContextFactory.class.getName();
    private static final String PROVIDER_URL = "file:c:/";
    
    private JNDIUtils() {
    }
    
    private static Hashtable<?, ?> configureEnvironment() {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, PROVIDER_URL);
        return env;
    }
    
    public static List<File> listDirectoryContents(File directory, Extensions extension) {
      return listDirectoryContents(directory.getAbsolutePath(), extension);
    }
    
    public static List<File> listDirectoryContents(String location, Extensions extension) {
        System.out.println("listDirectoryContents @ " + JNDIUtils.class);
        try (InitialDirContextCloseable context = new InitialDirContextCloseable(configureEnvironment())) {
            return listFiles(context, location, extension);
        } catch (NamingException ex) {
            Logger.getLogger(JNDIUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static List<File> listFiles(InitialDirContextCloseable context, String location, Extensions extension) throws NamingException {
        List<File> files = new ArrayList<>();
        
        NamingEnumeration<Binding> listBindings = context.listBindings(location);
        while (listBindings.hasMore()) {
            Binding binding = listBindings.next();
            if (binding.getClassName().equals(File.class.getName())) {
                File newFile = (File) binding.getObject();
                if (FileUtils.extensionOf(newFile, extension)) {
                    files.add(newFile);
                }
            }
        }
        return files;
    }
}
