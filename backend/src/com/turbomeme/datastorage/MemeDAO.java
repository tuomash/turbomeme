package com.turbomeme.datastorage;

import com.turbomeme.datamodel.Meme;
import org.apache.commons.lang3.RandomStringUtils;
import org.javalite.activejdbc.DBException;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

public final class MemeDAO
{
	private static final int MAX_TRIES;
	private static final String PG_DUPLICATE_KEY_ERROR;

	static
	{
		MAX_TRIES = 5;
		PG_DUPLICATE_KEY_ERROR = "duplicate key value violates unique constraint";
	}

	public static Meme find(final String hash)
	{
		Connection.open();
		final Meme meme = Meme.findFirst("hash = ?", hash);
		Connection.close();
		return meme;
	}

	public static Meme insert(final String data, final Integer canvasWidth,
														final Integer canvasHeight, final Integer memeId)
	{
		Connection.open();
		final Meme meme = new Meme();
		final int dataStorageId = 1;
		meme.set("datastorage_id", dataStorageId);
		meme.set("hash", dataStorageId + RandomStringUtils.randomAlphanumeric(7));
		meme.set("data", data);
		meme.set("created", new Timestamp(Calendar.getInstance(
				TimeZone.getTimeZone("Europe/Helsinki")).getTime().getTime()));
		meme.set("canvas_width", canvasWidth);
		meme.set("canvas_height", canvasHeight);
		meme.set("meme_id", memeId);
		insert(meme, dataStorageId);
		Connection.close();
		return meme;
	}

	private static Meme insert(final Meme meme, final int dataStorageId)
	{
		int tries = 0;
		boolean success = false;

		while (tries < MAX_TRIES)
		{
			try
			{
				meme.saveIt();
				success = true;
				break;
			}
			catch (final DBException e)
			{
				// Duplicate hash, try again
				if (e.getMessage().contains(PG_DUPLICATE_KEY_ERROR))
				{
					meme.set("hash", dataStorageId +
							RandomStringUtils.randomAlphanumeric(7));
					tries++;
				}
				else
				{
					throw e;
				}
			}
		}

		if (!success)
		{
			throw new IllegalStateException("Couldn't insert even after " + MAX_TRIES
					+ " tries! [hash=" + meme.get("hash") + ", data=" + meme.get("data")
					+ "]");
		}

		return meme;
	}
}