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

import jpos.config.*;

/**
 * This panel is meant to aggregate all bus specific properties as 
 * other inner panes
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (Washington DC 2001 meeting)
 */
class BusPropsViewPanel extends AbstractPropsViewPanel
{
    /** Default ctor */
    public BusPropsViewPanel()
    {
        setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.gray ), 
													 DEVICE_BUS_PROPERTIES_STRING ) );
		setLayout( new BorderLayout() );

		JPanel jPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
		jPanel.add( new JLabel( DEVICE_BUS_STRING ) );
		jPanel.add( deviceBusComboBox );
		add( jPanel, BorderLayout.NORTH );

		initBusPropsPanels();

		JScrollPane jScrollPane = new JScrollPane( busPropsPanel );
		add( jScrollPane, BorderLayout.CENTER );

		init();

		deviceBusComboBox.addActionListener(new ActionListener()
											{
												public void actionPerformed( ActionEvent e ) 
												{ deviceBusComboBoxStateChanged(); }
											}
										   );
    }

    //--------------------------------------------------------------------------
    // Private intance methods
    //

	/** Initializes all UI widgets to initial setting */
	private void init()
	{
		deviceBusComboBox.setSelectedItem( JposEntryConst.UNKNOWN_DEVICE_BUS );
		deviceBusComboBox.setEnabled( false );
	}

	/** Initializes all specific bus properties panels */
	private void initBusPropsPanels()
	{
		busPropsPanel.add( rs232PropsViewPanel, JposEntryConst.RS232_DEVICE_BUS );
		busPropsPanel.add( rs485PropsViewPanel, JposEntryConst.RS485_DEVICE_BUS );
		busPropsPanel.add( usbPropsViewPanel, JposEntryConst.USB_DEVICE_BUS );
		busPropsPanel.add( proprietaryPropsViewPanel, JposEntryConst.PROPRIETARY_DEVICE_BUS );
		busPropsPanel.add( unknownPropsViewPanel, JposEntryConst.UNKNOWN_DEVICE_BUS );

		busPropsPanelCardLayout.show( busPropsPanel, JposEntryConst.UNKNOWN_DEVICE_BUS );

		visiblePropsViewPanel = unknownPropsViewPanel;
	}

	/** Called when the deviceBusComboBox changes state */
	private void deviceBusComboBoxStateChanged()
	{
		showCorrectBusPanel( deviceBusComboBox.getSelectedItem().toString() );	

		if( inEditMode )
		{
			setEnabledAll( true );
			setEditableAll( true );

			getVisiblePropsViewPanel().setJposEntry( jposEntry );
		}
	}

	/** 
	 * Initializes and shows the correct bus panel
	 * @param deviceBus the deviceBus value
	 */
	private void showCorrectBusPanel( String deviceBus )
	{
		if( deviceBus.equals( JposEntryConst.RS232_DEVICE_BUS ) )
		{
			busPropsPanelCardLayout.show( busPropsPanel, JposEntryConst.RS232_DEVICE_BUS );
			visiblePropsViewPanel = rs232PropsViewPanel;
		}
		else
		if( deviceBus.equals( JposEntryConst.RS485_DEVICE_BUS ) )
		{
			busPropsPanelCardLayout.show( busPropsPanel, JposEntryConst.RS485_DEVICE_BUS );
			visiblePropsViewPanel = rs485PropsViewPanel;
		}
		else
		if( deviceBus.equals( JposEntryConst.USB_DEVICE_BUS ) )
		{
			busPropsPanelCardLayout.show( busPropsPanel, JposEntryConst.USB_DEVICE_BUS );
			visiblePropsViewPanel = usbPropsViewPanel;
		}
		else
		if( deviceBus.equals( JposEntryConst.PROPRIETARY_DEVICE_BUS ) )
		{
			busPropsPanelCardLayout.show( busPropsPanel, JposEntryConst.PROPRIETARY_DEVICE_BUS );
			visiblePropsViewPanel = proprietaryPropsViewPanel;
		}
		else
		{
			busPropsPanelCardLayout.show( busPropsPanel, JposEntryConst.UNKNOWN_DEVICE_BUS );
			visiblePropsViewPanel = unknownPropsViewPanel;
		}
	}

	/** @return the currently visible PropsViewPanel */
	private PropsViewPanel getVisiblePropsViewPanel() { return visiblePropsViewPanel; }

    //--------------------------------------------------------------------------
    // Public intance methods
    //

    /** Clears all the JTextField to "" */
    public void clearAll()
    {
		deviceBusComboBox.setSelectedItem( JposEntryConst.UNKNOWN_DEVICE_BUS );
		
		getVisiblePropsViewPanel().clearAll();
    }

    /**
     * Makes all JTextField enabled
     * @param b the boolean param
     */
    public void setEnabledAll( boolean b )
    {
		deviceBusComboBox.setEnabled( b );
		
		getVisiblePropsViewPanel().setEnabledAll( b );
    }

    /**
     * Makes all JTextField editable
     * @param b the boolean param
     */
    public void setEditableAll( boolean b )
    {
		getVisiblePropsViewPanel().setEditableAll( b );
    }

    /**
     * Sets the current JposEntry for this panel 
     * @param jposEntry the JposEntry
     */
    public void setJposEntry( JposEntry entry ) 
    { 
		jposEntry = entry;

		if( !jposEntry.hasPropertyWithName( JposEntryConst.DEVICE_BUS_PROP_NAME ) )
			jposEntry.addProperty( JposEntryConst.DEVICE_BUS_PROP_NAME,
								   JposEntryConst.UNKNOWN_DEVICE_BUS );

		inEditMode = false;

		deviceBusComboBox.setSelectedItem( (String)jposEntry.getPropertyValue( JposEntryConst.DEVICE_BUS_PROP_NAME ) );

		getVisiblePropsViewPanel().setJposEntry( entry );

		setEnabledAll( false );
		setEditableAll( false );
    }

    /** Called when this PropsViewPanel about to loose focus */
    public void aboutToLooseFocus()
    {
		if( jposEntry != null )
			cancelButtonClicked(); 
    }

    /** Called when the "Edit" command button is clicked */
    public void editButtonClicked()
    {
		getVisiblePropsViewPanel().editButtonClicked();

		setEnabledAll( true );
		setEditableAll( true );

		inEditMode = true;
	}

    /** Called when the "OK" command button is clicked */
    public void okButtonClicked()
    {
		String newDeviceBus = deviceBusComboBox.getSelectedItem().toString();
		String currentJposEntryDeviceBus = jposEntry.getPropertyValue( JposEntryConst.DEVICE_BUS_PROP_NAME ).toString();

		if( !currentJposEntryDeviceBus.equals( newDeviceBus ) )
		{
			int userChoice = JOptionPane.showConfirmDialog( this, SAVE_CHANGES_TO_DEVICE_BUS_PROP_MSG );

			if( userChoice == JOptionPane.YES_OPTION || userChoice == JOptionPane.CANCEL_OPTION )
			{
				jposEntry.modifyPropertyValue( JposEntryConst.DEVICE_BUS_PROP_NAME,
											   deviceBusComboBox.getSelectedItem().toString() );

				getVisiblePropsViewPanel().okButtonClicked();

				fireJposEntryChanged( new PropsViewPanel.Event( this ) );
			}
		}
		else
		{
			getVisiblePropsViewPanel().okButtonClicked();
			showCorrectBusPanel( jposEntry.getPropertyValue( JposEntryConst.DEVICE_BUS_PROP_NAME ).toString() );
		}


		setEnabledAll( false );
		setEditableAll( false );

		inEditMode = false;
    }

    /** Called when the "OK" command button is clicked */
    public void cancelButtonClicked()
    {
		if( jposEntry == null ) return;

		getVisiblePropsViewPanel().cancelButtonClicked();

		if( jposEntry.hasPropertyWithName( JposEntryConst.DEVICE_BUS_PROP_NAME ) )
			showCorrectBusPanel( (String)jposEntry.getPropertyValue( JposEntryConst.DEVICE_BUS_PROP_NAME ) );
		else
			showCorrectBusPanel( JposEntryConst.UNKNOWN_DEVICE_BUS );
		
		setEnabledAll( false );
		setEditableAll( false );

		inEditMode = false;
    }

    //--------------------------------------------------------------------------
    // Intance variables
    //

	private DefaultBusPropsViewPanel rs232PropsViewPanel = new RS232PropsViewPanel();
	private DefaultBusPropsViewPanel rs485PropsViewPanel = new DefaultBusPropsViewPanel();
	private DefaultBusPropsViewPanel usbPropsViewPanel = new DefaultBusPropsViewPanel();
	private DefaultBusPropsViewPanel proprietaryPropsViewPanel = new DefaultBusPropsViewPanel();
	private DefaultBusPropsViewPanel unknownPropsViewPanel = new DefaultBusPropsViewPanel();

	private AbstractPropsViewPanel visiblePropsViewPanel = null;

	private CardLayout busPropsPanelCardLayout = new CardLayout();
	private JPanel busPropsPanel = new JPanel( busPropsPanelCardLayout, true );

    private JComboBox deviceBusComboBox = new JComboBox( JposEntryConst.DEVICE_BUS_VALUES );

	private boolean inEditMode = false;

    //---------------------------------------------------------------------------
    // Class constants
    //

    //<i18n>
    public static final String DEVICE_BUS_PROPERTIES_STRING = "Device Bus (Connectivity) Properties";
	public static final String DEVICE_BUS_STRING = "Device Bus";
	public static final String SAVE_CHANGES_TO_DEVICE_BUS_PROP_MSG = "Save changes and loose previous setting?"; 
	//</i18n>
}
