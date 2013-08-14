package jpos.config.simple.editor;

///////////////////////////////////////////////////////////////////////////////
//
// This software is provided "AS IS".  The JavaPOS working group (including
// each of the Corporate members, contributors and individuals)  MAKES NO
// REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED 
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
// NON-INFRINGEMENT. The JavaPOS working group shall not be liable for
// any damages suffered as a result of using, modifying or distributing this
// software or its derivatives. Permission to use, copy, modify, and distribute
// the software and its documentation for any purpose is hereby granted. 
//
// The JavaPOS Config/Loader (aka JCL) is now under the CPL license, which 
// is an OSS Apache-like license.  The complete license is located at:
//    http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
//
///////////////////////////////////////////////////////////////////////////////

import javax.swing.*;    

/**
 * Simple entry point to start the PropertiesEditor application
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (Washington DC 2001 meeting)
 */
public class PropertiesEditor extends Object
{
    /** Default ctor (private) to avoid construction (no instance is ever needed) */
    private PropertiesEditor() {}

    /**
     * Main entry point of the editor application
     * @param args the command line arguments
     */
    public static void main( String[] args )
    {
        if( !defaultCloseOperationSet )
        {
            getFrame().setDefaultCloseOperation( JposEntryEditor.EXIT_ON_CLOSE );
            defaultCloseOperationSet = true;
        }

        getFrame().setVisible( true );
    }

    /**
     * Hides or shows the Frame
     * @param b the boolean param
     */
    public static void setFrameVisible( boolean b )
    { getFrame().setVisible( b ); }

    /**
     * Exposes the JFrame of the application setDefaultCloseOperation
     * this is needed to allow integration of the editor into other Java application
     * otherwise if the default is used then the frame will exit and exit the application
     * as well.  This method allows user to customize the closing of the editor.
     * @see jpos.config.simple.editor.JposEntryEditor#DO_NOTHING_ON_CLOSE
     * @see jpos.config.simple.editor.JposEntryEditor#HIDE_ON_CLOSE
     * @see jpos.config.simple.editor.JposEntryEditor#DISPOSE_ON_CLOSE
     * @see jpos.config.simple.editor.JposEntryEditor#EXIT_ON_CLOSE
     */
    public static void setDefaultFrameCloseOperation( int operation )
    {
        getFrame().setDefaultCloseOperation( operation );
        defaultCloseOperationSet = true;
    }

    /** @return the Frame and contructs it lazily if necessary */
    public static JFrame getFrame()
    {
        if( frame == null )
            frame = new PropertiesEditorFrame();

        return frame;
    }

    //-------------------------------------------------------------------------
    // Class variables
    //

    private static JFrame frame = null;
    private static boolean defaultCloseOperationSet = false;

    //-------------------------------------------------------------------------
    // Class constants
    //

    public static final int DO_NOTHING_ON_CLOSE = JFrame.DO_NOTHING_ON_CLOSE;
    public static final int HIDE_ON_CLOSE = JFrame.HIDE_ON_CLOSE;
    public static final int DISPOSE_ON_CLOSE = JFrame.DISPOSE_ON_CLOSE;
    public static final int EXIT_ON_CLOSE = 3;  //Note this constant is hard coded and not defined in Swing (see source code)
}
