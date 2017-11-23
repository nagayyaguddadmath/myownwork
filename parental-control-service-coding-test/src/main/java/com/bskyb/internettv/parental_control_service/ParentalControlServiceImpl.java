package com.bskyb.internettv.parental_control_service;

import com.bskyb.internettv.thirdparty.MovieService;
import com.bskyb.internettv.thirdparty.TechnicalFailureException;
import com.bskyb.internettv.thirdparty.TitleNotFoundException;

public class ParentalControlServiceImpl implements ParentalControlService {

	public ParentalControlServiceImpl(MovieService movieService) {
		this.movieService = movieService;
	}

	private MovieService movieService;

	private static String TITLENOTFOUND_ERROR = "Movie Title Not found";
	private static String TECHNICAL_ERROR = "Error occurred, currently you are not allowed to watch this movie";
	private static String CONTROLNOTSET_ERROR = "Parental control is not provided, Please set parental control";
	private static String SERVICEWRONGCONTROL_ERROR = "Parental control is not provided, Please set parental control";

	public boolean canWatchMovie(String customerParentalControlLevel, String movieId) throws Exception {
		try {
			ParentalControl parentControlLevel = validateInput(customerParentalControlLevel, movieId);
			String movieServiceLevel = movieService.getParentalControlLevel(movieId);
			ParentalControl movieControlLevel  = validateAndReturnControlLevel(movieServiceLevel);

			if (parentControlLevel.getPriority() >= movieControlLevel.getPriority()) {
				return true;
			} else {
				return false;
			}
		} catch (TitleNotFoundException e) {
			throw new Exception(TITLENOTFOUND_ERROR);
		} catch (TechnicalFailureException e) {
			throw new Exception(TECHNICAL_ERROR);
		} catch (Exception exceptio) {
			throw exceptio;
		}
	}

	private ParentalControl validateInput(String customerParentalControlLevel, String movieId) throws Exception {
		if (movieId == null || movieId.length() < 1) {
			throw new Exception(TITLENOTFOUND_ERROR);
		}
		if (customerParentalControlLevel == null || customerParentalControlLevel.length() < 1) {
			throw new Exception(CONTROLNOTSET_ERROR);
		}

		return validateAndReturnControlLevel(customerParentalControlLevel);
	}

	private ParentalControl validateAndReturnControlLevel (String movieServiceLevel ) throws Exception {
		ParentalControl movieControl = ParentalControl.getControlevel(movieServiceLevel);
		if (movieControl == null) {
			throw new Exception(SERVICEWRONGCONTROL_ERROR);
		}
		return movieControl;
	}

}
