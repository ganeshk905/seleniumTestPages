package com.seleniumsimplified.pulp.domain;

import com.seleniumsimplified.pulp.PulpApp;
import com.seleniumsimplified.pulp.PulpData;
import com.seleniumsimplified.pulp.reader.PulpDataPopulator;
import com.seleniumsimplified.pulp.reader.SavageReader;
import com.seleniumsimplified.pulp.reporting.filtering.BookFilter;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AppEndPointTest {

    @Test
    public void canGetBooksByAuthor(){

        PulpData books = new PulpData();
        PulpDataPopulator populator = new PulpDataPopulator(books);
        SavageReader reader = new SavageReader("/data/pulp/doc_savage_test.csv");
        populator.populateFrom(reader);

        PulpAuthor will = books.authors().findByName("Will Murray");

        List<PulpBook> authored = books.books().findByAuthorId(will.getId());

        Assert.assertEquals(1, authored.size());

        PulpAuthor lester = books.authors().findByName("Lester Dent");
        authored = books.books().findByAuthorId(lester.getId());
        Assert.assertEquals(5, authored.size());
    }

    @Test
    public void haveBasicAppWrapperForBooksByAuthor(){
        PulpApp app = new PulpApp();
        app.readSavageData("/data/pulp/doc_savage_test.csv");

        PulpAuthor will = app.books().authors().findByName("Will Murray");

        String report = app.reports().getBooksAsHtmlList(new BookFilter().where().author(will.getId()));

        System.out.println(report);
        Assert.assertTrue(report.contains("<li>The Angry Canary"));

        PulpAuthor lester = app.books().authors().findByName("Lester Dent");

        report = app.reports().getBooksAsHtmlList(new BookFilter().where().author(lester.getId()));

        System.out.println(report);
        Assert.assertTrue(report.contains("<li>The Angry Canary"));
        Assert.assertTrue(report.contains("<li>Up From Earth's Center"));
    }


    @Test
    public void haveBasicAppWrapperForBooksByPublisher(){
        PulpApp app = new PulpApp();
        app.readSavageData("/data/pulp/doc_savage_test.csv");

        PulpPublisher pub = app.books().publishers().findByName("Street And Smith");

        String report = app.reports().getBooksAsHtmlList(new BookFilter().where().publishedBy(pub.getId()));

        System.out.println(report);
        Assert.assertTrue(report.contains("<li>The Angry Canary"));

    }

    @Test
    public void haveBasicAppWrapperForBooksBySeries(){
        PulpApp app = new PulpApp();
        app.readData( new SavageReader("/data/pulp/doc_savage_test.csv"));

        PulpSeries series = app.books().series().findByName("Doc Savage");

        String report = app.reports().getBooksAsHtmlList(new BookFilter().where().partOfSeries(series.getId()));

        System.out.println(report);
        Assert.assertTrue(report.contains("<li>The Angry Canary"));

    }
}
