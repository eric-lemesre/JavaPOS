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

import javax.swing.*;    

import jpos.config.*;
import jpos.util.JposEntryUtility;

/**
 * This panel displays only RS232 Bus specific properties
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (SF 2K meeting)
 */
class RS232PropsViewPanel extends DefaultBusPropsViewPanel
{
    /** Default ctor */
    public RS232PropsViewPanel()
    {
        setBorder( BorderFactory.createLineBorder( Color.gray ) );

        setLayout( new FlowLayout( FlowLayout.CENTER ) );

        JPanel rs232Panel = new JPanel();
        rs232Panel.setLayout( new GridLayout( 6, 1 ) );
        rs232Panel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.gray ),
                                                                RS232_PROPERTIES_STRING ) );

        JPanel jPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jPanel.add( new JLabel( JposEntryConst.RS232_PORT_NAME_PROP_NAME ) );
        jPanel.add( portNameTextField );
        rs232Panel.add( jPanel );

        jPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jPanel.add( new JLabel( JposEntryConst.RS232_BAUD_RATE_PROP_NAME ) );
        jPanel.add( baudRateComboBox );
        rs232Panel.add( jPanel );

        jPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jPanel.add( new JLabel( JposEntryConst.RS232_DATA_BITS_PROP_NAME ) );
        jPanel.add( dataBitsComboBox );
        rs232Panel.add( jPanel );

        jPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jPanel.add( new JLabel( JposEntryConst.RS232_PARITY_PROP_NAME ) );
        jPanel.add( parityComboBox );
        rs232Panel.add( jPanel );

        jPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jPanel.add( new JLabel( JposEntryConst.RS232_STOP_BITS_PROP_NAME ) );
        jPanel.add( stopBitsComboBox );
        rs232Panel.add( jPanel );

        jPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jPanel.add( new JLabel( JposEntryConst.RS232_FLOW_CONTROL_PROP_NAME ) );
        jPanel.add( flowControlComboBox );
        rs232Panel.add( jPanel );

        add( rs232Panel );

        init();
    }

    //--------------------------------------------------------------------------
    // Private intance methods
    //

    /** Initializes the UI components intial state */
    private void init()
    {
        clearAll();
        setEditableAll( false );
        setEnabledAll( false );
    }

	/**
	 * Updates this view with RS232 properties from the JposEntry passed
	 * @param entry the JposEntry object to update UI from
	 */
	private void updateViewFromJposEntry( JposEntry entry )
	{
		String portNameString = "";
		String baudRateString = "";
		String dataBitsString = "";
		String parityString = "";
		String stopBitsString = "";
		String flowControlString = "";

		if( entry.hasPropertyWithName( JposEntryConst.RS232_PORT_NAME_PROP_NAME ) )
			portNameString = (String)entry.getPropertyValue( JposEntryConst.RS232_PORT_NAME_PROP_NAME );

		if( entry.hasPropertyWithName( JposEntryConst.RS232_BAUD_RATE_PROP_NAME ) )
			baudRateString = (String)entry.getPropertyValue( JposEntryConst.RS232_BAUD_RATE_PROP_NAME );

		if( entry.hasPropertyWithName( JposEntryConst.RS232_DATA_BITS_PROP_NAME ) )
			dataBitsString = (String)entry.getPropertyValue( JposEntryConst.RS232_DATA_BITS_PROP_NAME );

		if( entry.hasPropertyWithName( JposEntryConst.RS232_PARITY_PROP_NAME ) )
			parityString = (String)entry.getPropertyValue( JposEntryConst.RS232_PARITY_PROP_NAME );

		if( entry.hasPropertyWithName( JposEntryConst.RS232_STOP_BITS_PROP_NAME ) )
			stopBitsString = (String)entry.getPropertyValue( JposEntryConst.RS232_STOP_BITS_PROP_NAME );

		if( entry.hasPropertyWithName( JposEntryConst.RS232_FLOW_CONTROL_PROP_NAME ) )
			flowControlString = (String)entry.getPropertyValue( JposEntryConst.RS232_FLOW_CONTROL_PROP_NAME );

		portNameTextField.setText( portNameString );
		baudRateComboBox.setSelectedItem( baudRateString );
		dataBitsComboBox.setSelectedItem( dataBitsString );
		parityComboBox.setSelectedItem( parityString );
		stopBitsComboBox.setSelectedItem( stopBitsString );
		flowControlComboBox.setSelectedItem( flowControlString );
	}

	/**
	 * Updates the JposEntry from the view
	 * @param entry the JposEntry object to update UI from
	 */
	private void updateJposEntryFromView( JposEntry entry )
	{
		String portNameString = portNameTextField.getText();
		String baudRateString = baudRateComboBox.getSelectedItem().toString();       
		String dataBitsString = dataBitsComboBox.getSelectedItem().toString();
		String parityString = parityComboBox.getSelectedItem().toString();
		String stopBitsString = stopBitsComboBox.getSelectedItem().toString();       
		String flowControlString = flowControlComboBox.getSelectedItem().toString();

		if( entry.hasPropertyWithName( JposEntryConst.RS232_PORT_NAME_PROP_NAME ) )
			entry.modifyPropertyValue( JposEntryConst.RS232_PORT_NAME_PROP_NAME, portNameString );
		else
			entry.addProperty( JposEntryConst.RS232_PORT_NAME_PROP_NAME, portNameString );

		if( entry.hasPropertyWithName( JposEntryConst.RS232_BAUD_RATE_PROP_NAME ) )
			entry.modifyPropertyValue( JposEntryConst.RS232_BAUD_RATE_PROP_NAME, baudRateString );
		else
			entry.addProperty( JposEntryConst.RS232_BAUD_RATE_PROP_NAME, baudRateString );

		if( entry.hasPropertyWithName( JposEntryConst.RS232_DATA_BITS_PROP_NAME ) )
			entry.modifyPropertyValue( JposEntryConst.RS232_DATA_BITS_PROP_NAME, dataBitsString );
		else
			entry.addProperty( JposEntryConst.RS232_DATA_BITS_PROP_NAME, dataBitsString );

		if( entry.hasPropertyWithName( JposEntryConst.RS232_PARITY_PROP_NAME ) )
			entry.modifyPropertyValue( JposEntryConst.RS232_PARITY_PROP_NAME, parityString );
		else
			entry.addProperty( JposEntryConst.RS232_PARITY_PROP_NAME, parityString );

		if( entry.hasPropertyWithName( JposEntryConst.RS232_STOP_BITS_PROP_NAME ) )
			entry.modifyPropertyValue( JposEntryConst.RS232_STOP_BITS_PROP_NAME, stopBitsString );
		else
			entry.addProperty( JposEntryConst.RS232_STOP_BITS_PROP_NAME, stopBitsString );

		if( entry.hasPropertyWithName( JposEntryConst.RS232_FLOW_CONTROL_PROP_NAME ) )
			entry.modifyPropertyValue( JposEntryConst.RS232_FLOW_CONTROL_PROP_NAME, flowControlString );
		else
			entry.addProperty( JposEntryConst.RS232_FLOW_CONTROL_PROP_NAME, flowControlString );

		fireJposEntryChanged( new PropsViewPanel.Event( this ) );
	}

    //--------------------------------------------------------------------------
    // Public intance methods
    //

    /** Clears all the JTextField to "" */
    public void clearAll()
    {
        portNameTextField.setText( RS232Const.DEFAULT_RS232_PORT_NAME_VALUE );
		baudRateComboBox.setSelectedItem( RS232Const.DEFAULT_RS232_BAUD_RATE_VALUE );
		dataBitsComboBox.setSelectedItem( RS232Const.DEFAULT_RS232_DATA_BITS_VALUE );
		parityComboBox.setSelectedItem( RS232Const.DEFAULT_RS232_PARITY_VALUE );
		stopBitsComboBox.setSelectedItem( RS232Const.DEFAULT_RS232_STOP_BITS_VALUE );
		flowControlComboBox.setSelectedItem( RS232Const.DEFAULT_RS232_FLOW_CONTROL_VALUE );
    }

    /**
     * Makes all JTextField enabled
     * @param b the boolean param
     */
    public void setEnabledAll( boolean b )
    {
        portNameTextField.setEnabled( b );
        baudRateComboBox.setEnabled( b );
        dataBitsComboBox.setEnabled( b );
        parityComboBox.setEnabled( b );
        stopBitsComboBox.setEnabled( b );
        flowControlComboBox.setEnabled( b );
    }

    /**
     * Makes all JTextField editable
     * @param b the boolean param
     */
    public void setEditableAll( boolean b )
    {
        portNameTextField.setEditable( b );
    }

    /**
     * Sets the current JposEntry for this panel 
     * @param jposEntry the JposEntry
     */
    public void setJposEntry( JposEntry entry ) 
    { 
        jposEntry = entry; 

		updateViewFromJposEntry( jposEntry );
    }

    /** Called when this PropsViewPanel about to loose focus */
    public void aboutToLooseFocus()
    {
		cancelButtonClicked();
    }

    /** Called when the "Edit" command button is clicked */
    public void editButtonClicked()
    {
		setEditableAll( true );
		setEnabledAll( true );
    }

    /** Called when the "OK" command button is clicked */
    public void okButtonClicked()
    {
		updateJposEntryFromView( jposEntry );

		setEditableAll( false );
		setEnabledAll( false );
    }

    /** Called when the "OK" command button is clicked */
    public void cancelButtonClicked()
    {
		setEditableAll( false );
		setEnabledAll( false );
    }

	/** 
	 * @removes all current bus properties from the JposEntry passed
	 * @param entry the JposEntry to remove props from
	 */
	public void removeBusProps( JposEntry entry )
	{
		JposEntryUtility.removeAllRS232Props( entry );
	}

    //--------------------------------------------------------------------------
    // Intance variables
    //

    private JTextField portNameTextField = new JTextField( 10 );
    private JComboBox baudRateComboBox = new JComboBox( JposEntryConst.RS232_BAUD_RATE_VALUES );
    private JComboBox dataBitsComboBox = new JComboBox( JposEntryConst.RS232_DATA_BITS_VALUES );
    private JComboBox parityComboBox = new JComboBox( JposEntryConst.RS232_PARITY_VALUES );
    private JComboBox stopBitsComboBox = new JComboBox( JposEntryConst.RS232_STOP_BITS_VALUES );
    private JComboBox flowControlComboBox = new JComboBox( JposEntryConst.RS232_FLOW_CONTROL_VALUES );

    //---------------------------------------------------------------------------
    // Class constants
    //

    //<i18n>
    public static final String RS232_PROPERTIES_STRING = "RS232 Properties";
	//</i18n>
}
