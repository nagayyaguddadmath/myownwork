package com.bskyb.internettv.parental_control_service;

import com.bskyb.internettv.failure_exception.WrongArgumentException;
import com.bskyb.internettv.thirdparty.MovieService;
import com.bskyb.internettv.thirdparty.TechnicalFailureException;
import com.bskyb.internettv.thirdparty.TitleNotFoundException;

public class ParentalControlServiceImpl implements ParentalControlService {

	public ParentalControlServiceImpl(MovieService movieService) {
		this.movieService = movieService;
	}

	private MovieService movieService;

	private static final String TITLENOTFOUND_ERROR = "Movie Title Not found";
	private static final String THIRDPARTYWRONGCONTROL_ERROR = "Parental control is not provided, Please set parental control";

	public boolean canWatchMovie(String customerParentalControlLevel, String movieId) throws Exception {
		try {
			validateInputParameters(customerParentalControlLevel, movieId);
			ParentalControl parentControlLevel = validateAndReturnControlLevel(customerParentalControlLevel);
			String movieServiceLevel = movieService.getParentalControlLevel(movieId);
			ParentalControl movieControlLevel  = validateAndReturnControlLevel(movieServiceLevel);

			return parentControlLevel.getPriority() >= movieControlLevel.getPriority();
			
		} catch (TitleNotFoundException e) {
			throw new WrongArgumentException(TITLENOTFOUND_ERROR, "movieId");
		} catch (TechnicalFailureException e) {
			return false;
		} catch (WrongArgumentException e) {
			return false;
		}
	}

	private void validateInputParameters(String customerParentalControlLevel, String movieId) throws Exception {
		if (movieId == null || movieId.length() < 1) {
			throw new WrongArgumentException(TITLENOTFOUND_ERROR, "movieId");
		}
		if (customerParentalControlLevel == null || customerParentalControlLevel.length() < 1) {
			throw new WrongArgumentException(TITLENOTFOUND_ERROR, "customerParentalControlLevel");
		}
	}

	private ParentalControl validateAndReturnControlLevel (String movieServiceLevel ) throws Exception {
		ParentalControl movieControl = ParentalControl.getControlevel(movieServiceLevel);
		if (movieControl == null) {
			throw new WrongArgumentException(THIRDPARTYWRONGCONTROL_ERROR, "movieId");
		}
		return movieControl;
	}

}
