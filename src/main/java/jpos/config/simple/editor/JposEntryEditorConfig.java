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

import java.awt.*;

import javax.swing.JOptionPane;

import java.io.*;

import jpos.util.tracing.Tracer;
import jpos.util.tracing.TracerFactory;

/**
 * This class contains all the JposEntryEditor configuration.  
 * It is serializable in order to persist its object for latter retrieval.
 * It is also a Singleton class since only one configuration exist at a time.
 * @since 1.3 (Tokyo 2000 meeting)
 * @author Kriselie del Mar Rivera
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @version 1.2 (NYC 2K meeting)
 */
class JposEntryEditorConfig implements Serializable
{
    //--------------------------------------------------------------------------
    // Class methods 
    //

    /**
     * @return the single instance of Singleton class JposEntryEditorConfig
     * This class method is synchronized requiring exclusive lock to class 
     * to access instance
     */
    public synchronized static JposEntryEditorConfig getInstance()
    {
        if( instance == null )
        {
            try
            {
                File file = new File( JCL_EDITOR_CONFIG_FILE_PATH + 
                                      File.separator + 
                                      JPOSENTRYEDITORCONFIG_FILE_NAME );

                FileInputStream inFile = new FileInputStream( file );
                ObjectInputStream ois = new ObjectInputStream( inFile );
   
                instance = (JposEntryEditorConfig)ois.readObject();

                ois.close();
            }
            catch( Exception e ) { instance = new JposEntryEditorConfig(); }
        }

        return instance;
    }

    //--------------------------------------------------------------------------
    // Protected/private methods 
    //
    
    /** Creates (if necessary) the JCL_EDITOR_CONFIG_FILE_PATH path */
    private void createDir()
    {
    	File jclEditorConfigPath = new File( JCL_EDITOR_CONFIG_FILE_PATH );
    	if( jclEditorConfigPath.exists() == false )
    	{
    		boolean b =	jclEditorConfigPath.mkdir();

    		String message = ( b ? "Created directory: " + 
    		                       JCL_EDITOR_CONFIG_FILE_PATH :
    							   "Could not create directory: " + 
    							   JCL_EDITOR_CONFIG_FILE_PATH );	

    		tracer.println( message );
    	}
    }

    //--------------------------------------------------------------------------
    // Public methods 
    //
   
    /** @return true if this config was recently saved */
    public boolean isSaved() { return configSaved; }

    /**
     * This method serializes the single JposEntryEditor instance into a file named by the constant
     * JPOSENTRYEDITORCONFIG_FILE_NAME.  Any previous content of file is lost.
     */
    public void save()
    {
        try
        {
        	createDir();
            configSaved = false; 

            FileOutputStream outFile = 
            new FileOutputStream( JCL_EDITOR_CONFIG_FILE_PATH + File.separator + 
                                  JPOSENTRYEDITORCONFIG_FILE_NAME );

            ObjectOutputStream oos = new ObjectOutputStream( outFile );

            oos.writeObject( this );
            oos.flush();

            oos.close();

            configSaved = true; 
        }
        catch( Exception e ) 
        {
        	tracer.println( "save():Exception.message=" + e.getMessage() );
        	
            configSaved = true;  

            JOptionPane.
            showMessageDialog( null, ERRORSAVINGFILE_STRING, 
                               JPOSENTRYEDITOR_STRING, 
                               JOptionPane.ERROR_MESSAGE );
        }
    }

	/** This methods sets all configuration properties to their default values */
	public void reset()
	{
		mainFrameHDividerLocation = DEFAULT_MAINFRAME_HSPLIT_DIVIDER_LOCATION;
		mainFrameVDividerLocation = DEFAULT_MAINFRAME_VSPLIT_DIVIDER_LOCATION;
		mainFrameLocation = DEFAULT_MAINFRAME_LOCATION;
		mainFrameSize = DEFAULT_MAINFRAME_SIZE;
		jposEntryMergerDialogLocation = DEFAULT_MERGER_DIALOG_LOCATION;
		jposEntryMergerDialogSize = DEFAULT_MERGER_DIALOG_SIZE;
		currentLookAndFeel = JAVALNF;
		currentTreeView = CATEGORYVIEW;
		expandTreeCheckBox = true;
		showNumbersAsHexCheckBox = true;
		autoLoadCheckBox = true;
		autoDeleteEntryOnCopy = true;

		configSaved = false;
		showJCL12PropView = false;

		fileDialogPath = DEFAULT_FILE_DIALOG_PATH;
	}

    //--------------------------------------------------------------------------
    // Public getter methods
    //

    /** @return the mainFrameLocation instance variable */
    public Point getMainFrameLocation() { return mainFrameLocation; }
   
    /** @return the mainFrameSize instance variable */
    public Dimension getMainFrameSize() { return mainFrameSize; }

    /** @return the jposEntryMergerDialogLocation instance variable */
    public Point getJposEntryMergerDialogLocation() 
    { return jposEntryMergerDialogLocation; }
   
    /** @return the jposEntryMergerDialogLocation instance variable */
    public Dimension getJposEntryMergerDialogSize() 
    { return jposEntryMergerDialogSize; }

    /** @return the mainFrameHDividerLocation (Horiontal split pane) */    
    public int getMainFrameHDividerLocation() 
    { return mainFrameHDividerLocation; }

    /** @return the mainFrameVDividerLocation (Vertical slpit pane) */    
    public int getMainFrameVDividerLocation() 
    { return mainFrameVDividerLocation; }

    /** @return the currentLookAndFeel  */
    public int getCurrentLookAndFeel() { return currentLookAndFeel; }

    /** @return the currentTreeView  */
    public int getCurrentTreeView() { return currentTreeView; }

    /** @return the expandTreeCheckBox flag  */
    public boolean getExpandTreeCheckBox() 
    { return expandTreeCheckBox; }

    /** @return the showNumbersAsHexCheckBox flag  */
    public boolean getShowNumbersAsHexCheckBox() 
    { return showNumbersAsHexCheckBox; }

    /** @return the autoLoadCheckBox flag  */
    public boolean getAutoLoadCheckBox() 
    { return autoLoadCheckBox; }

    /** 
	 * @return the autoDeleteEntryOnCopy flag
	 * @since 2.0.0  
	 */
    public boolean getAutoDeleteEntryOnCopy() 
    { return autoDeleteEntryOnCopy; }
    
	/** @return true if editor should display tab for JCL 1.2 view */
	public boolean isShowJCL12PropView() 
	{ return showJCL12PropView; }

	/** @return the path that all file dialog will used */
	public String getFileDialogPath() { return fileDialogPath; }

    //--------------------------------------------------------------------------
    // Public setter methods 
    //

    /**
     * Setter method for the logFileViewerFrameSize
     * @param p the Point parameter
     */
    public void setMainFrameLocation( Point p ) { mainFrameLocation = p; }
    
    /**
     * Setter method for the mainFrameSize
     * @param d the Dimension parameter
     */
    public void setMainFrameSize( Dimension d ) { mainFrameSize = d; }

    /**
	 * Sets the jposEntryMergerDialogLocation instance variable 
	 * @param p the Point
	 * @since 1.3 (SF 2K meeting)
	 */
    public void setJposEntryMergerDialogLocation( Point p ) 
    { jposEntryMergerDialogLocation = p; }
   
    /**
	 * Sets the jposEntryMergerDialogLocation instance variable 
	 * @param d the Dimension
	 * @since 1.3 (SF 2K meeting)
	 */
    public void setJposEntryMergerDialogSize( Dimension d ) 
    { jposEntryMergerDialogSize = d; }

    /**
     * Setter method for the mainFrameHDividerLocation (Horizontal split pane)
     * @param i the int parameter
     */
    public void setMainFrameHDividerLocation( int i ) 
    { mainFrameHDividerLocation = i; }

    /**
     * Setter method for the mainFrameVDividerLocation (Vertical split pane)
     * @param i the int parameter
     */
    public void setMainFrameVDividerLocation( int i ) 
    { mainFrameVDividerLocation = i; }

    /**
     * Setter method for the currentLookAndFeel
     * @param i the int parameter
     */
    public void setCurrentLookAndFeel( int i ) 
    { currentLookAndFeel = i; }

    /**
     * Setter method for the currentTreeView
     * @param i the int parameter
     */
    public void setCurrentTreeView( int i ) 
    { currentTreeView = i; }

    /**
     * Setter method for the expandTree check box
     * @param b the boolean parameter
     */
    public void setExpandTreeCheckBox( boolean b ) 
    { expandTreeCheckBox = b; }

    /**
     * Setter method for the showNubersAsHex check box
     * @param b the boolean parameter
     */
    public void setShowNumbersAsHexCheckBox( boolean b ) 
    { showNumbersAsHexCheckBox = b; }
    
    /**
     * Setter method for the autoLoad check box
     * @param b the boolean parameter
     */
    public void setAutoLoadCheckBox( boolean b ) 
    { autoLoadCheckBox = b; }

    /**
     * Setter method for the autoDeleteEntryOnExit config
     * @param b the boolean parameter
	 * @since 2.0.0
     */
    public void setAutoDeleteEntryOnCopy( boolean b ) 
    { autoDeleteEntryOnCopy = b; }
       
	/**
	 * Set if editor should display tab for JCL 1.2 view
	 * @param b the boolean parameter
	 */
	public void setShowJCL12PropView( boolean b ) 
	{ showJCL12PropView = b; }

	/**
	 * Sets the all file dialog path
	 * @param path the File whose path will be used
	 */
	public void setFileDialogPath( String path ) 
	{ fileDialogPath = path; }

    //--------------------------------------------------------------------------
    // Instance variables
    //
   
    private int mainFrameHDividerLocation = 
                 DEFAULT_MAINFRAME_HSPLIT_DIVIDER_LOCATION;
                 
    private int mainFrameVDividerLocation = 
                 DEFAULT_MAINFRAME_VSPLIT_DIVIDER_LOCATION;
    
    private Point mainFrameLocation = DEFAULT_MAINFRAME_LOCATION;
    
    private Dimension mainFrameSize = DEFAULT_MAINFRAME_SIZE;
    
    private Point jposEntryMergerDialogLocation = 
                   DEFAULT_MERGER_DIALOG_LOCATION;
    
    private Dimension jposEntryMergerDialogSize = DEFAULT_MERGER_DIALOG_SIZE;
    
    private int currentLookAndFeel = JAVALNF;
    
    private int currentTreeView = CATEGORYVIEW;
    
    private boolean expandTreeCheckBox = true;
    
    private boolean showNumbersAsHexCheckBox = true;
    
    private boolean autoLoadCheckBox = true;
    
    private boolean autoDeleteEntryOnCopy = true;

    private boolean configSaved = false;
    
	private boolean showJCL12PropView = false;

	private String fileDialogPath = DEFAULT_FILE_DIALOG_PATH;

	private transient Tracer tracer = TracerFactory.getInstance().
							            createTracer( "JposEntryEditorConfig" );
    //-------------------------------------------------------------------------
    // Class variables 
    //

    private static JposEntryEditorConfig instance = null;

    //-------------------------------------------------------------------------
    // Class constants 
    //

    public static final Dimension SCREENSIZE = 
    								Toolkit.getDefaultToolkit().getScreenSize();
    								
    public static final int WIDTH_INT = SCREENSIZE.width;
    public static final int HEIGHT_INT = SCREENSIZE.height;
    public static final double WIDTH = (double)WIDTH_INT;
    public static final double HEIGHT = (double)HEIGHT_INT;

    public static final int DEFAULT_MAINFRAME_HSPLIT_DIVIDER_LOCATION = 170;
    public static final int DEFAULT_MAINFRAME_VSPLIT_DIVIDER_LOCATION = 405;
    
    public static final int MIN_SUPPORTED_WIDTH =   800;
    public static final int MIN_SUPPORTED_HEIGHT =  600;
   
    public static final int DEFAULT_MAINFRAME_WIDTH = 
    						   Math.max( MIN_SUPPORTED_WIDTH, 
    						             (int)(WIDTH* 0.75) );
    						             
    public static final int DEFAULT_MAINFRAME_HEIGHT =  
							   Math.max( MIN_SUPPORTED_HEIGHT, 
							             (int)(HEIGHT*0.75) );
    
    public static final Dimension DEFAULT_MAINFRAME_SIZE = 
    								new Dimension( DEFAULT_MAINFRAME_WIDTH, 
    								               DEFAULT_MAINFRAME_HEIGHT );

    public static final int DEFAULT_MAINFRAME_X_LOCATION =  
    						  (int)((WIDTH*0.5) - (((double)DEFAULT_MAINFRAME_WIDTH)*0.5));

    public static final int DEFAULT_MAINFRAME_Y_LOCATION =  
                              (int)((HEIGHT*0.5) - (((double)DEFAULT_MAINFRAME_HEIGHT)*0.5));
    
    public static final Point DEFAULT_MAINFRAME_LOCATION = 
    							new Point( DEFAULT_MAINFRAME_X_LOCATION, 
    							           DEFAULT_MAINFRAME_Y_LOCATION );
    
    public static final int DEFAULT_CONFIGURATIONFRAME_WIDTH =  375;

    public static final int DEFAULT_CONFIGURATIONFRAME_HEIGHT = 125;

    public static final Dimension DEFAULT_CONFIGURATIONFRAME_SIZE = 
    new Dimension( DEFAULT_CONFIGURATIONFRAME_WIDTH, 
                   DEFAULT_CONFIGURATIONFRAME_HEIGHT );
    
    public static final int DEFAULT_CONFIGURATIONFRAME_X_LOCATION = 
    						  (int)((WIDTH*0.5) - (((double)DEFAULT_CONFIGURATIONFRAME_WIDTH)*0.5));

    public static final int DEFAULT_CONFIGURATIONFRAME_Y_LOCATION = 
    						  (int)((HEIGHT*0.5) - (((double)DEFAULT_CONFIGURATIONFRAME_HEIGHT)*0.5));

    public static final Point DEFAULT_CONFIGURATIONFRAME_LOCATION =   
    							new Point( DEFAULT_CONFIGURATIONFRAME_X_LOCATION, 
    							           DEFAULT_CONFIGURATIONFRAME_Y_LOCATION ); 

    public static final int DEFAULT_MERGER_DIALOG_WIDTH =  482;
    public static final int DEFAULT_MERGER_DIALOG_HEIGHT = 138;
    public static final Dimension DEFAULT_MERGER_DIALOG_SIZE = 
    new Dimension( DEFAULT_MERGER_DIALOG_WIDTH, DEFAULT_MERGER_DIALOG_HEIGHT );
    
    public static final int DEFAULT_MERGER_DIALOG_X_LOCATION = 
    (int)((WIDTH*0.5) - (((double)DEFAULT_MERGER_DIALOG_WIDTH)*0.5));
    
    public static final int DEFAULT_MERGER_DIALOG_Y_LOCATION = 
    (int)((HEIGHT*0.5) - (((double)DEFAULT_MERGER_DIALOG_HEIGHT)*0.5));
    
    public static final Point DEFAULT_MERGER_DIALOG_LOCATION = 
    new Point( DEFAULT_MERGER_DIALOG_X_LOCATION, 
               DEFAULT_MERGER_DIALOG_Y_LOCATION ); 
   
   	public static final String JCL_EDITOR_CONFIG_FILE_PATH = 
   	System.getProperty( "user.home" ) + File.separator + ".jcl";
   	
    public static final String JPOSENTRYEDITORCONFIG_FILE_NAME = 
    							 "editor.config";

	public static final String DEFAULT_FILE_DIALOG_PATH = ".";

    // constants to control tree view
    public static final int SORTEDVIEW = 0;
    public static final int CATEGORYVIEW = 1;
    public static final int MANUFACTURERVIEW = 2;

    // constants to control look and feel
    public static final int JAVALNF = 0;
    public static final int NATIVELNF = 1;

    //-------------------------------------------------------------------------
    // I18N  class constants
    //

    public static final String JPOSENTRYEDITOR_STRING =  
    							 JposEntryEditorMsg.JPOSENTRYEDITOR_STRING;
    							 
    public static final String ERRORSAVINGFILE_STRING = 
    							 JposEntryEditorMsg.ERRORSAVINGFILE_STRING;   
}