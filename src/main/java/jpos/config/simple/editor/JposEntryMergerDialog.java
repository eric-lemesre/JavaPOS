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
import java.io.*;

import javax.swing.*;    
import javax.swing.filechooser.FileFilter;

import jpos.config.*;
import jpos.config.simple.*;
import jpos.config.simple.xml.*;

/**
 * Dialog ui for the JposEntryMerger facility.  Presents user option of merging
 * serialized entries and XML entries and save them as serialized and/or XML.
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (SF-2K meeting)
 */
public class JposEntryMergerDialog extends JDialog
{   
    //--------------------------------------------------------------------------
    // Ctor(s)
    //

	/**
	 * 1-arg ctor.  Creates a modal dialog with parentFrame as parent 
	 * @param parentFrame the MainFrame parent of this dialog
	 */
	public JposEntryMergerDialog( MainFrame parentFrame )
	{ this( parentFrame, true ); }

	/**
	 * 2-arg ctor 
	 * @param parentFrame the JFrame parent of this dialog
	 * @param modalFlag whether this dialog is modal or not
	 */
	public JposEntryMergerDialog( JFrame parentFrame, boolean modalFlag )
	{
		super( parentFrame, modalFlag );

		setTitle( JPOS_ENTRY_MERGER_DIALOG_TITLE_STRING );

		//Setup UI components
		getContentPane().setLayout( new BoxLayout( getContentPane(), 
												   BoxLayout.Y_AXIS ) );

		JPanel file1Panel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add( serFile1RadioButton );
		buttonGroup1.add( xmlFile1RadioButton );
		file1Panel.add( serFile1RadioButton );
		file1Panel.add( xmlFile1RadioButton );
		file1Panel.add( file1TextField );
		file1Panel.add( openFile1Button );
		getContentPane().add( file1Panel );

		JPanel file2Panel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
		ButtonGroup buttonGroup2 = new ButtonGroup();
		buttonGroup2.add( serFile2RadioButton );
		buttonGroup2.add( xmlFile2RadioButton );
		file2Panel.add( serFile2RadioButton );
		file2Panel.add( xmlFile2RadioButton );
		file2Panel.add( file2TextField );
		file2Panel.add( openFile2Button );
		getContentPane().add( file2Panel );

		JPanel jPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
		jPanel.add( mergeSaveXMLButton );
		jPanel.add( mergeSaveSerializedButton );
		getContentPane().add( jPanel );

		//Setup event registration
		openFile1Button.
		addActionListener(	new ActionListener()
							{
								public void actionPerformed( ActionEvent e ) 
								{ openFile1ButtonClicked(); }
							}
					     );

		openFile2Button.
		addActionListener( 	new ActionListener()
						   	{
								public void actionPerformed( ActionEvent e ) 
								{ openFile2ButtonClicked(); }
						    }
						 );

		mergeSaveXMLButton.
		addActionListener(	new ActionListener()
							{
								public void actionPerformed( ActionEvent e ) 
								{ mergeAndSaveXMLButtonClicked(); }
							}
						 );

		mergeSaveSerializedButton.
		addActionListener(	new ActionListener()
							{
								public void actionPerformed( ActionEvent e ) 
								{ mergeAndSaveSerializedButtonClicked(); }
							}
						 );

		addWindowListener( 	new WindowAdapter()
							{
								public void windowClosing( WindowEvent e )
								{ close(); }
							}
						 );

		//Other setup
		pack();
		init();
	}
                                                                                   
    //--------------------------------------------------------------------------
    // Private methods
    //

	/**
	 * Intialize the location/size of this dialog
	 * @since 1.3 (SF 2K meeting )
	 */
	private void init()
	{
		JposEntryEditorConfig config = JposEntryEditorConfig.getInstance();

		setLocation( config.getJposEntryMergerDialogLocation() );
		setSize( config.getJposEntryMergerDialogSize() );
	}

	/**
	 * Save the location and size of the dialog
	 * @since 1.3 (SF 2K meeting )
	 */
	private void close()
	{
		JposEntryEditorConfig config = JposEntryEditorConfig.getInstance();

		config.setJposEntryMergerDialogLocation( getLocation() );
		config.setJposEntryMergerDialogSize( getSize() );
	}

	/** Called when the openFile1Button is clicked */
	private void openFile1ButtonClicked()
	{
		Cursor oldCursor = getCursor();
		setCursor( new Cursor( Cursor.WAIT_CURSOR ) );

		JFileChooser fileChooser = getOpenFileChooser();

		FileFilter fileFilter = ( serFile1RadioButton.isSelected() ? 
								  serFileFilter : xmlFileFilter );

		getOpenFileChooser().resetChoosableFileFilters();
		getOpenFileChooser().setAcceptAllFileFilterUsed( false );
		getOpenFileChooser().addChoosableFileFilter( fileFilter );

		int rc = fileChooser.showDialog( this, SELECT_STRING );

		setCursor( oldCursor );

		if( rc == JFileChooser.APPROVE_OPTION )
			file1TextField.setText( fileChooser.getSelectedFile().
									getAbsolutePath() );
	}

	/** Called when the openFile2Button is clicked */
	private void openFile2ButtonClicked()
	{
		Cursor oldCursor = getCursor();
		setCursor( new Cursor( Cursor.WAIT_CURSOR ) );

		JFileChooser fileChooser = getOpenFileChooser();

		FileFilter fileFilter = ( serFile2RadioButton.isSelected() ? 
								  serFileFilter : xmlFileFilter );

		getOpenFileChooser().resetChoosableFileFilters();
		getOpenFileChooser().setAcceptAllFileFilterUsed( false );
		getOpenFileChooser().addChoosableFileFilter( fileFilter );

		int rc = fileChooser.showDialog( this, SELECT_STRING );

		setCursor( oldCursor );

		if( rc == JFileChooser.APPROVE_OPTION )
			file2TextField.setText( fileChooser.getSelectedFile().
									getAbsolutePath() );
	}

	/** Called when the mergeAndSaveXMLButton is clicked */
	private void mergeAndSaveXMLButtonClicked()
	{
		Vector vector = null;
		
		try{ vector = getCombinedEntries(); }
		catch( Exception e ) { return; }

		if( vector == null || vector.size() == 0 ) return;

		Cursor oldCursor = getCursor();
		setCursor( new Cursor( Cursor.WAIT_CURSOR ) );

		getSaveFileChooser().resetChoosableFileFilters();
		getSaveFileChooser().setAcceptAllFileFilterUsed( false );
		getSaveFileChooser().addChoosableFileFilter( xmlFileFilter );

		int rc = getSaveFileChooser().showSaveDialog( this );

		if( rc == JFileChooser.APPROVE_OPTION )
		{
			try
			{
				String absolutePath = getSaveFileChooser().
									  getSelectedFile().getAbsolutePath();
				
				String fileName = ( absolutePath.endsWith( XML_EXT ) == false ?
									absolutePath + XML_EXT : absolutePath );
				
				if( checkIfFileShouldBeOverwritten( fileName ) == false )
				{
					setCursor( oldCursor );
					return;				
				}
				
				xmlRegPopulator.save( vector.elements(), fileName ); 
				
				JOptionPane.
				showMessageDialog( this, SUCCESSFULLY_SAVED_XML_FILE_MSG );
			}
			catch( Exception e )
			{
				String message = ERROR_SAVING_XML_FILE_STRING + 
								 getSaveFileChooser().getSelectedFile().
								 getAbsolutePath();

				JOptionPane.
				showMessageDialog( this, message, 
								   ERROR_SAVING_DIALOG_TITLE_STRING, 
								   JOptionPane.ERROR_MESSAGE ); 
			}
		}

		setCursor( oldCursor );
	}
	
    /** @return true if File exist and user wants to overrite, false otherwise */
    private boolean checkIfFileShouldBeOverwritten( String fileName )
    {
    	File file = new File( fileName );
    	
    	if( file.exists() )
    	{
            int userAnswer = JOptionPane.
                             showConfirmDialog( this, OVERWRITE_FILE_STRING, 
                             				    JPOS_ENTRY_MERGER_DIALOG_TITLE_STRING, 
                             				    JOptionPane.YES_NO_OPTION, 
                             				    JOptionPane.QUESTION_MESSAGE ); 
    
            return ( userAnswer == JOptionPane.YES_OPTION );    		
    	}
    	
    	return true;
    }	

	/** Called when the mergeAndSaveSerializedButton is clicked */
	private void mergeAndSaveSerializedButtonClicked()
	{
		Vector vector = null;
		
		try{ vector = getCombinedEntries(); }
		catch( Exception e ) { return; }

		if( vector == null || vector.size() == 0 ) return;

		Cursor oldCursor = getCursor();
		setCursor( new Cursor( Cursor.WAIT_CURSOR ) );

		getSaveFileChooser().resetChoosableFileFilters();
		getSaveFileChooser().setAcceptAllFileFilterUsed( false );
		getSaveFileChooser().addChoosableFileFilter( serFileFilter );

		int rc = getSaveFileChooser().showSaveDialog( this );

		if( rc == JFileChooser.APPROVE_OPTION )
		{
			try
			{
				String absolutePath = getSaveFileChooser().
									  getSelectedFile().getAbsolutePath();
				
				String fileName = ( absolutePath.endsWith( SER_EXT ) == false ?
									absolutePath + SER_EXT : absolutePath );

				if( checkIfFileShouldBeOverwritten( fileName ) == false )
				{
					setCursor( oldCursor );					
					return;
				}
				
				serRegPopulator.save( vector.elements(), fileName ); 

				JOptionPane.
				showMessageDialog( this, SUCCESSFULLY_SAVED_CFG_FILE_MSG );
			}
			catch( Exception e )
			{
				String message = ERROR_SAVING_XML_FILE_STRING + 
								 getSaveFileChooser().getSelectedFile().
								 getAbsolutePath();

				JOptionPane.
				showMessageDialog( this, message, 
								   ERROR_SAVING_DIALOG_TITLE_STRING, 
								   JOptionPane.ERROR_MESSAGE ); 
			}
		}

		setCursor( oldCursor );
	}

	/** 
	 * @return a Vector with the combined entries in file1 and file2
	 * @throws java.lang.Exception if something was wrong with one of the files
	 */
	private Vector getCombinedEntries() throws Exception
	{
		Vector vector = new Vector();

		Enumeration entries1 = vector.elements();
		Enumeration entries2 = vector.elements();

		try
		{
			if( file1TextField.getText().endsWith( XML_FILE_SUFFIX_STRING ) )
				entries1 = getJposEntriesFromXml( file1TextField.getText() );
			else
				entries1 = getJposEntriesFromSerialized( file1TextField.getText() );
		}
		catch( Exception e )
		{
			String message = ERROR_READING_FILE_STRING + file1TextField.getText();

			JOptionPane.
			showMessageDialog( this, message, ERROR_DIALOG_TITLE_STRING, 
						       JOptionPane.ERROR_MESSAGE ); 
			throw e;
		}

		try
		{
			if( file2TextField.getText().endsWith( XML_FILE_SUFFIX_STRING ) )
				entries2 = getJposEntriesFromXml( file2TextField.getText() );
			else
				entries2 = getJposEntriesFromSerialized( file2TextField.getText() );
		}
		catch( Exception e )
		{
			String message = ERROR_READING_FILE_STRING + file2TextField.getText();

			JOptionPane.
			showMessageDialog( this, message, ERROR_DIALOG_TITLE_STRING, 
							   JOptionPane.ERROR_MESSAGE ); 
			throw e;
		}

		while( entries1.hasMoreElements() )
			vector.addElement( entries1.nextElement() );

		while( entries2.hasMoreElements() )
			vector.addElement( entries2.nextElement() );

		return vector;
	}

	/**
	 * @return an Enumeration of JposEntry loaded from the XML file
	 * @param xmlFileName the XML file name
	 * @exception java.lang.Exception if something goes wrong
	 * @since 1.3 (SF 2K meeting)
	 */
	private Enumeration getJposEntriesFromXml( String xmlFileName ) 
	throws Exception
	{
		xmlRegPopulator.load( xmlFileName );

		if( xmlRegPopulator.getLastLoadException() != null ) 
			throw xmlRegPopulator.getLastLoadException();			

		return xmlRegPopulator.getEntries();
	}

	/**
	 * @return an Enumeration of JposEntry loaded from the serialized file
	 * @param serFileName the serialized file name
	 * @exception java.lang.Exception if something goes wrong
	 * @since 1.3 (SF 2K meeting)
	 */
	private Enumeration getJposEntriesFromSerialized( String serFileName ) 
	throws Exception
	{
		serRegPopulator.load( serFileName );

		if( serRegPopulator.getLastLoadException() != null ) 
			throw serRegPopulator.getLastLoadException();
			

		Enumeration entries = serRegPopulator.getEntries();
		if( entries.hasMoreElements() == false )
			throw new Exception( "Invalid .cfg file or empty file" );			

		return serRegPopulator.getEntries();
	}

	/** @return a lazily created JFileChooser for opening file */
	private JFileChooser getOpenFileChooser()
	{
		if( openFileChooser == null )
		{
			openFileChooser = new JFileChooser();
			openFileChooser.setDialogTitle( SELECT_FILE_CHOOSER_TITLE_STRING );
			openFileChooser.setDialogType( JFileChooser.OPEN_DIALOG );
			openFileChooser.setApproveButtonText( SELECT_STRING );
			openFileChooser.setApproveButtonToolTipText( SELECT_BUTTON_TOOLTIP_TEXT_STRING );
			openFileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
		}

		return openFileChooser;
	}

	/** @return a lazily created JFileChooser for saving file */
	private JFileChooser getSaveFileChooser()
	{
		if( saveFileChooser == null )
		{
			saveFileChooser = new JFileChooser();
			saveFileChooser.setDialogTitle( SAVE_FILE_CHOOSER_TITLE_STRING );
			saveFileChooser.setDialogType( JFileChooser.SAVE_DIALOG );
			saveFileChooser.setApproveButtonText( SAVE_STRING );
			saveFileChooser.setApproveButtonToolTipText( SAVE_BUTTON_TOOLTIP_TEXT_STRING );
		}

		return saveFileChooser;
	}

    //--------------------------------------------------------------------------
    // Inner classes
    //

	/**
	 * Inner class to filter out other file but serialized file when 
	 * listing file
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public static class SerFileFilter extends FileFilter
	{
		//---------------------------------------------------------------------
		// Public methods
		//

		/**
		 * Tests whether or not the specified abstract pathname should be
		 * included in a pathname list.
		 * @param  pathname  The abstract pathname to be tested
		 * @return  <code>true</code> if and only if <code>pathname</code>
		 *          should be included
		 */
		public boolean accept( File pathname ) 
		{
			if( pathname.isDirectory() ) return true;

			return pathname.getName().endsWith( SER_FILE_SUFFIX_STRING );
		}

		/** @return the description of this FileFilter */
		public String getDescription() { return SER_FILEFILTER_DESCRIPTION_STRING; }
	}

	/**
	 * Inner class to filter out other file but XML file when listing file
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public static class XmlFileFilter extends FileFilter
	{
		//---------------------------------------------------------------------
		// Public methods
		//

		/**
		 * Tests whether or not the specified abstract pathname should be
		 * included in a pathname list.
		 * @param  pathname  The abstract pathname to be tested
		 * @return  <code>true</code> if and only if <code>pathname</code>
		 *          should be included
		 */
		public boolean accept( File pathname ) 
		{
			if( pathname.isDirectory() ) return true;

			return pathname.getName().endsWith( XML_FILE_SUFFIX_STRING );
		}

		/** @return the description of this FileFilter */
		public String getDescription() 
		{ return XML_FILEFILTER_DESCRIPTION_STRING; }
	}

    //--------------------------------------------------------------------------
    // Public class methods
    //

	//<temp>
	public static void main( String[] args )
	{
		JFrame frame = new JFrame( "Simple test frame" );

		( new JposEntryMergerDialog( frame, false ) ).setVisible( true );
	}
	//</temp>
    
    //--------------------------------------------------------------------------
    // Instance variables
    //

	private JFileChooser openFileChooser = null;
	private JFileChooser saveFileChooser = null;

	private JButton openFile1Button = new JButton( "..." );
	private JButton openFile2Button = new JButton( "..." );

	private JRadioButton serFile1RadioButton = 
						  new JRadioButton( SER_STRING, true );
						
	private JRadioButton serFile2RadioButton = 
						  new JRadioButton( SER_STRING, true );
	
	private JRadioButton xmlFile1RadioButton = new JRadioButton( XML_STRING );
	private JRadioButton xmlFile2RadioButton = new JRadioButton( XML_STRING );

	private JTextField file1TextField = new JTextField( 25 );
	private JTextField file2TextField = new JTextField( 25 );

	private JButton mergeSaveXMLButton = new JButton( MERGE_SAVE_XML_STRING );
	
	private JButton mergeSaveSerializedButton = 
				     new JButton( MERGE_SAVE_SERIALIZED_STRING );

	private JposRegPopulator xmlRegPopulator = new SimpleXmlRegPopulator();
	private JposRegPopulator serRegPopulator = new SimpleRegPopulator();

	private FileFilter serFileFilter = new SerFileFilter();
	private FileFilter xmlFileFilter = new XmlFileFilter();

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String XML_EXT = ".xml";
	public static final String SER_EXT = ".cfg";

	//<i18n>
	public static final String SER_STRING = "Serialized";
	public static final String XML_STRING = "XML";

	public static final String MERGE_SAVE_XML_STRING = 
								 "Merge and save XML...";
								 
	public static final String MERGE_SAVE_SERIALIZED_STRING = 
								 "Merge and save Serialized...";

	public static final String JPOS_ENTRY_MERGER_DIALOG_TITLE_STRING = 
								 "JposEntry Merger";

	public static final String SELECT_FILE_CHOOSER_TITLE_STRING = 
								 "Select JposEntries serialized or XML file";
	
	public static final String SELECT_STRING = "Select";
	
	public static final String SELECT_BUTTON_TOOLTIP_TEXT_STRING = 
								 "Click to select file";

	public static final String SAVE_FILE_CHOOSER_TITLE_STRING = 
								 "Save JposEntries serialized or XML file";
	
	public static final String SAVE_STRING = "Save";
	
	public static final String SAVE_BUTTON_TOOLTIP_TEXT_STRING = 
								 "Click to save file";
									 
	public static final String SER_FILE_SUFFIX_STRING = ".cfg";
	
	public static final String SER_FILEFILTER_DESCRIPTION_STRING = 
				                 "JposEntry serialized file (*.cfg)";

	public static final String XML_FILE_SUFFIX_STRING = ".xml";
	
	public static final String XML_FILEFILTER_DESCRIPTION_STRING = 
								 "JposEntry XML file (*.xml)";

	public static final String ERROR_DIALOG_TITLE_STRING = 
								 "Error loading XML or serialized file";
	
	public static final String ERROR_READING_FILE_STRING = 
								 "Error reading file: ";

	public static final String ERROR_SAVING_XML_FILE_STRING = 
								 "Error saving XML file: ";
								 
	public static final String ERROR_SAVING_SERIALIZED_FILE_STRING = 
								 "Error saving serialized file: ";
	
	public static final String ERROR_SAVING_DIALOG_TITLE_STRING = 
								 "Error saving XML or Serialized file";

	public static final String SUCCESSFULLY_SAVED_XML_FILE_MSG = 
								 "Successfully saved XML file...";
	
	public static final String SUCCESSFULLY_SAVED_CFG_FILE_MSG = 
								 "Successfully saved serialized file...";
								 
	public static final String OVERWRITE_FILE_STRING = 
								 "File exists, overwrite?";								 
	//</i18n>
}