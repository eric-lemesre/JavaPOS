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
 * Dialog for JposEntryEditorConfig manipulation, contains different
 * program settings to be mantained
 * @author Manuel M Monserrate
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (SF 2K meeting)
 */
class JposEntryEditorConfigDialog extends JDialog
{
    /**
     * 1-arg ctor
     * @param parent the parent JFrame
     * @since 1.3 (SF 2K meeting)
     */
    public JposEntryEditorConfigDialog( JFrame parent )
    {
        super( parent, JPOSENTRYPREFERENCES_STRING , true );
        
        setResizable( false );

        setLocation( JposEntryEditorConfig.DEFAULT_CONFIGURATIONFRAME_LOCATION );
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel treeViewPanel = new JPanel( new GridLayout( 2, 1 ) );

        JPanel viewOptionsPanel = new JPanel( new GridLayout( 1, 3 ) );

        viewOptionsPanel.setBorder( BorderFactory.createTitledBorder( 
                                    BorderFactory.createEtchedBorder(),  
                                    VIEWOPTIONS_STRING ) );
        
        ButtonGroup buttonGroup = new ButtonGroup();

        buttonGroup.add( sortedButton );
        buttonGroup.add( byCategoryButton );
        buttonGroup.add( byManufacturerButton );

        sortedButton.
        addActionListener(	new ActionListener()
                           	{
                            	public void actionPerformed( ActionEvent e )
                                { 
                                	currentTreeView = 
                                	JposEntryEditorConfig.SORTEDVIEW;  }
                                }
                         );

        byCategoryButton.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                                { 
                                	currentTreeView =  
                                	JposEntryEditorConfig.CATEGORYVIEW; 
                                }
                            }
                         );

        byManufacturerButton.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                                { 
                                	currentTreeView =  
                                	JposEntryEditorConfig.MANUFACTURERVIEW; 
                                }
                            }
                         );

        JPanel tempLayoutPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );

        tempLayoutPanel.add( sortedButton );
        viewOptionsPanel.add( tempLayoutPanel );

        tempLayoutPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        tempLayoutPanel.add( byCategoryButton );
        viewOptionsPanel.add( tempLayoutPanel );

        tempLayoutPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        tempLayoutPanel.add( byManufacturerButton );
        viewOptionsPanel.add( tempLayoutPanel );

        treeViewPanel.add( viewOptionsPanel );

        JPanel autoExpandPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );

        tempLayoutPanel = new JPanel( new GridLayout( 2, 1 ) );

        expandTreeCheckBox.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed(ActionEvent e) 
                                { expandTreeCheckBoxSelected(); }
                            }
                         );
        
        autoExpandPanel.add( expandTreeCheckBox );

        tempLayoutPanel.add( new JPanel() );
        tempLayoutPanel.add( autoExpandPanel );
        
        treeViewPanel.add( tempLayoutPanel );

        treeViewPanel.setPreferredSize( JposEntryEditorConfig.
        							    DEFAULT_CONFIGURATIONFRAME_SIZE );

        tabbedPane.addTab( TREEVIEW_STRING, treeViewPanel );
        
        JPanel lookAndFeelPanel = new JPanel( new GridLayout( 2, 1) );
        
        ButtonGroup lafButtons = new ButtonGroup();
        
        lafButtons.add( javaLNFButton );
        lafButtons.add( nativeLNFButton );

        javaLNFButton.
        addActionListener(	new ActionListener()
                            {
                         		public void actionPerformed(ActionEvent e) 
                                { lookAndFeelSelection = JposEntryEditorConfig.
                                					     JAVALNF; }
                            }
                         );

        nativeLNFButton.
        addActionListener(	new ActionListener()
                            {
                         		public void actionPerformed(ActionEvent e) 
                                { lookAndFeelSelection = JposEntryEditorConfig.
                                						 NATIVELNF; }
                            }
                         );

        JPanel lookAndFeelInsidePanel = new JPanel( new GridLayout( 1, 2 ) );
        lookAndFeelInsidePanel.setBorder( BorderFactory.createTitledBorder( 
                                          BorderFactory.createEtchedBorder(), 
                                          "Options" ) );

        tempLayoutPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        tempLayoutPanel.add( javaLNFButton );
        lookAndFeelInsidePanel.add( tempLayoutPanel );

        tempLayoutPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        tempLayoutPanel.add( nativeLNFButton );
        lookAndFeelInsidePanel.add( tempLayoutPanel );

        lookAndFeelPanel.add( lookAndFeelInsidePanel );
        
        lookAndFeelPanel.add( new JPanel() );

        tabbedPane.addTab( LOOKANDFEEL_STRING, lookAndFeelPanel );

        JPanel generalOptionsPanel = new JPanel( new GridLayout( 4, 1 ) );

        tempLayoutPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        showAsHexCheckBox.
        addActionListener(	new ActionListener()
                            {
                         		public void actionPerformed(ActionEvent e) 
                                { showAsHexCheckBoxSelected(); }
                            }
                         );

        tempLayoutPanel.add( showAsHexCheckBox );
        generalOptionsPanel.add( tempLayoutPanel );
        
        tempLayoutPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		autoLoadCheckBox.setToolTipText( RESET_CONFIG_TOOLTIPTEXT_STRING );
        autoLoadCheckBox.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed(ActionEvent e) 
                                { autoLoadCheckBoxSelected(); }
                            }
                                            );

        tempLayoutPanel.add( autoLoadCheckBox );
        generalOptionsPanel.add( tempLayoutPanel );

        tempLayoutPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        autoDeleteEntryOnCopyCheckBox.
        addActionListener(	new ActionListener()
                           	{
								public void actionPerformed(ActionEvent e) 
                                { autoDeleteEntryOnCopyCheckBoxSelected(); }
                            }
                         );

        tempLayoutPanel.add( autoDeleteEntryOnCopyCheckBox );
        generalOptionsPanel.add( tempLayoutPanel );

        tempLayoutPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        resetConfigButton.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed(ActionEvent e) 
                                { resetConfigButtonClicked(); }
                            }
                         );

        tempLayoutPanel.add( resetConfigButton );
        generalOptionsPanel.add( tempLayoutPanel );

        tabbedPane.addTab( OTHER_TAB_NAME_STRING, generalOptionsPanel );

        JPanel bottomPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );

        JButton okButton = new JButton( OKBUTTON_STRING );
        JButton cancelButton = new JButton( CANCELBUTTON_STRING );

        okButton.
        addActionListener( 	new ActionListener()
                           	{
                         		public void actionPerformed( ActionEvent e )
                            	{ ok(); }
                            }
                         );

        cancelButton.
        addActionListener(	new ActionListener()
                            {
                         		public void actionPerformed( ActionEvent e )
                                { cancel(); }
                            }
                         );
        
        bottomPanel.add( okButton );
        bottomPanel.add( cancelButton );

        getContentPane().setLayout( new BorderLayout() );
        getContentPane().add( tabbedPane, BorderLayout.CENTER );
        getContentPane().add( bottomPanel, BorderLayout.SOUTH );
        
        pack();
    }

    //-------------------------------------------------------------------------
    // Package methods
    //
    
    /**
     * connects this object to the MainFrame so that it can set its properties
     * @since 1.3 (SF 2K meeting)
     */
    void setMainFrame( MainFrame mainFrame) { this.mainFrame = mainFrame; }

    //-------------------------------------------------------------------------
    // Private methods
    //

    /**
     * called when Ok button is pressed to set preferences in MainFrame and 
     * save preferences File
     * @since 1.3 (SF 2K meeting)
     */
    private void ok()
    {
        mainFrame.setCurrentTreeView( currentTreeView );
        mainFrame.setLookAndFeel( lookAndFeelSelection );

        setVisible( false );
    }

    /**
     * called when Ok button is pressed to set preferences in MainFrame and 
     * save preferences File
     * @since 1.3 (SF 2K meeting)
     */
    private void cancel() { setVisible( false ); }

    /**
     * called when the "auto expand tree" checkbox is selected/deselected
     * @since 1.3 (SF 2K meeting)
     */
    private void expandTreeCheckBoxSelected() 
    { mainFrame.setExpandTreeFlag( getExpandTreeCheckBox() ); }

    /**
     * called when the "auto expand tree" checkbox is selected/deselected
     * @since 1.3 (SF 2K meeting)
     */
    private void showAsHexCheckBoxSelected() 
    { mainFrame.setShowAsHexFlag( getShowAsHexCheckBox() ); }

    /**
     * called when the "auto expand tree" checkbox is selected/deselected
     * @since 1.3 (SF 2K meeting)
     */
    private void autoLoadCheckBoxSelected() 
    { mainFrame.setAutoLoadFlag( getAutoLoadCheckBox() ); }

    /**
     * called when the "auto delete entry on copy" checkbox is 
     * selected/deselected
     * @since 2.0.0
     */
    private void autoDeleteEntryOnCopyCheckBoxSelected() 
    { mainFrame.setAutoDeleteEntryOnCopy( getAutoDeleteEntryOnCopy() ); }

	/**
	 * Called when the "Reset Config" button is clicked
	 * @since 2.0.0
	 */
	private void resetConfigButtonClicked() 
	{ 
		mainFrame.resetConfig(); 

		JOptionPane.showMessageDialog( this, 
		                               RESET_CONFIG_NEED_TO_RESTART_EDITOR_MSG );
	}

    //--------------------------------------------------------------------------
    // Package methods
    //

    /**
     * Getter method for the expandTreeCheckBox.isSelected() property
     * @return true
     */
    boolean getExpandTreeCheckBox() 
    { return expandTreeCheckBox.isSelected(); }

    /**
     * Setter method for the expandTreeCheckBox
     * @param b the boolean parameter
     */
    void setExpandTreeCheckBox( boolean b ) 
    { expandTreeCheckBox.setSelected( b ); }

    /**
     * Getter method for the showAsHexCheckBox.isSelected() property
     * @return true
     */
    boolean getShowAsHexCheckBox() 
    { return showAsHexCheckBox.isSelected(); }

    /**
     * Setter method for the showAsHexCheckBox
     * @param b the boolean parameter
     */
    void setShowAsHexCheckBox( boolean b ) 
    { showAsHexCheckBox.setSelected( b ); }

    /**
     * Getter method for the autoDeleteEntryOnCopy
     * @return true if selected
     */
    boolean getAutoDeleteEntryOnCopy() 
    { return autoDeleteEntryOnCopyCheckBox.isSelected(); }

    /**
     * Setter method for the autoDeleteCheckBox
     * @param b the boolean parameter
     */
    void setAutoDeleteEntryOnCopy( boolean b ) 
    { autoDeleteEntryOnCopyCheckBox.setSelected( b ); }

    /** @return the autoLoadCheckBox.isSelected() property */
    boolean getAutoLoadCheckBox() { return autoLoadCheckBox.isSelected(); }

    /**
     * Setter method for the autoLoadCheckBox
     * @param b the boolean parameter
     */
    void setAutoLoadCheckBox( boolean b ) 
    { autoLoadCheckBox.setSelected( b ); }

    /**
     * Setter method for the look and Feel Radio Buttons
     * @param i the current LookAndFeel param
     */
    void setLNFRadioButtonSelected( int i )
    {
        lookAndFeelSelection = i;

        if( lookAndFeelSelection == JposEntryEditorConfig.JAVALNF )
            javaLNFButton.setSelected( true );
        else
            nativeLNFButton.setSelected( true );
    }

    /**
     * Setter method for the current tree view radio buttons
     * @param i the currentTreeView param
     */
    void setCurrentTreeViewButtonSelected( int i )
    {
        currentTreeView = i;

        if( currentTreeView == JposEntryEditorConfig.CATEGORYVIEW )
            byCategoryButton.setSelected( true );
        else 
        if( currentTreeView == JposEntryEditorConfig.MANUFACTURERVIEW )
            byManufacturerButton.setSelected( true );
        else
            sortedButton.setSelected( true );
    }

    //--------------------------------------------------------------------------
    // Instance variables
    //

    private MainFrame mainFrame = null;

    private JCheckBox expandTreeCheckBox = 
    				   new JCheckBox( AUTOEXPAND_STRING, true );
    				   
    private JCheckBox showAsHexCheckBox = 
    				   new JCheckBox( SHOWINTEGERSASHEX, true );
    				   
    private JCheckBox autoLoadCheckBox = 
    				   new JCheckBox( AUTOLOADREGISTRY_STRING, true );
    				   
	private JCheckBox autoDeleteEntryOnCopyCheckBox = 
					   new JCheckBox( AUTO_DELETE_ENTRY_ON_COPY_STRING, true );
    
    private JRadioButton sortedButton =  
    					  new JRadioButton( SORTED_STRING, true );
    					  
    private JRadioButton byCategoryButton = 
    					  new JRadioButton(CATEGORY_STRING, false );
    					  
    private JRadioButton byManufacturerButton = 
    					  new JRadioButton( MANUFACTURER_STRING, false );

    private JRadioButton javaLNFButton =  
    					  new JRadioButton( JAVALOOKANDFEEL_STRING, true );
    private JRadioButton nativeLNFButton = 
    					  new JRadioButton( NATIVELOOKANDFEEL_STRING, false );
    
    private int currentTreeView = JposEntryEditorConfig.SORTEDVIEW;
    
    private int lookAndFeelSelection = JposEntryEditorConfig.JAVALNF;

	private JButton resetConfigButton = 
					 new JButton( RESET_CONFIG_BUTTON_STRING );

    //---------------------------------------------------------------------------
    // I18N  class constants
    //

	//<i18n>
	public static final String OTHER_TAB_NAME_STRING = "Other";
	
	public static final String RESET_CONFIG_BUTTON_STRING = "Reset Config";
	
	public static final String RESET_CONFIG_TOOLTIPTEXT_STRING = 
								 "Reset configuration options to defaults";
								 
	public static final String AUTO_DELETE_ENTRY_ON_COPY_STRING = 
								 "Auto delete device entry on copy";
								 
	public static final String RESET_CONFIG_NEED_TO_RESTART_EDITOR_MSG = 
								 "You will need to restart editor for configuration reset to take effect";
	//</i18n>
    
    public static final String JPOSENTRYPREFERENCES_STRING = 
                                 JposEntryEditorMsg.JPOSENTRYPREFERENCES_STRING;
                                 
    public static final String TREEVIEW_STRING = 
                                 JposEntryEditorMsg.TREEVIEW_STRING;
                                 
    public static final String LOOKANDFEEL_STRING = 
                                 JposEntryEditorMsg.LOOKANDFEEL_STRING;
                                 
    public static final String AUTOEXPAND_STRING = 
                                 JposEntryEditorMsg.AUTOEXPAND_STRING;
                                 
    public static final String SORTED_STRING = 
    							 JposEntryEditorMsg.SORTED_STRING;
    
    public static final String CATEGORY_STRING = 
    						     JposEntryEditorMsg.CATEGORY_STRING;
    
    public static final String MANUFACTURER_STRING = 
    							 JposEntryEditorMsg.MANUFACTURER_STRING;
    
    public static final String JAVALOOKANDFEEL_STRING = 
    							 JposEntryEditorMsg.JAVALOOKANDFEEL_STRING;
    
    public static final String NATIVELOOKANDFEEL_STRING = 
    							 JposEntryEditorMsg.NATIVELOOKANDFEEL_STRING;
    
    public static final String CANCELBUTTON_STRING = 
    							 JposEntryEditorMsg.CANCELBUTTON_STRING;
    
    public static final String OKBUTTON_STRING = 
    							 JposEntryEditorMsg.OKBUTTON_STRING;
    
    public static final String VIEWOPTIONS_STRING = 
    							 JposEntryEditorMsg.VIEWOPTIONS_STRING;
    
    public static final String SHOWINTEGERSASHEX = 
    							 JposEntryEditorMsg.SHOWINTEGERSASHEX_STRING;
    
    public static final String AUTOLOADREGISTRY_STRING = 
    							 JposEntryEditorMsg.AUTOLOADREGISTRY_STRING;
}