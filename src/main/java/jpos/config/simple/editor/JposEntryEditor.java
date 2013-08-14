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
 * Simple entry point to start the JposEntryEditor application
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @version 1.2 (NYC 2K meeting)
 * @since 0.1 (Philly 99 meeting)
 */
public class JposEntryEditor extends Object
{
    //-------------------------------------------------------------------------
    // Ctor
    //

    /**
     * Default ctor (private) to avoid construction (no instance is ever needed)
     * @since 0.1 (Philly 99 meeting)
     */
    private JposEntryEditor() {}

    //-------------------------------------------------------------------------
    // Private class methods
    //

	/**
	 * Makes sure that all required class files seem to be there.
	 * If not then shows a message otherwise all is OK
	 */
	private static boolean verifyPrerequisites()
	{
		boolean checkOk = false;

		try
		{
			checkJclClasses();
			checkXercesClasses();

			checkOk = true;
		}
		catch( Exception e )
		{
			JOptionPane.showMessageDialog( new JFrame(), JCL_PREREQUISITE_CLASSES_NOT_FOUND_MSG,
										   JCL_ERROR_TITLE_STRING, JOptionPane.ERROR_MESSAGE );
		}

		return checkOk;
	}

	/**
	 * Makes sure that JCL classes seems to be in CLASSPATH
	 * @exception java.lang.ClassNotFoundException if some classes where not found
	 */
	private static void checkJclClasses() throws ClassNotFoundException
	{
		//Try to load some JCL classes
		Class.forName( "jpos.config.JposEntry" );
		Class.forName( "jpos.config.simple.SimpleEntry" );
		Class.forName( "jpos.loader.JposServiceInstance" );
		Class.forName( "jpos.loader.simple.SimpleServiceConnection" );
	}

	/**
	 * Makes sure that Xerces classes seems to be in CLASSPATH
	 * @exception java.lang.ClassNotFoundException if some classes where not found
	 */
	private static void checkXercesClasses() throws ClassNotFoundException
	{
		//Try to load some Xerces classes
		Class.forName( "org.w3c.dom.Document" );
		Class.forName( "org.apache.xerces.parsers.DOMParser" );
	}

    //-------------------------------------------------------------------------
    // Public class methods
    //

    /**
     * Main entry point of the editor application
     * @param args the command line arguments
     * @since 0.1 (Philly 99 meeting)
     */
    public static void main( String[] args )
    {
		if( verifyPrerequisites() == false )
		{
			System.out.println( COULD_NOT_JCLEDITOR_MSG );
			System.exit( 0 );
		}

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
     * @version 1.2 (NYC 2K meeting)
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
     * @version 1.2 (NYC 2K meeting)
     */
    public static void setDefaultFrameCloseOperation( int operation )
    {
        getFrame().setDefaultCloseOperation( operation );
        defaultCloseOperationSet = true;
    }

    /**
     * @return the Frame and contructs it lazily if necessary
     * @version 1.2 (NYC 2K meeting)
     */
    public static JFrame getFrame()
    {
        if( frame == null )
            frame = new MainFrame();

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

	//<i18n>
	public static final String COULD_NOT_JCLEDITOR_MSG = "Could not run JCL Editor.  Please see install documentation and try again.";
	public static final String JCL_PREREQUISITE_CLASSES_NOT_FOUND_MSG = "The JCL prerequisite classes (controls and JCL and Apache.org Xerces XML parser) not found!";
	public static final String JCL_ERROR_TITLE_STRING = "JCL Editor Error Dialog";
	//</i18n>
}
