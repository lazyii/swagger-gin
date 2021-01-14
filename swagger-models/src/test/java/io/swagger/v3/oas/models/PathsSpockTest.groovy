package io.swagger.v3.oas.models

import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger
/**
 * Created by admin on 2021/1/13 10:43:11.
 */
class PathsSpockTest extends Specification {

    @Shared increr  = new AtomicInteger();

     def "testValue"() {
        given:
        def paths = new Paths()

        expect:

        paths.addPathItem("foo", null) == paths
    }

    def "testEquals"() {
        given:
        def paths = new Paths()

        expect:

        paths == paths
        paths == new Paths()
        paths != null
        paths != new String()
    }

    def "testGetExtensions1"() {
        given:
        def paths = new Paths();
        paths.addExtension("", null);
        paths.addExtension("y-", null);
        paths.addExtension(null, null);

        expect:

        Objects.isNull(paths.getExtensions())
    }

    def "testGetExtensions2"() {
        given:
        Paths paths = new Paths();
        paths.addExtension("x-", "foo");
        paths.addExtension("x-", "bar");
        paths.addExtension("x-", "baz");

        expect:

        paths.getExtensions() ==
                new HashMap<String, Object>() {
                    {
                        put("x-", "baz");
                    }
                }
    }

    def "testGetExtensions3"() {
        given:
        Paths paths = new Paths();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("x-", "foo");
        hashMap.put("x-", "bar");
        hashMap.put("x-", "baz");
        paths.setExtensions(hashMap);

        expect:

        paths.getExtensions() ==
                new HashMap<String, Object>() {
                    {
                        put("x-", "baz");
                    }
                }
    }

    def "testExtensions"() {
        given:
        Paths paths = new Paths();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("x-", "foo");
        hashMap.put("x-", "bar");
        hashMap.put("x-", "baz");

        expect:

        paths.extensions(hashMap) == paths
    }

    def "testToString"() {
        given:
        Paths paths = new Paths();
        paths.addPathItem("foo", null);

        expect:

        paths.toString() == "class Paths {\n    {foo=null}\n}"
    }
}
