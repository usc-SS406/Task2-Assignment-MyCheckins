package com.mycheckins.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;


/**
 * This class is a Location utility class which is used to retrieve the user's location.
 */
public enum AppLocationClass implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
	INSTANCE;
	private GoogleApiClient mGoogleApiClient;
	private Activity mActivity;
	private LocationRequest mLocationRequest;
	private Location mCurrentLocation;
	/*
	 * Note if updates have been turned on. Starts out as "false"; is set to "true" in the method handleRequestSuccess of LocationUpdateReceiver.
	 */
	boolean mUpdatesRequested = false;
	private String mLastUpdateTime;

	/**
	 * This method is used to initialize location services
	 *
	 * @param activity
	 * @return void
	 */
	public void buildGoogleApiClient(Activity activity)
	{
		mGoogleApiClient = new GoogleApiClient.Builder(activity)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
	}

	public void startServices(Activity activity)
	{
		this.mActivity = activity;
		buildGoogleApiClient(mActivity);
		/*
		 * Connect the client. Don't re-start any requests here; instead, wait for onResume()
		 */
		if (!servicesConnected()){
			mGoogleApiClient.connect();
		}
		else{
			if (getLocation() != null){

				Log.e("location",""+mCurrentLocation.getLatitude()+"----"+mCurrentLocation.getLongitude());
				//
			}
		}
	}

	public void stopServices()
	{
		// If the client is connected
		if (mGoogleApiClient.isConnected()){
			stopPeriodicUpdates();
		}

		mGoogleApiClient.disconnect();
	}

	/**
	 * In response to a request to start updates, send a request to Location Services
	 */
	@SuppressLint("MissingPermission")
	private void startPeriodicUpdates()
	{
		LocationRequest mLocationRequest = new LocationRequest();
		// To update
//		mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
//		mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

	}

	/**
	 * In response to a request to stop updates, send a request to Location Services
	 */
	public void stopPeriodicUpdates()
	{
		if (mGoogleApiClient != null)
		{
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
		}
	}

	/**
	 * Verify that Google Play services is available before making a request.
	 *
	 * @return true if Google Play services is available, otherwise false
	 */
	private boolean servicesConnected()
	{
		try {
			// Check that Google Play services is available
//		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
			int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mActivity);


			// If Google Play services is available
			if (ConnectionResult.SUCCESS == resultCode) {
				// Continue
				if (mGoogleApiClient.isConnected())
					return true;
				else
					return false;
			}
			// Google Play services was not available for some reason
			else {
				// Display an error dialog
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, mActivity, 70);
				if (dialog != null) {
					dialog.show();
				}
				return false;
			}
		}
		catch (Exception e)
		{
			Log.e("LOCATION","CONNECTION ERROR");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Invoked by the "Get Location" button.
	 *
	 * Calls getLastLocation() to get the current location
	 *
	 *            The view object associated with this method, in this case a Button.
	 */
	@SuppressLint("MissingPermission")
	public Location getLocation()
	{
		// If Google Play Services is available
		if (servicesConnected())
		{
			// Get the current location
			mCurrentLocation  = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			return mCurrentLocation;
		}
		return null;
	}

	/**
	 * Invoked by the "Start Updates" button Sends a request to start location updates
	 *
	 *            The view object associated with this method, in this case a Button.
	 */
	public void startUpdates()
	{
		mUpdatesRequested = true;
		if (servicesConnected())
		{
			startPeriodicUpdates();
		}
	}

	/**
	 * Invoked by the "Stop Updates" button Sends a request to remove location updates request them.
	 *
	 * The view object associated with this method, in this case a Button.
	 */
	public void stopUpdates(){
		mUpdatesRequested = false;
		if (servicesConnected()){
			stopPeriodicUpdates();
		}
	}

	/**
	 * /* Called by Location Services when the request to connect the client finishes successfully. At this point, you can request the current location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle bundle){
		startUpdates();
		if (getLocation() != null)
		{
			Log.e("location", "" + mCurrentLocation.getLatitude() + "----" + mCurrentLocation.getLongitude());

		}
		if (mUpdatesRequested && servicesConnected())
		{
			startPeriodicUpdates();
		}
		else
		{
			startServices(mActivity);
		}
	}

	@Override
	public void onConnectionSuspended(int i) {

		mGoogleApiClient.connect();
	}

	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult){
		Log.i(getClass().getCanonicalName(), "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
	}

	@Override
	public void onLocationChanged(Location location){
		mCurrentLocation = location;
//		AppPreferences.INSTANCE.setLatitude(""+ location.getLatitude());
//		AppPreferences.INSTANCE.setLongitude(""+ location.getLongitude());

		Log.e("Location==>>", "" + mCurrentLocation.getLatitude() + "----" + mCurrentLocation.getLongitude());

		mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

		Log.e("Current Time==>>",""+mLastUpdateTime);
	}
}
