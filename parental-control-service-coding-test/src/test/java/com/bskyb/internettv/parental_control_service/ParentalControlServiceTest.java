package com.bskyb.internettv.parental_control_service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.bskyb.internettv.failure_exception.WrongArgumentException;
import com.bskyb.internettv.thirdparty.MovieService;
import com.bskyb.internettv.thirdparty.TechnicalFailureException;
import com.bskyb.internettv.thirdparty.TitleNotFoundException;


public class ParentalControlServiceTest {

	private static MovieService movieService;
	private static ParentalControlService parentalControlService;

	private static final String TITLENOTFOUND_ERROR = "Movie Title Not found";

	private static String MOVIE_TITLE = "MATRIX";

	@BeforeClass
	public static void setUp(){
		//Create mock object of MovieService
		movieService = mock(MovieService.class);

		parentalControlService = new ParentalControlServiceImpl(movieService);
	}

	@Test
	public void throwArgumenrErrorIfTitleNotFindErrorFromThirdParty() {

		try {
			TitleNotFoundException exception = new TitleNotFoundException();
			Mockito.doThrow(exception).when(movieService).getParentalControlLevel(MOVIE_TITLE);

			boolean canWatch = parentalControlService.canWatchMovie(ParentalControl.LEVEL_PG.getLevel(), MOVIE_TITLE);
			fail("Error should be TitleNotFoundException, but returned value is:" + canWatch);
		} catch (Exception e) {
			assertTrue(e instanceof WrongArgumentException);
			assertTrue(e.getMessage().contains(TITLENOTFOUND_ERROR));
		}
	}

	@Test
	public void returnFalseIfTechnicalErrorFromThirdParty() {

		try {
			TechnicalFailureException exception = new TechnicalFailureException();
			Mockito.doThrow(exception).when(movieService).getParentalControlLevel(MOVIE_TITLE);

			boolean canWatch = parentalControlService.canWatchMovie(ParentalControl.LEVEL_PG.getLevel(), MOVIE_TITLE);
			assertTrue("For technical errors, return false", canWatch == false);
		} catch (Exception e) {
			assertTrue("For technical errors, return false", false);
		}
	}

	@Test
	public void userWithParentalControlLevel18CanWatchMovieWithAnyRating() throws Exception {
		ParentalControl userLevel = ParentalControl.LEVEL_18;
		final List<ParentalControl> allowedRating = Arrays.asList(ParentalControl.values());

		verifyUserCanWatch(userLevel, allowedRating);
	}

	@Test
	public void userWithParentalControlLevel15CanWatchMoviesExcept15And18() throws Exception {
		ParentalControl userLevel = ParentalControl.LEVEL_15;
		final List<ParentalControl> allowedRating = Arrays.asList(ParentalControl.LEVEL_U, ParentalControl.LEVEL_PG,
				ParentalControl.LEVEL_12, ParentalControl.LEVEL_15);

		verifyUserCanWatch(userLevel, allowedRating);
	}

	@Test
	public void userWithParentalControlLevel12CanWatchMoviesWithUAndPGAnd12() throws Exception {
		ParentalControl userLevel = ParentalControl.LEVEL_12;
		final List<ParentalControl> allowedRating = Arrays.asList(ParentalControl.LEVEL_U, ParentalControl.LEVEL_PG,
				ParentalControl.LEVEL_12);

		verifyUserCanWatch(userLevel, allowedRating);
	}

	@Test
	public void userWithParentalControlLevelPGCanWatchMoviesWithUAndPG() throws Exception {
		ParentalControl userLevel = ParentalControl.LEVEL_PG;
		final List<ParentalControl> allowedRating = Arrays.asList(ParentalControl.LEVEL_U, ParentalControl.LEVEL_PG);

		verifyUserCanWatch(userLevel, allowedRating);
	}

	@Test
	public void userWithParentalControlLevelUCanWatchMoviesWithU() throws Exception {
		ParentalControl userLevel = ParentalControl.LEVEL_18;
		final List<ParentalControl> allowedRating = Arrays.asList(ParentalControl.LEVEL_U);

		verifyUserCanWatch(userLevel, allowedRating);
	}

	@Test
	public void userWithParentalControlLevelUCannotWatchMoviesExceptU() throws Exception {
		ParentalControl userLevel = ParentalControl.LEVEL_U;
		final List<ParentalControl> allowedRating = Arrays.asList(ParentalControl.LEVEL_PG,
				ParentalControl.LEVEL_12, ParentalControl.LEVEL_15, ParentalControl.LEVEL_18);

		verifyUserCannotWatch(userLevel, allowedRating);
	}

	@Test
	public void userWithParentalControlLevelPGCannotWatchMoviesExceptUAndPG() throws Exception {
		ParentalControl userLevel = ParentalControl.LEVEL_U;
		final List<ParentalControl> allowedRating = Arrays.asList(ParentalControl.LEVEL_12, ParentalControl.LEVEL_15,
				ParentalControl.LEVEL_18);

		verifyUserCannotWatch(userLevel, allowedRating);
	}

	@Test
	public void userWithParentalControlLevel12CannotWatchMoviesWith15And18() throws Exception {
		ParentalControl userLevel = ParentalControl.LEVEL_U;
		final List<ParentalControl> allowedRating = Arrays.asList(ParentalControl.LEVEL_15, ParentalControl.LEVEL_18);

		verifyUserCannotWatch(userLevel, allowedRating);
	}

	@Test
	public void userWithParentalControlLevel15CannotWatchMoviesWith18() throws Exception {
		ParentalControl userLevel = ParentalControl.LEVEL_U;
		final List<ParentalControl> allowedRating = Arrays.asList(ParentalControl.LEVEL_18);

		verifyUserCannotWatch(userLevel, allowedRating);
	}

	@Test
	public void returnFalseIfBothParentcontrolLevelAndMovieIDIsNull() {

		try {

			boolean canWatch = parentalControlService.canWatchMovie(null, null);
			assertTrue("For argument errors, return false", canWatch == false);
		} catch (Exception e) {
			assertTrue("For argument errors, return false", false);
		}
	}

	@Test
	public void returnFalseIfBothParentcontrolLevelAndMovieIDIsBlank() {

		try {

			boolean canWatch = parentalControlService.canWatchMovie("", "");
			assertTrue("For argument errors, return false", canWatch == false);
		} catch (Exception e) {
			assertTrue("For argument errors, return false", false);
		}
	}

	@Test
	public void returnFalseIfMovieIDIsNull() {

		try {

			boolean canWatch = parentalControlService.canWatchMovie(ParentalControl.LEVEL_PG.getLevel(), null);
			assertTrue("For argument errors, return false", canWatch == false);
		} catch (Exception e) {
			assertTrue("For argument errors, return false", false);
		}
	}

	@Test
	public void returnFalseIfMovieIDIsBlank() {

		try {

			boolean canWatch = parentalControlService.canWatchMovie(null, "");
			assertTrue("For argument errors, return false", canWatch == false);
		} catch (Exception e) {
			assertTrue("For argument errors, return false", false);
		}
	}

	@Test
	public void returnFalseIfParentcontrolLevelIsNull() {

		try {

			boolean canWatch = parentalControlService.canWatchMovie(null, MOVIE_TITLE);
			assertTrue("For argument errors, return false", canWatch == false);
		} catch (Exception e) {
			assertTrue("For argument errors, return false", false);
		}
	}

	@Test
	public void returnFalseIfParentcontrolLevelIsBlank() {

		try {

			boolean canWatch = parentalControlService.canWatchMovie("", MOVIE_TITLE);
			assertTrue("For argument errors, return false", canWatch == false);
		} catch (Exception e) {
			assertTrue("For argument errors, return false", false);
		}
	}


	@Test
	public void returnFalseIfThirdPartySendsWrongControlLevel() {

		try {
			Mockito.doReturn("22").when(movieService).getParentalControlLevel(MOVIE_TITLE);


			boolean canWatch = parentalControlService.canWatchMovie(ParentalControl.LEVEL_PG.getLevel(), MOVIE_TITLE);
			assertTrue("For technical errors, return false", canWatch == false);
		} catch (Exception e) {
			assertTrue("For technical errors, return false", false);
		}
	}


	private void verifyUserCanWatch(ParentalControl userLevel, List<ParentalControl> allowedRating) throws Exception {
		for(ParentalControl rating : allowedRating) {
			Mockito.doReturn(rating.getLevel()).when(movieService).getParentalControlLevel(MOVIE_TITLE);

			assertTrue(parentalControlService.canWatchMovie(userLevel.getLevel(), MOVIE_TITLE));
		}
	}

	private void verifyUserCannotWatch(ParentalControl userLevel, List<ParentalControl> allowedRating) throws Exception {
		for(ParentalControl rating : allowedRating) {
			Mockito.doReturn(rating.getLevel()).when(movieService).getParentalControlLevel(MOVIE_TITLE);

			assertTrue(!parentalControlService.canWatchMovie(userLevel.getLevel(), MOVIE_TITLE));
		}
	}

}
