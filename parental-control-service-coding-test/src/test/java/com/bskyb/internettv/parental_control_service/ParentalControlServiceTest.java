package com.bskyb.internettv.parental_control_service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.bskyb.internettv.thirdparty.MovieService;
import com.bskyb.internettv.thirdparty.TechnicalFailureException;
import com.bskyb.internettv.thirdparty.TitleNotFoundException;


public class ParentalControlServiceTest {

	private static MovieService movieService;
	private static ParentalControlService parentalControlService;

	private static String TITLENOTFOUND_ERROR = "Movie Title Not found";
	private static String TECHNICAL_ERROR = "Error occurred, currently you are not allowed to watch this movie";
	private static String CONTROLNOTSET_ERROR = "Parental control is not provided, Please set parental control";
	private static String SERVICEWRONGCONTROL_ERROR = "Parental control is not provided, Please set parental control";

	private static String MOVIE_TITLE = "MATRIX";

	@BeforeClass
	public static void setUp(){
		//Create mock object of MovieService
		movieService = mock(MovieService.class);

		//		parentalControlService = mock(ParentalControlService.class);
		parentalControlService = new ParentalControlServiceImpl(movieService);
	}

	@Test
	public void test_both_null_parameters() {

		try {
			//			Mockito.doReturn(false).when(parentalControlService).canWatchMovie(null, null);

			boolean canWatch = parentalControlService.canWatchMovie(null, null);
			fail("Error should be TitleNotFoundException, but returned value is: " + canWatch);
		} catch (TitleNotFoundException e) {
			fail("This error should have been converted to Exception");
		} catch (TechnicalFailureException e) {
			fail("This error should have been converted to Exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(TITLENOTFOUND_ERROR));
		}
	}

	@Test
	public void test_both_blank_parameters() {

		try {
			//			Mockito.doReturn(false).when(parentalControlService).canWatchMovie("", "");

			boolean canWatch = parentalControlService.canWatchMovie("", "");
			fail("Error should be TitleNotFoundException, but returned value is: " + canWatch);
		} catch (TitleNotFoundException e) {
			fail("Error should be Exception");
		} catch (TechnicalFailureException e) {
			fail("Error should be Exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(TITLENOTFOUND_ERROR));
		}
	}

	@Test
	public void test_title_null_parameter() {

		try {
			//			Mockito.doReturn(false).when(parentalControlService).canWatchMovie(ParentalControl.LEVEL_PG.getLevel(), null);

			boolean canWatch = parentalControlService.canWatchMovie(ParentalControl.LEVEL_PG.getLevel(), null);
			fail("Error should be TitleNotFoundException, but returned value is:" + canWatch);
		} catch (TitleNotFoundException e) {
			fail("Error should be Exception");
		} catch (TechnicalFailureException e) {
			fail("Error should be Exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(TITLENOTFOUND_ERROR));
		}
	}

	@Test
	public void test_title_blank_parameter() {

		try {
			//			Mockito.doReturn(false).when(parentalControlService).canWatchMovie(ParentalControl.LEVEL_PG.getLevel(), "");

			boolean canWatch = parentalControlService.canWatchMovie(null, "");
			fail("Error should be TitleNotFoundException, but returned value is:" + canWatch);
		} catch (TitleNotFoundException e) {
			fail("Error should be Exception");
		} catch (TechnicalFailureException e) {
			fail("Error should be Exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(TITLENOTFOUND_ERROR));
		}
	}

	@Test
	public void test_parentcontrol_null_parameter() {

		try {
			//			Mockito.doReturn(false).when(parentalControlService).canWatchMovie(null, MOVIE_TITLE);

			boolean canWatch = parentalControlService.canWatchMovie(null, MOVIE_TITLE);
			fail("Error should be thrown as parental control is not set, but returned value is:" + canWatch);
		} catch (TitleNotFoundException e) {
			fail("Error should be Exception");
		} catch (TechnicalFailureException e) {
			fail("Error should be Exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(CONTROLNOTSET_ERROR));
		}
	}

	@Test
	public void test_parentcontrol_blank_parameter() {

		try {
			//			Mockito.doReturn(false).when(parentalControlService).canWatchMovie("", MOVIE_TITLE);

			boolean canWatch = parentalControlService.canWatchMovie("", MOVIE_TITLE);
			fail("Error should be thrown as parental control is not set, but returned value is:" + canWatch);
		} catch (TitleNotFoundException e) {
			fail("Error should be Exception");
		} catch (TechnicalFailureException e) {
			fail("Error should be Exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(CONTROLNOTSET_ERROR));
		}
	}

	@Test
	public void test_titlenotfound_error() {

		try {
			TitleNotFoundException exception = new TitleNotFoundException();
			Mockito.doThrow(exception).when(movieService).getParentalControlLevel(MOVIE_TITLE);

			//			Mockito.doReturn(false).when(parentalControlService).canWatchMovie(ParentalControl.LEVEL_PG.getLevel(), MOVIE_TITLE);

			boolean canWatch = parentalControlService.canWatchMovie(ParentalControl.LEVEL_PG.getLevel(), MOVIE_TITLE);
			fail("Error should be TitleNotFoundException, but returned value is:" + canWatch);
		} catch (TitleNotFoundException e) {
			fail("Error should be Exception");
		} catch (TechnicalFailureException e) {
			fail("Error should be Exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(TITLENOTFOUND_ERROR));
		}
	}


	@Test
	public void test_technical_error() {

		try {
			TechnicalFailureException exception = new TechnicalFailureException();
			Mockito.doThrow(exception).when(movieService).getParentalControlLevel(MOVIE_TITLE);

			//			Mockito.doReturn(false).when(parentalControlService).canWatchMovie(ParentalControl.LEVEL_PG.getLevel(), MOVIE_TITLE);

			boolean canWatch = parentalControlService.canWatchMovie(ParentalControl.LEVEL_PG.getLevel(), MOVIE_TITLE);
			fail("Technical Error should have occurred, but returned value is:" + canWatch);
		} catch (TitleNotFoundException e) {
			fail("Technical Error should have occurred");
		} catch (TechnicalFailureException e) {
			fail("Technical should have been converted to Exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(TECHNICAL_ERROR));
		}
	}

	@Test
	public void test_serviceWrongLevel_error() {

		try {
			Mockito.doReturn("22").when(movieService).getParentalControlLevel(MOVIE_TITLE);

			//			Mockito.doReturn(false).when(parentalControlService).canWatchMovie(ParentalControl.LEVEL_PG.getLevel(), MOVIE_TITLE);

			boolean canWatch = parentalControlService.canWatchMovie(ParentalControl.LEVEL_PG.getLevel(), MOVIE_TITLE);
			fail("Technical Error should have occurred, but returned value is:" + canWatch);
		} catch (TitleNotFoundException e) {
			fail("TitleNotFoundException should have been converted to Exception");
		} catch (TechnicalFailureException e) {
			fail("Technical should have been converted to Exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(SERVICEWRONGCONTROL_ERROR));
		}
	}

	@Test
	public void test_18_movietitle() {
		assertEquals("With 18 control, one movie should be allowed", 1, getNoOfMoviewAllowed(ParentalControl.LEVEL_18.getLevel()));
	}

	@Test
	public void test_15_movietitle() {
		assertEquals("With 15 control, two movies should be allowed", 2, getNoOfMoviewAllowed(ParentalControl.LEVEL_15.getLevel()));
	}

	@Test
	public void test_12_movietitle() {
		assertEquals("With 12 control, three movies should be allowed", 3, getNoOfMoviewAllowed(ParentalControl.LEVEL_12.getLevel()));
	}

	@Test
	public void test_PG_movietitle() {
		assertEquals("With PG control, four movies should be allowed", 4, getNoOfMoviewAllowed(ParentalControl.LEVEL_PG.getLevel()));
	}

	@Test
	public void test_U_movietitle() {
		assertEquals("With U control, five movies should be allowed", 5, getNoOfMoviewAllowed(ParentalControl.LEVEL_U.getLevel()));
	}


	private int getNoOfMoviewAllowed(String movieControlLevel) {
		int noOfAllowed = 0;
		for (ParentalControl parentControl : ParentalControl.values()) {
			try {
				Mockito.doReturn(movieControlLevel).when(movieService).getParentalControlLevel(MOVIE_TITLE);
				//				when(movieService.getParentalControlLevel(MOVIE_TITLE)).thenReturn(movieControlLevel);

				//				when(parentalControlService.canWatchMovie(parentControl.getLevel(), MOVIE_TITLE)).thenReturn(false);

				boolean canWatch = parentalControlService.canWatchMovie(parentControl.getLevel(), MOVIE_TITLE);
				if (canWatch == true) {
					noOfAllowed++;
				}
			} catch (TitleNotFoundException e) {
				fail("Error should be TitleNotFoundException");
			} catch (TechnicalFailureException e) {
				fail("Error should be TitleNotFoundException");
			} catch (Exception e) {
				assertEquals("Parental control is not provided, Please set parental control", e.getMessage());
			}
		}
		return noOfAllowed;

	}

}
