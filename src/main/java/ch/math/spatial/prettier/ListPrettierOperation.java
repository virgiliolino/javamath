package ch.math.spatial.prettier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ListPrettierOperation<P> implements Callable<List<String>> {
    private String prettyFormat;
    private List<P> objects;

    public ListPrettierOperation(String prettyFormat, List<P> objects) {
        this.prettyFormat = prettyFormat;
        this.objects = objects;
    }

    public List<String> call() {
        int index = 1;
        List<String> result = new ArrayList<>();
        for (Object object : objects) {
            result.add(String.format(prettyFormat, index, object));
            index++;
        }
        return result;

    }

}
