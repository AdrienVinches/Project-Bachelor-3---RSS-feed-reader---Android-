package com.supinfo.miniprojet.supreader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.supinfo.miniprojet.rss.R;
import com.supinfo.miniprojet.supreader.beans.RSSFeed;
import com.supinfo.miniprojet.supreader.sql.SqlLite;

public class SupReader extends Activity {

	private static final int RSS_FEED = 1;
	private SqlLite sqlite;
	private SQLiteDatabase sqlDb;
	String feedName;
	List<RSSFeed> rssfeedNames = new ArrayList<RSSFeed>();
	String urlName;
	String[] columns = { "id", "name", "link" };
	String isVerif;
	String currantOnglet;
	String streamTitle = "";
	String rssLink;
	String[] ilestla;
	String vasyjeanmi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		onglets();

		sqlite = new SqlLite(this);
		sqlDb = sqlite.getWritableDatabase();

	}

	public void rssFeedParser(String rssLink) {
		 TextView result = (TextView) findViewById(R.id.result);
		// Toast.makeText(this, rssLink, Toast.LENGTH_SHORT).show();
		try {
			URL rssUrl = new URL(rssLink);
			SAXParserFactory mySAXParserFactory = SAXParserFactory
					.newInstance();
			SAXParser mySAXParser = mySAXParserFactory.newSAXParser();
			XMLReader myXMLReader = mySAXParser.getXMLReader();
			RSSHandler myRSSHandler = new RSSHandler();
			myXMLReader.setContentHandler(myRSSHandler);
			InputSource myInputSource = new InputSource(rssUrl.openStream());
			myXMLReader.parse(myInputSource);
			result.setText(streamTitle);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setText("Cannot connect RSS!");
			Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setText("Cannot connect RSS!");
			Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setText("Cannot connect RSS!");
			Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setText("Cannot connect RSS!");
			Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_feed:
			showDialog(RSS_FEED);

			return true;
		case R.id.delete_feed:
			deleteFeed();
			return true;
		case R.id.quit:
			finish();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void deleteFeed() {
		sqlite = new SqlLite(this);
		sqlDb = sqlite.getWritableDatabase();
		String[] whereArgs = { currantOnglet };
		sqlDb.delete("FEEDS", "name=?", whereArgs);
		onCreate(null);
		Toast.makeText(this, "Feed remove", Toast.LENGTH_SHORT).show();
	}

	public void addOnglet(TabHost onglet, RSSFeed rssFeed, final String rssLink) {
		TabSpec tspec = onglet.newTabSpec(rssFeed.getName());

		tspec.setContent(new TabHost.TabContentFactory() {

			public View createTabContent(String tag) {
				TextView contenu = new TextView(SupReader.this);
				contenu.setText("");
				rssFeedParser(rssLink);
				return (contenu);
			}
		});

		tspec.setIndicator(rssFeed.getName(),
				getResources().getDrawable(R.drawable.rss));
		onglet.addTab(tspec);
		onglet.setCurrentTab(0);

	}
	
	public void vasyletoast(String linkRss){
		
		Toast.makeText(this, linkRss, Toast.LENGTH_SHORT).show();
		rssFeedParser(linkRss);
	}

	public void onglets() {
		sqlite = new SqlLite(this);
		sqlDb = sqlite.getReadableDatabase();
		Cursor resultFeedName = sqlDb.query("FEEDS", columns, null, null, null,
				null, null);
		TabHost onglet = (TabHost) this.findViewById(R.id.tabhost);

		onglet.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				currantOnglet = tabId;
				sqlite = new SqlLite(SupReader.this);
				sqlDb = sqlite.getWritableDatabase();
				Cursor coucou = sqlDb.query("FEEDS", new String[] {"link"}, 
		                "name like '" + currantOnglet+"'", null, null, null, null);
				
				coucou.moveToFirst();
				while (!coucou.isAfterLast()) {
					RSSFeed coucouFeed = new RSSFeed();
					coucouFeed.setLink(coucou.getString(0));
					vasyjeanmi = coucouFeed.getLink();

					coucou.moveToNext();

					vasyletoast(vasyjeanmi);

				}
				coucou.close();
				
			}
		});
		onglet.setup();

		resultFeedName.moveToFirst();
		while (!resultFeedName.isAfterLast()) {
			RSSFeed rssFeed = new RSSFeed();
			rssFeed.setId(resultFeedName.getLong(0));
			rssFeed.setName(resultFeedName.getString(1));
			rssFeed.setLink(resultFeedName.getString(2));
			rssLink = rssFeed.getLink();
			rssfeedNames.add(rssFeed);

			resultFeedName.moveToNext();

			addOnglet(onglet, rssFeed, rssLink);

		}
		resultFeedName.close();

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case RSS_FEED:
			dialog = createNewRssFeed();
			break;

		default:
			dialog = null;
		}
		return dialog;
	}

	private Dialog createNewRssFeed() {

		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialogrss);
		dialog.setTitle("Add new Feed");
		final EditText name = (EditText) dialog
				.findViewById(R.id.dialogRss_name);
		final EditText url = (EditText) dialog.findViewById(R.id.dialogRss_url);
		Button cancelButton = (Button) dialog
				.findViewById(R.id.dialogRss_cancel_button);
		Button okButton = (Button) dialog
				.findViewById(R.id.dialogRss_submit_button);

		url.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (url.getText().toString().equalsIgnoreCase("")) {
					url.setText("http://");
				}
				return false;
			}
		});

		name.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (url.getText().toString().equalsIgnoreCase("http://")) {
					url.setText("");
				}
				return false;
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.hide();
				name.setText("");
				url.setText("");
			}
		});

		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				feedName = name.getText().toString();
				urlName = url.getText().toString();
				Boolean verif = true;

				for (RSSFeed rssfeed : rssfeedNames) {

					if (rssfeed.getName().contentEquals(feedName)) {
						verif = false;
					}
				}
				if (verif) {
					ContentValues values = new ContentValues();
					values.put("name", feedName);
					values.put("link", urlName);
					sqlDb.insert("FEEDS", null, values);
					dialog.hide();
					name.setText("");
					url.setText("");
					onCreate(null);
					isVerif = "Feed Add";
					verif();
				} else {
					isVerif = "sorry but the name already exist";
					verif();
				}
			}
		});

		return dialog;
	}

	public void verif() {
		Toast.makeText(this, isVerif, Toast.LENGTH_SHORT).show();
	}

	private class RSSHandler extends DefaultHandler {
		final int stateUnknown = 0;
		final int stateTitle = 1;

		int state = stateUnknown;

		int numberOfTitle = 0;
		String strTitle = "";
		String strElement = "";

		String split = "<";
		String record[];
		String coucou;

		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			// strTitle = "--- Start Document ---\n";
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			// strTitle += "--- End Document ---";
			streamTitle = "Number Of Feeds: " + String.valueOf(numberOfTitle)
					+ "\n" + strTitle;
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			state = stateTitle;
			if (localName.equalsIgnoreCase("title")) {
				//state = stateTitle;
				strElement = "Title: ";
				numberOfTitle++;
			}
			if (localName.equalsIgnoreCase("description")) {
				//state = stateTitle;
				strElement = "Description: ";
			}
			if (localName.equalsIgnoreCase("pubdate")) {
				//state = stateTitle;
				strElement = "";
			}
			if (localName.equalsIgnoreCase("category")) {
				//state = stateTitle;
				strElement = "Catégorie: ";
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {

			// TODO Auto-generated method stub
			if (localName.equalsIgnoreCase("title")) {

				strTitle += "--------------------------------------------------------------- \n "
						+ strElement + "\n \n";
				// strTitle += strdescription + "\n";
				//strElement = "";
			}
			if (localName.equalsIgnoreCase("description")) {

				record = strElement.split(split);
				coucou = record[0];
				strTitle += coucou + " \n \n";
				//strElement = "";
			}
			if (localName.equalsIgnoreCase("pubdate")) {

				record = strElement.split("G");
				coucou = record[0];
				strTitle += coucou + " \n \n";
				//strElement = "";
			}
			if (localName.equalsIgnoreCase("category")) {

				strTitle += strElement + "\n \n";
				// strTitle += strdescription + "\n";
				//strElement = "";
			}
			else {
				strTitle += "";
				//strElement = "";
			}
			strElement ="";
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
			String strCharacters = new String(ch, start, length);
			if (state == stateTitle) {
				strElement += strCharacters;
			}
		}

	}

}