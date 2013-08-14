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

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Collections;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;    
import javax.swing.table.*;

import jpos.util.*;
import jpos.config.*;
import jpos.config.simple.*;

/**
 * This panel displays only Vendor specific properties
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (SF 2K meeting)
 */
class VendorPropsViewPanel extends AbstractPropsViewPanel
{
    /** Default ctor */
    public VendorPropsViewPanel()
    {
        setBorder( BorderFactory.createLineBorder( Color.gray ) );

		setLayout( new BorderLayout() );

        JScrollPane jScrollPane = 
        new JScrollPane( propsTable, 
                         JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		
		jScrollPane.
		setBorder( BorderFactory.
				   createTitledBorder( BorderFactory.createLineBorder( Color.gray ),
									   VENDOR_PROPS_TABLE_TITLE ) );

		add( jScrollPane, BorderLayout.CENTER );

		addButton.
		addActionListener(	new ActionListener()
							{
								public void actionPerformed(ActionEvent e) 
								{ addButtonClicked(); }
							}
						 );

		removeButton.
		addActionListener(	new ActionListener()
							{
								public void actionPerformed(ActionEvent e) 
								{ removeButtonClicked(); }
							}
						 );

		init();
    }

    //-------------------------------------------------------------------------
    // Private intance methods
    //

    /** Initializes the UI components intial state */
    private void init()
    {
		clearAll();
		setEditableAll( false );

		initCustomButtonPanel();

		initTable();
    }

	private void initTable()
	{
		TableColumn propTypeTableColumn = propsTable.getColumn( PROP_TYPE_STRING );

		propTypeTableColumn.
		setCellEditor( new DefaultCellEditor( new JComboBox( JposEntryConst.PROP_TYPES_SHORT_NAMES ) ) );

		propsTable.setRowHeight( (int)( propsTable.getRowHeight() * 1.50 ) );
	}


	/** Initializes the custom button panel for this PropsViewPanel */
	private void initCustomButtonPanel()
	{
		addButton.setEnabled( false );
		removeButton.setEnabled( false );

		addButton.setToolTipText( ADD_BUTTON_TTEXT_STRING );
		removeButton.setToolTipText( REMOVE_BUTTON_TTEXT_STRING );

		customButtonPanel = new JPanel( new GridLayout( 3, 1 ) );

		customButtonPanel.add( new JPanel() );

		JPanel jPanel1 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		jPanel1.add( addButton );
		customButtonPanel.add( jPanel1 );

		jPanel1 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		jPanel1.add( removeButton );
		customButtonPanel.add( jPanel1 );
	}

	/**
	 * Intializes all fields from the JposEntry object passed
	 * @param jposEntry the JposEntry object
	 */
	private void initFromJposEntry( JposEntry jposEntry )
	{
		propsTableModel.setJposEntry( jposEntry );
	}

	/** Called when the "Add" button is clicked */
	private void addButtonClicked() 
	{
		propsTableModel.add(); 
	}

	/** Called when the "Remove" button is clicked */
	private void removeButtonClicked() 
	{
		int userChoice = JOptionPane.
		                 showConfirmDialog( this, 
		                 					REMOVE_SELECTED_PROPERTY_FROM_ENTRY_MSG,
		                 					SELECT_AN_OPTION_MSG,
		                 					JOptionPane.YES_NO_OPTION );

		if( userChoice == JOptionPane.YES_OPTION )
		{
			int rowIndex = propsTable.getSelectedRow();

			if( rowIndex != -1 )
				propsTableModel.delete( rowIndex );
		}
	}

    //-------------------------------------------------------------------------
    // Public intance methods
    //

	/** @return the JPanel of of custom JButton object to add to for this panel */
	public JPanel getCustomButtonPanel() { return customButtonPanel; }

    /** Clears all the JTextField to "" */
    public void clearAll() { propsTableModel.clearAll(); }

    /**
     * Makes all JTextField enabled
     * @param b the boolean param
     */
    public void setEnabledAll( boolean b )
    {
		propsTable.setEnabled( b );

		addButton.setEnabled( b );
		removeButton.setEnabled( b );
    }

    /**
     * Makes all JTextField editable
     * @param b the boolean param
     */
    public void setEditableAll( boolean b ) 
    { propsTableModel.setEditable( b ); }

    /**
     * Sets the current JposEntry for this panel 
     * @param jposEntry the JposEntry
     */
    public void setJposEntry( JposEntry entry ) 
    { 
        if( jposEntry != null )
	        cancelButtonClicked();

        jposEntry = entry;        

		propsTableModel.setJposEntry( jposEntry );
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
		propsTableModel.setEditable( true );
		propsTable.setEnabled( true );
		
		addButton.setEnabled( true );
		if( propsTableModel.props.size() > 0 )
			removeButton.setEnabled( true );
	}

    /** Called when the "OK" command button is clicked */
    public void okButtonClicked()
    {
		int rowIndex = propsTable.getEditingRow();
		int colIndex = propsTable.getEditingColumn();

		if( rowIndex != -1 && colIndex != -1 )
			propsTable.getCellEditor( rowIndex, colIndex ).stopCellEditing();

		if( rowIndex != -1 && 
		    propsTableModel.getValueAt( rowIndex, 0 ).toString().equals( "" ) )
			JOptionPane.showMessageDialog( VendorPropsViewPanel.this,
										   CANNOT_ADD_PROP_WITH_EMPTY_NAME_MSG );

		propsTableModel.setEditable( false );

		if( propsTableModel.isJposEntryModified() )
			fireJposEntryChanged( new PropsViewPanel.Event( this ) );

		propsTableModel.updateJposEntry();
		
		addButton.setEnabled( false );
		removeButton.setEnabled( false );
    }

    /** Called when the "Cancel" command button is clicked */
    public void cancelButtonClicked()
    {
		int rowIndex = propsTable.getEditingRow();
		int colIndex = propsTable.getEditingColumn();

        if( propsTableModel.isJposEntryModified() )
		{
			int userChoice = JOptionPane.showConfirmDialog( this, 
							 SAVE_CHANGES_TO_JPOSENTRY_QUESTION_MSG,
							 VENDOR_PROPS_TABLE_TITLE, JOptionPane.YES_NO_OPTION );

			if( userChoice == JOptionPane.YES_OPTION )
			{
				if( rowIndex != -1 && colIndex != -1 )
					propsTable.getCellEditor( rowIndex, colIndex ).stopCellEditing();

				if( propsTableModel.isJposEntryModified() )
					fireJposEntryChanged( new PropsViewPanel.Event( this ) );

				propsTableModel.updateJposEntry();
			}
		}

		if( rowIndex != -1 && colIndex != -1 )
			propsTable.getCellEditor( rowIndex, colIndex ).cancelCellEditing();

		propsTableModel.cancelModifications();
		propsTableModel.setEditable( false );

		addButton.setEnabled( false );
		removeButton.setEnabled( false );
    }
			  
    //-------------------------------------------------------------------------
    // Intance variables
    //

	private JButton addButton = new JButton( ADD_STRING );
	private JButton removeButton = new JButton( REMOVE_STRING );

	private PropsTableModel propsTableModel = this.new PropsTableModel();
    private JTable propsTable = new JTable( propsTableModel );;

	//-------------------------------------------------------------------------
	// Inner classes
	//

	/**
	 * TableModel inner class for this VendorProsViewPanel
	 * @author E. Michael Maximilien (maxim@us.ibm.com) 
	 * @since 1.3 (Washington DC 2001)
	 */
	class PropsTableModel extends AbstractTableModel
	{
		//---------------------------------------------------------------------
		// Ctor()
		//

		/** Default ctor */
		PropsTableModel() {}

		//---------------------------------------------------------------------
		// Public methods
		//
		
		/**
		 * Sets the JposEntry to be edited
		 * @param entry the entry
		 */
		public void setJposEntry( JposEntry entry )
		{
			jposEntry = entry;

			Enumeration vendorPropNames = JposEntryUtility.
										  getVendorPropNames( jposEntry );

			props.clear();
			
			addedProps.clear();
			removedProps.clear();
			changedProps.clear();

			while( vendorPropNames.hasMoreElements() )
			{
				String propName = (String)vendorPropNames.nextElement();
				Object propValue = jposEntry.getPropertyValue( propName );

				props.add( new SimpleEntry.Prop( propName, propValue  ) );

				Collections.sort( props );
			}

			fireTableDataChanged();
		}

		/** Adds an empty property */
		public void add() { add( "", "" ); }

		/** 
		 * Adds a new JposEntry.Prop to the model
		 * @param name the property name to add
		 * @param value the property value to add
		 */
		public void add( String name, Object value )
		{
			JposEntry.Prop prop = new SimpleEntry.Prop( name, value );

			props.add( prop );
			addedProps.add( prop );

			removeButton.setEnabled( true );

			fireTableDataChanged();
		}

		/**
		 * Deletes the row index specified
		 * @param rowIndex the row index to delete
		 */
		public void delete( int rowIndex )
		{
			if( rowIndex >= 0 && rowIndex < props.size() )
			{
				JposEntry.Prop prop = (JposEntry.Prop)props.remove( rowIndex );

				removedProps.add( prop );
				addedProps.remove( prop );
			}

			if( props.size() == 0 )
				removeButton.setEnabled( false );

			fireTableDataChanged();
		}

		/** 
		 * Clears all by setting jposEntry to null and clearing 
		 * the JposEntry.Prop list 
		 */
		public void clearAll()
		{
			props.clear();
			
			addedProps.clear();
			removedProps.clear();
			changedProps.clear();

			jposEntry = EMPTY_JPOSENTRY;			

			fireTableDataChanged();
		}

		/** 
		 * Sets whether the table model is editable or not
		 * @param b the boolean parameter
		 */
		public void setEditable( boolean b ) { editable = b; }

		/** Updates the JposEntry with added, removed and changed property */
		public void updateJposEntry()
		{
			Iterator addedIterator = addedProps.iterator();
			while( addedIterator.hasNext() )
			{
				JposEntry.Prop prop = (JposEntry.Prop)addedIterator.next();

				if( !prop.getName().equals( "" ) )
                    jposEntry.add( prop );
			}

			Iterator removedIterator = removedProps.iterator();
			while( removedIterator.hasNext() )
			{
				JposEntry.Prop prop = (JposEntry.Prop)removedIterator.next();

				if( !prop.getName().equals( "" ) )
                    jposEntry.remove( prop );
			}
									  
			Iterator changedIterator = changedProps.iterator();
			while( changedIterator.hasNext() )
			{
				JposEntry.Prop prop = (JposEntry.Prop)changedIterator.next();

				if( !prop.getName().equals( "" ) )
                    jposEntry.modifyPropertyValue( prop.getName(), prop.getValue() );
			}

			setJposEntry( jposEntry );
		}

		/** Cancel all modifications */
		public void cancelModifications() { setJposEntry( jposEntry ); }

		/** @return true if any property was changed, added or removed */
		public boolean isJposEntryModified()
		{
			return ( ( changedProps.size() > 0 ) || ( addedProps.size() > 0 ) ||
					 ( removedProps.size() > 0 ) );
		}

		//---------------------------------------------------------------------
		// Public overridden methods
		//

		/** 
		 * @return the column name at column
		 * @param column the column number
		 */
		public String getColumnName( int column ) 
		{ return COLUMN_NAMES[ column ]; }

		/** @return the number of column in the table */
		public int getColumnCount() { return COLUMN_NAMES.length; }

		/** @return the current number of rows in the table */
		public int getRowCount(){ return props.size(); }

		/**
		 * Sets the value at the <rowIndex, columnIndex>
		 * @param value the Object value to set
		 * @param rowIndex the row index
		 * @param columnIndex the column index
		 */
		public void setValueAt( Object value, int rowIndex, int columnIndex ) 
		{
			if( rowIndex >= props.size() ) return;
			
            JposEntry.Prop prop = (JposEntry.Prop)props.get( rowIndex );

			String typeName = getValueAt( rowIndex, 2 ).toString();

			if( columnIndex == 1 ) 
			{
				String valueString = value.toString();

				try
				{
					Class propType = JposEntryUtility.propTypeFromString( typeName );
					Object propValue = JposEntryUtility.parsePropValue( valueString, propType );

					if( JposEntryUtility.validatePropValue( propValue, propType ) )
						prop.setValue( propValue );
					else
					{
						JOptionPane.
						showMessageDialog( VendorPropsViewPanel.this,
						 				   PROPERTY_VALUE_IS_NOT_VALID_FOR_TYPE_MSG );

						return;
					}

					if( !changedProps.contains( prop ) )
						changedProps.add( prop );

					fireTableDataChanged();
				}
				catch( JposConfigException jce )
				{
					JOptionPane.
					showMessageDialog( VendorPropsViewPanel.this,
									   PROPERTY_TYPE_IS_NOT_VALID_FOR_VALUE_MSG );
				}
			}
			else
			if( columnIndex == 2 )
			{
				String valueString = getValueAt( rowIndex, 1 ).toString();

				Class propType = null;
				Object propValue = null;

				try
				{
					propType = JposEntryUtility.propTypeFromString( value.toString() );
					propValue = JposEntryUtility.parsePropValue( valueString, propType );

					if( JposEntryUtility.validatePropValue( propValue, propType ) )
						prop.setValue( propValue );
//					else
//					{
//						
//						JOptionPane.
//						showMessageDialog( VendorPropsViewPanel.this,
//						 				   PROPERTY_VALUE_IS_NOT_VALID_FOR_TYPE_MSG );
//
//						return;
//					}

					if( !changedProps.contains( prop ) )
						changedProps.add( prop );

					fireTableDataChanged();
				}
				catch( JposConfigException jce )
				{
					try{ prop.setValue( JposEntryUtility.getDefaultValueForType( propType ) ); }
					catch( JposConfigException e ) {}
//					JOptionPane.
//					showMessageDialog( VendorPropsViewPanel.this,
//									   PROPERTY_TYPE_IS_NOT_VALID_FOR_VALUE_MSG );
				}
			}
			else
			{
				if( value.toString().equals( "" ) )
				{
					JOptionPane.
					showMessageDialog( VendorPropsViewPanel.this,
									   CANNOT_ADD_PROP_WITH_EMPTY_NAME_MSG );
					return;
				}

				if( JposEntryUtility.isRequiredPropName( value.toString() ) )
				{
					JOptionPane.
					showMessageDialog( VendorPropsViewPanel.this,
									   CANNOT_ADD_STANDARD_PROPERTY_IN_VENDOR_PROPS_PANEL_MSG );
					return;
				}

				if( JposEntryUtility.isRS232PropName( value.toString() ) )
				{
					JOptionPane.
					showMessageDialog( VendorPropsViewPanel.this,
									   CANNOT_ADD_RS232_PROPERTY_IN_VENDOR_PROPS_PANEL_MSG );
					return;
				}

				if( !prop.getName().equals( value.toString() ) &&
				    isNameInUse( value.toString() ) )
				{
					JOptionPane.
					showMessageDialog( VendorPropsViewPanel.this,
									   PROPERTY_BY_THIS_NAME_ALREADY_EXISTS_MSG );
					return;
				}

				if( !prop.getName().equals( value.toString() ) )
				{
					if( !removedProps.contains( prop ) )
						removedProps.add( prop.copy() );

					prop.setName( value.toString() );

					if( !addedProps.contains( prop ) )
						addedProps.add( prop );
				}

				fireTableDataChanged();
			}
		}

        /**
		 * @return the value in the table at <rowIndex, columnIndex>
		 * @param rowIndex the row index
		 * @param columnIndex the column index
		 */
		public Object getValueAt( int rowIndex, int columnIndex ) 
		{
			JposEntry.Prop prop = (JposEntry.Prop)props.get( rowIndex );

			switch( columnIndex )
			{
				case 0:
					return prop.getName();

				case 1:
					return formatPropValue( prop );

				case 2:
					return JposEntryUtility.shortClassName( prop.getType() );

				default:
					throw new RuntimeException( "Cannot have columnIndex > 2" );
			}
		}

		/** @return true if the cell is editable */
		public boolean isCellEditable( int rowIndex, int columnIndex ) 
		{ return editable; }

		//---------------------------------------------------------------------
		// Private methods
		//

		/**
		 * @return a formated String with the property value.  If the property
		 * if of Integer type then return hex or decimal depending on editor config
		 * @param prop the JposEntry.Prop object
		 */
		private String formatPropValue( JposEntry.Prop prop )
		{
			Class propType = prop.getType();
			String stringValue = "";

			if( getMainFrame().getShowAsHexFlag() == false )
				return prop.getValue().toString();
			else
			{
				if( propType.equals( Integer.class ) )
					stringValue = "0x" + Integer.toHexString( ( (Integer)prop.getValue() ).
										 intValue() ).toUpperCase();
				else
				if( propType.equals( Long.class ) )
					stringValue = "0x" + Long.toHexString( ( (Long)prop.getValue() ).
										 longValue() ).toUpperCase();
				else
				if( propType.equals( Byte.class ) )
					stringValue = "0x" + Integer.toHexString( ( (Byte)prop.getValue() ).
										 byteValue() ).toUpperCase();
				else
				if( propType.equals( Short.class ) )
					stringValue = "0x" + Integer.toHexString( ( (Short)prop.getValue() ).
										 shortValue() ).toUpperCase();
				else
					stringValue = prop.getValue().toString();
			}

			return stringValue;
		}

		/** 
		 * @return true if the name passed already is in the list of property 
		 * @param name the String name to check
		 */
		private boolean isNameInUse( String name )
		{
			Iterator iterator = props.iterator();

			while( iterator.hasNext() )
				if( ( (JposEntry.Prop)iterator.next() ).getName().equals( name ) )
					return true;

			return false;
		}

		//---------------------------------------------------------------------
		// Private instance variables
		//

		private boolean editable = false;

		private JposEntry jposEntry = null;
		private List props = new ArrayList();

		private List changedProps = new ArrayList();
		private List addedProps = new ArrayList();
		private List removedProps = new ArrayList();

		//---------------------------------------------------------------------
		// Private constants
		//

		private final String[] COLUMN_NAMES = 
					   { PROP_NAME_STRING, PROP_VALUE_STRING, PROP_TYPE_STRING };
	}

    //-------------------------------------------------------------------------
    // Class constants
    //

	public static final JposEntry EMPTY_JPOSENTRY = new SimpleEntry();

	//<i18n>
	public static final String PROP_NAME_STRING = "Property Name";
	
	public static final String PROP_VALUE_STRING = "Property Value";
	
	public static final String PROP_TYPE_STRING = "Property Type";
	
	public static final String VENDOR_PROPS_TABLE_TITLE = "Vendor Properties";
	
	public static final String ADD_STRING = "Add";
	
	public static final String REMOVE_STRING = "Remove";
	
	public static final String ADD_BUTTON_TTEXT_STRING = 
								 "Add a new property";

	public static final String REMOVE_BUTTON_TTEXT_STRING = 
								 "Remove the selected property";
								 
	public static final String CANNOT_ADD_STANDARD_PROPERTY_IN_VENDOR_PROPS_PANEL_MSG = 
		 					     "Cannot add a standard property in the Vendor Properties";
		 					     
	public static final String CANNOT_ADD_RS232_PROPERTY_IN_VENDOR_PROPS_PANEL_MSG = 
							     "Cannot add an RS232 property in the Vendor Properties";
							     
	public static final String CANNOT_ADD_PROP_WITH_EMPTY_NAME_MSG = 
								 "Cannot add a property with an empty name";
								 
	public static final String REMOVE_SELECTED_PROPERTY_FROM_ENTRY_MSG = 
								 "Remove selected property from entry?";
								 
	public static final String SAVE_CHANGES_TO_JPOSENTRY_QUESTION_MSG = 
								 "Save changes to JposEntry?";
								 
	public static final String PROPERTY_BY_THIS_NAME_ALREADY_EXISTS_MSG = 
								 "A property by this name already exists...";
								 
	public static final String PROPERTY_VALUE_IS_NOT_VALID_FOR_TYPE_MSG = 
								 "Property value is not valid for type selected...";
								 
	public static final String PROPERTY_TYPE_IS_NOT_VALID_FOR_VALUE_MSG = 
								 "Property type is not valid for value selected...";
	
	public static final String SELECT_AN_OPTION_MSG = "Select an Option";								 
	//</i18n>
}