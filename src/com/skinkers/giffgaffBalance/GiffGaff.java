package com.skinkers.giffgaffBalance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;




public class GiffGaff extends Activity 
{
	
	private Button setAsDefaultButton;
    private Button addButton;
    private Button deleteButton;
    private Button deleteOtherButton;
    private TextView infoText;
    private TextView statusText;
    private ImageView statusIcon;
    
   
    
    private static final int STATUS_NOT_CONNECTED = 1;
    private static final int STATUS_POSSIBLE_CONFLICT = 2;
    private static final int STATUS_CONNECTED = 3;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
       
        
        this.setContentView( R.layout.main );
        
        //Get UI elements
        this.infoText = (TextView) this.findViewById(R.id.infoText);
        this.statusText = (TextView) this.findViewById(R.id.statusInfo);
        this.setAsDefaultButton = (Button) this.findViewById(R.id.setAsDefaultButton);
        this.addButton = (Button) this.findViewById(R.id.addButton);
        this.deleteButton = (Button) this.findViewById(R.id.deleteButton);
        this.deleteOtherButton = (Button) this.findViewById(R.id.deleteOtherButton);
        this.statusIcon = (ImageView) this.findViewById(R.id.statusIcon );
        
        
       
        
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
        int id = GiffGaffAPN.getGiffGaffAPNID( this );
        
        addButton.setVisibility(View.GONE);
        setAsDefaultButton.setVisibility(View.GONE);
		deleteOtherButton.setVisibility(View.GONE);
		deleteButton.setVisibility(View.GONE);
        
    	//Update UI
        if ( id > -1 )
        {
        	if (GiffGaffAPN.isGiffGaffDefault(this))
        	{
        		if (GiffGaffAPN.getConflictingAPNNames(this).length()==0)
                {
        			this.infoText.setText( R.string.giffgaff_found_default );
        			setStatus(STATUS_CONNECTED);
        		}
                else
                {
                	setStatus(STATUS_POSSIBLE_CONFLICT);
                	this.infoText.setText( R.string.recommend_delete );
                	deleteOtherButton.setVisibility(View.VISIBLE);
                }
        	}
        	else
        	{
        		setStatus(STATUS_NOT_CONNECTED);
        		this.infoText.setText( R.string.giffgaff_found_not_default );
        		setAsDefaultButton.setVisibility(View.VISIBLE);
        	}
        	
        	deleteButton.setVisibility(View.VISIBLE);
        }
        else
        {
        	this.infoText.setText( R.string.giffgaff_not_found );
        	addButton.setVisibility(View.VISIBLE);
        	
        	setStatus(STATUS_NOT_CONNECTED);
        }
    }
    
    
    private void setStatus(int status) 
    {
		switch (status)
		{
			case STATUS_CONNECTED :
				statusText.setText( R.string.status_1 );
				statusText.setTextColor( this.getResources().getColor( R.color.status_1_col ));
				statusIcon.setImageResource( R.drawable.apn_status_1 );
				
			break;
			 
			case STATUS_POSSIBLE_CONFLICT :
				statusText.setText( R.string.status_2 );
				statusText.setTextColor( this.getResources().getColor(  R.color.status_2_col ));
				statusIcon.setImageResource( R.drawable.apn_status_2 );
			break;
			
			case STATUS_NOT_CONNECTED :
				statusText.setText( R.string.status_3 );
				statusText.setTextColor( this.getResources().getColor( R.color.status_3_col ));
				statusIcon.setImageResource( R.drawable.apn_status_3 );
			break;
			
		
			
			
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
         .setMessage( getResources().getString(R.string.confirm_delete_message, GiffGaffAPN.getConflictingAPNNames(this) ) )
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