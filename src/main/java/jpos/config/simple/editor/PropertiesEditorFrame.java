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
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.io.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import jpos.util.*;
import jpos.loader.*;

/**
 * JFrame that allows user to view and edit the jpos.properties editor
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (Washington DC 2001 meeting)
 */
public class PropertiesEditorFrame extends JFrame
{
    //-------------------------------------------------------------------------
    // Ctor
    //

    /** Default ctor */
    public PropertiesEditorFrame() 
	{
		setTitle( PROPERTIES_EDITOR_TITLE_STRING + " - " + 
		          JposProperties.JPOS_PROPERTIES_FILENAME );

		getContentPane().setLayout( new BorderLayout() );

		JPanel propPanel = new JPanel( new BorderLayout() );
		propPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.gray ),
															   JPOSPROPERTIES_TABLE_STRING ) );

		setupTable();

		JScrollPane jScrollPane = new JScrollPane( propsTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
												   JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );

		propPanel.add( jScrollPane );
		getContentPane().add( propPanel, BorderLayout.CENTER );

		JPanel commandPanel = new JPanel();

		commandPanel.setLayout( new BoxLayout( commandPanel, BoxLayout.Y_AXIS ) );

		commandPanel.add( new JPanel() );

		commandPanel.add( addButton );
		commandPanel.add( deleteButton );

		commandPanel.add( new JPanel() );

		commandPanel.add( refreshButton );
		commandPanel.add( saveButton );
		commandPanel.add( saveAsButton );
		commandPanel.add( closeButton );

		commandPanel.add( new JPanel() );

		getContentPane().add( commandPanel, BorderLayout.EAST );

		refreshButton.addActionListener(	new ActionListener()
											{
												public void actionPerformed( ActionEvent e ) 
												{ refresh(); }
											}
									   );

		saveButton.addActionListener(	new ActionListener()
										{
											public void actionPerformed( ActionEvent e ) 
											{ save(); }
										}
									   );

		saveAsButton.addActionListener(	new ActionListener()
										{
											public void actionPerformed( ActionEvent e ) 
											{ saveAs(); }
										}
									   );

		closeButton.addActionListener(	new ActionListener()
										{
											public void actionPerformed( ActionEvent e ) 
											{ close(); }
										}
									   );

		addButton.addActionListener(	new ActionListener()
										{
											public void actionPerformed( ActionEvent e ) 
											{ add(); }
										}
									   );

		deleteButton.addActionListener(	new ActionListener()
										{
											public void actionPerformed( ActionEvent e ) 
											{ delete(); }
										}
									   );

		addWindowListener( 	new WindowAdapter()
							{
								public void windowClosing( WindowEvent e ) 
								{ close(); }
                            }
						 );

		setSize( 660, 210 );
		centerFrame();

		//<temp>
		refreshButton.setEnabled( false );
		saveButton.setEnabled( false );
		//</temp>
	}

	//-------------------------------------------------------------------------
    // Private methods
    //

	private void setupTable()
	{
		propsTableModel = new PropsTableModel();
		propsTable = new JTable( propsTableModel );

		JComboBox jComboBox = new JComboBox();
		jComboBox.setEditable( true );

		Iterator propNames = DefaultProperties.getDefinedPropertyNames();

		while( propNames.hasNext() )
			jComboBox.addItem( (String)propNames.next() );

		TableColumn propNameColumn = (TableColumn)propsTable.getColumn( PROP_NAME_STRING );
		
		propNameColumn.setCellEditor( new DefaultCellEditor( jComboBox ) );

		propsTable.setRowHeight( INITIAL_ROWHEIGHT );

		propsTableModel.setProperties( JposServiceLoader.getManager().getProperties() );
	}

	/** Called when "Refresh" button clicked */
	private void refresh()
	{
		throw new RuntimeException( "Not yet implemented..." );
	}

	/** Called when "Save" button clicked */
	private void save()
	{
		throw new RuntimeException( "Not yet implemented... Need to find out how to save file in loaded JAR file" );
	}

	/** Called when "Save As..." button clicked */
	private void saveAs()
	{
		Cursor oldCursor = getCursor();

		try
		{
			setCursor( new Cursor( Cursor.WAIT_CURSOR ) );

			int code = getJFileChooser().showSaveDialog( this );

			if( code == JFileChooser.APPROVE_OPTION )
			{
				Properties properties = propsTableModel.getProperties();

				properties.store( new FileOutputStream( getJFileChooser().getSelectedFile() ),
								  JCL_PROPERTIES_FILE_SAVED_MSG );
			}
		}
		catch( IOException ioe )
		{ JOptionPane.showMessageDialog( this, ERROR_SAVING_PROPERTIES_FILE_MSG ); }
		finally { setCursor( oldCursor ); }

	}

	/** Called when "Add" button clicked */
	private void add() { propsTableModel.add( DEFAULT_PROP_NAME_STRING, DEFAULT_PROP_VALUE_STRING ); }

	/** Called when "Delete" button clicked */
	private void delete()
	{
		int selectedRowIndex = propsTable.getSelectedRow();
		propsTableModel.delete( selectedRowIndex );
	}

	/** Called when "Close" button clicked */
	private void close()
	{
		int defaultClose = getDefaultCloseOperation();

		if( defaultClose == JFrame.EXIT_ON_CLOSE )
			System.exit( 0 );
		else
			setVisible( false );
	}

    /** Centers the frame on the screen */
    private void centerFrame()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setLocation( ( screenSize.width / 2 ) - ( getSize().width / 2 ),
                     ( screenSize.height / 2 ) - ( getSize().height / 2 ) );
    }

	/** @return a lazily created JFileChooser object */
	private JFileChooser getJFileChooser()
	{
		if( jFileChooser == null )
		{
			jFileChooser = new JFileChooser();
			jFileChooser.setDialogTitle( SAVING_DIALOG_TITLE_STRING );
			jFileChooser.setDialogType( JFileChooser.SAVE_DIALOG );
		}

		return jFileChooser;
	}

	//-------------------------------------------------------------------------
    // Public methods
    //

	/**
	 * Updates the JposProperties that will be loaded and edited
	 * @param props the JposProperties object
	 */
	public void setProperties( JposProperties props )
	{
		propsTableModel.setProperties( props );

		saveAsButton.setEnabled( true );

		addButton.setEnabled( true );
		deleteButton.setEnabled( true );

		if( props.size() > 0 ) deleteButton.setEnabled( true );
	}

    //-------------------------------------------------------------------------
    // Instance variables
    //

	private PropsTableModel propsTableModel = null;
	private JTable propsTable = null;

	private JButton refreshButton = new JButton( REFRESH_BUTTON_STRING );
	private JButton saveButton = new JButton( SAVE_BUTTON_STRING );
	private JButton saveAsButton = new JButton( SAVE_AS_BUTTON_STRING );
	private JButton closeButton = new JButton( CLOSE_BUTTON_STRING );

	private JButton addButton = new JButton( ADD_BUTTON_STRING );
	private JButton deleteButton = new JButton( DELETE_BUTTON_STRING );

	private JFileChooser jFileChooser = null;

    //-------------------------------------------------------------------------
	// Inner classes
	//

	/**
	 * TableModel inner class for this PropertiesEditorFrame
	 * @author E. Michael Maximilien  (maxim@us.ibm.com)
	 */
	class PropsTableModel extends AbstractTableModel
	{
		//---------------------------------------------------------------------
		// Ctor
		//

		/** Default ctor */
		public PropsTableModel() {}

		//---------------------------------------------------------------------
		// Public methods
		//
		
		/**
		 * Sets the JposProperties to be edited
		 * @param properties the JposProperties
		 */
		public void setProperties( JposProperties properties )
		{
			jposProps = properties;

			Iterator iterator = jposProps.getProps();

			props.clear();

			while( iterator.hasNext() )
				props.add( iterator.next() );

			Collections.sort( props, DefaultProperties.propComparator() );
		}

		/** @return a Properties object with all properties in this model */
		public Properties getProperties()
		{
			Properties properties = new Properties();

			Iterator propIterator = props.iterator();

			while( propIterator.hasNext() )
			{
				JposProperties.Prop prop = (JposProperties.Prop)propIterator.next();
				properties.put( prop.getName(), prop.getValue() );
			}

			return properties;
		}

		/** 
		 * Adds a new JposProperties.Prop to the model
		 * @param name the property name to add
		 * @param value the property value to add
		 */
		public void add( String name, String value )
		{
			JposProperties.Prop prop = new DefaultProperties.Prop( name, value );

			props.add( prop );

			fireTableDataChanged();
		}

		/**
		 * Deletes the row index specified
		 * @param rowIndex the row index to delete
		 */
		public void delete( int rowIndex )
		{
			if( rowIndex >= 0 && rowIndex < props.size() )
				props.remove( rowIndex );

			fireTableDataChanged();
		}

		//---------------------------------------------------------------------
		// Public overridden methods
		//

		/** 
		 * @return the column name at column
		 * @param column the column number
		 */
		public String getColumnName( int column ) { return COLUMN_NAMES[ column ]; }

		/** @return the number of column in the table */
		public int getColumnCount() { return COLUMN_NAMES.length; }

		/** @return the current number of rows in the table */
		public int getRowCount(){ return props.size(); }

		/**
		 * Sets the value at the <rowIndex, columnIndex>
		 * @param rowIndex the row index
		 * @param columnIndex the column index
		 */
		public void setValueAt( Object value, int rowIndex, int columnIndex ) 
		{
			JposProperties.Prop prop = (JposProperties.Prop)props.get( rowIndex );

			if( columnIndex == 1 )
				prop.setValue( value.toString() );
			else
				prop.setName( value.toString() );
		}

        /**
		 * @return the value in the table at <rowIndex, columnIndex>
		 * @param rowIndex the row index
		 * @param columnIndex the column index
		 */
		public Object getValueAt( int rowIndex, int columnIndex ) 
		{
			JposProperties.Prop prop = (JposProperties.Prop)props.get( rowIndex );

			return ( columnIndex == 0 ? prop.getName() : prop.getValue() );
		}

		/** @return true if the cell is editable */
		public boolean isCellEditable( int rowIndex, int columnIndex ) { return true; }

		//---------------------------------------------------------------------
		// Private instance variables
		//

		private JposProperties jposProps = null;
		private List props = new ArrayList();

		//---------------------------------------------------------------------
		// Private constants
		//

		private final String[] COLUMN_NAMES = { PROP_NAME_STRING, PROP_VALUE_STRING };
	}					 

    //-------------------------------------------------------------------------
    // Class constants
    //

	public static final int INITIAL_ROWHEIGHT = 20;

	//<i18n>
	public static final String PROPERTIES_EDITOR_TITLE_STRING = "JposProperties Editor";
	public static final String REFRESH_BUTTON_STRING = "Refresh";
	public static final String SAVE_BUTTON_STRING = "Save";
	public static final String SAVE_AS_BUTTON_STRING = "Save As...";
	public static final String CLOSE_BUTTON_STRING = "Close";

	public static final String ADD_BUTTON_STRING = "Add";
	public static final String DELETE_BUTTON_STRING = "Delete";

	public static final String PROP_NAME_STRING = "Property Name";
	public static final String PROP_VALUE_STRING = "Property Value";

	public static final String DEFAULT_PROP_NAME_STRING = "default.prop.name";
	public static final String DEFAULT_PROP_VALUE_STRING = "default.prop.value";

	public static final String JPOSPROPERTIES_TABLE_STRING = "JposProperties <name, value> pairs";

	public static final String SAVING_DIALOG_TITLE_STRING = "Save JCL jpos.properties file";
	public static final String JCL_PROPERTIES_FILE_SAVED_MSG = "JCL properties file saved by JposProperties Editor";
	public static final String ERROR_SAVING_PROPERTIES_FILE_MSG = "Error saving properties file";
	//</i18n>
}
