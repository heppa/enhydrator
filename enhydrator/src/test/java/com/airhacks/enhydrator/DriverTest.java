package com.airhacks.enhydrator;

import com.airhacks.enhydrator.in.Entry;
import com.airhacks.enhydrator.in.Source;
import com.airhacks.enhydrator.out.SystemOutSink;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.PostConstruct;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verify;

/**
 *
 * @author airhacks.com
 */
public class DriverTest {

    Source source;

    @Before
    public void initialize() {
        this.source = new Source.Configuration().
                driver("org.apache.derby.jdbc.EmbeddedDriver").
                url("jdbc:derby:./coffees;create=true").
                newSource();
    }

    @Test
    public void oneToOneTransformationWithName() {
        CoffeeTestFixture.insertCoffee("arabica", 2, "hawai", Roast.LIGHT, "nice", "whole");
        CoffeeTestFixture.insertCoffee("niceone", 3, "russia", Roast.MEDIUM, "awful", "java beans");
        Consumer<List<Entry>> consumer = mock(Consumer.class);
        new Driver.Drive().
                from(source).
                with("name", t -> t.asList()).
                to(consumer).
                go("select * from Coffee");
        verify(consumer, times(2)).accept(any(List.class));
    }

    @Test
    public void oneToOneTransformationWithIndex() {
        CoffeeTestFixture.insertCoffee("arabica", 2, "hawai", Roast.LIGHT, "nice", "whole");
        CoffeeTestFixture.insertCoffee("niceone", 3, "russia", Roast.MEDIUM, "awful", "java beans");
        Consumer<List<Entry>> consumer = mock(Consumer.class);
        new Driver.Drive().
                from(source).
                with(1, t -> t.changeValue("duke").asList()).
                to(consumer).
                go("select * from Coffee");
        verify(consumer, times(2)).accept(any(List.class));
    }

    @Test
    public void ignoringPreprocessor() {
        CoffeeTestFixture.insertCoffee("arabica", 2, "hawai", Roast.LIGHT, "nice", "whole");
        CoffeeTestFixture.insertCoffee("niceone", 3, "russia", Roast.MEDIUM, "awful", "java beans");
        Consumer<List<Entry>> consumer = mock(Consumer.class);
        final ArrayList<Entry> entries = new ArrayList<>();
        new Driver.Drive().
                from(source).
                startWith(l -> entries).
                to(consumer).
                go("select * from Coffee");
        verify(consumer, times(2)).accept(entries);
    }

    @Test
    public void postPreprocessor() {
        CoffeeTestFixture.insertCoffee("arabica", 2, "hawai", Roast.LIGHT, "nice", "whole");
        CoffeeTestFixture.insertCoffee("niceone", 3, "russia", Roast.MEDIUM, "awful", "java beans");
        Consumer<List<Entry>> consumer = mock(Consumer.class);
        final ArrayList<Entry> entries = new ArrayList<>();
        new Driver.Drive().
                from(source).
                endWith(l -> entries).
                to(consumer).
                go("select * from Coffee");
        verify(consumer, times(2)).accept(entries);
    }

    @Test
    public void passThrough() {
        CoffeeTestFixture.insertCoffee("arabica", 2, "hawai", Roast.LIGHT, "nice", "whole");
        CoffeeTestFixture.insertCoffee("niceone", 3, "russia", Roast.MEDIUM, "awful", "java beans");
        Consumer<List<Entry>> consumer = mock(Consumer.class);
        new Driver.Drive().
                from(source).
                to(consumer).
                go("select * from Coffee");
        verify(consumer, times(2)).accept(any(List.class));
    }

    @After
    public void clearTables() {
        CoffeeTestFixture.deleteTable();
    }

}