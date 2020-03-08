package de.ntbit.projectearlybird.manager;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Test {
    private static List<ParseObject> allObjects = new ArrayList<ParseObject>();

    public void test() {
        /*
        final ParseQuery parseQuery = new ParseQuery("Message");
        parseQuery.setLimit(1000);
        parseQuery.findInBackground(getAllObjects());

        int skip = 0;
        FindCallback getAllObjects(){
            return new FindCallback() {
                public void done(List objects, ParseException e) {
                    if (e == null) {
                        List<ParseObject> allObjects = null;
                        allObjects.addAll(objects);
                        int limit = 1000;
                        if (objects.size() == limit) {
                            skip = skip + limit;
                            ParseQuery query = new ParseQuery("Message");
                            query.setSkip(skip);
                            query.setLimit(limit);
                            query.findInBackground(getAllObjects());
                        }
                        //We have a full PokeDex
                        else {
                            //USE FULL DATA AS INTENDED
                        }
                    }
                }
            };
        }

         */
    }
}
