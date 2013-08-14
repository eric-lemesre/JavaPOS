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

import jpos.config.*;

/**
 * Small editor dialog for a JposEntry property
 * @since 0.1 (Philly 99 meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @author Manuel M Monserat
 * @author Kriselie D Rivera
 */
class EditJposEntryDialog extends JDialog
{   
    /**
     * 1-arg ctor
     * @param parent the parent JFrame
     * @since 0.1 (Philly 99 meeting)
     */
    public EditJposEntryDialog( JFrame parent )
    {
        super( parent );

        setTitle( EDITTITLE_STRING );

        getContentPane().setLayout( new BorderLayout() );

        jposEntryViewPanel.setEnabledAll( true );
        jposEntryViewPanel.setEditableAll( true );

        getContentPane().add( jposEntryViewPanel, BorderLayout.CENTER );

        JPanel commandPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );

        commandPanel.add( addPropButton );
        commandPanel.add( modifyPropButton );
        commandPanel.add( deletePropButton );
        commandPanel.add( okButton );
        commandPanel.add( cancelButton );

        getContentPane().add( commandPanel, BorderLayout.SOUTH );

        addPropButton.setToolTipText( ADDPROP_STRING );
        modifyPropButton.setToolTipText( MODIFYPROP_STRING );
        deletePropButton.setToolTipText( DELETEPROP_STRING );

        addPropButton.addActionListener(    new ActionListener()
                                            {
                                                public void actionPerformed(ActionEvent e) 
                                                { addProp(); }
                                            }
                                       );

        modifyPropButton.addActionListener( new ActionListener()
                                            {                                            
                                                public void actionPerformed(ActionEvent e)
                                                  { modifyProp(); }                            
                                            }                                            
                                          );                                             
                                                                                       
        deletePropButton.addActionListener( new ActionListener()
                                            {                                            
                                                public void actionPerformed(ActionEvent e)
                                                { deleteProp(); }                            
                                            }                                            
                                          );                                             
                                                                                       
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

        jposEntryViewPanel.addPropertiesListSelectionListener(  new ListSelectionListener()
                                                                {
                                                                    public void valueChanged(ListSelectionEvent e) 
                                                                    { enableEditModifyButtons( true ); }
                                                                }
                                                             );
        initDialog();
        
        pack();
        centerFrame();
        
    }
                                                                                   
    //--------------------------------------------------------------------------
    // Private methods
    //

    /**
     * initializes the dialog for display
     *
     */
    public void initDialog()
    {
        jposEntryViewPanel.setFocusOnLogicalName();
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

    /**
     * Called when "Add" property is selected
     * @since 0.1 (Philly 99 meeting)
     */
    private void addProp()
    {
        editJposEntryPropDialog.setJposEntryProp( new JposEntryProp() );
        editJposEntryPropDialog.setJposEntry( jposEntry );
        editJposEntryPropDialog.setEditing( false );
        editJposEntryPropDialog.clearAll();
        editJposEntryPropDialog.setCanceled( false );
        editJposEntryPropDialog.setModal( true );
        editJposEntryPropDialog.setVisible( true );
        
        JposEntryProp jposEntryProp = ( editJposEntryPropDialog.isCanceled() ? null : editJposEntryPropDialog.getJposEntryProp() );

        if( jposEntryProp != null )
        {
            getJposEntry().addProperty( jposEntryProp.getName(), jposEntryProp.getValue() );
            jposEntryViewPanel.addProperty( jposEntryProp );
            jposEntryViewPanel.refresh();
        }

    }

    /**
     * Called when "Modify" property is selected
     * @since 0.1 (Philly 99 meeting)
     */
    private void modifyProp()
    {
        JposEntryProp jposEntryProp = jposEntryViewPanel.getSelectedJposEntryProp();

        String currentName = jposEntryProp.getName();

        editJposEntryPropDialog.setJposEntryProp( jposEntryProp );
        editJposEntryPropDialog.setJposEntry( jposEntry );
        editJposEntryPropDialog.setEditing( true );
        editJposEntryPropDialog.setCanceled( false );
        editJposEntryPropDialog.setModal( true );
        editJposEntryPropDialog.setVisible( true );

        if( !editJposEntryPropDialog.isCanceled() )
        {
            jposEntry.removeProperty( currentName );
            jposEntry.addProperty( jposEntryProp.getName(), jposEntryProp.getValue() );
            jposEntryViewPanel.refresh();
            jposEntryViewPanel.updateCurrentProp();
            enableEditModifyButtons( jposEntryViewPanel.isListEmpty() ? false : true );
        }

    }

    /**
     * Called when "Delete" propery is selected
     * @since 0.1 (Philly 99 meeting)
     */
    private void deleteProp()
    {
        JposEntryProp jposEntryProp = jposEntryViewPanel.getSelectedJposEntryProp();

        if( jposEntryProp != null  )
        {
            String logicalName = jposEntryProp.getName();

            int userAnswer = JOptionPane.showConfirmDialog( this, DELETEJPOSENTRY_STRING + logicalName, JPOSENTRYEDITOR_STRING, JOptionPane.YES_NO_OPTION ); 

            if( userAnswer == JOptionPane.YES_OPTION )
            {
                jposEntry.removeProperty( jposEntryProp.getName() );
                jposEntryViewPanel.removeProperty( jposEntryProp );
                jposEntryViewPanel.refresh();
                enableEditModifyButtons( false );
                jposEntryViewPanel.clearCurrentPropFields();
            }
        }
    }

    /**
     * Called when "OK" is clicked
     * @since 0.1 (Philly 99 meeting)
     */
    private void ok()
    {
        if( jposEntryViewPanel.getLogicalName().equals( "" ) )
        {
            JOptionPane.showMessageDialog( this, VALIDNAME_STRING, JPOSENTRYEDITOR_STRING, JOptionPane.ERROR_MESSAGE ); 
            jposEntryViewPanel.setFocusOnLogicalName();
            return;
        }
        else
        if( jposEntryViewPanel.getSIFactoryClassName().equals( "" ) )
        {
            JOptionPane.showMessageDialog( this, VALIDCLASS_STRING, JPOSENTRYEDITOR_STRING, JOptionPane.ERROR_MESSAGE ); 
            jposEntryViewPanel.setFocusOnSIFactoryClassName();
            return;
        }

        if ( isEditing() )
        {
            
            jposEntry.addProperty( JposEntry.LOGICAL_NAME_PROP_NAME, jposEntryViewPanel.getLogicalName() );
            jposEntry.addProperty( JposEntry.SI_FACTORY_CLASS_PROP_NAME, jposEntryViewPanel.getSIFactoryClassName() );

            Enumeration props = jposEntryViewPanel.getJposEntryProps();

            while( props.hasMoreElements() )
            {
                JposEntryProp prop = (JposEntryProp)props.nextElement();
                jposEntry.addProperty( prop.getName(), prop.getValue() );
            }

            if( !jposEntry.hasPropertyWithName( JposEntry.DEVICE_CATEGORY_PROP_NAME ) )
                jposEntry.addProperty( JposEntry.DEVICE_CATEGORY_PROP_NAME, UNKNOWN_STRING );
            if( !jposEntry.hasPropertyWithName( JposEntry.VENDOR_NAME_PROP_NAME ) )
                jposEntry.addProperty( JposEntry.VENDOR_NAME_PROP_NAME, UNKNOWN_STRING );


            setVisible( false );
            
        }
        else
        {
            if( !jposEntryList.hasJposEntry( jposEntryViewPanel.getLogicalName() ) )
            {
                jposEntry.addProperty( JposEntry.LOGICAL_NAME_PROP_NAME, jposEntryViewPanel.getLogicalName() );
                jposEntry.addProperty( JposEntry.SI_FACTORY_CLASS_PROP_NAME, jposEntryViewPanel.getSIFactoryClassName() );

                Enumeration props = jposEntryViewPanel.getJposEntryProps();

                while( props.hasMoreElements() )
                {
                    JposEntryProp prop = (JposEntryProp)props.nextElement();
                    jposEntry.addProperty( prop.getName(), prop.getValue() );
                }
      
                if( !jposEntry.hasPropertyWithName( JposEntry.DEVICE_CATEGORY_PROP_NAME ) )
                jposEntry.addProperty( JposEntry.DEVICE_CATEGORY_PROP_NAME,UNKNOWN_STRING );
                
                if( !jposEntry.hasPropertyWithName( JposEntry.VENDOR_NAME_PROP_NAME ) )
                jposEntry.addProperty( JposEntry.VENDOR_NAME_PROP_NAME, UNKNOWN_STRING );

                setVisible( false );
            }
            else
            {
                JOptionPane.showMessageDialog( this, USEDNAME_STRING, JPOSENTRYEDITOR_STRING,JOptionPane.ERROR_MESSAGE );
                jposEntryViewPanel.setFocusOnLogicalName();
            }
        }
    }

    /**
    * Called when "Cancel" is clicked
     * @since 0.1 (Philly 99 meeting)
     */
    private void cancel() 
    { 
        setCanceled( true );
        setVisible( false ); 
    }

    //--------------------------------------------------------------------------
    // Public methods
    //

    /**
     * @return the JposEntry that is being edited
     * @since 0.1 (Philly 99 meeting)
     */
    public JposEntry getJposEntry() { return jposEntry; }
   
    /**
     * Sets the JposEntry to edit
     * @param entry the JposEntry to edit
     * @since 0.1 (Philly 99 meeting)
     */
    public void setJposEntry( JposEntry entry ) 
    { 
        jposEntry = entry;
        jposEntryViewPanel.setJposEntry( jposEntry );
        
        enableEditModifyButtons( jposEntryViewPanel.isListEmpty() ? false : true );
            
    }

    /**
     * Clears all text fields
     * @since 0.1 (Philly 99 meeting)
     */
    public void clearAll() { jposEntryViewPanel.clearAll(); }

    /**
     * Enables/disables all text fields and UI widgets
     * @param b the boolean parameter
     * @since 0.1 (Philly 99 meeting)
     */
    public void setEnabledAll( boolean b ) { jposEntryViewPanel.setEnabled( b ); }

    /**
     * Makes all text fieldds editable or not
     * @param b the boolean parameter
     * @since 0.1 (Philly 99 meeting)
     */
    public void setEditableAll( boolean b ) { jposEntryViewPanel.setEditableAll( b ); }

    /**
     *  Enables/disables the modify/delete buttons
     * @param b the boolean parameter
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-5-2000)
     */
    public void enableEditModifyButtons( boolean b )
    {
        modifyPropButton.setEnabled( b );
        deletePropButton.setEnabled( b );
    }

    /**
     *  Sets the jposEntryList
     * @param list the JposEntryList
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-9-2000)
     */
    public void setJposEntryList( JposEntryList list )
    {
        jposEntryList = list;   
    }

    /**
     * sets the editing variable
     * @param b the boolean parameter to set the editing variable
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-11-2000)
     */
    public void setEditing( boolean b )
    {
        editing = b;
    }

    /**
     * @return true if editing is true, false otherwise
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-11-2000)
     */
    public boolean isEditing() { return editing; }

    /**
     * enables/disables the logicalNameTextField in the JposEntryViewPanel
     * @param b the boolean parameter
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-11-2000)
     */
    public void enableViewPanelLNTField( boolean b )
    {
           jposEntryViewPanel.enableLogicalNameTextField( b );
    }

    /**
     * sets the cancel variable
     * @param b the boolean parameter
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-11-2000)
     */
    public void setCanceled( boolean b )
    {
        canceled = b;
    }

    /**
     * @return canceled value 
     * @since 1.3 (SF 2K meeting)
     * @author MMM (05-02-2000)
     */
    public boolean isCanceled() { return canceled; }
    
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
        editJposEntryPropDialog.setShowNumbersAsHexFlag( b );
    }

    /**
     * clears currentProp fields in the jposEntryViewPanel
     * @author MMM (6-22-2000)
     * @since 1.3 (SF 2K meeting)
     */
    public void clearCurrentPropFields()
    {
        jposEntryViewPanel.clearCurrentPropFields();
    }

    //--------------------------------------------------------------------------
    // Instance variables
    //

    private JposEntry jposEntry = null;
    private JposEntryList jposEntryList = null;
    private boolean showNumbersAsHex = false;

    private JposEntryViewPanel jposEntryViewPanel = new JposEntryViewPanel();

    private EditJposEntryPropDialog editJposEntryPropDialog = new EditJposEntryPropDialog( (JFrame)getParent() );

    private JButton addPropButton = new JButton( ADD_STRING );
    private JButton modifyPropButton = new JButton( MODIFY_STRING );
    private JButton deletePropButton = new JButton( DELETE_STRING );
    private JButton okButton = new JButton( OKBUTTON_STRING );
    private JButton cancelButton = new JButton( CANCELBUTTON_STRING );

    private boolean editing = false;
    private boolean canceled = false;


    //---------------------------------------------------------------------------
    // I18N  class constants
    //
    
    public static final String JPOSENTRYEDITOR_STRING = JposEntryEditorMsg.JPOSENTRYEDITOR_STRING;
    public static final String EDITTITLE_STRING = JposEntryEditorMsg.EDITTITLE_STRING;
    public static final String ADDPROP_STRING = JposEntryEditorMsg.ADDPROP_STRING;
    public static final String MODIFYPROP_STRING = JposEntryEditorMsg.MODIFYPROP_STRING;
    public static final String DELETEPROP_STRING = JposEntryEditorMsg.DELETEPROP_STRING;
    public static final String DELETEJPOSENTRY_STRING = JposEntryEditorMsg.DELETEJPOSENTRY_STRING;
    public static final String VALIDNAME_STRING = JposEntryEditorMsg.VALIDNAME_STRING;
    public static final String VALIDCLASS_STRING = JposEntryEditorMsg.VALIDCLASS_STRING;
    public static final String UNKNOWN_STRING = JposEntryEditorMsg.UNKNOWN_STRING;
    public static final String USEDNAME_STRING = JposEntryEditorMsg.USEDNAME_STRING;
    public static final String CANCELBUTTON_STRING = JposEntryEditorMsg.CANCELBUTTON_STRING;
    public static final String OKBUTTON_STRING = JposEntryEditorMsg.OKBUTTON_STRING;
    public static final String ADD_STRING = JposEntryEditorMsg.ADD_STRING;
    public static final String DELETE_STRING = JposEntryEditorMsg.DELETE_STRING;
    public static final String MODIFY_STRING = JposEntryEditorMsg.MODIFY_STRING;


}
