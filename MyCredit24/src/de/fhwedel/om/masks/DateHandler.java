package de.fhwedel.om.masks;

import java.util.Date;

import com.google.gwt.i18n.shared.DateTimeFormat;

public class DateHandler {
	private DateTimeFormat format;
	public DateHandler (String format) {
		this.format = DateTimeFormat.getFormat(format);
	}
	
	public Date parseStringToDate(String dateString) {
		return this.format.parse(dateString);
	}
}
