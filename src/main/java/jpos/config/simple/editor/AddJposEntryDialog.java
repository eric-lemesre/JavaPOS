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


import java.util.Iterator;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;    

import jpos.config.*;
import jpos.config.simple.*;
import jpos.loader.JposServiceLoader;
import jpos.util.JposEntryUtility;
import jpos.profile.JposDevCats;

/**
 * Small editor dialog to get the name for the JposEntry to add and select
 * which JposEntryRegPopulator that it will belong to
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (Washington DC 2001 meeting)
 */
class AddJposEntryDialog extends JDialog
{   
    /**
     * 1-arg ctor
     * @param parent the parent JFrame
     */
    public AddJposEntryDialog( JFrame parent )
    {
        super( parent );

        setTitle( ADD_JPOSENTRY_DIALOG_TITLE_STRING );

		getContentPane().setLayout( new BorderLayout() );

		JPanel centerPanel = new JPanel( new GridLayout( 3, 1 ) );

		centerPanel.setBorder( BorderFactory.
							   createTitledBorder( BorderFactory.createLineBorder( Color.gray ),
												   NEW_JPOSENTRY_PROPERTY_STRING ) );;

		JPanel jPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		jPanel.add( new JLabel( LOGICAL_NAME_STRING ) );
		jPanel.add( logicalNameTextField );
		centerPanel.add( jPanel );

		jPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		jPanel.add( new JLabel( REGISTRY_POPULATOR_STRING ) );
		jPanel.add( regPopulatorComboBox );
		centerPanel.add( jPanel );

		jPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		jPanel.add( new JLabel( JPOS_VERSION_STRING ) );
		jPanel.add( jposVersionComboBox );

		jPanel.add( new JLabel( DEVICE_CATEGORY_STRING ) );
		jPanel.add( devCatComboBox );
		centerPanel.add( jPanel );

		getContentPane().add( centerPanel, BorderLayout.CENTER );

		JPanel buttonPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
		buttonPanel.add( okButton );
		buttonPanel.add( cancelButton );

		getContentPane().add( buttonPanel, BorderLayout.SOUTH );

        okButton.addActionListener( new ActionListener()
                                    {                                            
                                        public void actionPerformed( ActionEvent e )
                                        { ok(); }                            
                                    }                                            
                                  );                                             
                                                                               
        cancelButton.addActionListener( new ActionListener()
                                        {                                            
                                            public void actionPerformed( ActionEvent e )
                                            { cancel(); }                            
                                        }                                            
                                      );

		addWindowListener(	new WindowAdapter()
							{
								public void windowClosing( WindowEvent e ) 
								{ cancel(); }
							}
						 );

		initRegPopulatorComboBox();

        pack();
        centerFrame();
    }
                                                                                   
    //--------------------------------------------------------------------------
    // Private methods
    //

	/** Init regPopulatorComboBox with all populator names */
	private void initRegPopulatorComboBox()
	{
		regPopulatorComboBox.removeAllItems();

		JposRegPopulator populator = JposServiceLoader.getManager().
													   getRegPopulator();

		if( populator.isComposite() )
		{
			Iterator populators = ( (CompositeRegPopulator)populator ).
								  getPopulators();
								  
			while( populators.hasNext() )
				regPopulatorComboBox.addItem( populators.next() );
		}
		else
			regPopulatorComboBox.addItem( populator );
	}

    /**
     * Center's the Frame in the middle of the screen
     * <b>NOTE:</b>this method should propabley be moved to a utility class
     * @since 0.1 (Philly 99 meeting)
     */
    private void centerFrame()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setLocation( ( screenSize.width / 2 ) - ( getSize().width / 2 ),
                     ( screenSize.height / 2 ) - ( getSize().height / 2 ) );
    }

    /** Called when "OK" is clicked */
    private void ok()
    {
		if( logicalNameTextField.getText().equals( "" ) )
		{
			JOptionPane.showMessageDialog( this, 
			                  			   LOGICAL_NAME_MUST_BE_NON_EMPTY_MSG );
			return;
		}

		jposEntry = new SimpleEntry( logicalNameTextField.getText(),
									 (JposRegPopulator)regPopulatorComboBox.
									 getSelectedItem() );

		jposEntry.addProperty( JposEntry.JPOS_VERSION_PROP_NAME, 
		                       jposVersionComboBox.getSelectedItem().
		                       toString() );
		                       
		jposEntry.addProperty( JposEntry.DEVICE_CATEGORY_PROP_NAME, 
		                       devCatComboBox.getSelectedItem().toString() );

		JposEntryUtility.addMissingRequiredProps( jposEntry );

		canceled = false;
		setVisible( false );
    }

    /** Called when "Cancel" is clicked */
    private void cancel() 
	{ 
		canceled = true; 
		setVisible( false );
	}

    //--------------------------------------------------------------------------
    // Public overridden methods
    //

	/**
	 * Makes this dialog visible or not
	 * @param b the boolean parameter
	 */
	public void setVisible( boolean b )
	{
		if( b )
		{
			initRegPopulatorComboBox();
			logicalNameTextField.setText( "" );
			jposEntry = null;
			canceled = false;
		}

		super.setVisible( b );
	}

    //--------------------------------------------------------------------------
    // Public methods
    //

    /**
     * @return the JposEntry that is being edited
     * @since 0.1 (Philly 99 meeting)
     */
    public JposEntry getJposEntry() { return jposEntry; }
   
    /** @return true if this dialog was canceled */
    public boolean isCanceled() { return canceled; }
    
    //--------------------------------------------------------------------------
    // Instance variables
    //

    private JposEntry jposEntry = null;
    
	private JTextField logicalNameTextField = new JTextField( 20 );
	
	private JComboBox regPopulatorComboBox = new JComboBox();
	
	private JComboBox devCatComboBox = 
	                   new JComboBox( JposDevCats.DEVCAT_ARRAY );
	                   
	private JComboBox jposVersionComboBox = 
	                   new JComboBox( JposEntryConst.JPOS_VERSION_PROPS );

	private JButton okButton = new JButton( OKBUTTON_STRING );
	
    private JButton cancelButton = new JButton( CANCELBUTTON_STRING );

    private boolean canceled = false;

    //--------------------------------------------------------------------------
    // Class constants
    //
    
    public static final String CANCELBUTTON_STRING = 
                                 JposEntryEditorMsg.CANCELBUTTON_STRING;
                                 
    public static final String OKBUTTON_STRING = 
                                 JposEntryEditorMsg.OKBUTTON_STRING;

	//<i18n>
	public static final String LOGICAL_NAME_STRING = "Logical name:";
	
    public static final String ADD_JPOSENTRY_DIALOG_TITLE_STRING = 
                          "Add JposEntry";
                          
	public static final String REGISTRY_POPULATOR_STRING = 
	                             "Registry populator:";
	                             
	public static final String NEW_JPOSENTRY_PROPERTY_STRING = 
	                             "New JposEntry Properties";
	                             
	public static final String LOGICAL_NAME_MUST_BE_NON_EMPTY_MSG = 
	                             "Logical name cannot be empty!";
	                             
	public static final String JPOS_VERSION_STRING = "JavaPOS version:";
	
	public static final String DEVICE_CATEGORY_STRING = "Device Category:";
	//</i18n>
}