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

import javax.swing.*;    

import jpos.config.*;
import jpos.util.JposEntryUtility;

/**
 * Informs user that entry is not valid and thus can either be saved with
 * defaults or discarder
 * @since 1.3 (Tokyo 2001)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
class InvalidEntryDialog extends JDialog
{   
    /**
     * 1-arg ctor
     * @param parent the parent JFrame
     */
    public InvalidEntryDialog( JFrame parent )
    {
        super( parent );

        setTitle( INVALID_ENTRY_DIALOG_TITLE );
        getContentPane().setLayout( new BorderLayout() );

		JPanel topPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
		topPanel.add( missingEntryLabel );
		topPanel.add( logicalNameTextField );
		getContentPane().add( topPanel, BorderLayout.NORTH );

		JScrollPane jScrollPane = new JScrollPane( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
												   JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		jScrollPane.setViewportView( missingPropertiesTextArea );
		jScrollPane.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.gray ), 
																 MISSING_PROPERTIES_TITLE_STRING ) );
		getContentPane().add( jScrollPane, BorderLayout.CENTER );

        JPanel commandPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        commandPanel.add( saveWithDefaultsButton );
        commandPanel.add( dontSaveButton );
        getContentPane().add( commandPanel, BorderLayout.SOUTH );

		logicalNameTextField.setToolTipText( INVALID_ENTRY_LOGICAL_NAME_TTTEXT_STRING );
		missingPropertiesTextArea.setToolTipText( MISSING_PROPERTIES_TTTEXT_STRING );
		saveWithDefaultsButton.setToolTipText( SAVE_WITH_DEFAULTS_TTTEXT_STRING );
		dontSaveButton.setToolTipText( DONT_SAVE_TTTEXT_STRING );

		saveWithDefaultsButton.addActionListener( 	new ActionListener()
													{
														public void actionPerformed(ActionEvent e) 
														{ saveWithDefaultsButtonClicked(); }
                                                    }
												);

		dontSaveButton.addActionListener(	new ActionListener()
											{
												public void actionPerformed(ActionEvent e) 
												{ dontSaveButtonClicked(); }
                                            }
										);

		logicalNameTextField.setEditable( false );
		missingPropertiesTextArea.setEditable( false );

        pack();
        centerFrame();
    }
                                                                                   
    //--------------------------------------------------------------------------
    // Private methods
    //

    /**
     * Center's the Frame in the middle of the screen
     * <b>NOTE:</b>this method should propabley be moved to a utility class
     */
    private void centerFrame()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setLocation( ( screenSize.width / 2 ) - ( getSize().width / 2 ),
                     ( screenSize.height / 2 ) - ( getSize().height / 2 ) );
    }

	/**
	 * Called when the "Save With Defaults" Button is clicked
	 * @since 1.3 (Tokyo 2001 meeting)
	 */
	private void saveWithDefaultsButtonClicked() 
	{ 
		saveWithDefaultsButtonClicked = true;
		setVisible( false );
	}

	/**
	 * Called when the "Don't Save" Button is clicked
	 * @since 1.3 (Tokyo 2001 meeting)
	 */
	private void dontSaveButtonClicked() 
	{ 
		saveWithDefaultsButtonClicked = false;
		setVisible( false );
	}

    //--------------------------------------------------------------------------
    // Public methods
    //

	/** 
	 * Set the invalid JposEntry object
	 * @param jposEntry the JposEntry object
	 */
	public void setInvalidEntry( JposEntry jposEntry )
	{
		invalidEntry = jposEntry;

		logicalNameTextField.setText( invalidEntry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME ).toString() );

		Enumeration missingProperties = JposEntryUtility.getMissingRequiredPropNames( invalidEntry );

		missingPropertiesTextArea.setText( "" );

		while( missingProperties.hasMoreElements() )
			missingPropertiesTextArea.setText( missingPropertiesTextArea.getText() + missingProperties.nextElement() + "\n" );

		saveWithDefaultsButtonClicked = false;
	}

	/** 
	 * @return true if the "Save with Defaults" button is selected 
	 * @since 2.3 (Tokyo 2001 meeting)
	 */
	public boolean isSaveWithDefaultsButtonSelected() { return saveWithDefaultsButtonClicked; }

    //--------------------------------------------------------------------------
    // Instance variables
    //

	private JposEntry invalidEntry = null;

	private JLabel missingEntryLabel = new JLabel( MISSING_ENTRY_LABEL_STRING );
	private JTextField logicalNameTextField = new JTextField( 15 );
	private JTextArea missingPropertiesTextArea = new JTextArea( 5, 20 );
    private JButton saveWithDefaultsButton = new JButton( SAVE_WITH_DEFAULT_BUTTON_STRING );
    private JButton dontSaveButton = new JButton( DONT_SAVE_BUTTON_STRING );

	private boolean saveWithDefaultsButtonClicked = false;

    //---------------------------------------------------------------------------
    // I18N  class constants
    //
    
	public static final String MISSING_PROPERTIES_TITLE_STRING = "Missing required properties";
	public static final String MISSING_ENTRY_LABEL_STRING = "Required properties missing for entry: ";
	public static final String INVALID_ENTRY_DIALOG_TITLE = "Invalid Entry";
	public static final String SAVE_WITH_DEFAULT_BUTTON_STRING = "Save Entry With Defaults";
	public static final String DONT_SAVE_BUTTON_STRING = "Don't Save Entry";

	public static final String INVALID_ENTRY_LOGICAL_NAME_TTTEXT_STRING = "Logical name of invalid entry";
	public static final String MISSING_PROPERTIES_TTTEXT_STRING = "Required properties missing from invalid properties";
	public static final String SAVE_WITH_DEFAULTS_TTTEXT_STRING = "Save invalid entry with default values for required properties";
	public static final String DONT_SAVE_TTTEXT_STRING = "Don't save invalid entry (entry is discarded)";
}
