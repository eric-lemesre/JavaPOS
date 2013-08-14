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
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;

import jpos.config.*;
import jpos.profile.*;

/**
 * This panel displays only Standard JavaPOS properties
 * @author S. Hanai
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (Wahsington DC 2001 meeting)
 */
class StandardPropsViewPanel extends AbstractPropsViewPanel
{
	//-------------------------------------------------------------------------
	// Inner classes
	//

    /**
     * This is a dialog box which has a capability to edit multiline
     * @author S. Hanai
	 */
    class MultiLinePropertyEditor extends JDialog
    {
		//---------------------------------------------------------------------
		// Ctor(s)
		//

        /** ctor */
        public MultiLinePropertyEditor()
        {
            super ((Frame)null, " ", true);
            this.getContentPane().setLayout(gridLayout1);
            jPanel1.setLayout(gridBagLayout1);
            okBtn.setText(JposEntryEditorMsg.OKBUTTON_STRING);
            cancelBtn.setText(JposEntryEditorMsg.CANCELBUTTON_STRING);
            jTextArea.setText("");
            this.getContentPane().add(jPanel1, null);
            jPanel1.add(okBtn,
                        new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                                               GridBagConstraints.EAST,
                                               GridBagConstraints.NONE,
                                               new Insets(0, 0, 0, 10), 0, 0));
            jPanel1.add(cancelBtn,
                        new GridBagConstraints(1, 1, 1, 2, 1.0, 1.0,
                                               GridBagConstraints.WEST,
                                               GridBagConstraints.NONE,
                                               new Insets(0, 10, 0, 0), 0, 0));
            jPanel1.add(jScrollPane1,
                        new GridBagConstraints(0, 0, 2, 1, 1.0, 40.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));
            jScrollPane1.getViewport().add(jTextArea, null);
            this.setBounds(100, 100, 300, 300);
            okBtn.addActionListener(	new ActionListener()
										{
											public void actionPerformed( ActionEvent e )
											{
												canceled = false;
												hide();
											}
										}
									);

            cancelBtn.addActionListener(new ActionListener()
										{
											public void actionPerformed( ActionEvent e )
											{ hide(); }
										}
									   );
        }

		//---------------------------------------------------------------------
		// Public methods
		//

        /**
         * Set text to edit area
         * @param text Text to be edited.
		 */
        public void setPropertyValue( String text ) { jTextArea.setText( text ); }

        /**
         * Get text in edit area
         * @return Text in edit area
         * 
		 */
        public String getPropertyValue() { return jTextArea.getText(); }

        /** Show this dialog box and start editing */
        public void show()
        {
            canceled = true;
            super.show();
        }

        /**
         * True if this dialog box is closed by cancel button.
         * @return Text in edit area
         */
        public boolean isCanceled() { return canceled; }

		//---------------------------------------------------------------------
		// Instance variables
		//

        private boolean canceled = false;
        private GridLayout gridLayout1 = new GridLayout();
        private JPanel jPanel1 = new JPanel();
        private GridBagLayout gridBagLayout1 = new GridBagLayout();
        private JButton okBtn = new JButton();
        private JButton cancelBtn = new JButton();
        private JScrollPane jScrollPane1 = new JScrollPane();
        private JTextArea jTextArea = new JTextArea();
    }

    /**
     * A property change listener. MyTextArea has a editor implemented
     * by JDialog. When a client wants to know text is changed by this
     * dialog based editor, add linstener by
     * addPropertyChangedListener().
     * @author S. Hanai
     * @since 1.3 (Washington DC 2001 meeting)
     */
    interface PropertyChangeListener
    {
        /**
         * Called if property is changed.
         * @param newValue new value
         */
        void propertyChanged( String newValue );
    }

    /**
     * MyTextArea
     * A JTextArea added dialog based editor. If the client area is
     * double clicked, dialog based editor is invoked.
     * @author S. Hanai
     * @since 1.3 (Washington DC 2001 meeting)
	 */
    class MyTextArea extends JTextArea
    {
		//---------------------------------------------------------------------
		// Ctor(s)
		//

        /**
         * ctor
         * @param propertyName name of property to be edited
         */
        public MyTextArea(String propertyName)
        {
            super (" ", 4, 20);
            addMouseListener(new MouseListener()
                {
                    public void mouseClicked(MouseEvent e)
                    {
                        if (e.getClickCount() == 2)
                        {
                            mlEditor.setPropertyValue(getText());
                            mlEditor.show();
                            if (! mlEditor.isCanceled())
                            {
                                String text = mlEditor.getPropertyValue();
                                setText(text);
                                synchronized (propertyChangeListeners)
                                {
                                    for (Enumeration itr = propertyChangeListeners.elements();
                                         itr.hasMoreElements();)
                                    {
                                        PropertyChangeListener pcl =
                                            (PropertyChangeListener)itr.nextElement();
                                        pcl.propertyChanged(text);
                                    }
                                }
                            }
                        }
                    }

                    public void mousePressed( MouseEvent e ){}
                    public void mouseReleased( MouseEvent e ){}
                    public void mouseEntered( MouseEvent e ){}
                    public void mouseExited( MouseEvent e ){}
                });

            mlEditor.setTitle(propertyName);
        }

		//---------------------------------------------------------------------
		// Public methods
		//

        /**
         * add a property change listener.
         * If property is changed by dialog based editor, listener is notified.
         * @param pcl listener
         */
        public void addPropertyChangeListener( PropertyChangeListener pcl )
        {
            synchronized( propertyChangeListeners )
            { propertyChangeListeners.addElement( pcl ); }
        }

		//---------------------------------------------------------------------
		// Instance variables
		//

        private Vector propertyChangeListeners = new Vector();
        private MultiLinePropertyEditor mlEditor = new MultiLinePropertyEditor();
    }

    /**
     * Abstract base class of table.
     * @author S. Hanai
     * @since 1.3 (Washington DC 2001 meeting)
     * */
    abstract class MyTable extends JScrollPane
    {
		//---------------------------------------------------------------------
		// Ctor(s)
		//

        /**
         * Takes the list of property names for this table
         * @param propertyNames An array of property name
		 */
        public MyTable( String[] propertyNames )
        {
			this.propertyNames = propertyNames;
			dataModel = this.new DefaultDataModel();
        }

		/** Empty no-org ctor */
		public MyTable() {}

		//---------------------------------------------------------------------
		// Protected methods
		//

        protected void initialize() { initialize( null ); }

        protected void initialize( TableCellEditor cellEditor )
        {
            TableColumnModel tableColumnModel = new DefaultTableColumnModel();
            TableColumn tableColumn = new TableColumn( 0, 150, new DefaultTableCellRenderer(), null);
            tableColumn.setHeaderValue( PROPERTY_NAME );
            tableColumnModel.addColumn( tableColumn );
            tableColumn = new TableColumn( 1, 300, new DefaultTableCellRenderer(), cellEditor );
            tableColumn.setHeaderValue( PROPERTY_VALUE );
            tableColumnModel.addColumn( tableColumn );
            table = new JTable( dataModel, tableColumnModel );
            setViewportView( table);
        }

		protected void setEditable( boolean b ) { editable = b; }

		//---------------------------------------------------------------------
		// Public abstract methods
		//

		/**
		 * Updates the JposEntry with properties value of this table
		 * @param entry the JposEntry to update
		 */
		public abstract void updateJposEntryFromView( JposEntry entry );

		/**
		 * @return true if the entry's prop value differ from view
		 * @param entry the JposEntry to compare
		 */
		public abstract boolean isJposEntryDifferentFromView( JposEntry entry );

		//---------------------------------------------------------------------
		// Instance variables
		//

        protected TableModel dataModel = null;
        protected JTable table = null;
        protected String[] propertyNames = null;
		protected boolean editable = false;

		//---------------------------------------------------------------------
		// Inner classes
		//

		/**
		 * Default TableModel for the MyTable class and subclasses
		 * @author E. Michael Maximilien (maxim@us.ibm.com)
		 * @author S. Hanai
		 * @since 2.0.0
		 */
		protected class DefaultDataModel extends AbstractTableModel
		{
			private String[] data =
			new String[ MyTable.this.propertyNames.length ];

			public int getColumnCount() { return 2; }

			public int getRowCount() { return data.length; }

			public Object getValueAt( int row, int col )
			{
				Object ret = null;
				if (col == 0) ret = MyTable.this.propertyNames[row];
				else ret = data[row];

				return ret;
			}

			public void setValueAt( Object aValue, int row, int col )
			{
				try
				{
					if( aValue == null ) aValue = "";
					data[ row ] = aValue.toString();
				}
				catch( NumberFormatException ex ) { ex.printStackTrace(); }
			}

			public boolean isCellEditable( int rowIndex, int columnIndex )
			{ return ( columnIndex == 0 ? false : true ); }
		}
    }

    /**
     * A table for creation property.
     * @author S. Hanai
     * @since 1.3 (Washington DC 2001 meeting)
     * */
    class CreationPropertyTable extends MyTable
    {
		//---------------------------------------------------------------------
		// Ctor(s)
		//

        /** ctor */
        public CreationPropertyTable()
        {
			propertyNames =	new String[]
							{
								JposEntry.LOGICAL_NAME_PROP_NAME,
								JposEntry.SI_FACTORY_CLASS_PROP_NAME,
								JposEntry.SERVICE_CLASS_PROP_NAME
							};

			dataModel = this.new DefaultDataModel()
						{
							public boolean isCellEditable( int row, int column ) 
							{
								if( row == 0 ) return false;

								return ( column == 0 ? false : true );
							}
						};
            
			initialize();
        }

		//---------------------------------------------------------------------
		// Public methods
		//

        /** @return logicl name */
        public String getLogicalName() { return (String)dataModel.getValueAt( 0, 1 ); }
    
        /**
         * Sets logical name
         * @param logicalName logicl name
         */
        public void setLogicalName(String logicalName)
        {
            dataModel.setValueAt(logicalName, 0, 1);
            table.tableChanged(new TableModelEvent(dataModel, 0));
        }
    
        /**
         * Gets factory class name
         * @return factory class name
         * */
        public String getFactoryClassName() { return (String)dataModel.getValueAt( 1, 1 ); }
    
        /**
         * Sets factory class name
         * @param factoryClassName Name of factory class for device service
         */
        public void setFactoryClassName( String factoryClassName )
        {
            dataModel.setValueAt( factoryClassName, 1, 1 );
            table.tableChanged( new TableModelEvent( dataModel, 1 ) );
        }
    
        /** @return Name of device service class name */
        public String getServiceClassName() { return (String)dataModel.getValueAt( 2, 1 ); }
    
        /**
         * Sets service class name
         * @param serviceClassName Name of device service class
         */
        public void setServiceClassName( String serviceClassName )
        {
            dataModel.setValueAt( serviceClassName, 2, 1 );
            table.tableChanged( new TableModelEvent( dataModel, 2 ) );
        }
    
        /**
         * Let this table be enabled
         * @param b Set true to be enabled
         */
        public void setEnabled( boolean b )
        {
            super.setEnabled(b);
            table.setEnabled(b);
        }

		/**
		 * Updates the JposEntry with properties value of this table
		 * @param entry the JposEntry to update
		 */
		public void updateJposEntryFromView( JposEntry entry )
		{
			entry.modifyPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME, 
									   getLogicalName() );
			
			entry.modifyPropertyValue( JposEntry.SI_FACTORY_CLASS_PROP_NAME,
									   getFactoryClassName() );

			entry.modifyPropertyValue( JposEntry.SERVICE_CLASS_PROP_NAME,
									   getServiceClassName() );
		}

		/**
		 * @return true if the entry's prop value differ from view
		 * @param entry the JposEntry to compare
		 */
		public boolean isJposEntryDifferentFromView( JposEntry entry )
		{
			Object logicalNamePropValue = entry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME );
			Object factoryClassPropValue = entry.getPropertyValue( JposEntry.SI_FACTORY_CLASS_PROP_NAME ); 
			Object serviceClassPropValue = entry.getPropertyValue( JposEntry.SERVICE_CLASS_PROP_NAME );

			if( !logicalNamePropValue.equals( getLogicalName() ) )
				return true;
			
			if( !factoryClassPropValue.equals( getFactoryClassName() ) )
				return true;

			if( !serviceClassPropValue.equals( getServiceClassName() ) )
				return true;

			return false;
		}
    }

    /**
     * A table for JavaPos property.
     * @author S. Hanai
     * @since 1.3 (Washington DC 2001 meeting)
     * */
    class JavaPosPropertyTable extends MyTable
    {
		//---------------------------------------------------------------------
		// Ctor(s)
		//

        /** ctor */
        public JavaPosPropertyTable()
        {
            super(	new String[]
					{
						JposEntry.JPOS_VERSION_PROP_NAME,
						JposEntry.DEVICE_CATEGORY_PROP_NAME
					}
				 );
            
			initialize( new JavaPosPropertyCellEditor() );

			table.setRowHeight( table.getRowHeight() + 5 );
        }

		//---------------------------------------------------------------------
		// Public methods
		//

        /** @return device category */
        public String getDeviceCategory() { return (String)dataModel.getValueAt( 1, 1 ); }
    
        /**
         * Sets device category
         * @param deviceCategory Device category
         */
        public void setDeviceCategory( String deviceCategory )
        {
            dataModel.setValueAt( deviceCategory, 1, 1 );
            table.tableChanged( new TableModelEvent( dataModel, 1 ) );
        }
    
        /** @return JavaPos version */
        public String getJposVersion() { return (String)dataModel.getValueAt( 0, 1 ); }

        /**
         * Sets JavaPos version
         * @param JavaPos version
         */
        public void setJposVersion( String deviceVersion )
        {
            dataModel.setValueAt( deviceVersion, 0, 1 );
            table.tableChanged( new TableModelEvent( dataModel, 0 ) );
        }

        /**
         * Let this table be enabled
         * @param b Set true to be enabled
         */
        public void setEnabled( boolean b )
        {
            super.setEnabled( b );
            table.setEnabled( b );
        }

		/**
		 * Updates the JposEntry with properties value of this table
		 * @param entry the JposEntry to update
		 */
		public void updateJposEntryFromView( JposEntry entry )
		{
			entry.modifyPropertyValue( JposEntry.JPOS_VERSION_PROP_NAME, 
									   getJposVersion() );
			
			entry.modifyPropertyValue( JposEntry.DEVICE_CATEGORY_PROP_NAME,
									   getDeviceCategory() );
		}

		/**
		 * @return true if the entry's prop value differ from view
		 * @param entry the JposEntry to compare
		 */
		public boolean isJposEntryDifferentFromView( JposEntry entry )
		{
			Object jposVersionPropValue = entry.getPropertyValue( JposEntry.JPOS_VERSION_PROP_NAME );
			Object deviceCategoryPropValue = entry.getPropertyValue( JposEntry.DEVICE_CATEGORY_PROP_NAME ); 

			if( !jposVersionPropValue.equals( getJposVersion() ) )
				return true;
			
			if( !deviceCategoryPropValue.equals( getDeviceCategory() ) )
				return true;

			return false;
		}
    }

    /**
     * A table for vendor property.
     * @author S. Hanai
     * @since 1.3 (Washington DC 2001 meeting)
     * */
    class VendorPropertyTable extends MyTable
    {
		//---------------------------------------------------------------------
		// Ctor(s)
		//

        /** ctor */
        public VendorPropertyTable()
        {
            super(	new String[]
					{
						JposEntry.VENDOR_NAME_PROP_NAME,
						JposEntry.VENDOR_URL_PROP_NAME	
					}
				 );

            initialize();
        }

		//---------------------------------------------------------------------
		// Public methods
		//

        /** @return Name of vendor */
        public String getVendorName() { return (String)dataModel.getValueAt( 0, 1 ); }

        /**
         * Sets vendor name
         * @param vendorName Name of vendor
         */
        public void setVendorName( String vendorName )
        {
            dataModel.setValueAt( vendorName, 0, 1 );
            table.tableChanged( new TableModelEvent( dataModel, 0 ) );
        }

        /** @return Vendor URL */
        public String getVendorURL() { return (String)dataModel.getValueAt( 1, 1 ); }

        /**
         * Sets vendor URL
         * @param vendor URL Vendor URL
         */
        public void setVendorURL( String vendorURL )
        {
            dataModel.setValueAt( vendorURL, 1, 1 );
            table.tableChanged( new TableModelEvent( dataModel, 1 ) );
        }

        /**
         * Let this table be enabled
         * @param b Set true to be enabled
		 */
        public void setEnabled( boolean b )
        {
            super.setEnabled( b );
            table.setEnabled( b );
		}

		/**
		 * Updates the JposEntry with properties value of this table
		 * @param entry the JposEntry to update
		 */
		public void updateJposEntryFromView( JposEntry entry )
		{
			entry.modifyPropertyValue( JposEntry.VENDOR_NAME_PROP_NAME, 
									   getVendorName() );
			
			entry.modifyPropertyValue( JposEntry.VENDOR_URL_PROP_NAME,
									   getVendorURL() );
		}

		/**
		 * @return true if the entry's prop value differ from view
		 * @param entry the JposEntry to compare
		 */
		public boolean isJposEntryDifferentFromView( JposEntry entry )
		{
			Object vendorNamePropValue = entry.getPropertyValue( JposEntry.VENDOR_NAME_PROP_NAME );
			Object vendorUrlPropValue = entry.getPropertyValue( JposEntry.VENDOR_URL_PROP_NAME ); 

			if( !vendorNamePropValue.equals( getVendorName() ) )
				return true;
			
			if( !vendorUrlPropValue.equals( getVendorURL() ) )
				return true;

			return false;
		}
    }

    /**
     * A table for product property.
     * @author S. Hanai
     * @since 1.3 (Washington DC 2001 meeting)
     * */
    class ProductPropertyTable extends MyTable
    {
		//---------------------------------------------------------------------
		// Ctor(s)
		//

        /** ctor */
        public ProductPropertyTable()
        {
            super( 	new String[]
					{
						JposEntry.PRODUCT_NAME_PROP_NAME,
						JposEntry.PRODUCT_URL_PROP_NAME,
						JposEntry.PRODUCT_DESCRIPTION_PROP_NAME
					}
				 );
            
			initialize( new ProductPropertyCellEditor( this ) );
        }

		//---------------------------------------------------------------------
		// Public methods
		//

        /** @return Product name */
        public String getProductName() { return (String)dataModel.getValueAt( 0, 1 ); }

        /**
         * Sets product name
         * @param productName Product name
         */
        public void setProductName( String productName )
        {
            dataModel.setValueAt( productName, 0, 1 );
            table.tableChanged( new TableModelEvent( dataModel, 0 ) );
        }

        /** @return Product URL */
        public String getProductURL() { return (String)dataModel.getValueAt( 1, 1 ); }

        /**
         * Sets product URL
         * @param productURL
         */
        public void setProductURL( String productURL )
        {
            dataModel.setValueAt( productURL, 1, 1 );
            table.tableChanged( new TableModelEvent( dataModel, 1 ) );
        }

        /** @return Product desctiption */
        public String getProductDescription() { return (String)dataModel.getValueAt( 2, 1 ); }

        /**
         * Sets product desctiption
         * @param productDesctiption Product desctiption
         */
        public void setProductDescription( String productDescription )
        {
            dataModel.setValueAt( productDescription, 2, 1 );
            table.tableChanged( new TableModelEvent( dataModel, 2 ) );
        }

        /**
         * Let this table be enabled
         * @param b Set true to be enabled
         */
        public void setEnabled(boolean b)
        {
            super.setEnabled(b);
            table.setEnabled(b);
        }

		/**
		 * Updates the JposEntry with properties value of this table
		 * @param entry the JposEntry to update
		 */
		public void updateJposEntryFromView( JposEntry entry )
		{
			entry.modifyPropertyValue( JposEntry.PRODUCT_NAME_PROP_NAME, 
									   getProductName() );
			
			entry.modifyPropertyValue( JposEntry.PRODUCT_URL_PROP_NAME,
									   getProductURL() );

			entry.modifyPropertyValue( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME,
									   getProductDescription() );
		}

		/**
		 * @return true if the entry's prop value differ from view
		 * @param entry the JposEntry to compare
		 */
		public boolean isJposEntryDifferentFromView( JposEntry entry )
		{
			Object productNamePropValue = entry.getPropertyValue( JposEntry.PRODUCT_NAME_PROP_NAME );
			Object productUrlPropValue = entry.getPropertyValue( JposEntry.PRODUCT_URL_PROP_NAME ); 
			Object productDescriptionPropValue = entry.getPropertyValue( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME ); 

			if( !productNamePropValue.equals( getProductName() ) )
				return true;
			
			if( !productUrlPropValue.equals( getProductURL() ) )
				return true;

			if( !productDescriptionPropValue.equals( getProductDescription() ) )
				return true;

			return false;
		}
    }

    /**
     * A cell editor for JavaPos Property
     * @author S. Hanai
     * @since 1.3 (Washington DC 2001 meeting)
     * */
    class JavaPosPropertyCellEditor extends AbstractCellEditor implements TableCellEditor
    {
        private JComboBox versionComboBox = new JComboBox( JposEntryConst.JPOS_VERSION_PROPS );
        private JComboBox devCatComboBox = new JComboBox( JposDevCats.DEVCAT_ARRAY );
    
        /** ctor */
        public JavaPosPropertyCellEditor()
        {
            versionComboBox.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        stopCellEditing();
                    }
                });
            versionComboBox.addItemListener(new ItemListener()
                {
                    public void itemStateChanged(ItemEvent e)
                    {
                        stopCellEditing();
                    }
                });

            devCatComboBox.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        stopCellEditing();
                    }
                });
            devCatComboBox.addItemListener(new ItemListener()
                {
                    public void itemStateChanged(ItemEvent e)
                    {
                        stopCellEditing();
                    }
                });
        }

        /** @return value */
        public Object getCellEditorValue() { return editorComponent.getSelectedItem(); }

        /**
         * Gets component for editor
         * @param table Table object
         * @param value Current value
         * @param isSelected True if being selected
         * @param row Row index
         * @param column Column index
         * @return component for editor
         * */
        public Component getTableCellEditorComponent(JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     int row,
                                                     int column)
        {
            switch (row)
            {
            case 0:
                versionComboBox.setSelectedItem(value);
                editorComponent = versionComboBox;
                break;

            case 1:
                devCatComboBox.setSelectedItem(value);
                editorComponent = devCatComboBox;
                break;

            default:
                editorComponent = null;
                break;
            }

            return editorComponent;
        }

        protected JComboBox editorComponent;
    }

    /**
     * A cell editor for product property
     * @author S. Hanai
     * @since 1.4
     * */
    class ProductPropertyCellEditor extends AbstractCellEditor implements TableCellEditor
    {
        MyTextArea descriptionTextArea = new MyTextArea( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME );
        JTextField jTextField = new JTextField(" ");
        ProductPropertyTable productPropertyTable = null;

        /** ctor */
        public ProductPropertyCellEditor(ProductPropertyTable productPropertyTable)
        {
            this.productPropertyTable = productPropertyTable;
            descriptionTextArea.addFocusListener(new FocusListener()
                {
                    public void focusLost(FocusEvent e)
                    {
                        stopCellEditing();
                    }

                    public void focusGained(FocusEvent e){}
                });
            descriptionTextArea.addPropertyChangeListener(new PropertyChangeListener()
                {
                    public void propertyChanged(String newValue)
                    {
                        ProductPropertyCellEditor.this.
                            productPropertyTable.setProductDescription(newValue);
                    }
                });

            jTextField.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        stopCellEditing();
                    }
                });          
        }

        /**
         * Gets value in editor
         * @return value
		 */
        public Object getCellEditorValue() { return editorComponent.getText(); }

        /**
         * Gets component for editor
         * @param table Table object
         * @param value Current value
         * @param isSelected True if being selected
         * @param row Row index
         * @param column Column index
         * @return component for editor
         */
        public Component getTableCellEditorComponent( JTable table,
													  Object value,
													  boolean isSelected,
													  int row,
                                                      int column )
        {
            if (value == null) value = "";
            switch (row)
            {
            case 0:
            case 1:
                jTextField.setText(value.toString());
                editorComponent = jTextField;
                break;

            case 2:
                descriptionTextArea.setText(value.toString());
                editorComponent = descriptionTextArea;
                break;

            default:
                editorComponent = null;
                break;
            }

            return editorComponent;
        }

        protected JTextComponent editorComponent;
    }

	//-------------------------------------------------------------------------
	// Ctor(s)
	//

    /** Default ctor */
    public StandardPropsViewPanel()
    {
        setLayout(new GridLayout(4, 1));
        setBorder(BorderFactory.createLineBorder(Color.gray));

        //"Creation" properties
        JPanel creationPropPanel = new JPanel(new GridLayout(1, 1));
        creationPropPanel.setLayout(new GridLayout(1, 1));
        creationPropPanel.setBorder
            (BorderFactory.createTitledBorder
             (BorderFactory.createLineBorder(Color.gray), 
              CREATION_PROPS_STRING));

        creationPropPanel.setPreferredSize(new Dimension(200, 60));
        creationPropPanel.add(creationPropertyTable);
        add(creationPropPanel);

        //"JavaPOS" properties
        JPanel jposPropPanel = new JPanel(new GridLayout(1, 1));
        jposPropPanel.setLayout(new GridLayout(1, 1));
        jposPropPanel.setBorder
            (BorderFactory.createTitledBorder
             (BorderFactory.createLineBorder(Color.gray), 
              JPOS_PROPS_STRING));
        jposPropPanel.setPreferredSize(new Dimension(200, 60));
        jposPropPanel.add(javaPosPropertyTable);
        add(jposPropPanel);

        //"Vendor" properties
        JPanel vendorPropPanel = new JPanel(new GridLayout(1, 1));
        vendorPropPanel.setBorder
            (BorderFactory.createTitledBorder
             (BorderFactory.createLineBorder( Color.gray ), 
              VENDOR_PROPS_STRING));
        vendorPropPanel.setPreferredSize(new Dimension(200, 60));
        vendorPropPanel.add(vendorPropertyTable);
        add( vendorPropPanel );

        //"Product" properties
        JPanel productPropPanel = new JPanel(new GridLayout(1, 1));
        productPropPanel.setBorder
            (BorderFactory.createTitledBorder
             (BorderFactory.createLineBorder(Color.gray), 
              PRODUCT_PROPS_STRING ) );
        productPropPanel.setPreferredSize(new Dimension(200, 60));
        productPropPanel.add(productPropertyTable);
        add( productPropPanel );
                
        init();
    }

    //--------------------------------------------------------------------------
    // Private intance methods
    //

    /** Initializes the UI components intial state */
    private void init()
    {
        clearAll();
        setEnabledAll( false );
    }

    /** @return device logical name */
    private String getLogicalName() { return creationPropertyTable.getLogicalName(); }

    /**
     * Sets device logical name to table.
     * @param s device logical name
     */
    private void setLogicalName( String s ) { creationPropertyTable.setLogicalName( s ); }

    /** @return factory class name */
    private String getFactoryClassName() { return creationPropertyTable.getFactoryClassName(); }

    /**
     * Sets factory class name to table.
     * @param s factory class name
     */
    private void setFactoryClassName( String s ) { creationPropertyTable.setFactoryClassName( s ); }

    /** @return service class name */
    private String getServiceClassName() { return creationPropertyTable.getServiceClassName(); }

    /**
     * Sets service class name to table.
     * @param s service class name
     */
    private void setServiceClassName( String s ) { creationPropertyTable.setServiceClassName( s ); }

    /**
     * Gets vendor name to table.
     * @return vendor's name
     */
    private String getVendorName() { return vendorPropertyTable.getVendorName(); }

    /**
     * Sets vendor name to table.
     * @param s the vendor's name
     */
    private void setVendorName( String s ) { vendorPropertyTable.setVendorName( s ); }

    /** @return vendorURL */
    private String getVendorURL() { return vendorPropertyTable.getVendorURL(); }

    /**
     * Sets vendor's URL to table.
     * @param s the vendorURL
     */
    private void setVendorURL( String s ) { vendorPropertyTable.setVendorURL( s ); }

    /**
     * Gets device category string to table.
     * @return device category string
     */
    private String getDeviceCategory() { return javaPosPropertyTable.getDeviceCategory(); }

    /**
     * Sets device category string to table.
     * @param s device category string
     */
    private void setDeviceCategory( String s ) { javaPosPropertyTable.setDeviceCategory( s ); }

    /** @return device version string */
    private String getJposVersion() { return javaPosPropertyTable.getJposVersion(); }

    /**
     * Sets device service version string to table.
     * @param s device version string
     */
    private void setJposVersion( String s ) { javaPosPropertyTable.setJposVersion( s ); }

    /** @return product name */
    private String getProductName() { return productPropertyTable.getProductName(); }

    /**
     * Sets product name to table.
     * @param s product name
     */
    private void setProductName( String s ) { productPropertyTable.setProductName( s ); }

    /**
     * Sets product desctioption to table.
     * @param s product desctiption
     */
    private void setProductDescription( String s )  { productPropertyTable.setProductDescription( s ); }

    /** @return product desctiption */
    private String getProductDescription() { return productPropertyTable.getProductDescription(); }

    /**
     * Sets product URL to table.
     * @param s product URL
     */
    private void setProductURL( String s ) { productPropertyTable.setProductURL( s ); }

    /** @return product URL */
    private String getProductURL() { return productPropertyTable.getProductURL(); }

	/**
	 * Updates the JposEntry from all the different views
	 * @param entry the JposEntry object
	 */
	private void updateJposEntryFromView( JposEntry entry )
	{
        creationPropertyTable.updateJposEntryFromView( entry );
        javaPosPropertyTable.updateJposEntryFromView( entry );
        vendorPropertyTable.updateJposEntryFromView( entry );
        productPropertyTable.updateJposEntryFromView( entry );

		fireJposEntryChanged( new PropsViewPanel.Event( this ) ) ;
	}

	//-------------------------------------------------------------------------
	// Public methods
	//

    /** Clears all the JTextField to "" */
    public void clearAll()
    {
        setLogicalName( "" );
        setFactoryClassName( "" );
        setServiceClassName( "" );
        setVendorName( "" );
        setVendorURL( "" );
        setJposVersion( "" );
        setProductName( "" );
        setProductDescription( "" );
        setProductURL( "" );
        setDeviceCategory( "" );

		refreshed = false;
    }

    /**
     * Enables/disables all UI widgets
     * @param b the boolean param
     */
    public void setEnabledAll( boolean b )
    {
        creationPropertyTable.setEnabled( b );
        javaPosPropertyTable.setEnabled( b );
        vendorPropertyTable.setEnabled( b );
        productPropertyTable.setEnabled( b );
    }

    /**
     * Makes all UI widgets editable or not
     * @param b the boolean param
     */
    public void setEditableAll( boolean b )
    {
        creationPropertyTable.setEditable( b );
        javaPosPropertyTable.setEditable( b );
        vendorPropertyTable.setEditable( b );
        productPropertyTable.setEditable( b );
    }

    /**
     * Replaces the JposEntry specified
     * @param entry the JposEntry to replace
     */
    public void setJposEntry( JposEntry entry ) 
    { 
        jposEntry = entry; 
		refreshed = false;
        refresh( jposEntry );
    }

    /** 
	 * Refreshes the JposEntry property values 
	 * @param entry the JposEntry to refresh
	 */
    public void refresh( JposEntry entry )
    {
		if( entry != null )
        {
            setLogicalName( (String)entry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME ) );
            setFactoryClassName( (String)entry.getPropertyValue( JposEntry.SI_FACTORY_CLASS_PROP_NAME ) );
            setServiceClassName( (String)entry.getPropertyValue( JposEntry.SERVICE_CLASS_PROP_NAME ) );
			setVendorName( (String)entry.getPropertyValue( JposEntry.VENDOR_NAME_PROP_NAME ) );
            setVendorURL( (String)entry.getPropertyValue( JposEntry.VENDOR_URL_PROP_NAME ) );
			setJposVersion( (String)entry.getPropertyValue( JposEntry.JPOS_VERSION_PROP_NAME ) );
			setProductName( (String)entry.getPropertyValue( JposEntry.PRODUCT_NAME_PROP_NAME ) );
			setProductDescription( (String)entry.getPropertyValue( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME ) );
			setProductURL( (String)entry.getPropertyValue( JposEntry.PRODUCT_URL_PROP_NAME ) );
            setDeviceCategory( (String)entry.getPropertyValue(JposEntry.DEVICE_CATEGORY_PROP_NAME) );

			refreshed = true;
        }
    }

    /** Called when the "OK" command button is clicked */
    public void cancelButtonClicked()
    {
		boolean somePropChanged = creationPropertyTable.isJposEntryDifferentFromView( getJposEntry() ) ||
                                  javaPosPropertyTable.isJposEntryDifferentFromView( getJposEntry() ) ||
                                  vendorPropertyTable.isJposEntryDifferentFromView( getJposEntry() ) ||
                                  productPropertyTable.isJposEntryDifferentFromView( getJposEntry() );

		if( refreshed && somePropChanged )
		{
			int userChoice = JOptionPane.
							 showConfirmDialog( this, SAVE_CHANGES_TO_JPOSENTRY_QUESTION_MSG,
							 					SELECT_AN_OPTION_MSG, JOptionPane.YES_NO_OPTION );

			if( userChoice == JOptionPane.YES_OPTION )
				updateJposEntryFromView( getJposEntry() );
			else
				refresh( jposEntry );
		}

        setEnabledAll( false );
		setEditableAll( false );
    }

    /** Called when the "Edit" command button is clicked */
    public void editButtonClicked()
    {
        setEnabledAll( true );
		setEditableAll( true );
    }

    /** Called when the "OK" command button is clicked */
    public void okButtonClicked()
    {
        setEnabledAll( false );
		setEditableAll( false );

		updateJposEntryFromView( getJposEntry() );
    }

    /** Called when this PropsViewPanel about to loose focus */
    public void aboutToLooseFocus() 
	{
		if( jposEntry != null )
			cancelButtonClicked(); 
	}

    //--------------------------------------------------------------------------
    // Intance variables
    //

	private boolean refreshed = false;

    private CreationPropertyTable creationPropertyTable = new CreationPropertyTable();
    private JavaPosPropertyTable javaPosPropertyTable = new JavaPosPropertyTable();
    private VendorPropertyTable vendorPropertyTable = new VendorPropertyTable();
    private ProductPropertyTable productPropertyTable = new ProductPropertyTable();

    //---------------------------------------------------------------------------
    // Class constants
    //

    public static final String LOGICALNAMETEXT_STRING = JposEntryEditorMsg.LOGICALNAMETEXT_STRING;
    public static final String SIFACTORYCLASSTEXT_STRING = JposEntryEditorMsg.SIFACTORYCLASSTEXT_STRING;
    public static final String CREATION_PROPS_STRING = JposEntryEditorMsg.CREATION_PROPS_STRING;
    public static final String JPOS_PROPS_STRING = JposEntryEditorMsg.JPOS_PROPS_STRING;
    public static final String VENDOR_PROPS_STRING = JposEntryEditorMsg.VENDOR_PROPS_STRING;
    public static final String PRODUCT_PROPS_STRING = JposEntryEditorMsg.PRODUCT_PROPS_STRING;
    public static final String PROPERTY_NAME = JposEntryEditorMsg.PROPERTY_NAME_STRING;
    public static final String PROPERTY_VALUE = JposEntryEditorMsg.PROPERTY_VALUE_STRING;

	//<i18n>
	public static final String SAVE_CHANGES_TO_JPOSENTRY_QUESTION_MSG = "Save changes to entry?";
	public static final String SELECT_AN_OPTION_MSG = "Select and option"; 
	//</i18n>
}
