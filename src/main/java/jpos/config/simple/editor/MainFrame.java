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
import java.util.*;
import java.io.*;
import java.text.MessageFormat;

import javax.swing.*;    
import javax.swing.border.*;
import javax.swing.tree.*;

import jpos.loader.JposServiceLoader;
import jpos.config.*;
import jpos.config.simple.*;
import jpos.config.simple.xml.XercesRegPopulator;
import jpos.config.simple.xml.XmlRegPopulator;
import jpos.util.*;
import jpos.util.tracing.Tracer;
import jpos.util.tracing.TracerFactory;

/**
 * This is the main JFrame for the JposEntryEditor application
 * @since 0.1 (Philly 99 meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @author Manuel Monseratte
 * @author Kriselie Rivera
 */
class MainFrame extends JFrame
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	// 
	
    /**
     * Default ctor
     * @since 0.1 (Philly 99 meeting)
     */
    public MainFrame()
    {
        super( MAINFRAME_TITLE );

        JPanel toolbarPanel = new JPanel( new BorderLayout() );

        newFileButton.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed(ActionEvent e)
                                { newFile(); }
                            }
                         );

        openFileButton.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                            	{ openFile(); }
                            }
                         );

        saveFileButton.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                            	{ saveFile(); }
                            }
                         );

        saveAsFileButton.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                                { saveAsFile(); }
                            }
                         );

        closeFileButton.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                                {
                               		if( registryMode )
                                    	closeJposRegistry();
                                    else
                                    	closeFile(); 
                                }
                            }
                         );

        addEntryButton.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                                { addJposEntry(); }

							}
                        );

        copyEntryButton.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed(ActionEvent e)
                                { copyJposEntry(); }
                            }
                         );

        removeEntryButton.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                                { removeJposEntry(); }
                            }
                         );

        configButton.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed(ActionEvent e)
                                { configDialog(); }
                            }
                         );

        aboutButton.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed(ActionEvent e)
                                { about(); }
                            }
                         );

        jposPropertiesSubMenu.
        addActionListener(	new ActionListener()
							{
								public void actionPerformed(ActionEvent e)
								{ jposProperties(); }
							}
						 );
        
        toolbar.add( newFileButton );
        toolbar.add( openFileButton );
        toolbar.add( saveFileButton );
        toolbar.add( saveAsFileButton );
        toolbar.add( closeFileButton );
        toolbar.add( new JToolBar.Separator() );
        toolbar.add( addEntryButton );
        toolbar.add( copyEntryButton );
        toolbar.add( removeEntryButton );
        toolbar.add( new JToolBar.Separator() );
        toolbar.add( configButton );
        toolbar.add( aboutButton );

        toolbar.setFloatable( false );

        toolbarPanel.add( toolbar, BorderLayout.WEST );

        getContentPane().setLayout( new BorderLayout() );
        getContentPane().add( toolbarPanel, BorderLayout.NORTH );
        
        bottomPanel.setLayout( new BorderLayout() );
        bottomPanel.setBorder( BorderFactory.
        					   createTitledBorder( 
        					   BorderFactory.createLineBorder( Color.black ), 
                               MESSAGES_STRING, 
                               TitledBorder.ABOVE_TOP, 
                               TitledBorder.LEFT ) );

        bottomTextArea.setEditable( false );
        bottomTextArea.append( TOPOFFILE_STRING );
        bottomPanel.add( bottomTextArea, BorderLayout.CENTER );

        hSplit = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, 
        						 jposEntryTreePanel, jposEntryPanel );
        hSplit.setContinuousLayout( true );
        hSplit.setOneTouchExpandable( true );

        JPanel hPanel = new JPanel( new BorderLayout() );
        hPanel.add( hSplit, BorderLayout.CENTER );

        JScrollPane bottomPanelScrollPane = new JScrollPane();
        bottomPanelScrollPane.getViewport().add( bottomPanel );

        vSplit = new JSplitPane( JSplitPane.VERTICAL_SPLIT, hPanel, 
        						 bottomPanelScrollPane );
        vSplit.setContinuousLayout( true );
        vSplit.setOneTouchExpandable( true );

        getContentPane().add( vSplit, BorderLayout.CENTER );

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu( FILE_STRING );
        fileMenu.add( newFileSubMenu);
        fileMenu.add( openFileSubMenu);
        fileMenu.add( saveFileSubMenu);
        fileMenu.add( saveAsFileSubMenu);
        fileMenu.addSeparator();
        fileMenu.add( jposPropertiesSubMenu );
        fileMenu.addSeparator();
        fileMenu.add( loadJposRegistrySubMenu );
        fileMenu.add( saveJposRegistrySubMenu );
        fileMenu.add( saveJposRegistryAsSubMenu );        
        fileMenu.add( closeJposRegistrySubMenu );
        fileMenu.addSeparator();
        fileMenu.add( closeFileSubMenu);
        fileMenu.addSeparator();
        fileMenu.add( exitSubMenu );

        JMenu editMenu = new JMenu( EDIT_STRING );
        editMenu.add( addJposEntrySubMenu );
        editMenu.add( copyJposEntrySubMenu );
        editMenu.add( removeJposEntrySubMenu );
        editMenu.addSeparator();
        
        JMenu editorModeMenu = new JMenu( EDITOR_MODE_MENU_STRING );
        
        editorModeButtonGroup.add( xmlEditorModeMenuItem );
        editorModeButtonGroup.add( serEditorModeMenuItem );
        
        editorModeMenu.add( xmlEditorModeMenuItem );
        editorModeMenu.add( serEditorModeMenuItem );        
        editMenu.add( editorModeMenu );
        
        editMenu.addSeparator();
        editMenu.add( jposEntryMergerSubMenu );
        editMenu.addSeparator();
        editMenu.add( configSubMenu );

        JMenu helpMenu = new JMenu( HELP_STRING );
        helpMenu.add( aboutSubMenu );

        menuBar.add( fileMenu );
        menuBar.add( editMenu );
        menuBar.add( helpMenu );

        setJMenuBar( menuBar );
      
        initMenu();                   
            
        addWindowListener(	new WindowAdapter()
                            {
                                public void windowClosing( WindowEvent e )
                                { exit(); }
                            }
                         );

        jposEntryTreePanel.
        addJposEntryTreeListener(	new JposEntryTreeListener()
                                    {
                                    	public void newJposEntrySelected( 
                                    	             JposEntryTreeEvent event ) 
                                        { MainFrame.this.
                                          newJposEntrySelected( event ); }
                                    }
                               );

        newFileSubMenu.
        addActionListener(	new ActionListener()
                            {     
                            	public void actionPerformed( ActionEvent e )
                            	{ newFile(); }
                            }
                         );

        openFileSubMenu.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                            	{ openFile(); }
                            }
                         );

        saveFileSubMenu.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                            	{ saveFile(); }
                            }
                         );

        saveAsFileSubMenu.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                            	{ saveAsFile(); }
                            }
                         );

        jposPropertiesSubMenu.
        addActionListener(	new ActionListener()
							{
								public void actionPerformed( ActionEvent e )
								{ jposProperties(); }
							}
					     );

        loadJposRegistrySubMenu.
        addActionListener(  new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                            	{ loadJposRegistry(); }
                            }
                        );

        saveJposRegistrySubMenu.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                                { saveJposRegistry(); }
                            }
                         );

        saveJposRegistryAsSubMenu.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                                { saveJposRegistryAs(); }
                            }
                         );

                         
        closeJposRegistrySubMenu.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                                { closeJposRegistry(); }
                            }
                         );
      
        closeFileSubMenu.
        addActionListener(	new ActionListener()
                           	{
                         		public void actionPerformed( ActionEvent e )
                                { closeFile(); }
                            }
                         );

        exitSubMenu.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                                { exit(); }
                            }
                         );

        aboutSubMenu.
        addActionListener(	new ActionListener()
                            {
                            	public void actionPerformed( ActionEvent e )
                                { about(); }
                            }
                         );

        addJposEntrySubMenu.
        addActionListener(	new ActionListener()                            
                            {                                               
                            	public void actionPerformed( ActionEvent e )
                                { addJposEntry(); }                                
                            }                                               
                         );                                                 

        copyJposEntrySubMenu.
        addActionListener(  new ActionListener()                            
                            {                                               
                            	public void actionPerformed( ActionEvent e )
                            	{ copyJposEntry(); }                                
                            }                                               
                                             );                                                 

        removeJposEntrySubMenu.
        addActionListener(	new ActionListener()
                            {                                               
                            	public void actionPerformed( ActionEvent e )
                            	{ removeJposEntry(); }                                
                            }                                               
                         );                                                 

        addJposEntryPopupSubMenu.
        addActionListener(	new ActionListener()                            
                            {                                               
                            	public void actionPerformed( ActionEvent e )
                            	{ addJposEntry(); }                                
                            }                                               
                         );                                                 

        copyJposEntryPopupSubMenu.
        addActionListener(	new ActionListener()                            
                            {                                               
                            	public void actionPerformed( ActionEvent e )
                            	{ copyJposEntry(); }                                
                            }                                               
                         );                                                 

        removeJposEntryPopupSubMenu.
        addActionListener(	new ActionListener()
                            {                                               
                            	public void actionPerformed( ActionEvent e )
                            	{ removeJposEntry(); }                                
                            }                                               
                         );                                                 

        configSubMenu.
        addActionListener(    new ActionListener()
                              {
                              	public void actionPerformed(ActionEvent e) 
                              	{ configDialog(); }
                              }
                         );

        jposEntryMergerSubMenu.
        addActionListener(   new ActionListener()
                             {
                             	public void actionPerformed(ActionEvent e) 
                             	{ jposEntryMergerDialog(); }
                             }
                         );
        
        sortedRBMenuItem.
        addActionListener(   new ActionListener()
                             {
                             	public void actionPerformed( ActionEvent e )
                                { changeTreeView( JposEntryEditorConfig.SORTEDVIEW ); }
                             }
                         );

        categoryRBMenuItem.
        addActionListener(   new ActionListener()
                             {
                             	public void actionPerformed( ActionEvent e )
                                { changeTreeView( JposEntryEditorConfig.CATEGORYVIEW ); }
                             }
                         );

        manufacturerRBMenuItem.
        addActionListener(   new ActionListener()
                             {
                             	public void actionPerformed( ActionEvent e )
                                { changeTreeView( JposEntryEditorConfig.MANUFACTURERVIEW ); }
                             }
                         );

        autoExpandTreeMenuItem.
        addActionListener(   new ActionListener()
                             {
                             	public void actionPerformed( ActionEvent e )
                             	{ autoExpandTreeSelected( autoExpandTreeMenuItem.getState() ); }
                             }
                         );
                         
		xmlEditorModeMenuItem.
		addActionListener(	new ActionListener()
							{
								public void actionPerformed( ActionEvent e )
								{ xmlEditorModeSelected(); }
							}
						 );

		serEditorModeMenuItem.
		addActionListener(	new ActionListener()
							{
								public void actionPerformed( ActionEvent e )
								{ serEditorModeSelected(); }
							}
						 );

        jposEntryTreePanel.setMainFrame( this );
        
        JPopupMenu popupMenu = new JPopupMenu();

        ButtonGroup treeViewButtonGroup = new ButtonGroup();
        treeViewButtonGroup.add( sortedRBMenuItem );
        treeViewButtonGroup.add( categoryRBMenuItem );
        treeViewButtonGroup.add( manufacturerRBMenuItem );

        treeViewMenu.add( sortedRBMenuItem );
        treeViewMenu.add( categoryRBMenuItem );
        treeViewMenu.add( manufacturerRBMenuItem );
        treeViewMenu.addSeparator();
        treeViewMenu.add( autoExpandTreeMenuItem );
        

        popupMenu.add( treeViewMenu );
        popupMenu.addSeparator();
        popupMenu.add( addJposEntryPopupSubMenu );
        popupMenu.add( copyJposEntryPopupSubMenu );
        popupMenu.add( removeJposEntryPopupSubMenu );
        popupMenu.setBorderPainted( true );

        PopupListener popupListener = new PopupListener();

        jposEntryTreePanel.registerTreeForPopupMenu( popupMenu, popupListener );
        
        loadConfig();
    }

    //-------------------------------------------------------------------------
    // Protected and private methods
    //

    /**
     * @load the editor configuration
     * @since 1.3 (SF 2K meeting)
     * @author KDR (6-2-2000)
     */
    private void loadConfig()
    {
        setLocation( jposEntryEditorConfig.getMainFrameLocation() );
        setSize( jposEntryEditorConfig.getMainFrameSize() );

        hSplit.setDividerLocation( jposEntryEditorConfig.
        						   getMainFrameHDividerLocation() );
        						   
        vSplit.setDividerLocation( jposEntryEditorConfig.
        						   getMainFrameVDividerLocation() );

        getConfigDialog().
        setExpandTreeCheckBox( jposEntryEditorConfig.getExpandTreeCheckBox() );
        
        getConfigDialog().
        setShowAsHexCheckBox( jposEntryEditorConfig.getShowNumbersAsHexCheckBox() );
        
        getConfigDialog().
        setAutoLoadCheckBox( jposEntryEditorConfig.getAutoLoadCheckBox() );
        
        getConfigDialog().
        setLNFRadioButtonSelected( jposEntryEditorConfig.getCurrentLookAndFeel() );
        
        getConfigDialog().
        setCurrentTreeViewButtonSelected( jposEntryEditorConfig.getCurrentTreeView() );

        getConfigDialog().
        setAutoDeleteEntryOnCopy( jposEntryEditorConfig.getAutoDeleteEntryOnCopy() );

        setCurrentTreeView( jposEntryEditorConfig.getCurrentTreeView() );

        setExpandTreeFlag( jposEntryEditorConfig.getExpandTreeCheckBox() );

        setAutoLoadFlag( jposEntryEditorConfig.getAutoLoadCheckBox() );

        setShowAsHexFlag( jposEntryEditorConfig.getShowNumbersAsHexCheckBox() );

        setLookAndFeel( jposEntryEditorConfig.getCurrentLookAndFeel() );

        setAutoDeleteEntryOnCopy( jposEntryEditorConfig.getAutoDeleteEntryOnCopy() );

        autoExpandTreeMenuItem.setState( jposEntryEditorConfig.getExpandTreeCheckBox() );

        jposEntryPanel.initFromConfig();

        if( autoLoad )
            loadJposRegistry();
    }

    /**
     * @save the editor configuration
     * @since 1.3 (SF 2K meeting)
     * @author KDR (6-2-2000)
     */
    void saveConfig()
    {
    	if( configReset ) return;
    	
        jposEntryEditorConfig.setMainFrameLocation( getLocation() );
        jposEntryEditorConfig.setMainFrameSize( getSize() );
        
        jposEntryEditorConfig.
        setMainFrameHDividerLocation( hSplit.getDividerLocation() );
        
        jposEntryEditorConfig.
        setMainFrameVDividerLocation( vSplit.getDividerLocation() );

        jposEntryEditorConfig.
        setCurrentTreeView( jposEntryTreePanel.getCurrentTreeView() );
        
        jposEntryEditorConfig.
        setCurrentLookAndFeel( lookAndFeelSelected );
        
        jposEntryEditorConfig.
        setExpandTreeCheckBox( jposEntryTreePanel.getExpandTreeFlag() );
        
        jposEntryEditorConfig.
        setShowNumbersAsHexCheckBox( jposEntryPanel.getJposEntryView().
        							 getShowNumbersAsHexFlag() );
        							 
        jposEntryEditorConfig.
        setAutoLoadCheckBox( autoLoad );
                
		jposEntryEditorConfig.
		setAutoDeleteEntryOnCopy( getAutoDeleteEntryOnCopy() );

		if( jFileChooser != null )
			jposEntryEditorConfig.
			setFileDialogPath( jFileChooser.
			                   getCurrentDirectory().getAbsolutePath() );

        jposEntryEditorConfig.save();
    }

    /**
     * Initialize the menu items (enable/disable, selected, tooltip-text, ...)
     * @since 0.1 (Philly 99 meeting)
     */
    private void initMenu()
    {
        newFileSubMenu.setEnabled( true );
        newFileButton.setEnabled( true );
        openFileSubMenu.setEnabled( true );
        openFileButton.setEnabled( true );
        saveFileSubMenu.setEnabled( false );
        saveFileButton.setEnabled( false );
        saveAsFileSubMenu.setEnabled( false );
        saveAsFileButton.setEnabled( false );
        loadJposRegistrySubMenu.setEnabled( true );
        saveJposRegistrySubMenu.setEnabled( false );
        closeJposRegistrySubMenu.setEnabled( false );
        closeFileSubMenu.setEnabled( false );
        closeFileButton.setEnabled( false );

        setEditMenuEnabled( false );

        exitSubMenu.setEnabled( true );
        aboutSubMenu.setEnabled( true );
        aboutButton.setEnabled( true );
        configSubMenu.setEnabled( true );
        configButton.setEnabled( true );
        
        newFileSubMenu.setToolTipText( NEWFILEMENU_STRING );
        newFileButton.setToolTipText( NEWFILEBUTTON_STRING );
        openFileSubMenu.setToolTipText( OPENFILEMENU_STRING );
        openFileButton.setToolTipText( OPENFILEBUTTON_STRING );
        saveFileSubMenu.setToolTipText( SAVEFILEMENU_STRING );
        saveFileButton.setToolTipText( SAVEFILEBUTTON_STRING );
        saveAsFileSubMenu.setToolTipText( SAVEASFILEMENU_STRING );
        saveAsFileButton.setToolTipText( SAVEASFILEBUTTON_STRING );
        jposPropertiesSubMenu.setToolTipText( JPOSPROPERTIES_STRING );
        loadJposRegistrySubMenu.setToolTipText( LOADJPOSREGISTRYMENU_STRING );
        saveJposRegistrySubMenu.setToolTipText( SAVEJPOSREGISTRYMENU_STRING );
        closeJposRegistrySubMenu.setToolTipText( CLOSEJPOSREGISTRYMENU_STRING );
        closeFileSubMenu.setToolTipText( CLOSEFILEMENU_STRING );
        closeFileButton.setToolTipText( CLOSEFILEBUTTON_STRING );
        exitSubMenu.setToolTipText( EXITMENU_STRING );
        
        addJposEntrySubMenu.setToolTipText( ADDMENU_STRING );
        addEntryButton.setToolTipText( ADDBUTTON_STRING );
        copyJposEntrySubMenu.setToolTipText( COPYMENU_STRING );
        copyEntryButton.setToolTipText( COPYMENU_STRING );
        removeJposEntrySubMenu.setToolTipText( DELETEMENU_STRING );
        removeEntryButton.setToolTipText( DELETEBUTTON_STRING );

        aboutSubMenu.setToolTipText( ABOUTMENU_STRING );
        aboutButton.setToolTipText( ABOUTBUTTON_STRING );
        configSubMenu.setToolTipText( CONFIGMENU_STRING );
        configButton.setToolTipText( CONFIGBUTTON_STRING );
        
        xmlEditorModeMenuItem.setToolTipText( XML_EDITOR_MODE_TTTEXT_STRING );
        serEditorModeMenuItem.setToolTipText( SERIALIZED_EDITOR_MODE_TTTEXT_STRING );        
    }

    /**
     * Enables/Disables the "Edit" menu
     * @since 0.1 (Philly 99 meeting)
     */
    private void setEditMenuEnabled( boolean b )
    {
        addJposEntrySubMenu.setEnabled( b );
        addJposEntryPopupSubMenu.setEnabled( b );
        addEntryButton.setEnabled( b );
        copyJposEntrySubMenu.setEnabled( b );
        copyJposEntryPopupSubMenu.setEnabled( b );
        copyEntryButton.setEnabled( b );
        removeJposEntrySubMenu.setEnabled( b );
        removeJposEntryPopupSubMenu.setEnabled( b );
        removeEntryButton.setEnabled( b );
    }

    /**
     * Centers the MainFrame using the current screen resolution
     * @since 0.1 (Philly 99 meeting)
     */
    private void centerFrame()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
        setLocation( ( screenSize.width / 2 ) - ( getSize().width / 2 ),
                     ( screenSize.height / 2 ) - ( getSize().height / 2 ) );
    }

    /**
     * @return true if any entry changed
     * @since 0.1 (Philly 99 meeting)
     */
    private boolean isEntriesChanged() { return entriesChanged; }

    /**
     * Adds a JposEntry.  Invoke the JposEntry editor
     * @since 0.1 (Philly 99 meeting)
     */
    private void addJposEntry()
    {
        getAddJposEntryDialog().setModal( true );
        getAddJposEntryDialog().setVisible( true );

        if( !getAddJposEntryDialog().isCanceled() )
        {
            JposEntry entry = getAddJposEntryDialog().getJposEntry();
        
            if( entry != null )
            {
                String logicalName = (String)entry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME );
         
                jposEntryTreePanel.getJposEntryList().add( logicalName, entry );

                jposEntryPanel.clearAll();

                enableCopyDeleteSubMenus( false );

                jposEntryTreePanel.refreshTree();
                setEntriesChanged( true );
            }
        }
    }

    /**
     * Removes a JposEntry
     * @since 0.1 (Philly 99 meeting)
     */
    private void removeJposEntry() 
    { 
    	int selectionCount = jposEntryTreePanel.getSelectionCount();
    	
    	if ( selectionCount <= 1)
    	{
    		removeJposEntry( jposEntryPanel.getJposEntry(), 
		 			   	 DELETEENTRYWITHLOGNAM_STRING + 
					     jposEntryPanel.getJposEntry().
					     getLogicalName() + "?" );
    	}
    	else
    	{
    		removeJposEntries(selectionCount, DELETESELECTEDENTRIES_STRING);
    	} 
	}

    /**
     * Removes a JposEntry
	 * @param entry the JposEntry to remove
	 * @param message the message to ask user when deleting
     * @since 2.0.0
     */
    private void removeJposEntry( JposEntry entry, String message )
    {
        if( entry == null )
        {
            JOptionPane.showMessageDialog( this, SELECTENTRYTODELETE_STRING, 
            							   JPOSENTRYEDITOR_STRING, 
            							   JOptionPane.ERROR_MESSAGE ); 
            return;
        }

        String logicalName = entry.getLogicalName();

        int userAnswer = JOptionPane.showConfirmDialog( this, message, 
        				 JPOSENTRYEDITOR_STRING, JOptionPane.YES_NO_OPTION, 
        				 JOptionPane.QUESTION_MESSAGE ); 

        if( userAnswer == JOptionPane.YES_OPTION )
        {
            jposEntryTreePanel.getJposEntryList().remove( logicalName );

            enableCopyDeleteSubMenus( false );

            jposEntryTreePanel.refreshTree();

            setEntriesChanged( true );
        }
    }
    
    /**
     * Removes multiple JposEntries
     * @param selectionCount number of entries selected
     * @param message the message to ask the user when deleting
     * @author RAR (12-1-2003)
     */
    private void removeJposEntries( int selectionCount, String message )
    {
		int userAnswer = JOptionPane.showConfirmDialog( this, message, 
						JPOSENTRYEDITOR_STRING, JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE ); 
		
		if( userAnswer == JOptionPane.YES_OPTION )
		{						 
    	 	TreePath[] treePath = jposEntryTreePanel.getSelectionPaths();  	 
    	
    		for(int i = 0; i < selectionCount; i++)
    		{
				if (jposEntryTreePanel.isSelectionPathValid(i,treePath))
				{
			    	jposEntryTreePanel.setJposEntryToDelete(i,treePath);	
					String logicalName = jposEntryPanel.getJposEntry().getLogicalName();
					jposEntryTreePanel.getJposEntryList().remove( logicalName );
				}
    		}
			enableCopyDeleteSubMenus( false );
			jposEntryTreePanel.refreshTree();
			setEntriesChanged( true );
		}
    }

    /**
     * copies an existing JposEntry, prompts user for a new logicalName
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-22-2000)
     */
    private void copyJposEntry()
    {
        JposEntry entry = jposEntryPanel.getJposEntry();
		JposEntry oldEntry = entry;

        if( entry == null )
        {
            JOptionPane.showMessageDialog( this, SELECTENTRYTOCOPY_STRING, 
            							   JPOSENTRYEDITOR_STRING, 
            							   JOptionPane.OK_OPTION ); 
            return;
        }

		String currentLogicalName = entry.getLogicalName();

        String logicalNameInput = "";
        boolean logicalNameDifferent = false;

        do
		{
            logicalNameInput = JOptionPane.showInputDialog( this, 
            				   ENTERNEWLOGICALNAME_STRING, 
            				   JPOSENTRYEDITOR_STRING, 
            				   JOptionPane.QUESTION_MESSAGE );

            if( logicalNameInput == null )
                return;
            else 
            if( logicalNameInput.equals( "" ) )
                JOptionPane.showMessageDialog( this, 
			                VALIDNAME_STRING, 
			                JPOSENTRYEDITOR_STRING, 
			                JOptionPane.ERROR_MESSAGE ); 
            else 
            if( jposEntryTreePanel.getJposEntryList().
            					   hasJposEntry( logicalNameInput ) )
                JOptionPane.showMessageDialog( this, 
                			USEDNAME_STRING, 
                			JPOSENTRYEDITOR_STRING, 
                			JOptionPane.ERROR_MESSAGE );
            else
                logicalNameDifferent = true;

        } 
		while( !logicalNameDifferent );

        JposEntry newEntry = new SimpleEntry();

        newEntry.addProperty( JposEntry.LOGICAL_NAME_PROP_NAME, 
        					  logicalNameInput );
        					  
        newEntry.addProperty( JposEntry.SI_FACTORY_CLASS_PROP_NAME, 
        					  entry.getPropertyValue( 
        					  JposEntry.SI_FACTORY_CLASS_PROP_NAME ) );

        Enumeration props = jposEntryPanel.getJposEntryView().
        								   getJposEntryProps();

        while( props.hasMoreElements() )
        {
            JposEntryProp prop = (JposEntryProp)props.nextElement();
            newEntry.addProperty( prop.getName(), prop.getValue() );
        }
            
        jposEntryTreePanel.getJposEntryList().add( logicalNameInput, newEntry );

        jposEntryPanel.clearAll();

        enableCopyDeleteSubMenus( false );

        jposEntryTreePanel.refreshTree();
        setEntriesChanged( true );

		if( getAutoDeleteEntryOnCopy() )
			removeJposEntry( oldEntry, DELETE_OLD_ENTRY_WITH_LOGICAL_NAME_MSG + 
							 oldEntry.getLogicalName() + "?" );
    }

    /**
     * Do some finalization before exiting
     * <p>
     * NOTE: this no longer call System.exit( 0 ) since the exit policy can 
     * be set by setDefaultCloseOperation
     * </p>
     * @see javax.swing.JFrame#setDefaultCloseOperation
     * @since 0.1 (Philly 99 meeting)
     */
    private void exit()
    {
        saveConfig();

        if( isEntriesChanged() )
        {
            int userAnswer = JOptionPane.
            				 showConfirmDialog( this, SAVECURRENTCHANGES_STRING, 
            				 					JPOSENTRYEDITOR_STRING, 
                                                JOptionPane.YES_NO_OPTION, 
                                                JOptionPane.QUESTION_MESSAGE ); 

            if( userAnswer == JOptionPane.YES_OPTION )
                if( registryMode )
                    saveJposRegistry();
                else
                    saveFile();
        }

        //NOTE: hard coded "3" value is because Swing does not define 
        //EXIT_ON_CLOSE (Why?  Ask Sun and then tell me...)
        if( getDefaultCloseOperation() == 3 )
            System.exit( 0 );
        else
        if( getDefaultCloseOperation() == JFrame.HIDE_ON_CLOSE )
            setVisible( false );
        else
        if( getDefaultCloseOperation() == JFrame.DISPOSE_ON_CLOSE )
            this.dispose();
    }

    /**
     * Called when the "File" -> "New" is selected
     * @since 0.1 (Philly 99 meeting)
     */
    private void newFile()
    {
        if( jposEntryTreePanel.getJposEntryList() != null )
            closeFile();

        jposEntryPanel.clearAll();
        JposEntryList jposEntryList = new JposEntryList();
        
        if( xmlEditorMode )
        	jposEntryList.setRegPopulator( xmlRegPopulator );
        else
        	jposEntryList.setRegPopulator( serRegPopulator );
        
        jposEntryTreePanel.setJposEntryList( jposEntryList );

        updateTitle( MAINFRAME_TITLE + "- [NewFile<" + (++newFileCount) + ">]" );

        newFileSubMenu.setEnabled( false );
        newFileButton.setEnabled( false );
        openFileSubMenu.setEnabled( false );
        openFileButton.setEnabled( false );
        saveFileSubMenu.setEnabled( true );
        saveFileButton.setEnabled( true );
        saveAsFileSubMenu.setEnabled( true );
        saveAsFileButton.setEnabled( true );
        loadJposRegistrySubMenu.setEnabled( false );
        saveJposRegistrySubMenu.setEnabled( false );
        closeJposRegistrySubMenu.setEnabled( false );
        closeFileSubMenu.setEnabled( true );
        closeFileButton.setEnabled( true );

        addJposEntrySubMenu.setEnabled( true );
        addJposEntryPopupSubMenu.setEnabled( true );
        addEntryButton.setEnabled( true );
        copyJposEntrySubMenu.setEnabled( true );
        copyJposEntryPopupSubMenu.setEnabled( true );
        copyEntryButton.setEnabled( true );
        removeJposEntrySubMenu.setEnabled( false );
        removeJposEntryPopupSubMenu.setEnabled( false );
        removeEntryButton.setEnabled( false );

        setEntriesChanged( false );
        noFile = false;
    }

    /**
     * Called when "File" -> "Open" is selected
     * @since 0.1 (Philly 99 meeting)
     */
    private void openFile()
    {
        Cursor oldCursor = getCursor();
        setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );

        JFileChooser fileChooser = getJFileChooser();

        fileChooser.setDialogTitle( OPEN_STRING );
        fileChooser.setDialogType( JFileChooser.OPEN_DIALOG );
		
		fileChooser.resetChoosableFileFilters();
		fileChooser.setAcceptAllFileFilterUsed( false );
        fileChooser.addChoosableFileFilter( getJposEntryEditorFileFilter() );

        if( fileChooser.showOpenDialog( this ) != JFileChooser.CANCEL_OPTION )
        {
            File selectedFile = fileChooser.getSelectedFile();

            if( selectedFile == null ) 
            {
            	setCursor( oldCursor );
            	return; 
            }

            try
            {
                JposEntryList jposEntryList = new JposEntryList();                

		        if( xmlEditorMode )
		        	jposEntryList.setRegPopulator( xmlRegPopulator );
		        else		        
		        	jposEntryList.setRegPopulator( serRegPopulator );
                                
                jposEntryList.load( selectedFile.getAbsolutePath() );

				if( !xmlEditorMode && jposEntryList.size() == 0 )
					throw new Exception( INVALID_SER_FILE_MSG );

                jposEntryPanel.clearAll();
                                                
                jposEntryTreePanel.setJposEntryList( jposEntryList );
            
                updateTitle( MAINFRAME_TITLE + "- [" + 
                			 jposEntryList.getEntriesFileName() + "]" );
    
                newFileSubMenu.setEnabled( false );
                newFileButton.setEnabled( false );    
                openFileSubMenu.setEnabled( false );
                openFileButton.setEnabled( false );
                saveFileSubMenu.setEnabled( true );
                saveFileButton.setEnabled( true );
                saveAsFileSubMenu.setEnabled( true );
                saveAsFileButton.setEnabled( true );
                loadJposRegistrySubMenu.setEnabled( false );
                saveJposRegistrySubMenu.setEnabled( false );
                closeJposRegistrySubMenu.setEnabled( false );
                closeFileSubMenu.setEnabled( true );
                closeFileButton.setEnabled( true );
            
                addJposEntrySubMenu.setEnabled( true );
                addJposEntryPopupSubMenu.setEnabled( true );
                addEntryButton.setEnabled( true );
                copyJposEntrySubMenu.setEnabled( false );
                copyJposEntryPopupSubMenu.setEnabled( false );
                copyEntryButton.setEnabled( false );
                removeJposEntrySubMenu.setEnabled( false );
                removeJposEntryPopupSubMenu.setEnabled( false );
                removeEntryButton.setEnabled( false );
            
                setEntriesChanged( false );
    
                if( jposEntryList.size() > 0 ) 
                {
					Object[] args = { jposEntryList.getEntriesFileName(), 
						              new Integer( jposEntryList.size() ) }; 
                
					println( MessageFormat.format( OPENFILE_STRING, args ) );
    
                    jposEntryTreePanel.refreshTree();
                    jposEntryTreePanel.setTreeRootVisible( true );
                    jposEntryTreePanel.expandTree();
                }
                else
					println( NOENTRIES_STRING + 
					         jposEntryList.getEntriesFileName() + "." );
            }
            catch( Exception e )
            {
            	String msg = ( xmlEditorMode ? ERROR_OPENING_ENTRIES_FILE_MSG :
            								   INVALID_SER_FILE_MSG );
            	
				JOptionPane.
				showMessageDialog( this, msg, 
								   ERROR_MESSAGE_TITLE_STRING, 
								   JOptionPane.ERROR_MESSAGE );

				println( ERROR_STRING + selectedFile.getAbsolutePath() );
			}
        }

        setCursor( oldCursor );
        noFile = false;
    }

    /**
     * Called when "File" -> "Close" is selected
     * @since 0.1 (Philly 99 meeting)
     */
    private void closeFile()
    {   
        setClosing( true );
        
        jposEntryPanel.closingJposRegistryOrFile();

        if( isEntriesChanged() && !isSaved() ) 
        {
            int usrAns = JOptionPane.showConfirmDialog( this, 
                         CLOSECHANGES_STRING, JPOSENTRYEDITOR_STRING, 
                         JOptionPane.YES_NO_OPTION, 
                         JOptionPane.QUESTION_MESSAGE );

            if( usrAns == JOptionPane.YES_OPTION )
                saveFile();
            else 
                println( CLOSEDFILE_STRING );
        }


        if( jposEntryTreePanel.getJposEntryList().getEntriesFileName() != "" )
        {
            Object[] args = { jposEntryTreePanel.getJposEntryList().
            	              getEntriesFileName(), 
            	              new Integer( jposEntryTreePanel.
            	              getJposEntryList().size() ) }; 
                
            println( MessageFormat.format( CLOSEDFILEWITHCHANGES_STRING, args ) );
            
        }

		jposEntryPanel.setJposEntry( null );
        jposEntryPanel.clearAll();
        jposEntryPanel.setEnabledAll( false );
            
        jposEntryTreePanel.setJposEntryList( null );
        setEditMenuEnabled( false );

        updateTitle( MAINFRAME_TITLE );

        initMenu();

        setEntriesChanged( false );

        jposEntryTreePanel.setTreeRootVisible( false );
        
        setClosing( false );
        noFile = true;
    }

    /**
     * Called when "File" -> "Save" is selected 
     * @since 0.1 (Philly 99 meeting)
     */
    private void saveFile()
    {
        if( !jposEntryTreePanel.getJposEntryList().isLoadedFromFile() )
        {
            saveAsFile();
            return;
        }

        Cursor oldCursor = getCursor();
        setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );

        try
        {
            JposEntryList jposEntryList = jposEntryTreePanel.getJposEntryList();

            purgeJposEntryList( jposEntryList );

            jposEntryList.save();

            Object[] args = { jposEntryList.getEntriesFileName(), 
            	              new Integer( jposEntryList.size() ) }; 
                
            println( MessageFormat.format( SAVEDFILE_STRING, args ) );

            updateTitle( MAINFRAME_TITLE + 
                         "- [" + jposEntryList.getEntriesFileName() + "]" );
            setSaved( true );
        }
        catch( Exception e )
        { 
        	showErrorMessageToUser( ERROR_SAVING_ENTRIES_TO_FILE_MSG + ": " 
        							+ e.getMessage() );
        	
        	println( ERRORSAVING_STRING + jposEntryTreePanel.getJposEntryList().
        	       getEntriesFileName() ); 
       	}

        setCursor( oldCursor );
    }
    
    /** 
     * @return true if File exist and user wants to overrite, false otherwise
     * @param fileName the file name 
     */
    private boolean checkIfFileShouldBeOverwritten( String fileName )
    {
    	File file = new File( fileName );
    	
    	if( file.exists() )
    	{
            int userAnswer = JOptionPane.
                             showConfirmDialog( this, OVERWRITE_FILE_STRING, 
                             				    JPOSENTRYEDITOR_STRING, 
                             				    JOptionPane.YES_NO_OPTION, 
                             				    JOptionPane.QUESTION_MESSAGE ); 
    
            return ( userAnswer == JOptionPane.YES_OPTION );    		
    	}
    	
    	return true;
    }

	/** 
	 * Show an error message to the user
	 * @param msg the error message to the user  
	 */
	private void showErrorMessageToUser( String msg )
	{
		JOptionPane.showMessageDialog( this, msg, JPOSENTRYEDITOR_STRING,
									   JOptionPane.ERROR_MESSAGE ); 
	}

    /**
     * Called when "File" -> "Save as" is selected
     * @since 0.1 (Philly 99 meeting)
     */
    private void saveAsFile()
    {
        if( jposEntryTreePanel.getJposEntryList() == null ) return;

        Cursor oldCursor = getCursor();
        setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );

        JFileChooser fileChooser = getJFileChooser();

        fileChooser.setDialogTitle( SAVEAS_STRING );
        fileChooser.setDialogType( JFileChooser.SAVE_DIALOG );

		fileChooser.resetChoosableFileFilters();
		fileChooser.setAcceptAllFileFilterUsed( false );
        fileChooser.addChoosableFileFilter( getJposEntryEditorFileFilter() );

        fileChooser.setSelectedFile( null );

        if( fileChooser.showSaveDialog( this ) != JFileChooser.CANCEL_OPTION )
        {
            File selectedFile = fileChooser.getSelectedFile();
        
            if( selectedFile == null ) 
            {
                int userAnswer = JOptionPane.
                                 showConfirmDialog( this, SAVECHANGES_STRING, 
                                 				    JPOSENTRYEDITOR_STRING, 
                                 				    JOptionPane.YES_NO_OPTION, 
                                 				    JOptionPane.QUESTION_MESSAGE ); 
        
                if( ( userAnswer == JOptionPane.YES_OPTION ) && 
                    ( fileChooser.getSelectedFile() != null ) )
                    saveAsFile();
                else
                {
                    jposEntryPanel.clearAll();
                    jposEntryTreePanel.setJposEntryList( null );
                    initMenu();
                    setEditMenuEnabled( false );
                    updateTitle( MAINFRAME_TITLE );
                    return;
                }
            }
        
			String selectedFileName = selectedFile.getAbsolutePath();

			boolean xmlMode = ( registryMode ? isEditorInXmlMode() : xmlEditorMode );
												
			if( xmlMode )
				selectedFileName = ( selectedFileName.
									 substring( selectedFileName.length() - 4 ).
									 equalsIgnoreCase( XML_EXTENSION_STRING ) ? 
									 selectedFileName : selectedFileName + 
									                    XML_EXTENSION_STRING );
			else
				selectedFileName = ( selectedFileName.
									 substring( selectedFileName.length() - 4 ).
									 equalsIgnoreCase( CFG_EXTENSION_STRING ) ? 
									 selectedFileName : selectedFileName + 
									                    CFG_EXTENSION_STRING );

			if( !checkIfFileShouldBeOverwritten( selectedFileName ) )
			{
		        setCursor( oldCursor );
				return;
			}

            jposEntryTreePanel.getJposEntryList().
            setEntriesFileName( selectedFileName );
            
            try
            {
                JposEntryList jposEntryList = jposEntryTreePanel.
                							  getJposEntryList();

                purgeJposEntryList( jposEntryList );

                jposEntryList.save();
            
                Object[] args = { jposEntryList.getEntriesFileName(), 
                				  new Integer( jposEntryList.size() ) }; 
                
                println( MessageFormat.format( SAVEDFILE_STRING, args ) );
    
                updateTitle( MAINFRAME_TITLE + 
                		     "- [" + jposEntryList.getEntriesFileName() + "]" );

                setSaved( true );
				setEntriesChanged( false );
            
                addJposEntrySubMenu.setEnabled( true );
            }
            catch( Exception e )
            {
				showErrorMessageToUser( ERROR_SAVING_ENTRIES_TO_FILE_MSG + ": " 
										+ e.getMessage() );
            	 
            	println( ERRORSAVING_STRING + jposEntryTreePanel.
            	       getJposEntryList().getEntriesFileName() ); 
           	}
        }
        else
        {
            if( isClosing() )
            {
                int userAnswer = JOptionPane.
                                 showConfirmDialog( this,CHANGESLOST_STRING , 
                                                    JPOSENTRYEDITOR_STRING, 
                                                    JOptionPane.YES_NO_OPTION, 
                                                    JOptionPane.WARNING_MESSAGE ); 
        
                if( userAnswer == JOptionPane.NO_OPTION )
                    saveAsFile();
            }
        }

        setCursor( oldCursor );
    }

    /**
     * Called when "File" -> "JposProperties..." is selected
     * @since 1.3 (Washington DC 2001 meeting)
     */
	private void jposProperties()
	{
		getPropertiesEditorFrame().setProperties( JposServiceLoader.getManager().
		                                          getProperties() );
		getPropertiesEditorFrame().setVisible( true );
	}

    /**
     * Called when "File" -> "Save JposEntryRegistry As..." is selected
     * @since 2.1.0
     */
    private void saveJposRegistryAs() 
    { 
    	saveAsFile(); 
    	
        Cursor oldCursor = getCursor();
        setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
		
		if( isEntriesChanged() ) saveJposRegistry();

		loadJposRegistry();
		
		setCursor( oldCursor );
    }

    /**
     * Called when "File" -> "Save JposEntryRegistry" is selected
     * @since 0.1 (Philly 99 meeting)
     */
    private void saveJposRegistry()
    {
        JposEntryRegistry registry = JposServiceLoader.getManager().
                                     getEntryRegistry();

		jposEntryPanel.tabbedPaneStateChanged();

        try
        {
            JposEntryList jposEntryList = jposEntryTreePanel.getJposEntryList();

            Enumeration deletedEntries = jposEntryList.getRemovedEntries();

            while( deletedEntries.hasMoreElements() )
            {
                JposEntry entry = (JposEntry)deletedEntries.nextElement();

                String logicalName = (String)entry.
                                     getPropertyValue( JposEntry.
                                                       LOGICAL_NAME_PROP_NAME );
                Object value = entry.getPropertyValue( logicalName );

                if( registry.hasJposEntry( logicalName ) )
                    registry.removeJposEntry( logicalName );
            }

            Enumeration entries = jposEntryList.getEntries();

            while( entries.hasMoreElements() )
            {
                JposEntry entry = (JposEntry)entries.nextElement();

                String logicalName = (String)entry.
                                     getPropertyValue( JposEntry.
                                                       LOGICAL_NAME_PROP_NAME );

                Object value = entry.getPropertyValue( logicalName );

                if( verifyEntryValidity( entry ) == false )
                {
                    jposEntryTreePanel.getJposEntryList().remove( logicalName );
                    jposEntryTreePanel.refreshTree();
                    continue;
                }

                if( registry.hasJposEntry( logicalName ) )
                {
                    if( !registry.getJposEntry( logicalName ).equals( entry ) )
                        registry.modifyJposEntry( logicalName, entry );
                }               
                else
                    registry.addJposEntry( logicalName, entry );
            }

			//<temp>Need to remove jposRegistryEntriesUrl and make RegPopulator do the job</temp>            
            try
            {
            	if( jposRegistryEntriesUrl != null &&
            		!jposRegistryEntriesUrl.equals( "" ) )
            		registry.saveToFile( new File( new java.net.URL( jposRegistryEntriesUrl ).getFile() ) );
            	else
            		registry.save();
            }
            catch( Exception e )
            {
            	tracer.println( "Exception while saving JposEntryRegistry exception.message=" +
            					e.getMessage() );
            }
            //</temp>
        }
        catch( Exception e )
        { 
			showErrorMessageToUser( ERROR_SAVING_ENTRIES_TO_FILE_MSG + ": " 
									+ e.getMessage() );
        	
        	JOptionPane.showMessageDialog( this, ERRORSAVINGJPOSREGISTRY_STRING, 
        	                             JPOSENTRYEDITOR_STRING, 
        	                             JOptionPane.OK_OPTION );
			tracer.println( ERRORSAVINGJPOSREGISTRY_STRING );
       	}

        registryMode = true;
        setEntriesChanged( false );
        JposEntryList jposEntryList = jposEntryTreePanel.getJposEntryList();
        
        Object[] args = { new Integer( jposEntryList.size() ) }; 
                
        println( MessageFormat.format( SAVEDCHANGESTOREGISTRY_STRING, args ) );
    }

    /**
     * Called when "File" -> "Close JposEntryRegistry" is selected
     * @since 0.1 (Philly 99 meeting)
     */
    private void closeJposRegistry()
    {
    	jposEntryPanel.closingJposRegistryOrFile();
    	
        if( isEntriesChanged() )
        {
            int userAnswer = JOptionPane.showConfirmDialog( this, 
                             SAVECHANGESJPOSREGISTRY_STRING, 
                             JPOSENTRYEDITOR_STRING, 
                             JOptionPane.YES_NO_OPTION, 
                             JOptionPane.QUESTION_MESSAGE ); 

            if( userAnswer == JOptionPane.YES_OPTION )
                saveJposRegistry();
        }

        newFileSubMenu.setEnabled( true );
        newFileButton.setEnabled( true );
        openFileSubMenu.setEnabled( true );
        openFileButton.setEnabled( true );
        saveFileSubMenu.setEnabled( false );
        saveFileButton.setEnabled( false );
        saveAsFileSubMenu.setEnabled( false );
        saveAsFileButton.setEnabled( false );
        
        loadJposRegistrySubMenu.setEnabled( true );
        saveJposRegistrySubMenu.setEnabled( false );
        saveJposRegistryAsSubMenu.setEnabled( false );
        closeJposRegistrySubMenu.setEnabled( false );

        closeFileButton.setEnabled( false );

        setEditMenuEnabled( false );

		jposEntryPanel.setJposEntry( null );
        jposEntryPanel.clearAll();
        jposEntryPanel.setEnabledAll( false );
        jposEntryTreePanel.setJposEntryList( null );

        registryMode = false;
        
		xmlEditorModeMenuItem.setEnabled( true );
		serEditorModeMenuItem.setEnabled( true );        
        
        setEntriesChanged( false );
        
        setTitle( MAINFRAME_TITLE );
        
        if( xmlEditorMode )
			xmlEditorModeMenuItem.setSelected( true );
		else
			serEditorModeMenuItem.setSelected( true );
        
		updateTitle( getTitle() );

        println( CLOSEDJPOSREGISTRY_STRING );

        jposEntryTreePanel.setTreeRootVisible( false );
    }

    /**
     * Called when "File" -> "Load JposEntryRegistry" is selected
     * @since 0.1 (Philly 99 meeting)
     */
    private void loadJposRegistry()
    {
        newFileSubMenu.setEnabled( false );
        newFileButton.setEnabled( false );
        openFileSubMenu.setEnabled( false );
        openFileButton.setEnabled( false );
        saveFileSubMenu.setEnabled( false );
        saveFileButton.setEnabled( false );
        saveAsFileSubMenu.setEnabled( false );
        saveAsFileButton.setEnabled( false );

        loadJposRegistrySubMenu.setEnabled( false );
        saveJposRegistrySubMenu.setEnabled( true );
        saveJposRegistryAsSubMenu.setEnabled( true );        
        closeJposRegistrySubMenu.setEnabled( true );

        closeFileSubMenu.setEnabled( false );
        closeFileButton.setEnabled( true );

        JposEntryRegistry registry = JposServiceLoader.getManager().
        							 getEntryRegistry();
        							 
        JposEntryList jposEntryList = new JposEntryList();
    
		registry.load();

        Enumeration entries = registry.getEntries();

        while( entries.hasMoreElements() )
        {
            JposEntry entry = (JposEntry)entries.nextElement();

            if( entry.hasPropertyWithName( JposEntry.LOGICAL_NAME_PROP_NAME ) )
                jposEntryList.add( entry.getLogicalName(), entry );
        }

        addJposEntrySubMenu.setEnabled( true );
        addJposEntryPopupSubMenu.setEnabled( true );
        addEntryButton.setEnabled( true );
        copyJposEntrySubMenu.setEnabled( false );
        copyJposEntryPopupSubMenu.setEnabled( false );
        copyEntryButton.setEnabled( false );
        removeJposEntrySubMenu.setEnabled( false );
        removeJposEntryPopupSubMenu.setEnabled( false );
        removeEntryButton.setEnabled( false );

        jposEntryPanel.clearAll();
                
        jposEntryTreePanel.setJposEntryList( jposEntryList );

        registryMode = true;

		xmlEditorModeMenuItem.setEnabled( false );
		serEditorModeMenuItem.setEnabled( false );

        if( jposEntryList.size() > 0 ) 
        {
            Object[] args = { new Integer( jposEntryList.size() ) }; 
                
            println( MessageFormat.format( LOADENTRIESJPOSREGISTRY_STRING, args ) );

            jposEntryTreePanel.refreshTree();
            jposEntryTreePanel.setTreeRootVisible( true );
            jposEntryTreePanel.expandTree();
        }
        else
            println( NOENTRIESJPOSREGISTRY_STRING );

		String entriesUrl = registry.getRegPopulator().
							getEntriesURL().toExternalForm();
							
		//<temp>This needs to be fixed correctly in the RegPopulator</temp>
		jposRegistryEntriesUrl = updateEntriesUrl( entriesUrl );
		//</temp>							

        setTitle( MAINFRAME_TITLE + "- [JposEntryRegistry]" + " - " + 
        		  jposRegistryEntriesUrl );
    }
    
    //<temp>
    /**
     * Updates the entriesUrl String to contain the full path for the loaded 
     * entries
     * @param entriesUrlString the entries URL String returned by the RegPopulator
     */
    private String updateEntriesUrl( String entriesUrlString )
    {
    	String newEntriesUrlString = entriesUrlString;
    	try
    	{
			String defaultEntriesFileName = SimpleRegPopulator.
											DEFAULT_JPOS_SER_FILE_NAME;
    		
    		JposRegPopulator populator = JposServiceLoader.getManager().
        							     getEntryRegistry().getRegPopulator();

    		if( populator instanceof XmlRegPopulator )
				defaultEntriesFileName = XmlRegPopulator.DEFAULT_XML_FILE_NAME;    		
    		
	        JposProperties jposProperties = JposServiceLoader.
        								    getManager().getProperties();

	        if( jposProperties.
	        	isPropertyDefined( JposProperties.JPOS_POPULATOR_FILE_PROP_NAME ) )
	            defaultEntriesFileName = jposProperties.
	            					getPropertyString( JposProperties.
	            					JPOS_POPULATOR_FILE_PROP_NAME );
	        else
	        if( jposProperties.
	        	isPropertyDefined( JposProperties.JPOS_POPULATOR_FILE_URL_PROP_NAME ) )	        
	            defaultEntriesFileName = jposProperties.
	            					getPropertyString( JposProperties.
	            					JPOS_POPULATOR_FILE_URL_PROP_NAME );        	
    		
			File file = FileUtil.findFile( defaultEntriesFileName, true );
			newEntriesUrlString = file.toURL().toExternalForm();
    	}
    	catch( Exception e )
    	{
    		tracer.println( "Error updating entries URL Exception.message = " + 
    						e.getMessage() );
    	}
		
		return newEntriesUrlString;
    }
    //</temp>

    /**
     * @return true if this entry is a valid and verified entry
     * @param entry the JposEntry
     * @since 1.3 (Tokyo 2001 meeting)
     */
    private boolean verifyEntryValidity( JposEntry entry )
    {
        if( JposEntryUtility.isValidJposEntry( entry ) )
            return true;

        getInvalidEntryDialog().setInvalidEntry( entry );
        getInvalidEntryDialog().setModal( true );
        getInvalidEntryDialog().setVisible( true );

        if( getInvalidEntryDialog().isSaveWithDefaultsButtonSelected() )
        {
            JposEntryUtility.addMissingRequiredProps( entry );
            return true;
        }

        return false;
    }

    /**
     * Purges the JposEntryList passed of all invlid entries asking user
     * @param jposEntryList the JposEntryList object
     */
    private void purgeJposEntryList( JposEntryList jposEntryList )
    {
        Enumeration entries = jposEntryList.getEntries();

        while( entries.hasMoreElements() )
        {
            JposEntry entry = (JposEntry)entries.nextElement();

            String logicalName = (String)entry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME );
            Object value = entry.getPropertyValue( logicalName );

            if( verifyEntryValidity( entry ) == false )
            {
                jposEntryTreePanel.getJposEntryList().remove( logicalName );
                jposEntryTreePanel.refreshTree();
                continue;
            }
        }
    }

    /**
     * Called when the "Help" -> "About" is selected
     * @since 0.1 (Philly 99 meeting)
     */
    private void about() { getAboutDialog().setVisible( true ); }

    /**
     * Called when the "Edit" -> "Preferences" is selected
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-5-2000)
     */
    private void configDialog() { getConfigDialog().setVisible( true ); }

    /**
     * Called when the "Edit" -> "JposEntry Merger..." is selected
     * @since 1.3 (SF 2K meeting)
     */
    private void jposEntryMergerDialog() { getJposEntryMergerDialog().setVisible( true ); }

    /**
     * @return the AboutDialog creating it if necessary
     * @since 0.1 (Philly 99 meeting)
     */
    private JDialog getAboutDialog()
    {
        if( aboutDialog == null )
            aboutDialog = new AboutDialog( this );
        
        return aboutDialog;
    }

    /**
     * @return the JposEntryEditorConfigDialog creating it if necessary
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-5-2000)
     */
    public JposEntryEditorConfigDialog getConfigDialog()
    {
        if( configDialog == null )
            configDialog = new JposEntryEditorConfigDialog( null );

        configDialog.setMainFrame( this );

        return configDialog;
    }

    /**
     * @return the JposEntryMergerDialog creating it if necessary
     * @since 1.3 (SF 2K meeting)
     */
    public JposEntryMergerDialog getJposEntryMergerDialog()
    {
        if( jposEntryMergerDialog == null )
            jposEntryMergerDialog = new JposEntryMergerDialog( this );

        return jposEntryMergerDialog;
    }

    /**
     * @return the a reusable JFileChooser creating it if necessary
     * NOTE: need to do such lazy init for JFileDialog in Swing 1.1 are hoggs...
     * @since 0.1 (Philly 99 meeting)
     */
    private JFileChooser getJFileChooser()
    {
        if( jFileChooser == null )
		{
            jFileChooser = new JFileChooser();

			File defaultPath = null;

			try{ defaultPath = new File( jposEntryEditorConfig.getFileDialogPath() ); }
			catch( Exception e ) 
			{ 
				defaultPath = new File( "" );
				tracer.println( "getJFileChooser: Exception.message = " +
								e.getMessage() );
			}

			jFileChooser.setCurrentDirectory( defaultPath );
		}

        return jFileChooser;
    }

	/**
	 * @return a lazily created PropertiesEditorFrame
	 * @since 1.3 (Washington DC 2001)
	 */
	private PropertiesEditorFrame getPropertiesEditorFrame()
	{
		if( propEditorFrame == null )
			propEditorFrame = new PropertiesEditorFrame();

		return propEditorFrame;
	}

    /**
     * Called when "Edit" -> "New JposEntry" selected
     * @since 0.1 (Philly 99 meeting)
     */
    private void newJposEntrySelected( JposEntryTreeEvent event ) 
    {
        JposEntry entry = event.getJposEntry();

		if( entry != null )
		{
			jposEntryPanel.tabbedPaneStateChanged();
			
			jposEntryPanel.setJposEntry( entry );

			copyJposEntrySubMenu.setEnabled( true );
			copyJposEntryPopupSubMenu.setEnabled( true );
			copyEntryButton.setEnabled( true );
			removeJposEntrySubMenu.setEnabled( true );
			removeJposEntryPopupSubMenu.setEnabled( true );
			removeEntryButton.setEnabled( true );
		}
		else
			jposEntryPanel.clearAll();
    }

    /**
     * @return the getEditJposEntryDialog() instance, creating it if necesary
     * @version 1.2 (NYC 2K meeting)
     */
    private EditJposEntryDialog getEditJposEntryDialog()
    {
        if( editJposEntryDialog == null )
            editJposEntryDialog = new EditJposEntryDialog( this );

        return editJposEntryDialog;
    }

    /**
     * @return the AddJposEntryDialog instance, creating it if necesary
     * @version 1.3 (Washington DC 2001 meeting)
     */
    private AddJposEntryDialog getAddJposEntryDialog()
    {
        if( addJposEntryDialog == null )
            addJposEntryDialog = new AddJposEntryDialog( this );

        return addJposEntryDialog;
    }

    /** 
     * Called if a new LnF is selected different from the current
     * @since 1.2 (NYC 2K meeting)
     */
    private void newLnFSelected()
    {
        String newLnFClassName = null;

        if( lookAndFeelSelected == JposEntryEditorConfig.JAVALNF && isCurrentLnFMetal() )
            return;
        
        if( lookAndFeelSelected == JposEntryEditorConfig.JAVALNF )
        {
            String metalLnFClassName = UIManager.getCrossPlatformLookAndFeelClassName();
            newLnFClassName = metalLnFClassName;
        }
        else
        {
            String nativeLnFClassName = UIManager.getSystemLookAndFeelClassName();
            newLnFClassName = nativeLnFClassName;
        }
        try { UIManager.setLookAndFeel( newLnFClassName ); }
        catch ( Exception e ) 
        { 
        	tracer.println( "newLnFSelected: Exception.message=" + 
        					e.getMessage() );
		}

        updateLookAndFeel();
    }

    /**
     * @return true if the current LnF is metal
     * @since 1.2 (NYC 2K meeting)
     */
    private boolean isCurrentLnFMetal()
    { return ( UIManager.getLookAndFeel().getID().equals( METAL_STRING ) ); }

    /**
     * sets the <code>saved</code> flag
     * @param b the boolean parameter
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-15-2000)
     */
    private void setSaved( boolean b ) { saved = b; }

    /**
     * @return true if <code>saved</code> is true, false otherwise
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-15-2000)
     */
    private boolean isSaved() { return saved; }

    /**
     * sets the <code>closing</code> flag
     * @param b the boolean parameter
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-15-2000)
     */
    private void setClosing( boolean b ) { closing = b; }

    /**
     * @return true if <code>closing</code> is true, false otherwise
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-15-2000)
     */
    private boolean isClosing() { return closing; }
                              
    /**
     * changes the current tree view, updates the config dialog windows
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-23-2000)
     */
    private void changeTreeView( int currentView )
    {
        setCurrentTreeView( currentView );
        jposEntryEditorConfig.setCurrentTreeView( currentView );
        getConfigDialog().setCurrentTreeViewButtonSelected( currentView );
    }

    /**
     * sets the autoExpandTreeFlag, and changes the display if necessary
     * @author MMM (6-23-2000)
     * @param b the boolean param
     */
    private void autoExpandTreeSelected( boolean b )
    {
        setExpandTreeFlag( b );
        jposEntryEditorConfig.setExpandTreeCheckBox( b );
        getConfigDialog().setExpandTreeCheckBox( b );
        jposEntryTreePanel.refreshTree();
    }

    /**
     * @return the InvalidEntryDialog creating it if necessary
     * @since 1.3 (Tokyo 2001 meeting)
     */
    private InvalidEntryDialog getInvalidEntryDialog()
    {
        if( invalidEntryDialog == null )
            invalidEntryDialog = new InvalidEntryDialog( this );

        return invalidEntryDialog;
    }

	/** @return a formated string for the editor mode e.g. [XML] */
	private String getEditorModeSubtitleString()
	{
		if( xmlEditorMode ) 
			return XML_EDITOR_MODE_TITLE_STRING;
		else
			return SER_EDITOR_MODE_TITLE_STRING;
	}
	
	/**
	 * Update the editor title with correct editor mode
	 * @param title the title to update
	 * @since 2.1.0
	 */
	private void updateTitle( String title )
	{
		String editorModeSubtitle = getEditorModeSubtitleString();
	
		String finalTitle = title;
		
		if( title.endsWith( XML_EDITOR_MODE_TITLE_STRING ) )
			finalTitle = finalTitle.substring( 0, title.length() - 
			                                   XML_EDITOR_MODE_TITLE_STRING.length() );
		else
		if( title.endsWith( SER_EDITOR_MODE_TITLE_STRING ) )
			finalTitle = finalTitle.substring( 0, title.length() - 
			                                   SER_EDITOR_MODE_TITLE_STRING.length() );				                            
					
		setTitle( ( finalTitle.endsWith( " " ) ? finalTitle : ( finalTitle + " " ) )
		          + editorModeSubtitle );		
	}

	/** @return true if user really want to switch editor mode and close current file */
	private boolean okToSwithEditorMode()
	{
        int userAnswer = JOptionPane.
        				  showConfirmDialog( this, SWITCH_EDITOR_MODE_MSG, 
        							         JPOSENTRYEDITOR_STRING, 
        							         JOptionPane.YES_NO_OPTION, 
        							         JOptionPane.QUESTION_MESSAGE ); 

        if( userAnswer == JOptionPane.YES_OPTION )
			return true;

		return false;
	}

	/**
	 * Called when the "Editor mode" -> "XML" is selected
	 * @since 2.1.0
	 */
	private void xmlEditorModeSelected()
	{
		if( noFile == false && okToSwithEditorMode() == false )
		{
			serEditorModeMenuItem.setSelected( true );
			return;
		}
		
		closeFile();
		
		jposEntryTreePanel.getJposEntryList().
			   		       setRegPopulator( xmlRegPopulator );

		xmlEditorMode = true;
		getJposEntryEditorFileFilter().setMode( JposEntryEditorFileFilter.XML_MODE );
		updateTitle( getTitle() );		
	}

	/**
	 * Called when the "Editor mode" -> "Serialized" is selected
	 * @since 2.1.0
	 */
	private void serEditorModeSelected()
	{
		if( noFile == false && okToSwithEditorMode() == false )
		{
			xmlEditorModeMenuItem.setSelected( true );
			return;
		}
		
		closeFile();
		
		jposEntryTreePanel.getJposEntryList().
			   		       setRegPopulator( serRegPopulator );			   		       
		
		xmlEditorMode = false;
		getJposEntryEditorFileFilter().setMode( JposEntryEditorFileFilter.SER_MODE );		
		updateTitle( getTitle() );		
	}

    //-------------------------------------------------------------------------
    // Package methods
    //

    /**
     * Sets the entriesChanged flag and puts a * in the frame's title to indicate that
     * @since 0.1 (Philly 99 meeting)
     */
    void setEntriesChanged( boolean b ) 
    {
        entriesChanged = b;

        if( b ) setSaved( false );

        String title = getTitle();
        String editorModeSubtitle = getEditorModeSubtitleString();
        
        if( title.endsWith( editorModeSubtitle ) )
			title = title.substring( 0, title.length() - 
			                            editorModeSubtitle.length() );

        if( b && title.endsWith( "]" ) && !title.endsWith( "*]" ) )
        {
            title = title.substring( 0, title.length() - 1 ) + "*]";
            updateTitle( title );
        }
        else
        if( !b && title.endsWith( "*]" ) )
        {
            title = title.substring( 0, title.length() - 2 ) + "]";
            updateTitle( title );
        }
    }

	/**
	 * Called to reset the configuration options
	 * @since 2.0.0
	 */
	void resetConfig() 
	{ 
		jposEntryEditorConfig.reset(); 
        jposEntryEditorConfig.save();
        configReset = true;		
	}

    //-------------------------------------------------------------------------
    // Public methods
    //

    /** 
     * Calls the SwingUitlities.updateComponentTreeUI for every this and 
     * JFrame objects contained in this JFrame instance.  Used to update 
     * the Look and Feel
     * @since 1.2 (NYC 2K meeting)
     */
    public void updateLookAndFeel()
    {
        Cursor currentCursor = getCursor();
        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );

        //Force an update of all the UI and repaint
        SwingUtilities.updateComponentTreeUI( getAboutDialog() );
        SwingUtilities.updateComponentTreeUI( getJFileChooser() );
        SwingUtilities.updateComponentTreeUI( getEditJposEntryDialog() );
        SwingUtilities.updateComponentTreeUI( jposEntryTreePanel );
        SwingUtilities.updateComponentTreeUI( getConfigDialog() );
        SwingUtilities.updateComponentTreeUI( this );
        
        invalidate();
        validate();
        repaint();

        setCursor( currentCursor );
    }

    /**
     * Prints a message on the Message panel (bottom of frame)
     * NOTE: in 1.2 the JTextArea will scroll down
     * @since 0.1 (Philly 99 meeting)
     */
    public void print( String msg ) 
    { 
        bottomTextArea.append( msg ); 
        bottomTextArea.moveCaretPosition( 0 );

        bottomTextArea.setCaretPosition( bottomTextArea.getText().length() );
    }
   
    /**
     * Prints a message on the Message panel (bottom of frame) with a new line
     * NOTE: in 1.2 the JTextArea will scroll down
     * @since 0.1 (Philly 99 meeting)
     */
    public void println( String msg ) 
    { 
    	print( msg + "\n" ); 
    	
    	tracer.println( msg );
    }

    /**
     * enables/disables the delete buttons
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-10-2000)
     */
    public void enableCopyDeleteSubMenus( boolean b )
    {
        copyJposEntrySubMenu.setEnabled( b );
        copyJposEntryPopupSubMenu.setEnabled( b );
        copyEntryButton.setEnabled( b );
        removeJposEntrySubMenu.setEnabled( b );
        removeJposEntryPopupSubMenu.setEnabled( b );
        removeEntryButton.setEnabled( b );
    }

    /**
     * @return the JposEntryPanel
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-11-2000)
     */
    public JposEntryPanel getJposEntryPanel() { return jposEntryPanel; }

    /**
     * sets the JposEntryTreePanel's currentView variable
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-5-2000)
     */
    public void setCurrentTreeView( int currentView ) 
    { jposEntryTreePanel.setCurrentTreeView( currentView ); }

    /**
     * sets the JposEntryTreePanel's expandTreeFlag variable
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-5-2000)
     */
    public void setExpandTreeFlag( boolean b ) 
    { jposEntryTreePanel.setExpandTreeFlag( b ); }

    /**
     * sets the JposEntryViewPanel's showNumbersAsHex variable
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-20-2000)
     */
    public void setShowAsHexFlag( boolean b )
    {
        jposEntryPanel.getJposEntryView().setShowNumbersAsHexFlag( b );
        getEditJposEntryDialog().setShowNumbersAsHexFlag( b );

		showAsHexFlag = b;
    }

	/**
	 * @return true if the show integer as hex config is set
	 * @since 2.0.0
	 */
	public boolean getShowAsHexFlag() { return showAsHexFlag; }

    /**
     * Sets the autoDeleteEntryOnCopy config property
     * @since 2.0.0
     */
    public void setAutoDeleteEntryOnCopy( boolean b ) 
    { autoDeleteEntryOnCopy = b; }

	/**
	 * @return true if the the autoDeleteEntryOnCopy config 
	 * property config is set
	 * @since 2.0.0
	 */
	public boolean getAutoDeleteEntryOnCopy() 
	{ return autoDeleteEntryOnCopy; }

    /**
     * sets the autoLoad variable
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-20-2000)
     */
    public void setAutoLoadFlag( boolean b ) { autoLoad = b; }

    /**
     * sets the frame's look and feel
     * @param laFParam int that is one of MainFrame.JAVALNF or 
     * MainFrame.NATIVELNF
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-6-2000)
     */
    public void setLookAndFeel( int lookAndFeelSelected )
    {
        this.lookAndFeelSelected = lookAndFeelSelected;
        newLnFSelected();
    }

    /**
     * Called to show or hide the "JCL 1.2 View" tab
     * @param b the boolean param
     */
    public void setShowJCL12PropView( boolean b ) 
    { jposEntryPanel.setShowJCL12PropView( b ); }

	/** 
	 * @return a lazily created JposEntryEditorFileFilter 
	 * @since 2.0.0
	 */
	public JposEntryEditorFileFilter getJposEntryEditorFileFilter()
	{
		if( jposEntryEditorFileFilter == null )
			jposEntryEditorFileFilter = new JposEntryEditorFileFilter();

		return jposEntryEditorFileFilter;
	}

	/**
	 * @return true if the editor is in XML mode or false otherwise
	 * @since 2.0.0
	 */
	public boolean isEditorInXmlMode()
	{
		JposProperties jposProperties = JposServiceLoader.getManager().getProperties();

        if( jposProperties.
        	isPropertyDefined( JposProperties.
        				       JPOS_REG_POPULATOR_CLASS_PROP_NAME ) )
      	{
            if( jposProperties.
                getPropertyString( JposProperties.
                                   JPOS_REG_POPULATOR_CLASS_PROP_NAME ).
                                   endsWith( "SimpleXmlRegPopulator" ) )
                return true;
      	}

		return false;
	}
    
    //-------------------------------------------------------------------------
    // Instance variables
    //

	//<temp>Needs to be updated to avoid needing this</temp>   
    private String jposRegistryEntriesUrl = "";
    //<temp/>
    
    private JposRegPopulator xmlRegPopulator = new XercesRegPopulator();
    private JposRegPopulator serRegPopulator = new SimpleRegPopulator();
    
    private boolean registryMode = false;
    private boolean xmlEditorMode = true;
    private boolean entriesChanged = false;
    private boolean saved = false;
    private boolean closing = false;
    private boolean autoLoad = false;
    
    private boolean noFile = true;

    private JFileChooser jFileChooser = null;

    private JSplitPane vSplit = null;
    private JSplitPane hSplit = null;

    private JTextArea bottomTextArea = new JTextArea( 3, 20 );

    private JPanel bottomPanel = new  JPanel();

    private JposEntryPanel jposEntryPanel = new JposEntryPanel( this );

    private JposEntryTreePanel jposEntryTreePanel = new JposEntryTreePanel();

    private EditJposEntryDialog editJposEntryDialog = null;

    private AddJposEntryDialog addJposEntryDialog = null;

    private InvalidEntryDialog invalidEntryDialog = null;

    private JDialog aboutDialog = null;

    private JposEntryEditorConfigDialog configDialog = null;

    private JposEntryMergerDialog jposEntryMergerDialog = null;

    private JposEntryEditorConfig jposEntryEditorConfig = 
    						       JposEntryEditorConfig.getInstance();

    private JMenuItem newFileSubMenu = new JMenuItem( NEW_STRING );

    private JMenuItem openFileSubMenu = new JMenuItem( OPEN_STRING );

    private JMenuItem saveFileSubMenu = new JMenuItem( SAVE_STRING );

    private JMenuItem saveAsFileSubMenu = new JMenuItem( SAVEAS_STRING );
    
    private JMenuItem jposPropertiesSubMenu = 
    				   new JMenuItem( JPOS_PROPERTIES_MENU_STRING );

    private JMenuItem loadJposRegistrySubMenu = 
    				   new JMenuItem( LOADJPOSREGISTRY_STRING );

    private JMenuItem saveJposRegistrySubMenu = 
    				   new JMenuItem( SAVEJPOSREGISTRY_STRING );

    private JMenuItem saveJposRegistryAsSubMenu = 
    				   new JMenuItem( SAVEJPOSREGISTRYAS_STRING );    				   

    private JMenuItem closeJposRegistrySubMenu = 
    				   new JMenuItem( CLOSEJPOSREGISTRY_STRING );
    
    private JMenuItem closeFileSubMenu = new JMenuItem( CLOSE_STRING );

    private JMenuItem exitSubMenu = new JMenuItem( EXIT_STRING );

    private JMenuItem addJposEntrySubMenu = new JMenuItem( ADD_STRING );

    private JMenuItem copyJposEntrySubMenu = new JMenuItem( COPY_STRING );

    private JMenuItem removeJposEntrySubMenu = new JMenuItem( DELETE_STRING );

    private JMenuItem aboutSubMenu = new JMenuItem( ABOUT_STRING );

    private JMenuItem configSubMenu = new JMenuItem( PREFERENCES_STRING );

	private ButtonGroup editorModeButtonGroup = new ButtonGroup();

    private JRadioButtonMenuItem xmlEditorModeMenuItem = 
    							  new JRadioButtonMenuItem( 
    							      XML_EDITOR_MODE_MENU_STRING );

    private JRadioButtonMenuItem serEditorModeMenuItem = 
    							  new JRadioButtonMenuItem( 
    							      SERIALIZED_EDITOR_MODE_MENU_STRING );

    private JMenuItem jposEntryMergerSubMenu = 
                       new JMenuItem( JPOSENTRY_MERGER_MENU_STRING );

    private JMenuItem addJposEntryPopupSubMenu = 
    				   new JMenuItem( ADD_STRING );

    private JMenuItem copyJposEntryPopupSubMenu = 
    				   new JMenuItem( COPY_STRING );

    private JMenuItem removeJposEntryPopupSubMenu = 
    				   new JMenuItem( DELETE_STRING );

    private JMenu treeViewMenu = new JMenu( TREEVIEW_STRING );

    private JRadioButtonMenuItem sortedRBMenuItem = 
    							  new JRadioButtonMenuItem( SORTED_STRING );
    
    private JRadioButtonMenuItem categoryRBMenuItem = 
    							  new JRadioButtonMenuItem( CATEGORY_STRING );
    
    private JRadioButtonMenuItem manufacturerRBMenuItem = 
    							  new JRadioButtonMenuItem( MANUFACTURER_STRING );
    
    private JCheckBoxMenuItem autoExpandTreeMenuItem = 
    						   new JCheckBoxMenuItem( AUTOEXPAND_STRING, false );

    private JToolBar toolbar = new JToolBar();

    private ImageIcon newFileIcon = 
    	     JposEntryEditorUtility.getTreeImage( "newfile_icon.gif" );
    	     
    private ImageIcon openFileIcon = 
    	     JposEntryEditorUtility.getTreeImage( "openfile_icon.gif" );
    	     
    private ImageIcon saveFileIcon = 
    	     JposEntryEditorUtility.getTreeImage( "savefile_icon.gif" );
    
    private ImageIcon saveAsFileIcon = 
    		 JposEntryEditorUtility.getTreeImage( "saveas_icon.gif" );
    
    private ImageIcon closeFileIcon = 
    		 JposEntryEditorUtility.getTreeImage( "closefile_icon.gif" );
    
    private ImageIcon addEntryIcon = 
    		 JposEntryEditorUtility.getTreeImage( "addentry_icon.gif" );
    
    private ImageIcon copyEntryIcon = 
    		 JposEntryEditorUtility.getTreeImage( "copyentry_icon.gif" );
    
    private ImageIcon removeEntryIcon = 
    		 JposEntryEditorUtility.getTreeImage( "deleteentry_icon.gif" );
    
    private ImageIcon configIcon = 
    		 JposEntryEditorUtility.getTreeImage( "config_icon.gif" );
    
    private ImageIcon aboutIcon = 
    		 JposEntryEditorUtility.getTreeImage( "about_icon.gif" );

    private JButton newFileButton = new JButton( newFileIcon );
    private JButton openFileButton = new JButton( openFileIcon );
    private JButton saveFileButton = new JButton( saveFileIcon );
    private JButton saveAsFileButton = new JButton( saveAsFileIcon );
    private JButton closeFileButton = new JButton( closeFileIcon );
    private JButton addEntryButton = new JButton( addEntryIcon );
    private JButton copyEntryButton = new JButton( copyEntryIcon );
    private JButton removeEntryButton = new JButton( removeEntryIcon );
    private JButton configButton = new JButton( configIcon );
    private JButton aboutButton = new JButton( aboutIcon );

    private int lookAndFeelSelected = JposEntryEditorConfig.JAVALNF;

	private PropertiesEditorFrame propEditorFrame = null;

	private JposEntryEditorFileFilter jposEntryEditorFileFilter = null;
	
	private boolean showAsHexFlag = 
					  jposEntryEditorConfig.getShowNumbersAsHexCheckBox();
					  
	private boolean autoDeleteEntryOnCopy = 
					  jposEntryEditorConfig.getAutoDeleteEntryOnCopy();
	
	private boolean configReset = false;
	
	private Tracer tracer = TracerFactory.getInstance().
							 createTracer( "MainFrame" );

    //-------------------------------------------------------------------------
    // Class variables
    //

    private static int newFileCount = 0;

    //-------------------------------------------------------------------------
    // Public class constants
    //

    public static final String MAINFRAME_TITLE = "JposEntry Editor";
	public static final String XML_EXTENSION_STRING = ".xml";
	public static final String CFG_EXTENSION_STRING = ".cfg";

    //-------------------------------------------------------------------------
    // I18N  class constants
    //

    public static final String JPOSENTRYEDITOR_STRING = JposEntryEditorMsg.JPOSENTRYEDITOR_STRING;
    public static final String FILE_STRING = JposEntryEditorMsg.FILE_STRING;
    public static final String NEW_STRING = JposEntryEditorMsg.NEW_STRING;
    public static final String OPEN_STRING = JposEntryEditorMsg.OPEN_STRING;
    public static final String SAVE_STRING = JposEntryEditorMsg.SAVE_STRING;
    public static final String SAVEAS_STRING = JposEntryEditorMsg.SAVEAS_STRING;
    public static final String LOADJPOSREGISTRY_STRING = JposEntryEditorMsg.LOADJPOSREGISTRY_STRING;
    public static final String SAVEJPOSREGISTRY_STRING = JposEntryEditorMsg.SAVEJPOSREGISTRY_STRING;
    public static final String CLOSEJPOSREGISTRY_STRING = JposEntryEditorMsg.CLOSEJPOSREGISTRY_STRING;
    public static final String CLOSE_STRING = JposEntryEditorMsg.CLOSE_STRING;
    public static final String EXIT_STRING = JposEntryEditorMsg.EXIT_STRING;
    public static final String EDIT_STRING = JposEntryEditorMsg.EDIT_STRING;
    public static final String ADD_STRING = JposEntryEditorMsg.ADD_STRING;
    public static final String COPY_STRING = JposEntryEditorMsg.COPY_STRING;
    public static final String DELETE_STRING = JposEntryEditorMsg.DELETE_STRING;
    public static final String MODIFY_STRING = JposEntryEditorMsg.MODIFY_STRING;
    public static final String PREFERENCES_STRING = JposEntryEditorMsg.PREFERENCES_STRING;
    public static final String HELP_STRING = JposEntryEditorMsg.HELP_STRING;
    public static final String ABOUT_STRING = JposEntryEditorMsg.ABOUT_STRING;
    public static final String JPOSENTRIES_STRING = JposEntryEditorMsg.JPOSENTRIES_STRING;
    public static final String PROPERTIES_STRING = JposEntryEditorMsg.PROPERTIES_STRING;
    public static final String IMPORTANTPROPERTIES_STRING = JposEntryEditorMsg.IMPORTANTPROPERTIES_STRING;
    public static final String CURRENTPROPERTY_STRING = JposEntryEditorMsg.CURRENTPROPERTY_STRING;
    public static final String PROPERTYNAME_STRING = JposEntryEditorMsg.PROPERTYNAME_STRING;
    public static final String PROPERTYVALUE_STRING = JposEntryEditorMsg.PROPERTYVALUE_STRING;
    public static final String PROPERTYTYPE_STRING = JposEntryEditorMsg.PROPERTYTYPE_STRING;
    public static final String OTHERPROPERTIES_STRING = JposEntryEditorMsg.OTHERPROPERTIES_STRING;
    public static final String MESSAGES_STRING = JposEntryEditorMsg.MESSAGES_STRING;
    public static final String TOPOFFILE_STRING = JposEntryEditorMsg.TOPOFFILE_STRING;
    
    public static final String NEWFILEMENU_STRING = JposEntryEditorMsg.NEWFILEMENU_STRING;
    public static final String NEWFILEBUTTON_STRING = JposEntryEditorMsg.NEWFILEBUTTON_STRING;
    public static final String OPENFILEMENU_STRING = JposEntryEditorMsg.OPENFILEMENU_STRING;
    public static final String OPENFILEBUTTON_STRING = JposEntryEditorMsg.OPENFILEBUTTON_STRING;
    public static final String SAVEFILEMENU_STRING = JposEntryEditorMsg.SAVEFILEMENU_STRING;
    public static final String SAVEFILEBUTTON_STRING = JposEntryEditorMsg.SAVEFILEBUTTON_STRING;
    public static final String SAVEASFILEMENU_STRING = JposEntryEditorMsg.SAVEFILEMENU_STRING;
    public static final String SAVEASFILEBUTTON_STRING = JposEntryEditorMsg.SAVEFILEBUTTON_STRING;
    public static final String LOADJPOSREGISTRYMENU_STRING = JposEntryEditorMsg.LOADJPOSREGISTRYMENU_STRING;
    public static final String SAVEJPOSREGISTRYMENU_STRING = JposEntryEditorMsg.SAVEJPOSREGISTRYMENU_STRING;
    public static final String CLOSEJPOSREGISTRYMENU_STRING = JposEntryEditorMsg.CLOSEJPOSREGISTRYMENU_STRING;
    public static final String CLOSEFILEMENU_STRING = JposEntryEditorMsg.CLOSEFILEMENU_STRING;
    public static final String CLOSEFILEBUTTON_STRING = JposEntryEditorMsg.CLOSEFILEBUTTON_STRING;
    public static final String EXITMENU_STRING = JposEntryEditorMsg.EXITMENU_STRING;

    public static final String ADDMENU_STRING = JposEntryEditorMsg.ADDMENU_STRING;
    public static final String COPYMENU_STRING = JposEntryEditorMsg.COPYMENU_STRING;
    public static final String ADDBUTTON_STRING = JposEntryEditorMsg.ADDBUTTON_STRING;
    public static final String DELETEMENU_STRING = JposEntryEditorMsg.DELETEMENU_STRING;
    public static final String DELETEBUTTON_STRING = JposEntryEditorMsg.DELETEBUTTON_STRING;
    public static final String MODIFYMENU_STRING = JposEntryEditorMsg.MODIFYMENU_STRING;
    public static final String MODIFYBUTTON_STRING = JposEntryEditorMsg.MODIFYBUTTON_STRING;

    public static final String ABOUTMENU_STRING = JposEntryEditorMsg.ABOUTMENU_STRING;
    public static final String ABOUTBUTTON_STRING = JposEntryEditorMsg.ABOUTBUTTON_STRING;
    public static final String CONFIGMENU_STRING = JposEntryEditorMsg.CONFIGMENU_STRING;
    public static final String CONFIGBUTTON_STRING = JposEntryEditorMsg.CONFIGBUTTON_STRING;

    public static final String TREEVIEW_STRING = JposEntryEditorMsg.TREEVIEW_STRING;
    public static final String SORTED_STRING = JposEntryEditorMsg.SORTED_STRING;
    public static final String CATEGORY_STRING = JposEntryEditorMsg.CATEGORY_STRING;
    public static final String MANUFACTURER_STRING = JposEntryEditorMsg.MANUFACTURER_STRING;
    public static final String AUTOEXPAND_STRING = JposEntryEditorMsg.AUTOEXPAND_STRING;
    
    //-------------------------------------------------------------------------
    // Delete Messages (I18N)
    //

    public static final String SELECTENTRYTODELETE_STRING = JposEntryEditorMsg.SELECTENTRYTODELETE_STRING;
    public static final String DELETEENTRYWITHLOGNAM_STRING = JposEntryEditorMsg.DELETEENTRYWITHLOGNAM_STRING;
	public static final String DELETESELECTEDENTRIES_STRING = JposEntryEditorMsg.DELETE_STRING;
    //-------------------------------------------------------------------------
    // Modify Messages (I18N)
    //

    public static final String SELECTENTRYTOMODIFY_STRING =  JposEntryEditorMsg.SELECTENTRYTOMODIFY_STRING;

    //-------------------------------------------------------------------------
    // Copy Messages (I18N)
    //

    public static final String ENTERNEWLOGICALNAME_STRING =  JposEntryEditorMsg.ENTERNEWLOGICALNAME_STRING;
    public static final String VALIDNAME_STRING = JposEntryEditorMsg.VALIDNAME_STRING;
    public static final String USEDNAME_STRING = JposEntryEditorMsg.USEDNAME_STRING;
    
    //-------------------------------------------------------------------------
    // Save Messages (I18N)
    //

    public static final String SAVECURRENTCHANGES_STRING =  JposEntryEditorMsg.SAVECURRENTCHANGES_STRING;

    //-------------------------------------------------------------------------
    // Open Messages (I18N)
    //

    public static final String OPENFILE_STRING =  JposEntryEditorMsg.OPENFILE_STRING;
    public static final String NOENTRIES_STRING =  JposEntryEditorMsg.NOENTRIES_STRING;
    public static final String ERROR_STRING =  JposEntryEditorMsg.ERROR_STRING;
    
    //-------------------------------------------------------------------------
    // Close Messages (I18N)
    //

    public static final String CLOSECHANGES_STRING =  JposEntryEditorMsg.CLOSECHANGES_STRING;
    public static final String CLOSEDFILE_STRING =  JposEntryEditorMsg.CLOSEDFILE_STRING;
    public static final String CLOSEDFILEWITHCHANGES_STRING = JposEntryEditorMsg.CLOSEDFILEWITHCHANGES_STRING;

    //-------------------------------------------------------------------------
    // Save Messages (I18N)
    //

    public static final String SAVEDFILE_STRING =  JposEntryEditorMsg.SAVEDFILE_STRING;
    public static final String ERRORSAVING_STRING =  JposEntryEditorMsg.ERRORSAVING_STRING;

    //-------------------------------------------------------------------------
    // Save As Messages (I18N)
    //

    public static final String SAVECHANGES_STRING =  JposEntryEditorMsg.SAVECHANGES_STRING;
    public static final String CHANGESLOST_STRING =  JposEntryEditorMsg.CHANGESLOST_STRING;

    //-------------------------------------------------------------------------
    // LoadJposRegistry Messages (I18N)
    //

    public static final String LOADENTRIESJPOSREGISTRY_STRING =  JposEntryEditorMsg.LOADENTRIESJPOSREGISTRY_STRING;

    //-------------------------------------------------------------------------
    // SaveJposRegistry Messages (I18N)
    // 
    
    public static final String NOENTRIESJPOSREGISTRY_STRING =  JposEntryEditorMsg.NOENTRIESJPOSREGISTRY_STRING;
    public static final String ERRORSAVINGJPOSREGISTRY_STRING =  JposEntryEditorMsg.ERRORSAVINGJPOSREGISTRY_STRING;
    public static final String SAVEDCHANGESTOREGISTRY_STRING = JposEntryEditorMsg.SAVEDCHANGESTOREGISTRY_STRING;

    //-------------------------------------------------------------------------
    // CloseJposRegistry Messages (I18N)
    //
    
    public static final String SAVECHANGESJPOSREGISTRY_STRING =  JposEntryEditorMsg.SAVECHANGESJPOSREGISTRY_STRING;
    public static final String CLOSEDJPOSREGISTRY_STRING =  JposEntryEditorMsg.CLOSEDJPOSREGISTRY_STRING;
     
    //-------------------------------------------------------------------------
    // isCurrentLnFMetal class (I18N)
    //

    public static final String METAL_STRING =  JposEntryEditorMsg.METAL_STRING;

    //<i18n>
	public static final String JPOSPROPERTIES_STRING = 
								 "Shows the content of the jpos.properties...";
	
	public static final String JPOS_PROPERTIES_MENU_STRING = 
								 "JposProperties...";
	
    public static final String JPOSENTRY_MERGER_MENU_STRING = 
    							 "JposEntry Merger...";
    
    public static final String JPOSENTRY_MERGER_TTTEXT_STRING = 
    							 "XML<->Serialize JposEntry merger";

	public static final String ERROR_SAVING_ENTRIES_TO_FILE_MSG = 
								 "Error saving entries to file";
    
	public static final String ERROR_OPENING_ENTRIES_FILE_MSG = 
								 "Error opening entries file";
	
	public static final String ERROR_MESSAGE_TITLE_STRING = 
								 "Error loading entries";
	
	public static final String DELETE_OLD_ENTRY_WITH_LOGICAL_NAME_MSG = 
								 "Delete old entry with logical name = ";
	
	public static final String SAVEJPOSREGISTRYAS_STRING = 
								 "Save JposEntryRegistry As...";
								 
	public static final String OVERWRITE_FILE_STRING = 
								 "File exists, overwrite?";
								 
	public static final String EDITOR_MODE_MENU_STRING = "Editor mode";
	
	public static final String XML_EDITOR_MODE_MENU_STRING = "XML";
	
	public static final String SERIALIZED_EDITOR_MODE_MENU_STRING = 
								 "Serialized";
								 
	public static final String XML_EDITOR_MODE_TTTEXT_STRING = 
								 "Switch editor mode to XML";

	public static final String SERIALIZED_EDITOR_MODE_TTTEXT_STRING = 
								 "Switch editor mode to Serialized";

	public static final String XML_EDITOR_MODE_TITLE_STRING = 
								 "[" + XML_EDITOR_MODE_MENU_STRING + "]";

	public static final String SER_EDITOR_MODE_TITLE_STRING = 
								 "[" + SERIALIZED_EDITOR_MODE_MENU_STRING + "]";

	public static final String SELECTENTRYTOCOPY_STRING = 
								 "Select entry to copy.";

	public static final String SWITCH_EDITOR_MODE_MSG = 
								 "Switch editor mode and close file?";

	public static final String INVALID_SER_FILE_MSG = 
								 "Invalid or empty serialized file";
    //</i18n>
}