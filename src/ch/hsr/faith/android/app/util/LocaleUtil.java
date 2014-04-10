package ch.hsr.faith.android.app.util;

import java.util.Locale;

import ch.hsr.faith.domain.FAITHLocale;

public class LocaleUtil {

	public static FAITHLocale getCurrentLocale() {
		String locale = Locale.getDefault().getLanguage();
		for (FAITHLocale faithLocale : FAITHLocale.values()) {
			if (faithLocale.getCode().equals(locale.toLowerCase())) {
				return faithLocale;
			}
		}
		return FAITHLocale.ENGLISH;
	}

}
