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
import java.util.*;

import javax.swing.*;    
import javax.swing.event.*;
import javax.swing.border.*;

import jpos.config.*;

/**
 * This panel displays the contents of a JposEntry
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @author Manuel M Monserat	
 * @author Kriselie D Rivera
 * @since 0.1 (Philly 99 meeting)
 */
class JposEntryViewPanel extends AbstractPropsViewPanel
{
    /**
     * Default ctor
     * @since 0.1 (Philly 99 meeting)
     */
    public JposEntryViewPanel()
    {
        setLayout( new GridLayout( 3, 1 ) );

        JPanel importantPropPanel = new JPanel();
        importantPropPanel.setLayout( new BoxLayout( importantPropPanel, BoxLayout.Y_AXIS ) );
        importantPropPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.gray ), IMPORTANTPROPERTIES_STRING, TitledBorder.ABOVE_TOP, TitledBorder.CENTER ) );

        JPanel jPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        jPanel.add( new JLabel( JposEntry.LOGICAL_NAME_PROP_NAME + ":" ) );
        jPanel.add( logicalNameTextField );
        importantPropPanel.add( jPanel );

        jPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        jPanel.add( new JLabel( JposEntry.SI_FACTORY_CLASS_PROP_NAME + ":" ) );
        jPanel.add( siFactoryClassNameTextField );
        importantPropPanel.add( jPanel );

        add( importantPropPanel );

        JPanel currentPropPanel = new JPanel();
        currentPropPanel.setLayout( new BoxLayout( currentPropPanel, BoxLayout.Y_AXIS ) );
        currentPropPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.gray ), CURRENTPROPERTY_STRING, TitledBorder.ABOVE_TOP, TitledBorder.CENTER ) );

        jPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        jPanel.add( new JLabel( EDITPROPERTYNAME_STRING ) );
        jPanel.add( currentPropNameTextField );
        currentPropPanel.add( jPanel );

        jPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        jPanel.add( new JLabel( EDITPROPERTYVALUE_STRING ) );
        jPanel.add( currentPropValueTextField );
        currentPropPanel.add( jPanel );

        jPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        jPanel.add( new JLabel( EDITPROPERTYTYPE_STRING ) );
        jPanel.add( currentPropValueTypeComboBox );
        currentPropPanel.add( jPanel );

        add( currentPropPanel );

        JPanel propListPanel = new JPanel( new BorderLayout() );
        propListPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.gray ), OTHERPROPERTIES_STRING, TitledBorder.ABOVE_TOP, TitledBorder.CENTER ) );
      
        JScrollPane jScrollPane = new JScrollPane( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ); 
        jScrollPane.setViewportView( propertiesList );

        propListPanel.add( jScrollPane, BorderLayout.CENTER );

        add( propListPanel );

        init();

        propertiesList.addListSelectionListener(    new ListSelectionListener()
                                                    {
                                                        public void valueChanged(ListSelectionEvent e) 
                                                        { listSelectionValueChanged( e ); }
                                                    }
                                               );
    }

    //--------------------------------------------------------------------------
    // Private intance methods
    //

    /**
     * Initializes the UI components intial state
     * @since 0.1 (Philly 99 meeting)
     */
    private void init()
    {
        logicalNameTextField.setEditable( false ); 
        siFactoryClassNameTextField.setEditable( false );
                                 
        currentPropNameTextField.setEditable( false ); 
        currentPropValueTextField.setEditable( false );
        currentPropValueTypeComboBox.setEditable( false );
        currentPropValueTypeComboBox.setEnabled( false );

        defaultJTextFieldTTText();
    }

    /**
     * Called when a new list value is selected
     * @param event the ListSelection event
     * @since 0.1 (Philly 99 meeting)
     */
    private void listSelectionValueChanged( ListSelectionEvent event )
    {
        currentProp = (JposEntryProp)propertiesList.getSelectedValue();

        if( currentProp == null ) return;

        updateCurrentProp();
    }

    /**
     * Adds this new property to the list of properties
     * @param name the property name
     * @param value the property value
     * @since 0.1 (Philly 99 meeting)
     */
    private void addPropToList( String name, Object value )
    { propListModel.addElement( new JposEntryProp( name, value ) ); }

    /**
     * Set's the JTextField text property and if text is longer then current
     * JTextField number of columns then set this text to the toolTipText
     * @param jTF the JTextField instance
     * @param text the String text value
     * @since 1.2 (NY 2K meeting)
     */
    private void setJTextFieldText( JTextField jTF, String text )
    {
        jTF.setText( text );
        if( text.length() > jTF.getColumns() )
            jTF.setToolTipText( text );
    }

    /**
     * Initialize all JTextField with default tool-tip text values
     * @since 1.2 (NY 2K meeting)
     */
    private void defaultJTextFieldTTText()
    {
        logicalNameTextField.setToolTipText( LOGICALNAMETEXT_STRING );
        siFactoryClassNameTextField.setToolTipText( SIFACTORYCLASSTEXT_STRING );
        currentPropNameTextField.setToolTipText( CURRENTPROPNAMETEXT_STRING );
        currentPropValueTextField.setToolTipText( CURRENTPROPVALUETEXT_STRING );
    }

    //--------------------------------------------------------------------------
    // Public intance methods
    //

    /**
     * Clears all the JTextField to ""
     * @since 0.1 (Philly 99 meeting)
     */
    public void clearAll()
    {
        logicalNameTextField.setText( "" );
        siFactoryClassNameTextField.setText( "" );
                                 
        currentPropNameTextField.setText( "" );
        currentPropValueTextField.setText( "" );

        defaultJTextFieldTTText();

        propListModel.removeAllElements();
    }

    /**
     * Called to update the current selected property
     * @param currentProp the current property
     * @since 0.1 (Philly 99 meeting)
     */
    public void updateCurrentProp()
    {
        if( currentProp == null ) return;

        setJTextFieldText( currentPropNameTextField, currentProp.getName() );

        if( currentProp.getValueType().equalsIgnoreCase( "integer" ) )
            setJTextFieldText( currentPropValueTextField, JposEntryEditorUtility.formatText( currentProp.getValue().toString(), showNumbersAsHex ) );            
        else
            setJTextFieldText( currentPropValueTextField, currentProp.getValue().toString() );

        currentPropValueTypeComboBox.setSelectedItem( currentProp.getValueType() ); 
    }

    /**
     * Makes all JTextField enabled
     * @param b the boolean param
     * @since 0.1 (Philly 99 meeting)
     */
    public void setEnabledAll( boolean b )
    {
        logicalNameTextField.setEnabled( b );
        siFactoryClassNameTextField.setEnabled( b );
    }

    /**
     * Makes all JTextField editable
     * @param b the boolean param
     * @since 0.1 (Philly 99 meeting)
     */
    public void setEditableAll( boolean b )
    {
        logicalNameTextField.setEditable( b );
        siFactoryClassNameTextField.setEditable( b );
    }

    /**
     * Add the property to the list
     * @param jposEntryProp the JposEntry property
     * @since 0.1 (Philly 99 meeting)
     */
    public void addProperty( JposEntryProp jposEntryProp )
    { propListModel.addElement( jposEntryProp ); }

    /**
     * Removes the specified property in the list
     * @param jposEntryProp the JposEntry property
     * @since 0.1 (Philly 99 meeting)
     */
    public void removeProperty( JposEntryProp jposEntryProp )
    { propListModel.removeElement( jposEntryProp ); }

    /**
     * Refreshes the JposEntry property values
     * @since 0.1 (Philly 99 meeting)
     */
    public void refresh()
    {
        JposEntry entry = getJposEntry();

        String propName = (String)entry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME );
        String siFactoryClass = (String)entry.getPropertyValue( JposEntry.SI_FACTORY_CLASS_PROP_NAME );

        if( propName != null && propName.equals( JposEntry.LOGICAL_NAME_PROP_NAME ) )
            setJTextFieldText( logicalNameTextField, propName );
        else
        if( propName != null && propName.equals( JposEntry.SI_FACTORY_CLASS_PROP_NAME ) )
            setJTextFieldText( siFactoryClassNameTextField, siFactoryClass );

        propertiesList.repaint();

    }

    /**
     * @return the JposEntry that is beeing viewed
     * @since 0.1 (Philly 99 meeting)
     */
    public JposEntry getJposEntry() { return jposEntry; }

    /**
     * @return the JposEntry logicalName property value
     * @since 0.1 (Philly 99 meeting)
     */
    public String getLogicalName() { return logicalNameTextField.getText(); }

    /**
     * @return the serviceInstanceFactoryClass property value
     * @since 0.1 (Philly 99 meeting)
     */
    public String getSIFactoryClassName() { return siFactoryClassNameTextField.getText(); }

    /**
     * @return the selected property
     * @since 0.1 (Philly 99 meeting)
     */
    public JposEntryProp getSelectedJposEntryProp() { return currentProp; }

    /**
     * @return an enumeration of the properties
     * @since 0.1 (Philly 99 meeting)
     */
    public Enumeration getJposEntryProps() { return ( (DefaultListModel)propertiesList.getModel() ).elements(); }

    /**
     * Replaces the JposEntry specified
     * @param entry the JposEntry to replace
     * @since 0.1 (Philly 99 meeting)
     */
    public void setJposEntry( JposEntry entry ) 
    { 
        jposEntry = entry; 

        clearAll();

        if( jposEntry == null ) return;

        Enumeration propNames = jposEntry.getPropertyNames();

        while( propNames.hasMoreElements() )
        {
            String propName = (String)propNames.nextElement();

            if( propName.equals( JposEntry.LOGICAL_NAME_PROP_NAME ) )
                setJTextFieldText( logicalNameTextField, (String)entry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME ) );
            else
            if( propName.equals( JposEntry.SI_FACTORY_CLASS_PROP_NAME ) )
                setJTextFieldText( siFactoryClassNameTextField, (String)entry.getPropertyValue( JposEntry.SI_FACTORY_CLASS_PROP_NAME ) );
            else
                addPropToList( propName, entry.getPropertyValue( propName ) );
        }

        propertiesList.setModel( propListModel );
    }

    /**
     * @return true if properties list is empty, false otherwise
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-5-2000)
     */
    public boolean isListEmpty() 
    { 
        if( propListModel.isEmpty() )
        {
            clearCurrentPropFields();
            return true;
        }
        else return false;
    }
            

    /**
     * Clears all the information on the middles panel when a property is deleted
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-5-2000)
     */
    public void clearCurrentPropFields() 
    {
        currentPropNameTextField.setText("");
        currentPropValueTextField.setText("");
        currentPropValueTypeComboBox.setSelectedIndex(0);
    }

    /**
     * adds a listener to the properties list
     * @param listener the listener to be added
     * @since 1.3 (SF 2K meeting)
     * @authoer MMM (5-5-2000)
     */
    public void addPropertiesListSelectionListener( ListSelectionListener listener )
    {   
        propertiesList.addListSelectionListener( listener );
    }

    /**
     * removes a listener to the properties list
     * @param listener the listener to be removed
     * @since 1.3 (SF 2K meeting)
     * @authoer MMM (5-5-2000)
     */
    public void removePropertiesListSelectionListener( ListSelectionListener listener )
    {
        propertiesList.removeListSelectionListener( listener );
    }

    /**
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-9-2000)
     */
    public void setFocusOnLogicalName()
    {   
        logicalNameTextField.requestFocus();
        if( logicalNameTextField.getText() != null && !logicalNameTextField.getText().equals("") )
            logicalNameTextField.selectAll();
    }

    /**
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-9-2000)
     */
    public void setFocusOnSIFactoryClassName()
    {   
        siFactoryClassNameTextField.requestFocus();
        if( siFactoryClassNameTextField.getText() != null && !siFactoryClassNameTextField.getText().equals("") )
            siFactoryClassNameTextField.selectAll();
    }

    /**
     * enables/disables the logicalNameTextField
     * @param b the boolean parameter
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-11-2000)
     */
    public void enableLogicalNameTextField( boolean b )
    {
           logicalNameTextField.setEditable( b );
    }

    /**
     * getter method for the showNumbersAsHex variable
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-20-2000)
     */
    public boolean getShowNumbersAsHexFlag() { return showNumbersAsHex; }

    /**
     * sets the showAsHexFlag variable to display integers
     * @param b the boolean param 
     * @since 1.3 (SF 2K meeting)
     */
    public void setShowNumbersAsHexFlag( boolean b)
    {
        showNumbersAsHex = b;
    }

    /** Called when loosing focus */
    public void aboutToLooseFocus()
    {
        //<temp>
        System.out.println( "aboutToLooseFocus()" );
        //</temp>
    }

    /** Called when the "Edit" command button is clicked */
    public void editButtonClicked()
    {
        //<temp>
        System.out.println( "editButtonClicked" );
        //</temp>
    }

    /** Called when the "OK" command button is clicked */
    public void okButtonClicked()
    {
        //<temp>
        System.out.println( "okButtonClicked" );
        //</temp>
    }

    /** Called when the "OK" command button is clicked */
    public void cancelButtonClicked()
    {
        //<temp>
        System.out.println( "cancelButtonClicked" );
        //</temp>
    }

    //--------------------------------------------------------------------------
    // Intance variables
    //

    private boolean showNumbersAsHex = false;

    private JposEntry jposEntry = null;

    private JposEntryProp currentProp = null;

    private JTextField logicalNameTextField = new JTextField( 20 );
    private JTextField siFactoryClassNameTextField = new JTextField( 25 );

    private JTextField currentPropNameTextField = new JTextField( 25 );
    private JTextField currentPropValueTextField = new JTextField( 25 );
    private JComboBox currentPropValueTypeComboBox = new JComboBox( PROPERTY_VALUE_TYPES );

    private DefaultListModel propListModel = new DefaultListModel();
    private Vector propertiesVector = new Vector();
    private JList propertiesList = new JList();

    //--------------------------------------------------------------------------
    // Class constants
    //

    private static final String[] PROPERTY_VALUE_TYPES = { "String", "Integer", "Boolean", "Long", "Float", "Byte", "Character" };


    //---------------------------------------------------------------------------
    // I18N  class constants
    //

    public static final String EDITPROPERTYNAME_STRING = JposEntryEditorMsg.EDITPROPERTYNAME_STRING;
    public static final String EDITPROPERTYVALUE_STRING = JposEntryEditorMsg.EDITPROPERTYVALUE_STRING;
    public static final String EDITPROPERTYTYPE_STRING = JposEntryEditorMsg.EDITPROPERTYTYPE_STRING;
    public static final String LOGICALNAMETEXT_STRING = JposEntryEditorMsg.LOGICALNAMETEXT_STRING;
    public static final String SIFACTORYCLASSTEXT_STRING = JposEntryEditorMsg.SIFACTORYCLASSTEXT_STRING;
    public static final String CURRENTPROPNAMETEXT_STRING = JposEntryEditorMsg.CURRENTPROPNAMETEXT_STRING;
    public static final String CURRENTPROPVALUETEXT_STRING = JposEntryEditorMsg.CURRENTPROPVALUETEXT_STRING;
    public static final String CURRENTPROPERTY_STRING = JposEntryEditorMsg.CURRENTPROPERTY_STRING;
    public static final String OTHERPROPERTIES_STRING = JposEntryEditorMsg.OTHERPROPERTIES_STRING;
    public static final String IMPORTANTPROPERTIES_STRING = JposEntryEditorMsg.IMPORTANTPROPERTIES_STRING;

}
