package com.supinfo.miniprojet.supreader.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import android.net.Uri;

public class RSSItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private Uri link;
	private Date publicationDate;
	private List<String> categories;
	
	public RSSItem(){};
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Uri getLink() {
		return link;
	}
	public void setLink(Uri link) {
		this.link = link;
	}
	public Date getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	
	
	
	
}
