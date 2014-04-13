package com.airhacks.enhydrator.transform;

import com.airhacks.enhydrator.in.Entry;
import java.sql.SQLType;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class FunctionScriptLoaderTest {

    FunctionScriptLoader cut;

    @Before
    public void init() {
        this.cut = new FunctionScriptLoader();
    }

    @Test
    public void scriptLoadingWorks() throws Exception {
        Entry entry = new Entry(0, "chief", 42, "duke");
        Callable<List<Entry>> function = this.cut.getEntryTransformer(entry);
        assertNotNull(function);
        List<Entry> transformedEntries = function.call();
        assertThat(transformedEntries, hasItem(entry));

    }

}