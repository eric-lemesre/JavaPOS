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
import javax.swing.border.*;

import jpos.config.*;

/**
 * Dialog to allow editing of a JposEntry property
 * @since 0.1 (Philly 99 meeting)
 * @author E. Michael Maximilien
 * @author Manuel M Monserat
 * @author Kriselie D Rivera
 */
class EditJposEntryPropDialog extends JDialog
{
    /**
     * 1-arg ctor
     * @param parent the JFrame parent for this dialog   
     * @since 0.1 (Philly 99 meeting)
     */
    public EditJposEntryPropDialog( JFrame parent )
    {
        super( parent );
        setTitle( EDITPROPTITLE_STRING );

        getContentPane().setLayout( new BorderLayout() );

        JPanel currentPropPanel = new JPanel();
        currentPropPanel.setLayout( new BoxLayout( currentPropPanel, BoxLayout.Y_AXIS ) );
        currentPropPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.gray ), CURRENTPROPNAMETEXT_STRING, TitledBorder.ABOVE_TOP, TitledBorder.CENTER ) );

        JPanel jPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        jPanel.add( new JLabel(EDITPROPERTYNAME_STRING ) );
        jPanel.add( currentPropNameTextField );
        currentPropPanel.add( jPanel );

        jPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        jPanel.add( new JLabel( EDITPROPERTYVALUE_STRING ) );
        jPanel.add( currentPropValueTextField );
        currentPropPanel.add( jPanel );

        jPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        jPanel.add( new JLabel( EDITPROPERTYVALUE_STRING ) );
        jPanel.add( currentPropValueTypeComboBox );
        currentPropPanel.add( jPanel );

        getContentPane().add( currentPropPanel, BorderLayout.CENTER );

        JPanel commandPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        commandPanel.add( okButton );
        commandPanel.add( cancelButton );
        getContentPane().add( commandPanel, BorderLayout.SOUTH );

        okButton.addActionListener( new ActionListener()
                                    {                                            
                                        public void actionPerformed(ActionEvent e)
                                        { ok(); }                            
                                    }                                            
                                  );                                             
                                                                               
        cancelButton.addActionListener( new ActionListener()
                                        {                                            
                                            public void actionPerformed(ActionEvent e)
                                            { cancel(); }                            
                                        }                                            
                                      );

        pack();
        centerDialog();
    }

    //--------------------------------------------------------------------------
    // Private methods
    //

    /**
     * Center's the Frame in the middle of the screen
     * <b>NOTE:</b>this method should propabley be moved to a utility class
     * @since 0.1 (Philly 99 meeting)
     */
    private void centerDialog()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setLocation( ( screenSize.width / 2 ) - ( getSize().width / 2 ),
                     ( screenSize.height / 2 ) - ( getSize().height / 2 ) );
    }

    /**
     * Called when the "OK" method is called
     * @since 0.1 (Philly 99 meeting)
     */
    private void ok()
    {
        if( currentName.equals("") )
            currentName = jposEntryProp.getName();
        
        String name = currentPropNameTextField.getText().trim();
        
        String valueString = currentPropValueTextField.getText().trim();
        String valueType = (String)currentPropValueTypeComboBox.getSelectedItem();

        if( valueType.equalsIgnoreCase( "integer" ) )
            valueString = JposEntryEditorUtility.readHex( valueString, showNumbersAsHex );

        boolean usedProperty = false;
        
        try
        {
            Object value = determineJposEntryPropValue( valueString, valueType );

            jposEntryProp.setName( name );
            jposEntryProp.setValue( value );

            if( isEditing() ) 
            {
                if( !currentName.equals( name ) && getJposEntry().hasPropertyWithName( jposEntryProp.getName() ) ) 
                {
                    usedProperty = true;
                    throw new Exception(EDITINVALIDPROPERTY_STRING);
                }
            }
            else
            if( getJposEntry().hasPropertyWithName( jposEntryProp.getName() ) ) 
            {
                usedProperty = true;
                throw new Exception(EDITINVALIDPROPERTY_STRING);
            }
                    
            currentName = "";
            setCanceled( false );                
            setVisible( false );
        }
        catch( Exception e ) 
        { 
            if( usedProperty ) 
            {
                JOptionPane.showMessageDialog( this, PROPERTYDEFINED_STRING, JPOSENTRYEDITOR_STRING, JOptionPane.ERROR_MESSAGE ); 
                currentPropNameTextField.requestFocus();
                currentPropNameTextField.selectAll();
            }
            else
            if( !invalidProperty ) 
            {
                JOptionPane.showMessageDialog( this, EDITVALIDNAME_STRING, JPOSENTRYEDITOR_STRING, JOptionPane.ERROR_MESSAGE ); 
                currentPropNameTextField.requestFocus();
            }
        }
    }

    /**
     * Called when the "Cancel" method is called
     * @since 0.1 (Philly 99 meeting)
     */
    private void cancel()
    {
        jposEntryProp = null;
        setCanceled( true );        
        setVisible( false );
    }

    /**
     * @return the value of a JposEntry property from its String value and type
     * @param valueString the String value of the property
     * @param valueType the property value's type
     * @since 0.1 (Philly 99 meeting)
     */
    private Object determineJposEntryPropValue( String valueString, String valueType ) throws Exception
    {
        Object value = null;
        invalidProperty = false;

        try
        {
            if( valueType.equals( "String" ) )
                value = valueString;
            else
            if( valueType.equals( "Integer" ) )
                value = new Integer( valueString );
            else
            if( valueType.equals( "Long" ) )
                value = new Long( valueString );
            else
            if( valueType.equals( "Character" ) )
                value = new Character( valueString.charAt( 0 ) );
            else
            if( valueType.equals( "Byte" ) )
                value = new java.lang.Byte( valueString );
            else
            if( valueType.equals( "Boolean" ) )
                value = new Boolean( valueString );
            else
            if( valueType.equals( "Float" ) )
                value = new java.lang.Float( valueString );

        }
        catch( Exception e )
        {
            JOptionPane.showMessageDialog( this, NOTCOMPATIBLE_STRING, JPOSENTRYEDITOR_STRING, JOptionPane.ERROR_MESSAGE );
            invalidProperty = true;
            throw e;
        }

        return value;
    }

    //--------------------------------------------------------------------------
    // Public methods
    //

    /**
     * Called to clear all the UI input objects
     * @since 0.1 (Philly 99 meeting)
     */
    public void clearAll()
    {
        currentPropNameTextField.setText( "" );
        currentPropValueTextField.setText( "" );
        currentPropValueTypeComboBox.setSelectedItem( "" );
        currentPropNameTextField.requestFocus();
    }

    /**
     * @return the JposEntryProp that is being edited
     * @since 0.1 (Philly 99 meeting)
     */
    public JposEntryProp getJposEntryProp() { return jposEntryProp; }

    /**
     * Sets the JposEntryProp to edit
     * @param entry the JposEntryProp object
     * @since 0.1 (Philly 99 meeting)
     */
    public void setJposEntryProp( JposEntryProp entry ) 
    { 
        if( entry == null ) return;

        jposEntryProp = entry; 

        currentPropNameTextField.setText( jposEntryProp.getName() );

        if( jposEntryProp.getValueType().equalsIgnoreCase( "integer" ) )
            currentPropValueTextField.setText( JposEntryEditorUtility.formatText( jposEntryProp.getValue().toString(), showNumbersAsHex ) );    
        else
            currentPropValueTextField.setText( jposEntryProp.getValue().toString() );    
        
        currentPropValueTypeComboBox.setSelectedItem( jposEntryProp.getValueType() );
    }

    /**
     * Sets the canceled property 
     * @param b the boolean parameter 
     * @since 1.3 (SF 2K meeting)
     * @author MMM (05-02-2000)
     */
    public void setCanceled( boolean b ) { canceled = b; }

    /**
     * @return canceled value 
     * @since 1.3 (SF 2K meeting)
     * @author MMM (05-02-2000)
     */
    public boolean isCanceled() { return canceled; }

    /**
     * @return the JposEntry that is being edited
     * @since 1.3 (SF 2K meeting)
     * @author MMM (05-03-2000)
     */
    public JposEntry getJposEntry() { return jposEntry; }
   
    /**
     * Sets the JposEntry to edit
     * @param entry the JposEntry to edit
     * @since 1.3 (SF 2K meeting)
     * @author MMM (05-03-2000)
     */
    public void setJposEntry( JposEntry entry ) { jposEntry = entry; }

    /**
     * Sets the editing property 
     * @param b the boolean parameter 
     * @since 1.3 (SF 2K meeting)
     * @author MMM (05-02-2000)
     */
    public void setEditing( boolean b ) { editing = b; }

    /**
     * @return editing value 
     * @since 1.3 (SF 2K meeting)
     * @author MMM (05-02-2000)
     */
    public boolean isEditing() { return editing; }

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

    //--------------------------------------------------------------------------
    // Instance variables
    //
        
    private boolean showNumbersAsHex = false;

    private JposEntry jposEntry = null;
    
    private JposEntryProp jposEntryProp = null;

    private JTextField currentPropNameTextField = new JTextField( 15 );
    private JTextField currentPropValueTextField = new JTextField( 15 );
    private JComboBox currentPropValueTypeComboBox = new JComboBox( PROPERTY_VALUE_TYPES );

    private JButton okButton = new JButton( OKBUTTON_STRING );
    private JButton cancelButton = new JButton( CANCELBUTTON_STRING );

    private boolean canceled = false;
    private boolean invalidProperty = false;
    private boolean editing = false;
    private String currentName = "";

    //--------------------------------------------------------------------------
    // Class constants
    //

    private static String[] PROPERTY_VALUE_TYPES = { "String", "Integer", "Boolean", "Long", "Float", "Byte", "Character" };
    //---------------------------------------------------------------------------
    // I18N  class constants
    //
     
    public static final String JPOSENTRYEDITOR_STRING = JposEntryEditorMsg.JPOSENTRYEDITOR_STRING;
    public static final String EDITPROPTITLE_STRING = JposEntryEditorMsg.EDITPROPTITLE_STRING;
    public static final String EDITPROPERTYNAME_STRING = JposEntryEditorMsg.EDITPROPERTYNAME_STRING;
    public static final String EDITPROPERTYVALUE_STRING = JposEntryEditorMsg.EDITPROPERTYVALUE_STRING;
    public static final String EDITPROPERTYTYPE_STRING = JposEntryEditorMsg.EDITPROPERTYTYPE_STRING;
    public static final String EDITINVALIDPROPERTY_STRING = JposEntryEditorMsg.EDITINVALIDPROPERTY_STRING;
    
    public static final String PROPERTYDEFINED_STRING = JposEntryEditorMsg.PROPERTYDEFINED_STRING;
    public static final String EDITVALIDNAME_STRING = JposEntryEditorMsg.EDITVALIDNAME_STRING;
    public static final String NOTCOMPATIBLE_STRING = JposEntryEditorMsg.NOTCOMPATIBLE_STRING;
    public static final String CANCELBUTTON_STRING = JposEntryEditorMsg.CANCELBUTTON_STRING;
    public static final String OKBUTTON_STRING = JposEntryEditorMsg.OKBUTTON_STRING;
    public static final String CURRENTPROPNAMETEXT_STRING = JposEntryEditorMsg.CURRENTPROPNAMETEXT_STRING;
    
    
}
