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

import java.util.Locale;
import java.util.ResourceBundle;
//import java.util.ListResourceBundle;

/**
 * Interface for all DevEx application message constants Strings
 * that are dynamicaly intialized using I18N support classes
 * @author Kriselie D Rivera
 * @version 0.1.0 (JDK 1.x.x)
 * @since 1.3 (SF 2K meeting)
 */
public interface JposEntryEditorMsg
{                                                                                                
   //--------------------------------------------------------------------------
   // Ressource bundle constant for this interface
   // NOTE: this object is dynamically intitialized at class load time
   //
   //public static final ListResourceBundle lrb = (ListResourceBundle)ResourceBundle.
   //                                             getBundle( "jpos.config.simple.editor.i18n.JposEntryEditorRc", Locale.getDefault() );
   public static final ResourceBundle lrb = ResourceBundle.
												   getBundle( "jpos.config.simple.editor.i18n.JposEntryEditorRc", Locale.getDefault() );
   
   //--------------------------------------------------------------------------
   // Exerciser application I18N String messages - String message constants
   //
   
   //---------------------------------------------------------------------------
   //Messages UI
   //

   public static final String JPOSENTRYEDITOR_STRING = lrb.getString( "JPOSENTRYEDITOR_STRING_KEY" );
   public static final String FILE_STRING = lrb.getString( "FILE_STRING_KEY" );
   public static final String NEW_STRING = lrb.getString( "NEW_STRING_KEY");
   public static final String OPEN_STRING = lrb.getString( "OPEN_STRING_KEY" );
   public static final String SAVE_STRING = lrb.getString( "SAVE_STRING_KEY" );
   public static final String SAVEAS_STRING = lrb.getString( "SAVEAS_STRING_KEY" );
   public static final String LOADJPOSREGISTRY_STRING = lrb.getString( "LOADJPOSREGISTRY_STRING_KEY" );
   public static final String SAVEJPOSREGISTRY_STRING = lrb.getString( "SAVEJPOSREGISTRY_STRING_KEY" );
   public static final String CLOSEJPOSREGISTRY_STRING = lrb.getString( "CLOSEJPOSREGISTRY_STRING_KEY" );
   public static final String CLOSE_STRING = lrb.getString( "CLOSE_STRING_KEY" );
   public static final String EXIT_STRING = lrb.getString( "EXIT_STRING_KEY" );
   public static final String EDIT_STRING = lrb.getString( "EDIT_STRING_KEY" );
   public static final String ADD_STRING = lrb.getString( "ADD_STRING_KEY" );
   public static final String COPY_STRING = lrb.getString( "COPY_STRING_KEY" );
   public static final String DELETE_STRING = lrb.getString( "DELETE_STRING_KEY" );
   public static final String MODIFY_STRING = lrb.getString( "MODIFY_STRING_KEY" );
   public static final String PREFERENCES_STRING = lrb.getString( "PREFERENCES_STRING_KEY" );
   public static final String HELP_STRING = lrb.getString( "HELP_STRING_KEY" );
   public static final String ABOUT_STRING = lrb.getString( "ABOUT_STRING_KEY" );
   
   public static final String JPOSENTRIES_STRING = lrb.getString( "JPOSENTRIES_STRING_KEY" );
   public static final String PROPERTIES_STRING = lrb.getString( "PROPERTIES_STRING_KEY" );
   public static final String IMPORTANTPROPERTIES_STRING = lrb.getString( "IMPORTANTPROPERTIES_STRING_KEY" );
   public static final String CURRENTPROPERTY_STRING = lrb.getString( "CURRENTPROPERTY_STRING_KEY" );
   public static final String PROPERTYNAME_STRING = lrb.getString( "PROPERTYNAME_STRING_KEY" );
   public static final String PROPERTYVALUE_STRING = lrb.getString( "PROPERTYVALUE_STRING_KEY" );
   public static final String PROPERTYTYPE_STRING = lrb.getString( "PROPERTYTYPE_STRING_KEY" );
   public static final String OTHERPROPERTIES_STRING = lrb.getString( "OTHERPROPERTIES_STRING_KEY" );
   public static final String MESSAGES_STRING = lrb.getString( "MESSAGES_STRING_KEY" );
   public static final String TOPOFFILE_STRING = lrb.getString( "TOPOFFILE_STRING_KEY" );
     
   //---------------------------------------------------------------------------
   //Messages Main Frame
   //

   public static final String NEWFILEMENU_STRING = lrb.getString( "NEWFILEMENU_STRING_KEY" );
   public static final String NEWFILEBUTTON_STRING = lrb.getString( "NEWFILEBUTTON_STRING_KEY" );
   public static final String OPENFILEMENU_STRING = lrb.getString( "OPENFILEMENU_STRING_KEY" );
   public static final String OPENFILEBUTTON_STRING = lrb.getString( "OPENFILEBUTTON_STRING_KEY" );
   public static final String SAVEFILEMENU_STRING = lrb.getString( "SAVEFILEMENU_STRING_KEY" );
   public static final String SAVEFILEBUTTON_STRING = lrb.getString( "SAVEFILEBUTTON_STRING_KEY" );
   public static final String SAVEASFILEMENU_STRING = lrb.getString( "SAVEASFILEMENU_STRING_KEY" );
   public static final String SAVEASFILEBUTTON_STRING = lrb.getString( "SAVEASFILEBUTTON_STRING_KEY" );
   public static final String LOADJPOSREGISTRYMENU_STRING = lrb.getString( "LOADJPOSREGISTRYMENU_STRING_KEY" );
   public static final String SAVEJPOSREGISTRYMENU_STRING = lrb.getString( "SAVEJPOSREGISTRYMENU_STRING_KEY" );
   public static final String CLOSEJPOSREGISTRYMENU_STRING = lrb.getString( "CLOSEJPOSREGISTRYMENU_STRING_KEY" );
   public static final String CLOSEFILEMENU_STRING = lrb.getString( "CLOSEFILEMENU_STRING_KEY" );
   public static final String CLOSEFILEBUTTON_STRING = lrb.getString( "CLOSEFILEBUTTON_STRING_KEY" );
   public static final String EXITMENU_STRING = lrb.getString( "EXITMENU_STRING_KEY" );
   
   public static final String ADDMENU_STRING = lrb.getString( "ADDMENU_STRING_KEY" );
   public static final String COPYMENU_STRING = lrb.getString( "COPYMENU_STRING_KEY" );
   public static final String ADDBUTTON_STRING = lrb.getString( "ADDBUTTON_STRING_KEY" );
   public static final String DELETEMENU_STRING = lrb.getString( "DELETEMENU_STRING_KEY" );
   public static final String DELETEBUTTON_STRING = lrb.getString( "DELETEBUTTON_STRING_KEY" );
   public static final String MODIFYMENU_STRING = lrb.getString( "MODIFYMENU_STRING_KEY" );
   public static final String MODIFYBUTTON_STRING = lrb.getString( "MODIFYBUTTON_STRING_KEY" );

   public static final String ABOUTMENU_STRING = lrb.getString( "ABOUTMENU_STRING_KEY" );
   public static final String ABOUTBUTTON_STRING = lrb.getString( "ABOUTBUTTON_STRING_KEY" );
   public static final String CONFIGMENU_STRING = lrb.getString( "CONFIGMENU_STRING_KEY" );
   public static final String CONFIGBUTTON_STRING = lrb.getString( "CONFIGBUTTON_STRING_KEY" );

   //---------------------------------------------------------------------------
   //Delete Messages

   public static final String SELECTENTRYTODELETE_STRING = lrb.getString( "SELECTENTRYTODELETE_STRING_KEY" );
   public static final String DELETEENTRYWITHLOGNAM_STRING = lrb.getString( "DELETEENTRYWITHLOGNAM_STRING_KEY" );
   public static final String DELETESELECTEDENTRIES_STRING = lrb.getString( "DELETE_STRING_KEY" );

   //---------------------------------------------------------------------------
   //Modify Messages

   public static final String SELECTENTRYTOMODIFY_STRING = lrb.getString( "SELECTENTRYTOMODIFY_STRING_KEY" );
   
   //---------------------------------------------------------------------------
   //Copy Messages

   public static final String ENTERNEWLOGICALNAME_STRING = lrb.getString( "ENTERNEWLOGICALNAME_STRING_KEY" );

   //---------------------------------------------------------------------------
   //Save Messages
   
   public static final String SAVECURRENTCHANGES_STRING = lrb.getString( "SAVECURRENTCHANGES_STRING_KEY" );

   //---------------------------------------------------------------------------
   //Open Messages

   public static final String OPENFILE_STRING = lrb.getString( "OPENFILE_STRING_KEY" );
   public static final String NOENTRIES_STRING = lrb.getString( "NOENTRIES_STRING_KEY" );
   public static final String ERROR_STRING = lrb.getString( "ERROR_STRING_KEY" );
     
   //---------------------------------------------------------------------------
   //Close Messages

   public static final String CLOSECHANGES_STRING = lrb.getString( "CLOSECHANGES_STRING_KEY" );
   public static final String CLOSEDFILE_STRING = lrb.getString( "CLOSEDFILE_STRING_KEY" );
   public static final String CLOSEDFILEWITHCHANGES_STRING = lrb.getString( "CLOSEDFILEWITHCHANGES_STRING_KEY" );

   //---------------------------------------------------------------------------
   //Save Messages

   public static final String ERRORSAVING_STRING = lrb.getString( "ERRORSAVING_STRING_KEY" );
   public static final String SAVEDFILE_STRING = lrb.getString( "SAVEDFILE_STRING_KEY" );

   //---------------------------------------------------------------------------
   //Save As Messages

   public static final String SAVECHANGES_STRING = lrb.getString( "SAVECHANGES_STRING_KEY" );
   public static final String CHANGESLOST_STRING = lrb.getString( "CHANGESLOST_STRING_KEY" );

   //---------------------------------------------------------------------------
   //LoadJposRegistry Messages

   public static final String LOADENTRIESJPOSREGISTRY_STRING = lrb.getString( "LOADENTRIESJPOSREGISTRY_STRING_KEY" );
   public static final String NOENTRIESJPOSREGISTRY_STRING = lrb.getString( "NOENTRIESJPOSREGISTRY_STRING_KEY" );
      
   //---------------------------------------------------------------------------
   //SaveJposRegistry Messages
     
   public static final String ERRORSAVINGJPOSREGISTRY_STRING = lrb.getString( "ERRORSAVINGJPOSREGISTRY_STRING_KEY" );
   public static final String SAVEDCHANGESTOREGISTRY_STRING = lrb.getString( "SAVEDCHANGESTOREGISTRY_STRING_KEY" );

   //---------------------------------------------------------------------------
   //CloseJposRegistry Messages

   public static final String SAVECHANGESJPOSREGISTRY_STRING = lrb.getString( "SAVECHANGESJPOSREGISTRY_STRING_KEY" );
   public static final String CLOSEDJPOSREGISTRY_STRING = lrb.getString( "CLOSEDJPOSREGISTRY_STRING_KEY" );

   //---------------------------------------------------------------------------
   //isCurrentLnFMetal Message

   public static final String METAL_STRING = lrb.getString( "METAL_STRING_KEY" );

   //---------------------------------------------------------------------------
   //AboutDialog Class

   public static final String ABOUTTITLE_STRING = lrb.getString( "ABOUTTITLE_STRING_KEY" );
   public static final String ABOUTTITLELABEL_STRING = lrb.getString( "ABOUTTITLELABEL_STRING_KEY" );
   public static final String OKBUTTON_STRING = lrb.getString( "OKBUTTON_STRING_KEY" );
   public static final String JAVAVERSION_STRING = lrb.getString( "JAVAVERSION_STRING_KEY" );
   public static final String LABEL_STRING = lrb.getString( "LABEL_STRING_KEY" );
   public static final String DIALOG_STRING = lrb.getString( "DIALOG_STRING_KEY" );
   public static final String OS_STRING = lrb.getString( "OS_STRING_KEY" );
   public static final String ARCHITECTURE_STRING = lrb.getString( "ARCHITECTURE_STRING_KEY" );
   public static final String TOTALMEMORY_STRING = lrb.getString( "TOTALMEMORY_STRING_KEY" );
   public static final String BYTES_STRING = lrb.getString( "BYTES_STRING_KEY" );
   public static final String FREEMEMORY_STRING = lrb.getString( "FREEMEMORY_STRING_KEY" );
     
   //---------------------------------------------------------------------------
   //EditJposEntryDialog class

   public static final String EDITTITLE_STRING = lrb.getString( "EDITTITLE_STRING_KEY" );
   public static final String ADDPROP_STRING = lrb.getString( "ADDPROP_STRING_KEY" );
   public static final String MODIFYPROP_STRING = lrb.getString( "MODIFYPROP_STRING_KEY" );
   public static final String DELETEPROP_STRING = lrb.getString( "DELETEPROP_STRING_KEY" );
   public static final String DELETEJPOSENTRY_STRING = lrb.getString( "DELETEJPOSENTRY_STRING_KEY" );
   public static final String VALIDNAME_STRING = lrb.getString( "VALIDNAME_STRING_KEY" );
   public static final String VALIDCLASS_STRING = lrb.getString( "VALIDCLASS_STRING_KEY" );
   public static final String UNKNOWN_STRING = lrb.getString( "UNKNOWN_STRING_KEY" );
   public static final String USEDNAME_STRING = lrb.getString( "USEDNAME_STRING_KEY" );
   public static final String CANCELBUTTON_STRING = lrb.getString( "CANCELBUTTON_STRING_KEY" );

   //------------------------------------------------------------------------
   //EditJposEntryPropDialog class

   public static final String EDITPROPTITLE_STRING = lrb.getString( "EDITPROPTITLE_STRING_KEY" );
   public static final String EDITPROPERTYNAME_STRING = lrb.getString( "EDITPROPERTYNAME_STRING_KEY" );
   public static final String EDITPROPERTYVALUE_STRING = lrb.getString( "EDITPROPERTYVALUE_STRING_KEY" );
   public static final String EDITPROPERTYTYPE_STRING = lrb.getString( "EDITPROPERTYTYPE_STRING_KEY" );
   public static final String EDITINVALIDPROPERTY_STRING = lrb.getString( "EDITINVALIDPROPERTY_STRING_KEY" );
     
   public static final String PROPERTYDEFINED_STRING = lrb.getString( "PROPERTYDEFINED_STRING_KEY" );
   public static final String EDITVALIDNAME_STRING = lrb.getString( "EDITVALIDNAME_STRING_KEY" );
   public static final String NOTCOMPATIBLE_STRING = lrb.getString( "NOTCOMPATIBLE_STRING_KEY" );

   //------------------------------------------------------------------------
   //JposEntryEditorConfig class

   public static final String ERRORSAVINGFILE_STRING = lrb.getString( "ERRORSAVINGFILE_STRING_KEY" );
   
   //------------------------------------------------------------------------
   //JposEntryEditorConfigDialog class

   public static final String JPOSENTRYPREFERENCES_STRING = lrb.getString( "JPOSENTRYPREFERENCES_STRING_KEY" );
   public static final String TREEVIEW_STRING = lrb.getString( "TREEVIEW_STRING_KEY" );
   public static final String LOOKANDFEEL_STRING = lrb.getString( "LOOKANDFEEL_STRING_KEY" );
   public static final String AUTOEXPAND_STRING = lrb.getString( "AUTOEXPAND_STRING_KEY" );
   public static final String SORTED_STRING = lrb.getString( "SORTED_STRING_KEY" );
   public static final String CATEGORY_STRING = lrb.getString( "CATEGORY_STRING_KEY" );
   public static final String MANUFACTURER_STRING = lrb.getString( "MANUFACTURER_STRING_KEY" );
   public static final String JAVALOOKANDFEEL_STRING = lrb.getString( "JAVALOOKANDFEEL_STRING_KEY" );
   public static final String NATIVELOOKANDFEEL_STRING = lrb.getString( "NATIVELOOKANDFEEL_STRING_KEY" );
   public static final String VIEWOPTIONS_STRING = lrb.getString( "VIEWOPTIONS_STRING_KEY" );
   public static final String SHOWINTEGERSASHEX_STRING = lrb.getString( "SHOWINTEGERSASHEX_STRING_KEY" );
   public static final String AUTOLOADREGISTRY_STRING = lrb.getString( "AUTOLOADREGISTRY_STRING_KEY" );

   //------------------------------------------------------------------------
   //JposEntryTreePanel class

   public static final String ENTRIES_STRING = lrb.getString( "ENTRIES_STRING_KEY" );

   //------------------------------------------------------------------------
   //JposEntryViewPanel class

   public static final String LOGICALNAMETEXT_STRING = lrb.getString( "LOGICALNAMETEXT_STRING_KEY" );
   public static final String SIFACTORYCLASSTEXT_STRING = lrb.getString( "SIFACTORYCLASSTEXT_STRING_KEY" );
   public static final String CURRENTPROPNAMETEXT_STRING = lrb.getString( "CURRENTPROPNAMETEXT_STRING_KEY" );
   public static final String CURRENTPROPVALUETEXT_STRING = lrb.getString( "CURRENTPROPVALUETEXT_STRING_KEY" );

   //------------------------------------------------------------------------
   //JposEntryProp class

   public static final String SETNAMEEXCEPTION_STRING = lrb.getString( "SETNAMEEXCEPTION_STRING_KEY" );

    //-----------------------------------------------------------------------
    // StandardPropsViewPanel class

    public static final String CREATION_PROPS_STRING
        = lrb.getString( "CREATION_PROPS_STRING_KEY" );
    public static final String JPOS_PROPS_STRING
        = lrb.getString( "JPOS_PROPS_STRING_KEY" );
    public static final String VENDOR_PROPS_STRING
        = lrb.getString( "VENDOR_PROPS_STRING_KEY" );
    public static final String PRODUCT_PROPS_STRING
        = lrb.getString( "PRODUCT_PROPS_STRING_KEY" );
    public static final String PROPERTY_NAME_STRING
        = lrb.getString( "PROPERTY_NAME_STRING_KEY" );
    public static final String PROPERTY_VALUE_STRING
        = lrb.getString("PROPERTY_VALUE_STRING_KEY");
}
