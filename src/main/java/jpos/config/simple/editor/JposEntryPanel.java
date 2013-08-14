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
import javax.swing.event.*;

import jpos.config.*;

/**
 * Simple JPanel to display a JposEntry panel
 * @since 0.1 (Philly 99 meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @author S. Hanai
 */
class JposEntryPanel extends JPanel
{
	//--------------------------------------------------------------------------
	// Ctor(s)
	//

    /**
     * Default no-argument ctor
	 * @param mainFrame the MainFrame object
     * @since 0.1 (Philly 99 meeting)
     */
    public JposEntryPanel( MainFrame mainFrame )
    {
		this.mainFrame = mainFrame;

        setLayout( new BorderLayout() );
    
        add( jTabbedPane, BorderLayout.CENTER );

        JScrollPane jScrollPane = new JScrollPane( standardPropsPanel, 
                                                   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                                   JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );

        jTabbedPane.addTab( STANDARD_PROPS_TAB_STRING, jScrollPane );
        jTabbedPane.addTab( BUS_PROPS_TAB_STRING, busPropsPanel );
        jTabbedPane.addTab( VENDOR_PROPS_TAB_STRING, vendorPropsPanel );

        buttonPanel.setLayout( new GridLayout( 4, 1 ) );

        buttonPanel.add( new JPanel() );
        buttonPanel.add( new JPanel() );
        buttonPanel.add( editOkCancelButtonPanel );
        buttonPanel.add( customButtonPanel );

        add( buttonPanel, BorderLayout.EAST );

		jTabbedPane.addChangeListener( 	new ChangeListener()
										{
											public void stateChanged( ChangeEvent e ) 
											{ tabbedPaneStateChanged(); }
										}
									 );

		currentPropsViewPanel = standardPropsPanel;

		initPropsViewPanel();
    }

    //--------------------------------------------------------------------------
    // Private methods
    //

	/** Initializes the first PropsViewPanel for the EditOkCancelButtonPanel */
	private void initPropsViewPanel()
	{
		String tabName = jTabbedPane.getTitleAt( jTabbedPane.getSelectedIndex() );

		if( tabName.equals( JCL12_VIEW_TAB_NAME ) )
			editOkCancelButtonPanel.setButtonsVisible( false );
		else
		{
			editOkCancelButtonPanel.setButtonsVisible( true );

			customButtonPanel.removeAll();
			
			if( tabName.equals( STANDARD_PROPS_TAB_STRING ) )
			{
				customButtonPanel.add( standardPropsPanel.getCustomButtonPanel(), BorderLayout.CENTER );
				editOkCancelButtonPanel.setPropsViewPanel( standardPropsPanel );
				currentPropsViewPanel = standardPropsPanel;
			}
			else
			if( tabName.equals( BUS_PROPS_TAB_STRING ) )
			{
				customButtonPanel.add( busPropsPanel.getCustomButtonPanel(), BorderLayout.CENTER );
				editOkCancelButtonPanel.setPropsViewPanel( busPropsPanel );
				currentPropsViewPanel = busPropsPanel;
			}
			else
			{
				customButtonPanel.add( vendorPropsPanel.getCustomButtonPanel(), BorderLayout.CENTER );
				editOkCancelButtonPanel.setPropsViewPanel( vendorPropsPanel );
				currentPropsViewPanel = vendorPropsPanel;
			}
		}

		if( jposEntry != null )
			editOkCancelButtonPanel.getEditButton().setEnabled( true );

		PropsViewPanel.Listener listener = 	new PropsViewPanel.Listener()
											{
												public void jposEntryChanged( PropsViewPanel.Event event )
												{ mainFrame.setEntriesChanged( true ); }
											};

		standardPropsPanel.addListener( listener );
		busPropsPanel.addListener( listener );
		vendorPropsPanel.addListener( listener );

		standardPropsPanel.setMainFrame( mainFrame );
		busPropsPanel.setMainFrame( mainFrame );
		vendorPropsPanel.setMainFrame( mainFrame );
	}

    //--------------------------------------------------------------------------
    // Public methods
    //

	/** 
	 * Called when a new tab is selected from the JTabbedPane or a new JposEntry
	 * is selected  
	 */
	public void tabbedPaneStateChanged()
	{
		if( currentPropsViewPanel != null )
			currentPropsViewPanel.aboutToLooseFocus();

		initPropsViewPanel();
	}

    public void initFromConfig()
    {
        if( JposEntryEditorConfig.getInstance().isShowJCL12PropView() )
            jTabbedPane.addTab( JCL12_VIEW_TAB_NAME, jposEntryViewPanel );
    }
     
    /**
     * @return the JposEntryView for this panel
     * @since 0.1 (Philly 99 meeting)
     */
    public JposEntryViewPanel getJposEntryView() { return jposEntryViewPanel; }

    /** @return the current selected JposEntry */
    public JposEntry getJposEntry() { return jposEntry; }

    /**
     * Sets the current JposEntry 
     * @param entry the JposEntry to set
     */
    public void setJposEntry( JposEntry entry )
    {
		if( entry != null )
		{
			jposEntry = entry;

			jposEntryViewPanel.setJposEntry( jposEntry );
			standardPropsPanel.setJposEntry( jposEntry );
			busPropsPanel.setJposEntry( jposEntry );
			vendorPropsPanel.setJposEntry( jposEntry );

			editOkCancelButtonPanel.getEditButton().setEnabled( true );
		}
		else
			clearAll();
    }

    /** Clears all available views */
    public void clearAll()
    {
        jposEntryViewPanel.clearAll();
        standardPropsPanel.clearAll();
        busPropsPanel.clearAll();
        vendorPropsPanel.clearAll();

		jposEntry = null;
    }

    /** 
	 * Enables/disable all panels
	 * @param b the boolean parameter 
	 */
    public void setEnabledAll( boolean b )
    {
		editOkCancelButtonPanel.setEnabledAll( false );    	
    	
        jposEntryViewPanel.setEnabledAll( b );
        standardPropsPanel.setEnabledAll( b );
        busPropsPanel.setEnabledAll( b );
        vendorPropsPanel.setEnabledAll( b );
    }

    /**
     * Called to show or hide the "JCL 1.2 View" tab
     * @param b the boolean param
     */
    public void setShowJCL12PropView( boolean b ) 
    {                                       
        if( b )
        {
            if( jTabbedPane.indexOfTab( JCL12_VIEW_TAB_NAME ) == -1 )
                jTabbedPane.addTab( JCL12_VIEW_TAB_NAME, jposEntryViewPanel );
        }
        else
        {
            if( jTabbedPane.indexOfTab( JCL12_VIEW_TAB_NAME ) != -1 )
                jTabbedPane.remove( jposEntryViewPanel );
        }
    }
    
	/** 
	 * Called when JposRegistry or File is being closed so that panel will
	 * ask user to save property and entry
	 */
	public void closingJposRegistryOrFile()
	{
		if( currentPropsViewPanel != null )
			currentPropsViewPanel.aboutToLooseFocus();
	}

    //--------------------------------------------------------------------------
    // Instance variables
    //

    private JposEntry jposEntry = null;

    private JTabbedPane jTabbedPane = new JTabbedPane();

    private JposEntryViewPanel jposEntryViewPanel = new JposEntryViewPanel();

	private PropsViewPanel currentPropsViewPanel = null;

    private EditOkCancelButtonPanel editOkCancelButtonPanel = this.new EditOkCancelButtonPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel customButtonPanel = new JPanel( new BorderLayout() );

	private MainFrame mainFrame = null;

    private StandardPropsViewPanel standardPropsPanel = new StandardPropsViewPanel();
    private BusPropsViewPanel busPropsPanel = new BusPropsViewPanel();
    private VendorPropsViewPanel vendorPropsPanel = new VendorPropsViewPanel();

    //--------------------------------------------------------------------------
    // Innner classes
    //

   /**
    * Simple EditOkCancelButtonPanel with "Edit", "OK", "Cancel" buttons
    * @since 1.3 (SF 2K meeting)
    * @author E. Michael Maximilien (maxim@us.ibm.com)
    */
    class EditOkCancelButtonPanel extends JPanel
    {
        //---------------------------------------------------------------------
        // Ctor(s)
        //

        /** Default ctor */
        EditOkCancelButtonPanel() 
        {
            setLayout( new GridLayout( 3, 1 ) );

            JPanel jPanel1 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
            jPanel1.add( editButton );

            add( jPanel1 );

            jPanel1 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
            jPanel1.add( okButton );

            add( jPanel1 );
    
            jPanel1 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
            jPanel1.add( cancelButton );

            add( jPanel1 );

            editButton.addActionListener(   new ActionListener()
                                            {
                                                public void actionPerformed( ActionEvent e ) 
                                                { editButtonClicked(); }
                                            }
                                        );

            okButton.addActionListener( new ActionListener()
                                        {
                                            public void actionPerformed( ActionEvent e ) 
                                            { okButtonClicked(); }
                                        }
                                      );

            cancelButton.addActionListener( new ActionListener()
                                            {
                                                public void actionPerformed( ActionEvent e ) 
                                                { cancelButtonClicked(); }
                                            }
                                          );

            init();
        }

        //---------------------------------------------------------------------
        // Private methods
        //

        /** Sets initial state of the buttons */
        private void init() { setEnabledAll( false ); }

        //---------------------------------------------------------------------
        // Package methods
        //

        /**
         * Enables or disables all buttons
         * @param b the boolean
         */
        void setEnabledAll( boolean b )
        {
            editButton.setEnabled( b );
            okButton.setEnabled( b );
            cancelButton.setEnabled( b );
        }

        /**
         * Enables or disables the edit button
         * @param b the boolean
         */
        void setEditButtonEnabled( boolean b )
        {
            editButton.setEnabled( b );
        }

        /** Called when "Edit" button is clicked */
        void editButtonClicked()
        {
			editButton.setEnabled( false );

			okButton.setEnabled( true );
			cancelButton.setEnabled( true );

			currentPropsViewPanel.editButtonClicked();
        }

        /** Called when "OK" button is clicked */
        void okButtonClicked()
        {
			editButton.setEnabled( true );

			okButton.setEnabled( false );
			cancelButton.setEnabled( false );

			currentPropsViewPanel.okButtonClicked();
        }

        /** Called when "Cancel" button is clicked */
        void cancelButtonClicked()
        {
			editButton.setEnabled( true );

			okButton.setEnabled( false );
			cancelButton.setEnabled( false );

			currentPropsViewPanel.cancelButtonClicked();
        }

		/**
		 * Show/hide all buttons
		 * @param b the boolean flag
		 */
		void setButtonsVisible( boolean b )
		{
			editButton.setVisible( b );
			okButton.setVisible( b );
			cancelButton.setVisible( b );
		}

		/** 
		 * Called to indicate that a new JposEntry has been set
		 * @param propsViewPanel the Current JposEntryViewPanel
		 */
		void setPropsViewPanel( PropsViewPanel propsViewPanel )
		{
			editButton.setEnabled( false );
			okButton.setEnabled( false );
			cancelButton.setEnabled( false );

			currentPropsViewPanel = propsViewPanel;
		}


		/** @return the "Edit" JButton object */
		JButton getEditButton() { return editButton; }

        //---------------------------------------------------------------------
        // Instance variables
        //

        private JButton editButton = new JButton( EDIT_BUTTON_STRING );
        private JButton okButton = new JButton( OK_BUTTON_STRING );
        private JButton cancelButton = new JButton( CANCEL_BUTTON_STRING );

		private PropsViewPanel currentPropsViewPanel = (PropsViewPanel)jTabbedPane.getSelectedComponent();
    }

    //--------------------------------------------------------------------------
    // Class constants
    //

	//<i18n>
    public static final String JCL12_VIEW_TAB_NAME = "JCL 1.2 View";
    public static final String STANDARD_PROPS_TAB_STRING = "Standard Properties";
    public static final String BUS_PROPS_TAB_STRING = "Bus Properties";
    public static final String VENDOR_PROPS_TAB_STRING = "Vendor Properties";

    public static final String EDIT_BUTTON_STRING = "Edit";
    public static final String OK_BUTTON_STRING = "OK";
    public static final String CANCEL_BUTTON_STRING = "Cancel";
	//</i18n>
}
