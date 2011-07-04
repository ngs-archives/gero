package org.ngsdev.android.sample20.test.model;

import java.sql.Timestamp;
import java.util.Date;

import org.ngsdev.android.model.ManagedObject;

public abstract class ManagedTicket extends ManagedObject {

	public String name;
	public String address;
	public Timestamp endDate;
	public Timestamp beginDate;
	
	public ManagedTicket() {
	}

	public Date getEndDate() {
		return new Date(endDate.getTime());
	}

	public void setEndDate(Date time) {
		this.endDate = new Timestamp(time.getTime());
	}

	public Date getBeginDate() {
		return new Date(beginDate.getTime());
	}

	public void setBeginDate(Date time) {
		this.endDate = new Timestamp(time.getTime());
	}
	
}
