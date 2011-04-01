package com.skinkers.giffgaffAPN;

import com.skinkers.helloandroid.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class GiffGaff extends Activity 
{
	
	private Button setAsDefaultButton;
    private Button addButton;
    private Button deleteButton;
    private Button deleteOtherButton;
    private TextView welcomeText;
    private TextView infoText;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
       
        
        this.setContentView( R.layout.main );
        
        //Get UI elements
        this.welcomeText = (TextView) this.findViewById(R.id.welcomeText);
        this.infoText = (TextView) this.findViewById(R.id.infoText);
        this.setAsDefaultButton = (Button) this.findViewById(R.id.setAsDefaultButton);
        this.addButton = (Button) this.findViewById(R.id.addButton);
        this.deleteButton = (Button) this.findViewById(R.id.deleteButton);
        this.deleteOtherButton = (Button) this.findViewById(R.id.deleteOtherButton);
        
        
        this.welcomeText.setText( R.string.welcome );
        
        checkState();
        
       
        
       
        
    }
    
    


    
    @Override
    public void onResume() 
    {
    	super.onResume();
    	 checkState();
    }
    
    
    
    public void checkState()
    {
    	//Check if we have a giff gaff APN
        int id = GiffGaffAPN.getGiffGaffAPNID(this);
        
        if (GiffGaffAPN.getOtherAPNNames(this).length()==0)
        {
        	deleteOtherButton.setVisibility(View.GONE);
        	
        }
        else
        {
        	this.infoText.setText( R.string.recommend_delete );
        	deleteOtherButton.setVisibility(View.VISIBLE);
        }
        
    	//Update UI
        if ( id > -1 )
        {
        	if (GiffGaffAPN.isGiffGaffDefault(this))
        	{
        		this.infoText.setText( R.string.giffgaff_found_default );
        		setAsDefaultButton.setVisibility(View.GONE);
        	}
        	else
        	{
        		this.infoText.setText( R.string.giffgaff_found_not_default );
        		setAsDefaultButton.setVisibility(View.VISIBLE);
        	}
        	
        	
        	addButton.setVisibility(View.GONE);
        	deleteButton.setVisibility(View.VISIBLE);
        }
        else
        {
        	this.infoText.setText( R.string.giffgaff_not_found );
        	
        	addButton.setVisibility(View.VISIBLE);
        	
        	setAsDefaultButton.setVisibility(View.GONE);
        	deleteButton.setVisibility(View.GONE);
        }
        
        
        
       
        
    }
    
    
    public void addButtonOnClick(View view)
    {
    	 int id = GiffGaffAPN.getGiffGaffAPNID(this);
    	 
    	if (id == -1)
    	{
    		boolean added = GiffGaffAPN.addGiffGaff(this);
         
    		if (added)
    			Toast.makeText(this, R.string.add_notification, Toast.LENGTH_LONG).show();
    		else
    			Toast.makeText(this, R.string.add_failed_notification, Toast.LENGTH_LONG).show();
    	}
    	else
    	{
    		Toast.makeText(this, R.string.exists_notification, Toast.LENGTH_LONG).show();
    	}
    	
    	checkState();
    }
    
    
    public void doDelete()
    {
    	GiffGaffAPN.removeGiffGaff(this);
    	
    	checkState();
    }
    
    public void deleteButtonOnClick(View view)
    {
    	 new AlertDialog.Builder(this)
         .setIcon(android.R.drawable.ic_dialog_alert)
         .setTitle(R.string.confirm_delete_title)
         .setMessage( R.string.confirm_delete_giff_message )
         .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

        	 public void onClick(DialogInterface dialog, int which) 
             {

            	 doDelete();
             }

         })
         .setNegativeButton(R.string.no, null)
         .show();
    }
    
    
    
    public void setDefaultOnClick(View view)
    {
    	int id = GiffGaffAPN.getGiffGaffAPNID(this);
    	
    	if (GiffGaffAPN.setDefaultAPN(id, this))
    		Toast.makeText(this, R.string.set_default_notification, Toast.LENGTH_LONG).show();
    	else
    		Toast.makeText(this, R.string.set_default_failed_notification, Toast.LENGTH_LONG).show();
    	
    	checkState();
    }
    
    public void deleteOthersButtonOnClick(View view)
    {
    	 new AlertDialog.Builder(this)
         .setIcon(android.R.drawable.ic_dialog_alert)
         .setTitle(R.string.confirm_delete_title)
         .setMessage( getResources().getString(R.string.confirm_delete_message, GiffGaffAPN.getOtherAPNNames(this) ) )
         .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

        	 
        	 
             public void onClick(DialogInterface dialog, int which) 
             {

            	 doDeleteOthers();
             }

         })
         .setNegativeButton(R.string.no, null)
         .show();

    	 
    	
    }
    
    private void doDeleteOthers()
    {
    	int count= GiffGaffAPN.removeOtherAPNs(this);
        
        if (count>0)
        	Toast.makeText(this, getResources().getString(R.string.other_deleted_notification, count), Toast.LENGTH_LONG).show();
   	 else
   	 	Toast.makeText(this, R.string.other_not_found_notification, Toast.LENGTH_LONG).show();
        
        checkState();  
    }
    
    public void showAPNSButtonOnClick(View view)
    {
    	startActivity(new Intent(Settings.ACTION_APN_SETTINGS));
    }
    
    
   
    
    
}