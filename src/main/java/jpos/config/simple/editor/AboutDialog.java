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
import java.awt.event.*;

import javax.swing.*;

/**
 * This is a simple About dialog box
 * @since 0.1 (Philly 99 meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @author Kriselie D Rivera
 */
public class AboutDialog extends JDialog
{
   /**
    * AboutDialog default one-argument constructor.
    * @param parent Parent java.awt.Frame of this dialog
    */
    public AboutDialog( JFrame parent )
    {
        //Call superclass (Dialog) constructor
        super( parent, ABOUTTITLE_STRING , false );
        setResizable( false );

        //Initialize all GUI widgets
        getContentPane().setLayout( null );
        setSize( getInsets().left + getInsets().right + 408, getInsets().top + 
        		 getInsets().bottom + 227 );
        aboutBoxTitleLabel = new JLabel( ABOUTTITLELABEL_STRING + jpos.loader.Version.getVersionString() );
        aboutBoxTitleLabel.setFont( new Font( "Helvetica", Font.BOLD, 14 ) );
        getContentPane().add( aboutBoxTitleLabel );
        aboutBoxTitleLabel.setBounds( getInsets().left + 12, 
        							  getInsets().top + 13, 255, 20 );
        okButton=new JButton( OKBUTTON_STRING );
        getContentPane().add( okButton );
        okButton.setBounds( getInsets().left + 306, 
        				    getInsets().top + 18, 72, 26 );
        javaVerionLabel= new JLabel( JAVAVERSION_STRING );
        getContentPane().add( javaVerionLabel );
        javaVerionLabel.setBounds( getInsets().left + 22, 
        						   getInsets().top + 46, 108, 13 );
        javaVersion = new JLabel( LABEL_STRING );
        javaVersion.setFont( new Font( DIALOG_STRING, Font.PLAIN, 10 ) );
        getContentPane().add( javaVersion );
        javaVersion.setBounds( getInsets().left + 138, 
        					   getInsets().top + 46, 84, 13 );
        osNameLabel = new JLabel( OS_STRING );
        getContentPane().add( osNameLabel );
        osNameLabel.setBounds( getInsets().left + 18, 
        					   getInsets().top + 72, 102, 13 );
        osName = new JLabel( LABEL_STRING );
        osName.setFont( new Font( DIALOG_STRING, Font.PLAIN, 10 ) );
        getContentPane().add( osName );
        osName.setBounds( getInsets().left + 138, 
        				  getInsets().top + 72, 90, 14 );
        architectureLabel = new JLabel( ARCHITECTURE_STRING );
        getContentPane().add( architectureLabel );
        architectureLabel.setBounds( getInsets().left + 18, 
        							 getInsets().top + 96, 102, 15 );
        architecture = new JLabel(  LABEL_STRING );
        architecture.setFont( new Font( DIALOG_STRING, Font.PLAIN, 10 ) );
        getContentPane().add( architecture );
        architecture.setBounds( getInsets().left + 138, 
        						getInsets().top + 96, 90, 14 );
        osVersion = new JLabel(  LABEL_STRING );
        osVersion.setFont(new Font( DIALOG_STRING, Font.PLAIN, 10 ) );
        getContentPane().add( osVersion );
        osVersion.setBounds( getInsets().left + 228, 
        					 getInsets().top + 71, 66, 13 );
        totalMemLabel= new JLabel( TOTALMEMORY_STRING );
        getContentPane().add( totalMemLabel );
        totalMemLabel.setBounds( getInsets().left + 18, 
        						 getInsets().top + 124, 108, 13 );
        totalMem = new JLabel(  LABEL_STRING );
        totalMem.setFont( new Font( DIALOG_STRING, Font.PLAIN, 10 ) );
        getContentPane().add( totalMem );
        totalMem.setBounds( getInsets().left + 138, 
        					getInsets().top + 124, 90, 14 );
        JLabel bytesLabel1 = new JLabel( BYTES_STRING );
        bytesLabel1.setFont( new Font( DIALOG_STRING, Font.PLAIN, 10 ) );
        getContentPane().add( bytesLabel1 );
        bytesLabel1.setBounds( getInsets().left + 228, 
        					   getInsets().top + 124, 48, 14 );
        freeMemLabel = new JLabel( FREEMEMORY_STRING );
        getContentPane().add( freeMemLabel );
        freeMemLabel.setBounds( getInsets().left + 18, 
        						getInsets().top + 150, 108, 13 );
        freeMem = new JLabel(  LABEL_STRING );
        freeMem.setFont( new Font( DIALOG_STRING, Font.PLAIN, 10 ) );
        getContentPane().add( freeMem );
        freeMem.setBounds( getInsets().left + 138, 
        				   getInsets().top + 150, 90, 14 );
        JLabel bytesLabel2 = new JLabel( BYTES_STRING );
        bytesLabel2.setFont( new Font( DIALOG_STRING, Font.PLAIN, 10 ) );
        getContentPane().add( bytesLabel2 );
        bytesLabel2.setBounds( getInsets().left + 228, 
        					   getInsets().top + 150 , 48, 14 );

        //Set the information labels
        javaVersion.setText( System.getProperties().
        					 getProperty( SYSTEM_JAVA_VERSION_KEY ) );
        					 
        osName.setText( System.getProperties().
        				getProperty( SYSTEM_OS_NAME_KEY ) );
        				
        architecture.setText( System.getProperties().
        					  getProperty( SYSTEM_OS_ARCH_KEY ) );
        					  
        osVersion.setText( System.getProperties().
        				   getProperty( SYSTEM_OS_VERSION_KEY ) );

        String totalMemString = ( new Long( Runtime.getRuntime().
        						totalMemory() ) ).toString();
        totalMem.setText( totalMemString );

        String freeMemString = ( new Long( Runtime.getRuntime().
        					   freeMemory() ) ).toString();
        freeMem.setText( freeMemString );

        //Create the JavaLogo image
        javaLogoImage = loadImage( JAVA_LOGO_FILE );
        
        //Register ActionListener anonymous inner class for OK button
        okButton.
        addActionListener(	new ActionListener()
                           	{
                            	public void actionPerformed( ActionEvent e ) 
                            	{ AboutDialog.this.okButtonClicked( e ); }
                            }
                         );

        //Register a WindowAdapter anonymous inner class for this Dialog
        addWindowListener(	new WindowAdapter()
                            {
                                public void windowClosing( WindowEvent e ) 
                                { AboutDialog.this.windowClosing( e ); }
                            }
                         );
    }

    private Image loadImage( String fileName )
    {
       return Toolkit.getDefaultToolkit().getImage( fileName );
    }

    /**
     * Draws Java logo image on the dialog box
     * @param g the Graphics object to draw on
     */
    public synchronized void paint( Graphics g )
    {
       super.paint( g );

       //Draw the image
       g.drawImage( javaLogoImage,
                    getSize().width - javaLogoImage.getWidth( this ) - 
                    JAVA_LOGO_WIDHT_ADJUST,
                    getSize().height - javaLogoImage.getHeight( this ) - 
                    JAVA_LOGO_HEIGHT_ADJUST, this );
    }

    /**
     * Position's the dialog in middle of parent frame and show/hide the dialog
     * @param b the boolean parameter
     */
    public synchronized void setVisible( boolean b )
    {
        Rectangle bounds = getParent().getBounds();
        Rectangle abounds = getBounds();

        setLocation( bounds.x + ( bounds.width - abounds.width ) / 2,
                        bounds.y + ( bounds.height - abounds.height ) / 2);

        super.setVisible( b );

        okButton.requestFocus();
    }

    /**
     * This methods implements the ActionListener interface.  Just hides dialog. 
     * @param e the event object
     */
    void okButtonClicked( ActionEvent e ) { setVisible( false ); }

    /**
     * Invoked when a window is in the process of being closed.
     * The close operation can be overridden at this point.
     * @param e the event object
     */
    void windowClosing( WindowEvent e ) { setVisible( false ); }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private JLabel   aboutBoxTitleLabel;
    private JButton  okButton;
    private JLabel   copyrightLabel;
    private JLabel   javaVerionLabel;
    private JLabel   javaVersion;
    private JLabel   osNameLabel;
    private JLabel   osName;
    private JLabel   architectureLabel;
    private JLabel   architecture;
    private JLabel   osVersion;
    private JLabel   totalMemLabel;
    private JLabel   totalMem;
    private JLabel   bytesLabel;
    private JLabel   freeMemLabel;
    private JLabel   freeMem;

    private Image   javaLogoImage;

	//--------------------------------------------------------------------------
	// Class constants
    //

    private static final String SYSTEM_JAVA_VERSION_KEY = "java.version";
    private static final String SYSTEM_OS_NAME_KEY      = "os.name";
    private static final String SYSTEM_OS_ARCH_KEY      = "os.arch";
    private static final String SYSTEM_OS_VERSION_KEY   = "os.version";
    private static final String JAVA_LOGO_FILE          = "JavaLogo.gif";
    private static final int JAVA_LOGO_WIDHT_ADJUST  = 50;
    private static final int JAVA_LOGO_HEIGHT_ADJUST = 60;


	//--------------------------------------------------------------------------
    // I18N  class constants
    //

	public static final String ABOUTTITLE_STRING = 
     							 JposEntryEditorMsg.ABOUTTITLE_STRING;
     							  
    public static final String ABOUTTITLELABEL_STRING = 
     							 JposEntryEditorMsg.ABOUTTITLELABEL_STRING;
     							  
    public static final String OKBUTTON_STRING = 
     							 JposEntryEditorMsg.OKBUTTON_STRING;
     							  
    public static final String JAVAVERSION_STRING = 
     							 JposEntryEditorMsg.JAVAVERSION_STRING;
     							  
    public static final String LABEL_STRING = 
     							 JposEntryEditorMsg.LABEL_STRING;
     							  
    public static final String DIALOG_STRING = 
     							 JposEntryEditorMsg.DIALOG_STRING;
     							  
    public static final String OS_STRING = 
     							 JposEntryEditorMsg.OS_STRING;
     							  
    public static final String ARCHITECTURE_STRING = 
     							 JposEntryEditorMsg.ARCHITECTURE_STRING;
     							  
    public static final String TOTALMEMORY_STRING = 
     							 JposEntryEditorMsg.TOTALMEMORY_STRING;
     							  
    public static final String BYTES_STRING = 
     							 JposEntryEditorMsg.BYTES_STRING;
     							  
    public static final String FREEMEMORY_STRING = 
     							 JposEntryEditorMsg.FREEMEMORY_STRING;
}